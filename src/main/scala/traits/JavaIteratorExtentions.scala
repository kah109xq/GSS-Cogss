package CSVValidation
package traits
import scala.jdk.javaapi.{CollectionConverters => conv}
import java.{lang => jl, util => ju}

object JavaIteratorExtensions {
  implicit class IteratorHasAsScalaArray[A](i: ju.Iterator[A]) {
    /** Converts a Java `Iterator` to a Scala `Array` **/

    def asScalaArray: Array[A] = Array.from(conv.asScala(i))
  }
}
