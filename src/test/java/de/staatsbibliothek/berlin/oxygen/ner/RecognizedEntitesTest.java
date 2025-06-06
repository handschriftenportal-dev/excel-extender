package de.staatsbibliothek.berlin.oxygen.ner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 22.11.23
 */
class RecognizedEntitesTest {

  private static final String[] TEST_PREDICTIONS = {
      "B-LOC", "Aachen",
      "O", "-",
      "O", "Dedicatio",
      "O", "ecclesiae",
      "O", "138",
      "O", ",",
      "O", "42",
      "B-PER", "Ado",
      "I-PER", "Viennensis",
      "O", "-",
      "O", "Martyrologium",
      "O", "138",
      "O", ",",
      "O", "passim",
      "B-PER", "Aegidius",
      "I-PER", "Romanus",
      "O", "(",
      "O", "S",
      "O", ")",
      "O", "-",
      "O", "Quodlibeta",
      "O", "VI",
      "O", "116",
      "O", ",",
      "O", "135",
      "O", "-",
      "O", "Theoremata",
      "O", "L",
      "O", "de",
      "O", "corpore",
      "O", "Christi",
      "O", "145",
      "O", ",",
      "O", "195",
      "B-PER", "Albertanus",
      "I-PER", "Causidicus",
      "I-PER", "Brixiensis",
      "B-LOC", "Berlin",
      "O", ",",
      "B-ORG", "Staatsbibliothek",
      "O", "67",
      "O", ",",
      "O", "72",
      "B-LOC", "Soest",
      "O", ",",
      "O", "Stadtbibliothek",
      "O", "72",
      "O", ",",
      "O", "78",
      "B-LOC", "Berlin",
      "I-LOC", "Staatsbibliothek",
      "O", "67",
      "O", ",",
      "O", "72",
      "O", "-",
      "O", "De",
      "O", "arte",
      "O", "loquendi",
      "O", "et",
      "O", "tacendi",
      "O", "76",
      "O", ",",
      "O", "172",
      "O", "Ps",
      "O", ".",
      "O", "-",
      "B-PER", "Albertus",
      "I-PER", "Magnus",
      "O", "-",
      "O", "Sermones",
      "O", "XXXII",
      "O", "de",
      "O", "corpore",
      "O", "Christi",
      "O", "siehe",
      "O", "Ps",
      "O", ".",
      "O", "-",
      "B-PER", "Thomas",
      "I-PER", "de",
      "I-PER", "Aquino",
      "B-PER", "Albertus",
      "I-PER", "de",
      "I-PER", "Padua",
      "O", "-",
      "O", "Postilla",
      "O", "super",
      "O", "evangelia",
      "O", "dominicalia",
      "O", "et",
      "O", "in",
      "O", "praecipuis",
      "O", "festivitatibus",
      "O", "sanctorum",
      "O", "7372"};

  private static final int LENGTH = TEST_PREDICTIONS.length;

  private static final List<WordWithPrediction> PREDICTION_LIST = new ArrayList<>(LENGTH / 2);

  static {
    for (int i = 0; i < LENGTH; i += 2) {
      PREDICTION_LIST.add(new WordWithPrediction(TEST_PREDICTIONS[i], TEST_PREDICTIONS[i + 1]));
    }
  }


  @Test
  void testGetPersons() {
    RecognizedEntites recognizedEntites = new RecognizedEntites(PREDICTION_LIST);
    List<String> persons = recognizedEntites.getPersons();
    assertNotNull(persons);
    assertEquals(6, persons.size());
  }

  @Test
  void testGetOrganizations() {
    RecognizedEntites recognizedEntites = new RecognizedEntites(PREDICTION_LIST);
    List<String> organizations = recognizedEntites.getOrganizations();
    assertNotNull(organizations);
    assertEquals(1, organizations.size());
  }

  @Test
  void testGetPlaces() {
    RecognizedEntites recognizedEntites = new RecognizedEntites(PREDICTION_LIST);
    List<String> places = recognizedEntites.getPlaces();
    assertNotNull(places);
    assertEquals(4, places.size());
  }
}
