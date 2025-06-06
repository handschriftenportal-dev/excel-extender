package de.staatsbibliothek.berlin.oxygen.ner;

import java.util.regex.Pattern;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 14.03.25
 */
public class NERTestData {

  public static final String TEXT_TO_ANALYZE1 = "Aachen - Dedicatio ecclesiae 138,42 Ado Viennensis - Martyrologium 138, passim Aegidius Rom";
  public static final String TEXT_TO_ANALYZE2 = "anus (S) - Quodlibeta VI 116,135 - Theoremata L de corpore Christi 145,195 Albertanus Causidicus Brixiensis Berlin, Staatsbibliothek 67,72 Soest, Stadtbibliothek 72,78 Berlin Staatsbibliothek 67,72 - De arte loquendi et tacendi 76,172 Ps.-Albertus Magnus - Sermones XXXII de corpore Christi siehe Ps.- Thomas de Aquino Albertus de Padua - Postilla super evangelia dominicalia et in praecipuis festivitatibus sanctorum 7372";
  public static final String TEXT_TO_ANALYZE_NOT_CLEAN = TEXT_TO_ANALYZE1 + "-\n" + TEXT_TO_ANALYZE2;
  public static final String TEXT_TO_ANALYZE = TEXT_TO_ANALYZE1 + TEXT_TO_ANALYZE2;
  public static final String URL = "http://b-dev1049.pk.de:5000/ner/4";
  public static final String ENTITY_FUNCTIONS = "S;Schreiber||C;Schreiber||P;Provenienz||A;Autor||M;Erwähnung||Tr;Übersetzer||U;Benutzer||E;Entstehung||B;Buchmalerei||V;Vorbesitz";
  public static final Pattern PATTERN_SPLIT_REGISTRY_ENTRIES = Pattern.compile(
      "(?i)(?<=)([0-9]+[\\s\\]\\\"\\“\\”\\™']*(\\,?\\s?[IVXLCDM]+)?[\\*\\(]?['\\*\\\"\\”\\“\\™\\’\\«\\^A-Z]?(\\,?\\s?VB)?(\\,?\\s?RB)?(\\,?\\s?RA)?(\\,?\\s?VA)?(\\,?\\s?VD)?(\\,?\\s?LRA?)?(\\,?\\s?XPB)?(\\,?\\s?[T])?(\\,?\\s?QRA)?(\\,?\\s?HD)?(\\,?\\s?(HINTERER )?SPIEGEL)?(\\,?\\s?LIRA)?(\\,?\\s?MIT VARIANTEN)?(\\,?\\s?HB)?(\\,?\\s?L?LV)?(\\,?\\s?LLRB?)?(\\,?\\s?RT)?(\\,?\\s?DB)?(\\,?\\s?(VORSATZ\\s)?RECTO)?[\\s\\'\\’\\\"\\”\\“\\™\\*\\)\\>bca]*(u\\.ö\\.)?\\n)(?-i)");
  public static final String TEXT_DIRTY =
      "Mit * gekennzeichnete Initien sind in den Handschriftenbeschreibungen nicht aufgeführt. V = Verse.\n"
          + "Aaz apprehendens vel apprehensio Öh 1, \n";
  public static final String TEXT_CLEAN = "Mit * gekennzeichnete Initien sind in den Handschriftenbeschreibungen nicht aufgeführt. V = Verse. Aaz apprehendens vel apprehensio Öh 1,";
  public static final String JSON_STRING = "[[{\"prediction\": \"I-PER\", \"word\": \"Absberg\"}, {\"prediction\": "
      + "\"I-PER\", \"word\": \",\"}, {\"prediction\": \"I-PER\", \"word\": \"Heinrich\"}, {\"prediction\": \"I-PER\", "
      + "\"word\": \"von\"}, {\"prediction\": \"O\", \"word\": \"(\"}, {\"prediction\": \"O\", \"word\": \"S\"}, "
      + "{\"prediction\": \"O\", \"word\": \")\"}, {\"prediction\": \"O\", \"word\": \",\"}, {\"prediction\": \"O\", "
      + "\"word\": \"Bischof\"}, {\"prediction\": \"O\", \"word\": \"von\"}, {\"prediction\": \"I-LOC\", \"word\": "
      + "\"Regensburg\"}, {\"prediction\": \"O\", \"word\": \"(\"}, {\"prediction\": \"O\", \"word\": \"P\"}, "
      + "{\"prediction\": \"O\", \"word\": \"I\"}, {\"prediction\": \"O\", \"word\": \"+\"}, {\"prediction\": \"O\", "
      + "\"word\": \"II\"}, {\"prediction\": \"O\", \"word\": \")\"}, {\"prediction\": \"O\", \"word\": \",\"}]]";
}
