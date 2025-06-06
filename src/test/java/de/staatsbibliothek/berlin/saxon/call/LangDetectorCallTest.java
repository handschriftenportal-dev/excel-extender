package de.staatsbibliothek.berlin.saxon.call;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.staatsbibliothek.berlin.oxygen.lang.Messages;
import java.util.logging.Logger;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;
import org.junit.jupiter.api.Test;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 20.02.25
 */
class LangDetectorCallTest {

  private static final Logger logger = Logger.getLogger(LangDetectorCallTest.class.getName());
  private static final String EXCEPTION_MESSAGE = "detect-language: requires exactly one argument: textToCheck";

  public static final String TEST_VALUE = "Llibre de contemplació en Déu";

  @Test
  void call() throws XPathException {
    LangDetectorCall call = new LangDetectorCall();
    Sequence[] arguments = {StringValue.makeStringValue(TEST_VALUE)};
    Sequence result = call.call(null, arguments);
    String expected = Messages.getMessage("CATALAN");
    assertEquals(expected, result.head().getStringValue());
  }

  @Test
  void TestCallWithError() {
    LangDetectorCall call = new LangDetectorCall();
    XPathException exception = assertThrows(XPathException.class, () -> {
      call.call(null, null);
    });
    String message = exception.getMessage();
    logger.info(message);
    assertEquals(EXCEPTION_MESSAGE, message);
  }
}
