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
  
  // for statistics page
  val timeData: ListBuffer[Int] = ListBuffer()
  val populationData: ListBuffer[Int] = ListBuffer()
  
  // def a growth function for each city
  def GrowPopulationByCity(counter : Int): Unit =
    // obtain list of city in Map
    timeData.append(this.growthCounter)
    populationData.append(this.population_total)
    val cities : ListBuffer[City] = BiomeMap.ListOfCity
    Population.population_total = 0
    this.growthCounter += 1
    for city <- cities do
      city.CityPopulationGrowth()
      Population.population_total += city.local_population
      city.ExpandCity()
      totalMoney += city.Revenue()
    end for
    
//    println(Population.totalMoney)
//    println(Population.population_total)
    //this.population_foodSupply -= population_total
    //println(Population.population_foodSupply)
  end GrowPopulationByCity
end Population

  // def PopulationGrowthPerCity(): Unit
  