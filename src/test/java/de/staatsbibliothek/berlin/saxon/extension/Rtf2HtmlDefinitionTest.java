package de.staatsbibliothek.berlin.saxon.extension;

import static de.staatsbibliothek.berlin.saxon.extension.Rtf2TxtDefinitionTest.VALUE_1;
import static org.junit.jupiter.api.Assertions.assertFalse;

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;
import org.junit.jupiter.api.Test;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 20.02.25
 */
class Rtf2HtmlDefinitionTest {

  @Test
  void testMakeCallExpression() throws XPathException {
    Rtf2HtmlDefinition definition = new Rtf2HtmlDefinition();
    ExtensionFunctionCall extensionFunctionCall = definition.makeCallExpression();
    Sequence[] arguments = {StringValue.makeStringValue(VALUE_1)};
    Sequence result = extensionFunctionCall.call(null, arguments);
    assertFalse(result.head().getStringValue().contains("\\"));
  }
}
