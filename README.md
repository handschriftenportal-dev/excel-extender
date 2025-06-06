# Handschriftenportal - excel-extender - xpath / saxon extension for oxygen.xml plugin / tei-odd 

## Description

This repository contains the xpath/ xsl extensions for the Handschriftenportal. It offers a set of XSLT saxon extensions.
Creation of Excel tables from CSV files, Export values from the Excel tables to XML, NER queries via the NER service, RTF conversion.

This tool is independent of the backend services of the manuscripts portal "Handschriftenportal". It provides for data editing / data conversion.

## Technology stack

- The pipeline is implemented as a [Maven](https://maven.apache.org/) project.
- [Saxon-HE](https://github.com/Saxonica/Saxon-HE) is used for XSLT extension.
- [poi-ooxml](https://poi.apache.org)
- [Tika](https://tika.apache.org/) / EditorKit (plus customization) is used for RTF 2 Html translation
- The [lingua](https://github.com/pemistahl/lingua) is used for language detection.
- To build the project `mvn clean package` is used.

## Status

Beta (in development)

## Getting started

1. Get the source code

   ```
   git clone https://github.com/handschriftenportal-dev/hsp-excel-extender
   ```

2. Build the project

   ```
   mvn clean package
   ```


After a successful build, you will find the excel-extender jar and shaded jar files in the `target` directory. In case of errors in the XML files, the pipeline may have stopped.

## Usage

XML files containing manuscript descriptions and authority files have to be filed under the [`nachweis_daten`](nachweis_daten/) directory:

```xml
sbbfunc:csv-to-excel(
   csvFilePath, 
   excelFilePath, 
   password, 
   lockedKeysStr, 
   dropDownListStr, 
   datatypeListStr, 
   sheetName
)


sbbfunc:excel-to-xml(
   excelFile, 
   xmlFile, 
   password, 
   xmlFieldNameForExcelColumnNumber, 
   excelColumnNames, 
   optional xmlFieldTypeForExcelColumnNumber
)


sbbfunc:detect-language(
    textToCheck
)


sbbfunc:ner-service-client(
   nerServiceURL, 
   textToAnalyze, 
   entityFunc
)


sbbfunc:rtf-to-html(
   rtfText - required,
   onlyHtmlBodyContent - optional ('true', 'false' (default)),
   converterType - optional: (
      1 - JEditorPane,
      2 - TikaRTF (default),
      3 - TikaAuto
   )
)


sbbfunc:rtf-to-mime(
   rtfText - required,
   mimeType - required,
   converterType - optional: (
      1 - JEditorPane,
      2 - TikaRTF (default),
      3 - TikaAuto
   )
)


sbbfunc:rtf-to-node(
   rtfText - required,
   converterType - optional: (
      1 - JEditorPane,
      2 - TikaRTF (default),
      3 - TikaAuto
   )
)


sbbfunc:rtf-to-txt(
   rtfText - required,
   converterType - optional: (
      1 - JEditorPane,
      2 - TikaRTF (default),
      3 - TikaAuto
   )
)
```

For oxygen.xml the plugin must be located in the classpath of the XSL transformation and registered via **xsl:stylesheet**:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
  xmlns:sbbfunc="http://dev.sbb.berlin/sbb"
  version="3.0">
```

Excel-Extender:
```xml
<xsl:variable name="inputCSV" select="concat($inputPath, 'liste_5382_Format.csv')" as="xs:string" />
<xsl:variable name="outputXSLX" select="concat($outputPath, 'liste_5382_Format.xlsx')" as="xs:string" />
<xsl:variable name="password" select="'hsp'" as="xs:string" />
<!-- Semicolon separated list with the non editable fields -->
<xsl:variable name="lockedFields" select="'aktuelle Wert'" as="xs:string" />
<!-- || separated list for the drop down fields, First element in the line define field Name. From the second element in the line are possible Drop Down field values -->
<xsl:variable name="dropDownFields" select="'neuer Wert;&gt;Folio;Folio;Quart;Octav;&lt;Octav||NEU;Ja;Nein'" as="xs:string" />
<xsl:variable name="sheetName" select="'Format(5282)'" as="xs:string"/>
<xsl:value-of select="sbbfunc:csv-to-excel($inputCSV, $outputXSLX, $password, $lockedFields, $dropDownFields, $dataTypeFields, $sheetName)"/>
```

NER Service Client:
```xml
    <xsl:template name="outputCurrentLineToCSV">
		<xsl:variable name="textToAnalyze" select="normalize-space(concat($lemmaBegriff, ' ', $unterlemma1, ' ', $unterlemma2, ' ', $unterlemma3))"/>
        <xsl:variable name="nerServiceURL" select="'http://NER-SERVER:5000/ner/4'"/>
		<xsl:variable name="nerResult">
			<xsl:choose>
				<xsl:when test="$withNer eq '1'">
					<xsl:value-of select="sbbfunc:ner-service-client($nerServiceURL, $textToAnalyze, $entityFunc)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of>"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";""</xsl:value-of>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:message> Value:  <xsl:value-of select="$textToAnalyze"/> - <xsl:value-of select="$nerResult"/> </xsl:message>

		<xsl:text>"</xsl:text>
		<xsl:value-of select="$lemmaBegriff"/>
		<xsl:text>";"</xsl:text>
		<xsl:value-of select="$unterlemma1"/>
		<xsl:text>";"</xsl:text>
		<xsl:value-of select="$unterlemma2"/>
		<xsl:text>";"</xsl:text>
		<xsl:value-of select="$unterlemma3"/>
		<xsl:text>";"</xsl:text>
		<xsl:value-of select="$signatur"/>
		<xsl:text>";"</xsl:text>
		<xsl:value-of select="$fundstelle"/>
		<xsl:text>";</xsl:text>
		<xsl:value-of select="$nerResult"/>
		<xsl:text>&#xD;</xsl:text>
	</xsl:template>
```

Beispiel:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	xmlns:h1="http://www.startext.de/HiDA/DefService/XMLSchema" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema" 
	xmlns:saxon="http://saxon.sf.net/"
    xmlns:jt="http://saxon.sf.net/java-type"
    xmlns:hsp="http://handschriftenportal.de/hsp"
    xmlns:sbbfunc="http://dev.sbb.berlin/sbb"
	extension-element-prefixes="saxon" 
	exclude-result-prefixes="xs h1 saxon sbbfunc jt" 
	version="3.0">
	
	<xsl:include href="HandschriftenFunctions.xsl"/>
	<xsl:output indent="yes" method="text" encoding="utf-8" exclude-result-prefixes="#all" byte-order-mark="yes"/>

	<xsl:variable name="inputDocumentPath" select="base-uri()"/>
	<xsl:variable name="rootPath" select="ckm:substring-before-last(ckm:substring-before-last($inputDocumentPath, '/'), '/')"/>
	<xsl:variable name="inputMXMLPath" select="ckm:substring-before-last($inputDocumentPath, '/')"/>
	<xsl:variable name="directoryName" select="ckm:substring-after-last($inputMXMLPath, '/')"/>
	<xsl:variable name="inputPath" select="concat($rootPath, '/', $directoryName, '_excel/')"/>
	<xsl:variable name="outputPath" select="concat($rootPath, '/', $directoryName, '_EXCEL_OK/')"/>
		<xsl:variable name="dataTypeFields" select="'Anzahl;NUMERIC'"/>
    	
    	
    	<xsl:template match="/">
    		
    		<xsl:message>
    			<xsl:variable name="inputCSV" select="concat($inputPath, 'liste_5382_Format.csv')" as="xs:string" />
    			<xsl:variable name="outputXSLX" select="concat($outputPath, 'liste_5382_Format.xlsx')" as="xs:string" />
    			<xsl:variable name="password" select="'hsp'" as="xs:string" />
    			<!-- Semicolon separated list with the non editable fields -->
    			<xsl:variable name="lockedFields" select="'aktuelle Wert'" as="xs:string" />
    			<!-- || separated list for the drop down fields, First element in the line define field Name. From the second element in the line are possible Drop Down field values -->
    			<xsl:variable name="dropDownFields" select="'neuer Wert;&gt;Folio;Folio;Quart;Octav;&lt;Octav||NEU;Ja;Nein'" as="xs:string" />
    			<xsl:variable name="sheetName" select="'Format(5282)'" as="xs:string"/>
    			<xsl:value-of select="sbbfunc:csv-to-excel($inputCSV, $outputXSLX, $password, $lockedFields, $dropDownFields, $dataTypeFields, $sheetName)"/>
    			----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    			----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    		</xsl:message>
        </xsl:template>
	
</xsl:stylesheet>
```



For  **xml-maven-plugin** the custom transformer factory must be used **<transformerFactory>de.staatsbibliothek.berlin.saxon.factory.HSPTransformerFactory</transformerFactory>**
```xml
<plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>xml-maven-plugin</artifactId>
        <version>${xml-maven-plugin.version}</version>
        <dependencies>
          <dependency>
            <groupId>net.sf.saxon</groupId>
            <artifactId>Saxon-HE</artifactId>
            <version>${Saxon-HE.version}</version>
          </dependency>
          <dependency>
            <groupId>com.componentcorp.xml.validation</groupId>
            <artifactId>jxvc</artifactId>
            <version>${jxvc.version}</version>
          </dependency>
          <dependency>
            <groupId>com.componentcorp.xml.validation</groupId>
            <artifactId>relaxng</artifactId>
            <version>${jxvc.version}</version>
          </dependency>
          <dependency>
            <groupId>com.helger.schematron</groupId>
            <artifactId>ph-schematron-xslt</artifactId>
            <version>${ph-schematron.version}</version>
          </dependency>
          <dependency>
            <groupId>staatsbibliothek-berlin.oxygen</groupId>
            <artifactId>excel-extender</artifactId>
            <version>${excel-extender.version}</version>
          </dependency>
        </dependencies>
        <executions>
          <execution>
            <id>transform</id>
            <phase>generate-sources</phase>
            <goals>
              <goal>transform</goal>
            </goals>
            <configuration>
              <transformerFactory>de.staatsbibliothek.berlin.saxon.factory.HSPTransformerFactory</transformerFactory>
              <transformationSets>
                <transformationSet>
                  <dir>${baseDirEnc}</dir>
                  <includes>hsp_cataloguing.odd</includes>
                  <stylesheet>${baseDirEnc}/src/main/resources/Stylesheets/odds/odd2odd.xsl</stylesheet>
                  <parameters>
                    <parameter>
                      <name>currentDirectory</name>
                      <value>file:///${baseDirEnc}</value>
                    </parameter>
                    <parameter>
                      <name>defaultSource</name>
                      <value>file:///${baseDirEnc}/src/main/resources/Stylesheets/source/p5subset.xml</value>
                    </parameter>
                    <parameter>
                      <name>suppressTEIexamples</name>
                      <value>true</value>
                    </parameter>
                  </parameters>
                  <outputDir>${buildDirEnc}/${project.artifactId}/odd-compiled</outputDir>
                  <fileMappers>
                    <fileMapper implementation="org.codehaus.plexus.components.io.filemappers.FileExtensionMapper">
                      <targetExtension>.compiled</targetExtension>
                    </fileMapper>
                  </fileMappers>
                </transformationSet>
              </transformationSets>
            </configuration>
          </execution>
        </executions>
      </plugin>
```

## Known issues

## Getting help

To get help please use our contact possibilities on [twitter](https://twitter.com/hsprtl)
and [handschriftenportal.de](https://handschriftenportal.de/)

## Getting involved

To get involved please contact our develoment
team [handschriftenportal@sbb.spk-berlin.de](handschriftenportal-dev@sbb.spk-berlin.de)

## Open source licensing info

The project is published under the [MIT License](https://opensource.org/licenses/MIT).

## Credits and references

1. [Github Project Repository](https://github.com/handschriftenportal-dev)
2. [Project Page](https://handschriftenportal.de/)
3. [Internal Documentation](docs/README.md)

## RELEASE Notes
