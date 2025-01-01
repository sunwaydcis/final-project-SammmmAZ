package model

import model.City.{GetGenerationPoint, RandomTextureAssigning}
import scalafx.scene.image.Image
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
        hasGeneratedPoints = true
        points = (point_x, point_y) // store them as return value
        println(f"Points for city generation starts at $points") // for debug
      end if
    points
  end GetGenerationPoint




end City



class City():
  // to hold all background process list of each city
  var backgroundProcesslist : List[Runnable] = List()

  val texturePacks : List[Int] = RandomTextureAssigning
  // will have fixed texture packs

  val generationPoint : (Int, Int) = GetGenerationPoint
  // will have fixed spawning locations
  
  // list of all city tiles:
  var cityTiles : scala.collection.mutable.Set[(Int, Int)] = scala.collection.mutable.Set[(Int, Int)]().add(generationPoint)
  
  //  





end City
