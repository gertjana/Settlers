package net.addictivesoftware.settlers

import com.jme3.math.{Vector3f, FastMath, Quaternion}


object Rotations {
  val pitch90 = new Quaternion().fromAngleAxis(FastMath.PI/2, new Vector3f(1,0,0))
  val pitch60min = new Quaternion().fromAngleAxis(-FastMath.PI /3, new Vector3f(1,0,0))
  val roll180 = new Quaternion().fromAngleAxis(FastMath.PI, new Vector3f(0,0,1))
}
