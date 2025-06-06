package de.staatsbibliothek.berlin.saxon.call;

import static de.staatsbibliothek.berlin.saxon.constant.ExtensionConstants.NER_SERVICE_CLIENT;

import de.staatsbibliothek.berlin.oxygen.ner.NERServiceClient;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;

/**
 * Extension function call of the NERServiceClient
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 23.09.24
 */
public class NERServiceClientCall extends ExtensionFunctionCall {

  private static final Logger logger = Logger.getLogger(NERServiceClientCall.class.getName());
  public static final int CALL_ARGUMENTS_COUNT = 3;

  @Override
  public Sequence call(XPathContext xPathContext, Sequence[] arguments) throws XPathException {
    if (arguments == null || arguments.length != CALL_ARGUMENTS_COUNT) {
      throw new XPathException(NER_SERVICE_CLIENT + ": requires " + CALL_ARGUMENTS_COUNT
          + " arguments: nerServiceURL, textToAnalyze, entityFunc");
    }
    String result = "";
    try {
      String nerServiceURL = arguments[0].head().getStringValue();
      String textToAnalyze = arguments[1].head().getStringValue();
      String entityFunc = arguments[2].head().getStringValue();
      result = NERServiceClient.getNamedEntitiesAsCSVSnipset(nerServiceURL, textToAnalyze, entityFunc);
      return StringValue.makeStringValue(result);
    } catch (Exception e) {
      logger.log(Level.SEVERE, e.getMessage() + "\n" + result, e);
      throw new XPathException(e.getMessage(), e);
    }
  }
}
