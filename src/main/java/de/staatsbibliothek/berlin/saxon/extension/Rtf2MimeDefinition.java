package de.staatsbibliothek.berlin.saxon.extension;

import de.staatsbibliothek.berlin.saxon.call.Rtf2MimeCall;
import de.staatsbibliothek.berlin.saxon.constant.ExtensionConstants;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;

/**
 * Decode RTF to HTML with help of JEditor
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 19.09.24
 */
public class Rtf2MimeDefinition extends ExtensionFunctionDefinition {

  private static final Rtf2MimeCall RTF_2_MIME_CALL = new Rtf2MimeCall();
  private static final StructuredQName RTF_2_MIME_STRUCTURED_Q_NAME = new StructuredQName(ExtensionConstants.PREFIX,
      ExtensionConstants.NAMESPACE, ExtensionConstants.RTF_2_MIME);
  public static final SequenceType[] ARGUMENT_SEQUENCE_TYPES = {SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING,
      SequenceType.OPTIONAL_STRING};

  @Override
  public int getMinimumNumberOfArguments() {
    return ExtensionConstants.MINIMUM_NUMBERS_OF_ARGUMENTS;
  }

  @Override
  public int getMaximumNumberOfArguments() {
    return ARGUMENT_SEQUENCE_TYPES.length + 1;
  }

  @Override
  public StructuredQName getFunctionQName() {
    return RTF_2_MIME_STRUCTURED_Q_NAME;
  }

  @Override
  public SequenceType[] getArgumentTypes() {
    return ARGUMENT_SEQUENCE_TYPES;
  }

  @Override
  public SequenceType getResultType(SequenceType[] sequenceTypes) {
    return SequenceType.SINGLE_STRING;
  }

  @Override
  public ExtensionFunctionCall makeCallExpression() {
    return RTF_2_MIME_CALL;
  }
}
