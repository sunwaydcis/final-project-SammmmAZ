package model

import scalafx.scene.image.Image

// define class for Commercial Center
// define companion object for commercial center
object CommercialCenter:
  val name: String = f"Commercial_Center_$cc_counter"
  val commercialCenterTile: Image = new Image(getClass.getResource("/image/tiles/cityTile.png").toExternalForm)
  val startLevel: Int = 1
  // counts all commercial center in city
  var cc_counter: Int = 0
end CommercialCenter

// define object member for objects of class Commercial center
class CommercialCenter(pointX: Int, pointY: Int) extends Building(
  tileData = CommercialCenter.commercialCenterTile,
  price = 300):
  // code to execute when constructor is called
  CommercialCenter.cc_counter += 1
  // method to identify the 
  val id: String = CommercialCenter.name
  // accepts from the constructor
  val coordinate: (Int, Int) = (pointX, pointY)


end CommercialCenter

