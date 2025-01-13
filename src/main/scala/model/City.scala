package model

import City.{GetGenerationPoint, GrowthFunctionReg, RandomTextureAssigning, max_rural_tiles, max_suburban_tiles, max_urban_tiles}
import scalafx.scene.image.Image

import scala.collection.mutable.Queue
import scala.language.dynamics
// import texture packs for map processing key
import java.lang.Runnable
import scala.collection.mutable.ListBuffer
import scala.language.postfixOps
import scala.util.Random

// import building class data

// companion object city
object City:
  // to declare static variables for all class

  // tile related data
  private val max_tiles: Int = max_urban_tiles + max_suburban_tiles + max_rural_tiles
  private val max_urban_tiles : Int= 200 // should be random between 30 - 60
  private val max_suburban_tiles : Int= 250 // should be capped at val where suburban tile and urban tile sum up to 100
  private val max_rural_tiles : Int= 200

  // population related data
  private val max_population : Int = 300000 // max per city should be between 300,000 and 400,000
  private val start_population : Int = 5000 // minimum that is given when a city is initiated
  // start population is subtracted from an existing city's population

  // for access of dynamic element function execution
  // private var ListOfCity : scala.collection.mutable.ListBuffer[City] = ListBuffer[City]()

  // define scheme for assigning tile identity for the cityTexture variable

  // predefined in BiomeMap object
  // Plains -> 1
  // Mountains -> 2
  // Water -> 0


  // CT1_urban -> 3
  // CT1_suburban -> 4
  // CT1_rural  -> 5

  // CT2_urban -> 6
  // CT2_suburban -> 7
  // CT2_rural -> 8

  // CT3_urban -> 9
  // CT3_suburban -> 10
  // CT3_rural -> 11

  // static set of tile graphics
  private var cityTexture :ListBuffer[List[Int]] =
    ListBuffer(
      // for the first texture pack of the BiomeMap
      List(
      3 ,
      4 ,
        3
    ),
      List(
        6 ,
        7 ,
        6
      ),
      List(
        9 ,
        10,
        9
      ))

  // helper data for ExpandCity Function
  // direction data:
  private var directions: scala.collection.mutable.Queue[(Int, Int)] = Queue(
    (-2,0),
    (-1,-2),
    (0,-2),
    (-1, 0), // sliced
    (-1, 1), // sliced
    (1,-2),
    (2,-2),
    (-2,2), // sliced
    (-2,1), // sliced
    (2,-1),
    (2,0),
    (1,-1),  // sliced
    (1,1),  // sliced
    (2,1),
    (-2,0), // sliced
    (-2,-1), // sliced
    (2,2),
    (0,1), // sliced
    (-1,-1), // sliced
    (-2,1),
    (-2,-2),
    (1,2),
    (0,2),
    (1,0),  // sliced
    (0,-1), // sliced
    (-1,2)
  )


  // once a city object has been initiated, it will remove the mapping from this list
  protected[model] def RandomTextureAssigning : List[Int] =
    //println(this.cityTexture) // for debug purposes
    // to select and pop one random city texture from the city texture variable
    // and assign it to a an object of the class city
    val selectedTexture : List[Int] = this.cityTexture(Random.nextInt(3))
    //println(this.cityTexture) // for debug purposes
    selectedTexture
  end RandomTextureAssigning

  private var possibleGenerationPoints : List[(Int, Int, Int, Int)=> Boolean] =
    List(
      (x : Int, y : Int , maxY : Int, maxX : Int) => (x >= 50 && x <= 100) && (y >= 50 && y <= 100),
      (x: Int, y : Int, maxY : Int, maxX : Int) => ( x>= 50 && x< 100) && ( y>= maxY -100 && y <= maxY -50),
      (x: Int, y : Int, maxY : Int, maxX : Int) => ( x>= maxX -100 && x <= maxX -50) && (y >= 50 && y <= 100),
      (x: Int, y : Int, maxY : Int, maxX : Int) => ( x>= maxX -100 && x <= maxX -50) && ( y>= maxY -100 && y <= maxY -50)
    )
  // the previous filer : cityCoordsX >80 && cityCoordsX < mapHeight - 80 && cityCoordsY > 80 && cityCoordsY < mapWidth -80 && mapRegion(cityCoordsX)(cityCoordsY) == 1
  // a city will only be generated in a fixed generation point, random between the 4 spawn centers
  protected[model] def GetGenerationPoint : (Int, Int) =
    // get the filter
    //println(this.possibleGenerationPoints) // to debug
    val pointFilter : (Int,Int,Int,Int) => Boolean = possibleGenerationPoints.head
    this.possibleGenerationPoints = possibleGenerationPoints.tail
    // debug purpose:
    //println(this.possibleGenerationPoints)

    // declare flag to stop the loop once a generation point has been found
    var hasGeneratedPoints : Boolean = false
    var points : (Int,Int) = (0,0)

    while !hasGeneratedPoints do
      // define potential points for city
      val point_x : Int = Random.between(1, BiomeMap.mapHeight)
      val point_y : Int = Random.between(1, BiomeMap.mapWidth)
      // obtain constraints from BiomeMap object
      val max_x : Int = BiomeMap.mapHeight
      val max_y : Int = BiomeMap.mapWidth

      if (pointFilter(point_x, point_y, max_x, max_y)) then
        // side note, the regions in which the point is selected, is all 1's due to the filtering method added into the isValid fx in BiomeMap
        hasGeneratedPoints = true
        points = (point_x, point_y) // store them as return value
        //println(f"Points for city generation starts at $points") // for debug
      end if
    points
  end GetGenerationPoint

  private def GrowthFunctionReg(pop : Int, hosp : Int, rate : Int) : Int =
    Population.totalFood -= 1000 + Random.nextInt(rate)
    // growth rate at constant : 0.03
    // so 5000 +
    (pop + (pop * 0.05) + hosp * (pop*0.02)).toInt
  end GrowthFunctionReg

