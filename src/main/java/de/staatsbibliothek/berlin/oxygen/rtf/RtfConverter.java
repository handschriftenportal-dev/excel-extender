package de.staatsbibliothek.berlin.oxygen.rtf;

import de.staatsbibliothek.berlin.oxygen.exception.RtfConverterException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Provides unified RTF converter
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 26.02.25
 */
public interface RtfConverter {

  Logger logger = Logger.getLogger(RtfConverter.class.getName());

  String TEXT_RTF = "text/rtf";
  String TEXT_PLAIN = "text/plain";
  String TEXT_HTML = "text/html";

  String SUPER_BEGIN = "\\super\\";
  String SCAPS_BEGIN = "\\scaps\\";
  String CAPS_BEGIN = "\\caps\\";
  String SUB_BEGIN = "\\sub\\)";
  String STRIKE_BEGIN = "\\strike\\)";
  String OUTLINE_BEGIN = "\\outl\\)";
  String UNDERLINE_BEGIN = "\\ul\\)";

  String SUPER_END = "\\super0";
  String SCAPS_END = "\\scaps0";
  String CAPS_END = "\\caps0";
  String SUB_END = "\\sub0";
  String STRIKE_END = "\\strike0";
  String OUTLINE_END = "\\outl0";
  String UNDERLINE_END = "\\ul0)";

  String TAG_END_PLAIN = "\\plain";
  String TAG_PAR_END = "\\par";
  String TAG_LINE_END = "\\line";
  String TAG_FLD_END = "}";
  String TAG_FLD_BEGIN_END = "{";
  String HTML_SUPER_BEGIN = "<sup>";
  String HTML_SUPER_END = "</sup>";
  String HTML_SUB_BEGIN = "<sub>";
  String HTML_SUB_END = "</sub>";
  String HTML_SCAPS_BEGIN = "<h6>";
  String HTML_SCAPS_END = "</h6>";
  String HTML_CAPS_BEGIN = "<h4>";
  String HTML_CAPS_END = "</h4>";
  String HTML_STRIKE_BEGIN = "<s>";
  String HTML_STRIKE_END = "</s>";
  String HTML_OUTLINE_BEGIN = "<ins>";
  String HTML_OUTLINE_END = "</ins>";
  String HTML_UNDERLINE_BEGIN = "<u>";
  String HTML_UNDERLINE_END = "</u>";

  RTFTag TAG_SUPER = new RTFTag(SUPER_BEGIN, HTML_SUPER_BEGIN, HTML_SUPER_END, SUPER_END, TAG_END_PLAIN, TAG_PAR_END,
      TAG_FLD_END, TAG_FLD_BEGIN_END, TAG_LINE_END);
  RTFTag TAG_SCAPS = new RTFTag(SCAPS_BEGIN, HTML_SCAPS_BEGIN, HTML_SCAPS_END, SCAPS_END, TAG_END_PLAIN, TAG_PAR_END,
      TAG_FLD_END, TAG_FLD_BEGIN_END, TAG_LINE_END);
  RTFTag TAG_CAPS = new RTFTag(CAPS_BEGIN, HTML_CAPS_BEGIN, HTML_CAPS_END, CAPS_END, TAG_END_PLAIN, TAG_PAR_END, TAG_FLD_END,
      TAG_FLD_BEGIN_END, TAG_LINE_END);
  RTFTag TAG_SUB = new RTFTag(SUB_BEGIN, HTML_SUB_BEGIN, HTML_SUB_END, SUB_END, TAG_END_PLAIN, TAG_PAR_END, TAG_FLD_END,
      TAG_FLD_BEGIN_END, TAG_LINE_END);
  RTFTag TAG_STRIKE = new RTFTag(STRIKE_BEGIN, HTML_STRIKE_BEGIN, HTML_STRIKE_END, STRIKE_END, TAG_END_PLAIN, TAG_PAR_END,
      TAG_FLD_END, TAG_FLD_BEGIN_END, TAG_LINE_END);
  RTFTag TAG_OUTLINE = new RTFTag(OUTLINE_BEGIN, HTML_OUTLINE_BEGIN, HTML_OUTLINE_END, OUTLINE_END, TAG_END_PLAIN,
      TAG_PAR_END, TAG_FLD_END, TAG_FLD_BEGIN_END, TAG_LINE_END);
  RTFTag TAG_UNDERLINE = new RTFTag(UNDERLINE_BEGIN, HTML_UNDERLINE_BEGIN, HTML_UNDERLINE_END, UNDERLINE_END,
      TAG_END_PLAIN,
      TAG_PAR_END, TAG_FLD_END, TAG_FLD_BEGIN_END, TAG_LINE_END);
  List<RTFTag> RTF_TAGS = Arrays.asList(TAG_SUPER, TAG_SCAPS, TAG_CAPS, TAG_SUB, TAG_STRIKE, TAG_OUTLINE,
      TAG_UNDERLINE);

