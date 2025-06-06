package de.staatsbibliothek.berlin.saxon.extension;

import de.staatsbibliothek.berlin.saxon.call.ExcelToXMLCall;
import de.staatsbibliothek.berlin.saxon.constant.ExtensionConstants;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 16.10.24
 */
public class ExcelToXMLDefinition extends ExtensionFunctionDefinition {

  private static final StructuredQName EXCEL_TO_XML_STRUCTURED_Q_NAME = new StructuredQName(ExtensionConstants.PREFIX,
      ExtensionConstants.NAMESPACE, ExtensionConstants.EXCEL_TO_XML);
  private static final SequenceType[] ARGUMENT_SEQUENCE_TYPES = {SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING,
      SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING, SequenceType.OPTIONAL_STRING};
  private static final ExcelToXMLCall EXCEL_TO_XML_CALL = new ExcelToXMLCall();

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
    return EXCEL_TO_XML_STRUCTURED_Q_NAME;
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
    return EXCEL_TO_XML_CALL;
  }
}
