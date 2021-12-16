package CSVValidation
case class ErrorMessage(`type`:String, category:String, row:String, column:String, content:String, constraints:String){
  def getType: String = `type`
  def getCategory: String = category
  def getRow:String = row
  def getColumn: String = column
  def getContent: String = content
  def getConstraints: String = constraints
}
