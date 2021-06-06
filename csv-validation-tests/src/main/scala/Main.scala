import scopt.OParser
import CSVValidation.Validator
case class Config(inputCSV: String = "", inputSchema: String = "")
object Main extends App {
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
      println(errors(0))
    case None =>
  }
}