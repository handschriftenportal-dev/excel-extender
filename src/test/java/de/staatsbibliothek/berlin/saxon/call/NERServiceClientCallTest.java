package de.staatsbibliothek.berlin.saxon.call;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.staatsbibliothek.berlin.oxygen.ner.NERTestData;
import java.util.logging.Logger;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;
import org.junit.jupiter.api.Test;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 11.03.25
 */
class NERServiceClientCallTest {

  private static final Logger logger = Logger.getLogger(NERServiceClientCallTest.class.getName());
  private static final String EXCEPTION_MESSAGE = "ner-service-client: requires 3 arguments: nerServiceURL, textToAnalyze, entityFunc";

  @Test
  void TestCallWithError() {
    NERServiceClientCall call = new NERServiceClientCall();
    XPathException exception = assertThrows(XPathException.class, () -> {
      call.call(null, null);
    });
    String message = exception.getMessage();
    logger.info(message);
    assertEquals(EXCEPTION_MESSAGE, message);
  }

  @Test
  void testCall() throws XPathException {
    NERServiceClientCall call = new NERServiceClientCall();
    Sequence[] arguments = {
        StringValue.makeStringValue(NERTestData.URL),
        StringValue.makeStringValue(NERTestData.TEXT_TO_ANALYZE),
        StringValue.makeStringValue(NERTestData.ENTITY_FUNCTIONS)
    };
    Sequence result = call.call(null, arguments);
    String content = result.head().getStringValue();
    assertNotNull(content);
  }
}
