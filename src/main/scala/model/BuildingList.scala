package model

import model.Building
import scala.collection.mutable.ListBuffer

class BuildingList[B <: model.Building]:
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
    // step 1: detect type and see requirement
    // 
    building match
      case _ : Waterstation => counter_list.updated(0, counter_list(0) + 1) ; list_Buildings.prepend(building) // increment counter, then add building to list
      case _ : Electricstation => counter_list.updated(1, counter_list(1) + 1) ; list_Buildings.prepend(building) // increment counter then add building to list
      case _ : Hospital if CheckBuildingSupportedOrNot(building) => counter_list.updated(2, counter_list(2) + 1); list_Buildings.prepend(building)
      case _ : CommercialCenter if CheckBuildingSupportedOrNot(building) => counter_list.updated(3, counter_list(3) +1) ; list_Buildings.prepend(building)
      case _ : University if CheckBuildingSupportedOrNot(building) => counter_list.updated(4, counter_list(4) + 1); list_Buildings.prepend(building)
      case _ : Farm if CheckBuildingSupportedOrNot(building) => counter_list.updated(5, counter_list(5) + 1); list_Buildings.prepend(building)
    end match
  end AddBuildingToCity
  
  def CheckBuildingSupportedOrNot(building: B): Boolean =
    // have a flag that is updated if requirement is satisfied or not
    var supportedFlag : Boolean = false
    
    if building.isInstanceOf[Hospital] || building.isInstanceOf[CommercialCenter] || building.isInstanceOf[Farm] || building.isInstanceOf[University] then
      val bool : Boolean = (counter_list(0) >= 1 && counter_list(1) >= 1)
      supportedFlag = bool
      val typing = building.getClass
      println(f"$typing has been checked")
      // return supported flag
      supportedFlag

      else
        val typing = building.getClass
        println(f"$typing has been checked")
        // return control
        supportedFlag = true
        supportedFlag
    end if
  end CheckBuildingSupportedOrNot
  
end BuildingList
