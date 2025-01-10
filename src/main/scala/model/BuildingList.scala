package model

import model.Building
import scala.collection.mutable.ListBuffer

class BuildingList[B <: model.Building]:
  // define a method to contain Buildings
  val list_Buildings : ListBuffer[B] = ListBuffer()

  // for the purpose of testing
  def Add(building : B): Unit=
    this.list_Buildings += building
    // println statement for debug
    println(f"$building has been added to Building list")
  end Add
  
end BuildingList
