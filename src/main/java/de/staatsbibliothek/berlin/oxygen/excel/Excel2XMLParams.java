package de.staatsbibliothek.berlin.oxygen.excel;

import java.util.Objects;

/**
 * Encapsulate Excel2XML parameters
 *
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 28.03.25
 */
public class Excel2XMLParams {

  private final String excelFile;
  private final String xmlFile;
  private final String password;
  private final String xmlFieldNameForExcelColumnNumber;
  private final String excelColumnNames;
  private final String xmlFieldTypeForExcelColumnNumber;

  public Excel2XMLParams(Excel2XMLParamsBuilder builder) {
    this.excelFile = builder.excelFile;
    this.xmlFile = builder.xmlFile;
    this.password = builder.password;
    this.xmlFieldNameForExcelColumnNumber = builder.xmlFieldNameForExcelColumnNumber;
    this.excelColumnNames = builder.excelColumnNames;
    this.xmlFieldTypeForExcelColumnNumber = builder.xmlFieldTypeForExcelColumnNumber;
  }

  public String getExcelFile() {
    return this.excelFile;
  }

  public String getXmlFile() {
    return this.xmlFile;
  }

  public String getPassword() {
    return this.password;
  }

  public String getXmlFieldNameForExcelColumnNumber() {
    return this.xmlFieldNameForExcelColumnNumber;
  }

  public String getExcelColumnNames() {
    return this.excelColumnNames;
  }

  public String getXmlFieldTypeForExcelColumnNumber() {
    return this.xmlFieldTypeForExcelColumnNumber;
  }

  @Override
  public boolean equals(Object o) {
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    Excel2XMLParams that = (Excel2XMLParams) o;
    return Objects.equals(this.excelFile, that.excelFile) && Objects.equals(this.xmlFile, that.xmlFile)
        && Objects.equals(this.password, that.password) && Objects.equals(this.xmlFieldNameForExcelColumnNumber,
        that.xmlFieldNameForExcelColumnNumber) && Objects.equals(this.excelColumnNames, that.excelColumnNames)
        && Objects.equals(this.xmlFieldTypeForExcelColumnNumber, that.xmlFieldTypeForExcelColumnNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.excelFile, this.xmlFile, this.password, this.xmlFieldNameForExcelColumnNumber,
        this.excelColumnNames,
        this.xmlFieldTypeForExcelColumnNumber);
  }


  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Excel2XMLParams{");
    sb.append("excelFile='").append(excelFile).append('\'');
    sb.append(", xmlFile='").append(xmlFile).append('\'');
    sb.append(", password='").append(password).append('\'');
    sb.append(", xmlFieldNameForExcelColumnNumber='").append(xmlFieldNameForExcelColumnNumber).append('\'');
    sb.append(", excelColumnNames='").append(excelColumnNames).append('\'');
    sb.append(", xmlFieldTypeForExcelColumnNumber='").append(xmlFieldTypeForExcelColumnNumber).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public static class Excel2XMLParamsBuilder {

    private String excelFile;
    private String xmlFile;
    private String password;
    private String xmlFieldNameForExcelColumnNumber;
    private String excelColumnNames;
    private String xmlFieldTypeForExcelColumnNumber;

    public Excel2XMLParamsBuilder withExcelFile(String excelFile) {
      this.excelFile = excelFile;
      return this;
    }

    public Excel2XMLParamsBuilder withXmlFile(String xmlFile) {
      this.xmlFile = xmlFile;
      return this;
    }

    public Excel2XMLParamsBuilder withPassword(String password) {
      this.password = password;
      return this;
    }

    public Excel2XMLParamsBuilder withXmlFieldNameForExcelColumnNumber(String xmlFieldNameForExcelColumnNumber) {
      this.xmlFieldNameForExcelColumnNumber = xmlFieldNameForExcelColumnNumber;
      return this;
    }

    public Excel2XMLParamsBuilder withExcelColumnNames(String excelColumnNames) {
      this.excelColumnNames = excelColumnNames;
      return this;
    }

    public Excel2XMLParamsBuilder withXmlFieldTypeForExcelColumnNumber(String xmlFieldTypeForExcelColumnNumber) {
      this.xmlFieldTypeForExcelColumnNumber = xmlFieldTypeForExcelColumnNumber;
      return this;
    }

    public Excel2XMLParams createExcel2XMLParams() {
      return new Excel2XMLParams(this);
    }
  }
}
