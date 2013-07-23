import com.jme3.math.{Vector2f, Vector3f}
import scala.collection.immutable.TreeMap

//
//        03  07  11
//      02  06  10
//    01  05  09
//  00  04  08
//
val size = 1
val xFactor = 1.0f
val yFactor:Float = xFactor * (math.sin(math.toRadians(60)).toFloat)
val zFactor = 0.1f
def coordinates(size:Int):TreeMap[(Int, Int), Int] = {
  var cnt = -1
  val coords = for (x <- 0 to size; y <- 0 to size)
  yield {
    cnt += 1
    (x,y) -> cnt
  }
  TreeMap(coords:_*)
}


def vertices(size:Int):Array[Vector3f] = {
  coordinates(size).map {
    c =>
      val x = c._1._1
      val y = c._1._2
      new Vector3f(
        (x + 0.5f * y) * xFactor,
        (y) * yFactor,
        (math.random.toFloat - 0.5f) * zFactor
      )
  }.toArray
}


def textureCoordinates(size:Int) = {
  Array[Vector2f](
    new Vector2f(0.0f, 0.0f),
    new Vector2f(xFactor*size, 0.0f),
    new Vector2f((xFactor+0.5f)*size, yFactor*size),
    new Vector2f(0.5f*size, yFactor*size)
  )
}
def triangleIndices(size:Int):Array[Int] = {
  var result = Array[Int]()
  val coords = coordinates(size)
  coordinates(size-1).map {
    c =>
      val x = c._1._1
      val y = c._1._2
      val first = coords.get((x,y))
      val second = coords.get((x, y+1))
      val third = coords.get((x+1, y+1))
      val fourth = coords.get((x+1, y))
      //Triangle top up
      (first, fourth, second) match {
        case (Some(a), Some(b), Some(c)) => result = result ++ Array[Int](a,b,c)
        case _ => {}
      }
      // Triangle top down
      (second, fourth, third) match {
        case (Some(a), Some(b), Some(c)) => result = result ++ Array[Int](a,b,c)
        case _ => {}
      }
  }
  result
}
vertices(size)




textureCoordinates(size)

triangleIndices(size)




