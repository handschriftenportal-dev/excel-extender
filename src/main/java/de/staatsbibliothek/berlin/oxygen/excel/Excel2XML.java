package de.staatsbibliothek.berlin.oxygen.excel;

import static de.staatsbibliothek.berlin.oxygen.excel.ExcelCommon.LINE_SEPARATOR;
import static de.staatsbibliothek.berlin.oxygen.excel.ExcelCommon.PATTERN_DOUBLE_PIPE;
import static de.staatsbibliothek.berlin.oxygen.excel.ExcelCommon.getExceptionStacktraceAsString;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Convert Excel sheet to xml file
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 28.01.20
 */
public class Excel2XML {

  private static final Logger logger = Logger.getLogger(Excel2XML.class.getName());

  public static final String TAG_LINES = "lines";
  public static final String TAG_LINE = "line";

  public static String convertExcel2XML(Excel2XMLParams params) {

    StringBuilder log = logParams(params);

    if (params.getXmlFieldNameForExcelColumnNumber() == null || params.getXmlFieldNameForExcelColumnNumber()
        .isEmpty()) {
      return log.append("FEHLER: Erforderlicher Parameter 'xmlFieldNameForExcelColumnNumber' nicht angegeben!")
          .toString();
    }
    if (params.getExcelColumnNames() == null || params.getExcelColumnNames().isEmpty()) {
      return log.append("FEHLER: Erforderlicher Parameter 'excelColumnNames' nicht angegeben!")
          .toString();
    }
    return exportExcel2XML(params, log);
  }

  private static String exportExcel2XML(Excel2XMLParams params, StringBuilder log) {
    //"0;key||2;anzeige||3;gndAnsetzung||4;gndNummer"
    Map<Integer, String> xmlFieldNameForExcelColumnNumberMap = getColumnNumberMap(log,
        params.getXmlFieldNameForExcelColumnNumber());

    //"0;regex||2;regex
    Map<Integer, String> xmlFieldTypeForExcelColumnNumberMap = getColumnNumberMap(log,
        params.getXmlFieldTypeForExcelColumnNumber());

    //"0;key||2;anzeige||3;gndAnsetzung||4;gndNummer"
    Map<Integer, String> excelColumnNamesMap = getColumnNumberMap(log, params.getExcelColumnNames());

    Path excelPath = ExcelCommon.resolvePathFromString(params.getExcelFile());
    Path xmlPath = ExcelCommon.resolvePathFromString(params.getXmlFile());

    return storeXML2File(params, log, excelPath, xmlFieldNameForExcelColumnNumberMap,
        xmlFieldTypeForExcelColumnNumberMap,
        excelColumnNamesMap, xmlPath);
  }

  private static String storeXML2File(Excel2XMLParams params, StringBuilder log, Path excelPath,
      Map<Integer, String> xmlFieldNameForExcelColumnNumberMap,
      Map<Integer, String> xmlFieldTypeForExcelColumnNumberMap, Map<Integer, String> excelColumnNamesMap,
      Path xmlPath) {
    try (
        InputStream inputStream = Files.newInputStream(excelPath);
        Workbook workbook = WorkbookFactory.create(inputStream, params.getPassword())
    ) {
      Sheet sheet = workbook.getSheetAt(0);
      Document doc = prepareXMLTargetDocument();

      createXMLFromExcel(log, xmlFieldNameForExcelColumnNumberMap, xmlFieldTypeForExcelColumnNumberMap,
          excelColumnNamesMap, sheet, doc);

      storeXmlFileToDisk(xmlPath, doc);

      return log.toString();
    } catch (Exception ex) {
      String exceptionStackTrace = getExceptionStacktraceAsString(ex);
      return new StringBuilder(4096).append("FEHLER: ").append(ex.getMessage()).append(log)
          .append(exceptionStackTrace).toString();
    }
  }

  private static StringBuilder logParams(Excel2XMLParams params) {
    StringBuilder log = new StringBuilder(1024).append(LINE_SEPARATOR).append("Parameters excelFile='")
        .append(params.getExcelFile()).append("',").append(LINE_SEPARATOR).append(" xmlFile='")
        .append(params.getXmlFile()).append("',").append(LINE_SEPARATOR).append(" password='")
        .append(params.getPassword().replaceAll(".", "*"))
        .append("',").append(LINE_SEPARATOR).append(" xmlFieldNameForExcelColumnNumber='")
        .append(params.getXmlFieldNameForExcelColumnNumber())
        .append(LINE_SEPARATOR).append(" xmlFieldTypeForExcelColumnNumber='")
        .append(params.getXmlFieldTypeForExcelColumnNumber())
        .append(LINE_SEPARATOR).append(" excelColumnNames='")
        .append(params.getExcelColumnNames()).append(LINE_SEPARATOR);
    return log;
  }

  private static Map<Integer, String> getColumnNumberMap(StringBuilder log, String xmlFieldNameForExcelColumnNumber) {
    Map<Integer, String> xmlFieldNameForExcelColumnNumberMap = new HashMap<>();
    log.append(
        parseXMLFieldNameForExcelColumnNumber(xmlFieldNameForExcelColumnNumber, xmlFieldNameForExcelColumnNumberMap,
            log));
    return xmlFieldNameForExcelColumnNumberMap;
  }

  private static Document prepareXMLTargetDocument() throws ParserConfigurationException {
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    return docBuilder.newDocument();
  }

