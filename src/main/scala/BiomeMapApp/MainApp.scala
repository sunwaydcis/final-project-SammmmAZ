package BiomeMapApp

import BiomeMapApp.MainApp.getClass
import controller.{CenterPaneController, RibbonBarGameController, RootController}
import javafx.fxml.FXMLLoader
import javafx.scene.Scene as JFXScene
import javafx.scene.control.{ScrollPane, SplitPane}
import javafx.scene.layout.BorderPane
import model.BiomeMap.{RunnableUpdateMapData, RunnableUpdateMapView}
import model.{BiomeMap, Population}
import model.Population.{GrowPopulation, growthCounter}
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.image.Image

import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}
import scala.concurrent
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

object MainApp extends JFXApp3:

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

  // load root fxml file
  lazy val rootLoader: FXMLLoader = new FXMLLoader(getClass.getResource("/views/fxml/root.fxml"))
  //lazy val ribbonLoader : FXMLLoader = new FXMLLoader(getClass.getResource("/views/fxml/RibbonDisplayGame.fxml"))

  override  def start(): Unit =
    // main entry point into the app
    BiomeMap.GenerateBiomeMap(mapDataArray = BiomeMap.mapRegion)
    var mainGameMap = BiomeMap.gameMap
    //---------
    //---------
    try
      // set controller for rootLoader
      val rootPane: BorderPane = rootLoader.load().asInstanceOf[BorderPane]
      //
      // set the primary stage
      primaryStage = new JFXApp3.PrimaryStage:
        title = "Civilization Simulation - Alpha Testing"
        scene = new Scene (new JFXScene(rootPane,1280,720))
        icons.add(new Image(getClass.getResource("/image/tiles/plainsTile.png").toExternalForm))
        maximized = true
    catch
      case e: Exception => e.printStackTrace()
  end start

  // function to call map updates
  private def ScheduleRandomMapUpdate(): Unit =
    // select the delay to be between 1 - 5 seconds
    val delay : Int = Random.nextInt(5000) + 1000
    // use  the  future block

    Future:
      // Pause or delay for 1 second atleast
      Thread.sleep(delay)
      // call the grow population function
      Population.GrowPopulationByCity(Population.growthCounter)
      //UpdateRibbonGameLabels()
      // call the Platform Runlater
      Platform.runLater(BiomeMap.RunnableUpdateMapView())
      //println(f"Map updated times: ${Population.growthCounter}")
      RefreshGamePane()
      ScheduleRandomMapUpdate()
  end ScheduleRandomMapUpdate

  // define a function to start game cycle
  def StartGameCycle(): Unit =
    ScheduleRandomMapUpdate()
  end StartGameCycle


//  private def RefreshStage(): Unit =
//    // Obtain HValue and VValue of the scroll pane
//    val vPointer : Double = BiomeMap.gameMap.vvalue.toDouble
//    val hPointer : Double = BiomeMap.gameMap.hvalue.toDouble
//    Platform.runLater(
//      // refresh the BiomeMap
//      ()  => (BiomeMap.loadBiomeMap)
//    )
//    // assign the scroll values
//
//    BiomeMap.gameMap.vvalue = vPointer
//    BiomeMap.gameMap.hvalue = hPointer
//    RefreshGamePane()
//  end RefreshStage

  def TransitionToGame(): Unit=
    val rootController: RootController = rootLoader.getController[controller.RootController]()
    if rootController != null then println("Rootcontroller is loaded")
    rootController.StartGame() // check
    MainApp.StartGameCycle()
  end TransitionToGame

  def RefreshGamePane(): Unit=
    val rootController: RootController = rootLoader.getController[RootController]()
    rootController.RefreshMapDisplay()
  end RefreshGamePane

  // invoked by the RibbonBarGameController Buttons
  def LoadGameToCenter(): Unit=
    val rootController: RootController = rootLoader.getController[RootController]()
    //rootController.

  def LoadStatToCenter(): Unit =
    val rootController: RootController = rootLoader.getController[RootController]()
    rootController.StatsToCenterPane()
  end LoadStatToCenter

  def UpdateRibbonGameLabels(): Unit =
    val ribbonController : RibbonBarGameController = new FXMLLoader(getClass.getResource("/views/fxml/RibbonDisplayGame.fxml")).getController[RibbonBarGameController]()
    if ribbonController != null then println("Ribbon Loader is loaded")
    ribbonController.UpdateLabels()
  end UpdateRibbonGameLabels