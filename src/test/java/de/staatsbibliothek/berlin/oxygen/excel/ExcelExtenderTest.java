package de.staatsbibliothek.berlin.oxygen.excel;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.staatsbibliothek.berlin.oxygen.excel.ExcelExtenderParams.ExcelExtenderParamsBuilder;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 20.11.19
 */
public class ExcelExtenderTest {

  private static final Logger logger = Logger.getLogger(ExcelExtenderTest.class.getName());


  @Test
  void testConvertCSV2Excel(@TempDir Path tempDirectory) throws IOException {
    Path excelFiles = tempDirectory.resolve("krz");
    //Path excelFiles = Paths.get("/tmp/krz/");
    Path targetFile = excelFiles.resolve("04_Berlin_SB_Kat_6_1_KRZ.xlsx");
    Path csvFile = Paths.get("src", "test", "resources", "04_Berlin_SB_Kat_6_1_KRZ.csv");
    ExcelExtenderParams params = new ExcelExtenderParamsBuilder()
        .withCsvFilePath(csvFile.toAbsolutePath().toString())
        .withExcelFilePath(targetFile.toAbsolutePath().toString())
        .withDropDownListStr(ExcelTestData.ALL_DROP_DOWN_FIELDS)
        .withSheetName("04_Berlin_SB_Kat_6_1_KRZ.xlsx")
        .createExcelExtenderParams();
    ExcelExtender.convertCSV2Excel(params);
    assertAll(
        () -> assertTrue(Files.exists(excelFiles)),
        () -> assertTrue(Files.size(excelFiles) > 0L)
    );
    logger.info("Excels exported to: " + excelFiles.toAbsolutePath());
  }

  @Test
  void testAutoSizeCell() throws IOException {
    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet();
      Row row = sheet.createRow(0);
      int columnIndex = 0;
      Cell cell = row.createCell(columnIndex);
      int cellWidth = sheet.getColumnWidth(columnIndex);
      String llibreDeContemplacioEnDeu = "Llibre de contemplació en Déu";
      cell.setCellValue(llibreDeContemplacioEnDeu);
      ExcelExtender.autoSizeCell(cell, llibreDeContemplacioEnDeu);
      assertTrue(cellWidth < sheet.getColumnWidth(columnIndex));
    }
  }

  @Test
  void testConvertCSV2ExcelPerEntry(@TempDir Path tempDirectory) throws IOException {

    Path excelFiles = tempDirectory.resolve("lieferanten");
    //Path excelFiles = Paths.get("/tmp/lieferanten/");

    Path csvFile = Paths.get("src", "test", "resources", "lieferanten20231124.csv");

    ExcelExtender.convertCSV2ExcelPerEntry(csvFile.toAbsolutePath().toString(), excelFiles.toAbsolutePath().toString(),
        "", "", "", "", true, ExcelCommon.COLUMN_KEY_WIDTH, ExcelCommon.COLUMN_VALUE_WIDTH);
    assertAll(
        () -> assertTrue(Files.exists(excelFiles)),
        () -> assertTrue(Files.size(excelFiles) > 0L)
    );
    logger.info("Excels exported to: " + excelFiles.toAbsolutePath());

  }

  @Test
  void testConvertCSV2ExcelPerEntryLine(@TempDir Path tempDirectory) throws IOException {

    Path excelFiles = tempDirectory.resolve("lieferantenLine");

    Path csvFile = Paths.get("src", "test", "resources", "lieferanten20231124.csv");

    ExcelExtender.convertCSV2ExcelPerEntry(csvFile.toAbsolutePath().toString(), excelFiles.toAbsolutePath().toString(),
        "", "", "", "", false, ExcelCommon.COLUMN_KEY_WIDTH, ExcelCommon.COLUMN_VALUE_WIDTH);
    assertAll(
        () -> assertTrue(Files.exists(excelFiles)),
        () -> assertTrue(Files.size(excelFiles) > 0L)
    );
    logger.info("Excels exported to: " + excelFiles.toAbsolutePath());

  }

  @Test
  void testReadCSV(@TempDir Path tempDirectory) throws IOException {
    Path csvFile = tempDirectory.resolve("test2.csv");
    Path excelFile = tempDirectory.resolve("test.xlsx");
    //TODO: Remove it
    //Path excelFile = Paths.get("/tmp/test2.xlsx");
    Files.write(csvFile, Arrays.asList(ExcelTestData.CSV_CONTENT.split("\n")), Charset.defaultCharset());
    ExcelExtenderParams params = new ExcelExtenderParamsBuilder()
        .withCsvFilePath(csvFile.toAbsolutePath().toString())
        .withExcelFilePath(excelFile.toAbsolutePath().toString())
        .withPassword(ExcelTestData.PASSWORD)
        .withLockedKeysStr("aktuelle Wert")
        .withDropDownListStr("\"neuer Wert\";\"vorhanden\";\"nicht vorhanden\"")
        .withDatatypeListStr("NEU;BOOLEAN||Anzahl;NUMERIC")
        .withSheetName(ExcelTestData.SHEET_NAME)
        .createExcelExtenderParams();
    ExcelExtender.convertCSV2Excel(params);
    assertAll(
        () -> assertTrue(Files.exists(excelFile)),
        () -> assertTrue(Files.size(excelFile) > 0L)
    );
  }

  @Test
  void testRemoveValueQuotation() {
    assertEquals(ExcelTestData.TEXT, ExcelExtender.removeValueQuotation("\"" + ExcelTestData.TEXT));
    assertEquals(ExcelTestData.TEXT, ExcelExtender.removeValueQuotation(ExcelTestData.TEXT + "\""));
    assertEquals(ExcelTestData.TEXT, ExcelExtender.removeValueQuotation("\"" + ExcelTestData.TEXT + "\""));
    assertEquals(ExcelTestData.TEXT, ExcelExtender.removeValueQuotation("\"" + ExcelTestData.TEXT + "\"\n"));
    assertEquals(ExcelTestData.TEXT, ExcelExtender.removeValueQuotation("\uFEFF\"" + ExcelTestData.TEXT + "\"\n"));
    assertEquals(ExcelTestData.TEXT, ExcelExtender.removeValueQuotation("\uFFFE\"" + ExcelTestData.TEXT + "\"\n"));
  }


  @Test
  void testGetEndIndexBeforeQuotation() {
    assertEquals(0, ExcelExtender.getEndIndexBeforeQuotation(ExcelTestData.TEXT));
    assertEquals(1, ExcelExtender.getEndIndexBeforeQuotation(ExcelTestData.TEXT + "\""));
    assertEquals(2, ExcelExtender.getEndIndexBeforeQuotation(ExcelTestData.TEXT + "\"\n"));
  }

  @Test
  void testGetStartIndexAfterQuotation() {
    assertEquals(0, ExcelExtender.getStartIndexAfterQuotation(ExcelTestData.TEXT));
    assertEquals(1, ExcelExtender.getStartIndexAfterQuotation("\"" + ExcelTestData.TEXT));
    assertEquals(2, ExcelExtender.getStartIndexAfterQuotation("\uFEFF\"" + ExcelTestData.TEXT));
    assertEquals(2, ExcelExtender.getStartIndexAfterQuotation("\uFFFE\"" + ExcelTestData.TEXT));
  }
}
