import CSVValidation.{MessageWithCsvContext, Validator}
import com.typesafe.scalalogging.Logger
import scopt.OParser

import java.io.File
import java.net.URI
case class Config(
    inputSchema: Option[String] = None,
    csvPath: Option[String] = None
)
object Main extends App {
  val logger = Logger("Root")
  val builder = OParser.builder[Config]
  val parser = {
    import builder._
    OParser.sequence(
      programName("CSVW-Validation"),
      head("CSVW-Validation", "1.0"),
      opt[String]('s', "schema")
        .action((x, c) => c.copy(inputSchema = Some(x)))
        .text("filename of Schema/metadata file"),
      opt[String]('c', "csv")
        .action((x, c) => c.copy(csvPath = Some(x)))
        .text("filename of CSV file")
    )
  }

  OParser.parse(parser, args, Config()) match {
    case Some(config) =>
      val validator = new Validator(config.inputSchema.get)
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
    case _ => throw new Exception("Invalid arguments")
  }

  private def getDescriptionForMessage(
      errorMessage: MessageWithCsvContext
  ): String = {
    s"Type: ${errorMessage.`type`}, Category: ${errorMessage.category}, " +
      s"Row: ${errorMessage.row}, Column: ${errorMessage.column}, " +
      s"Content: ${errorMessage.content}, Constraints: ${errorMessage.constraints} \n"
  }
}
