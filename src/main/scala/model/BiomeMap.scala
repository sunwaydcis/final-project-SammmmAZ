package model

// import necessary libraries
import BiomeMapApp.MainApp
import scalafx.scene.Scene
import scalafx.scene.control.{Label, ScrollPane, Separator}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, Pane, VBox}

import scala.collection.mutable.{ListBuffer, Queue}
import scala.util.{Failure, Success}
import scala.concurrent.*
import scala.concurrent.ExecutionContext.Implicits.global
import javafx.application.Platform
import scalafx.scene.control.ProgressBar
import scalafx.geometry.Orientation.VERTICAL

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

  var gameMap: ScrollPane = loadBiomeMap
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
  // define other data for buildings
  val comCenterImage : Image = CommercialCenter.commercialCenterTile
  // define data for Electric Station
  val electricStationTile : Image = Electricstation.electricStationTile
  // define data for
  val farmTile : Image = Farm.farmTile
  val hospitalTile : Image = Hospital.hospitalTile
  val uniTile : Image = University.UniversityTile
  val waterStationTile : Image = WaterStation.waterStationTile

  // define a list of City objects
  var ListOfCity : ListBuffer[City] = ListBuffer()

  // to track city tiles
  var cityTiles : scala.collection.mutable.Set[(Int, Int)]= scala.collection.mutable.Set[(Int, Int)]() // Track city tile coordinates
  val commercialCenter : Image = new Image(getClass.getResource("/image/tiles/cityTile.png").toExternalForm)
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
      11 -> city_set_3c,
      12 -> hospitalTile, // for hospital
       13 -> farmTile, // for Farm
       14 -> commercialCenter, // for commercial center
      15 -> uniTile, // for university
      16 -> waterStationTile, // for water tile
      17 -> electricStationTile // for electric tile
    )


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
    var currentMountainCount : Int = 0

    while currentMountainCount < mountainCount do
      // selects a random point on the map
      val startX : Int= Random.nextInt(mapHeight)
      val startY : Int= Random.nextInt(mapWidth)
      // check if point has been visited or not
      if mapRegion(startX)(startY) == 1 then
        val mountainSize : Int = Random.between(10, 30)
        randomFloodFill(startX, startY, 2, mountainSize, visitedRegion, mapRegion)
        currentMountainCount += mountainSize
      end if
    end while

    // instantiates a city
    var city1 : City = new City
    city1.AddCityToMap(city1.generationPoint)
    this.ListOfCity.append(city1)
    




    // creates a pane
    val pane = new Pane()
    for i <- mapRegion.indices do
      for j <- mapRegion(i).indices do
        val region = mapRegion(i)(j)
        val tile = this.regionToSprite(region)
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

  // Create a function that wraps the GameMap in a VBox with all the labels


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

  // define new functions to work with Cities
  protected[model] def UpdateMapData(): Unit =
    //println("UpdateMapDataIII has been called")
    var counter : Int = 1 // for debug purposes
    for city <- this.ListOfCity do
      city.ExpandCity()
      //println(s"City $counter has expanded") // for debug purposes
    end for
  end UpdateMapData


  // define wrapper functions to wrap updateMapView as runnable
  // to be made public, accessed via mainApp.scala
  def RunnableUpdateMapView(): Runnable =
    new Runnable {
      override def run(): Unit =
        UpdateMapView()
        //println("RunnableUpdateMapView has been executed")
    }
  end RunnableUpdateMapView


  // define a function to add a city to the BiomeMap
  private def AddMapToCity(): Unit =
    // initiate a city object
    val city: City = new City()
    //println("AddMapToCity invoked")
    // add city to the map
    city.AddCityToMap(city.generationPoint)
    // update BiomeMap member of CityList
    ListOfCity.append(city) // add generated city to list of city
  end AddMapToCity

  def ActionAddCityToMap(): Unit=
    //println("ActionAddCityToMap invoked")
    AddMapToCity()
  end ActionAddCityToMap


  // define new function to display all labels
  def ReturnMapWrapper(): VBox =
    val popLabel: Label = new Label("Population:"):
      prefHeight = 32
      prefWidth = 200
      style = "-fx-font-family: 'Cambria Bold Italic'; -fx-font-size: 14;"
    end popLabel

    val popNumbers: Label = new Label(Population.population_total.toString):
      prefHeight = 32
      prefWidth = 200
      style = "-fx-font-family: 'Cambria Bold Italic'; -fx-font-size: 14;"
    end popNumbers

    val moneyLabel: Label = new Label("Money:"):
      prefHeight = 32
      prefWidth = 200
      style = "-fx-font-family: 'Cambria Bold Italic'; -fx-font-size: 14;"
    end moneyLabel

    val moneyNumbers: Label = new Label(Population.totalMoney.toString):
      prefHeight = 32
      prefWidth = 200
      style = "-fx-font-family: 'Cambria Bold Italic'; -fx-font-size: 14;"
    end moneyNumbers

    val foodLabel : Label = new Label("Food: "):
      prefHeight = 32
      prefWidth = 200
      style = "-fx-font-family: 'Cambria Bold Italic'; -fx-font-size: 14;"
    end foodLabel

    val foodNumbers: Label = new Label(Population.totalFood.toString):
      prefHeight = 32
      prefWidth = 200
      style = "-fx-font-family: 'Cambria Bold Italic'; -fx-font-size: 14;"


    val dayLabel : Label = new Label("Days: "):
      prefHeight = 32
      prefWidth = 200
      style = "-fx-font-family: 'Cambria Bold Italic'; -fx-font-size: 14;"
    end dayLabel

    val dayNumber : Label = new Label(Population.growthCounter.toString):
      prefHeight = 32
      prefWidth = 200
      style = "-fx-font-family: 'Cambria Bold Italic'; -fx-font-size: 14;"
    end dayNumber

    val gameProgressTag : Label = new Label("Game Progress"):
      prefHeight = 32
      prefWidth = 120
      style = "-fx-font-family: 'Cambria Bold Italic'; -fx-font-size: 14;"

    val gameProgress : ProgressBar = new ProgressBar():
      progress = ReturnProgress()
      prefHeight = 32
      prefWidth = 800
    end gameProgress


    if gameProgress.getProgress == 1 then
      MainApp.ShowEndDialogue()


    // target population to win game is
    // Filled out predefined labels
    val labelHBox : HBox = new HBox():
      children = Seq(popLabel,popNumbers,new Separator { orientation = VERTICAL },foodLabel,foodNumbers,new Separator { orientation = VERTICAL }, moneyLabel, moneyNumbers,new Separator { orientation = VERTICAL }, dayLabel, dayNumber)
      prefWidth = 1200
    end labelHBox

    val progBox : HBox = new HBox():
      children = Seq(gameProgressTag,gameProgress)
      prefWidth = 1200
      prefHeight = 32


    val mapPane : ScrollPane = loadBiomeMap

    val wrappedMapBox : VBox = new VBox():
      children = Seq(labelHBox,progBox,mapPane)
    end wrappedMapBox
    return wrappedMapBox
  end ReturnMapWrapper

  def ReturnProgress():Double =
    if Population.population_total >= 10000 && Population.population_total <= 20000 then
      0.2
      else if Population.population_total >= 20000 && Population.population_total <= 30000 then
        0.4
      else if Population.population_total >= 30000 && Population.population_total <= 40000 then
        0.6
      else if Population.population_total >= 40000 && Population.population_total < 50000then
        0.8
      else if Population.population_total >= 50000 then
        1.0
        else
        0.0
    end if
  end ReturnProgress






  


