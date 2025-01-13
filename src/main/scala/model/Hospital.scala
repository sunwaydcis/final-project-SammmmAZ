package model

import scalafx.scene.image.Image
// define hospital class for Buildings

// Hospital companion object for static members of all instances of Class Hospital objects
object Hospital:
  // to uniquely id a Hospital object upon constructor called
  val hospitalTile: Image = new Image(getClass.getResource("/image/tiles/city/hospital.png").toExternalForm)
  // counts all the hospitals in all cities
  // for updating population growth pattern
  var totalHospital: Int = 0
end Hospital

class Hospital(pointX: Int, pointY: Int) extends Building(
  tileData = Hospital.hospitalTile, // all hospitals share the same Image data
  price = 500) with Levels: // this is the initial price to build
  // ):
  // define a point / coordinate data upon constructor called
  val coordinate: (Int, Int) = (pointX, pointY)
  // everytime Hospital constructor is called, add one to the
  
  val hasLevels = true
  val upgradePrice = 1500

  override def AddBuildingEffects(list: Array[Int]): Array[Int] =
    list(3) = list(3) + 1
    list
  end AddBuildingEffects
  
end Hospital

