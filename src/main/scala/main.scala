// main entry point of the program
// initiates the app

import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import model.BiomeMap
import model.BiomeMap.{RunnableUpdateMapData, RunnableUpdateMapView}
import model.Population.{GrowPopulation, growthCounter}
import scalafx.scene.control.ScrollPane
import model.Population
import scalafx.scene.image.Image

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random
import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}
import scala.util.Random
import scala.util.Random


object main extends JFXApp3:

  // define an execution thread to manage thread pool for background tasks
  // allows to run Background operation without blocking UI thread
  // enables application to remain responsive
  implicit val execContext: ExecutionContext = ExecutionContext.global
  
  // define a primary stage as a member 
  var primaryStage : JFXApp3.PrimaryStage = _

  // define the scene outside the functions, make it a member of the main object:
  lazy val mapScene: Scene = 
    new Scene(1280, 720):
      root = BiomeMap.gameMap
    
  

  override  def start(): Unit =
    // main entry point into the app
    BiomeMap.GenerateBiomeMap(mapDataArray = BiomeMap.mapRegion)
    
    var mainGameMap = BiomeMap.gameMap

    // sets the stage
    primaryStage = new JFXApp3.PrimaryStage:
      title = "Civilization Simulation"
      scene = mapScene
      icons.add(new Image(getClass.getResource("/image/tiles/plainsTile.png").toExternalForm))
      maximized = true

    // run the random map update
    ScheduleRandomMapUpdate()
  end start

  // function to call map updates
  def ScheduleRandomMapUpdate(): Unit =
    // select the delay to be between 1 - 5 seconds
    val delay : Int = Random.nextInt(5000) + 1000
    // use  the  future block
    Future{
      // Pause or delay for 1 second atleast
      Thread.sleep(delay)
      // call the grow population function
      GrowPopulation(Population.growthCounter)
      // call the Platform Runlater
      Platform.runLater(BiomeMap.RunnableUpdateMapView())
      println("Map has been updated")
      RefreshStage()
      ScheduleRandomMapUpdate()
    }
  end ScheduleRandomMapUpdate

  private def RefreshStage(): Unit =
    // Obtain HValue and VValue of the scroll pane
    val vPointer : Double = BiomeMap.gameMap.vvalue.toDouble
    val hPointer : Double = BiomeMap.gameMap.hvalue.toDouble
    Platform.runLater(
      // refresh the BiomeMap
      ()  => (BiomeMap.loadBiomeMap)
    )
    
    // assign the scroll values
    
    BiomeMap.gameMap.vvalue = vPointer
    BiomeMap.gameMap.hvalue = hPointer
    Platform.runLater(
      // reloads the BiomeMap Map UI View
      mapScene.root = BiomeMap.loadBiomeMap
    )
    println("Stage has been refreshed")
  end RefreshStage