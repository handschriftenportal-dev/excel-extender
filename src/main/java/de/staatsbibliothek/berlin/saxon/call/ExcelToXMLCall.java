package de.staatsbibliothek.berlin.saxon.call;

import static de.staatsbibliothek.berlin.saxon.constant.ExtensionConstants.EXCEL_TO_XML;

import de.staatsbibliothek.berlin.oxygen.excel.Excel2XML;
import de.staatsbibliothek.berlin.oxygen.excel.Excel2XMLParams;
import de.staatsbibliothek.berlin.oxygen.excel.Excel2XMLParams.Excel2XMLParamsBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;

/**
 * Extension function call of the ExcelToXMLCall
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 23.09.24
 */
public class ExcelToXMLCall extends ExtensionFunctionCall {

  private static final Logger logger = Logger.getLogger(ExcelToXMLCall.class.getName());
  public static final int CALL_ARGUMENTS_COUNT = 5;

  @Override
  public Sequence call(XPathContext xPathContext, Sequence[] arguments) throws XPathException {
    if (arguments == null
        || (arguments.length != CALL_ARGUMENTS_COUNT && arguments.length != CALL_ARGUMENTS_COUNT + 1)) {
      throw new XPathException(EXCEL_TO_XML +
          ": requires " + CALL_ARGUMENTS_COUNT
              + " arguments excelFile, xmlFile, password, xmlFieldNameForExcelColumnNumber"
              + ", excelColumnNames, optional xmlFieldTypeForExcelColumnNumber");
    }
    String result = "";
    try {
      Excel2XMLParams params = getExcel2XMLParams(arguments);
      result = Excel2XML.convertExcel2XML(params);
      return StringValue.makeStringValue(result);
    } catch (Exception e) {
      logger.log(Level.SEVERE, e.getMessage() + "\n" + result, e);
      throw new XPathException(e.getMessage(), e);
    }
  }

  private static Excel2XMLParams getExcel2XMLParams(Sequence[] arguments) throws XPathException {
    String excelFile = arguments[0].head().getStringValue();
    String xmlFile = arguments[1].head().getStringValue();
    String password = arguments[2].head().getStringValue();
    String xmlFieldNameForExcelColumnNumber = arguments[3].head().getStringValue();
    String excelColumnNames = arguments[4].head().getStringValue();
    String xmlFieldTypeForExcelColumnNumber = null;
    if (arguments.length > 5) {
      xmlFieldTypeForExcelColumnNumber = arguments[5].head().getStringValue();
    }
    Excel2XMLParams params = new Excel2XMLParamsBuilder()
        .withExcelFile(excelFile)
        .withXmlFile(xmlFile)
        .withPassword(password)
        .withXmlFieldNameForExcelColumnNumber(xmlFieldNameForExcelColumnNumber)
        .withExcelColumnNames(excelColumnNames)
        .withXmlFieldTypeForExcelColumnNumber(xmlFieldTypeForExcelColumnNumber)
        .createExcel2XMLParams();
    return params;
  }
}
