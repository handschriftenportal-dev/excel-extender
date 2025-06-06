package de.staatsbibliothek.berlin.oxygen.ner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.staatsbibliothek.berlin.oxygen.exception.ExcelExtenderException;
import de.staatsbibliothek.berlin.oxygen.lang.LangDetector;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 13.09.23
 */
public class NERServiceClientTest {

  private static final Logger logger = Logger.getLogger(NERServiceClientTest.class.getName());

  @Test
  public void testCallNERService() throws Exception {
    String content = NERServiceClient.callNERService(NERTestData.URL, NERTestData.TEXT_TO_ANALYZE);
    assertNotNull(content);

    logger.info(content);
    assertFalse(content.isEmpty());

    List<WordWithPrediction> results = NERServiceClient.getResults(content);
    assertFalse(results.isEmpty());

    RecognizedEntites recognizedEntites = new RecognizedEntites(results);

    logger.info(recognizedEntites.toString());
  }

  @Test
  void testGetNamedEntitiesAsCSVSnipset() throws Exception {
    String content = NERServiceClient.getNamedEntitiesAsCSVSnipset(NERTestData.URL,
        "Absberg, Heinrich von (S), Bischof von Regensburg (P I+II),", NERTestData.ENTITY_FUNCTIONS);
    assertNotNull(content);

    logger.info(content);
    assertFalse(content.isEmpty());

  }

  @Test
  void testParseRegistryEntries() throws Exception {
    for (int i = 0; i < 4; i++) {
      Path txtFile = Paths.get("src", "test", "resources", "txt", "INI" + i + ".txt");
      String fileContent = new String(Files.readAllBytes(txtFile), StandardCharsets.UTF_8);
      String[] registryEntries = NERTestData.PATTERN_SPLIT_REGISTRY_ENTRIES.split(fileContent);
      for (int j = 1; j < registryEntries.length; j++) {
        String registryEntry = registryEntries[j];
        logger.info("--->>>" + registryEntry + " - " + LangDetector.detectLanguage(registryEntry));
        String content = NERServiceClient.getNamedEntitiesAsCSVSnipset(NERTestData.URL,
            registryEntry, NERTestData.ENTITY_FUNCTIONS);
        assertNotNull(content);
        logger.info("<<<<--------" + content);
      }

    }
  }

  @Test
  void testPurifyText() {
    assertEquals(NERTestData.TEXT_TO_ANALYZE, NERServiceClient.purifyText(NERTestData.TEXT_TO_ANALYZE_NOT_CLEAN));
    assertEquals(NERTestData.TEXT_CLEAN, NERServiceClient.purifyText(NERTestData.TEXT_DIRTY));
  }

  @Test
  void testGetResults() throws ExcelExtenderException {
    List<WordWithPrediction> results = NERServiceClient.getResults(NERTestData.JSON_STRING);
    assertNotNull(results);
    logger.info(results.toString());
    assertFalse(results.isEmpty());
  }
}
