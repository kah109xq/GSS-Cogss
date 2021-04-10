import java.io._
import java.io.File
import sys.process._
import java.net.URL
import spray.json._
import scala.language.postfixOps
import org.apache.jena.rdf.model.{ModelFactory, Property, RDFList}
import org.apache.jena.riot.{Lang, RDFDataMgr}
import org.apache.jena.vocabulary.XSD
import org.apache.jena.rdf.model._
import org.apache.jena.vocabulary._
import jdk.internal.loader.Resource

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
  pw.write(s"# Auto-generated file based on standard validation CSVW tests from ${baseUrl}manifest-validation.jsonld \n\n")

  fileDownloader(manifestValidationUrl, "manifest-validation.jsonld")

  private val model = ModelFactory.createDefaultModel
//  model.read("manifest-validation.jsonld", "JSONLD")
  model.read("https://w3c.github.io/csvw/tests/manifest-validation.jsonld")
  val pw2 = new PrintWriter(new File("model.txt" ))
  model.write(pw2)
  pw2.close
  val manifest = model.getResource("https://w3c.github.io/csvw/tests/manifest-validation")
  val label = manifest.getProperty(RDFS.label).getString
  pw.write(s"Feature: ${label} \n\n")

  val entries = model.getProperty("http://www.w3.org/2001/sw/DataAccess/tests/test-manifest#entries")
  val entriesList = manifest.getProperty(entries).getList()
  val name = model.createProperty("http://www.w3.org/2001/sw/DataAccess/tests/test-manifest#name")


  val items = entriesList.iterator
  while ( {
    items.hasNext
  }) {
    val item = items.next.asResource
    pw.write(s"\t# ${item.getProperty(RDFS.comment).getString} \n")
    pw.write(s"\t# ${item.getURI} \n")
    val scenarioName = (item.getProperty(name).getString).replace("<", "less than")
    pw.write(s"\tScenario: ${item.getURI.split("tests/").last} ${scenarioName} \n\n")
//    file.puts "\tScenario: #{entry["id"]} #{entry["name"].gsub("<", "less than")}"
//    val value1 = item.getRequiredProperty(myitemvalue1).getObject
//    val value2 = item.getRequiredProperty(myitemvalue2).getObject
//    System.out.println(item + " has:\n\tvalue1: " + value1 + "\n\tvalue2: " + value2)
  }
// String substitution scala
    //.replace("${begindate}", begindate)

//  Sample rdf graph read working code in scala
//  private val modelExample = ModelFactory.createDefaultModel
//  modelExample.read("vc-db-1.rdf")
//
//  val vcard = modelExample.getResource("http://somewhere/JohnSmith")
//  vcard.addProperty(VCARD.NICKNAME, "Ajay Joseph")
//  vcard.addProperty(VCARD.NICKNAME, "Adman")
//  val pw2 = new PrintWriter(new File("modelExample.txt" ))
//  modelExample.write(pw2)
//  pw2.close
//  val fullName: String = vcard.getProperty(VCARD.FN).getString
//  println(fullName)
  pw.close
}