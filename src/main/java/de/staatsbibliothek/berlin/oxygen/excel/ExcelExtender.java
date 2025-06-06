package de.staatsbibliothek.berlin.oxygen.excel;

import static java.lang.Math.max;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Provide functionality to convert CSV file into formatted excel
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 15.11.19
 */
public class ExcelExtender {

  private static final Logger logger = Logger.getLogger(ExcelExtender.class.getName());

  public static final Pattern PATTERN_CSV_LINE_SPLIT = Pattern.compile("(?<=\")(;)(?=\")");
  public static final Pattern PATTERN_END_CSV_LINE = Pattern.compile("(?<=\")\\v");
  public static final String DEFAULT_SHEET_NAME = "sheet1";
  public static final int ROW_HEADER = 1;

  private static final Pattern LINE_SEPARATOR = Pattern.compile("\r\n|\r|\n");
  public static final int MAX_COLUMN_LENGTH = 48000;
  public static final char CARET_NEW_LINE = '\n';
  public static final int LINE_CHARS_COUNT = 80;
  private static final Pattern ALLOWED_CHARACTERS_FILENAME = Pattern.compile("[^a-zA-Z0-9\\.\\-]");
  private static final Pattern DOUBLE_DOUBLE_QUOTE = Pattern.compile("\"\"+");

  public static String convertCSV2Excel(ExcelExtenderParams params) {

    String lockedKeysStr = decodeXMLEncoded(params.getLockedKeysStr());
    String dropDownListStr = decodeXMLEncoded(params.getDropDownListStr());
    String datatypeListStr = decodeXMLEncoded(params.getDatatypeListStr());
    String csvFilePath = params.getCsvFilePath();
    String sheetName = params.getSheetName();
    String password = params.getPassword();
    String excelFilePath = params.getExcelFilePath();

    StringBuilder allParameters = new StringBuilder(2048).append("\nParameters csvFile='").append(csvFilePath)
        .append("',").append(LINE_SEPARATOR).append("excelFilePath='").append(excelFilePath).append("',\n password='")
        .append(password).append("',").append(LINE_SEPARATOR).append(" lockedKeysStr='").append(lockedKeysStr)
        .append("', \ndropDownListStr='").append(dropDownListStr).append("', ").append(LINE_SEPARATOR)
        .append("sheetName='").append(sheetName).append("', \ndatatypeListStr='").append(datatypeListStr)
        .append('\'').append(LINE_SEPARATOR);
    try {
      Set<String> lockedKeys = getLockedKeys(lockedKeysStr);

      Map<String, String[]> dropDownListMap = getDropDownListMap(dropDownListStr);

      Map<String, CellType> dataTypeListMap = new HashMap<>();
      ExcelCommon.parseDataTypeList(datatypeListStr, dataTypeListMap);

      allParameters.append(
          convertCSV2Excel(csvFilePath, excelFilePath, password, lockedKeys, dropDownListMap, dataTypeListMap,
              sheetName));
    } catch (Exception ex) {
      String exceptionStackTrace = ExcelCommon.getExceptionStacktraceAsString(ex);
      logger.warning(exceptionStackTrace);
      allParameters.append("\nFEHLER: ").append(ex.getMessage()).append(allParameters)
          .append(exceptionStackTrace);
    }
    return allParameters.toString();
  }

  private static Set<String> getLockedKeys(String lockedKeysStr) {
    Set<String> lockedKeys = new HashSet<>();
    if (!lockedKeysStr.isEmpty()) {
      String[] lockedKeyValues = lockedKeysStr.split(";");
      Collections.addAll(lockedKeys, lockedKeyValues);
    }
    return lockedKeys;
  }


