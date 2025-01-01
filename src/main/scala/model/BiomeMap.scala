package model

// import necessary libraries
import scalafx.scene.Scene
import scalafx.scene.control.ScrollPane
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.Pane
import scala.collection.mutable.Queue
import scala.util.{Success, Failure}
import scala.concurrent.*
import scala.concurrent.ExecutionContext.Implicits.global
import javafx.application.Platform
import scala.util.{Random, Success}



// represents model class / singleton object for Game Map
// map has many tiles 
// tiles can be of 3-6 types which reflect their biome type
// 
// Game implements 4-5 Biomes, keep 2 or 3 for simplicity
// --> Plains
// --> River/ Lakes/ Oceans
// --> Mountains
//
// --> Plains biome is most desirable by population, occupied plains tiles can reflect 200/ 400 or 500 depending
//     on Game difficulty
// --> Water / Mountain biome , not habitable by default, a population center is required to be within certain range 
//     of these 2 types of tiles to acquire progress

object BiomeMap:

  // object of map class will have the following attributes, reasons?
  // singleton : reason? BiomeMap should be able to be accessed from any other objects
  //           > Globally available, can be accessed from any part of the application
  // map will have pixels of 450 by 450
  // a suggestion to increase the size or decrease it based on game difficulty

  var gameMap : ScrollPane = loadBiomeMap
  // image var traits
  // tile pixel size
  protected[model] var mapHeight: Int = 250
  protected[model] var mapWidth: Int = 250
  // tileSize : Corresponds to pixel length or width of sprite used as map floor
  // check sprite file, if sprite pixel size is 8x8, then enter 8
  // if sprite size is bigger than this, the sprite will be automatically scaled down
  // else: Sprite size smaller than this, will cause holes to appear in app
  // rule : scale the tileSize down to smallest size of pixel length of tile used
  protected[model] val tileSize: Int = 8


  // difficulty adjustment
  // biome proportions
  // map frame size 
  // scene generator 
  val isDynamic : Boolean = true

  // Biome distribution ratios
  // game now has 3 biomes:
  protected[model] var mountainRatio: Double = 0.05
  protected[model] var waterRatio: Double = 0.15
  protected[model] var plainsRatio: Double = 1 - mountainRatio - waterRatio

  // set water, plains and mountain tiles & city tile
  private val plainsTile: Image = new Image(getClass.getResource("/image/tiles/plainsTile.png").toExternalForm)
  private val waterTile: Image = new Image(getClass.getResource("/image/tiles/waterTile.png").toExternalForm)
  private val mountainTile: Image = new Image(getClass.getResource("/image/tiles/mountainFloorTile.png").toExternalForm)
  // new city tile
  //val cityTile : Image = new Image(getClass.getResource("/image/tiles/cityTile.png").toExternalForm)
  val cityTile : Image = new Image(getClass.getResource("/image/tiles/city/CT1_urban.jpg").toExternalForm)


  // added new texture packs for city's
  
  // for city A
  // numbered 3
  protected[model] val city_set_1a : Image = new Image(getClass.getResource("/image/tiles/city/CT1_urban.jpg").toExternalForm)
  // numbered 4
  protected[model] val city_set_1b : Image = new Image(getClass.getResource("/image/tiles/city/CT1_suburban.jpg").toExternalForm)
  // numbered 5
  protected[model] val city_set_1c : Image = new Image(getClass.getResource("/image/tiles/city/CT1_rural.jpg").toExternalForm)
  
  // for city B
  // numbered 6
  val city_set_2a : Image = new Image(getClass.getResource("/image/tiles/city/CT2_Urban.png").toExternalForm)
  // numbered 7
  val city_set_2b : Image = new Image(getClass.getResource("/image/tiles/city/CT2_suburban.png").toExternalForm)
  // numbered 8
  val city_set_2c : Image = new Image(getClass.getResource("/image/tiles/city/CT2_rural.png").toExternalForm)
  
  // for city C
  // numbered 9
  val city_set_3a : Image = new Image(getClass.getResource("/image/tiles/city/CT3_Urban.jpg").toExternalForm)
  // numbered 10
  val city_set_3b : Image = new Image(getClass.getResource("/image/tiles/city/CT3_Suburban.png").toExternalForm)
  // numbered 11
  val city_set_3c : Image = new Image(getClass.getResource("/image/tiles/city/CT3_Rural.png").toExternalForm)
  // define data structure that will update the map
  val mapRegion: Array[Array[Int]] = Array.fill(mapHeight, mapWidth)(1)

  // to track city tiles
  var cityTiles = scala.collection.mutable.Set[(Int, Int)]() // Track city tile coordinates

  // to map map region data to imageview
  // maps interger to image view
  val regionToSprite: Map[Int, Image] = 
    Map(0 -> waterTile,
      1 -> plainsTile,
      2 -> mountainTile,
      3 -> city_set_1a,
      4 -> city_set_1b,
      5 -> city_set_1c,
      6 -> city_set_2a,
      7 -> city_set_2b,
      8 -> city_set_2c,
      9 -> city_set_3a,
      10 -> city_set_3b,
      11 -> city_set_3c)


  private def IsValid(x: Int, y: Int, visited: Array[Array[Boolean]], mapRegions: Array[Array[Int]]): Boolean =
    // assuming that X & Y is not on the edge of the app or exceeds normal bounds of the map,
    // and the specific tile has not been visited, by checking the !visited(x)(y)
    // if the tile is valid, and has not been visited
    // and has not been modified, as mapRegions(x)(y) == 1 -> Plains tile
    val condition_1 : Boolean = ((50 < x && x <=100) && (50 < y && y <= 100))
    val condition_2 : Boolean = (mapHeight - 100 < x && x <= mapHeight - 50) && (mapWidth - 100 < y && y <= mapWidth - 50)
    val condition_3 : Boolean = (mapHeight - 100 < x  && x <= mapHeight - 50) && (50 < y && y <= 100)
    val condition_4 : Boolean = (50 < x && x <= 100) && (mapWidth - 100 < y && y <= mapWidth - 50)
    // put conditions
    x >= 0 && y >= 0 && x <= mapHeight - 1 && y <= mapWidth -1  && !visited(x)(y) && mapRegions(x)(y) == 1 && !condition_1 && !condition_2 && !condition_3 && !condition_4

  private def randomFloodFill(startX: Int, startY: Int, biomeType: Int, maxSize: Int, visited: Array[Array[Boolean]], mapRegions: Array[Array[Int]]): Unit =
    // copies the logic of a floodfill algorithm to
    // distribute and generate the biome

    // define directions to add biomes,
    // a point selected, will change or convert blocks up, down, left and right of it to a similar biome
    val directions: List[(Int, Int)] = List((0, 1), (1, 0), (0, -1), (-1, 0))

    // define a queue
    // from where to start and change the tiles according to the biome
    val queue = scala.collection.mutable.Queue((startX, startY))
    // serves as counter to check for amount tiles added
    var tilesAdded = 0

    while queue.nonEmpty && tilesAdded < maxSize do
      // extract x,y coordinate from the end of the queue
      val (cx, cy) = queue.dequeue()
      // check if the x,y coordinates are valid, if so then swap the biomes
      if IsValid(cx, cy, visited, mapRegions) then
        mapRegions(cx)(cy) = biomeType
        // mark tiles as visited
        visited(cx)(cy) = true
        // add to the counter
        tilesAdded += 1
        // shuffle for randomness effect
        Random.shuffle(directions).foreach { case (dx, dy) =>
          queue.enqueue((cx + dx, cy + dy))
        }


  //def BiomeMapGenerator : Unit
  def GenerateBiomeMap( mapDataArray : Array[Array[Int]]): Unit =
    // Get number of total tiles:
    val totalTiles = mapWidth*mapHeight

    val mountainCount = (totalTiles * mountainRatio).toInt
    val waterCount = (totalTiles * waterRatio).toInt

    // initialize an array, all start with 1s
    val mapRegion : Array[Array[Int]] = mapDataArray
    // initialize another array, filled with booleans
    // work togather with floodfill algorithm for randomness effect
    // checks if a region of the map has been visited by the floodfill algorithm
    val visitedRegion : Array[Array[Boolean]] = Array.fill(mapHeight, mapWidth)(false)

    // add water tiles counter
    // initially map has 0 water tiles
    var currentWaterCount =  0

    while currentWaterCount < waterCount do
      // selects a random point on the map
      val startX = Random.nextInt(mapHeight)
      val startY = Random.nextInt(mapWidth)
      // checks if the point is not altered/
      // if the point has 1, a plains tile, then
      if mapRegion(startX)(startY) == 1 then
        val lakeSize = Random.between(10,25)
        randomFloodFill( startX = startX, startY = startY, biomeType = 0, maxSize= lakeSize, visited=visitedRegion, mapRegions = mapRegion)
        currentWaterCount += lakeSize

        // mark the visited coordinates
        visitedRegion.foreach( row => java.util.Arrays.fill(row,false))
      end if
    end while

    // add mountain tile counter
    // map doesn't randomly generate with
    var currentMountainCount = 0

    while currentMountainCount < mountainCount do
      // selects a random point on the map
      val startX = Random.nextInt(mapHeight)
      val startY = Random.nextInt(mapWidth)
      // check if point has been visited or not
      if mapRegion(startX)(startY) == 1 then
        val mountainSize = Random.between(10, 30)
        randomFloodFill(startX, startY, 2, mountainSize, visitedRegion, mapRegion)
        currentMountainCount += mountainSize
      end if
    end while

    // creates scroll pane


    // initialize city center
    var cityAddedFlag = false
    while !cityAddedFlag do
      var cityCoordsX : Int = Random.nextInt(mapHeight)
      var cityCoordsY : Int = Random.nextInt(mapWidth)
      if cityCoordsX >80 && cityCoordsX < mapHeight - 80 && cityCoordsY > 80 && cityCoordsY < mapWidth -80 && mapRegion(cityCoordsX)(cityCoordsY) == 1 then
        cityTiles.add((cityCoordsX, cityCoordsY))
        // adds the city coordinate to the cityTiles
        mapRegion(cityCoordsX)(cityCoordsY) = 3
        // change the map region array data for that coordinate to be 3
        cityAddedFlag = true
        // change the flag tp true
      end if
    end while




    // maps interger to image view
    val regionToSprite = Map(0 -> waterTile, 1 -> plainsTile, 2 -> mountainTile, 3 -> cityTile)

    // initialize city center

    // creates a pane
    val pane = new Pane()
    for i <- mapRegion.indices do
      for j <- mapRegion(i).indices do
        val region = mapRegion(i)(j)
        val tile = regionToSprite(region)
        val imageView = new ImageView(tile):
          x = j * tileSize
          y = i * tileSize
          fitWidth = tileSize
          fitHeight = tileSize
          preserveRatio = false
        pane.children.add(imageView)
      end for
    end for



    val scrollPane = new ScrollPane:
      content = pane
      prefWidth = 1280
      prefHeight = 700
      pannable = true
    end scrollPane

    // returns the scroll pane
    this.gameMap = scrollPane
  end GenerateBiomeMap


  // public access method to load the BiomeMap
  def loadBiomeMap : ScrollPane =
    this.gameMap
  end loadBiomeMap

  // define a function to update the MapRegion data structure
  //


  // define a function to update the BiomeMap Control view
  private def UpdateMapView() : Unit =
    // returns the ScrollPane view with updated

    // record original hVal and vVal to make sure scroll pane stays at same position
    val hVal: Double = this.gameMap.hvalue.toDouble
    val vVal : Double = this.gameMap.vvalue.toDouble

    val updatedScrollPane = new ScrollPane:
      content = new Pane:
        // goes through the map
        for i <- mapRegion.indices do
          for j <- mapRegion(i).indices do
            val region = mapRegion(i)(j)
            val tile = regionToSprite(region)
            val imageView = new ImageView(tile):
              x = j * tileSize
              y = i * tileSize
              fitWidth = tileSize
              fitHeight = tileSize
              preserveRatio = false
            end imageView
            children.add(imageView)
          end for
        end for
    // update the game map
    this.gameMap = updatedScrollPane
    this.gameMap.hvalue = hVal
    this.gameMap.vvalue = vVal
    // debugger
    Platform.runLater(() => updatedScrollPane.requestLayout())
    //println("BiomeMap update map view has been invoked")
  end UpdateMapView

  // remove access from other functions
  private def UpdateMapData(mapData : Array[Array[Int]], knownCityTiles : scala.collection.mutable.Set[(Int, Int)]) : Unit =
    // use sequence data structure to store order in which the city will expand
    val directions = Seq(
      (-1, 0), // North
      (1, 0), // South
      (0, -1), // West
      (0, 1), // East
      (-1, -1), // North-West
      (-1, 1), // North-East
      (1, -1), // South-West
      (1, 1) // South-East
    )

    // iterate through city tiles
    for (x,y) <- knownCityTiles do
      // check for all tiles in all direction of the set of known city tiles
      var isCurrentCityUpdated : Boolean = false

      for ((dx,dy) <- directions if !isCurrentCityUpdated) do
        // for every tile adjacent to a city tile
        val newX = x + dx
        val newY = y + dy

        //check if the new tile is within bounds
        if newX > 0 && newX < mapWidth && newY > 0 && newY < mapHeight && mapRegion(newX)( newY) == 1 then
          // convert said coordinate on mapRegion data structure to 3 for city tile
          mapRegion(newX)( newY) = 3
          // add the new set of coordinate to CityTiles
          cityTiles.add((newX, newY))
          //println(f"$newX, $newY has been added to city tiles")
          // change it to true to update the city once
          isCurrentCityUpdated = true
        end if
      end for
    end for
  end UpdateMapData


  // define a function that updates the map data structure
  // only update one tile
  protected[model] def UpdateMapDataII(mapData : Array[Array[Int]], knownCityTiles : scala.collection.mutable.Set[(Int,Int)]): Unit =
    // define  a flag to terminate the while loop when tile is added
    var tileAdded : Boolean = false
    // define a flag to terminate the loop once there is no more tile to add
    var tiletoAdd : Int = 1

    while !tileAdded do
      // get the next tile direction
      val nextTileVector : (Int,Int) = GetDirection(directions)
      val dx : Int = nextTileVector(0)
      val dy : Int = nextTileVector(1)

      while tiletoAdd != 0 do
        for ((x,y)<- knownCityTiles  if !tileAdded && tiletoAdd!= 0) 
          // get coordinate of city tile
          val x_coordinate : Int = x
          val y_coordinate : Int = y
          // get coordinate of next city tile
          val nextX : Int = x + dx
          val nextY : Int = y + dy

          // get coordinate of adjacent tile to the current city tile in the direction of the current vector
          val newX : Int =  x_coordinate + dx
          val newY : Int = y_coordinate + dy

          // check if the tile is valid with IsValid function
          if newX > 0 && newX < mapWidth - 1 && newY > 0 && newY < mapHeight - 1 && mapRegion(newX)(newY) == 1 then
            // add the city tile
            mapRegion(newX)(newY) = 3
            // add the city tile to the set
            knownCityTiles.add((newX, newY))
            //println(f"City has added tile : $newX, $newY") // for debug purposes
            // update the flags to terminate the loop
            tiletoAdd -= 1
            tileAdded = true
          end if
        end for
      end while
    end while
  end UpdateMapDataII


  // direction data:
  private var directions : scala.collection.mutable.Queue[(Int,Int)] = Queue(
    (-1, 0), // North
    (1, 0), // South
    (0, -1), // West
    (0, 1), // East
    (-1, -1), // North-West
    (-1, 1), // North-East
    (1, -1), // South-West
    (1, 1) // South-East
  )

  // defined helper function to parse through the directions available
  private def GetDirection( setOfDirections: scala.collection.mutable.Queue[(Int,Int)]): (Int,Int) =
    // takes in direction sequence at the parameter
    // removes first element
    //println(setOfDirections) // debug purposes
    val pointedDirection : (Int,Int)= setOfDirections.dequeue()
    // adds it back to the end of the queue
    setOfDirections.enqueue(pointedDirection)
    //println(setOfDirections) // debug purposes
    // returns the pointedDirection
    pointedDirection
  end GetDirection

  // define wrapper functions to wrap updateMapView as runnable
  def RunnableUpdateMapView(): Runnable =
    new Runnable {
      override def run(): Unit =
        UpdateMapView()
        //println("RunnableUpdateMapView has been executed")
    }
  end RunnableUpdateMapView

  // define wrapper function tp wrap update map data structure
  def RunnableUpdateMapData(): Runnable =
    () => UpdateMapData(mapData = this.mapRegion, knownCityTiles = cityTiles)
  end RunnableUpdateMapData

  // define wrapper function to wrap the updateMapDatastructure function II
  def RunnableUpdateMapDataII(): Runnable =
    () => UpdateMapDataII(mapData = this.mapRegion, knownCityTiles = cityTiles)
  end RunnableUpdateMapDataII



