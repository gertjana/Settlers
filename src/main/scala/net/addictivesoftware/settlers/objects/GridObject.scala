package net.addictivesoftware.settlers.objects

import com.jme3.math.Vector3f
import com.jme3.asset.AssetManager
import com.jme3.scene.Spatial
import net.addictivesoftware.settlers.World

abstract class GridObject {

  def init(assetManager:AssetManager):Spatial

  def update(world:World, tpf:Float):Vector3f
}
