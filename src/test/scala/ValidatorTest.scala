package CSVValidation
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.scalatest.FunSuite

import java.io.File
import java.net.URI
import java.time.{ZoneId, ZonedDateTime}
import scala.collection.mutable

class ValidatorTest extends FunSuite {
  val csvwExamplesBaseDir = "src/test/resources/csvwExamples/"
  val objectMapper = new ObjectMapper()
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
      error.content.contains("key already present")
    )
    assert(error.category === "schema")
  }

  test(
    "it should NOT set primary key violation if datetime value is equal in UTC and the timezones differ"
  ) {
    val uri = new URI(
      s"file://${new File(s"${csvwExamplesBaseDir}observations_primary_key_datetime.csv-metadata.json").getAbsolutePath}"
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
      error.content.contains("key already present")
    )
    assert(error.category === "schema")
  }

  test(
    "it should not set foreign key violation errors for correct foreign key references"
  ) {
    val uri = new URI(
      s"file://${new File(s"${csvwExamplesBaseDir}foreignKeyValidationTest.csv-metadata.json").getAbsolutePath}"
    )
    val validator = new Validator(uri)
    validator.validate()
    assert(validator.errors.length === 0)
  }

  // Scala Sets are used to check for duplicates in PrimaryKeys. PrimaryKey columns received back in the Validator class will be a collection of Any type.
  // This test ensures that a List of type Any with same values are not added again in a Set, whereas Array of Type any behaves differently.
  test(
    "sets scala test for Array[Any] and List[Any]"
  ) {
    var exampleSet: Set[List[Any]] = Set()
    exampleSet += List[Any](12, 1, 4, "Abcd")
    exampleSet += List[Any](12, 1, 4, "Abcd")
    exampleSet += List[Any](12, 1, 4, "Abcd")

    assert(exampleSet.size == 1)

    var exampleSet2: Set[Array[Any]] = Set()

    exampleSet2 += Array[Any](12, 1, 4, "Abcd")
    exampleSet2 += Array[Any](12, 1, 4, "Abcd")
    exampleSet2 += Array[Any](12, 1, 4, "Abcd")
    assert(
      exampleSet2.size == 3
    )

    var exampleSet3: Set[List[Any]] = Set()
    val zone = ZoneId.of("UTC+1")
    ZonedDateTime.of(1947, 8, 15, 12, 12, 12, 12, zone)
    exampleSet3 += List[Any](
      12,
      1,
      4,
      "Abcd",
      ZonedDateTime.of(1947, 8, 15, 12, 12, 12, 12, zone)
    )
    exampleSet3 += List[Any](
      12,
      1,
      4,
      "Abcd",
      ZonedDateTime.of(1947, 8, 15, 12, 12, 12, 12, zone)
    )
    exampleSet3 += List[Any](
      12,
      1,
      4,
      "Abcd",
      ZonedDateTime.of(1947, 8, 15, 12, 12, 12, 12, zone)
    )

    assert(exampleSet3.size == 1)
  }

  test("asdasd") {

    val set = mutable.Set[KeyWithContext]()
    val val1 = KeyWithContext(1, List(1, 2, 3))
    val val2 = KeyWithContext(89, List(1, 2, 3))
    set += val1
    set += val2
    assert(set.size == 1)
  }
}