  public static String convertCSV2Excel(String csvFilePath, String excelFilePath, String password,
      Set<String> lockedKeys, Map<String, String[]> dropDownListMap, Map<String, CellType> dataTypeListMap,
      String sheetName) throws IOException {
    StringBuilder log = new StringBuilder(1024);
    log.append("\nPrepare to parse CSV file ").append(csvFilePath).append(" into ").append(excelFilePath).append(
        CARET_NEW_LINE);

    Workbook workBook = new XSSFWorkbook();

    CellStyle unLockedTextStyle = workBook.createCellStyle();
    unLockedTextStyle.setLocked(false);

    Font calibra11Bold = createBoldFontForHeader(workBook);

    CellStyle headerCellTextStyle = creatrHeaderCellStyle(workBook, calibra11Bold);

    Sheet sheet = createSheetForWorkBook(sheetName, workBook);

    try (Scanner scanner = new Scanner(Files
        .newBufferedReader(ExcelCommon.resolvePathFromString(csvFilePath), StandardCharsets.UTF_8))) {
      scanner.useDelimiter(PATTERN_END_CSV_LINE);
      Map<String, String> headerKeysPosition = new HashMap<>();
      int currentRowNumber = 0;
      int columnsCount = 0;
      while (scanner.hasNext()) {
        String line = scanner.next();
        Row currentRow = sheet.createRow(currentRowNumber);
        currentRowNumber++;

        String[] lineCells = PATTERN_CSV_LINE_SPLIT.split(line);
        columnsCount = lineCells.length;
        processCSVLine(lockedKeys, dataTypeListMap, columnsCount, currentRow, lineCells, currentRowNumber,
            headerKeysPosition,
            headerCellTextStyle, unLockedTextStyle);
      }
      log.append(LINE_SEPARATOR).append("---------").append(LINE_SEPARATOR);

      autoSizeSheetColumns(sheet, columnsCount, null);
      createSpecialColumns(dropDownListMap, headerKeysPosition, sheet, currentRowNumber);

      freezeHeaderRow(sheet);

      if (password != null && !password.isEmpty()) {
        sheet.protectSheet(password);
      }

    }

    storeExceltToDiskAndClose(excelFilePath, workBook);
    log.append(LINE_SEPARATOR).append(excelFilePath).append(" Stored");
    return log.toString();
  }

  static void freezeHeaderRow(Sheet sheet) {
    sheet.createFreezePane(0, 1);
  }

  static String removeValueQuotation(String line) {
    if (line != null && line.length() > 1) {
      int startIndexShift = getStartIndexAfterQuotation(line);
      int endIndexShift = getEndIndexBeforeQuotation(line);

      if (startIndexShift > 0 || endIndexShift > 0) {
        line = line.substring(startIndexShift, line.length() - endIndexShift);
      }
    }
    return line;
  }

  static int getEndIndexBeforeQuotation(String line) {
    int endIndexShift = 0;
    if (line.endsWith("\"")) {
      endIndexShift = 1;
    } else if (line.endsWith("\"\n")) {
      endIndexShift = 2;
    }
    return endIndexShift;
  }

  static int getStartIndexAfterQuotation(String line) {
    int startIndexShift = 0;
    if (line.startsWith("\"")) {
      startIndexShift = 1;
    } else if (line.length() > 2 && (line.startsWith("\uFEFF\"") || line.startsWith("\uFFFE\""))) {
      startIndexShift = 2;
    }
    return startIndexShift;
  }


  public static String convertCSV2ExcelPerEntry(String csvFilePath, String excelDirPath, String password,
      String lockedKeysStr, String dropDownListStr, String datatypeListStr, boolean asKeyValue, int keyWidth,
      int valueWidth) {

    lockedKeysStr = decodeXMLEncoded(lockedKeysStr);
    dropDownListStr = decodeXMLEncoded(dropDownListStr);
    datatypeListStr = decodeXMLEncoded(datatypeListStr);

    StringBuilder allParameters = new StringBuilder(2048).append("\nParameters csvFile='").append(csvFilePath)
        .append("',").append(LINE_SEPARATOR).append("excelDirPath='").append(excelDirPath).append("',\n password='")
        .append(password).append("',").append(LINE_SEPARATOR).append(" lockedKeysStr='").append(lockedKeysStr)
        .append("', \ndropDownListStr='").append(dropDownListStr).append("', ").append(LINE_SEPARATOR)
        .append("asKeyValue='").append(asKeyValue).append("', \ndatatypeListStr='").append(datatypeListStr)
        .append('\'').append(LINE_SEPARATOR).append("keyWidth='").append(keyWidth).append(LINE_SEPARATOR)
        .append("valueWidth='").append(valueWidth);
    try {
      Set<String> lockedKeys = getLockedKeys(lockedKeysStr);

      Map<String, String[]> dropDownListMap = getDropDownListMap(dropDownListStr);

      Map<String, CellType> dataTypeListMap = new HashMap<>();
      ExcelCommon.parseDataTypeList(datatypeListStr, dataTypeListMap);

      String conversionResult = convertCSV2ExcelPerEntry(csvFilePath, excelDirPath, password, lockedKeys,
          dropDownListMap, dataTypeListMap, asKeyValue, keyWidth, valueWidth);
      allParameters.append(conversionResult);
    } catch (Exception ex) {
      String exceptionStackTrace = ExcelCommon.getExceptionStacktraceAsString(ex);
      logger.info(exceptionStackTrace);
      allParameters.append("FEHLER: ").append(ex.getMessage()).append(allParameters).append(exceptionStackTrace);
    }
    return allParameters.toString();
  }

