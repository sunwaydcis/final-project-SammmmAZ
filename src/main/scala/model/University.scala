package model

import scalafx.scene.image.Image

// define class for University
object University:
  val university_name : String = f"University_$uniCount"
  val UniversityTile : Image = new Image(getClass.getResource("/image/tiles/cityTile.png").toExternalForm)
  val startLevel : Int = 1
  // implement counter
  var uniCount : Int = 0
end University

class University extends Building(
  tileData = University.UniversityTile,
  price = 1350,
) with Levels:
  // improve the counter by 1
  University.uniCount += 1
  // save the current instance of uni name as id for uni object of class uni
  val id : String = University.university_name

  // define a member for uniqueness effect

  // levels members
  var hasLevels = true
  var upgradePrice = 2000
end University
