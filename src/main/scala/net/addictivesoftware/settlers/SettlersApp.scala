package net.addictivesoftware.settlers

import com.jme3.app.SimpleApplication
import com.jme3.scene.{Spatial, Node, Geometry}
import com.jme3.material.Material
import com.jme3.math.{Vector3f, FastMath, Quaternion, ColorRGBA}
import scala.collection.immutable.TreeMap
import com.jme3.light.DirectionalLight
import com.jme3.texture.Texture.WrapMode


class SettlersApp() extends SimpleApplication {
  val size = 40
  var world = new HexagonWorld(size)


  override def simpleInitApp() = {

    val gridNode = world.init(assetManager)

    val sun = new DirectionalLight();
    sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
    rootNode.addLight(sun);

    rootNode.attachChild(gridNode)

  }

  override def simpleUpdate(tpf: Float) {
    world.update(tpf)
    super.simpleUpdate(tpf)
  }

}


object SettlersApp {
  def main(args:Array[String]) {
    val app = new SettlersApp
    app.start()
  }
}



