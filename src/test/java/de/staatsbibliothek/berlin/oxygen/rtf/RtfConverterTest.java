package de.staatsbibliothek.berlin.oxygen.rtf;

import static de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter.HTML_SUPER_BEGIN;
import static de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter.ISO_8859_1;
import static de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter.TAG_SUPER;
import static de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter.logger;
import static de.staatsbibliothek.berlin.oxygen.rtf.RtfTestData.VALUE_4;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.staatsbibliothek.berlin.oxygen.exception.RtfConverterException;
import org.junit.jupiter.api.Test;

/**
 * Test static RtfConverter methods
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 24.03.25
 */
class RtfConverterTest {

  @Test
  void testGetConverterForType() {
    assertSame(Rtf2MimeTypeTika.class,
        RtfConverter.getConverterForType(RtfConverter.CONVERTER_TYPE_TIKA_RTF).getClass());
    assertSame(Rtf2MimeType.class,
        RtfConverter.getConverterForType(RtfConverter.CONVERTER_TYPE_JEDITORPANE).getClass());
    assertSame(Rtf2MimeTypeTika.class,
        RtfConverter.getConverterForType(RtfConverter.CONVERTER_TYPE_TIKA_AUTO).getClass());
  }

  @Test
  void testEncodeSpecialCharacters() throws RtfConverterException {
    String testString = "<>>&lt;&gt;>";
    String encoded = RtfConverter.encodeSpecialCharacters(testString);
    assertNotNull(encoded);
    assertTrue(encoded.contains("&lt;"));
    assertTrue(encoded.contains("&gt;"));
    assertFalse(encoded.contains(">"));
    assertFalse(encoded.contains("<"));
  }

  @Test
  void testGetCharacterEncoding() {
    String characterEncoding = RtfConverter.getConverterForType(RtfConverter.CONVERTER_TYPE_JEDITORPANE)
        .getCharacterEncoding(RtfTestData.VALUE_1);
    assertNotNull(characterEncoding);
    assertEquals(ISO_8859_1, characterEncoding);
  }

  @Test
  void testPrepareRtf() {
    String testRTF = RtfTestData.VALUE_14;
    assertFalse(testRTF.contains(HTML_SUPER_BEGIN));
    String preparedRTFTags = RtfConverter.prepareRTFTags(testRTF);
    assertNotNull(preparedRTFTags);
    assertTrue(preparedRTFTags.contains(HTML_SUPER_BEGIN));
  }

  @Test
  void testPostprocessResult() {
    String convertedRTF = "<p class=default>&lt;&gt; &";
    String postproccessedRtf = RtfConverter.getConverterForType(RtfConverter.CONVERTER_TYPE_TIKA_AUTO)
        .postprocessResult(convertedRTF);
    assertNotNull(postproccessedRtf);
    assertFalse(postproccessedRtf.contains("class=default"));
    assertTrue(postproccessedRtf.contains("&amp;"));
    assertTrue(postproccessedRtf.contains("&lt;"));
    assertTrue(postproccessedRtf.contains("&gt;"));
    assertTrue(postproccessedRtf.contains("<p>"));
  }

  @Test
  void testPrepareRTFTags() {
    String testRTF = RtfTestData.VALUE_14;
    assertFalse(testRTF.contains(HTML_SUPER_BEGIN));
    String preparedRTFTags = RtfConverter.prepareRTFTags(testRTF);
    assertNotNull(preparedRTFTags);
    assertTrue(preparedRTFTags.contains(HTML_SUPER_BEGIN));
  }

  @Test
  void testPrepareRtfHyperlinks() {
    String prepareRtfHyperlinks = RtfConverter.prepareRtfHyperlinks(VALUE_4);
    assertNotNull(prepareRtfHyperlinks);
    logger.info(prepareRtfHyperlinks);
    assertTrue(prepareRtfHyperlinks.contains(RtfTestData.HREF_VALUE_4));
  }

  @Test
  void testPrepareRTFTag() {
    String testRTF = RtfTestData.VALUE_14;
    assertFalse(testRTF.contains(HTML_SUPER_BEGIN));
    StringBuilder result = new StringBuilder();
    RtfConverter.prepareRTFTag(result, testRTF, TAG_SUPER);
    String preparedRTFTags = result.toString();
    assertNotNull(preparedRTFTags);
    assertTrue(preparedRTFTags.contains(HTML_SUPER_BEGIN));
  }

  @Test
  void testGetTagEnd() {
    int expectedTagEnd = 392;
    String testRTF = "\\plain \\f1\\lang1033\\fs22\\cf1  S. 168&#x3b; mit 15 Anforderungen \\u252 \\'fcberliefert in Trier StB {\\field{\\*\\fldinst HYPERLINK &quot;hida://5007[1].501m/?u&quot;}{\\fldrslt \\plain \\f1\\lang1033\\fs22\\chcbpat17\\cf1 Hs. 1935/1432 4\\u176 \\'b0}}\\plain \\f1\\lang1033\\fs22\\cf1 ,16\\plain \\f1\\super\\lang1033\\fs24\\cf1 <sup>ra</sup>\\plain \\f1\\lang1033\\fs22\\cf1  s. \\plain \\f1\\scaps\\lang1033\\fs22\\cf1 Bushey\\plain \\f1\\lang1033\\fs22\\cf1  S. 221). \\par \\pard\\fi0\\li0\\ql\\ri0\\sb0\\sa0\\itap0 \\par}";
    int tagEnd = RtfConverter.getTagEnd(testRTF, RtfConverter.TAG_SUPER, 386);
    assertEquals(expectedTagEnd, tagEnd);
  }
}
