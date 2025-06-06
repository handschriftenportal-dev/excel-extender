package de.staatsbibliothek.berlin.saxon.call;

import static de.staatsbibliothek.berlin.saxon.constant.ExtensionConstants.RTF_2_HTML;

import de.staatsbibliothek.berlin.oxygen.exception.RtfConverterException;
import de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;

/**
 * Covert rtf to html
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 23.10.24
 */
public class Rtf2HtmlCall extends RtfConverterCall {

  private static final Logger logger = Logger.getLogger(Rtf2HtmlCall.class.getName());

  public static final int MIN_CALL_ARGUMENTS_COUNT = 1;
  public static final int MAX_CALL_ARGUMENTS_COUNT = 3;
  private static final String SUPPORTED_ARGUMENTS_DESCRIPTION = "rtfText - required,\n onlyHtmlBodyContent - optional ('true', 'false' (default)),\n ";
  private static final Pattern HTML_CONTENT = Pattern.compile("(?s)<body[^>]*>(.*)<\\/body>");
  int converterTypeIndex = 2;

  @Override
  public Sequence call(XPathContext xPathContext, Sequence[] arguments) throws XPathException {
    if (arguments == null
        || arguments.length < MIN_CALL_ARGUMENTS_COUNT
        || arguments.length > MAX_CALL_ARGUMENTS_COUNT) {
      StringBuilder reason = new StringBuilder()
          .append(RTF_2_HTML)
          .append(": Invalid number of arguments!\n Supported arguments:\n ")
          .append(SUPPORTED_ARGUMENTS_DESCRIPTION);
      addConverterTypeArgumentDescrition(reason);
      throw new XPathException(reason.toString());
    }
    String result = "";
    try {
      String textToConvert = arguments[0].head().getStringValue();
      int converterTypeIndex = 2;
      boolean onlyHtmlBodyContent = false;
      if (arguments.length > 1) {
        String stringValue = arguments[1].head().getStringValue();
        try {
          Integer.parseInt(stringValue);
          converterTypeIndex = 1;
        } catch (NumberFormatException e) {
          onlyHtmlBodyContent = Boolean.parseBoolean(stringValue);
        }
      }

      RtfConverter rtfConverter = getRtfConverter(arguments, converterTypeIndex, SUPPORTED_ARGUMENTS_DESCRIPTION);
      result = rtfConverter.convertToHtml(textToConvert);
      if (onlyHtmlBodyContent) {
        Matcher bodyContent = HTML_CONTENT.matcher(result);
        if (bodyContent.find()) {
          return StringValue.makeStringValue(bodyContent.group(1));
        }
      }

      return StringValue.makeStringValue(result);
    } catch (ArrayIndexOutOfBoundsException | RtfConverterException e) {
      logger.log(Level.SEVERE, e.getMessage() + "\n" + result, e);
      throw new XPathException(e.getMessage(), e);
    }
  }
}
