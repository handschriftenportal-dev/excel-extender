package de.staatsbibliothek.berlin.oxygen.excel;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.staatsbibliothek.berlin.oxygen.excel.Excel2XMLParams.Excel2XMLParamsBuilder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 10.02.20
 */
class Excel2XMLTest {

  private static final Logger logger = Logger.getLogger(Excel2XMLTest.class.getName());

  @Test
  void convertExcel2XMLTest(@TempDir Path tempDirectory) {
    Path resourceDirectory = Paths.get("src", "test", "resources", "excel", "Tab_5130_vorab.xlsx");
    String excelFile = resourceDirectory.toFile().getAbsolutePath();
    String password = "";
    Path xmlFile = tempDirectory.resolve("test.xml");
    String xmlOut = xmlFile.toFile().getAbsolutePath();
    Excel2XMLParams params = new Excel2XMLParamsBuilder()
        .withExcelFile(excelFile)
        .withXmlFile(xmlOut)
        .withPassword(password)
        .withXmlFieldNameForExcelColumnNumber(ExcelTestData.XML_FIELDS_POS_NAME)
        .withExcelColumnNames(ExcelTestData.EXCEL_COLUMN_NAMES)
        .withXmlFieldTypeForExcelColumnNumber(ExcelTestData.XML_FIELDS_POS_TYPE)
        .createExcel2XMLParams();
    logger.info(Excel2XML.convertExcel2XML(params));

    assertAll(
        () -> assertTrue(Files.exists(xmlFile)),
        () -> assertTrue(Files.size(xmlFile) > 10000L)
    );

  }

  @Test
  void convertExcel2XMLWrongTest(@TempDir Path tempDirectory) {
    Path resourceDirectory = Paths.get("src", "test", "resources", "excel", "Tab_5130_vorab.xlsx");
    String excelFile = resourceDirectory.toFile().getAbsolutePath();
    String password = "";
    Path xmlFile = tempDirectory.resolve("test.xml");
    String xmlOut = xmlFile.toFile().getAbsolutePath();
    Excel2XMLParams params = new Excel2XMLParamsBuilder()
        .withExcelFile(excelFile)
        .withXmlFile(xmlOut)
        .withPassword(password)
        .withXmlFieldNameForExcelColumnNumber(ExcelTestData.XML_FIELDS_POS_NAME)
        .withExcelColumnNames(ExcelTestData.EXCEL_COLUMN_NAMES_WRONG)
        .withXmlFieldTypeForExcelColumnNumber(ExcelTestData.XML_FIELDS_POS_TYPE)
        .createExcel2XMLParams();
    assertThrows(NoSuchFieldError.class,
        () -> Excel2XML.convertExcel2XML(params)
    );
  }
}
