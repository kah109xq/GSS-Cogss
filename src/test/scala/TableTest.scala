package CSVValidation
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.{JsonNodeFactory, ObjectNode}
import org.scalatest.FunSuite
import scala.collection.mutable.Map

class TableTest extends FunSuite {
  val objectMapper = new ObjectMapper()
  test("should create a table from pre-parsed CSV-W metadata") {
    val json =
      """{
        |  "@context": "http://www.w3.org/ns/csvw",
        |  "tables": [{
        |    "url": "countries.csv",
        |    "tableSchema": {
        |      "columns": [{
        |        "name": "countryCode",
        |        "titles": "countryCode",
        |        "datatype": "string",
        |        "propertyUrl": "http://www.geonames.org/ontology{#_name}"
        |      }, {
        |        "name": "latitude",
        |        "titles": "latitude",
        |        "datatype": "number"
        |      }, {
        |        "name": "longitude",
        |        "titles": "longitude",
        |        "datatype": "number"
        |      }, {
        |        "name": "name",
        |        "titles": "name",
        |        "datatype": "string"
        |      }],
        |      "aboutUrl": "http://example.org/countries.csv{#countryCode}",
        |      "propertyUrl": "http://schema.org/{_name}",
        |      "primaryKey": "countryCode"
        |    }
        |  }, {
        |    "url": "country_slice.csv",
        |    "tableSchema": {
        |      "columns": [{
        |        "name": "countryRef",
        |        "titles": "countryRef",
        |        "valueUrl": "http://example.org/countries.csv{#countryRef}"
        |      }, {
        |        "name": "year",
        |        "titles": "year",
        |        "datatype": "gYear"
        |      }, {
        |        "name": "population",
        |        "titles": "population",
        |        "datatype": "integer"
        |      }],
        |      "foreignKeys": [{
        |        "columnReference": "countryRef",
        |        "reference": {
        |          "resource": "countries.csv",
        |          "columnReference": "countryCode"
        |        }
        |      }]
        |    }
        |  }]
        |}""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val table = Table.fromJson(
      jsonNode.get("tables").elements().next().asInstanceOf[ObjectNode],
      "http://w3c.github.io/csvw/tests/countries.json",
      "und",
      Map(),
      Map()
    )

    assert(table.url === "http://w3c.github.io/csvw/tests/countries.csv")
    assert(table.columns.length === 4)

  }
}
