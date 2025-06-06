package de.staatsbibliothek.berlin.oxygen.rtf;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Hold RTF tag encode data
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 12.03.25
 */
public class RTFTag {

  private final String tagBegin;
  private final List<String> tagEnds;
  private final String htmlReplacementBegin;
  private final String htmlReplacementEnd;

  public RTFTag(String tagBegin, String htmlReplacementBegin, String htmlReplacementEnd, String... tagEnds) {
    this.tagBegin = tagBegin;
    this.tagEnds = Arrays.asList(tagEnds);
    this.htmlReplacementBegin = htmlReplacementBegin;
    this.htmlReplacementEnd = htmlReplacementEnd;
  }

  public String getTagBegin() {
    return this.tagBegin;
  }

  public List<String> getTagEnds() {
    return this.tagEnds;
  }

  public String getHtmlReplacementBegin() {
    return this.htmlReplacementBegin;
  }

  public String getHtmlReplacementEnd() {
    return this.htmlReplacementEnd;
  }

  @Override
  public boolean equals(Object o) {
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    RTFTag RTFTags = (RTFTag) o;
    return Objects.equals(this.tagBegin, RTFTags.tagBegin);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.tagBegin);
  }


  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("RtfTags{");
    sb.append("tagBegin='").append(tagBegin).append('\'');
    sb.append(", tagEnd='").append(tagEnds).append('\'');
    sb.append(", htmlReplacementBegin='").append(htmlReplacementBegin).append('\'');
    sb.append(", htmlReplacementEnd='").append(htmlReplacementEnd).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
