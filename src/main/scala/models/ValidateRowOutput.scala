package CSVValidation

case class ValidateRowOutput(
    recordNumber: Long,
    warningsAndErrors: WarningsAndErrors = WarningsAndErrors(),
    primaryKeyValues: List[Any] = List(),
    parentTableForeignKeyReferences: Map[
      ParentTableForeignKeyReference,
      KeyWithContext
    ] = Map(),
    childTableForeignKeys: Map[ChildTableForeignKey, KeyWithContext] = Map()
)