end City



class City():
  // to hold all background process list of each city

  // initiate local population
  var local_population : Int = City.start_population

  var consumption_rate : Int = (0.5 * local_population).toInt
  val texturePacks : List[Int] = RandomTextureAssigning
  // will have fixed texture packs
  val generationPoint : (Int, Int) = GetGenerationPoint
  // will have fixed spawning locations
  // list of all city tiles:
  var cityTiles : scala.collection.mutable.Set[(Int, Int)] = scala.collection.mutable.Set[(Int, Int)]()
  // put a counter on the urban tiles
  var urbanTileCounter : Int = 0
  // put a counter on the suburban tiles

  // put a buffer list for urban, suburban, rural tile points
  protected[model] var urbanTilePoints : scala.collection.mutable.ListBuffer[(Int,Int)] = ListBuffer()
  protected[model] var suburbanTilePoints : scala.collection.mutable.ListBuffer[(Int,Int)] = ListBuffer()
  protected[model] var ruralTilePoints: scala.collection.mutable.ListBuffer[(Int, Int)] = ListBuffer()


  var suburbanTileCounter : Int = 0
  // put a counter on the rural tiles
  var ruralTileCounter : Int = 0

  var buildingEffects: Array[Int] = Array(0,0,0,0,0,0) // stored effect of building on money production


  // define variable to mark city will grow or not
  private var maxPopulationTotal : Int = City.max_population

  //  function to call edit or update BiomeMap Map Data
  protected[model] def AddCityToMap( point : (Int,Int)): Unit =
    // unpack tuple data
    val coordsX : Int = point(0)
    val coordsY : Int = point(1)
    // access BiomeMap Data & assign the first point
    this.cityTiles.add((coordsX,coordsY))
    BiomeMap.mapRegion(coordsX)(coordsY) = texturePacks(0)
    // first tile of a city is always an urban tile
    this.urbanTileCounter += 1
    Population.population_total += this.local_population
    //println(s"The city has been added at $coordsX, $coordsY")
  end AddCityToMap

  // function to expand city
  protected[model] def ExpandCity(): Unit =
    // potential to modify:: with isExpandable? --> Stops this function from executing once total tiles has reach max amount of tiles
    // the function always update only one tile, by changing 1 on map array data to one of city's texture tile
    // step 1: define a flag for to initiate the loop
    //val startTime = System.nanoTime()
    var cityExpandedFlag : Boolean = false
    var tileToAdd: Int = 4
    // step 2 : initiate a loop
    while !cityExpandedFlag do
      // obtain the next direction from the direction vector collection
      val pointedDirection: (Int, Int) = City.directions.dequeue()
      // move the direction used to the back
      City.directions.enqueue(pointedDirection)
      //println(pointedDirection)
      // unpack the direction into
      // 1. dx - changes in x
      val dx : Int = pointedDirection(0)
      // 2. dy - changes in y
      val dy : Int = pointedDirection(1)

      // initiate another loop
      while tileToAdd != 0 do
        for ((x,y) <- this.cityTiles if !cityExpandedFlag && tileToAdd!=0 )
          // calculate the next to-be tile in the city
          val nextX : Int = x + dx
          val nextY : Int = y + dy
          //println(nextX +"," + nextY +" now at")
          // filter
          if CheckValidPoints(i = nextX, j = nextY) then
            // check if the urban city tile has reached is limit

            // add new coordinates to the this.cityTiles
            if urbanTileCounter != max_urban_tiles then
              val tag: Int = this.texturePacks(0)
              BiomeMap.mapRegion(nextX)(nextY) = tag
              urbanTileCounter += 1
              this.cityTiles.add((nextX, nextY))
            //  println(f"Urban tile for this city is $urbanTileCounter") // for debug purposes
              // deduct the tile to add counter
              tileToAdd -= 1
              if tileToAdd == 0 then
                cityExpandedFlag = true
              end if
              
            else if suburbanTileCounter !=  max_suburban_tiles then
              val tag : Int = this.texturePacks(1)
              BiomeMap.mapRegion(nextX)(nextY) = tag

            //  println(f"subsurban tile added to $nextX,$nextY") // for debug purposes
              this.suburbanTileCounter += 1
              this.cityTiles.add((nextX, nextY))
            //  println(f"Suburban tile for this city is $suburbanTileCounter") // for debug purposes
              // set the expanded flag to true
              // deduct the tile to add counter
              tileToAdd -= 1
              if tileToAdd == 0 then
                cityExpandedFlag = true
              end if
              

            else if ruralTileCounter != max_rural_tiles then
              BiomeMap.mapRegion(nextX)(nextY) = this.texturePacks(2)
            //  println(f"rural tile assed to $nextX,$nextY")
              this.ruralTileCounter += 1
              this.cityTiles.add((nextX, nextY))
            //  println(f"rural tile for this city is $ruralTileCounter") // for debug
              // set the expanded flag to true
              // deduct the tile to add counter
              tileToAdd -= 1
              if tileToAdd == 0 then
                cityExpandedFlag = true
              end if

            end if
          end if
        end for
      end while
    end while
    //val endTime = System.nanoTime()
    //println(f"At ${cityTiles.size} : Duration : ${(endTime -startTime)/1e9d}")
  end ExpandCity

  private def CheckValidPoints(i : Int, j : Int): Boolean=
    (i > 0 ) && (i < BiomeMap.mapHeight - 1) && (j > 0) && (j < BiomeMap.mapWidth - 1) && BiomeMap.mapRegion(i)(j) == 1
  end CheckValidPoints



  private def IfValidTileThenConvert(point :(Int,Int), tagText: Int): Unit=
    val (x,y) = point
    var dir : Seq[(Int,Int)] =  Seq((-1,0),(0,1),(1,0),(0,-1))

    var counter : Int = dir.size

    while counter != 0 do
      val dir_unit : (Int,Int) = dir.head
      dir = dir.tail // to cycle the seq
      val cX : Int = x + dir_unit._1
      val cY : Int = y + dir_unit._2
      if CheckValidPoints(i = cX, j = cY) then
        BiomeMap.mapRegion(cX)(cY) = tagText
      end if
      counter -= 1
    end while
  end IfValidTileThenConvert







  // define growth model function
  protected[model] def CityPopulationGrowth(): Unit =
    // check for conditions
    //println(f"$local_population")
    this.local_population = City.GrowthFunctionReg(pop = this.local_population,hosp =this.buildingEffects(3), rate = this.consumption_rate)
    // once a new population value has occurred
    //println(f"$local_population")
    // do check to ensure local population doesnt exceed threshold
    if this.local_population > City.max_population then
      this.local_population = City.max_population
      //println("local population capped at 300000")
      // for debug purposes
      // set max population flag to mark city has reached max population
    end if

  protected[model] def Revenue(): Unit =
    // count all tiles
    val profit1 : Double = urbanTileCounter * 2.5
    val profit2 : Double = suburbanTileCounter * 1.0
    val profit3 : Double = ruralTileCounter * 0.75
    val profit4 : Double = this.buildingEffects(0) * 15.35

    // add money produced by revenue of all city tiles -- tax
    Population.totalMoney += (profit1 + profit2 + profit3 + profit4)

    // more source of revenue
    // 0 - commercial center (produce more money)
    // 1 - electric station (consume more money)
    // 2 - Farm (produce food, add to population food)
    // 3 - Hospital (increase birth rate)
    // 4  - university (produce money)
    // 5 - waterstation (consume some money)
    Population.totalMoney += (this.buildingEffects(0) * 500)
    Population.totalMoney -= (this.buildingEffects(1) * 150) // to provide electricity to city's
    Population.totalFood += (this.buildingEffects(2) * 3500) // farms mass produce food resupply to goverment
    Population.totalMoney += (this.buildingEffects(4) * 4000)
    Population.totalMoney -= (this.buildingEffects(5)* 300)
  end Revenue

