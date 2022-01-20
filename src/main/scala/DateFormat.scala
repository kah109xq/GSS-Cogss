package CSVValidation

case class DateFormat(pattern: Option[String], dataType: Option[String]) {
  private var `type`: String = _
  private var format: String = _
  private val fields = Array[String](
    "yyyy",
    "M",
    "MM",
    "dd",
    "d",
    "HH",
    "mm",
    "ss",
    "X",
    "XX",
    "XXX",
    "x",
    "xx",
    "xxx"
  )
  private val xmlSchemaBaseUrl = "http://www.w3.org/2001/XMLSchema#"

  def getType() = `type`
  def getFormat() = format

  (pattern, dataType) match {
    case (None, Some(dt)) => {
      `type` = dt
      format = "YYYY-MM-DDThh:mm:ss.sTZD"
    }
    case (Some(p), None) => {
      format = p
      ensureDateTimeFormatContainsRecognizedSymbols(p)
      val hoursRegExp = "HH".r
      val yearsRegExp = "yyyy".r
      val includesHours = hoursRegExp.pattern.matcher(p).find()
      val includesYears = yearsRegExp.pattern.matcher(p).find
      `type` = if (!includesHours && includesYears) {
        s"${xmlSchemaBaseUrl}date"
      } else if (includesHours && !(includesYears)) {
        s"${xmlSchemaBaseUrl}time"
      } else if (includesHours && includesYears) {
        s"${xmlSchemaBaseUrl}dateTime"
      } else
        throw new DateFormatError(
          s"Unexpected datetime format '${p}' does not appear to contain date or time components."
        )
    }
  }

  /**
    * This function ensures that the pattern received does not contain symbols which this class does not
    * know to process. An exception is thrown when it finds a symbol outside the recognised list.
    *
    * @param pattern
    */
  private def ensureDateTimeFormatContainsRecognizedSymbols(pattern: String) = {
    var testPattern = pattern
    // Fractional sections are variable length so are dealt with outside of the `fields` map.
    testPattern = testPattern.replaceAll("/S+/", "")

    val fieldsLongestFirst = fields.sortBy(_.length).reverse
    for (k <- fieldsLongestFirst) testPattern = testPattern.replaceAll(k, "")
    // http://www.unicode.org/reports/tr35/tr35-dates.html#Date_Field_Symbol_Table
    val matcher =
      "[GyYuUrQqMLlwWdDFgEecahHKkjJmsSAzZOvVXx]".r.pattern.matcher(testPattern)
    if (matcher.find()) {
      throw new DateFormatError(
        "Unrecognised date field symbols in date format"
      )
    }
  }
}
