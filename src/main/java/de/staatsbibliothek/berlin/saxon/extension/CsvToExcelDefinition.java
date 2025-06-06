package de.staatsbibliothek.berlin.saxon.extension;

import de.staatsbibliothek.berlin.saxon.call.CsvToExcelCall;
import de.staatsbibliothek.berlin.saxon.constant.ExtensionConstants;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 16.10.24
 */
public class CsvToExcelDefinition extends ExtensionFunctionDefinition {

  private static final StructuredQName CSV_TO_EXCEL_STRUCTURED_Q_NAME = new StructuredQName(ExtensionConstants.PREFIX,
      ExtensionConstants.NAMESPACE, ExtensionConstants.CSV_TO_EXCEL);
  private static final SequenceType[] ARGUMENT_SEQUENCE_TYPES = {SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING,
      SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING,
      SequenceType.SINGLE_STRING};
  private static final CsvToExcelCall CSV_TO_EXCEL_CALL = new CsvToExcelCall();

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
    return CSV_TO_EXCEL_STRUCTURED_Q_NAME;
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
    return CSV_TO_EXCEL_CALL;
  }
}
