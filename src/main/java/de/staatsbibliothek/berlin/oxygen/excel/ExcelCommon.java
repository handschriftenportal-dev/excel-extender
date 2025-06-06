package de.staatsbibliothek.berlin.oxygen.excel;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.CellType;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 28.01.20
 */
public class ExcelCommon {

  public static final Pattern PATTERN_DOUBLE_PIPE = Pattern.compile("\\|\\|");
  public static final String LINE_SEPARATOR = System.lineSeparator();

  public static final String SPACE = " ";
  public static final String LINE_BREAK = "\n";
  public static final String EMPTY_STRING = "";
  public static final Map<Pattern, String> XML_ENCODED_VALUE_DECODER = new HashMap<>(7);
  public static final String CARET_RETURN = "\r";
  public static final int COLUMN_KEY_WIDTH = 12000;
  public static final int COLUMN_VALUE_WIDTH = 46000;

  private static final Pattern HTML_LINE_SEPARATOR = Pattern.compile("<(BR|br)[^>]*>|~");
  private static final Pattern MAIL_WEB_LINK = Pattern.compile("mlink:|wlink:");
  private static final Pattern HTML_SPACE = Pattern.compile("&nbsp;");
  private static final Pattern HTML_TAGS = Pattern.compile("<[^>]*>");

  static {
    XML_ENCODED_VALUE_DECODER.put(Pattern.compile("&quot;|&#34;"), "\"");
    XML_ENCODED_VALUE_DECODER.put(Pattern.compile("&lt;"), "<");
    XML_ENCODED_VALUE_DECODER.put(Pattern.compile("&gt;"), ">");
    XML_ENCODED_VALUE_DECODER.put(Pattern.compile("&eq;"), "=");
    XML_ENCODED_VALUE_DECODER.put(Pattern.compile("&#xD;|&#xd;|_x000D_|&#13;"), CARET_RETURN);
    XML_ENCODED_VALUE_DECODER.put(Pattern.compile("&#xA;|&#xa;|_x000A_|&#10;"), LINE_BREAK);
  }

  static Path resolvePathFromString(String pathToResolve) {
    Path result;
    try {
      result = Paths.get(new URL(pathToResolve).toURI());
    } catch (MalformedURLException | URISyntaxException e) {
      result = Paths.get(pathToResolve);
    }
    return result;
  }

  static String getExceptionStacktraceAsString(Exception ex) {
    StringWriter sw = new StringWriter();
    try (PrintWriter pw = new PrintWriter(sw)) {
      ex.printStackTrace(pw);
      return sw.getBuffer().toString();
    }
  }

  static void parseDataTypeList(String datatypeListStr, Map<String, CellType> dataTypeListMap) {
    if (!datatypeListStr.isEmpty()) {
      for (String line : PATTERN_DOUBLE_PIPE.split(datatypeListStr)) {
        parseDataTypeLine(dataTypeListMap, line);
      }
    }
  }

  static void parseDataTypeLine(Map<String, CellType> dataTypeListMap, String line) {
    String[] dataTypeListValues = line.split(";");
    if (dataTypeListValues.length > 1) {
      String key = dataTypeListValues[0];
      CellType cellType = parseCellType(dataTypeListValues[1]);
      if (!key.isEmpty()) {
        dataTypeListMap.put(key, cellType);
      }
    }
  }

  static void parseDropDownList(String dropDownListStr, Map<String, String[]> dropDownListMap) {
    if (!dropDownListStr.isEmpty()) {
      for (String line : PATTERN_DOUBLE_PIPE.split(dropDownListStr)) {
        parseDropDownLine(dropDownListMap, line);
      }
    }
  }

  static void parseDropDownLine(Map<String, String[]> dropDownListMap, String line) {
    boolean first = true;
    String key = null;
    String[] dropDownListValues = line.split(";");
    List<String> values = new ArrayList<>(dropDownListValues.length);
    for (String value : dropDownListValues) {
      if (first) {
        key = value;
        first = false;
      } else {
        values.add(value);
      }
    }
    if ((key != null) && !key.isEmpty() && !values.isEmpty()) {
      dropDownListMap.put(key, values.toArray(new String[0]));
    }
  }

  static CellType parseCellType(String cellTypeAsString) {
    if (cellTypeAsString == null) {
      return CellType.STRING;
    }
    String upperCaseCellTypeAsString = cellTypeAsString.toUpperCase();
    CellType result = CellType.valueOf(upperCaseCellTypeAsString);
    if (result == CellType._NONE) {
      result = CellType.STRING;
    }
    return result;
  }

  static String removeHTMLTagsFromCellValue(String lineCells) {
    String valueString =
        HTML_TAGS.matcher(
            HTML_SPACE.matcher(
                MAIL_WEB_LINK.matcher(
                    HTML_LINE_SEPARATOR.matcher(lineCells).replaceAll(LINE_BREAK)
                ).replaceAll(EMPTY_STRING)
            ).replaceAll(SPACE)
        ).replaceAll(EMPTY_STRING);
    return valueString;
  }
}
