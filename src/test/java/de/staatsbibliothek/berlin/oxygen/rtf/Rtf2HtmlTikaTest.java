package de.staatsbibliothek.berlin.oxygen.rtf;

import static de.staatsbibliothek.berlin.oxygen.rtf.RtfTestData.ALL_CANDIDATES;
import static org.junit.jupiter.api.Assertions.assertFalse;

import de.staatsbibliothek.berlin.oxygen.exception.RtfConverterException;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 29.10.24
 */
class Rtf2HtmlTikaTest {

  private static final Logger logger = Logger.getLogger(Rtf2HtmlTikaTest.class.getName());

  @Test
  void testConvertToHtmlAuto() throws RtfConverterException {
    testConvertToHtml(RtfConverter.CONVERTER_TYPE_TIKA_AUTO);
  }

  @Test
  void testConvertToHtmlRtf() throws RtfConverterException {
    testConvertToHtml(RtfConverter.CONVERTER_TYPE_TIKA_RTF);
  }

  void testConvertToHtml(int coverterType) throws RtfConverterException {
    RtfConverter converter = RtfConverter.getConverterForType(coverterType);
    String converterName = RtfConverter.ALL_CONVERTER_TYPES[coverterType];
    for (String value : ALL_CANDIDATES) {
      logger.info(converterName + " --- " + value);
      logger.info("........................................... ");
      String convertedToHtml = converter.convertToHtml(value);
      logger.info(converterName + " --- " + convertedToHtml);
      assertFalse(convertedToHtml.contains("\\"));
      logger.info("==========================================================");
    }
  }
}
