package de.staatsbibliothek.berlin.oxygen.excel;

import java.util.Objects;

/**
 * Encapsulate ExcelExtender parameters
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 27.03.25
 */
public class ExcelExtenderParams {

  private final String csvFilePath;
  private final String excelFilePath;
  private final String password;
  private final String lockedKeysStr;
  private final String dropDownListStr;
  private final String datatypeListStr;
  private final String sheetName;

  ExcelExtenderParams(ExcelExtenderParamsBuilder builder) {
    this.csvFilePath = builder.csvFilePath;
    this.excelFilePath = builder.excelFilePath;
    this.password = builder.password;
    this.lockedKeysStr = builder.lockedKeysStr;
    this.dropDownListStr = builder.dropDownListStr;
    this.datatypeListStr = builder.datatypeListStr;
    this.sheetName = builder.sheetName;
  }

  public String getCsvFilePath() {
    return this.csvFilePath;
  }

  public String getExcelFilePath() {
    return this.excelFilePath;
  }

  public String getPassword() {
    return this.password;
  }

  public String getLockedKeysStr() {
    return this.lockedKeysStr;
  }

  public String getDropDownListStr() {
    return this.dropDownListStr;
  }

  public String getDatatypeListStr() {
    return this.datatypeListStr;
  }

  public String getSheetName() {
    return this.sheetName;
  }

  @Override
  public boolean equals(Object o) {
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    ExcelExtenderParams that = (ExcelExtenderParams) o;
    return Objects.equals(this.csvFilePath, that.csvFilePath) && Objects.equals(this.excelFilePath,
        that.excelFilePath) && Objects.equals(this.password, that.password) && Objects.equals(
        this.lockedKeysStr, that.lockedKeysStr) && Objects.equals(this.dropDownListStr, that.dropDownListStr)
        && Objects.equals(this.datatypeListStr, that.datatypeListStr) && Objects.equals(this.sheetName,
        that.sheetName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.csvFilePath, this.excelFilePath, this.password, this.lockedKeysStr, this.dropDownListStr,
        this.datatypeListStr,
        this.sheetName);
  }


  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ExcelExtenderParams{");
    sb.append("csvFilePath='").append(csvFilePath).append('\'');
    sb.append(", excelFilePath='").append(excelFilePath).append('\'');
    sb.append(", password='").append(password).append('\'');
    sb.append(", lockedKeysStr='").append(lockedKeysStr).append('\'');
    sb.append(", dropDownListStr='").append(dropDownListStr).append('\'');
    sb.append(", datatypeListStr='").append(datatypeListStr).append('\'');
    sb.append(", sheetName='").append(sheetName).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public static class ExcelExtenderParamsBuilder {

    private String csvFilePath;
    private String excelFilePath;
    private String password;
    private String lockedKeysStr;
    private String dropDownListStr;
    private String datatypeListStr;
    private String sheetName;

    public ExcelExtenderParamsBuilder withCsvFilePath(String csvFilePath) {
      this.csvFilePath = csvFilePath;
      return this;
    }

    public ExcelExtenderParamsBuilder withExcelFilePath(String excelFilePath) {
      this.excelFilePath = excelFilePath;
      return this;
    }

    public ExcelExtenderParamsBuilder withPassword(String password) {
      this.password = password;
      return this;
    }

    public ExcelExtenderParamsBuilder withLockedKeysStr(String lockedKeysStr) {
      this.lockedKeysStr = lockedKeysStr;
      return this;
    }

    public ExcelExtenderParamsBuilder withDropDownListStr(String dropDownListStr) {
      this.dropDownListStr = dropDownListStr;
      return this;
    }

    public ExcelExtenderParamsBuilder withDatatypeListStr(String datatypeListStr) {
      this.datatypeListStr = datatypeListStr;
      return this;
    }

    public ExcelExtenderParamsBuilder withSheetName(String sheetName) {
      this.sheetName = sheetName;
      return this;
    }

    public ExcelExtenderParams createExcelExtenderParams() {
      return new ExcelExtenderParams(this);
    }
  }
}
