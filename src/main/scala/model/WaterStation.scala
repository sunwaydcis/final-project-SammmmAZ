package model

import scalafx.scene.image.Image

// define WaterStation object singleton
object WaterStation:
  val waterStationTile : Image = new Image(getClass.getResource("/image/tiles/city/waterStation.png").toExternalForm)
end WaterStation

class WaterStation (pointX : Int, pointY : Int)extends Building(
  tileData = WaterStation.waterStationTile,
  price = 300
):
  val coords : (Int,Int) = (pointX, pointY)

  override def AddBuildingEffects(list: Array[Int]): Array[Int] =
    list(5) = list(5) + 1
    list
  end AddBuildingEffects
  
end WaterStation
