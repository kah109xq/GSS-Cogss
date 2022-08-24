import CSVValidation.{MessageWithCsvContext, Validator}
import akka.actor.ActorSystem
import akka.stream.scaladsl.Sink
import com.typesafe.scalalogging.Logger
import scopt.OParser

import scala.concurrent.Await
import scala.concurrent.duration.Duration
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
      implicit val system: ActorSystem = ActorSystem("actor-system")
      val validator = new Validator(config.inputSchema)
      val akkaStream = validator
        .validate()
        .map(warningsAndErrors => {
          if (warningsAndErrors.warnings.nonEmpty) {
            println(Console.YELLOW + "Warnings")
            warningsAndErrors.warnings
              .foreach(x => logger.warn(getDescriptionForMessage(x)))
          }
          if (warningsAndErrors.errors.nonEmpty) {
            println(Console.RED + "Error")
            warningsAndErrors.errors
              .foreach(x => logger.warn(getDescriptionForMessage(x)))
            print(Console.RESET + "")
            sys.exit(1)
          }
          println(Console.GREEN + "Result")
          println("Valid CSV-W")
          print(Console.RESET + "")
        })
      Await.ready(akkaStream.runWith(Sink.ignore), Duration.Inf)
      system.terminate()
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
