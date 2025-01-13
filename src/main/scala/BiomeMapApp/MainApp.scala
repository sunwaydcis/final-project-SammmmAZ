package BiomeMapApp

import controller.{RibbonBarGameController, RootController}
import javafx.fxml.FXMLLoader
import javafx.scene.Scene as JFXScene
import javafx.scene.control.Alert.AlertType
import javafx.scene.layout.{BorderPane, VBox}
import model.{BiomeMap, Population}
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.image.Image
import javafx.scene.control.{Alert, Label}
import javafx.scene.image.ImageView

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random
import java.net.URL

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
    val delay : Int = 2000
    // use  the  future block

    Future:
      // Pause or delay for 1 second atleast
      Thread.sleep(delay)
      if !this.isPaused then
        if this.backgroundUserCommands.length != 0 then
          for process <- this.backgroundUserCommands do
            process()
            //println(process)
          end for
          this.backgroundUserCommands.clear()

      // call the grow population function
      Population.GrowPopulationByCity(Population.growthCounter)
      if Population.totalFood <= 0 then
        ShowGameOverDialogue()
      //RootCallUpdateGameLabels()
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


  def BuyCity(): Unit =
    println("Buy City Accessed")
    println(f"${BiomeMap.ListOfCity.length}")
    BiomeMap.ListOfCity.length match
      case 1 if Population.totalMoney >= 1500  =>  Population.totalMoney -= 1500; AppendUserInvokedProcess(() => BiomeMap.ActionAddCityToMap())
      case 2 if Population.totalMoney >= 7000 => Population.totalMoney -= 7000; AppendUserInvokedProcess(() => BiomeMap.ActionAddCityToMap())
      case 3 if Population.totalMoney >= 10000 => Population.totalMoney -= 10000; AppendUserInvokedProcess(() => BiomeMap.ActionAddCityToMap())
      case _  => //println("Max City reaced")
  end BuyCity

  def BuyHospitals(): Unit =
    for city <- BiomeMap.ListOfCity do
      AppendUserInvokedProcess(() => city.AddHospital() )
    end for
  end BuyHospitals

  def BuyFarms(): Unit =
    for city <- BiomeMap.ListOfCity do
      AppendUserInvokedProcess(() => city.AddFarm() )
    end for
  end BuyFarms

  def BuyCommercialCenter(): Unit =
    for city <- BiomeMap.ListOfCity do
      AppendUserInvokedProcess(() => city.AddCommercialCenter())
    end for
  end BuyCommercialCenter

  def BuyWaterStations():Unit =
    for city <- BiomeMap.ListOfCity do
      AppendUserInvokedProcess(() => city.AddWaterStation())
    end for
  end BuyWaterStations

  def BuyElectricStations(): Unit =
    for city <- BiomeMap.ListOfCity do
      AppendUserInvokedProcess(() => city.AddElectricStation())
    end for
  end BuyElectricStations

  def BuyUniversities(): Unit =
    for city <- BiomeMap.ListOfCity do
      AppendUserInvokedProcess(() => city.AddUniversity())
    end for
  end BuyUniversities

  def AppendUserInvokedProcess(process: () => Unit): Unit =
    backgroundUserCommands += process
  end AppendUserInvokedProcess

  def BuyFood():Unit=
    AppendUserInvokedProcess( () =>Population.totalMoney -= 1000)
    AppendUserInvokedProcess(() => Population.totalFood += 2000)
  end BuyFood



  // to avoid having more than 4 cities
  def condition1: Boolean=
    if BiomeMap.ListOfCity.length >= 4 then
      true
    else
      false
    end if
  end condition1



  private var isPaused : Boolean= false

  def PauseGame(): Unit =
    AppendUserInvokedProcess( () => isPaused = true)
  end PauseGame

  def ResumeGame(): Unit =
    AppendUserInvokedProcess( () => isPaused = false)
  end ResumeGame

  def ShowEndDialogue(): Unit =
    val alert: Alert = new Alert(AlertType.CONFIRMATION):
      val resource: URL = getClass.getResource("/image/icons/gui_labels/dialogueWin.png")
      initOwner(MainApp.stage)
      setTitle("Congratulations, you have finished the game")
      setHeaderText(" ")
      setGraphic(new ImageView( new Image(resource.toExternalForm)))
      setContentText(f"Statistics| Population Size : ${Population.population_total} | Money : ${Population.totalMoney} | Day : ${Population.growthCounter} days")
    end alert
    alert.showAndWait()
    Platform.exit()
  end ShowEndDialogue

  def ShowHowToDialogue(): Unit =
    val alert: Alert = new Alert(AlertType.INFORMATION)
    val resource: URL = getClass.getResource("/image/icons/gui_labels/simulation_logo.png")
      // load resource into Image View
    alert.setGraphic(new ImageView(new Image(resource.toExternalForm)))
    alert.setTitle("How to play Civilization Management - by Yusuf Arman")

    val row_1 : Label = new Label("1. Reach 50000 population")
    val row_2 : Label = new Label("2. Buildings can affect population behavior")
    val row_3 : Label = new Label("3. Game ends if food is non-existent (Hint: 0)")
    val row_4 : Label = new Label("4. Lastly, don't forget to enjoy")
    alert.initOwner(MainApp.stage)
    val box : VBox = new VBox()
    box.getChildren.addAll(row_1,row_2,row_3,row_4)
    alert.getDialogPane.setContent(box)
    alert.setHeaderText("Follow the below")
    alert.setContentText(null)
    alert.showAndWait()
  end ShowHowToDialogue

  def ShowGameOverDialogue(): Unit =
    val alert: Alert = new Alert(AlertType.CONFIRMATION):
      val resource: URL = getClass.getResource("/image/icons/gui_labels/dialogueLooseLogo.png")
      initOwner(MainApp.stage)
      setTitle("You Lost")
      setHeaderText("Population run out of food")
      setGraphic(new ImageView(new Image(resource.toExternalForm)))
      setContentText(f"Last know details| Population Size : ${Population.population_total} | Money : ${Population.totalMoney} | Day : ${Population.growthCounter} days")
    alert.showAndWait()
    Platform.exit()
  end ShowGameOverDialogue
end MainApp


