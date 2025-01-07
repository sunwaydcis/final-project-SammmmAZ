package model

import scalafx.scene.image.Image
import model.City
import model.Hospital.mapLevelToProfit

import scala.collection.immutable.List
import scala.collection.mutable.ListBuffer
import scala.language.postfixOps

abstract class Building(tileData : Image,price : Int, built : Boolean, spawned : Boolean, profitable : Boolean, consumes : Boolean, init_level : Int, levels_available : List[Int], upgrade_prices : List[Double]):
  // define unimplemented methods that will be implemented in Building class
  // building Image data to be implemented in building class
  val buildingTileImage : Image
  // building cost
  var buildingPrice : Int = price
  // some structures spawn naturally in cities
  protected[model] val isNatural : Boolean = spawned
  protected[model] val isBuilt: Boolean = built
  protected[model] val produceMoney: Boolean = profitable // some structures produce money
  protected[model] val isConsumeResource : Boolean = consumes
  // to store data on Building level
  var level: Int = init_level
  // to store information on building can level up or not
  protected[model] val availableLevel: List[Int] = levels_available
  // to store information on Price to Level up the building
  protected[model] val upgradePrice: List[Double] = upgrade_prices

  // check game state & map state, if conditions satisfy, returns buildable
  def IsBuildable(funds : Double, city :City): Boolean
  // method to add Building to the Game
  def Build(loc : Set[(Int,Int)], city: City): Unit
  // method to level up the building
  def Upgrade(): Unit
  // special conditions that affect building money generation rate
  def CalculateMoneyGenerationRate(): Double
end Building



// define hospital class for Buildings
object Hospital:
  val name : String = "Hospital"
  val hospitalTile : Image = new Image(getClass.getResource("/image/tiles/cityTile.png").toExternalForm)
  val startLevel : Int = 1
  // counts all the hospitals in all cities
  var totalHospital : Int = 0

  val mapLevelToProfit : Map[Int,Int] = Map(1 -> 28, 2 -> 53, 3 -> 78, 4 -> 125)
end Hospital

class Hospital(pointX : Int, pointY : Int) extends Building(
  tileData = Hospital.hospitalTile,  // all hospitals share the same Image data
  price = 200,  // this is the initial price to build
  built = true,
  spawned = false,
  profitable = true,
  consumes = true,
  init_level = Hospital.startLevel,
  levels_available = List(1,2,3,4),
  upgrade_prices = List(350,600,950,1500)
):
  // define a point / coordinate data upon constructor called
  val coordinate : (Int,Int) = (pointX,pointY)
  // define money generation rate
  // tied to level property of Hospital
  var moneyProductionRate : Int = mapLevelToProfit(this.level)

  //  Implement IsBuildable Method
  override def IsBuildable( funds : Double, city :City): Boolean =
    // check if funds is sufficient to build a hospital
    // check if city has less than 5 hospitals
    // checks static member val, totalHospital has less than 20 hospital
    return (funds >= this.buildingPrice) && (Hospital.totalHospital < 20) // implement city checker for hospital logic
  end IsBuildable

  override def Build(loc : Set[(Int,Int)], city: City): Unit =
    // check for city's current method
    // logic of adding Hospital should be implemented in City Class
    val current_city : City = city
    val urban_tiles : ListBuffer[(Int,Int)] = current_city.urbanTilePoints
    val suburban_tiles : ListBuffer[(Int,Int)] = current_city.suburbanTilePoints
    val rural_tiles : ListBuffer[(Int,Int)] = current_city.ruralTilePoints
    // compile a logic to prefer to build in urban more than suburban and rural
    for ((x,y) <- loc)
      // iterate through each points
      // if x & y is a good tile
      // and in an urban area
      (x,y) match
        // for the first hospital
        case (x,y) if current_city.hospitalCount == 0 && current_city.hospitalCount < 4 && urban_tiles.length >= 100 && IsBuildable(funds = Population.totalMoney, city = current_city) =>
          Some(Hospital(pointX = x, pointY = y))
          // logic to add
          // 1. remove (x,y) from urban tile/ suburban tile
          // 2. add (x,y) to Building tiles of city object
          // 3. deduct money from Population.total_money -->
          // 4.
          println(f"Hospital built at $x,$y")
        // for the second hospital and third hospital
        case (x,y) if suburban_tiles.contains((x,y)) &&  current_city.hospitalCount < 4 && suburban_tiles.length >= 200 && urban_tiles.length - 100 >= 200 && IsBuildable(funds = Population.totalMoney , city = current_city) =>
          Some(Hospital(pointX = x, pointY = y))
          println(f"Hospital built at $x,$y")
        case (x,y) if rural_tiles.contains((x,y)) && current_city.hospitalCount < 4 && suburban_tiles.length - 200 >= 400 && rural_tiles.length >= 100 &&  IsBuildable(funds = Population.totalMoney , city = current_city) =>
          Some(Hospital(pointX = x, pointY = y))
          println("___")

    end for
  end Build

  // define a val for total money generated
  override def CalculateMoneyGenerationRate(): Double =
    var profit : Double = 200.00
  end CalculateMoneyGenerationRate

  override def Upgrade(): Unit =
    // define


// override def CalculateMoneyGenerationRate(): Double




