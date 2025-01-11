package model

import scalafx.scene.image.Image
import model.City
import model.Hospital.hospital_name
import model.University.{uniCount, university_name}

import scala.collection.immutable.List
import scala.collection.mutable.ListBuffer
import scala.language.postfixOps

abstract class Building(tileData : Image,price : Int):
  // define unimplemented methods that will be implemented in Building class
  // building Image data to be implemented in building class
  
  // at  a high level view, building has data for map,
  // and money to add building to city
  val buildingTileImage : Image = tileData
  // building cost
  var buildingPrice : Int = price
end Building