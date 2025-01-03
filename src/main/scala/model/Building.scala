package model

import scalafx.scene.image.Image

import scala.collection.immutable.List
import scala.language.postfixOps

abstract class Building(tileData : Image,price : Int, built : Boolean, spawned : Boolean, profitable : Boolean, consumes : Boolean, init_level : Int, levels_available : List[Int], upgrade_prices : List[Double]):
  // define unimplemented methods that will be implemented in Building class
  // building Image data to be implemented in building class
  val buildingTileImage : Image
  // building cost
  var buildingPrice : Int = price
  // some structures spawn naturally in cities
  val isNatural : Boolean = spawned
  val isBuilt: Boolean = built
  val produceMoney: Boolean = profitable // some structures produce money
  val isConsumeResource : Boolean = consumes
  // to store data on Building level
  var level: Int = init_level
  // to store information on building can level up or not
  val availableLevel: List[Int] = levels_available
  // to store information on Price to Level up the building
  val upgradePrice: List[Double] = upgrade_prices

  // check game state & map state, if conditions satisfy, returns buildable
  def IsBuildable(): Boolean
  // method to add Building to the Game
  def Build(): Unit
  // method to level up the building
  def Upgrade(): Unit
  // special conditions that affect building money generation rate
  def CalculateMoneyGenerationRate(): Double
end Building


