package de.staatsbibliothek.berlin.saxon.extension;

import de.staatsbibliothek.berlin.saxon.call.Rtf2NodeCall;
import de.staatsbibliothek.berlin.saxon.constant.ExtensionConstants;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 15.11.24
 */
public class Rtf2NodeDefinition extends ExtensionFunctionDefinition {

  private static final Rtf2NodeCall RTF_2_NODE_CALL = new Rtf2NodeCall();
  private static final StructuredQName RTF_2_NODE_STRUCTURED_Q_NAME = new StructuredQName(
      ExtensionConstants.PREFIX,
      ExtensionConstants.NAMESPACE,
      ExtensionConstants.RTF_2_NODE);

  public static final SequenceType[] ARGUMENT_SEQUENCE_TYPES = {SequenceType.SINGLE_STRING,
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
    return RTF_2_NODE_STRUCTURED_Q_NAME;
  }

  @Override
  public SequenceType[] getArgumentTypes() {
    return ARGUMENT_SEQUENCE_TYPES;
  }

  @Override
  public SequenceType getResultType(SequenceType[] sequenceTypes) {
    return SequenceType.SINGLE_NODE;
  }

  @Override
  public ExtensionFunctionCall makeCallExpression() {
    return RTF_2_NODE_CALL;
  }
}
