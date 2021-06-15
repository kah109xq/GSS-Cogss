package CSVValidation
import java.io._
import scala.concurrent._
import scala.util.{Failure, Success}
import ExecutionContext.Implicits.global
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import akka.NotUsed
import akka.util.ByteString
import java.nio.file.Paths
import java.nio.file.Path

// This is only a skeleton for the core Validator class to be created. It is planned to accept the source csv and
// schema and return errors and warnings collection when validate method is invoked. Changes to this signature can be
// made in the due course or as per requirements
class Validator(var source: String, var schema: String = "") {
  def validate(): (Array[String], Array[String]) = {
    try {
      var errors = Array[String]()
      val lineDelimiter: Flow[ByteString, ByteString, NotUsed] =
        Framing.delimiter(
          ByteString("\n"),
          maximumFrameLength = 256,
          allowTruncation = true
        )
      if (scala.reflect.io.File(source).exists) {
        implicit val system = ActorSystem("actor-system")
        implicit val materialzier = ActorMaterializer()
        val p1 = Paths.get(source)
        FileIO
          .fromPath(p1)
          .via(lineDelimiter)
          .map(byteString => byteString.utf8String)
          .mapAsyncUnordered(parallelism = 10)(processRowValue(_))
          .runWith(Sink.foreach(doNothing))
          .onComplete(_ => system.terminate())
        setErrorsAndWarnings(Array[String]())
      } else {
        errors = errors:+ "File not found"
        setErrorsAndWarnings(errors)
      }
    } catch {
      // Catch specific exceptions and handle them here
      case _: Throwable => {
        var errors = Array[String]()
        errors = errors:+ "Some unknown error occured"
        setErrorsAndWarnings(errors)
      }
    }
  }


  def doNothing(x: String) = None
  def isAllDigits(x: String) = x forall Character.isDigit

  def setErrorsAndWarnings(errors: Array[String]): (Array[String], Array[String]) = {
    var warnings = Array[String]()
    return (errors, warnings)
  }

  def processRowValue(str: String): Future[String] = Future {
    var columnValues = str.split(",")
    var returnValues: Array[String] = Array.empty
    for (cv <- columnValues) {
      if (isAllDigits(cv)) {
        Thread.sleep(1)
        returnValues = returnValues :+ (cv.toFloat + 10).toString().concat("Added 10")
      } else if (cv.toDoubleOption.isDefined) {
        returnValues = returnValues :+ (cv.toFloat + 111).toString().concat("Added 111")
      } else {
        returnValues = returnValues :+ cv.concat("hello world")
      }
    }
    returnValues.mkString(", ")
  }

}