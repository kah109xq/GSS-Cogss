package CSVValidation
import org.scalatest.FunSuite

import java.io.File
import java.net.URI

class ValidatorTest extends FunSuite {
  val csvwExamplesBaseDir = "src/test/resources/csvwExamples/"
  test("set warning when title is empty for a column") {
    val uri = new URI(
      s"file://${new File(s"${csvwExamplesBaseDir}observations_missing_headers.csv-metadata.json").getAbsolutePath}"
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
      s"file://${new File(s"${csvwExamplesBaseDir}observations_missing_headers.csv-metadata.json").getAbsolutePath}"
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
      s"file://${new File(s"${csvwExamplesBaseDir}observations_duplicate_headers.csv-metadata.json").getAbsolutePath}"
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
      s"file://${new File(s"${csvwExamplesBaseDir}observations_duplicate_headers.csv-metadata.json").getAbsolutePath}"
    )
    val validator = new Validator(uri)
    validator.validate()
    assert(validator.errors.length === 1)
    val error = validator.errors(0)
    assert(error.`type` === "Invalid Header")
    assert(error.column === "2")
    assert(error.content === "Age")
  }

  test("should set error when primary keys are not unique") {
    val uri = new URI(
      s"file://${new File(s"${csvwExamplesBaseDir}observations_duplicate_primary_key.csv-metadata.json").getAbsolutePath}"
    )
    val validator = new Validator(uri)
    validator.validate()
    assert(validator.errors.length === 1)
    val error = validator.errors(0)
    assert(error.`type` === "duplicate_key")
    assert(
      error.content === "key already present - W00000001,owned-owned-with-a-mortgage-or-loan-or-shared-ownership,Y0T15,very-good-or-good-health"
    )
    assert(error.category === "schema")
  }

  test(
    "it should NOT set primary key violation if datetime value is equal in UTC and the strings representing them differ"
  ) {
    val uri = new URI(
      s"file://${new File(s"${csvwExamplesBaseDir}observations_primary_key_violation(datetime).csv-metadata.json").getAbsolutePath}"
    )
    val validator = new Validator(uri)
    validator.validate()
    assert(validator.errors.length === 0)
  }

  test(
    "it should set primary key violation when decimal value is equal even if the strings representing them differ"
  ) {
    val uri = new URI(
      s"file://${new File(s"${csvwExamplesBaseDir}obs_decimal_primary_key_vio.csv-metadata.json").getAbsolutePath}"
    )
    val validator = new Validator(uri)
    validator.validate()
    assert(validator.errors.length === 1)
    val error = validator.errors(0)
    assert(error.`type` === "duplicate_key")
    assert(
      error.content === "key already present - W00000001,6.45,Y16T49,very-good-or-good-health"
    )
    assert(error.category === "schema")
  }
}