//  // implement a buildings list
  private var buildingList : BuildingList[Building] = new BuildingList()

  // define function to retrieve one coordinate from ListOfCityTiles
  private def RetrieveRandomPoint(): (Int,Int)=
    val coordinates : List[(Int,Int)]= this.cityTiles.toList
    val coordinate : (Int,Int) = coordinates(Random.nextInt(this.cityTiles.size) + 1)
    coordinate
  end RetrieveRandomPoint




  def AddHospital(): Unit =
    val buildingCoords: (Int,Int) = RetrieveRandomPoint()
    val x : Int = buildingCoords(0)
    val y : Int = buildingCoords(1)
    val hospital : Hospital = new Hospital(pointX = x, pointY = y)
    BiomeMap.mapRegion(x)(y) = 12 // # add Hospital Texture Pack
    this.buildingList.Add(hospital)
    this.buildingEffects = hospital.AddBuildingEffects(this.buildingEffects)
    this.maxPopulationTotal = (City.max_population + City.max_population*(0.05 * this.buildingEffects(3)).ceil.toInt)
    Population.totalMoney -= hospital.buildingPrice
  end AddHospital

  def AddCommercialCenter(): Unit =
    val builingCoords : (Int,Int) = RetrieveRandomPoint()
    val x : Int = builingCoords(0)
    val y : Int = builingCoords(1)

    val comCenter : CommercialCenter = new CommercialCenter(pointX = x, pointY = y)
    BiomeMap.mapRegion(x)(y) = 14//# CommercialCenter texture pack
    this.buildingList.Add(comCenter)
    this.buildingEffects = comCenter.AddBuildingEffects(this.buildingEffects)

    Population.totalMoney -= comCenter.buildingPrice
  end AddCommercialCenter

  def AddFarm(): Unit =
    val coords : (Int,Int) = RetrieveRandomPoint()
    val x : Int = coords(0)
    val y : Int = coords(1)
  //     add effect : city now produces food
  //                : price to buy food decreases
    val farm : Farm = new Farm(pointX = x, pointY = y)
    BiomeMap.mapRegion(x)(y) = 13 //# Farm texture reference
    this.buildingList.Add(farm)
    this.buildingEffects = farm.AddBuildingEffects(this.buildingEffects)
    Population.totalMoney -= farm.buildingPrice
  end AddFarm


  def AddUniversity(): Unit =
    val coords : (Int,Int) = RetrieveRandomPoint()
    val x : Int = coords(0)
    val y : Int = coords(1)
    val uni : University = new University(pointX = x, pointY = y)
    BiomeMap.mapRegion(x)(y) = 15
    this.buildingList.Add(uni)
    this.buildingEffects = uni.AddBuildingEffects(this.buildingEffects)
    Population.totalMoney -= uni.buildingPrice
  end AddUniversity

  def AddWaterStation(): Unit =
    val coords : (Int,Int) = RetrieveRandomPoint()
    val x: Int = coords(0)
    val y : Int = coords(1)
    val ws : WaterStation = new WaterStation(pointX = x, pointY = y)
    BiomeMap.mapRegion(x)(y)= 16
    this.buildingList.Add(ws)
    this.buildingEffects = ws.AddBuildingEffects(this.buildingEffects)
    Population.totalMoney -= ws.buildingPrice
  end AddWaterStation

  def AddElectricStation(): Unit =
    val coords : (Int,Int) = RetrieveRandomPoint()
    val x : Int = coords(0)
    val y : Int = coords(1)
    val es : Electricstation = new Electricstation(pointX = x, pointY = y)
    BiomeMap.mapRegion(x)(y) = 17
    this.buildingList.Add(es)
    this.buildingEffects = es.AddBuildingEffects(this.buildingEffects)
    Population.totalMoney -= es.buildingPrice
  end AddElectricStation



end City

