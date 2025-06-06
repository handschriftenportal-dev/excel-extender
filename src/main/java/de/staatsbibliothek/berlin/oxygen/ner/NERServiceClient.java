package de.staatsbibliothek.berlin.oxygen.ner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.staatsbibliothek.berlin.oxygen.excel.ExcelCommon;
import de.staatsbibliothek.berlin.oxygen.exception.ExcelExtenderException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 11.09.23
 */
public class NERServiceClient {

  public static final Type TYPE = new NERServiceTypeToken().getType();
  public static final String CSV_SEPARATOR_TWO_VALUES = "\";\"";

  public static final int CSV_FIRST_FIELD = 1;

  private static final Pattern PATTERN_NON_PRINTABLE = Pattern.compile("[\\\\\n\r\t]");
  private static final Pattern PATTERN_BINDESTRICH = Pattern.compile("(?![A-Za-z]{2})-\n");
  private static final Pattern PATTERN_SPACES_BACKSLASH = Pattern.compile("\\s{2,}");
  private static final Pattern PATTERN_QUOTES = Pattern.compile("\"");


  private static final Gson GSON = new GsonBuilder().create();

  /**
   * @param nerServiceURL ie http://nerServiceHost:5000/ner/4
   */
  public static String getNamedEntitiesAsCSVSnipset(String nerServiceURL, String textToAnalyze, String entityFunc)
      throws Exception {
    textToAnalyze = purifyText(textToAnalyze);
    List<WordWithPrediction> wordWithPredictions = extractNamedEntities(nerServiceURL, textToAnalyze);

    Pattern patternForFuncSplit = null;
    Map<Pattern, String> entityFunctionMap = new HashMap<>();
    String regExForFunctionSplit = parseEntityFunction(entityFunc, entityFunctionMap);
    if (!regExForFunctionSplit.isEmpty()) {
      patternForFuncSplit = Pattern.compile(regExForFunctionSplit);
    }
    StringBuilder result = new StringBuilder(512);

    RecognizedEntites entites = new RecognizedEntites(wordWithPredictions);
    int currentIndex = formatEntitiesAsCSV(CSV_FIRST_FIELD, EntityCategory.PER, result, textToAnalyze,
        entites.getPersons(), patternForFuncSplit, entityFunctionMap);
    currentIndex = formatEntitiesAsCSV(currentIndex, EntityCategory.SOZ, result, textToAnalyze,
        entites.getOrganizations(), patternForFuncSplit, entityFunctionMap);
    formatEntitiesAsCSV(currentIndex, EntityCategory.GEO, result, textToAnalyze, entites.getPlaces(),
        patternForFuncSplit, entityFunctionMap);
    return result.toString();
  }

  static String purifyText(String textToAnalyze) {
    textToAnalyze = PATTERN_BINDESTRICH.matcher(textToAnalyze).replaceAll("");
    textToAnalyze = PATTERN_NON_PRINTABLE.matcher(textToAnalyze).replaceAll(" ");
    textToAnalyze = PATTERN_SPACES_BACKSLASH.matcher(textToAnalyze).replaceAll(" ");
    textToAnalyze = PATTERN_QUOTES.matcher(textToAnalyze).replaceAll("''");
    textToAnalyze = textToAnalyze.trim();
    return textToAnalyze;
  }

  private static int formatEntitiesAsCSV(int currentIndex, EntityCategory category, StringBuilder result,
      String textToAnalyze, List<String> entities, Pattern patternForFuncSplit, Map<Pattern, String> entityFunctions) {

    boolean withEntityFunctions =
        !entityFunctions.isEmpty() && patternForFuncSplit != null && patternForFuncSplit.matcher(textToAnalyze).find();
    for (String currentEntity : entities) {
      if (currentIndex > 1) {
        result.append(';');
      }
      result.append('"');
      result.append(currentEntity);
      result.append(CSV_SEPARATOR_TWO_VALUES);
      result.append(category);
      result.append(CSV_SEPARATOR_TWO_VALUES);
      textToAnalyze = findAndAddEntityFunction(result, textToAnalyze, entityFunctions, currentEntity,
          withEntityFunctions);
      result.append('"');
      ++currentIndex;
    }
    return currentIndex;
  }

