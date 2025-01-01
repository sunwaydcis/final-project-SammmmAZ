package model

import model.City.{GetGenerationPoint, RandomTextureAssigning, max_rural_tiles, max_suburban_tiles, max_urban_tiles}
import scalafx.scene.image.Image

import scala.language.dynamics
// import texture packs for map processing key
import java.lang.Runnable
import scala.collection.mutable.ListBuffer
import scala.language.postfixOps
import scala.util.Random

// companion object city
object City:
  // to declare static variables for all class

  // tile related data
  private val max_tiles: Int = 200
  private val max_urban_tiles : Int= 50 // should be random between 30 - 60
  private val max_suburban_tiles : Int= 50 // should be capped at val where suburban tile and urban tile sum up to 100
  private val max_rural_tiles : Int= 100

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
  private var cityTexture :List[List[Int]] =
    List(
      // for the first texture pack of the BiomeMap
      List(
      3 ,
      4 ,
      5
    ),
      List(
        6 ,
        7 ,
        8
      ),
      List(
        9 ,
        10,
        11
      ))

  // once a city object has been initiated, it will remove the mapping from this list
  protected[model] def RandomTextureAssigning : List[Int] =
    println(this.cityTexture) // for debug purposes
    // to select and pop one random city texture from the city texture variable
    // and assign it to a an object of the class city
    val selectedTexture : List[Int] = this.cityTexture.head
    this.cityTexture.drop(0)
    println(this.cityTexture) // for debug purposes
    selectedTexture
  end RandomTextureAssigning

  private var possibleGenerationPoints : List[(Int, Int, Int, Int)=> Boolean] =
    List(
      (x : Int, y : Int , maxY : Int, maxX : Int) => (x >= 50 && x <= 100) && (y >= 50 && y <= 100),
      (x: Int, y : Int, maxY : Int, maxX : Int) => ( x>= 50 && x< 100) && ( y>= maxY -100 && y <= maxY -50),
      (x: Int, y : Int, maxY : Int, maxX : Int) => ( x>= maxX -100 && x <= maxX -50) && (y >= 50 && y <= 100),
      (x: Int, y : Int, maxY : Int, maxX : Int) => ( x>= maxX -100 && x <= maxX -50) && ( y>= maxY -100 && y <= maxY -50)
    )
  // a city will only be generated in a fixed generation point, random between the 4 spawn centers
  protected[model] def GetGenerationPoint : (Int, Int) =
    // get the filter
    println(this.possibleGenerationPoints) // to debug
    val pointFilter : (Int,Int,Int,Int) => Boolean = possibleGenerationPoints.head
    this.possibleGenerationPoints = possibleGenerationPoints.tail
    // debug purpose:
    println(this.possibleGenerationPoints)

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
        println(f"Points for city generation starts at $points") // for debug
      end if
    points
  end GetGenerationPoint
end City



class City():
  // to hold all background process list of each city
  var backgroundProcesslist : List[Runnable] = 
    List(
      () => ExpandCity()
    )

  val texturePacks : List[Int] = RandomTextureAssigning
  // will have fixed texture packs
  val generationPoint : (Int, Int) = GetGenerationPoint
  // will have fixed spawning locations
  // list of all city tiles:
  var cityTiles : scala.collection.mutable.Set[(Int, Int)] = scala.collection.mutable.Set[(Int, Int)]()
  // put a counter on the urban tiles
  var urbanTileCounter : Int = 0
  // put a counter on the suburban tiles
  var suburbanTileCounter : Int = 0
  // put a counter on the rural tiles
  var ruralTileCounter : Int = 0
  //  function to call edit or update BiomeMap Map Data
  protected[model] def AddCityToMap( point : (Int,Int)): Unit =
    // unpack tuple data
    val coordsX : Int = point(0)
    val coordsY : Int = point(1)
    // access BiomeMap Data & assign the first point
    BiomeMap.mapRegion(coordsX)(coordsY) = texturePacks(0)
    // first tile of a city is always an urban tile
    this.urbanTileCounter += 1
    println(s"The city has been added at $coordsX, $coordsY")
  end AddCityToMap
  // helper data for ExpandCity Function
  // direction data:
  private var directions: scala.collection.mutable.Queue[(Int, Int)] = Queue(
    (-1, 0), // North
    (1, 0), // South
    (0, -1), // West
    (0, 1), // East
    (-1, -1), // North-West
    (-1, 1), // North-East
    (1, -1), // South-West
    (1, 1) // South-East
  )

  // function to expand city
  protected[model] def ExpandCity(): Unit =
    // the function always update only one tile, by changing 1 on map array data to one of city's texture tile
    
    // step 1: define a flag for to initiate the loop
    var cityExpandedFlag : Boolean = false
    var tileToAdd: Int = 1
    // step 2 : initiate a loop 
    while !cityExpandedFlag do
      // obtain the next direction from the direction vector collection
      val pointedDirection: (Int, Int) = this.directions.dequeue()
      // move the direction used to the back
      this.directions.enqueue(pointedDirection)
      
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
          
          // filter
          if nextX > 0 && nextX < BiomeMap.mapHeight - 1 && nextY > 0 && nextY < BiomeMap.mapWidth - 1 && mapRegion(nextX)(nextY) == 1 then
            // check if the urban city tile has reached is limit
            if urbanTileCounter != max_urban_tiles then
              BiomeMap.mapRegion(nextX)(nextY) = this.texturePacks(0)
              println(f"Urban tile added to $nextX, $nextY") // for debug purposes
              urbanTileCounter += 1
              println(f"Urban tile for this city is $urbanTileCounter") // for debug purposes
              // set the expanded flag to true
              cityExpandedFlag = true
              // deduct the tile to add counter
              tileToAdd -= 1
            else if suburbanTileCounter !=  max_suburban_tiles then
              BiomeMap.mapRegion(nextX)(nextY) = this.texturePacks(1)
              println(f"subsurban tile added to $nextX,$nextY") // for debug purposes
              this.suburbanTileCounter += 1
              println(f"Suburban tile for this city is $suburbanTileCounter") // for debug purposes
              // set the expanded flag to true
              cityExpandedFlag = true
              // deduct the tile to add counter
              tileToAdd -= 1
            else if ruralTileCounter != max_rural_tiles then
              BiomeMap.mapRegion(nextX)(nextY) = this.texturePacks(2)
              println(f"rural tile assed to $nextX,$nextY")
              this.ruralTileCounter += 1
              println(f"rural tile for this city is $ruralTileCounter") // for debug 
              // set the expanded flag to true
              cityExpandedFlag = true
              // deduct the tile to add counter
              tileToAdd -= 1
            end if
          end if
        end for
      end while
    end while
  end ExpandCity
  
  // function to add specific buildings to the city
  // def AddHospital(): Unit
  //     add certain affects: use stronger growth function
  //     work over the algorithm
  
  // def AddCommercialCenter(): Unit
  //     add certain effects: Increase money production rate
  
  // def AddFarm(): Unit
  //     add effect : city now produces food 
end City
