package de.staatsbibliothek.berlin.saxon.call;

import static de.staatsbibliothek.berlin.saxon.constant.ExtensionConstants.RTF_2_TXT;

import de.staatsbibliothek.berlin.oxygen.exception.RtfConverterException;
import de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;

/**
 * Convert RTF to TXT
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 23.10.24
 */
public class Rtf2TxtCall extends RtfConverterCall {

  private static final Logger logger = Logger.getLogger(Rtf2TxtCall.class.getName());

  @Override
  public Sequence call(XPathContext xPathContext, Sequence[] parameters) throws XPathException {
    checkParameters(parameters, RTF_2_TXT);
    String result = "";
    try {
      String textToConvert = parameters[0].head().getStringValue();
      RtfConverter rtfConverter = getRtfConverter(parameters);
      result = rtfConverter.convertToText(textToConvert);
      return StringValue.makeStringValue(result);
    } catch (ArrayIndexOutOfBoundsException | RtfConverterException e) {
      logger.log(Level.SEVERE, e.getMessage() + "\n" + result, e);
      throw new XPathException(e.getMessage(), e);
    }
  }
}
