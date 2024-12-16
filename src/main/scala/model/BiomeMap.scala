package model

// import necessary libraries
import scalafx.scene.Scene
import scalafx.scene.control.ScrollPane
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.Pane



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
  

//def BiomeMapGenerator : Unit 
  
  //def UpdateMap : Unit
  
  //def  
