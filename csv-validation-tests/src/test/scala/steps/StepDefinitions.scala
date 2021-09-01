import io.cucumber.scala.{EN, ScalaDsl}
import scala.io.Source
import org.slf4j.LoggerFactory
import sttp.client3._
import sttp.model._
import sttp.client3.testing._

class StepDefinitions extends ScalaDsl with EN {
  // Definitions for steps in scenarios given in csvw_validation_tests.feature
  // Adapted from https://github.com/Data-Liberation-Front/csvlint.rb/tree/master/features/step_definitions
  private val log = LoggerFactory.getLogger(classOf[StepDefinitions])
  private val fixturesPath = "src/test/resources/features/fixtures/"
  private var csv:String = ""
  private var metadata:String = ""
  private var content:String = ""
  private var schemaUrl:String = ""
  private var fileUrl = ""
  private var contextUrl:String = ""
  private var testingBackend = None
  private var notFoundStartsWith = List[String]()
  private var notFountEndsWith =  List[String]()

  // Assume we use sttp as http client.
  // Call this funciton and set the testing backend object. Pass the backend object into validator function
  // Stubbing for sttp is done as given in their docs at https://sttp.softwaremill.com/en/latest/testing.html
  def setTestingBackend() = {
    var testingBackend = SttpBackendStub.synchronous
      .whenRequestMatchesPartial({
        case r if r.uri.path.startsWith(List(fileUrl)) =>
          Response.ok(content)
        case r if r.uri.path.startsWith(List(schemaUrl)) =>
          Response.ok(metadata)
        case r if r.uri.path.startsWith(List(contextUrl)) =>
          Response.ok(csv)
        case r if r.uri.path.endsWith(notFountEndsWith) =>
          Response("Not found", StatusCode.NotFound)
        case r if r.uri.path.endsWith(notFoundStartsWith) =>
        Response("Not found", StatusCode.NotFound)
      })
  }

  Given("""^I have a CSV file called "(.*?)"$""") { (filename: String) =>
    val filePath = fixturesPath + filename
    csv = Source.fromFile(filePath).getLines.mkString
  }

  Given("""^it is stored at the url "(.*?)"$""") { (url: String) =>
    contextUrl = url
    // notFoundEndsWith is a list which holds all the url endwith strings for which a 404 should be returned.
    // This list is later used in setTestingBackend function to mock http calls
    notFountEndsWith = "/.well-known/csvm" :: notFountEndsWith
    notFountEndsWith = "-metadata.json" :: notFountEndsWith
    notFountEndsWith = "csv-metadata.json" :: notFountEndsWith
  }

  Given("""^there is no file at the url "(.*?)"$""") { (url: String) =>
    // notFoundStartsWith is a list which holds all the url startwith strings for which a 404 should be returned.
    // This list is later used in setTestingBackend function to mock http calls
    notFoundStartsWith = url :: notFoundStartsWith
  }

  Given("""^I have a metadata file called "([^"]*)"$""") { filename: String =>
    val filePath = fixturesPath + filename
    metadata = Source.fromFile(filePath).getLines.mkString
  }

  And("""^the (schema|metadata) is stored at the url "(.*?)"$""") { (schemaType:String, url:String) =>
    schemaUrl = url
  }

  And("""^I have a file called "(.*?)" at the url "(.*?)"$""") { (fileName: String, url:String) =>
    val filePath = fixturesPath + fileName
    content = Source.fromFile(filePath).getLines.mkString
    fileUrl = url
  }

  When("I carry out CSVW validation") { () =>
    throw new io.cucumber.scala.PendingException()
  }

  Then("there should not be errors") { () =>
    throw new io.cucumber.scala.PendingException()
  }

  And("there should not be warnings") { () =>
    throw new io.cucumber.scala.PendingException()
  }
}