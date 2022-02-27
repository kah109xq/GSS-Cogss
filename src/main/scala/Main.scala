import scopt.OParser
import CSVValidation.Validator
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
      val result = validator.validate()
      result match {
        case Right(warnings) => {
          println(Console.YELLOW + "Warnings")
          warnings.foreach(x => logger.warn(x))
        }
        case Left(errorMessage) => {
          println(Console.RED + "Error")
          logger.error(errorMessage)
          sys.exit(1)
        }
      }
      println((Console.GREEN + "Result"))
      println("Valid metadata")
    case None =>
  }

  private def getAbsoluteSchemaUri(schemaPath: String): URI = {
    val inputSchemaUri = new URI(schemaPath)
    if (inputSchemaUri.getScheme == null) {
      new URI(s"file://${new File(schemaPath).getAbsolutePath}")
    } else {
      inputSchemaUri
    }
  }
}
