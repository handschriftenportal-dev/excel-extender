package de.staatsbibliothek.berlin.oxygen.lang;

import static com.github.pemistahl.lingua.api.Language.ARABIC;
import static com.github.pemistahl.lingua.api.Language.ARMENIAN;
import static com.github.pemistahl.lingua.api.Language.CATALAN;
import static com.github.pemistahl.lingua.api.Language.CZECH;
import static com.github.pemistahl.lingua.api.Language.DANISH;
import static com.github.pemistahl.lingua.api.Language.DUTCH;
import static com.github.pemistahl.lingua.api.Language.ENGLISH;
import static com.github.pemistahl.lingua.api.Language.FRENCH;
import static com.github.pemistahl.lingua.api.Language.GEORGIAN;
import static com.github.pemistahl.lingua.api.Language.GERMAN;
import static com.github.pemistahl.lingua.api.Language.GREEK;
import static com.github.pemistahl.lingua.api.Language.HEBREW;
import static com.github.pemistahl.lingua.api.Language.HINDI;
import static com.github.pemistahl.lingua.api.Language.HUNGARIAN;
import static com.github.pemistahl.lingua.api.Language.ITALIAN;
import static com.github.pemistahl.lingua.api.Language.LATIN;
import static com.github.pemistahl.lingua.api.Language.POLISH;
import static com.github.pemistahl.lingua.api.Language.PORTUGUESE;
import static com.github.pemistahl.lingua.api.Language.ROMANIAN;
import static com.github.pemistahl.lingua.api.Language.RUSSIAN;
import static com.github.pemistahl.lingua.api.Language.SERBIAN;
import static com.github.pemistahl.lingua.api.Language.SPANISH;
import static com.github.pemistahl.lingua.api.Language.SWEDISH;
import static com.github.pemistahl.lingua.api.Language.TURKISH;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;

/**
 * Provides textual language detection
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 27.02.24
 */
public class LangDetector {

  public static final LanguageDetector LANGUAGE_DETECTOR = LanguageDetectorBuilder
      .fromLanguages(LATIN, GERMAN, GREEK, CATALAN, POLISH, PORTUGUESE, HEBREW, ITALIAN, ENGLISH, ROMANIAN, SERBIAN,
          CZECH, TURKISH, HUNGARIAN, FRENCH, ARABIC, ARMENIAN, DANISH, GEORGIAN, DUTCH, HINDI, RUSSIAN, SPANISH,
          SWEDISH)
      .build();

  public static String detectLanguage(String textToCheck) {
    Language language = LANGUAGE_DETECTOR.detectLanguageOf(textToCheck);
    return Messages.getMessage(language.name()).toLowerCase();
  }

}
