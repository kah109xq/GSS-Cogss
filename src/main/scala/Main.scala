import CSVValidation.{MessageWithCsvContext, Validator}
import com.typesafe.scalalogging.Logger
import scopt.OParser

import java.io.File
import java.net.URI
case class Config(inputSchema: String = "", csvPath: String = "")
object Main extends App {
  val logger = Logger("Root")
  val builder = OParser.builder[Config]
  val parser = {
    import builder._
    OParser.sequence(
      programName("CSVW-Validation"),
      head("CSVW-Validation", "1.0"),
      opt[String]('s', "schema")
        .action((x, c) => c.copy(inputSchema = x))
        .text("filename of Schema/metadata file"),
      opt[String]('c', "csv")
        .action((x, c) => c.copy(csvPath = x))
        .text("filename of CSV file")
    )
  }

  OParser.parse(parser, args, Config()) match {
    case Some(config) =>
      if (config.inputSchema.isEmpty) {
        println((Console.GREEN + "Result"))
        println("Valid CSV-W")
      } else {
        val validator = new Validator(getAbsoluteSchemaUri(config.inputSchema))
        val errorsAndWarnings = validator.validate()
        if (errorsAndWarnings.warnings.nonEmpty) {
          println(Console.YELLOW + "Warnings")
          errorsAndWarnings.warnings.foreach(x =>
            logger.warn(getDescriptionForMessage(x))
          )
        }
        if (errorsAndWarnings.errors.nonEmpty) {
          println(Console.RED + "Error")
          errorsAndWarnings.errors.foreach(x =>
            logger.warn(getDescriptionForMessage(x))
          )
          print(Console.RESET + "")
          sys.exit(1)
        }
        println((Console.GREEN + "Result"))
        println("Valid CSV-W")
        print(Console.RESET + "")
      }
    case _ => throw new Exception("Invalid arguments")
  }

  private def getAbsoluteSchemaUri(schemaPath: String): URI = {
    val inputSchemaUri = new URI(schemaPath)
    if (inputSchemaUri.getScheme == null) {
      new URI(s"file://${new File(schemaPath).getAbsolutePath}")
    } else {
      inputSchemaUri
    }
  }

  private def getDescriptionForMessage(
      errorMessage: MessageWithCsvContext
  ): String = {
    s"Type: ${errorMessage.`type`}, Category: ${errorMessage.category}, " +
      s"Row: ${errorMessage.row}, Column: ${errorMessage.column}, " +
      s"Content: ${errorMessage.content}, Constraints: ${errorMessage.constraints} \n"
  }
}
