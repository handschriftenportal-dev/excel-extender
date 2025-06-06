package de.staatsbibliothek.berlin.saxon.extension;

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
class Rtf2TxtDefinitionTest {

  public static final String VALUE_1 = "{\\rtf1\\ansi\\ansicpg0\\uc1\\deff0\\deflang0\\deflangfe0{\\fonttbl{\\f0\\fnil\\fcharset1 Arial;}{\\f1\\fnil\\fcharset1 Times New Roman;}{\\f2\\fnil\\fcharset0 Times New Roman;}}{\\colortbl;\\red0\\green0\\blue0;\\red0\\green0\\blue255;\\red0\\green255\\blue255;\\red0\\green255\\blue0;\\red255\\green0\\blue255;\\red255\\green0\\blue0;\\red255\\green255\\blue0;\\red255\\green255\\blue255;\\red0\\green0\\blue128;\\red0\\green128\\blue128;\\red0\\green128\\blue0;\\red128\\green0\\blue128;\\red128\\green0\\blue0;\\red128\\green128\\blue0;\\red128\\green128\\blue128;\\red192\\green192\\blue192;\\red204\\green238\\blue221;} \\uc1 \\pard\\fi0\\li0\\ql\\ri0\\sb0\\sa0\\itap0 \\plain \\f1\\b\\fs24 Kanonistische Handschrift}";

  @Test
  void testMakeCallExpression() throws XPathException {
    Rtf2TxtDefinition definition = new Rtf2TxtDefinition();
    ExtensionFunctionCall extensionFunctionCall = definition.makeCallExpression();
    Sequence[] arguments = {StringValue.makeStringValue(VALUE_1)};
    Sequence result = extensionFunctionCall.call(null, arguments);
    assertFalse(result.head().getStringValue().contains("\\"));
  }
}
