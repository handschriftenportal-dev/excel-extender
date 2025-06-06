package de.staatsbibliothek.berlin.saxon.extension;

import de.staatsbibliothek.berlin.saxon.call.NERServiceClientCall;
import de.staatsbibliothek.berlin.saxon.constant.ExtensionConstants;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 23.09.24
 */
public class NERServiceClientDefinition extends ExtensionFunctionDefinition {

  private static final NERServiceClientCall NER_SERVICE_CLIENT_CALL = new NERServiceClientCall();
  private static final StructuredQName NER_SERVICE_CLIENT_STRUCTURED_Q_NAME = new StructuredQName(
      ExtensionConstants.PREFIX,
      ExtensionConstants.NAMESPACE, ExtensionConstants.NER_SERVICE_CLIENT);
  private static final SequenceType[] ARGUMENT_SEQUENCE_TYPES = {SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING,
      SequenceType.SINGLE_STRING};

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
    return NER_SERVICE_CLIENT_STRUCTURED_Q_NAME;
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
    return NER_SERVICE_CLIENT_CALL;
  }
}
