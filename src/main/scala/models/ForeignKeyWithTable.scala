package CSVValidation

case class ForeignKeyWithTable(
    foreignKey: ForeignKeyWrapper,
    referencedTable: Table
) {}
