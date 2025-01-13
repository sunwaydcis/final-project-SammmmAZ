package model

import scalafx.scene.image.Image

// define farm class object
object Farm :
  val farmTile : Image = new Image(getClass.getResource("/image/tiles/city/farm.png").toExternalForm)
end Farm

class Farm(pointX : Int, pointY : Int) extends Building(
  tileData =  Farm.farmTile,
  price = 450
) with Levels:
  val coords : (Int,Int) = (pointX, pointY)
  val hasLevels = true
  val upgradePrice = 300

  override def AddBuildingEffects(list: Array[Int]): Array[Int] =
    list(2) = list(2) + 1
    list
  end AddBuildingEffects
  
end Farm
