package model

import scalafx.scene.image.Image

// define farm class object
object Farm :
  val farmTile : Image = new Image(getClass.getResource("/image/tiles/cityTile.png").toExternalForm)
end Farm

class Farm(pointX : Int, pointY : Int) extends Building(
  tileData =  Farm.farmTile,
  price = 450
) with Levels:
  val coords : (Int,Int) = (pointX, pointY)
  val hasLevels = true
  val upgradePrice = 300
end Farm