  String RTF_HYPERLINK_BEGIN = "{\\field{\\*\\fldinst HYPERLINK ";
  int RTF_HYPERLINK_BEGIN_LENGTH = RTF_HYPERLINK_BEGIN.length();
  Pattern QUTATION_LINK = Pattern.compile("[ \"\n\r'\\\\]|(&#34;)|(&x22;)|(&#x22;)|(&quot;)|(&amp;quot;)");
  String A_HREF_BEGIN_TAG = "<a href=\"";
  String A_HREF_END_TAG = "</a>";

  int CONVERTER_TYPE_JEDITORPANE = 1;
  int CONVERTER_TYPE_TIKA_RTF = 2;
  int CONVERTER_TYPE_TIKA_AUTO = 3;
  String[] ALL_CONVERTER_TYPES = {"default", "JEditorPane", "TikaRTF (default)", "TikaAuto"};
  String RTF_CP_ANSICPG = "\\ansicpg";
  String UTF_8 = "utf-8";
  String ISO_8859_1 = "iso-8859-1";

  Pattern PATTERN_CLASS_WITHOUT_QUOTATION = Pattern.compile("<p class=[a-zA-Z]+>");
  String CLASS_WITH_QUOTATION = "<p>";

  Pattern PATTERN_WHITESPACE_BETWEEN_TAGS = Pattern.compile("(?<=[>])\\s{2,}|\\s{2,}(?=[<])");
  Pattern PATTERN_WHITESPACE_BETWEEN_TAGS_POST = Pattern.compile("(?<=[>])\\s+(?=[<])");
  Pattern PATTERN_MORE_THEN_ONE_WHITESPACE = Pattern.compile("^\\s+|\\s+$|\\s+(?=\\s)");
  Pattern PATTERN_GREATER_THEN = Pattern.compile(">");
  Pattern PATTERN_LESS_THEN = Pattern.compile("<");
  String REPLACEMENT_GREATER_THEN = "&gt";
  String REPLACEMENT_LESS_THEN = "&lt;";
  Pattern PATTERN_SEMICOLON = Pattern.compile("(&#x3b;)");
  Pattern PATTERN_AMPERSAND = Pattern.compile("&(?!\\w+;)");
  Pattern PATTERN_EMPTY_LINES = Pattern.compile("(&#x0d;)|(&#x0a;)|[\\t\\n\\r]");
  String REPLACEMENT_AMPERSAND = "&amp;";
  String START_FLDRSLT_PLAIN = "\\fldrslt \\plain ";
  int START_FLDRSLT_PLAIN_LENGTH = START_FLDRSLT_PLAIN.length();


  String convertToMime(String rtfString, String outputMime) throws RtfConverterException;

  default String convertToText(String rtfString) throws RtfConverterException {
    return convertToMime(rtfString, TEXT_PLAIN);
  }

  default String convertToHtml(String rtfString) throws RtfConverterException {
    String result = convertToMime(rtfString, TEXT_HTML);
    result = postprocessResult(result);
    return result;
  }

  static RtfConverter getConverterForType(int converterType) {
    if (converterType == CONVERTER_TYPE_JEDITORPANE) {
      return new Rtf2MimeType();
    }
    return new Rtf2MimeTypeTika(converterType);
  }

  static String encodeSpecialCharacters(String rtfString) throws RtfConverterException {
    if (rtfString == null || rtfString.isEmpty()) {
      return rtfString;
    }
    return PATTERN_LESS_THEN.matcher(
            PATTERN_GREATER_THEN.matcher(rtfString)
                .replaceAll(REPLACEMENT_GREATER_THEN))
        .replaceAll(REPLACEMENT_LESS_THEN);
  }

  default String getCharacterEncoding(String rtfString) {
    if (rtfString == null) {
      return ISO_8859_1;
    }
    if (rtfString.contains(RTF_CP_ANSICPG)) {
      int index = rtfString.indexOf(RTF_CP_ANSICPG);
      if (index != -1) {
        String encoding = rtfString.substring(index + RTF_CP_ANSICPG.length());
        index = encoding.indexOf((int) '\\');
        if (index != -1) {
          encoding = encoding.substring(0, index);
          if ("0".equals(encoding)) {
            return ISO_8859_1;
          }
          return "Cp" + encoding;
        }
      }
    }
    return ISO_8859_1;
  }

  default String prepareRtf(String rtfString, boolean withHyperlinks) throws RtfConverterException {
    if (rtfString == null) {
      return rtfString;
    }
    rtfString = encodeSpecialCharacters(rtfString);
    rtfString = prepareRTFTags(rtfString);
    if (withHyperlinks) {
      rtfString = prepareRtfHyperlinks(rtfString);
    }
    logger.log(Level.FINE, "prepared: {0}", rtfString);
    return rtfString;
  }

