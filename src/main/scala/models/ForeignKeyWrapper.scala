package CSVValidation

import com.fasterxml.jackson.databind.node.ObjectNode

case class ForeignKeyWrapper(
    jsonObject: ObjectNode,
    localColumns: Array[Column]
) {}
