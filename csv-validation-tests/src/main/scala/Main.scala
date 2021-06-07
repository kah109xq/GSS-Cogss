import scopt.OParser
import CSVValidation.Validator
import com.typesafe.scalalogging.Logger
case class Config(inputCSV: String = "", inputSchema: String = "")
object Main extends App {
  val logger = Logger("Root")
  val parser = new scopt.OptionParser[Config]("csvwvalidation") {
    head("CSVW-Validation", "1.0")
    arg[String]("<file_path>") action { (x, c) =>
      c.copy(inputCSV = x)
    } text "filename of source csv"
    arg[String]("<file_path>") action { (x, c) =>
      c.copy(inputSchema = x)
    } text "filename of schema"
  }

  parser.parse(args, Config()) match {
    case Some(config) =>
      var validator = new Validator(config.inputCSV)
      val (errors, warnings) = validator.validate()
      logger.info("Hello there!")
      println(Console.BLUE + errors(0))
      logger.error(errors(0))
    case None =>
  }
}