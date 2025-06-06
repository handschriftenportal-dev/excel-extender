package de.staatsbibliothek.berlin.oxygen.ner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 13.09.23
 */
public class RecognizedEntites {

  public static final String B_PER = "B-PER";
  public static final String I_PER = "I-PER";
  public static final String B_LOC = "B-LOC";
  public static final String I_LOC = "I-LOC";
  public static final String B_ORG = "B-ORG";
  public static final String I_ORG = "I-ORG";
  public static final String EMPTY_STRING = "";
  private final List<String> persons = new ArrayList<>();
  private final List<String> organizations = new ArrayList<>();
  private final List<String> places = new ArrayList<>();

  private final StringBuilder person = new StringBuilder(512);
  private final StringBuilder organization = new StringBuilder(512);
  private final StringBuilder place = new StringBuilder(512);

  public RecognizedEntites(List<WordWithPrediction> wordWithPredictions) {
    for (WordWithPrediction wordWithPrediction : wordWithPredictions) {
      convertWordWithPredictionToCSVLine(wordWithPrediction);
    }
    processNewEntitiy(person, persons, EMPTY_STRING);
    processNewEntitiy(place, places, EMPTY_STRING);
    processNewEntitiy(organization, organizations, EMPTY_STRING);
  }

  private void convertWordWithPredictionToCSVLine(WordWithPrediction wordWithPrediction) {
    String prediction = wordWithPrediction.getPrediction();
    String word = wordWithPrediction.getWord();
    switch (prediction) {
      case B_PER:
        processNewEntitiy(person, persons, word);
        processNewEntitiy(place, places, EMPTY_STRING);
        processNewEntitiy(organization, organizations, EMPTY_STRING);
        break;
      case I_PER:
        appendWordWithTrailingSpace(person, word);
        processNewEntitiy(place, places, EMPTY_STRING);
        processNewEntitiy(organization, organizations, EMPTY_STRING);
        break;
      case B_LOC:
        processNewEntitiy(place, places, word);
        processNewEntitiy(person, persons, EMPTY_STRING);
        processNewEntitiy(organization, organizations, EMPTY_STRING);
        break;
      case I_LOC:
        appendWordWithTrailingSpace(place, word);
        processNewEntitiy(person, persons, EMPTY_STRING);
        processNewEntitiy(organization, organizations, EMPTY_STRING);
        break;
      case B_ORG:
        processNewEntitiy(organization, organizations, word);
        processNewEntitiy(person, persons, EMPTY_STRING);
        processNewEntitiy(place, places, EMPTY_STRING);
        break;
      case I_ORG:
        appendWordWithTrailingSpace(organization, word);
        processNewEntitiy(person, persons, EMPTY_STRING);
        processNewEntitiy(place, places, EMPTY_STRING);
        break;
      default:
        processNewEntitiy(person, persons, EMPTY_STRING);
        processNewEntitiy(place, places, EMPTY_STRING);
        processNewEntitiy(organization, organizations, EMPTY_STRING);
        break;
    }
  }

  private static void appendWordWithTrailingSpace(StringBuilder person, String word) {
    if (person.length() > 0 && (int) word.charAt(0) != (int) ',') {
      person.append(' ');
    }
    person.append(word);
  }

  private static void processNewEntitiy(StringBuilder entityFullName, List<String> entityList, String word) {
    if (entityFullName.length() > 0) {
      entityList.add(entityFullName.toString());
      entityFullName.delete(0, entityFullName.length());
    }
    entityFullName.append(word);
  }

  public List<String> getPersons() {
    return this.persons;
  }

  public List<String> getOrganizations() {
    return this.organizations;
  }

  public List<String> getPlaces() {
    return this.places;
  }


  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("RecognizedEntites{");
    sb.append("persons=").append(persons);
    sb.append(", organizations=").append(organizations);
    sb.append(", places=").append(places);
    sb.append(", person=").append(person);
    sb.append(", organization=").append(organization);
    sb.append(", place=").append(place);
    sb.append('}');
    return sb.toString();
  }
}
