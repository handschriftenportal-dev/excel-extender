package de.staatsbibliothek.berlin.saxon.call;

import static de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter.CONVERTER_TYPE_TIKA_AUTO;
import static de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter.CONVERTER_TYPE_TIKA_RTF;
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
 * @since 11.03.25
 */
class Rtf2NodeCallTest {

  private static final Logger logger = Logger.getLogger(Rtf2NodeCallTest.class.getName());
  private static final String EXCEPTION_MESSAGE = "rtf-to-node: Invalid number of arguments!\n Supported arguments:\n rtfText - required,\n converterType - optional: (\n 1 - JEditorPane,\n 2 - TikaRTF (default),\n 3 - TikaAuto\n)";

  @Test
  void testCallJEditorPane() throws XPathException {
    callRTF2Node("1");
  }

  @Test
  void testCallTika() throws XPathException {
    callRTF2Node(String.valueOf(CONVERTER_TYPE_TIKA_RTF));
  }

  @Test
  void testCallTikaAuto() throws XPathException {
    callRTF2Node(String.valueOf(CONVERTER_TYPE_TIKA_AUTO));
  }

  private static void callRTF2Node(String converter) throws XPathException {
    Rtf2NodeCall call = new Rtf2NodeCall();
    StringValue coverterStringValue = StringValue.makeStringValue(converter);
    for (String testValue : ALL_CANDIDATES) {
      Sequence[] arguments = {StringValue.makeStringValue(testValue), coverterStringValue};
      Sequence result = call.call(null, arguments);
      assertFalse(result.head().getStringValue().contains("\\"));
    }
  }

  @Test
  void testCallWithError() {
    Rtf2NodeCall call = new Rtf2NodeCall();
    XPathException exception = assertThrows(XPathException.class, () -> {
      call.call(null, null);
    });
    String message = exception.getMessage();
    logger.info(message);
    assertEquals(EXCEPTION_MESSAGE, message);
  }
}
