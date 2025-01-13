package model

import scalafx.scene.image.Image

// define class for Commercial Center
// define companion object for commercial center
object CommercialCenter:
  val commercialCenterTile: Image = new Image(getClass.getResource("/image/tiles/city/CommercialCenter.png").toExternalForm)
  val startLevel: Int = 1
  // counts all commercial center in city
  var cc_counter: Int = 0
end CommercialCenter

// define object member for objects of class Commercial center
class CommercialCenter(pointX: Int, pointY: Int) extends Building(
  tileData = CommercialCenter.commercialCenterTile,
  price = 300) with Levels:
  // code to execute when constructor is called
  CommercialCenter.cc_counter += 1
  // method to identify the 
  // accepts from the constructor
  val coordinate: (Int, Int) = (pointX, pointY)
  
  // members of levels 
  var hasLevels = true
  var upgradePrice = 500

  override def AddBuildingEffects(list: Array[Int]): Array[Int] =
    list(0) = list(0) + 1
    list
  end AddBuildingEffects
  
end CommercialCenter

