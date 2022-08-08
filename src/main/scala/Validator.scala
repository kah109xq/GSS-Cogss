package CSVValidation

import CSVValidation.WarningsAndErrors.{Errors, Warnings}
import CSVValidation.traits.OptionExtensions.OptionIfDefined
import org.apache.commons.csv.{CSVFormat, CSVParser, CSVRecord, QuoteMode}
import ConfiguredObjectMapper.objectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.typesafe.scalalogging.Logger

import java.io.File
import java.net.{URI, URL}
import java.nio.charset.Charset
import java.time.ZonedDateTime
import scala.collection.mutable
import scala.collection.mutable.{Map, Set}
import scala.jdk.CollectionConverters.{IterableHasAsScala, MapHasAsScala}
import scala.util.control.NonFatal

class Validator(
    var schemaUri: Option[String],
    csvUri: Option[String] = None,
    var sourceUriUsed: Boolean = false
) {
  private val logger = Logger(this.getClass.getName)
  val mapAvailableCharsets = Charset.availableCharsets().asScala
  private def getAbsoluteSchemaUri(schemaPath: String): URI = {
    val inputSchemaUri = new URI(schemaPath)
    if (inputSchemaUri.getScheme == null) {
      new URI(s"file://${new File(schemaPath).getAbsolutePath}")
    } else {
      inputSchemaUri
    }
  }

  /**
    *
    * BaseReturnMessage
    * FileNotFoundReturnMessage extends BaseReturnMessage
    * OtherErrorReturnMessage extends BaseReturnMessage
    *
    */

  private def attemptToFindMatchingTableGroup(
      maybeCsvUri: Option[URI],
      possibleSchemaUri: URI
  ): Either[CsvwLoadError, (TableGroup, Array[WarningWithCsvContext])] = {
    try {
      val jsonNode = if (possibleSchemaUri.getScheme == "file") {
        objectMapper.readTree(new File(possibleSchemaUri))
      } else {
        objectMapper.readTree(possibleSchemaUri.toURL)
      }
      val (tableGroup, warnings) =
        Schema.fromCsvwMetadata(
          possibleSchemaUri.toString,
          jsonNode.asInstanceOf[ObjectNode]
        )

      maybeCsvUri
        .map(csvUri => {
          if (tableGroup.tables.contains(csvUri.toString))
            // todo: We need to be able to try both relative & absolute CSVURIs here.
            Right((tableGroup, warnings))
          else {
            val message =
              s"Schema file does not contain a definition for ${maybeCsvUri}"
            Left(
              CascadeToOtherFilesError(new IllegalArgumentException(message))
            )
          }

        })
        .getOrElse(Right((tableGroup, warnings)))

    } catch {
      case metadataError: MetadataError =>
        Left(GeneralCsvwLoadError(metadataError))
      // todo: Find out what the appropriate exceptions to catch are for FileNotFound and where the URL doesn't exist as well.
      case e: java.io.FileNotFoundException => Left(CascadeToOtherFilesError(e))
      case e: Throwable                     => Left(GeneralCsvwLoadError(e))
    }
  }

  def validate(): WarningsAndErrors = {
    // When CSV is fetched from web and the associated schema is in the header, this wont work.
    // Change this (returning empty warnings and errors when schemaUri is blank) when that feature is implemented

    // csvlint ajay.csv => metadata?????
    if (schemaUri.isEmpty) return WarningsAndErrors()
    val absoluteSchemaUri = getAbsoluteSchemaUri(schemaUri.get)

    val maybeCsvUri = csvUri.map(new URI(_))

    val schemaUrisToCheck = maybeCsvUri
      .map(csvUri =>
        Array(
          absoluteSchemaUri,
          new URI(s"$csvUri-metadata.json"),
          csvUri.resolve("csv-metadata.json")
        )
      )
      .getOrElse(Array(absoluteSchemaUri))

    findAndValidateCsvwSchemaFileForCsv(maybeCsvUri, schemaUrisToCheck)
  }

  private def findAndValidateCsvwSchemaFileForCsv(
      maybeCsvUri: Option[URI],
      schemaUrisToCheck: Seq[URI]
  ): WarningsAndErrors = {
    schemaUrisToCheck match {
      case Seq() =>
        WarningsAndErrors(
          Array(
            WarningWithCsvContext(
              "source_url_mismatch",
              s"CSV supplied not found in metadata",
              "",
              "",
              "",
              ""
            )
          )
        )
      case Seq(uri, uris @ _*) =>
        attemptToFindMatchingTableGroup(
          maybeCsvUri,
          uri
        ) match {
          case Right((tableGroup, warnings)) => {
            val warningsAndErrors = validateSchemaTables(tableGroup)
            WarningsAndErrors(
              warningsAndErrors.warnings ++ warnings,
              warningsAndErrors.errors
            )
          }
          case Left(GeneralCsvwLoadError(err)) => {
            val error = ErrorWithCsvContext(
              "metadata",
              err.getClass.getName,
              "",
              "",
              err.getMessage,
              ""
            )
            logger.debug(err.getMessage)
            logger.debug(err.getStackTrace.mkString("\n"))
            WarningsAndErrors(errors = Array(error))
          }
          case Left(CascadeToOtherFilesError(err)) => {
            logger.debug(err.getMessage)
            logger.debug(err.getStackTrace.mkString("\n"))
            findAndValidateCsvwSchemaFileForCsv(maybeCsvUri, uris)
          }

        }
    }
  }

  def validateSchemaTables(schema: TableGroup): WarningsAndErrors = {
    val allForeignKeyValues =
      Map[Table, Map[ChildTableForeignKey, Set[KeyWithContext]]]()
    val allForeignKeyReferenceValues =
      Map[Table, Map[ParentTableForeignKeyReference, Set[KeyWithContext]]]()
    var warnings: Warnings = Array()
    var errors: Errors = Array()

    for (tableUrl <- schema.tables.keys) {
      val table = schema.tables(tableUrl)
      val tableUri = new URI(tableUrl)
      val dialect = table.dialect.getOrElse(Dialect())
      val format = getCsvFormat(dialect)
      val (warningsAndErrors, foreignKeys, foreignKeyReferences) =
        readAndValidateCsv(schema, tableUri, format, dialect)

      warnings ++= warningsAndErrors.warnings
      errors ++= warningsAndErrors.errors
      allForeignKeyValues(table) = foreignKeys
      allForeignKeyReferenceValues(table) = foreignKeyReferences
    }

    errors ++= validateForeignKeyReferences(
      allForeignKeyValues,
      allForeignKeyReferenceValues
    )

    WarningsAndErrors(warnings, errors)
  }

  def getCsvFormat(dialect: Dialect): CSVFormat = {
    val format = CSVFormat.RFC4180
      .withDelimiter(dialect.delimiter)
      .withQuote(dialect.quoteChar)
      .withTrim() // Default for trim is true. Implement trim as per w3c spec, issue for this exists
      .withIgnoreEmptyLines(dialect.skipBlankRows)

    if (dialect.doubleQuote) {
      // https://github.com/apache/commons-csv/commit/c025d73d31ca9c9c467f3bad142ca62d7ebee76b
      // Above link explains that escaping with a double-quote mark only works if you avoid specifying the escape character.
      // The default behaviour of CsvParser will ensure the escape functions correctly.
      format
    } else {
      format.withEscape('\\')
    }
  }

  def getParser(
      tableUri: URI,
      dialect: Dialect,
      format: CSVFormat
  ): Either[WarningsAndErrors, CSVParser] = {
    if (tableUri.getScheme == "file") {
      val tableCsvFile = new File(tableUri)
      if (!tableCsvFile.exists) {
        Left(
          WarningsAndErrors(
            Array(),
            Array(
              ErrorWithCsvContext(
                "file_not_found",
                "",
                "",
                "",
                s"File named ${tableUri.toString} cannot be located",
                ""
              )
            )
          )
        )
      }
      Right(
        CSVParser.parse(
          tableCsvFile,
          mapAvailableCharsets(dialect.encoding),
          format
        )
      )
    } else {
      try {
        val csvParser = CSVParser.parse(
          tableUri.toURL,
          mapAvailableCharsets(dialect.encoding),
          format
        )
        Right(csvParser)
      } catch {
        case NonFatal(_) =>
          Left(
            WarningsAndErrors(
              Array(
                WarningWithCsvContext(
                  "url_cannot_be_fetched",
                  "",
                  "",
                  "",
                  s"Url ${tableUri.toString} cannot be fetched",
                  ""
                )
              ),
              Array()
            )
          )
      }
    }
  }

  def readAndValidateCsv(
      schema: TableGroup,
      tableUri: URI,
      format: CSVFormat,
      dialect: Dialect
  ): (
      WarningsAndErrors,
      Map[ChildTableForeignKey, Set[KeyWithContext]],
      Map[ParentTableForeignKeyReference, Set[KeyWithContext]]
  ) = {
    getParser(tableUri, dialect, format) match {
      case Right(parser) =>
        try {
          readAndValidateWithParser(schema, tableUri, dialect, parser)
        } catch {
          case NonFatal(_) => {
            val warnings = Array(
              WarningWithCsvContext(
                "source_url_mismatch",
                "CSV requested not found in metadata",
                "",
                "",
                "",
                ""
              )
            )
            (
              WarningsAndErrors(warnings, Array()),
              mutable.Map(),
              mutable.Map()
            )
          }
        }
      case Left(warningsAndErrors) => {
        if (tableUri.toString != csvUri && !sourceUriUsed) {
          sourceUriUsed = true
          readAndValidateCsv(schema, new URI(csvUri.get), format, dialect)
        }
        // AND tableUri != sourceUri
        // THEN we re-try readAndValidate using the sourceUri as the tableUri
        // AND THEN we set a flag on Validator to say we had to use the sourceUri
        (warningsAndErrors, Map(), Map())
      }
    }

  }

  private def readAndValidateWithParser(
      schema: TableGroup,
      tableUri: URI,
      dialect: Dialect,
      parser: CSVParser
  ) = {
    var warnings: Array[WarningWithCsvContext] = Array()
    var errors: Array[ErrorWithCsvContext] = Array()

    val parserAfterSkippedRows = parser.asScala.drop(dialect.skipRows)
    val table =
      try {
        schema.tables(tableUri.toString)
      } catch {
        case NonFatal(_) =>
          throw MetadataError(
            "Metadata does not contain requested tabular data file"
          )
      }
    val childTableForeignKeys =
      Map[ChildTableForeignKey, mutable.Set[KeyWithContext]]()
    val parentTableForeignKeyReferences =
      Map[ParentTableForeignKeyReference, mutable.Set[KeyWithContext]]()
    val allPrimaryKeyValues: mutable.Set[List[Any]] =
      Set() // List of type Any with same values are not added again in a Set, whereas Array of Type any behaves
    // differently.

    parserAfterSkippedRows
      .map(row =>
        (
          row,
          parseRow(
            schema,
            tableUri,
            dialect,
            table,
            row
          )
        )
      )
      .foreach {
        case (row, validateRowOutput) => {
          errors ++= validateRowOutput.warningsAndErrors.errors
          warnings ++= validateRowOutput.warningsAndErrors.warnings
          setChildTableForeignKeys(
            validateRowOutput,
            childTableForeignKeys
          )
          setParentTableForeignKeyReferences(
            validateRowOutput,
            parentTableForeignKeyReferences
          )
          validatePrimaryKey(allPrimaryKeyValues, row, validateRowOutput)
            .ifDefined(e => errors :+= e)
        }
      }

    (
      WarningsAndErrors(warnings, errors),
      childTableForeignKeys,
      parentTableForeignKeyReferences
    )
  }

  private def parseRow(
      schema: TableGroup,
      tableUri: URI,
      dialect: Dialect,
      table: Table,
      row: CSVRecord
  ): ValidateRowOutput = {
    if (row.getRecordNumber == 1 && dialect.header) {
      val warningsAndErrors = schema.validateHeader(row, tableUri.toString)
      ValidateRowOutput(warningsAndErrors)
    } else {
      if (row.size == 0) {
        val blankRowError = ErrorWithCsvContext(
          "Blank rows",
          "structure",
          row.getRecordNumber.toString,
          "",
          "",
          ""
        )
        val warningsAndErrors =
          WarningsAndErrors(errors = Array(blankRowError))
        ValidateRowOutput(warningsAndErrors)
      } else {
        if (table.columns.length >= row.size()) {
          table.validateRow(row)
        } else {
          val raggedRowsError = ErrorWithCsvContext(
            "ragged_rows",
            "structure",
            row.getRecordNumber.toString,
            "",
            "",
            ""
          )
          val warningsAndErrors =
            WarningsAndErrors(errors = Array(raggedRowsError))
          ValidateRowOutput(warningsAndErrors)
        }
      }
    }
  }

  private def validatePrimaryKey(
      existingPrimaryKeyValues: mutable.Set[List[Any]],
      row: CSVRecord,
      validateRowOutput: ValidateRowOutput
  ): Option[ErrorWithCsvContext] = {
    val primaryKeyValues = validateRowOutput.primaryKeyValues
    if (
      primaryKeyValues.nonEmpty && existingPrimaryKeyValues.contains(
        primaryKeyValues
      )
    ) {
      Some(
        ErrorWithCsvContext(
          "duplicate_key",
          "schema",
          row.toString,
          "",
          s"key already present - ${getListStringValue(primaryKeyValues)}",
          ""
        )
      )
    } else {
      existingPrimaryKeyValues += primaryKeyValues
      None
    }
  }

  private def setParentTableForeignKeyReferences(
      validateRowOutput: ValidateRowOutput,
      parentTableForeignKeyReferences: Map[ParentTableForeignKeyReference, Set[
        KeyWithContext
      ]]
  ) = {
    validateRowOutput.parentTableForeignKeyReferences
      .foreach {
        case (k, value) => {
          val allPossibleParentKeyValues =
            parentTableForeignKeyReferences.getOrElse(
              k,
              Set[KeyWithContext]()
            )
          if (allPossibleParentKeyValues.contains(value)) {
            allPossibleParentKeyValues -= value
            value.isDuplicate = true
          }
          allPossibleParentKeyValues += value
          parentTableForeignKeyReferences(k) = allPossibleParentKeyValues
        }
      }
  }

  private def setChildTableForeignKeys(
      validateRowOutput: ValidateRowOutput,
      childTableForeignKeys: Map[ChildTableForeignKey, Set[KeyWithContext]]
  ) = {
    validateRowOutput.childTableForeignKeys
      .foreach {
        case (k, value) => {
          val childKeyValues = childTableForeignKeys.getOrElse(
            k,
            Set[KeyWithContext]()
          )
          childKeyValues += value
          childTableForeignKeys(k) = childKeyValues
        }
      }
  }

  private def validateForeignKeyReferences(
      childTableForeignKeysByTable: Map[
        Table,
        Map[ChildTableForeignKey, Set[KeyWithContext]]
      ],
      parentTableForeignKeyReferencesByTable: Map[
        Table,
        Map[ParentTableForeignKeyReference, Set[KeyWithContext]]
      ]
  ): Errors = {
    // Child Table : Parent Table
    // Country, Year, Population  : Country, Name
    // UK, 2021, 67M  : UK, United Kingdom
    // EU, 2021, 448M : EU, Europe
    var errors: Errors = Array[ErrorWithCsvContext]()
    for (
      (parentTable, mapParentTableForeignKeyReferenceToAllPossibleValues) <-
        parentTableForeignKeyReferencesByTable
    ) {
      for (
        (parentTableForeignKeyReference, allPossibleParentTableValues) <-
          mapParentTableForeignKeyReferenceToAllPossibleValues
      ) {
        val childTableForeignKeys
            : Map[ChildTableForeignKey, Set[KeyWithContext]] =
          childTableForeignKeysByTable
            .get(parentTableForeignKeyReference.childTable)
            .getOrElse(
              throw new Exception(
                s"Could not find corresponding child table(${parentTableForeignKeyReference.childTable.url}) for parent table ${parentTable.url}"
              )
            )

        val childTableKeyValues: Set[KeyWithContext] =
          childTableForeignKeys
            .get(parentTableForeignKeyReference.foreignKey)
            .getOrElse(
              throw new Exception(
                s"Could not find foreign key against child table." + parentTableForeignKeyReference.foreignKey.jsonObject.toPrettyString
              )
            )

        val keyValuesNotDefinedInParent =
          childTableKeyValues -- allPossibleParentTableValues
        if (keyValuesNotDefinedInParent.nonEmpty) {
          errors ++= keyValuesNotDefinedInParent
            .map(k =>
              ErrorWithCsvContext(
                "unmatched_foreign_key_reference",
                "schema",
                k.rowNumber.toString,
                "",
                getListStringValue(k.keyValues),
                ""
              )
            )
        }

        val duplicateKeysInParent = allPossibleParentTableValues
          .intersect(childTableKeyValues)
          .filter(k => k.isDuplicate)

        if (duplicateKeysInParent.nonEmpty) {
          errors ++= duplicateKeysInParent
            .map(k =>
              ErrorWithCsvContext(
                "multiple_matched_rows",
                "schema",
                k.rowNumber.toString,
                "",
                getListStringValue(k.keyValues),
                ""
              )
            )
        }
      }
    }
    errors
  }

  private def getListStringValue(list: List[Any]): String = {
    val stringList = list.map {
      case listOfAny: List[Any] =>
        listOfAny.map(s => s.toString).mkString(",")
      case i => i.toString
    }
    stringList.mkString(",")
  }

}
