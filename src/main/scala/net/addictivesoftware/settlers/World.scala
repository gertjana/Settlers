package net.addictivesoftware.settlers

import com.jme3.math.Vector3f


trait World {

  def getRandomDirection(position:Vector3f):Vector3f

}
