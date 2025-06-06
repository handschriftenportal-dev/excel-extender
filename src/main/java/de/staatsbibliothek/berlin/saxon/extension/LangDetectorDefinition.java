package de.staatsbibliothek.berlin.saxon.extension;

import de.staatsbibliothek.berlin.saxon.call.LangDetectorCall;
import de.staatsbibliothek.berlin.saxon.constant.ExtensionConstants;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;

/**
 * Detects language base on the provided string
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 23.09.24
 */
public class LangDetectorDefinition extends ExtensionFunctionDefinition {

  private static final LangDetectorCall LANG_DETECTOR_CALL = new LangDetectorCall();
  private static final StructuredQName LANG_DETECTOR_STRUCTURED_Q_NAME = new StructuredQName(ExtensionConstants.PREFIX,
      ExtensionConstants.NAMESPACE, ExtensionConstants.DETECT_LANGUAGE);
  private static final SequenceType[] ARGUMENT_SEQUENCE_TYPES = {SequenceType.SINGLE_STRING};

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
    return LANG_DETECTOR_STRUCTURED_Q_NAME;
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
    return LANG_DETECTOR_CALL;
  }
}
