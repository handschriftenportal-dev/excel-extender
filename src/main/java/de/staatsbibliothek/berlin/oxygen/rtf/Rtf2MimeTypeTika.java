package de.staatsbibliothek.berlin.oxygen.rtf;

import de.staatsbibliothek.berlin.oxygen.exception.RtfConverterException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.rtf.RTFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * TIka based RtfConverter implementation
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 26.02.25
 */
public class Rtf2MimeTypeTika implements RtfConverter {

  public static final ParseContext PARSE_CONTEXT = new ParseContext();
  private final Parser parser;

  Rtf2MimeTypeTika() {
    this(CONVERTER_TYPE_TIKA_AUTO);
  }

  public Rtf2MimeTypeTika(int converterType) {
    if (converterType == CONVERTER_TYPE_TIKA_AUTO) {
      parser = new AutoDetectParser();
    } else {
      parser = new RTFParser();
    }
  }

  @Override
  public String convertToMime(String rtfString, String outputMime) throws RtfConverterException {
    ContentHandler handler = getContentHandler(outputMime);
    rtfString = prepareRtf(rtfString, false);
    String rtfTextCharacterEncoding = getCharacterEncoding(rtfString);
    Metadata metadata = new Metadata();
    try (InputStream stream = new ByteArrayInputStream(rtfString.getBytes(rtfTextCharacterEncoding))) {
      parser.parse(stream, handler, metadata, PARSE_CONTEXT);
      return postprocessResult(handler.toString());
    } catch (IOException | SAXException | TikaException e) {
      throw new RtfConverterException(e);
    }
  }

  private static ContentHandler getContentHandler(String outputMime) {
    ContentHandler handler;
    if (TEXT_PLAIN.equals(outputMime)) {
      handler = new BodyContentHandler();
    } else {
      handler = new ToXMLContentHandler(UTF_8);
    }
    return handler;
  }
}
