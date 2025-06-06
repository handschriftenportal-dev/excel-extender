package de.staatsbibliothek.berlin.oxygen.rtf;

import static de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter.TEXT_HTML;
import static de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter.TEXT_PLAIN;
import static de.staatsbibliothek.berlin.oxygen.rtf.RtfTestData.ALL_CANDIDATES;
import static org.junit.jupiter.api.Assertions.assertFalse;

import de.staatsbibliothek.berlin.oxygen.exception.RtfConverterException;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 14.03.25
 */
class Rtf2MimeTypeTikaTest {

  private static final Logger logger = Logger.getLogger(Rtf2MimeTypeTikaTest.class.getName());

  @Test
  void testConvertToMimeAutoText() throws RtfConverterException {
    testConvertToMime(RtfConverter.CONVERTER_TYPE_TIKA_AUTO, TEXT_PLAIN);
  }

  @Test
  void testConvertToMimeRtfText() throws RtfConverterException {
    testConvertToMime(RtfConverter.CONVERTER_TYPE_TIKA_RTF, TEXT_PLAIN);
  }

  @Test
  void testConvertToMimeAutoHtml() throws RtfConverterException {
    testConvertToMime(RtfConverter.CONVERTER_TYPE_TIKA_AUTO, TEXT_HTML);
  }

  @Test
  void testConvertToHtmlRtfHtml() throws RtfConverterException {
    testConvertToMime(RtfConverter.CONVERTER_TYPE_TIKA_RTF, TEXT_HTML);
  }

  void testConvertToMime(int coverterType, String mime) throws RtfConverterException {
    RtfConverter converter = new Rtf2MimeTypeTika(coverterType);
    String converterName = RtfConverter.ALL_CONVERTER_TYPES[coverterType];
    for (String value : ALL_CANDIDATES) {
      logger.info(converterName + " --- " + value);
      logger.info("........................................... ");
      String convertedToHtml = converter.convertToMime(value, mime);
      logger.info(converterName + " --- " + convertedToHtml);
      assertFalse(convertedToHtml.contains("\\"));
      logger.info("==========================================================");
    }
  }
}
