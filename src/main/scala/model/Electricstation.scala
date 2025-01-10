package model

import scalafx.scene.image.Image

object Electricstation:
  val electricStationTile : Image = new Image(getClass.getResource("/image/tiles/cityTile.png").toExternalForm)
end Electricstation

class Electricstation(pointX : Int, pointY : Int) extends Building(
  tileData = Electricstation.electricStationTile
  , price = 300
):
  val coords : (Int,Int) = (pointX, pointY)
end Electricstation
