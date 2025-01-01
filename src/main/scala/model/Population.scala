package model

import model.BiomeMap

object Population:
  // to self update 
  val isDynamic : Boolean = true
  
  // crucial population data
  var population_total : Int = 2500
  // to check for need to expand/ add new tiles to the populations' city
  val population_threshold : Int = 3000
  
  // can migrate:
  var population_min_to_migrate : Int = 5000
  // logic,
  // the city or population must spread, thus a threshold to check if the
  // population can send a new colonizing team to build new establishments
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
    BiomeMap.UpdateMapDataII(mapData = BiomeMap.mapRegion, knownCityTiles = BiomeMap.cityTiles)
  end GrowPopulation
  
  // def a growth function for each city
  // def PopulationGrowthPerCity(): Unit
  