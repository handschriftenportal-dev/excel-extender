package de.staatsbibliothek.berlin.saxon.extension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.staatsbibliothek.berlin.oxygen.lang.Messages;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;
import org.junit.jupiter.api.Test;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 20.02.25
 */
class LangDetectorDefinitionTest {

  public static final String TEST_VALUE = "Eldenominació oficial a Catalunya, a les Illes Balears, a Andorra, a la ciutat de l'Alguer i tradicional a Catalunya del Nord) o valencià (denominació oficial al País Valencià i tradicional al Carxe)";

  @Test
  void testMakeCallExpression() throws XPathException {
    LangDetectorDefinition definition = new LangDetectorDefinition();
    Sequence[] arguments = {StringValue.makeStringValue(TEST_VALUE)};
    Sequence result = definition.makeCallExpression().call(null, arguments);
    String expected = Messages.getMessage("CATALAN");
    assertEquals(expected, result.head().getStringValue());
  }
}
