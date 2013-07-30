package net.addictivesoftware.settlers.objects

import scala.collection.immutable.TreeMap
import com.jme3.scene.{Spatial, Geometry, Node}
import com.jme3.scene.shape.{Box, Quad}
import com.jme3.material.Material
import com.jme3.math.{Quaternion, FastMath, Vector3f, ColorRGBA}
import com.jme3.asset.AssetManager
import com.jme3.texture.Texture
import net.addictivesoftware.settlers.{HexagonWorld, World, Rotations, objects}

class People(position:Vector3f) extends objects.GridObject {
  val speed = 0.01f
 // val translation = new Vector3f(0,1,1)

  var current = position
  var origin = current
  var destination = current
  var count = math.random * 10f
  var isMoving = false

  def init(assetManager:AssetManager):Spatial = {
    val ninja = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml")
    ninja.scale(0.001f, 0.001f, 0.001f)
    ninja.setLocalRotation(Rotations.pitch90)
    ninja
  }

  def currentPosition() = {
    current
  }

  def update(world:World, geometry: Spatial, tpf:Float) = {
    count = count - tpf

    if (count < 0) {
      count = 0.02f

      if (isMoving) {
        val length = destination.subtract(current).length
        current = current.add(destination.subtract(current).mult(speed /length))
        if (current.subtract(destination).length() < 0.05f) {
          current = destination;
          isMoving = false
        }
      } else {
        destination = world.getRandomDirection(current)
        origin = current
        isMoving = true
      }
    }
    geometry.setLocalTranslation(current)
    val q = new Quaternion()
    q.lookAt(origin.subtract(destination), new Vector3f(0,0,1))
    geometry.setLocalRotation(q)
  }

}

object People {
  def createABunchOfPeople(size:Int, nrOfPeople:Int, world:HexagonWorld):TreeMap[Int, People] = {

    val peeps = for (i <- 0 to nrOfPeople-1)
    yield {
      val x = math.random*size toInt
      val y = math.random*size toInt

      val p = new People(world.getPosition(x,y))
      (i -> p)
    }
    TreeMap(peeps:_*)
  }
}