  private static void storeXmlFileToDisk(Path xmlPath, Document doc)
      throws IOException, TransformerException {
    Files.createDirectories(xmlPath.getParent());
    DOMSource source = new DOMSource(doc);
    try (BufferedWriter writer = Files.newBufferedWriter(xmlPath)) {
      StreamResult streamResult = new StreamResult(writer);
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      resetAttribute(transformerFactory, XMLConstants.ACCESS_EXTERNAL_DTD);
      resetAttribute(transformerFactory, XMLConstants.ACCESS_EXTERNAL_STYLESHEET);

      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.transform(source, streamResult);
    }
  }

  private static void resetAttribute(TransformerFactory transformerFactory, String attributeName) {
    try {
      transformerFactory.setAttribute(attributeName, "");
    } catch (Exception e) {
      logger.warning("Unable to set attribute: " + attributeName + ": " + e.getMessage());
    }
  }

  private static void createXMLFromExcel(StringBuilder log, Map<Integer, String> xmlFieldNameForExcelColumnNumberMap,
      Map<Integer, String> xmlFieldTypeForExcelColumnNumberMap, Map<Integer, String> excelColumnNamesMap, Sheet sheet,
      Document doc) throws Exception {
    Element rootElement = doc.createElement(TAG_LINES);
    doc.appendChild(rootElement);
    for (Row row : sheet) {
      int rowNumber = row.getRowNum();
      if (rowNumber == 0) {
        checkFirstRow(log, excelColumnNamesMap, row);
      } else {
        processExcelLineValues(log, xmlFieldNameForExcelColumnNumberMap, xmlFieldTypeForExcelColumnNumberMap, doc,
            rootElement, row);
      }
    }

  }

  static void processExcelLineValues(StringBuilder log,
      Map<Integer, String> xmlFieldNameForExcelColumnNumberMap,
      Map<Integer, String> xmlFieldTypeForExcelColumnNumberMap, Document doc, Element rootElement, Row row)
      throws Exception {
    StringBuilder nameForType = new StringBuilder();
    StringBuilder valueForType = new StringBuilder();
    Element line = doc.createElement(TAG_LINE);
    for (Entry<Integer, String> entry : xmlFieldNameForExcelColumnNumberMap.entrySet()) {
      Integer fieldNumber = entry.getKey();
      Cell cell = row.getCell(fieldNumber, RETURN_BLANK_AS_NULL);
      if (cell != null) {
        String value = getValueAsStringOrNumber(cell);
        line.setAttribute(entry.getValue(), value);
        if (xmlFieldTypeForExcelColumnNumberMap.containsKey(fieldNumber)) {

          valueForType.delete(0, valueForType.length());
          valueForType.append("\\b(");
          valueForType.append(Pattern.quote(value));
          valueForType.append(")\\b");

          nameForType.delete(0, nameForType.length());
          nameForType.append(entry.getValue());
          nameForType.append('-');
          nameForType.append(xmlFieldTypeForExcelColumnNumberMap.get(fieldNumber));

          line.setAttribute(nameForType.toString(), valueForType.toString());
        }
      }
    }
    rootElement.appendChild(line);
  }

  static void checkFirstRow(StringBuilder log, Map<Integer, String> excelColumnNamesMap, Row row)
      throws Exception {
    for (Entry<Integer, String> entry : excelColumnNamesMap.entrySet()) {
      Cell cell = row.getCell(entry.getKey(), RETURN_BLANK_AS_NULL);
      Objects.requireNonNull(cell,
          MessageFormat.format("Column name ''{0}'' not available in the ExcelFile", entry.getValue()));

      String value = getValueAsStringOrNumber(cell);
      if (!entry.getValue().equals(value)) {
        throw new NoSuchFieldError(
            MessageFormat.format(
                "FEHLER: Wrong column name at index ''{0}'' Expected:''{1}'' != Available:''{2}''{3}{4}",
                entry.getKey(), entry.getValue(), value, LINE_SEPARATOR, log));
      }
    }
  }

  static String parseXMLFieldNameForExcelColumnNumber(String nameForExcelColumnNumber,
      Map<Integer, String> xmlFieldNameForExcelColumnNumberMap, StringBuilder log) {
    if (nameForExcelColumnNumber != null) {
      for (String line : PATTERN_DOUBLE_PIPE.split(nameForExcelColumnNumber)) {
        mapColumnIndexWithName(xmlFieldNameForExcelColumnNumberMap, line, log);
      }
    }
    return log.toString();
  }

  private static void mapColumnIndexWithName(Map<Integer, String> xmlFieldNameForExcelColumnNumberMap, String line,
      StringBuilder log) {
    boolean first = true;
    Integer columnNumber = null;
    String xmlFieldName = null;
    String[] xmlFieldNameForExcelColumnNumberValues = line.split(";");
    for (String value : xmlFieldNameForExcelColumnNumberValues) {
      if (first) {
        columnNumber = Integer.parseInt(value);
        first = false;
      } else {
        xmlFieldName = value;
        break;
      }
    }
    if (columnNumber == null || xmlFieldName == null || xmlFieldName.isEmpty()) {
      log.append("Fehlerhafter Eintrag im 'xmlFieldNameForExcelColumnNumber': 'columnNumber'='").append(columnNumber)
          .append("', 'xmlFieldName'='").append(xmlFieldName).append('\'').append(LINE_SEPARATOR);
    } else {
      xmlFieldNameForExcelColumnNumberMap.put(columnNumber, xmlFieldName);
    }
  }

  static String getValueAsStringOrNumber(Cell cell) throws Exception {
    String value;
    if (Objects.requireNonNull(cell.getCellType()) == CellType.NUMERIC) {
      long cellAsANumber = (long) cell.getNumericCellValue();
      if (cellAsANumber == 0L) {
        value = "";
      } else {
        value = String.valueOf(cellAsANumber);
      }
    } else {
      value = cell.getStringCellValue();
    }
    return value;
  }
}
