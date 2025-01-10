package model

import scalafx.scene.image.Image

// define WaterStation object singleton
object Waterstation:
  val waterStationTile : Image = new Image(getClass.getResource("/image/tiles/cityTile.png").toExternalForm)
end Waterstation

class Waterstation (pointX : Int, pointY : Int)extends Building(
  tileData = Waterstation.waterStationTile,
  price = 300
):
  val coords : (Int,Int) = (pointX, pointY)
end Waterstation
