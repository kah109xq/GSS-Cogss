package CSVValidation

import org.apache.commons.csv.{CSVFormat, CSVParser, CSVRecord}

import java.io.File
import java.net.URI
import java.nio.charset.StandardCharsets
import scala.jdk.CollectionConverters.IterableHasAsScala

// This is only a skeleton for the core Validator class to be created. It is planned to accept the source csv and
// schema and return errors and warnings collection when validate method is invoked. Changes to this signature can be
// made in the due course or as per requirements
class Validator(var tableCsvFile: URI, sourceUri: String = "") {
  val isCsvHeader = true
  var warnings: Array[ErrorMessage] = Array()
  var errors: Array[ErrorMessage] = Array()
  var schema: Option[TableGroup] = None
  var source: String = "" // Define how this is set, will it always be string?
  val csvHeaderExpected = true

  def validate(): Either[String, Array[String]] = {
    val mayBeTableGroupObject = Schema.loadMetadataAndValidate(tableCsvFile)
    mayBeTableGroupObject match {
      case Right(tableGroup) => {
        schema = Some(tableGroup)
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
      val tableUri = new URI(tableUrl)
      readCSV(tableUri)
      // Call validate csv function
    }
  }

  def readCSV(tableUri: URI) = {
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
          StandardCharsets.UTF_8,
          CSVFormat.DEFAULT
        )
    } else {
      CSVParser.parse(
        tableUri.toURL,
        StandardCharsets.UTF_8,
        CSVFormat.DEFAULT
      )
    }
    for (row <- parser.asScala) {
      if (row.getRecordNumber == 1 && csvHeaderExpected) {
        validateHeader(row, tableUri)
      } else {
        if (row.size == 0) {
          warnings :+= ErrorMessage(
            "Blank rows",
            "structure",
            row.getRecordNumber.toString,
            "",
            "",
            ""
          )
        }
        schema.get.validateRow(row, tableUri.toString)
      }
    }
  }

  private def validateHeader(header: CSVRecord, tableUri: URI): Unit = {
    // If the dialect trim is set to true, do header.map{|h| h.strip! } if @dialect["trim"] == :true
    schema match {
      case Some(tableGroup) => {
        val WarningsAndErrors(w, e) =
          tableGroup.validateHeader(header, tableUri.toString)
        warnings = warnings.concat(w)
        errors = errors.concat(e)
      }
    }
  }

  private def processWarnings(errorMessage: ErrorMessage): String = {
    s"Type: ${errorMessage.`type`}, Category: ${errorMessage.category}, " +
      s"Row: ${errorMessage.row}, Column: ${errorMessage.column}, " +
      s"Content: ${errorMessage.content}, Constraints: ${errorMessage.constraints} \n"
  }
}
