package de.staatsbibliothek.berlin.saxon.call;

import static de.staatsbibliothek.berlin.saxon.constant.ExtensionConstants.CSV_TO_EXCEL;

import de.staatsbibliothek.berlin.oxygen.excel.ExcelExtender;
import de.staatsbibliothek.berlin.oxygen.excel.ExcelExtenderParams;
import de.staatsbibliothek.berlin.oxygen.excel.ExcelExtenderParams.ExcelExtenderParamsBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;

/**
 * Extension function call of the CsvToExcel
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 23.09.24
 */
public class CsvToExcelCall extends ExtensionFunctionCall {

  private static final Logger logger = Logger.getLogger(CsvToExcelCall.class.getName());
  public static final int CALL_ARGUMENTS_COUNT = 7;

  @Override
  public Sequence call(XPathContext xPathContext, Sequence[] arguments) throws XPathException {
    if (arguments == null || arguments.length != CALL_ARGUMENTS_COUNT) {
      throw new XPathException(CSV_TO_EXCEL +
          ": requires " + CALL_ARGUMENTS_COUNT
              + " arguments csvFilePath, excelFilePath, password, lockedKeysStr, dropDownListStr, datatypeListStr, sheetName");
    }
    String result = "";
    try {
      String csvFilePath = arguments[0].head().getStringValue();
      String excelFilePath = arguments[1].head().getStringValue();
      String password = arguments[2].head().getStringValue();
      String lockedKeysStr = arguments[3].head().getStringValue();
      String dropDownListStr = arguments[4].head().getStringValue();
      String datatypeListStr = arguments[5].head().getStringValue();
      String sheetName = arguments[6].head().getStringValue();
      ExcelExtenderParams params = new ExcelExtenderParamsBuilder()
          .withCsvFilePath(csvFilePath)
          .withExcelFilePath(excelFilePath)
          .withPassword(password)
          .withLockedKeysStr(lockedKeysStr)
          .withDropDownListStr(dropDownListStr)
          .withDatatypeListStr(datatypeListStr)
          .withSheetName(sheetName)
          .createExcelExtenderParams();
      result = ExcelExtender.convertCSV2Excel(params);
      return StringValue.makeStringValue(result);
    } catch (Exception e) {
      logger.log(Level.SEVERE, e.getMessage() + "\n" + result, e);
      throw new XPathException(e.getMessage(), e);
    }
  }
}
