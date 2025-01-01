package model

import model.City.RandomTextureAssigning
import scalafx.scene.image.Image
// import texture packs for map processing key
import java.lang.Runnable
import scala.collection.mutable.ListBuffer
import scala.language.postfixOps


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
  private var ListOfCity : scala.collection.mutable.ListBuffer[City] = ListBuffer[City]()

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
    this.cityTexture.drop(selectedTexture)
    println(this.cityTexture) // for debug purposes
    selectedTexture
  end RandomTextureAssigning

  private var possibleGenerationPoints : List[()=> Boolean] =
    List(
      (x : Int, y : Int , maxY : Int, maxX : Int) -> (x >= 50 && x <= 100) && (y >= 50 && y <= 100),
      (x: Int, y : Int, maxY : Int, maxX : Int) -> ( x>= 50 && x< 100) && ( y>= maxY -100 && y <= maxY -50)
    )
  // a city will only be generated in a fixed generation point, random between the 4 spawn centers
  protected[model] def GetGenerationBoundary : (Int, Int) =
end City



class City():
  // to hold all background process list of each city
  var backgroundProcesslist : List[Runnable] = List()
  val texturePacks : List[Int] = RandomTextureAssigning
  //val generationPoint : (Int, Int) =
  // will have fixed texture packs
  // will have fixed spawning locations




end City
