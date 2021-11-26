import scopt.OParser
import CSVValidation.Validator
import com.typesafe.scalalogging.Logger
case class Config(inputCSV: String = "", inputSchema: String = "")
object Main extends App {
  val logger = Logger("Root")
  val parser = new scopt.OptionParser[Config]("csvwvalidation") {
    head("CSVW-Validation", "1.0")
    arg[String]("<source_file_path>")
      .action { (x, c) => c.copy(inputCSV = x) }
      .text("filename of source csv")
    arg[String]("<schema_file_path>")
      .action { (x, c) => c.copy(inputSchema = x) }
      .text("filename of schema")
      .optional()
  }

  parser.parse(args, Config()) match {
    case Some(config) =>
      var validator = new Validator(config.inputCSV)
      val (errors, warnings) = validator.validate()
      println(Console.YELLOW + "Warnings")
      warnings.map(x => logger.warn(x))
      println(Console.RED + "errors")
      errors.map(x => logger.error(x))
    case None =>
  }
}