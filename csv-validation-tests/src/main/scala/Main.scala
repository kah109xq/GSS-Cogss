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
  val path = "pwd".!!.trim
  s"$path/src/main/shell/GenerateTests.sh".!
}