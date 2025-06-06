package de.staatsbibliothek.berlin.saxon.call;

import static de.staatsbibliothek.berlin.saxon.constant.ExtensionConstants.DETECT_LANGUAGE;

import de.staatsbibliothek.berlin.oxygen.lang.LangDetector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;

/**
 * Call LangDetector
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 23.09.24
 */
public class LangDetectorCall extends ExtensionFunctionCall {

  private static final Logger logger = Logger.getLogger(LangDetectorCall.class.getName());

  public static final int CALL_ARGUMENTS_COUNT = 1;

  @Override
  public Sequence call(XPathContext xPathContext, Sequence[] arguments) throws XPathException {
    if (arguments == null || arguments.length != CALL_ARGUMENTS_COUNT) {
      throw new XPathException(DETECT_LANGUAGE + ": requires exactly one argument: textToCheck");
    }
    String result = "";
    try {
      String textToCheck = arguments[0].head().getStringValue();
      result = LangDetector.detectLanguage(textToCheck);
      return StringValue.makeStringValue(result);
    } catch (Exception e) {
      logger.log(Level.SEVERE, e.getMessage() + "\n" + result, e);
      throw new XPathException(e.getMessage(), e);
    }
  }
}
