package de.staatsbibliothek.berlin.saxon.call;

import static de.staatsbibliothek.berlin.saxon.constant.ExtensionConstants.RTF_2_MIME;

import de.staatsbibliothek.berlin.oxygen.exception.RtfConverterException;
import de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;

/**
 * Convert RTF to given mime type
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 20.09.24
 */
public class Rtf2MimeCall extends RtfConverterCall {

  private static final Logger logger = Logger.getLogger(Rtf2MimeCall.class.getName());

  public static final int MIN_CALL_ARGUMENTS_COUNT = 2;
  public static final int MAX_CALL_ARGUMENTS_COUNT = 3;
  private static final String SUPPORTED_ARGUMENTS_DESCRIPTION = "rtfText - required,\n mimeType - required,\n ";

  @Override
  public Sequence call(XPathContext xPathContext, Sequence[] arguments) throws XPathException {
    if (arguments == null || arguments.length < MIN_CALL_ARGUMENTS_COUNT
        || arguments.length > MAX_CALL_ARGUMENTS_COUNT) {
      StringBuilder reason = new StringBuilder(256)
          .append(RTF_2_MIME)
          .append(": Invalid number of arguments!\n Supported arguments:\n ")
          .append(SUPPORTED_ARGUMENTS_DESCRIPTION);
      addConverterTypeArgumentDescrition(reason);
      throw new XPathException(reason.toString());
    }
    String result = "";
    try {
      String textToConvert = arguments[CALL_WITHOUT_PARAMETER].head().getStringValue();
      String outputContentType = arguments[1].head().getStringValue();
      RtfConverter converter = getRtfConverter(arguments, 2, SUPPORTED_ARGUMENTS_DESCRIPTION);

      result = converter.convertToMime(textToConvert, outputContentType);
      return StringValue.makeStringValue(result);
    } catch (ArrayIndexOutOfBoundsException | RtfConverterException e) {
      logger.log(Level.SEVERE, e.getMessage() + "\n" + result, e);
      throw new XPathException(e.getMessage(), e);
    }
  }
}
