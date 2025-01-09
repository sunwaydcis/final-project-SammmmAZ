package model

import scalafx.scene.image.Image
import model.City
import model.CommercialCenter.{cc_counter, name}
import model.Hospital.{hospital_name, mapLevelToProfit}

import scala.collection.immutable.List
import scala.collection.mutable.ListBuffer
import scala.language.postfixOps

abstract class Building(tileData : Image,price : Int, profitable : Boolean, consumes : Boolean, init_level : Int, levels_available : List[Int], upgrade_prices : List[Double]):
  // define unimplemented methods that will be implemented in Building class
  // building Image data to be implemented in building class
  val buildingTileImage : Image 
  // building cost
  var buildingPrice : Int = price
  // some structures spawn naturally in cities
  protected[model] val produceMoney: Boolean = profitable // some structures produce money
  protected[model] val isConsumeResource : Boolean = consumes
  // to store data on Building level
  var level: Int = init_level
  // to store information on building can level up or not
  protected[model] val availableLevel: List[Int] = levels_available
  // to store information on Price to Level up the building
  protected[model] val upgradePrice: List[Double] = upgrade_prices

  def Upgrade(): Unit
end Building



// define hospital class for Buildings

// Hospital companion object for static members of all instances of Class Hospital objects
object Hospital:
  var hospital_name : String = f"Hospital_$totalHospital"
  val hospitalTile : Image = new Image(getClass.getResource("/image/tiles/cityTile.png").toExternalForm)
  val startLevel : Int = 1
  // counts all the hospitals in all cities
  var totalHospital : Int = 0

  val mapLevelToProfit : Map[Int,Int] = Map(1 -> 28, 2 -> 53, 3 -> 78, 4 -> 125)
end Hospital

class Hospital(pointX : Int, pointY : Int) extends Building(
  tileData = Hospital.hospitalTile,  // all hospitals share the same Image data
  price = 200,  // this is the initial price to build
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
  // everytime Hospital constructor is called, add one to the
  Hospital.totalHospital += 1
  val id : String = hospital_name

  override def Upgrade(money : Double): Unit =
    // input takes in total money
    // takes in current level
    val current_level : Int = this.level
    val max_level : Int = this.availableLevel.tail
    // checks if current level is max level, if not then continue
    if !(current_level == max_level) then
      // logic to subtract money from total_money
      money -= this.upgradePrice[current_level -1] // from money required
      this.level += 1 // increase level count
    else
      println("Max level reached, cannot upgrade")
    end if
  end Upgrade
end Hospital

// define class for Commercial Center

// define companion object for commercial center
object CommercialCenter:
  val name : String = f"Commercial_Center_$cc_counter"
  val commercialCenterTile : Image = new Image(getClass.getResource("/image/tiles/cityTile.png").toExternalForm)
  val startLevel : Int = 1
  // counts all commercial center in city
  var cc_counter : Int = 0
end CommercialCenter

// define object member for objects of class Commercial center
class CommercialCenter(pointX : Int, pointY : Int) extends Building(
  tileData = CommercialCenter.commercialCenterTile,
  price = 300,
  profitable = true, 
  consumes = true, 
  init_level = CommercialCenter.startLevel, 
  levels_available = List(1,2,3,4,5), 
  upgrade_prices = List(400,900,1200,1600)
):
  cc_counter += 1
  // method to identify the 
  val id : String = CommercialCenter.name
  // accepts from the constructor
  val coordinate : (Int,Int) = (pointX , pointY)

  override def Upgrade(): Unit =
    var a: Int = 5
    var b : Int = 10
    a+ b
    // place holder function for Upgrade
  end Upgrade
  
end CommercialCenter


  






