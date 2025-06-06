package de.staatsbibliothek.berlin.oxygen.ner;

/**
 * Parse predictions
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 13.09.23
 */
public class WordWithPrediction {


  private String prediction;
  private String word;

  public WordWithPrediction(String prediction, String word) {
    this.prediction = prediction;
    this.word = word;
  }

  public String getPrediction() {
    return this.prediction;
  }

  public void setPrediction(String prediction) {
    this.prediction = prediction;
  }

  public String getWord() {
    return this.word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("WordWithPrediction{");
    sb.append("prediction='").append(prediction).append('\'');
    sb.append(", word='").append(word).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
