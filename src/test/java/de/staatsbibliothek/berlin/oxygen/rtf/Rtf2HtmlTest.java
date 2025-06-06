package de.staatsbibliothek.berlin.oxygen.rtf;

import static de.staatsbibliothek.berlin.oxygen.rtf.RtfTestData.VALUE_16;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.staatsbibliothek.berlin.oxygen.exception.RtfConverterException;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 21.01.22
 */
class Rtf2HtmlTest {

  private static final Logger logger = Logger.getLogger(Rtf2HtmlTest.class.getName());

  @Test
  void convertToHtmlEditorKit() throws RtfConverterException {
    RtfConverter converter = RtfConverter.getConverterForType(RtfConverter.CONVERTER_TYPE_JEDITORPANE);
    String converterName = RtfConverter.ALL_CONVERTER_TYPES[RtfConverter.CONVERTER_TYPE_JEDITORPANE];
    for (String value : RtfTestData.ALL_CANDIDATES) {
      logger.info(converterName + " --- " + value);
      logger.info("........................................... ");
      String convertedToHtml = converter.convertToHtml(value);
      logger.info(converterName + " --- " + convertedToHtml);
      assertFalse(convertedToHtml.contains("\\"));
      logger.info("==========================================================");
    }
  }

  @Test
  void convertToHtmlEditorKitSpacePeresist() throws RtfConverterException {
    RtfConverter converter = RtfConverter.getConverterForType(RtfConverter.CONVERTER_TYPE_JEDITORPANE);
    String convertedToHtml = converter.convertToHtml(VALUE_16);
    assertFalse(convertedToHtml.contains("\\"));
    assertTrue(convertedToHtml.contains(" leer."));

  }
}
