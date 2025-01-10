package model

import model.Farm.getClass
import scalafx.scene.image.Image

// define farm class object
object Farm :
  val farmTile : Image = new Image(getClass.getResource("/image/tiles/cityTile.png").toExternalForm)
end Farm

class Farm(pointX : Int, pointY : Int) extends Building(
  tileData = new Image(getClass.getResource("/image/tiles/cityTile.png").toExternalForm),
  price = 450
) with Levels:
  val coords : (Int,Int) = (pointX, pointY)
  hasLevels = true
  upgradePrice = 300
end Farm
