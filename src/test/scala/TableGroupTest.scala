package CSVValidation
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.{JsonNodeFactory, ObjectNode}
import org.scalatest.FunSuite

class TableGroupTest extends FunSuite {
  val objectMapper = new ObjectMapper()
  test("should create table group object from pre parsed metadata") {
    val json =
      """
        |{
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
        |}
        |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val tableGroup = TableGroup.fromJson(
      jsonNode.asInstanceOf[ObjectNode],
      "http://w3c.github.io/csvw/tests/countries.json"
    )
    assert(tableGroup.id.isEmpty)
    assert(tableGroup.tables.size === 2)
    assert(tableGroup.annotations.size === 0)
  }
  test("should inherit null to all columns") {
    val json =
      """
        |{
        |  "@context": "http://www.w3.org/ns/csvw",
        |  "null": true,
        |  "tables": [{
        |    "url": "test040.csv",
        |    "tableSchema": {
        |      "columns": [{
        |        "titles": "null"
        |      }, {
        |        "titles": "lang"
        |      }, {
        |        "titles": "textDirection"
        |      }, {
        |        "titles": "separator"
        |      }, {
        |        "titles": "ordered"
        |      }, {
        |        "titles": "default"
        |      }, {
        |        "titles": "datatype"
        |      }, {
        |        "titles": "aboutUrl"
        |      }, {
        |        "titles": "propertyUrl"
        |      }, {
        |        "titles": "valueUrl"
        |      }]
        |    }
        |  }]
        |}
        |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val tableGroup = TableGroup.fromJson(
      jsonNode.asInstanceOf[ObjectNode],
      "http://w3c.github.io/csvw/tests/test040-metadata.json"
    )
    val table =
      tableGroup.tables("http://w3c.github.io/csvw/tests/test040.csv")

    assert(tableGroup.annotations.size === 0)
    assert(tableGroup.warnings.length === 1)
    assert(tableGroup.warnings(0).`type` === "invalid_value")
    assert(tableGroup.warnings(0).content === "null : true")
    assert(tableGroup.warnings(0).category === "metadata")
    assert(tableGroup.tables.size === 1)
    assert(table.columns.length === 10)
    assert(
      table.columns(0).nullParam === Array("")
    ) // should inherit null to all columns - assertion which justifies test name
  }

  test(
    "should work as expected when tables key is not present (just one table)"
  ) {
    val json =
      """
        |{
        |    "@context": ["http://www.w3.org/ns/csvw", {"@language": "en"}],
        |    "url": "tree-ops.csv",
        |    "dc:title": "Tree Operations",
        |    "dcat:keyword": ["tree", "street", "maintenance"],
        |    "dc:publisher": {
        |      "schema:name": "Example Municipality",
        |      "schema:url": {"@id": "http://example.org"}
        |    },
        |    "dc:license": {"@id": "http://opendefinition.org/licenses/cc-by/"},
        |    "dc:modified": {"@value": "2010-12-31", "@type": "xsd:date"},
        |    "tableSchema": {
        |      "columns": [{
        |        "name": "GID",
        |        "titles": ["GID", "Generic Identifier"],
        |        "dc:description": "An identifier for the operation on a tree.",
        |        "datatype": "string",
        |        "required": true
        |      }, {
        |        "name": "on_street",
        |        "titles": "On Street",
        |        "dc:description": "The street that the tree is on.",
        |        "datatype": "string"
        |      }, {
        |        "name": "species",
        |        "titles": "Species",
        |        "dc:description": "The species of the tree.",
        |        "datatype": "string"
        |      }, {
        |        "name": "trim_cycle",
        |        "titles": "Trim Cycle",
        |        "dc:description": "The operation performed on the tree.",
        |        "datatype": "string"
        |      }, {
        |        "name": "inventory_date",
        |        "titles": "Inventory Date",
        |        "dc:description": "The date of the operation that was performed.",
        |        "datatype": {"base": "date", "format": "M/d/yyyy"}
        |      }],
        |      "primaryKey": "GID",
        |      "aboutUrl": "#gid-{GID}"
        |    }
        |  }
        |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val tableGroup = TableGroup.fromJson(
      jsonNode.asInstanceOf[ObjectNode],
      "http://w3c.github.io/csvw/tests/test040-metadata.json"
    )
    val table =
      tableGroup.tables("http://w3c.github.io/csvw/tests/tree-ops.csv")

    assert(tableGroup.tables.size === 1)
    assert(table.columns.length === 5)
  }

}
