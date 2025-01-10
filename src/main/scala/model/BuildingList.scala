package model

import model.Building
import scala.collection.mutable.ListBuffer

class BuildingList[B <: Building]:
  // define a method to contain Buildings
  val list_Buildings : ListBuffer[B] = ListBuffer()
  // List index 0 - 6
  // 0 - Water Station
  // 1 - Electric station
  // 2 - Hospital
  // 3 - commercial center
  // 4 - university
  // 5 - Farm
  var counter_list : List[Int] = List(0,0,0,0,0,0)


  // for the purpose of testing
  def Add(building : B): Unit=
    this.list_Buildings += building
    // println statement for debug
    println(f"$building has been added to Building list")
  end Add

  def AddBuildingToCity(building: B): Unit =
    // step 1
end BuildingList
