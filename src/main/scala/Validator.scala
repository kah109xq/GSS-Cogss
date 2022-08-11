package CSVValidation

import CSVValidation.ConfiguredObjectMapper.objectMapper
import CSVValidation.WarningsAndErrors.{Errors, Warnings}
import CSVValidation.traits.OptionExtensions.OptionIfDefined
import akka.NotUsed
import akka.actor.ActorSystem
import com.fasterxml.jackson.databind.node.ObjectNode
import com.typesafe.scalalogging.Logger
import org.apache.commons.csv.{CSVFormat, CSVParser, CSVRecord}

import java.io.File
import java.net.URI
import java.nio.charset.Charset
import scala.collection.mutable
import scala.collection.mutable.{Map, Set}
import akka.stream.scaladsl.{Flow, Sink, Source, SubFlow}
import scala.collection.immutable
import scala.concurrent.Future

import scala.jdk.CollectionConverters.{IterableHasAsScala, MapHasAsScala}
import scala.util.control.NonFatal

class Validator(
    val schemaUri: Option[String],
    csvUri: Option[String] = None,
    var sourceUriUsed: Boolean = false
) {
  implicit val system: ActorSystem = ActorSystem("actor-system")
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

  private def attemptToFindMatchingTableGroup(
      maybeCsvUri: Option[URI],
      possibleSchemaUri: URI
  ): Either[CsvwLoadError, (TableGroup, WarningsAndErrors)] = {
    try {
      val jsonNode = if (possibleSchemaUri.getScheme == "file") {
        objectMapper.readTree(new File(possibleSchemaUri))
      } else {
        objectMapper.readTree(possibleSchemaUri.toURL)
      }
      val (tableGroup, warningsAndErrors) =
        Schema.fromCsvwMetadata(
          possibleSchemaUri.toString,
          jsonNode.asInstanceOf[ObjectNode]
        )

      // http://example.com/imports-data-as-csv?year=1010

      val workingWithUserSpecifiedMetadata =
        if (schemaUri.isDefined && possibleSchemaUri.toString == schemaUri.get)
          true
        else false
      maybeCsvUri
        .map(csvUri => {
          if (
            tableGroupContainsCsv(
              tableGroup,
              csvUri
            ) || warningsAndErrors.errors.nonEmpty || workingWithUserSpecifiedMetadata
          ) // the last 2 or cases are required. See tests 104 and 121 respectively
            Right((tableGroup, warningsAndErrors))
          else {
            val message =
              s"Schema file does not contain a definition for ${maybeCsvUri}"
            Left(
              SchemaDoesNotContainCsvError(
                new IllegalArgumentException(message)
              )
            )
          }

        })
        .getOrElse(Right((tableGroup, warningsAndErrors)))

    } catch {
      case metadataError: MetadataError =>
        Left(GeneralCsvwLoadError(metadataError))
      case e: java.io.FileNotFoundException => Left(CascadeToOtherFilesError(e))
      case e: Throwable => {
        Left(GeneralCsvwLoadError(e))
      }
    }
  }

  private def tableGroupContainsCsv(
      tableGroup: TableGroup,
      csvUri: URI
  ): Boolean = {
    val csvUrl = csvUri.toString
    val tables = tableGroup.tables

    val csvUrlWithoutQueryString = getUriWithoutQueryString(csvUri).toString

    // todo: We need to be able to try both relative & absolute CSVURIs here.

    tables.contains(csvUrl) || tables.contains(csvUrlWithoutQueryString)
  }

  private def getUriWithoutQueryString(csvUri: URI): URI = {
    if (csvUri.getRawQuery == null)
      csvUri
    else {
      val queryStringLength = csvUri.getRawQuery.length
      val url = csvUri.toString
      new URI(url.substring(0, url.length - (queryStringLength + 1)))
    }
  }

  def validate(): WarningsAndErrors = {
    val absoluteSchemaUri = schemaUri.map(getAbsoluteSchemaUri)

    val maybeCsvUri = csvUri.map(new URI(_))

    val schemaUrisToCheck = maybeCsvUri
      .map(csvUri =>
        Array(
          absoluteSchemaUri,
          Some(new URI(s"${getUriWithoutQueryString(csvUri)}-metadata.json")),
          Some(csvUri.resolve("csv-metadata.json"))
        )
      )
      .getOrElse(Array(absoluteSchemaUri))
      .flatten
      .distinct

    findAndValidateCsvwSchemaFileForCsv(maybeCsvUri, schemaUrisToCheck)
  }

  private def findAndValidateCsvwSchemaFileForCsv(
      maybeCsvUri: Option[URI],
      schemaUrisToCheck: Seq[URI]
  ): WarningsAndErrors = {
    schemaUrisToCheck match {
      case Seq() => {
        if (schemaUri.isDefined) {
          val error = ErrorWithCsvContext(
            "metadata",
            "cannot locate schema",
            "",
            "",
            s"${schemaUri.get} not found",
            ""
          )
          WarningsAndErrors(errors = Array[ErrorWithCsvContext](error))
        } else WarningsAndErrors()
      }
      case Seq(uri, uris @ _*) =>
        attemptToFindMatchingTableGroup(
          maybeCsvUri,
          uri
        ) match {
          case Right((tableGroup, wAndE)) => {
            val warningsAndErrors = validateSchemaTables(tableGroup)
            WarningsAndErrors(
              warningsAndErrors.warnings ++ wAndE.warnings,
              warningsAndErrors.errors ++ wAndE.errors
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
          case Left(SchemaDoesNotContainCsvError(err)) => {
            logger.debug(err.getMessage)
            logger.debug(err.getStackTrace.mkString("\n"))
            val errorsAndWarnings =
              findAndValidateCsvwSchemaFileForCsv(maybeCsvUri, uris)

            val warnings =
              errorsAndWarnings.warnings :+ WarningWithCsvContext(
                "source_url_mismatch",
                s"CSV supplied not found in metadata $uri",
                "",
                "",
                "",
                ""
              )
            WarningsAndErrors(warnings, errorsAndWarnings.errors)
          }
          case Left(CascadeToOtherFilesError(err)) => {
            logger.debug(err.getMessage)
            logger.debug(err.getStackTrace.mkString("\n"))
            val errorsAndWarnings =
              findAndValidateCsvwSchemaFileForCsv(maybeCsvUri, uris)
            errorsAndWarnings
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
//    val parserAfterSkippedRowsImmutable = collection.immutable.Seq(parserAfterSkippedRows: _*)
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

    val source = Source[CSVRecord](parserAfterSkippedRows)
//    val flow:Flow[CSVRecord, ValidateRowOutput, NotUsed] = {
//      ParseRowInput(schema, tableUri, dialect, table, csv)
//    }
    source
      .mapAsync(parallelism = 1000)(csvRow => parseRow(schema, tableUri, dialect, table, csvRow))
      .runWith(Sink.foreach(
        {
          validateRowOutput =>
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
            validatePrimaryKey(allPrimaryKeyValues, validateRowOutput.row, validateRowOutput)
              .ifDefined(e => errors :+= e)
        }

      ))
//    parserAfterSkippedRows
//      .map(row =>
//        (
//          parseRow(
//            schema,
//            tableUri,
//            dialect,
//            table,
//            row
//          )
//        )
//      )
//      .foreach {
//        case (validateRowOutput) => {
//          errors ++= validateRowOutput.warningsAndErrors.errors
//          warnings ++= validateRowOutput.warningsAndErrors.warnings
//          setChildTableForeignKeys(
//            validateRowOutput,
//            childTableForeignKeys
//          )
//          setParentTableForeignKeyReferences(
//            validateRowOutput,
//            parentTableForeignKeyReferences
//          )
//          validatePrimaryKey(allPrimaryKeyValues, validateRowOutput.row, validateRowOutput)
//            .ifDefined(e => errors :+= e)
//        }
//      }

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
  ): Future[ValidateRowOutput] = Future {
    if (row.getRecordNumber == 1 && dialect.header) {
      val warningsAndErrors = schema.validateHeader(row, tableUri.toString)
      ValidateRowOutput(warningsAndErrors = warningsAndErrors, row = row)
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
        ValidateRowOutput(warningsAndErrors = warningsAndErrors, row = row)
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
          ValidateRowOutput(warningsAndErrors = warningsAndErrors, row = row)
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
