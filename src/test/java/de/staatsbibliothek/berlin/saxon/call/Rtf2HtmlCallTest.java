package de.staatsbibliothek.berlin.saxon.call;

import static de.staatsbibliothek.berlin.oxygen.rtf.RtfTestData.ALL_CANDIDATES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Logger;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;
import org.junit.jupiter.api.Test;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 20.02.25
 */
class Rtf2HtmlCallTest {

  private static final Logger logger = Logger.getLogger(Rtf2HtmlCallTest.class.getName());
  private static final String EXCEPTION_MESSAGE = "rtf-to-html: Invalid number of arguments!\n Supported arguments:\n rtfText - required,\n onlyHtmlBodyContent - optional ('true', 'false' (default)),\n converterType - optional: (\n 1 - JEditorPane,\n 2 - TikaRTF (default),\n 3 - TikaAuto\n)";

  @Test
  void testCall1() throws XPathException {
    Rtf2HtmlCall rtf2HtmlCall = new Rtf2HtmlCall();
    for (String testValue : ALL_CANDIDATES) {
      Sequence[] arguments = {StringValue.makeStringValue(testValue), StringValue.makeStringValue("true")};
      Sequence result = rtf2HtmlCall.call(null, arguments);
      String value = result.head().getStringValue();
      logger.info(value);
      assertFalse(value.contains("\\"));
      assertFalse(value.contains("body"));
    }
  }

  @Test
  void testCall2() throws XPathException {
    Rtf2HtmlCall rtf2HtmlCall = new Rtf2HtmlCall();
    for (String testValue : ALL_CANDIDATES) {
      Sequence[] arguments = {StringValue.makeStringValue(testValue)};
      Sequence result = rtf2HtmlCall.call(null, arguments);
      String value = result.head().getStringValue();
      assertFalse(value.contains("\\"));
      assertTrue(value.contains("body"));
    }
  }

  @Test
  void TestCallWithError() {
    Rtf2HtmlCall call = new Rtf2HtmlCall();
    XPathException exception = assertThrows(XPathException.class, () -> {
      call.call(null, null);
    });
    String message = exception.getMessage();
    logger.info(message);
    assertEquals(EXCEPTION_MESSAGE, message);
  }
}
