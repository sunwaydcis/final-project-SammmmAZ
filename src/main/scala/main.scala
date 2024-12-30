// main entry point of the program
// initiates the app

import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import model.BiomeMap
import model.BiomeMap.{updateMapData, updateMapView}
import model.Population.{GrowPopulation, growthCounter}
import scalafx.scene.control.ScrollPane

// import scala libraries for UI updates and batch updates:
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.Random

import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}
import scala.util.Random
import scala.util.Random


object main extends JFXApp3:

  // define an execution thread to manage thread pool for background tasks
  // allows to run Background operation without blocking UI thread
  // enables application to remain responsive
  implicit val execContext: ExecutionContext = ExecutionContext.global

  override  def start(): Unit =
    // main entry point into the app
    BiomeMap.GenerateBiomeMap(mapDataArray = BiomeMap.mapRegion)

    // define Map
    var gameMap : ScrollPane = BiomeMap.loadBiomeMap


    // sets the stage
    stage = new JFXApp3.PrimaryStage:
      title = "Biome Map Game"
      scene = new Scene(1280,720):
        root = gameMap


    // run the random map update
    scheduleRandomMapUpdate()
  end start

  // function to call map updates
  def ScheduleRandomMapUpdate(): Unit =
    // select the delay to be between 1 - 5 seconds
    val delay : Int = Random.nextInt(5000) + 1000

    // use  the  future block
    Future{
      // Pause or delay for 1 second atleast
      Thread.sleep(delay)

    }

