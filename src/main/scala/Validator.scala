package CSVValidation

import org.apache.commons.csv.{CSVFormat, CSVParser, CSVRecord}

import java.io.File
import java.net.URI
import java.nio.charset.Charset
import scala.jdk.CollectionConverters.{IterableHasAsScala, MapHasAsScala}

// This is only a skeleton for the core Validator class to be created. It is planned to accept the source csv and
// schema and return errors and warnings collection when validate method is invoked. Changes to this signature can be
// made in the due course or as per requirements
class Validator(var tableCsvFile: URI, sourceUri: String = "") {
  var warnings: Array[ErrorWithCsvContext] = Array()
  var errors: Array[ErrorWithCsvContext] = Array()
  var source: String = "" // Define how this is set, will it always be string?
  val mapAvailableCharsets = Charset.availableCharsets().asScala

  def validate(): Either[String, Array[String]] = {
    val mayBeTableGroupObject = Schema.loadMetadataAndValidate(tableCsvFile)
    mayBeTableGroupObject match {
      case Right(tableGroup) => {
        if (sourceUri.isEmpty) {
          fetchSchemaTables(tableGroup)
        }
        Right(tableGroup.warnings.map(w => processWarnings(w)))
      }
      case Left(errorMessage) => Left(errorMessage)
    }
  }

  def fetchSchemaTables(schema: TableGroup) = {
    for (tableUrl <- schema.tables.keys) {
      val table = schema.tables(tableUrl)
      val tableUri = new URI(tableUrl)
      val dialect = table.dialect.getOrElse(Dialect())
      val format = setCsvFormat(dialect)
      readCSV(schema, tableUri, format, dialect)
      // Call validate csv function
    }
  }

  def setCsvFormat(dialect: Dialect): CSVFormat = {
    CSVFormat.DEFAULT
      .withDelimiter(dialect.delimiter)
      .withQuote(dialect.quoteChar)
      .withIgnoreEmptyLines(dialect.skipBlankRows)
      .withEscape(if (dialect.doubleQuote) '"' else '\\')
  }

  def readCSV(
      schema: TableGroup,
      tableUri: URI,
      format: CSVFormat,
      dialect: Dialect
  ) = {
    val parser = if (tableUri.getScheme == "file") {
      val tableCsvFile = new File(tableUri)
      if (!tableCsvFile.exists) {
        throw new MetadataError(
          "CSV NOT FOUND"
        ) // Change this to an error returned to the CLI
      }
      CSVParser
        .parse(
          tableCsvFile,
          mapAvailableCharsets(dialect.encoding),
          format
        )
    } else {
      CSVParser.parse(
        tableUri.toURL,
        mapAvailableCharsets(dialect.encoding),
        format
      )
    }

    val parserAfterSkippedRows = parser.asScala.drop(dialect.skipRows)

    val table = schema.tables(tableUri.toString)

    // List of type Any with same values are not added again in a Set, whereas Array of Type any behaves differently.
    var allPrimaryKeyValues: Set[List[Any]] = Set()
    for (row <- parserAfterSkippedRows) {
      if (row.getRecordNumber == 1 && dialect.header) {
        validateHeader(schema, row, tableUri)
      } else {
        if (row.size == 0) {
          warnings :+= ErrorWithCsvContext(
            "Blank rows",
            "structure",
            row.getRecordNumber.toString,
            "",
            "",
            ""
          )
        }
        val result = table.validateRow(row)
        result match {
          case Some(validateRowOutput) => {
            val primaryKeyValues = validateRowOutput.primaryKeyValues
            if (allPrimaryKeyValues.contains(primaryKeyValues)) {
              errors = errors :+ ErrorWithCsvContext(
                "duplicate_key",
                "schema",
                row.toString,
                "",
                s"key already present - ${fetchPrimaryKeyString(primaryKeyValues)}",
                ""
              )
            } else allPrimaryKeyValues += primaryKeyValues
          }
          case None => {}
        }
      }
    }
  }

  private def validateHeader(
      schema: TableGroup,
      header: CSVRecord,
      tableUri: URI
  ): Unit = {
    // If the dialect trim is set to true, do header.map{|h| h.strip! } if @dialect["trim"] == :true
    val WarningsAndErrors(w, e) =
      schema.validateHeader(header, tableUri.toString)
    warnings = warnings.concat(w)
    errors = errors.concat(e)
  }

  private def fetchPrimaryKeyString(list: List[Any]): String = {
    val stringList = list.map {
      case listOfAny: List[Any] =>
        listOfAny.map(s => s.toString).mkString(",")
      case i => i.toString
    }
    stringList.mkString(",")
  }

  private def processWarnings(errorMessage: ErrorWithCsvContext): String = {
    s"Type: ${errorMessage.`type`}, Category: ${errorMessage.category}, " +
      s"Row: ${errorMessage.row}, Column: ${errorMessage.column}, " +
      s"Content: ${errorMessage.content}, Constraints: ${errorMessage.constraints} \n"
  }

}
