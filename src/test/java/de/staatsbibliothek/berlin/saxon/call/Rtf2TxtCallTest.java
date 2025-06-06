package de.staatsbibliothek.berlin.saxon.call;

import static de.staatsbibliothek.berlin.oxygen.rtf.RtfTestData.ALL_CANDIDATES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.logging.Logger;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;
import org.junit.jupiter.api.Test;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 20.02.25
 */
class Rtf2TxtCallTest {

  private static final Logger logger = Logger.getLogger(Rtf2TxtCallTest.class.getName());
  private static final String EXCEPTION_MESSAGE = "rtf-to-txt: Invalid number of arguments!\n Supported arguments:\n rtfText - required,\n converterType - optional: (\n 1 - JEditorPane,\n 2 - TikaRTF (default),\n 3 - TikaAuto\n)";

  @Test
  void testCall() throws XPathException {
    Rtf2TxtCall rtf2TxtCall = new Rtf2TxtCall();
    for (String testValue : ALL_CANDIDATES) {
      Sequence[] arguments = {StringValue.makeStringValue(testValue)};
      Sequence result = rtf2TxtCall.call(null, arguments);
      assertFalse(result.head().getStringValue().contains("\\"));
    }
  }

  @Test
  void TestCallWithError() {
    Rtf2TxtCall call = new Rtf2TxtCall();
    XPathException exception = assertThrows(XPathException.class, () -> {
      call.call(null, null);
    });
    String message = exception.getMessage();
    logger.info(message);
    assertEquals(EXCEPTION_MESSAGE, message);
  }
}
