package model

import scalafx.scene.image.Image

// define class for University
object University:
  val UniversityTile : Image = new Image(getClass.getResource("/image/tiles/city/University.png").toExternalForm)
  val startLevel : Int = 1
  // implement counter
  var uniCount : Int = 0
end University

class University(pointX : Int , pointY : Int) extends Building(
  tileData = University.UniversityTile,
  price = 1350,
) with Levels:
  // improve the counter by 1
  val coords : (Int,Int) = (pointX, pointY)
  University.uniCount += 1
  // save the current instance of uni name as id for uni object of class uni

  // define a member for uniqueness effect

  // levels members
  var hasLevels = true
  var upgradePrice = 2000

  override def AddBuildingEffects(list: Array[Int]): Array[Int] =
    list(4) = list(4) + 1
    list
  end AddBuildingEffects
  
end University
