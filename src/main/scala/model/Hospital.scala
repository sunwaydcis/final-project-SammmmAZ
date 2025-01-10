package model

import scalafx.scene.image.Image
import model.Building
// define hospital class for Buildings

// Hospital companion object for static members of all instances of Class Hospital objects
object Hospital:
  // to uniquely id a Hospital object upon constructor called
  var hospital_name: String = f"Hospital_$totalHospital"
  val hospitalTile: Image = new Image(getClass.getResource("/image/tiles/cityTile.png").toExternalForm)
  // counts all the hospitals in all cities
  // for updating population growth pattern
  var totalHospital: Int = 0
end Hospital

class Hospital(pointX: Int, pointY: Int) extends Building(
  tileData = Hospital.hospitalTile, // all hospitals share the same Image data
  price = 500): // this is the initial price to build
  // ):
  // define a point / coordinate data upon constructor called
  val coordinate: (Int, Int) = (pointX, pointY)
  // everytime Hospital constructor is called, add one to the
  Hospital.totalHospital += 1
  val id: String = Hospital.hospital_name
end Hospital

