package de.staatsbibliothek.berlin.saxon.call;

import static de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter.ALL_CONVERTER_TYPES;
import static de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter.CONVERTER_TYPE_JEDITORPANE;

import de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import org.jetbrains.annotations.NotNull;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 26.02.25
 */
public abstract class RtfConverterCall extends ExtensionFunctionCall {

  public static final String URN_FROM_STRING = "urn:from-string";
  public static final int MAX_CALL_PARAMETERS_COUNT = 2;
  public static final int CALL_WITHOUT_PARAMETER = 0;
  public static final String RTF_TEXT_REQUIRED = "rtfText - required,\n ";

  static RtfConverter getRtfConverter(Sequence[] parameters) throws XPathException {
    return getRtfConverter(parameters, 1, RTF_TEXT_REQUIRED);
  }

  static RtfConverter getRtfConverter(Sequence[] arguments, int parameterIndex, String supportedArguments)
      throws XPathException {
    int converterType = CONVERTER_TYPE_JEDITORPANE;
    if (arguments.length > parameterIndex) {
      String converterTypeAsString = arguments[parameterIndex].head().getStringValue();
      if (converterTypeAsString != null) {
        try {
          converterType = Integer.parseInt(converterTypeAsString);
        } catch (Exception e) {
          String message = getErrorMessage("Unknown converter type: '" + converterTypeAsString + "'",
              supportedArguments);
          throw new XPathException(message);
        }
      }
    }

    RtfConverter rtfConverter = RtfConverter.getConverterForType(converterType);
    return rtfConverter;
  }


  static void checkParameters(Sequence[] arguments, String extensionName) throws XPathException {
    if (arguments == null
        || arguments.length == CALL_WITHOUT_PARAMETER
        || arguments.length > MAX_CALL_PARAMETERS_COUNT) {
      String message = getErrorMessage(extensionName + ": Invalid number of arguments!", RTF_TEXT_REQUIRED);
      throw new XPathException(message);
    }
  }

  @NotNull
  static String getErrorMessage(String reason, String supportedArguments) {
    StringBuilder stringBuilder = new StringBuilder(512);
    stringBuilder.append(reason).append("\n ")
        .append("Supported arguments:\n ")
        .append(supportedArguments);
    addConverterTypeArgumentDescrition(stringBuilder);
    return stringBuilder.toString();
  }

  static void addConverterTypeArgumentDescrition(StringBuilder stringBuilder) {
    stringBuilder.append("converterType - optional: (\n ");
    for (int i = 1; i < ALL_CONVERTER_TYPES.length; i++) {
      if (i > 1) {
        stringBuilder.append(",\n ");
      }
      stringBuilder.append(i).append(" - ").append(ALL_CONVERTER_TYPES[i]);
    }
    stringBuilder.append("\n)");
  }
}
