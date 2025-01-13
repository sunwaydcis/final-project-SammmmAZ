package model

import scalafx.scene.image.Image

object Electricstation:
  val electricStationTile : Image = new Image(getClass.getResource("/image/tiles/city/ElectricStation.png").toExternalForm)
end Electricstation

class Electricstation(pointX : Int, pointY : Int) extends Building(
  tileData = Electricstation.electricStationTile
  , price = 300
):
  val coords : (Int,Int) = (pointX, pointY)

  //var
  override def AddBuildingEffects(list: Array[Int]): Array[Int] =
    list(1) = list(1) + 1
    list
  end AddBuildingEffects
  
end Electricstation