  private static Map<String, String[]> getDropDownListMap(String dropDownListStr) {
    Map<String, String[]> dropDownListMap = new HashMap<>();
    ExcelCommon.parseDropDownList(dropDownListStr, dropDownListMap);
    return dropDownListMap;
  }

  public static String convertCSV2ExcelPerEntry(String csvFilePath, String excelDirPath, String password,
      Set<String> lockedKeys, Map<String, String[]> dropDownListMap, Map<String, CellType> dataTypeListMap,
      boolean asKeyValue, int keyWidth, int valueWidth) throws IOException {
    StringBuilder log = new StringBuilder(1024);
    log.append("\nPrepare to parse CSV file ").append(csvFilePath).append(" into ").append(excelDirPath).append(
        CARET_NEW_LINE);

    try (Scanner scanner = new Scanner(Files
        .newBufferedReader(ExcelCommon.resolvePathFromString(csvFilePath), StandardCharsets.UTF_8))) {
      scanner.useDelimiter(PATTERN_END_CSV_LINE);
      Map<String, String> headerKeysPosition = new HashMap<>();
      int currentRowNumber = 0;
      int columnsCount = 0;
      String[] firstRow = null;
      boolean first = true;
      Map<String, Integer> fileNames = new HashMap<>();
      while (scanner.hasNext()) {
        String line = scanner.next();
        if (first) {
          firstRow = PATTERN_CSV_LINE_SPLIT.split(line);
          columnsCount = firstRow.length;
          first = false;
        } else {
          Workbook workBook = new XSSFWorkbook();
          CellStyle unLockedTextStyle = createUnlockedCellStyle(workBook);

          Font calibra11Bold = createBoldFontForHeader(workBook);

          CellStyle headerCellTextStyle = creatrHeaderCellStyle(workBook, calibra11Bold);
          headerCellTextStyle.setVerticalAlignment(VerticalAlignment.TOP);

          CellStyle headerRowCellTextStyle = createHeaderRowCellStyle(workBook, calibra11Bold);

          String[] lineCells = PATTERN_CSV_LINE_SPLIT.split(line);
          String excelFileName = prepareUniqueFilename(lineCells, fileNames);
          Sheet sheet = createSheetForWorkBook(lineCells[1], workBook);
          if (asKeyValue) {
            createSheetEntryAsKeyValue(lockedKeys, dataTypeListMap, sheet, headerKeysPosition, headerCellTextStyle,
                unLockedTextStyle, columnsCount, lineCells, firstRow, headerRowCellTextStyle, keyWidth, valueWidth);
          } else {
            createSheetEntryPerLine(lockedKeys, dataTypeListMap, sheet, columnsCount, firstRow, headerKeysPosition,
                headerCellTextStyle, unLockedTextStyle, lineCells);
          }

          createSpecialColumns(dropDownListMap, headerKeysPosition, sheet, currentRowNumber);
          if (password != null && !password.isEmpty()) {
            sheet.protectSheet(password);
          }
          storeExceltToDiskAndClose(MessageFormat.format("{0}/{1}.xlsx", excelDirPath, excelFileName), workBook);
          log.append(LINE_SEPARATOR).append(excelDirPath).append(" Stored");
        }
      }
      log.append(LINE_SEPARATOR).append("---------").append(LINE_SEPARATOR);

    }

    return log.toString();
  }

