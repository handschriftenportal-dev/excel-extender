package de.staatsbibliothek.berlin.oxygen.lang;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 28.02.24
 */
public class Messages {

  public static final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages", Locale.GERMAN);

  public static String getMessage(String key) {
    if (MESSAGES.containsKey(key)) {
      return MESSAGES.getString(key);
    }
    return key;
  }

}
