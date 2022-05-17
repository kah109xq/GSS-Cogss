package CSVValidation

case class ValidateRowOutput(
    warningsAndErrors: WarningsAndErrors,
    primaryKeyValues: List[Any],
    foreignKeyWithTable: List[(ForeignKeyWithTable, List[Any])],
    foreignKeyWrapper: List[(ForeignKeyWrapper, List[Any])]
)
