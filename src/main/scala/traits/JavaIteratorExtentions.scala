package CSVValidation
package traits

import scala.jdk.javaapi.{CollectionConverters => conv}
import java.{util => ju}
import scala.reflect.{ClassTag}

object JavaIteratorExtensions {
  implicit class IteratorHasAsScalaArray[A: ClassTag](i: ju.Iterator[A]) {
    /** Converts a Java `Iterator` to a Scala `Array` **/

    def asScalaArray: Array[A] = Array.from(conv.asScala(i))
  }
}
