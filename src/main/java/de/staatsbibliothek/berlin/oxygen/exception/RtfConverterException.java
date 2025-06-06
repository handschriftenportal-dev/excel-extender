package de.staatsbibliothek.berlin.oxygen.exception;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 26.02.25
 */
public class RtfConverterException extends Exception {

  private static final long serialVersionUID = 7368039663660543641L;

  public RtfConverterException() {
  }

  public RtfConverterException(String message) {
    super(message);
  }

  public RtfConverterException(String message, Throwable cause) {
    super(message, cause);
  }

  public RtfConverterException(Throwable cause) {
    super(cause);
  }

  public RtfConverterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
