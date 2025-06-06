package de.staatsbibliothek.berlin.oxygen.exception;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 16.09.24
 */
public class ExcelExtenderException extends Exception {

  private static final long serialVersionUID = 8693386830480740275L;

  public ExcelExtenderException() {
  }

  public ExcelExtenderException(String message) {
    super(message);
  }

  public ExcelExtenderException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExcelExtenderException(Throwable cause) {
    super(cause);
  }

  public ExcelExtenderException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
