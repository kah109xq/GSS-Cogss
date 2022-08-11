package CSVValidation
import org.apache.commons.csv.CSVRecord

case class ValidateRowOutput(
    warningsAndErrors: WarningsAndErrors = WarningsAndErrors(),
    primaryKeyValues: List[Any] = List(),
    parentTableForeignKeyReferences: Map[
      ParentTableForeignKeyReference,
      KeyWithContext
    ] = Map(),
    childTableForeignKeys: Map[ChildTableForeignKey, KeyWithContext] = Map(),
    row:CSVRecord
)
