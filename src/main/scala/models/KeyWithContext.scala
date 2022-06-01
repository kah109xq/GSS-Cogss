package CSVValidation
case class KeyWithContext(
    rowNumber: Long,
    keyValues: List[Any],
    var isDuplicate: Boolean = false
) {

  override def equals(obj: Any): Boolean =
    obj != null &&
      obj.isInstanceOf[KeyWithContext] &&
      this.keyValues.equals(obj.asInstanceOf[KeyWithContext].keyValues)

  override def hashCode(): Int = this.keyValues.hashCode()

}
