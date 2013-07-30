package net.addictivesoftware.settlers.objects

import net.addictivesoftware.settlers.{HexagonWorld, World}
import com.jme3.math.{ColorRGBA, Vector3f}
import com.jme3.asset.AssetManager
import com.jme3.scene.{Geometry, Node, Spatial}
import com.jme3.scene.shape.{Sphere, Cylinder}
import com.jme3.material.Material
import scala.collection.immutable.TreeMap
import scala.util.Random

class Tree(position:Vector3f) extends GridObject {

  def currentPosition() = {
    position
  }

  def init(assetManager: AssetManager): Spatial = {
    Random.nextBoolean match {
      case true => treeOne(assetManager)
      case false => treeTwo(assetManager)
    }
  }

  def update(world: World, geometry:Spatial, tpf: Float) = {
    // nothing to do for now
  }

  def treeOne(assetManager:AssetManager) = {
    val treeNode = new Node()

    val stem = new Cylinder(6, 6, 0.05f, 0.2f, true)
    val stemGeom = new Geometry("stem", stem)
    val stemMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    stemMat.setColor("Color", new ColorRGBA(0,0,0.4f,1))
    stemGeom.setMaterial(stemMat)
    stemGeom.setLocalTranslation(new Vector3f(0,0, 0.1f))
    //stemGeom.getMaterial.getAdditionalRenderState.setWireframe(true)

    val leaves = new Cylinder(4, 4, 0.02f, 0.2f, 0.4f, true, false)
    val leavesGeom = new Geometry("leaves", leaves)
    val leavesMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    //val grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg")
    //leavesMat.setTexture("ColorMap", grass)
    leavesMat.setColor("Color", ColorRGBA.Blue)

    leavesGeom.setMaterial(leavesMat)
    leavesGeom.getMaterial.getAdditionalRenderState.setWireframe(true)
    leavesGeom.setLocalTranslation(new Vector3f(0, 0, 0.4f))

    treeNode.attachChild(stemGeom)
    treeNode.attachChild(leavesGeom)
    treeNode
  }
  def treeTwo(assetManager:AssetManager) = {
    val treeNode = new Node()

    val stem = new Cylinder(6, 6, 0.05f, 0.4f, true)
    val stemGeom = new Geometry("stem", stem)
    val stemMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    stemMat.setColor("Color", new ColorRGBA(0,0,0.4f,1))

    stemGeom.setMaterial(stemMat)
    stemGeom.setLocalTranslation(new Vector3f(0,0, 0.2f))
    //stemGeom.getMaterial.getAdditionalRenderState.setWireframe(true)

    val leaves = new Sphere(6, 6, 0.2f)
    val leavesGeom = new Geometry("leaves", leaves)
    val leavesMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    //val grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg")
    //leavesMat.setTexture("ColorMap", grass)
    leavesMat.setColor("Color", ColorRGBA.Blue)
    leavesGeom.setMaterial(leavesMat)
    leavesGeom.getMaterial.getAdditionalRenderState.setWireframe(true)

    leavesGeom.setLocalTranslation(new Vector3f(0, 0, 0.6f))

    treeNode.attachChild(stemGeom)
    treeNode.attachChild(leavesGeom)
    treeNode
  }

}

object Tree {
  def createABunchOfTrees(size:Int, nrOfPeople:Int, world:HexagonWorld):TreeMap[Int, Tree] = {

    var occupanceMap = List[(Int, Int)]()

    val trees = for (i <- 0 to nrOfPeople-1)
    yield {
      var x = math.random*size toInt;
      var y = math.random*size toInt;
      while (occupanceMap.contains((x,y))) {
        x = math.random*size toInt;
        y = math.random*size toInt;
      }
      occupanceMap = (x,y) :: occupanceMap

      val p = new Tree(world.getPosition(x,y))
      (i -> p)
    }
    TreeMap(trees:_*)
  }
}
