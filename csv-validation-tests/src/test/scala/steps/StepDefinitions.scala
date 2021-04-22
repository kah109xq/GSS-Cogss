import io.cucumber.scala.{ScalaDsl, EN}

class StepDefinitions extends ScalaDsl with EN {

  Given("I have a CSV file called {String}") { fileName: String =>
    val contents = io.Source.fromFile(fileName).mkString
  }

  Given("I have a metadata file called {String}") { filename: String =>
    // define
  }

  And("it is stored at the url {String}") { url:String =>
    // define
  }

  And("the metadata is stored at the url {String}") { url: String =>
    // define
  }

  And("I have a file called {String} at the url {String}") { (fileName: String, url:String) =>
    // define
  }

  And("there is no file at the url {String}") { url: String =>
    // define
  }

  And("there is no file at the url {String}") { url: String =>
    // define
  }

  When("I carry out CSVW validation") { () =>
    // define
  }

  Then("there should not be errors") { () =>
    // define
  }

  And("there should not be warnings") { () =>
    // define
  }

}