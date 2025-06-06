package de.staatsbibliothek.berlin.oxygen.rtf;

import de.staatsbibliothek.berlin.oxygen.exception.RtfConverterException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Pattern;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;

/**
 * Decode RTF to given Mime-Type, plain text, html
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 21.01.22
 */
public class Rtf2MimeType implements RtfConverter {

  public static final String XHTML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<html xmlns=\"http://www.w3.org/1999/xhtml\">";
  public static final String HTML = "<html>";
  private static final Pattern PATTERN_HTML_TAG_BEGIN = Pattern.compile(HTML);

  @Override
  public String convertToMime(String rtfString, String outputMime) throws RtfConverterException {
    String result;
    JEditorPane editorPane = new JEditorPane();
    editorPane.setContentType(TEXT_RTF);
    rtfString = prepareRtf(rtfString, true);
    EditorKit editorKitForRtf = editorPane.getEditorKitForContentType(TEXT_RTF);
    Document document = editorPane.getDocument();
    try (Reader inputStringReader = new StringReader(rtfString);
        Writer writer = new StringWriter()) {
      editorKitForRtf.read(inputStringReader, document, 0);
      EditorKit kitHtml = editorPane.getEditorKitForContentType(outputMime);
      kitHtml.write(writer, document, 0, document.getLength());

      result = writer.toString();
    } catch (IOException | BadLocationException e) {
      throw new RtfConverterException(e);
    }
    if (result.startsWith(HTML)) {
      result = PATTERN_HTML_TAG_BEGIN.matcher(result).replaceFirst(XHTML);
    }
    result = postprocessResult(result);
    return result;
  }
}
