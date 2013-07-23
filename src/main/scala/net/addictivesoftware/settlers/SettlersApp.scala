package net.addictivesoftware.settlers

import com.jme3.app.SimpleApplication
import com.jme3.scene.Geometry
import com.jme3.material.Material
import com.jme3.math.{Vector3f, FastMath, Quaternion, ColorRGBA}


class SettlersApp() extends SimpleApplication {

    override def simpleInitApp() = {
      val size = 25
      val nrOfPeople = 10
      val world = new HexagonWorld(size)
      val people:List[People] = List[People]()

      val pitch60 = new Quaternion()
      pitch60.fromAngleAxis(-FastMath.PI /3, new Vector3f(1,0,0))

      //cam.setFrustumPerspective(45f, cam.getWidth() / cam.getHeight(), 0.01f, 1000f);

      val geom = new Geometry("Box", world.createMesh);
      val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
      mat.setColor("Color", ColorRGBA.White)
      geom.setMaterial(mat)
      geom.getMaterial.getAdditionalRenderState.setWireframe(true)

      geom.setLocalRotation(pitch60)
      geom.setLocalTranslation(new Vector3f(-size/2, 0, -size))
      rootNode.attachChild(geom)
    }
  }


object SettlersApp {
  def main(args:Array[String]) {
    val app = new SettlersApp
    app.start()
  }
}