  private static String prepareUniqueFilename(String[] lineCells, Map<String, Integer> fileNames) {
    String excelFileName = ALLOWED_CHARACTERS_FILENAME.matcher(lineCells[1].trim())
        .replaceAll(ExcelCommon.EMPTY_STRING);
    if (fileNames.containsKey(excelFileName)) {
      int currentCount = fileNames.get(excelFileName) + 1;
      fileNames.put(excelFileName, currentCount);
      excelFileName = new StringBuilder(excelFileName).append('_').append(currentCount).toString();
    } else {
      fileNames.put(excelFileName, 0);
    }
    return excelFileName;
  }

  private static void createSheetEntryPerLine(Set<String> lockedKeys, Map<String, CellType> dataTypeListMap,
      Sheet sheet, int columnsCount, String[] firstRow, Map<String, String> headerKeysPosition,
      CellStyle headerCellTextStyle, CellStyle unLockedTextStyle, String[] lineCells) {

    Row currentRow = sheet.createRow(0);
    processCSVLine(lockedKeys, dataTypeListMap, columnsCount, currentRow, firstRow, 1,
        headerKeysPosition, headerCellTextStyle, unLockedTextStyle);

    currentRow = sheet.createRow(1);
    processCSVLine(lockedKeys, dataTypeListMap, columnsCount, currentRow, lineCells, 2,
        headerKeysPosition, headerCellTextStyle, unLockedTextStyle);
    autoSizeSheetColumns(sheet, columnsCount, null);
  }

  private static void createSheetEntryAsKeyValue(Set<String> lockedKeys, Map<String, CellType> dataTypeListMap,
      Sheet sheet, Map<String, String> headerKeysPosition, CellStyle headerCellTextStyle, CellStyle unLockedTextStyle,
      int columnsCount, String[] lineCells, String[] firstRow, CellStyle headerRowCellTextStyle, int keyWidth,
      int valueWidth) {
    int[] widths = null;
    if (keyWidth > 0 && valueWidth > 0) {
      widths = new int[]{keyWidth, valueWidth};
    }
    processCSVLineValues(lockedKeys, dataTypeListMap, columnsCount, sheet, lineCells, firstRow,
        headerKeysPosition, headerCellTextStyle, unLockedTextStyle, headerRowCellTextStyle);
    autoSizeSheetColumns(sheet, 2, widths);
  }

  private static CellStyle createHeaderRowCellStyle(Workbook workBook, Font calibra11Bold) {
    CellStyle headerRowCellTextStyle = creatrHeaderCellStyle(workBook, calibra11Bold);
    headerRowCellTextStyle.setVerticalAlignment(VerticalAlignment.TOP);
    headerRowCellTextStyle.setWrapText(true);
    headerRowCellTextStyle.setShrinkToFit(false);
    headerRowCellTextStyle.setAlignment(HorizontalAlignment.CENTER);
    return headerRowCellTextStyle;
  }

  private static CellStyle createUnlockedCellStyle(Workbook workBook) {
    CellStyle unLockedTextStyle = workBook.createCellStyle();
    unLockedTextStyle.setLocked(false);
    unLockedTextStyle.setWrapText(true);
    unLockedTextStyle.setShrinkToFit(false);
    unLockedTextStyle.setVerticalAlignment(VerticalAlignment.TOP);
    return unLockedTextStyle;
  }

