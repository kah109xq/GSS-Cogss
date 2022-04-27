package CSVValidation
import CSVValidation.traits.JavaIteratorExtensions.IteratorHasAsScalaArray
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.{
  JsonNodeFactory,
  ObjectNode,
  TextNode
}
import org.scalatest.FunSuite

import scala.collection.mutable.Map

class TableTest extends FunSuite {
  val objectMapper = new ObjectMapper()
  test("should create a table from pre-parsed CSV-W metadata") {
    val json =
      """{
        |  "@context": "http://www.w3.org/ns/csvw",
        |  "tables": [{
        |    "@id": "sample_id_value",
        |    "dialect": {"encoding": "utf-8"},
        |    "notes": ["sample value"],
        |    "url": "countries.csv",
        |    "suppressOutput": true,
        |    "@type": "Table",
        |    "tableSchema": {
        |      "@id": "sample_id_value",
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
        |      "primaryKey": "countryCode",
        |      "rowTitles": ["countryCode"]
        |    }
        |  }, {
        |    "url": "country_slice.csv",
        |    "@type": "Table",
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
        |      "primaryKey": "population",
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
    val tableObject1 = jsonNode.get("tables").elements().asScalaArray(0)
    val tableObject2 = jsonNode.get("tables").elements().asScalaArray(1)
    val table1 = Table.fromJson(
      tableObject1.asInstanceOf[ObjectNode],
      "http://w3c.github.io/csvw/tests/countries.json",
      "und",
      Map(),
      Map()
    )

    val table2 = Table.fromJson(
      tableObject2.asInstanceOf[ObjectNode],
      "http://w3c.github.io/csvw/tests/countries.json",
      "und",
      Map(),
      Map()
    )

    assert(table1.url === "http://w3c.github.io/csvw/tests/countries.csv")
    assert(table1.id.get === "http://w3c.github.io/csvw/tests/sample_id_value")
    assert(table1.columns.length === 4)
    val expectedDialect = JsonNodeFactory.instance.objectNode()
    expectedDialect.set("encoding", new TextNode("utf-8"))
    assert(table1.dialect.get === expectedDialect)
    assert(table2.foreignKeys.length === 1)
    assert(table2.foreignKeys(0).localColumns(0).name.get === "countryRef")
    assert(table1.notes.isDefined)
    assert(
      table1.notes.get.elements().asScalaArray(0) === new TextNode(
        "sample value"
      )
    )
    assert(table1.primaryKey(0).name.get === "countryCode")
    assert(table1.rowTitleColumns.length === 1)
    assert(table1.rowTitleColumns(0).name.get === "countryCode")
    assert(
      table1.schemaId.get === "sample_id_value"
    )
    assert(table1.suppressOutput === true)
    assert(table1.annotations.isEmpty)
    assert(table1.warnings.isEmpty)
  }

  test("should raise exception for duplicate column names") {
    val json =
      """{
        |  "@context": "http://www.w3.org/ns/csvw",
        |  "tables": [{
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
        |        "name": "year",
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
    val thrown = intercept[MetadataError] {
      Table.fromJson(
        jsonNode.get("tables").elements().next().asInstanceOf[ObjectNode],
        "http://w3c.github.io/csvw/tests/countries.json",
        "und",
        Map(),
        Map()
      )
    }
    assert(thrown.getMessage === "Multiple columns named year")
  }

  test(
    "should raise exception if virtual columns are found before non virtual columns"
  ) {
    val json =
      """{
        |  "@context": "http://www.w3.org/ns/csvw",
        |  "tables": [{
        |    "url": "country_slice.csv",
        |    "tableSchema": {
        |      "columns": [{
        |        "name": "countryRef",
        |        "titles": "countryRef",
        |        "valueUrl": "http://example.org/countries.csv{#countryRef}"
        |      }, {
        |        "name": "year",
        |        "titles": "year",
        |        "datatype": "gYear",
        |        "virtual": true
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
    val thrown = intercept[MetadataError] {
      Table.fromJson(
        jsonNode.get("tables").elements().next().asInstanceOf[ObjectNode],
        "http://w3c.github.io/csvw/tests/countries.json",
        "und",
        Map(),
        Map()
      )
    }
    assert(
      thrown.getMessage === "virtual columns before non-virtual column population (3)"
    )
  }

  test("should raise exception if url is not present for table") {
    val json =
      """{
        |  "@context": "http://www.w3.org/ns/csvw",
        |  "tables": [{
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
        |        "name": "year",
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
    val thrown = intercept[MetadataError] {
      Table.fromJson(
        jsonNode.get("tables").elements().next().asInstanceOf[ObjectNode],
        "http://w3c.github.io/csvw/tests/countries.json",
        "und",
        Map(),
        Map()
      )
    }
    assert(thrown.getMessage === "URL not found for table")
  }

  test("should raise exception if tableSchema is not an object") {
    val json =
      """{
        |  "@context": "http://www.w3.org/ns/csvw",
        |  "tables": [{
        |    "url": "country_slice.csv",
        |    "tableSchema": false
        |  }]
        |}""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val thrown = intercept[MetadataError] {
      Table.fromJson(
        jsonNode.get("tables").elements().next().asInstanceOf[ObjectNode],
        "http://w3c.github.io/csvw/tests/countries.json",
        "und",
        Map(),
        Map()
      )
    }
    assert(
      thrown.getMessage === "Table schema must be object for table http://w3c.github.io/csvw/tests/country_slice.csv "
    )
  }

  test("should throw an exception when @type is not Table") {
    val json =
      """{
        |  "@context": "http://www.w3.org/ns/csvw",
        |  "tables": [{
        |    "url": "country_slice.csv",
        |    "@type": "Table!@£!@£"
        |  }]
        |}""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val thrown = intercept[MetadataError] {
      Table.fromJson(
        jsonNode.get("tables").elements().next().asInstanceOf[ObjectNode],
        "http://w3c.github.io/csvw/tests/countries.json",
        "und",
        Map(),
        Map()
      )
    }
    assert(
      thrown.getMessage === "@type of table is not 'Table' - country_slice.csv.@type"
    )
  }

  test(
    "sets scala test for Array[Any]"
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
      exampleSet2.size == 1
    ) // @Rob This test will since we are using Array, but for list it will work. In our case we could use Lists and get the expected result

  }
}
