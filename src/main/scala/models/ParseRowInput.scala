package CSVValidation

import org.apache.commons.csv.CSVRecord
import java.net.URI

case class ParseRowInput(schema: TableGroup,
                         tableUri: URI,
                         dialect: Dialect,
                         table: Table,
                         row: CSVRecord)
