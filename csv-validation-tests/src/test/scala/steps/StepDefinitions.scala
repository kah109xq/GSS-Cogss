import io.cucumber.scala.{EN, ScalaDsl}
import scala.io.Source
import org.slf4j.LoggerFactory

class StepDefinitions extends ScalaDsl with EN {
  private val log = LoggerFactory.getLogger(classOf[StepDefinitions])
  private val fixturesPath = "src/test/resources/features/fixtures/"

  Given("""^I have a CSV file called "(.*?)"$""") { (filename: String) =>
    //    log.info("Log me here..") Not working, need to fix
    val filePath = fixturesPath + filename
    val csv = Source.fromFile(filePath).getLines.mkString
  }

  Given("""^it is stored at the url "(.*?)"$""") { (url: String) =>
    // Implemented using stub_request in ruby implementation
    // Need to figure out purpose of this stub request, may be called during validation later?
  }

  Given("""^there is no file at the url "(.*?)"$""") { (url: String) =>
    // Implemented using stub_request in ruby implementation
    // Need to figure out purpose of this stub request, may be called during validation later?
  }

//  Given("I have a metadata file called {String}") { filename: String =>
//    // define
//  }
//
//  And("it is stored at the url {String}") { url:String =>
//    // define
//  }
//
//  And("the metadata is stored at the url {String}") { url: String =>
//    // define
//  }
//
//  And("I have a file called {String} at the url {String}") { (fileName: String, url:String) =>
//    // define
//  }
//
//  And("there is no file at the url {String}") { url: String =>
//    // define
//  }
//
//  And("there is no file at the url {String}") { url: String =>
//    // define
//  }
//
//  When("I carry out CSVW validation") { () =>
//    // define
//  }
//
//  Then("there should not be errors") { () =>
//    // define
//  }
//
//  And("there should not be warnings") { () =>
//    // define
//  }

}