  private static String findAndAddEntityFunction(StringBuilder result, String textToAnalyze,
      Map<Pattern, String> entityFunctions,
      String currentEntity, boolean withEntityFunctions) {
    if (withEntityFunctions && currentEntity != null) {
      int startCurrentEntity = textToAnalyze.indexOf(currentEntity);
      Matcher matcher;
      textToAnalyze = textToAnalyze.substring(startCurrentEntity + currentEntity.length());
      for (Entry<Pattern, String> entry : entityFunctions.entrySet()) {
        matcher = entry.getKey().matcher(textToAnalyze);
        if (matcher.find()) {
          int funcPos = matcher.start();
          if (funcPos < 3) {
            String functionName = entry.getValue();
            result.append(functionName);
            textToAnalyze = textToAnalyze.substring(matcher.end());
            break;
          }
        }
      }
    }
    return textToAnalyze;
  }

  public static List<WordWithPrediction> extractNamedEntities(String nerServiceURL, String textToAnalyze)
      throws Exception {
    String jsonResult = callNERService(nerServiceURL, textToAnalyze);
    return getResults(jsonResult);
  }

  protected static List<WordWithPrediction> getResults(String json) throws ExcelExtenderException {
    List<List<WordWithPrediction>> result;
    try {
      result = GSON.fromJson(json, TYPE);
    } catch (Exception ex) {
      throw new ExcelExtenderException("Problem during parsing '" + json + "' due to " + ex.getMessage(), ex);
    }
    if (result.isEmpty()) {
      return Collections.emptyList();
    }

    return result.get(0);
  }

  public static String callNERService(String url, String text) throws Exception {
    String jsonBody = prepareJSONBody(text);
    StringEntity httpEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);

    ClassicHttpRequest httpPost = ClassicRequestBuilder.post(url)
        .setEntity(httpEntity)
        .build();

    try (CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(httpPost)) {
      HttpEntity entity = response.getEntity();
      try (InputStream content = entity.getContent()) {
        return extractContentFromInputStream(content);
      }
    }
  }

  private static String prepareJSONBody(String text) {
    if (text == null) {
      return "{}";
    }

    StringBuilder jsonBody = new StringBuilder(512);
    jsonBody.append("{\"text\":\"").append(text).append("\"}");
    return jsonBody.toString();
  }

  private static String extractContentFromInputStream(InputStream is) throws Exception {
    try (InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
      String result = bufferedReader
          .lines()
          .collect(Collectors.joining("\n"));
      return result;
    }
  }

  static String parseEntityFunction(String entityFunc, Map<Pattern, String> entityFunctionMap) {
    StringBuilder result = new StringBuilder(128);
    if (!entityFunc.isEmpty()) {
      boolean first;
      for (String line : ExcelCommon.PATTERN_DOUBLE_PIPE.split(entityFunc)) {
        first = true;
        String key = null;
        String val = null;
        String[] dropDownListValues = line.split(";");
        for (String value : dropDownListValues) {
          if (first) {
            key = value;
            first = false;
          } else {
            val = value;
          }
        }

        if (key != null && !key.isEmpty() && val != null && !val.isEmpty()) {
          entityFunctionMap.put(Pattern.compile("\\(" + key + "[\\)\\s]"), val);
          if (result.length() == 0) {
            result.append("\\(");
          } else {
            result.append("|\\(");
          }
          result.append(key);
          result.append("[\\)\\s]");
        }
      }
    }
    return result.toString();
  }

  private static class NERServiceTypeToken extends TypeToken<List<List<WordWithPrediction>>> {

  }
}
