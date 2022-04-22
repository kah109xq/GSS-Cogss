package CSVValidation
import org.scalatest.FunSuite

import java.io.File
import java.net.URI

class ValidatorTest extends FunSuite {
  test("set warning when title is empty for a column") {
    val uri = new URI(
      s"file://${new File("src/test/resources/csvwExamples/observations_missing_headers.csv-metadata.json").getAbsolutePath}"
    )
    val validator = new Validator(uri)
    validator.validate()
    assert(validator.warnings.length === 1)
    val warning = validator.warnings(0)
    assert(warning.`type` === "Empty column name")
    assert(warning.column === "2")
    assert(warning.category === "Schema")
  }

  test(
    "set error when title is empty for a column and specified in the metadata file"
  ) {
    val uri = new URI(
      s"file://${new File("src/test/resources/csvwExamples/observations_missing_headers.csv-metadata.json").getAbsolutePath}"
    )
    val validator = new Validator(uri)
    validator.validate()
    assert(validator.errors.length === 1)
    val error = validator.errors(0)
    assert(error.`type` === "Invalid Header")
    assert(error.row === "1")
    assert(error.column === "2")
    assert(error.content === "")
  }
  test(
    "set warnings when duplicate titles are present"
  ) {
    val uri = new URI(
      s"file://${new File("src/test/resources/csvwExamples/observations_duplicate_headers.csv-metadata.json").getAbsolutePath}"
    )
    val validator = new Validator(uri)
    validator.validate()
    assert(validator.warnings.length === 1)
    val warning = validator.warnings(0)
    assert(warning.`type` === "Duplicate column name")
    assert(warning.column === "3")
    assert(warning.content === "Age")
  }

  test(
    "set error when title is found in csv file is different from the metadata file"
  ) {
    val uri = new URI(
      s"file://${new File("src/test/resources/csvwExamples/observations_duplicate_headers.csv-metadata.json").getAbsolutePath}"
    )
    val validator = new Validator(uri)
    validator.validate()
    assert(validator.errors.length === 1)
    val error = validator.errors(0)
    assert(error.`type` === "Invalid Header")
    assert(error.column === "2")
    assert(error.content === "Age")
  }
}
