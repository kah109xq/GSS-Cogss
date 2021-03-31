import java.io._
import java.io.File
import sys.process._
import java.net.URL
import scala.language.postfixOps
import spray.json._
object Main extends App {

  def fileDownloader(url: String, filename: String) = {
    new URL(url) #> new File(filename) !!
  }
  println("Lets valid ate csv's..!")

  val baseUrl = "https://w3c.github.io/csvw/tests/"
  val currentDirectory = System.getProperty("user.dir")
  println(currentDirectory)

  val validationFeatureFilePath = currentDirectory + "/csvw_validation_tests.feature"
  val pw = new PrintWriter(new File(validationFeatureFilePath ))
  val manifestValidationUrl = s"${baseUrl}manifest-validation.jsonld"
  println(manifestValidationUrl)
  pw.write(s"# Auto-generated file based on standard validation CSVW tests from ${baseUrl}manifest-validation.jsonld")
  pw.close
  fileDownloader(manifestValidationUrl, "manifest-validation.jsonld")
//  Manifest json file read
  val input_file = "manifest-validation.jsonld"
  val source = scala.io.Source.fromFile(input_file)
  val lines = try source.mkString finally source.close()
  val manifest = lines.parseJson
  println(manifest)
}