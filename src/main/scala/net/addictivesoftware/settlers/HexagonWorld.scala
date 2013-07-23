package net.addictivesoftware.settlers

import com.jme3.scene.Mesh
import com.jme3.scene.VertexBuffer.Type
import com.jme3.util.BufferUtils

class HexagonWorld(size:Int) {

  import com.jme3.math.{Vector2f, Vector3f}
  import scala.collection.immutable.TreeMap

  val coordinatesMap = coordinates(size)
  val verts = vertices(size)

  def createMesh:Mesh = {
    val start = System.currentTimeMillis()
    val mesh = new Mesh()

    mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(verts:_*))
    mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(textureCoordinates(size):_*))
    mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(triangleIndices(size):_*))
    mesh.updateBound()

    println("Creating world of size " + size + " took: " + (System.currentTimeMillis()-start) + " ms")

    mesh
  }

  def getPosition(x:Int, y:Int):Vector3f = {
    coordinatesMap.get((x,y)) match {
      case Some(i:Int) => {
        verts(i)
      }
      case _ => Vector3f.NAN
    }
  }

  //
  //        03  07  11
  //      02  06  10
  //    01  05  09
  //  00  04  08
  //
  val xFactor = 1.0f
  val yFactor:Float = xFactor * (math.sin(math.toRadians(60)).toFloat)
  val zFactor = 0.3f
  private def coordinates(size:Int):TreeMap[(Int, Int), Int] = {
    var cnt = -1
    val coords = for (x <- 0 to size; y <- 0 to size)
    yield {
      cnt += 1
      (x,y) -> cnt
    }
    TreeMap(coords:_*)
  }

  private def vertices(size:Int):IndexedSeq[Vector3f] = {
    coordinatesMap.map {
      c =>
        val x = c._1._1
        val y = c._1._2
        new Vector3f(
          (x + 0.5f * y) * xFactor,
          (y) * yFactor,
          (math.random.toFloat - 0.5f) * zFactor
        )
    }.toIndexedSeq
  }

  private def textureCoordinates(size:Int) = {
    Array[Vector2f](
      new Vector2f(0.0f, 0.0f),
      new Vector2f(xFactor*size, 0.0f),
      new Vector2f((xFactor+0.5f)*size, yFactor*size),
      new Vector2f(0.5f*size, yFactor*size)
    )
  }

  private def triangleIndices(size:Int):Array[Int] = {
    var result = Array[Int]()
    coordinates(size-1).map {
      c =>
        val x = c._1._1
        val y = c._1._2
        val first = coordinatesMap.get((x,y))
        val second = coordinatesMap.get((x, y+1))
        val third = coordinatesMap.get((x+1, y+1))
        val fourth = coordinatesMap.get((x+1, y))
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
}