  default String postprocessResult(String result) {
    result = PATTERN_EMPTY_LINES.matcher(result).replaceAll("");
    result = PATTERN_WHITESPACE_BETWEEN_TAGS.matcher(result).replaceAll(" ");
    result = PATTERN_WHITESPACE_BETWEEN_TAGS_POST.matcher(result).replaceAll("");
    result = PATTERN_MORE_THEN_ONE_WHITESPACE.matcher(result).replaceAll(" ");
    result = PATTERN_AMPERSAND.matcher(result).replaceAll(REPLACEMENT_AMPERSAND);
    result = PATTERN_SEMICOLON.matcher(result).replaceAll(";");
    result = PATTERN_CLASS_WITHOUT_QUOTATION.matcher(result).replaceAll(CLASS_WITH_QUOTATION);
    return result;
  }

  static String prepareRTFTags(String rtf) {
    StringBuilder result = new StringBuilder(rtf.length());
    for (RTFTag tag : RTF_TAGS) {
      result.delete(0, result.length());
      prepareRTFTag(result, rtf, tag);
      rtf = result.toString();
    }
    return rtf;
  }

  static String prepareRtfHyperlinks(String rtf) {
    //{\field{\*\fldinst HYPERLINK &#34;http://5060[0].5064/?u&#34;}{\fldrslt \plain \f1\fs24\chcbpat17 15. Jh.}}
    StringBuilder result = new StringBuilder(rtf.length());
    int begin = rtf.indexOf(RTF_HYPERLINK_BEGIN);
    if (begin == -1) {
      return rtf;
    }

    result.append(rtf.substring(0, begin));

    String rest = rtf.substring(begin + RTF_HYPERLINK_BEGIN_LENGTH);
    int hyperlinkEnd = rest.indexOf((int) '}');

    String linkAddress = rest.substring(0, hyperlinkEnd);
    String hyperlink = QUTATION_LINK.matcher(linkAddress).replaceAll("");

    int startAfterLink = hyperlinkEnd + 2;
    if (rest.length() > startAfterLink) {
      rest = rest.substring(startAfterLink);
    }
    int doubleParenthesis = rest.indexOf("}}");
    if (doubleParenthesis == -1) {
      logger.severe(
          "RTF hyperlink " + hyperlink + " with the target address " + linkAddress + "  does not contain '}}'\n"
              + rest);
      result.append(rest);
    } else {
      String linkText = rest.substring(0, doubleParenthesis);
      int startFormatCmd = linkText.indexOf(START_FLDRSLT_PLAIN);
      if (startFormatCmd != -1) {
        linkText = linkText.substring(startFormatCmd + START_FLDRSLT_PLAIN_LENGTH);
        startFormatCmd = linkText.indexOf((int) ' ');
        if (startFormatCmd != -1) {
          startFormatCmd++;
          String linkFormatting = linkText.substring(0, startFormatCmd);
          if (!linkFormatting.contains(TAG_END_PLAIN)) {
            result.append(TAG_END_PLAIN);
          }
          result.append(linkFormatting);
          linkText = linkText.substring(startFormatCmd);
        }
      }
      result.append(A_HREF_BEGIN_TAG);
      result.append(hyperlink);
      result.append("\">");
      result.append(linkText);

      String endLinkField = rest.substring(doubleParenthesis + 2);
      result.append(A_HREF_END_TAG).append(' ');
      result.append(endLinkField);
    }
    return prepareRtfHyperlinks(result.toString());
  }

  // omit \plain \f1\b\scaps\lang1033\fs24   \line \plain
  static void prepareRTFTag(StringBuilder result, String rtf, RTFTag tag) {
    int begin = rtf.indexOf(tag.getTagBegin());
    if (begin == -1) {
      result.append(rtf);
      return;
    }
    begin = rtf.indexOf((int) ' ', begin);
    if (begin == -1) {
      result.append(rtf);
      return;
    }
    begin++;
    result.append(rtf.substring(0, begin));
    int end = getTagEnd(rtf, tag, begin);
    if (end == -1) {
      result.append(rtf.substring(begin));
      return;
    }
    result.append(tag.getHtmlReplacementBegin());
    String formattedContent = rtf.substring(begin, end);
    result.append(formattedContent);
    result.append(tag.getHtmlReplacementEnd());

    prepareRTFTag(result, rtf.substring(end), tag);
  }

  static int getTagEnd(String rtf, RTFTag rtfTag, int begin) {
    int end = -1;
    int end2;
    for (String endTag : rtfTag.getTagEnds()) {
      end2 = rtf.indexOf(endTag, begin);
      if (end2 != -1) {
        if (end == -1) {
          end = end2;
        }
        end = Math.min(end, end2);
      }
    }
    return end;
  }
}