  private static void processCSVLineValues(Set<String> lockedKeys, Map<String, CellType> dataTypeListMap,
      int columnsCount, Sheet sheet, String[] lineCells, String[] firstRow, Map<String, String> headerKeysPosition,
      CellStyle headerCellTextStyle, CellStyle unLockedTextStyle, CellStyle headerRowCellTextStyle) {
    for (int i = 0; i < columnsCount; i++) {
      int rowNumber = i + 1;
      Row subRow = sheet.createRow(i);
      String valueString = ExcelCommon.removeHTMLTagsFromCellValue(lineCells[i]);
      processCSVLine(lockedKeys, dataTypeListMap, 2, subRow,
          new String[]{firstRow[i], valueString}, rowNumber,
          headerKeysPosition, headerCellTextStyle, unLockedTextStyle, false);
      subRow.getCell(0).setCellStyle(headerRowCellTextStyle);
    }
  }

  private static void processCSVLine(Set<String> lockedKeys, Map<String, CellType> dataTypeListMap, int columnsCount,
      Row currentRow, String[] lineCells, int currentRowNumber, Map<String, String> headerKeysPosition,
      CellStyle headerCellTextStyle, CellStyle unLockedTextStyle) {
    processCSVLine(lockedKeys, dataTypeListMap, columnsCount, currentRow, lineCells, currentRowNumber,
        headerKeysPosition, headerCellTextStyle, unLockedTextStyle, true);
  }

  private static void processCSVLine(Set<String> lockedKeys, Map<String, CellType> dataTypeListMap, int columnsCount,
      Row currentRow, String[] lineCells, int currentRowNumber, Map<String, String> headerKeysPosition,
      CellStyle headerCellTextStyle, CellStyle unLockedTextStyle, boolean firstRowIsHeader) {

    int lines = 1;

    for (int i = 0; i < columnsCount; i++) {
      Cell currentCell = currentRow.createCell(i, CellType.STRING);

      String cellValue = lineCells[i];
      if (cellValue != null && cellValue.length() > 1) {
        cellValue = removeValueQuotation(cellValue);
        cellValue = DOUBLE_DOUBLE_QUOTE.matcher(cellValue).replaceAll("\"");
        lines = max(
            max(
                lines,
                cellValue.length() / LINE_CHARS_COUNT
            )
            , LINE_SEPARATOR.split(cellValue).length);
      }
      fillColumnData(lockedKeys, dataTypeListMap, i, currentRowNumber, headerKeysPosition, cellValue, currentCell,
          headerCellTextStyle, unLockedTextStyle, firstRowIsHeader);
    }

    ++lines;
    currentRow.setHeight((short) ((int) currentRow.getHeight() * lines));
  }

  private static Sheet createSheetForWorkBook(String sheetName, Workbook workBook) {
    if ((sheetName == null) || sheetName.isEmpty()) {
      sheetName = DEFAULT_SHEET_NAME;
    }
    return workBook.createSheet(sheetName);
  }

  private static void fillColumnData(Set<String> lockedKeys, Map<String, CellType> dataTypeListMap,
      int currentColumnIndex, int currentRowNumber, Map<String, String> headerKeysPosition, String cellValue,
      Cell currentCell, CellStyle headerCellTextStyle, CellStyle unLockedTextStyle, boolean firstRowIsHeader) {

    String currentColumnIndexAsString = String.valueOf(currentColumnIndex);
    if (firstRowIsHeader && currentRowNumber == ROW_HEADER) {
      headerKeysPosition.put(currentColumnIndexAsString, cellValue);
      currentCell.setCellStyle(headerCellTextStyle);
      currentCell.setCellValue(cellValue);
    } else {
      String currentColumnName = headerKeysPosition.get(currentColumnIndexAsString);
      if (!lockedKeys.contains(currentColumnName)) {
        currentCell.setCellStyle(unLockedTextStyle);
      }
      setColumnDataTypeValue(dataTypeListMap, cellValue, currentCell, currentColumnName);
    }
    autoSizeCell(currentCell, cellValue);
  }

  static void autoSizeCell(Cell currentCell, String cellValue) {
    if (cellValue != null && cellValue.length() > 1) {
      currentCell.getRow().getSheet().autoSizeColumn(currentCell.getColumnIndex());
    }
  }

