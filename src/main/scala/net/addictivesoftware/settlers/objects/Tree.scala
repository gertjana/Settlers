package net.addictivesoftware.settlers.objects

import net.addictivesoftware.settlers.World
import com.jme3.math.Vector3f
import com.jme3.asset.AssetManager
import com.jme3.scene.Spatial

class Tree(position:Vector3f) extends GridObject {
  def init(assetManager: AssetManager): Spatial = ???

  def update(world: World, geometry:Spatial, tpf: Float) = ???

}
