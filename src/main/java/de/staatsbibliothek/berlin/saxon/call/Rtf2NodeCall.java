package de.staatsbibliothek.berlin.saxon.call;

import static de.staatsbibliothek.berlin.saxon.constant.ExtensionConstants.RTF_2_NODE;

import de.staatsbibliothek.berlin.oxygen.exception.RtfConverterException;
import de.staatsbibliothek.berlin.oxygen.rtf.RtfConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.trans.XPathException;

/**
 * Convert RTF to xml-node
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 15.11.24
 */
public class Rtf2NodeCall extends RtfConverterCall {

  private static final Logger logger = Logger.getLogger(Rtf2NodeCall.class.getName());

  @Override
  public Sequence call(XPathContext xPathContext, Sequence[] arguments) throws XPathException {
    checkParameters(arguments, RTF_2_NODE);
    String result = "";
    try {
      String textToConvert = arguments[0].head().getStringValue();

      RtfConverter rtfConverter = getRtfConverter(arguments);

      result = rtfConverter.convertToHtml(textToConvert);

      DocumentBuilder docBuilder = getDocumentBuilder(xPathContext);

      try (InputStream stream = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8))) {
        StreamSource streamSource = new StreamSource(stream);

        return docBuilder.build(streamSource).getUnderlyingNode();
      }
    } catch (IOException | ArrayIndexOutOfBoundsException | URISyntaxException |
             SaxonApiException | RtfConverterException e) {
      logger.log(Level.SEVERE, e.getMessage() + "\n" + result, e);
      throw new XPathException(e.getMessage(), e);
    }
  }

  private static DocumentBuilder getDocumentBuilder(XPathContext xPathContext) throws URISyntaxException {
    Processor processor;
    if (xPathContext == null || xPathContext.getConfiguration() == null) {
      logger.log(Level.SEVERE, "xPathContext is null");
      processor = new Processor(false);
    } else {
      processor = (Processor) xPathContext.getConfiguration().getProcessor();
    }
    DocumentBuilder docBuilder = processor.newDocumentBuilder();
    docBuilder.setBaseURI(new URI(URN_FROM_STRING));
    return docBuilder;
  }
}
