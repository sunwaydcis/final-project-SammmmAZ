package model

import BiomeMapApp.MainApp

import scala.collection.mutable.ListBuffer

object Population:
  // to self update 
  // total funds by all Population
  var totalMoney : Double = 20000.00
  // crucial population data
  var population_total : Int = 0
  var totalFood : Int = 50000 // start with 50000 food
  // to check for need to expand/ add new tiles to the populations' city
  //var population_foodSupply : Int = 50000
  // buy 20000 food for 4000$ money 
  
  // and start a new city else where
  var growthCounter : Int = 0

  // for statistics page
  val timeData: ListBuffer[String] = ListBuffer()
  val populationData: ListBuffer[Int] = ListBuffer()
  
  // def a growth function for each city
  def GrowPopulationByCity(counter : Int): Unit =
    // obtain list of city in Map
    //timeData.prepend(f"Day $growthCounter")
    //populationData.prepend(this.population_total)
    val cities : ListBuffer[City] = BiomeMap.ListOfCity
    Population.population_total = 0
    this.growthCounter += 1
    for city <- cities do
      val startTime = System.nanoTime()
      city.CityPopulationGrowth()
      //println(city.local_population)
      Population.population_total += city.local_population
      city.ExpandCity()
      city.Revenue()
//      val endTime = System.nanoTime()
//      println(s"Total Duration at ${city.cityTiles.size} = ${(endTime - startTime)/1e9d}")
      //println(timeData.toString() + populationData.toString())
    end for
    if this.totalFood <= 0 then
      MainApp.ShowGameOverDialogue()

  //    println(Population.totalMoney)
//    println(Population.population_total)
    //this.population_foodSupply -= population_total
    //println(Population.population_foodSupply)
  end GrowPopulationByCity
end Population

  // def PopulationGrowthPerCity(): Unit
  