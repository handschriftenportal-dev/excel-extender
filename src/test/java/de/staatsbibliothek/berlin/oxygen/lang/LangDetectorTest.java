package de.staatsbibliothek.berlin.oxygen.lang;

import static de.staatsbibliothek.berlin.oxygen.ner.NERTestData.TEXT_CLEAN;
import static de.staatsbibliothek.berlin.oxygen.ner.NERTestData.TEXT_TO_ANALYZE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 27.02.24
 */
class LangDetectorTest {

  public static final String TEXT_CATALA = "Aquest article tracta sobre l'idioma. Vegeu-ne altres significats a";
  public static final String GERMAN = Messages.getMessage("GERMAN");
  public static final String LATIN = Messages.getMessage("LATIN");
  public static final String CATALAN = Messages.getMessage("CATALAN");
  public static final String POLISH = Messages.getMessage("POLISH");
  public static final String ENGLISH = Messages.getMessage("ENGLISH");


  @Test
  void testDetectLanguage() {
    assertEquals(GERMAN, LangDetector.detectLanguage(TEXT_CLEAN));
    assertEquals(LATIN, LangDetector.detectLanguage("Theoremata L de corpore Christi"));
    assertEquals(LATIN, LangDetector.detectLanguage(TEXT_TO_ANALYZE));
    assertEquals(ENGLISH, LangDetector.detectLanguage("which way now"));
    assertEquals(CATALAN, LangDetector.detectLanguage(TEXT_CATALA));
    assertEquals(POLISH, LangDetector.detectLanguage("co to znaczy"));
  }
}

