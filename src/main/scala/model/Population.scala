package model

import scala.collection.mutable.ListBuffer

object Population:
  // to self update 
  // total funds by all Population
  var totalMoney : Double = 20000.00
  // crucial population data
  var population_total : Int = 0
  // to check for need to expand/ add new tiles to the populations' city
  //var population_foodSupply : Int = 50000
  // buy 20000 food for 4000$ money 
  
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
    println(Population.population_total)
    val cities : ListBuffer[City] = BiomeMap.ListOfCity
    Population.population_total = 0
    for city <- cities do
      city.CityPopulationGrowth()
      Population.population_total += city.local_population
      city.ExpandCity()
      totalMoney += city.Revenue()
    end for
    println(Population.totalMoney)
    println(Population.population_total)
    //this.population_foodSupply -= population_total
    //println(Population.population_foodSupply)
  end GrowPopulationByCity
  
  // def PopulationGrowthPerCity(): Unit
  