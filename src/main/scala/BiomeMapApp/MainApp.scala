package BiomeMapApp

import BiomeMapApp.MainApp.getClass
import controller.{RibbonBarGameController, RootController}
import javafx.fxml.FXMLLoader
import javafx.scene.Scene as JFXScene
import javafx.scene.control.{ScrollPane, SplitPane}
import javafx.scene.layout.BorderPane
import model.BiomeMap.{AddMapToCity, RunnableUpdateMapView}
import model.{BiomeMap, Population}
import model.Population.{growthCounter}
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.image.Image

import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}
import scala.collection.mutable.ListBuffer
import scala.concurrent
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

import model.{Hospital,Farm,CommercialCenter}

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

  lazy val ribbonLoader : FXMLLoader =  new FXMLLoader(getClass.getResource("/views/fxml/RibbonDisplayGame.fxml"))
  val ribbonController : RibbonBarGameController = ribbonLoader.getController[RibbonBarGameController]()

  override  def start(): Unit =
    // main entry point into the app
    BiomeMap.GenerateBiomeMap(mapDataArray = BiomeMap.mapRegion)
    var mainGameMap = BiomeMap.gameMap
    //---------
    //---------
    try
      // set controller for rootLoader
      val rootPane: BorderPane = rootLoader.load().asInstanceOf[BorderPane]
      // set the primary stage
      primaryStage = new JFXApp3.PrimaryStage:
        title = "Civilization Simulation - Alpha Testing"
        scene = new Scene (new JFXScene(rootPane,1280,720))
        icons.add(new Image(getClass.getResource("/image/tiles/plainsTile.png").toExternalForm))
        maximized = true
    catch
      case e: Exception => e.printStackTrace()
  end start

  private var backgroundUserCommands: ListBuffer[() => Unit] = ListBuffer()

  // function to call map updates
  private def ScheduleRandomMapUpdate(): Unit =
    // select the delay to be between 1 - 5 seconds
    val delay : Int = Random.nextInt(5000) + 1000
    // use  the  future block

    Future:
      // Pause or delay for 1 second atleast
      Thread.sleep(delay)
      if this.backgroundUserCommands.length != 0 then
        for process <- this.backgroundUserCommands do
          process()
          println(process)
        end for
        this.backgroundUserCommands.clear()
      else
        println("Nothing to code")
      end if

      // call the grow population function
      Population.GrowPopulationByCity(Population.growthCounter)
      RootCallUpdateGameLabels()
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

  def TransitionToGame(): Unit=
    val rootController: RootController = rootLoader.getController[controller.RootController]()
    //if rootController != null then println("Rootcontroller is loaded")
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
    rootController.MapToCenterPane()
  end LoadGameToCenter


  def LoadStatToCenter(): Unit =
    val rootController: RootController = rootLoader.getController[RootController]()
    rootController.StatsToCenterPane()
  end LoadStatToCenter

  def RootCallUpdateGameLabels(): Unit =
    val rootController: RootController = rootLoader.getController[RootController]()
    println(f"$rootController")
    //rootController.UpdateRibbonLabels(population = Population.population_total, money = Population.totalMoney)
  end RootCallUpdateGameLabels

  def BuyCity(): Unit =
    println("Buy City Accessed")
    println(f"${BiomeMap.ListOfCity.length}")
    BiomeMap.ListOfCity.length match
      case 1 if Population.totalMoney >= 500  =>  Population.totalMoney -= 500; AppendUserInvokedProcess(() => BiomeMap.ActionAddCityToMap())
      case 2 if Population.totalMoney >= 1000 => Population.totalMoney -= 1000; AppendUserInvokedProcess(() => BiomeMap.ActionAddCityToMap())
      case 3 if Population.totalMoney >= 2000 => Population.totalMoney -= 2000; AppendUserInvokedProcess(() => BiomeMap.ActionAddCityToMap())
      case _  => println("Max City reaced")
  end BuyCity

  def BuyHospitals(): Unit =
    for city <- BiomeMap.ListOfCity do
      AppendUserInvokedProcess(() => city.AddHospital())
    end for
  
    

  def AppendUserInvokedProcess(process: () => Unit): Unit =
    backgroundUserCommands += process
  end AppendUserInvokedProcess

  def condition1(): Boolean=
    if BiomeMap.ListOfCity.length == 4 then
      true
    else
      false
    end if
  end condition1
  
  def condition2(): Boolean=
    var hospCount : Int = 0
    for city <- BiomeMap.ListOfCity do 
      hospCount += city.hospitalCount
    end for
    
    if hospCount == 4 then
      // needs 4 hospital to win
      true
      else 
        false
  end condition2
  
  def BuyFarms(): Unit =
    for city <- BiomeMap.ListOfCity do 
      AppendUserInvokedProcess( () => city.AddFarm())
  end BuyFarms

  def Condition3(): Boolean =
    var farmCount: Int = 0
    for city <- BiomeMap.ListOfCity do 
      farmCount += city.farmCount 
    end for

    if farmCount >= 2 then
      true
    else
      false
  end Condition3
  
  def BuyCommercialCenter(): Unit =
    for city <- BiomeMap.ListOfCity do 
      AppendUserInvokedProcess( () => city.AddCommercialCenter())
    end for
  end BuyCommercialCenter
  
  def condition4(): Boolean =
    var comCC : Int = 0
    for city <- BiomeMap.ListOfCity do 
      comCC += city.commercialCenterCount
    end for
    
    if comCC >= 2 then
      true
    else
      false
  end condition4
  
  
  
      
  



