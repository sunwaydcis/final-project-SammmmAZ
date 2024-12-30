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

  def scheduleRandomMapUpdate(): Unit = {
    // Random delay between updates (e.g., between 1 and 5 seconds)
    val delay = Random.nextInt(5000) + 1000 // delay in milliseconds, between 1000ms and 6000ms

    // Schedule the update to run after the random delay
    scheduler.schedule(new Runnable {
      override def run(): Unit = {
        // Simulate population growth or map update logic
        GrowPopulation(growthCounter)

        // Call the map update
        Platform.runLater {
          updateMapData(BiomeMap.mapRegion, BiomeMap.cityTiles)
          updateMapView()
        }
        println("Updated Map Once")
        // Recursively schedule the next update with a new random delay
        scheduleRandomMapUpdate()
      }
    }, delay, TimeUnit.MILLISECONDS)
  }

