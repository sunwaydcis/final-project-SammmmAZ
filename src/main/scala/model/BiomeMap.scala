package model

// import necessary libraries
import scalafx.scene.Scene
import scalafx.scene.control.ScrollPane
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.Pane

import scala.util.Random



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

  
  // image var traits
  // tile pixel size
  var mapHeight: Int = 450
  var mapWidth: Int = 450
  // tileSize : Corresponds to pixel length or width of sprite used as map floor
  // check sprite file, if sprite pixel size is 8x8, then enter 8
  // if sprite size is bigger than this, the sprite will be automatically scaled down
  // else: Sprite size smaller than this, will cause holes to appear in app
  // rule : scale the tileSize down to smallest size of pixel length of tile used
  val tileSize: Int = 8


  // difficulty adjustment
  // biome proportions
  // map frame size 
  // scene generator 
  val isDynamic : Boolean = true

  // Biome distribution ratios
  // game now has 3 biomes:
  var mountainRatio: Double = 0.05
  var waterRatio: Double = 0.15
  var plainsRatio: Double = 1 - mountainRatio - waterRatio

  // set water, plains and mountain tiles
  val plainsTile: Image = new Image(getClass.getResource("/image/tiles/plainsTile.png").toExternalForm)
  val waterTile: Image = new Image(getClass.getResource("/image/tiles/waterTile.png").toExternalForm)
  val mountainTile: Image = new Image(getClass.getResource("/image/tiles/mountainFloorTile.png").toExternalForm)


  def IsValid(x: Int, y: Int, visited: Array[Array[Boolean]], mapRegions: Array[Array[Int]]): Boolean =
    // assuming that X & Y is not on the edge of the app or exceeds normal bounds of the map,
    // and the specific tile has not been visited, by checking the !visited(x)(y)
    // if the tile is valid, and has not been visited
    // and has not been modified, as mapRegions(x)(y) == 1 -> Plains tile
    x >= 0 && y >= 0 && x < mapHeight && y < mapWidth && !visited(x)(y) && mapRegions(x)(y) == 1

  def randomFloodFill(startX: Int, startY: Int, biomeType: Int, maxSize: Int, visited: Array[Array[Boolean]], mapRegions: Array[Array[Int]]): Unit =
    // copies the logic of a floodfill algorithm to
    // distribute and generate the biome

    // define directions to add biomes,
    // a point selected, will change or convert blocks up, down, left and right of it to a similar biome
    val directions = List((0, 1), (1, 0), (0, -1), (-1, 0))

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
  def GenerateBiomeMap(): ScrollPane =
    // Get number of total tiles:
    val totalTiles = mapWidth*mapHeight

    val mountainCount = (totalTiles * mountainRatio).toInt
    val waterCount = (totalTiles * waterRatio).toInt

    // initialize an array, all start with 1s
    val mapRegion : Array[Array[Int]] = Array.fill(mapHeight, mapWidth)(1)
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
      