  private static void setColumnDataTypeValue(Map<String, CellType> dataTypeListMap, String cellValue, Cell currentCell,
      String currentColumnName) {
    if (dataTypeListMap.containsKey(currentColumnName)) {
      switch (dataTypeListMap.get(currentColumnName)) {
        case NUMERIC:
          extractNumericValue(currentCell, cellValue);
          break;
        case BOOLEAN:
          currentCell.setCellValue("1".equals(cellValue));
          break;
        case FORMULA:
          currentCell.setCellFormula(cellValue);
          break;
        case ERROR:
          parseErrorValue(currentCell, cellValue);
          break;
        default:
          currentCell.setCellValue(cellValue);
          break;
      }
    } else {
      currentCell.setCellValue(cellValue);
    }
  }

  private static void extractNumericValue(Cell currentCell, String cellValue) {
    try {
      currentCell.setCellValue(Double.parseDouble(cellValue));
    } catch (NumberFormatException nfe) {
      currentCell.setCellValue(cellValue);
    }
  }

  private static void parseErrorValue(Cell currentCell, String cellValue) {
    try {
      currentCell.setCellErrorValue(Byte.parseByte(cellValue));
    } catch (NumberFormatException ignore) {
      currentCell.setCellValue(cellValue);
    }
  }

  private static void storeExceltToDiskAndClose(String excelFilePath, Workbook workBook) throws IOException {
    Path excelPath = ExcelCommon.resolvePathFromString(excelFilePath);
    Files.createDirectories(excelPath.getParent());
    try (OutputStream os = Files.newOutputStream(excelPath)) {
      workBook.write(os);
      os.flush();
    }
    workBook.close();
  }

  private static void autoSizeSheetColumns(Sheet sheet, int columnsCount, int[] widths) {
    for (int i = 0; i < columnsCount; i++) {
      sheet.autoSizeColumn(i);
      if (widths == null || widths.length <= i) {
        if (sheet.getColumnWidth(i) > MAX_COLUMN_LENGTH) {
          sheet.setColumnWidth(i, MAX_COLUMN_LENGTH);
        }
      } else {
        sheet.setColumnWidth(i, widths[i]);
      }


    }
  }

  private static CellStyle creatrHeaderCellStyle(Workbook workBook, Font fontForHeader) {
    CellStyle headerCellTextStyle = workBook.createCellStyle();
    headerCellTextStyle.setAlignment(HorizontalAlignment.CENTER);
    headerCellTextStyle.setFont(fontForHeader);
    return headerCellTextStyle;
  }

  private static Font createBoldFontForHeader(Workbook workBook) {
    Font fontAt0 = workBook.getFontAt(0);
    Font calibra11Bold = workBook.createFont();
    calibra11Bold.setFontName(fontAt0.getFontName());
    calibra11Bold.setFontHeight(fontAt0.getFontHeight());
    calibra11Bold.setBold(true);
    return calibra11Bold;
  }

  private static void createSpecialColumns(Map<String, String[]> dropDownListMap,
      Map<String, String> headerKeysPosition, Sheet sheet, int currentRowNumber) throws NumberFormatException {
    DataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
    for (Entry<String, String> entry : headerKeysPosition.entrySet()) {
      String columnIndex = entry.getKey();
      String columnName = entry.getValue();
      if (dropDownListMap.containsKey(columnName)) {
        int columnPos = Integer.parseInt(columnIndex);
        CellRangeAddressList addressList = new CellRangeAddressList(1, currentRowNumber, columnPos, columnPos);
        DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(
            dropDownListMap.get(columnName));
        DataValidation dataValidation = dvHelper.createValidation(dvConstraint, addressList);
        dataValidation.setShowErrorBox(true);
        dataValidation
            .createErrorBox(new StringBuilder(2048).append("Fehler:  \"").append(columnName).append('"').toString(),
                "Bitte geben Sie gültige Daten in der Dropdown-Liste an");
        sheet.addValidationData(dataValidation);
      }
    }
  }

  private static String decodeXMLEncoded(String value) {
    if (value == null || value.isEmpty()) {
      return ExcelCommon.EMPTY_STRING;
    }

    for (Entry<Pattern, String> entry : ExcelCommon.XML_ENCODED_VALUE_DECODER.entrySet()) {
      value = entry.getKey().matcher(value).replaceAll(entry.getValue());
    }
    return value;
  }

}
