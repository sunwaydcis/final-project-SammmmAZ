package model

import scala.collection.mutable.ListBuffer

object Population:
  // to self update 
  val isDynamic : Boolean = true
  
  // total funds by Population
  var totalMoney : Double = 20000.00
  // crucial population data
  var population_total : Int = 2500
  // to check for need to expand/ add new tiles to the populations' city
  val population_threshold : Int = 3000
  
  // and start a new city else where
  var growthCounter : Int = 0
  
  // define a growth function
  def GrowPopulation(counter : Int): Unit =
    this.population_total += 5000
    this.growthCounter += 1
    val pop_now = this.population_total
    val cityTiles = BiomeMap.cityTiles
    //println(f"Population is now at $pop_now")
    //println(f"City tiles now at $cityTiles")
    BiomeMap.UpdateMapDataIII()
  end GrowPopulation
  
  
  
  // def a growth function for each city
  def GrowPopulationByCity(counter : Int): Unit =
    // obtain list of city in Map
    val cities : ListBuffer[City] = BiomeMap.ListOfCity
    
//    for city <- cities do 
//      city.local_population =  
  
  // def PopulationGrowthPerCity(): Unit
  