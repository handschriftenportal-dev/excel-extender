package de.staatsbibliothek.berlin.saxon.factory;

import de.staatsbibliothek.berlin.saxon.extension.CsvToExcelDefinition;
import de.staatsbibliothek.berlin.saxon.extension.ExcelToXMLDefinition;
import de.staatsbibliothek.berlin.saxon.extension.LangDetectorDefinition;
import de.staatsbibliothek.berlin.saxon.extension.NERServiceClientDefinition;
import de.staatsbibliothek.berlin.saxon.extension.Rtf2HtmlDefinition;
import de.staatsbibliothek.berlin.saxon.extension.Rtf2MimeDefinition;
import de.staatsbibliothek.berlin.saxon.extension.Rtf2NodeDefinition;
import de.staatsbibliothek.berlin.saxon.extension.Rtf2TxtDefinition;
import java.util.Arrays;
import java.util.List;
import net.sf.saxon.Configuration;
import net.sf.saxon.jaxp.SaxonTransformerFactory;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.s9api.Processor;

/**
 * Custom transformer factory with all extension functions
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 14.10.24
 */
public class HSPTransformerFactory extends SaxonTransformerFactory {

  private static final List<ExtensionFunctionDefinition> ALL_EXTENSION_FUNCTIONS = Arrays.asList(
      new CsvToExcelDefinition(),
      new ExcelToXMLDefinition(),
      new LangDetectorDefinition(),
      new Rtf2MimeDefinition(),
      new Rtf2HtmlDefinition(),
      new Rtf2TxtDefinition(),
      new Rtf2NodeDefinition(),
      new NERServiceClientDefinition()
  );


  public HSPTransformerFactory() {
    super();
    registerExtensionFunctions();
  }

  public HSPTransformerFactory(Configuration config) {
    super(config);
    registerExtensionFunctions();
  }

  private void registerExtensionFunctions() {
    Processor processor = getProcessor();
    processor.getUnderlyingConfiguration().setProcessor(processor);
    for (ExtensionFunctionDefinition extensionFunctionDefinition : ALL_EXTENSION_FUNCTIONS) {
      processor.registerExtensionFunction(extensionFunctionDefinition);
    }
  }
}
