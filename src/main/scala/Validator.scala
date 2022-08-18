package CSVValidation

import CSVValidation.ConfiguredObjectMapper.objectMapper
import CSVValidation.WarningsAndErrors.{Errors, Warnings}
import CSVValidation.traits.OptionExtensions.OptionIfDefined
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Merge, Sink, Source}
import com.fasterxml.jackson.databind.node.ObjectNode
import com.typesafe.scalalogging.Logger
import org.apache.commons.csv.{CSVFormat, CSVParser, CSVRecord}

import java.io.File
import java.net.URI
import java.nio.charset.Charset
import scala.::
import scala.collection.mutable
import scala.collection.mutable.{Map, Set}
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.jdk.CollectionConverters.{IterableHasAsScala, MapHasAsScala}
import scala.language.postfixOps
import scala.util.control.NonFatal
import scala.util.{Failure, Success}

class Validator(
    val schemaUri: Option[String],
    csvUri: Option[String] = None,
    var sourceUriUsed: Boolean = false
) {
  private val logger = Logger(this.getClass.getName)
  val mapAvailableCharsets = Charset.availableCharsets().asScala

  type ForeignKeys = Map[ChildTableForeignKey, Set[KeyWithContext]]
  type ForeignKeyReferences =
    Map[ParentTableForeignKeyReference, Set[KeyWithContext]]
  type MapTableToForeignKeys = Map[Table, ForeignKeys]
  type MapTableToForeignKeyReferences = Map[Table, ForeignKeyReferences]
  type DataToAccumulate =
    (WarningsAndErrors, MapTableToForeignKeys, MapTableToForeignKeyReferences)

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

  def validate(): Source[WarningsAndErrors, NotUsed] = {
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
  ): Source[WarningsAndErrors, NotUsed] = {
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
          val warningsAndErrorsToReturn = List(
            WarningsAndErrors(errors = Array[ErrorWithCsvContext](error))
          )
          Source(warningsAndErrorsToReturn)
//          WarningsAndErrors(errors = Array[ErrorWithCsvContext](error))
        } else Source(List(WarningsAndErrors()))
      }
      case Seq(uri, uris @ _*) =>
        attemptToFindMatchingTableGroup(
          maybeCsvUri,
          uri
        ) match {
          case Right((tableGroup, wAndE)) =>
//            val warningsAndErrors = validateSchemaTables(tableGroup)
//            WarningsAndErrors(
//              warningsAndErrors.warnings ++ wAndE.warnings,
//              warningsAndErrors.errors ++ wAndE.errors
//            )

            validateSchemaTables(tableGroup).map { wAndE2 =>
              WarningsAndErrors(
                wAndE2.warnings ++ wAndE.warnings,
                wAndE2.errors ++ wAndE.errors
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
            Source(List(WarningsAndErrors(errors = Array(error))))
          }
          case Left(SchemaDoesNotContainCsvError(err)) => {
            logger.debug(err.getMessage)
            logger.debug(err.getStackTrace.mkString("\n"))
//            val errorsAndWarnings =
//              findAndValidateCsvwSchemaFileForCsv(maybeCsvUri, uris)
//
//            val warnings =
//              errorsAndWarnings.warnings :+ WarningWithCsvContext(
//                "source_url_mismatch",
//                s"CSV supplied not found in metadata $uri",
//                "",
//                "",
//                "",
//                ""
//              )
//            WarningsAndErrors(warnings, errorsAndWarnings.errors)
            findAndValidateCsvwSchemaFileForCsv(maybeCsvUri, uris).map(x => {
              WarningsAndErrors(
                x.warnings :+ WarningWithCsvContext(
                  "source_url_mismatch",
                  s"CSV supplied not found in metadata $uri",
                  "",
                  "",
                  "",
                  ""
                )
              )
            })
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

  def validateSchemaTables(
      schema: TableGroup
  ): Source[WarningsAndErrors, NotUsed] = {
    Source
      .fromIterator(() => schema.tables.keys.iterator)
      .flatMapMerge(
        4,
        tableUrl => {
          val table = schema.tables(tableUrl)
          val tableUri = new URI(tableUrl)
          val dialect = table.dialect.getOrElse(Dialect())
          val format = getCsvFormat(dialect)

          readAndValidateCsv(schema, tableUri, format, dialect).map {
            case (wAndE, fk, fkRef) =>
              (wAndE, fk, fkRef, table)
          }
        }
      )
      .fold[DataToAccumulate](
        (
          WarningsAndErrors(),
          Map(),
          Map()
        )
      ) {
        case (
              (
                warningsAndErrorsAccumulator,
                foreignKeysAccumulator,
                foreignKeyReferencesAccumulator
              ),
              (
                warningsAndErrorsSource,
                foreignKeysSource,
                foreignKeyReferencesSource,
                table
              )
            ) => {
          val wAndE = WarningsAndErrors(
            warningsAndErrorsAccumulator.warnings ++ warningsAndErrorsSource.warnings,
            warningsAndErrorsAccumulator.errors ++ warningsAndErrorsSource.errors
          )
          foreignKeysAccumulator(table) = foreignKeysSource
          foreignKeyReferencesAccumulator(table) = foreignKeyReferencesSource
          (
            wAndE,
            foreignKeysAccumulator,
            foreignKeyReferencesAccumulator
          )
        }
      }
      .map {
        case (warningsAndErrors, allForeignKeys, allForeignKeyReferences) =>
          val foreignKeyErrors = validateForeignKeyReferences(
            allForeignKeys,
            allForeignKeyReferences
          )
          WarningsAndErrors(
            errors = warningsAndErrors.errors ++ foreignKeyErrors,
            warnings = warningsAndErrors.warnings
          )
      }
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
  ): Source[
    (
        WarningsAndErrors,
        ForeignKeys,
        ForeignKeyReferences
    ),
    NotUsed
  ] = {
    getParser(tableUri, dialect, format) match {
      case Right(parser) =>
        readAndValidateWithParser(schema, tableUri, dialect, parser)
          .recover {
            case NonFatal(err) => {
              logger.debug(err.getMessage)
              logger.debug(err.getStackTrace.mkString("\n"))
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
                WarningsAndErrors(warnings = warnings),
                Map(),
                Map()
              )
            }
          }
      case Left(warningsAndErrors) =>
        if (tableUri.toString != csvUri && !sourceUriUsed) {
          sourceUriUsed = true
          readAndValidateCsv(
            schema,
            new URI(csvUri.get),
            format,
            dialect
          )
        }
        // AND tableUri != sourceUri
        // THEN we re-try readAndValidate using the sourceUri as the tableUri
        // AND THEN we set a flag on Validator to say we had to use the sourceUri

        val collection = List(
          (
            warningsAndErrors,
            Map[ChildTableForeignKey, Set[KeyWithContext]](),
            Map[ParentTableForeignKeyReference, Set[KeyWithContext]]()
          )
        )
        Source(collection)
    }
  }
  implicit val ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  private def readAndValidateWithParser(
      schema: TableGroup,
      tableUri: URI,
      dialect: Dialect,
      parser: CSVParser
  ): Source[
    (
        WarningsAndErrors,
        mutable.Map[ChildTableForeignKey, mutable.Set[KeyWithContext]],
        mutable.Map[ParentTableForeignKeyReference, mutable.Set[
          KeyWithContext
        ]]
    ),
    NotUsed
  ] = {

    /**
      * Source(Array([Tuple("first-table.csv", "my-favourite-csvw.csv-metadata.json"), Tuple("second-table.csv", "my-favourite-csvw.csv-metadata.json", dialect, parser)])).flatMap(x => readAndValidateWithParser(x))
      */

    var warnings: Array[WarningWithCsvContext] = Array()
    var errors: Array[ErrorWithCsvContext] = Array()
    val parserAfterSkippedRows =
      parser.asScala.drop(dialect.skipRows).iterator
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

    def accumulateErrorsWarningsAndKeys(
        validateRowOutput: ValidateRowOutput
    ): Unit = {
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
      validatePrimaryKey(
        allPrimaryKeyValues,
        validateRowOutput.row,
        validateRowOutput
      ).ifDefined(e => errors :+= e)
    }

    /**
      * #
      * #
      * #
      * #
      *
      * #, #, #, #
      */

    Source
      .fromIterator(() => parserAfterSkippedRows)
      .grouped(1000)
      .mapAsyncUnordered(8)(csvRows =>
        Future {
          csvRows.map(parseRow(schema, tableUri, dialect, table, _))
        }
      )
      // x :: y :: Nil
      // Nil
      .fold[List[ValidateRowOutput]](Nil)(
        (acc: List[ValidateRowOutput], v: Seq[ValidateRowOutput]) =>
          List.from(v) ++ acc
      )
      .map(listOfValidateRowOutputs => {
        listOfValidateRowOutputs
          .foreach(x => accumulateErrorsWarningsAndKeys(x))

        (
          WarningsAndErrors(warnings, errors),
          childTableForeignKeys,
          parentTableForeignKeyReferences
        )
      })
  }

//  implicit val ec: scala.concurrent.ExecutionContext =
//    scala.concurrent.ExecutionContext.global
  private def parseRow(
      schema: TableGroup,
      tableUri: URI,
      dialect: Dialect,
      table: Table,
      row: CSVRecord
  ): ValidateRowOutput = {
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
