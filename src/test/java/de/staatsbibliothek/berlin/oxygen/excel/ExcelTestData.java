package de.staatsbibliothek.berlin.oxygen.excel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.CellType;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 14.03.25
 */
public class ExcelTestData {

  public static final String XML_FIELDS_POS_NAME = "0;key||2;anzeige||3;gndAnsetzung||4;gndNummer";
  public static final String XML_FIELDS_POS_TYPE = "0;regex";
  public static final String EXCEL_COLUMN_NAMES = "0;MXML||1;Anzahl||2;HSP_Anzeige||3;GND-Ansetzung||4;GND-Nummer||5;MXML-GEO-Dok-Nummer";
  public static final String EXCEL_COLUMN_NAMES_WRONG = "0;MXML||1;Anzahl||2;HSP_Anzeige-WRONG||3;GND-Ansetzung||4;GND-Nummer||5;MXML-GEO-Dok-Nummer";
  public static final String TEXT = "Piotr Czarnecki";
  public static final String CSV_CONTENT =
      "\"NEU\";\"aktuelle Wert\";\"neuer Wert\";\"Anzahl\";\"BEARB.\";\"HidaID\"\n"
          + "\"\";\"Nikolaus von Jeroschin, ‚Kronike von Pruzinlant‘, Handschrift D \";\"\";\"Nikolaus von Jeroschin,\n ‚Kronike von Pruzinlant‘, Handschrift D \";\"1\";\"\";\"31603447\"\n"
          + "\"\";\"Druck: Weichbildrecht mit Glosse (GW 9265)\n"
          + "Handschrift: Glosse zum Sachsenspiegel-Lehnrecht • ‚Richtsteig Lehnrechts‘\";\"\";\"Druck: Weichbildrecht mit Glosse (GW 9265)\n"
          + "Handschrift: Glosse zum Sachsenspiegel-Lehnrecht • ‚Richtsteig Lehnrechts‘\";\"1\";\"\";\"31603542\"\n"
          + "\"\";\"Stadtrecht von Breda\";\"\";\"Stadtrecht von Breda\";\"1\";\"\";\"31603552\"\n"
          + "\"\";\"‚Feuerwerkbuch von 1420’\";\"\";\"‚Feuerwerkbuch von 1420’\";\"1\";\"\";\"31603567\"";
  public static final Set<String> LOCKED_COLUMNS = new HashSet<>();
  public static final Map<String, String[]> DROP_DOWN_LIST = new HashMap<>();
  public static final Map<String, CellType> DATA_TYPE_LIST_MAP = new HashMap<>();
  public static final String SHEET_NAME = "testSheet";
  public static final String PASSWORD = "hsp";
  public static final String FUNCTIONS = ";Schreiber;Übersetzer;Benutzer;Provenienz;Autor;Erwähnung;Entstehung;Buchmalerei;Vorbesitz";
  public static final String CATEGORIES = ";PER;SOZ;GEO;TIT;SBG;HSZ";
  public static final String CATEGORIES_DROP_DOWN_FIELDS =
      "Kategorie-1;" + CATEGORIES + "||Kategorie-2;" + CATEGORIES + "||Kategorie-3;" + CATEGORIES + "'||Kategorie-4;"
          + CATEGORIES + "||Kategorie-5;" + CATEGORIES;
  public static final String ALL_DROP_DOWN_FIELDS =
      CATEGORIES_DROP_DOWN_FIELDS + "||Function-1;" + FUNCTIONS + "||Function-2;" + FUNCTIONS + "||Function-3;"
          + FUNCTIONS + "||Function-4;" + FUNCTIONS + "||Function-5;" + FUNCTIONS;

  static {
    LOCKED_COLUMNS.add("aktuelle Wert");
    DROP_DOWN_LIST.put("neuer Wert", new String[]{"", "vorhanden", "nicht vorhanden"});
    DATA_TYPE_LIST_MAP.put("NEU", CellType.BOOLEAN);
    DATA_TYPE_LIST_MAP.put("Anzahl", CellType.NUMERIC);
  }
}
