package model

import scala.language.postfixOps
import model.Building
import scalafx.scene.image.Image
import scala.util.Random

object Hospital:
  val name : String = "Hospital"
  // all Hospitals share same Image
  def GenerateSpawnPoints(listOfPoints : Set[(Int,Int)], uTiles : Set[(Int,Int)], sTiles: Set[(Int,Int)], rTiles : Set[(Int,Int)] ): (Int,Int)=
    // Hospitals have a higher tendency to generate in urban areas
    // ensure Hospital generation prioritizes urban tile swapping

    // define flag to state point for hospital generation is not found
    var isSpawnSet : Boolean = false

    while !isSpawnSet do
      // acquire random point
      val randomPoints : (Int,Int) = listOfPoints.toSeq(Random.nextInt(listOfPoints.size))
      // checks if point is in urban tiles
      if randomPoints.subsetOf(uTiles) then // add more logic here



end Hospital


class Hospital extends Building(
  tileData = new Image(getClass.getResource("/image/tiles/cityTile.png").toExternalForm), // sets the hospital tile display data
  built = true, // when a hospital is added to the map, it is likely true
  spawned = false, // hospital can spawn naturally too, this is to check which hospitals spawn with city expansion, and which is built for score points
  profitable = true, // some buildings dont produce money, hospital does
  consumes = true, // hospital consumes some resources too
  init_level = 1, // when Hospital is added, it is at level one
  levels_available = List(1,2,3,4,5), // shows available hospital uprade levels
  upgrade_prices = List(200.00, 450.99, 555.55, 750.00), // money needed to upgrade hospital
  price = 200 // price to build a hospital
):
  // add other Building Requirement:
  val requires : List[B <: Building] = List()
  val coordinate : Set[(Int,Int)]


  def this(spawn_coords : Set[(Int,Int)])=

end Hospital


