import scopt.OParser
import CSVValidation.{ErrorWithCsvContext, Validator}
import com.typesafe.scalalogging.Logger

import java.io.File
import java.net.URI
case class Config(inputSchema: String = "")
object Main extends App {
  val logger = Logger("Root")
  val parser = new scopt.OptionParser[Config]("csvwvalidation") {
    head("CSVW-Validation", "1.0")
    arg[String]("<schemaFilePath>")
      .action { (x, c) => c.copy(inputSchema = x) }
      .text("filename of schema")
      .optional()
  }

  parser.parse(args, Config()) match {
    case Some(config) =>
      val validator = new Validator(getAbsoluteSchemaUri(config.inputSchema))
      val errorsAndWarnings = validator.validate()
      if (errorsAndWarnings.warnings.nonEmpty) {
        println(Console.YELLOW + "Warnings")
        errorsAndWarnings.warnings.foreach(x =>
          logger.warn(processWarningsAndErrors(x))
        )
      }
      if (errorsAndWarnings.errors.nonEmpty) {
        println(Console.RED + "Error")
        errorsAndWarnings.errors.foreach(x =>
          logger.warn(processWarningsAndErrors(x))
        )
        print(Console.RESET + "")
        sys.exit(1)
      }
      println((Console.GREEN + "Result"))
      println("Valid CSV-W")
    case None =>
  }

  print(Console.RESET + "")

  private def getAbsoluteSchemaUri(schemaPath: String): URI = {
    val inputSchemaUri = new URI(schemaPath)
    if (inputSchemaUri.getScheme == null) {
      new URI(s"file://${new File(schemaPath).getAbsolutePath}")
    } else {
      inputSchemaUri
    }
  }

  private def processWarningsAndErrors(
      errorMessage: ErrorWithCsvContext
  ): String = {
    s"Type: ${errorMessage.`type`}, Category: ${errorMessage.category}, " +
      s"Row: ${errorMessage.row}, Column: ${errorMessage.column}, " +
      s"Content: ${errorMessage.content}, Constraints: ${errorMessage.constraints} \n"
  }
}
