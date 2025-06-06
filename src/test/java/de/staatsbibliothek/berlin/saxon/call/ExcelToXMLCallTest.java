package de.staatsbibliothek.berlin.saxon.call;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.staatsbibliothek.berlin.oxygen.excel.ExcelTestData;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 11.03.25
 */
class ExcelToXMLCallTest {

  private static final Logger logger = Logger.getLogger(ExcelToXMLCallTest.class.getName());
  private static final String EXCEPTION_MESSAGE = "excel-to-xml: requires 5 arguments excelFile, xmlFile, password,"
      + " xmlFieldNameForExcelColumnNumber, excelColumnNames, optional xmlFieldTypeForExcelColumnNumber";

  @Test
  void TestCallWithError() {
    ExcelToXMLCall call = new ExcelToXMLCall();
    XPathException exception = assertThrows(XPathException.class, () -> {
      call.call(null, null);
    });
    String message = exception.getMessage();
    logger.info(message);
    assertEquals(EXCEPTION_MESSAGE, message);
  }

  @Test
  void testCall(@TempDir Path tempDirectory) throws XPathException {
    Path resourceDirectory = Paths.get("src", "test", "resources", "excel", "Tab_5130_vorab.xlsx");
    String excelFile = resourceDirectory.toFile().getAbsolutePath();
    String password = "";
    Path xmlFile = tempDirectory.resolve("test.xml");
    String xmlOut = xmlFile.toFile().getAbsolutePath();
    ExcelToXMLCall call = new ExcelToXMLCall();

    Sequence[] arguments = {
        StringValue.makeStringValue(excelFile),
        StringValue.makeStringValue(xmlOut),
        StringValue.makeStringValue(password),
        StringValue.makeStringValue(ExcelTestData.XML_FIELDS_POS_NAME),
        StringValue.makeStringValue(ExcelTestData.EXCEL_COLUMN_NAMES)};
    Sequence result = call.call(null, arguments);
    logger.info(result.head().getStringValue());

    assertAll(
        () -> assertTrue(Files.exists(xmlFile)),
        () -> assertTrue(Files.size(xmlFile) > 10000L)
    );
  }

  @Test
  void testCallWrong(@TempDir Path tempDirectory) throws XPathException {
    Path resourceDirectory = Paths.get("src", "test", "resources", "excel", "Tab_5130_vorab.xlsx");
    String excelFile = resourceDirectory.toFile().getAbsolutePath();
    String password = "";
    Path xmlFile = tempDirectory.resolve("test.xml");
    String xmlOut = xmlFile.toFile().getAbsolutePath();
    ExcelToXMLCall call = new ExcelToXMLCall();
    Sequence[] arguments = {
        StringValue.makeStringValue(excelFile),
        StringValue.makeStringValue(xmlOut),
        StringValue.makeStringValue(password),
        StringValue.makeStringValue(ExcelTestData.XML_FIELDS_POS_NAME),
        StringValue.makeStringValue(ExcelTestData.EXCEL_COLUMN_NAMES_WRONG)};
    assertThrows(NoSuchFieldError.class,
        () -> call.call(null, arguments));
  }
}
