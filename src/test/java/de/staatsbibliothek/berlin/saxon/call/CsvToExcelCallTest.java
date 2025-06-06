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
 * @since 20.02.25
 */
class CsvToExcelCallTest {

  private static final Logger logger = Logger.getLogger(CsvToExcelCallTest.class.getName());
  private static final String EXCEPTION_MESSAGE = "csv-to-excel: requires 7 arguments csvFilePath, excelFilePath, password, lockedKeysStr, dropDownListStr, datatypeListStr, sheetName";

  @Test
  void TestCallWithError() {
    CsvToExcelCall call = new CsvToExcelCall();
    XPathException exception = assertThrows(XPathException.class, () -> {
      call.call(null, null);
    });
    String message = exception.getMessage();
    logger.info(message);
    assertEquals(EXCEPTION_MESSAGE, message);
  }

  @Test
  void testCall(@TempDir Path tempDirectory) throws XPathException {
    Path excelFiles = tempDirectory.resolve("krz");
    Path targetFile = excelFiles.resolve("04_Berlin_SB_Kat_6_1_KRZ.xlsx");
    Path csvFile = Paths.get("src", "test", "resources", "04_Berlin_SB_Kat_6_1_KRZ.csv");
    CsvToExcelCall call = new CsvToExcelCall();
    Sequence[] arguments = {
        StringValue.makeStringValue(csvFile.toAbsolutePath().toString()),
        StringValue.makeStringValue(targetFile.toAbsolutePath().toString()),
        StringValue.makeStringValue(""),
        StringValue.makeStringValue(""),
        StringValue.makeStringValue(ExcelTestData.ALL_DROP_DOWN_FIELDS),
        StringValue.makeStringValue(""),
        StringValue.makeStringValue("04_Berlin_SB_Kat_6_1_KRZ.xlsx")};
    call.call(null, arguments);
    assertAll(
        () -> assertTrue(Files.exists(excelFiles)),
        () -> assertTrue(Files.size(excelFiles) > 0L)
    );
    logger.info("Excels exported to: " + excelFiles.toAbsolutePath());
  }
}
