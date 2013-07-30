package net.addictivesoftware.settlers

import com.jme3.scene.{Node, Geometry, Spatial, Mesh}
import com.jme3.scene.VertexBuffer.Type
import com.jme3.util.BufferUtils
import scala.util.Random
import com.jme3.asset.AssetManager
import com.jme3.math.{ColorRGBA, Vector2f, Vector3f}
import scala.collection.immutable.TreeMap
import com.jme3.texture.Texture.WrapMode
import com.jme3.material.Material
import net.addictivesoftware.settlers.objects.{People, Tree, GridObject}

class HexagonWorld(size:Int) extends World {

  val coordinatesMap = coordinates(size)
  var verts = TreeMap[Int, Vector3f]()

  var gridObjectGeometries:List[(GridObject, Spatial)] = List[(GridObject, Spatial)]()
  val nrOfPeople = 200
  var peoples:TreeMap[Int, People] = TreeMap[Int, People]()

  val nrOfTrees = 200
  var trees:TreeMap[Int, Tree] = TreeMap[Int, Tree]()

  def init(assetManager:AssetManager) = {
    val geom = new Geometry("Box", createMesh(assetManager))

    peoples = People.createABunchOfPeople(size, nrOfPeople, this)
    trees = Tree.createABunchOfTrees(size, nrOfTrees, this)

    //cam.setFrustumPerspective(45f, cam.getWidth() / cam.getHeight(), 0.01f, 1000f);

    val grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg")
    //grass.setWrap(WrapMode.Repeat)

    val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat.setColor("Color", ColorRGBA.LightGray)
    //mat.setTexture("ColorMap", grass)
    geom.setMaterial(mat)
    geom.getMaterial.getAdditionalRenderState.setWireframe(true)

    val gridNode = new Node()
    gridNode.setLocalRotation(Rotations.pitch60min)
    gridNode.setLocalTranslation(new Vector3f(-size/2, 0, 0))

    for ((i, p) <- peoples) {
      val g = p.init(assetManager)
      g.setLocalTranslation(p.currentPosition())
      gridObjectGeometries = (p,g) :: gridObjectGeometries
      gridNode.attachChild(g)
    }

    for ((i, p) <- trees) {
      val g = p.init(assetManager)
      g.setLocalTranslation(p.currentPosition())
      gridObjectGeometries = (p,g) :: gridObjectGeometries
      gridNode.attachChild(g)
    }



    gridNode.attachChild(geom)
    gridNode
  }

  def createMesh(assetManager:AssetManager):Mesh = {
    val start = System.currentTimeMillis()
    val mesh = new Mesh()

    verts = vertices(size)

    mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(verts.values.toArray:_*))
    mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(textureCoordinates(size):_*))
    mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(triangleIndices(size):_*))
    mesh.updateBound()

    println("Creating world of size " + size + " took: " + (System.currentTimeMillis()-start) + " ms")

    mesh
  }

  def update(tpf:Float) = {
    for ((p,g) <- gridObjectGeometries) {
      p.update(this, g, tpf)
    }
  }

  def getPosition(x:Int, y:Int):Vector3f = {
    coordinatesMap.get((x,y)) match {
      case Some(i:Int) => {
        verts(i)
      }
      case _ =>
        println("vert not found for x: " + x + " and y: " + y)
        Vector3f.NAN
    }
  }

  val xFactor = 1.0f
  val yFactor:Float = xFactor * (math.sin(math.toRadians(60)).toFloat)
  val zFactor = 0.3f


  def getRandomDirection(position:Vector3f):Vector3f = {
    val vertice = verts.find((v:(Int, Vector3f)) => v._2.x == position.x && v._2.y == position.y)
    val index = vertice.get._1
    val coordinate = coordinatesMap.find((c:((Int,Int), Int)) => c._2 == index).get._1
    val x = coordinate._1
    val y = coordinate._2

    var result = List[Vector3f]()
    coordinatesMap.get(x,y-1) match {case Some(i) => result = verts(i) :: result case _ => {}}
    coordinatesMap.get(x+1,y-1) match {case Some(i) => result = verts(i) :: result case _ => {}}
    coordinatesMap.get(x+1,y) match {case Some(i) => result = verts(i) :: result case _ => {}}
    coordinatesMap.get(x,y+1) match {case Some(i) => result = verts(i) :: result case _ => {}}
    coordinatesMap.get(x-1,y+1) match {case Some(i) => result = verts(i) :: result case _ => {}}
    coordinatesMap.get(x-1,y) match {case Some(i) => result = verts(i) :: result case _ => {}}

    //for now pick any of the six hexagonal directions
    //later check if someone is not already moving to that point
    Random.shuffle(result).head

  }


  private def coordinates(size:Int):TreeMap[(Int, Int), Int] = {
    var cnt = -1
    val coords = for (x <- 0 to size; y <- 0 to size)
    yield {
      cnt += 1
      (x,y) -> cnt
    }
    TreeMap(coords:_*)
  }

  private def vertices(size:Int):TreeMap[Int, Vector3f] = {
    coordinatesMap.map {
      c =>
        val x = c._1._1
        val y = c._1._2

        c._2 -> new Vector3f(
          (x + 0.5f * y) * xFactor,
          (y) * yFactor,
          (math.random.toFloat - 0.5f) * zFactor
          //heightMap.getInterpolatedHeight(x,y)
        )
    }
  }

  private def textureCoordinates(size:Int) = {
    Array[Vector2f](
      new Vector2f(0.0f, 0.0f),
      new Vector2f(1.0f/size, 0.0f),
      new Vector2f(0.0f, 1.0f/size),
      new Vector2f(1.0f/size, 1.0f/size)
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
        //Triangle top up counterclockwise
        (first, fourth, second) match {
          case (Some(a), Some(b), Some(c)) => result = result ++ Array[Int](a,b,c)
          case _ => {}
        }
        // Triangle top down counterclockwise
        (second, fourth, third) match {
          case (Some(a), Some(b), Some(c)) => result = result ++ Array[Int](a,b,c)
          case _ => {}
        }
    }
    result
  }
}
