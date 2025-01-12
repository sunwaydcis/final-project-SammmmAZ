package BiomeMapApp.controller

import BiomeMapApp.MainApp
import javafx.fxml.FXML
import javafx.scene.layout.{BorderPane, Pane, VBox}
import scalafx.scene.layout.BorderPane as BP
import javafx.fxml.FXMLLoader
import scalafx.scene.control.ScrollPane
import model.BiomeMap
import javafx.application.Platform

class RootController:
  // inititate root border pane
  @FXML private var rootPane : BorderPane = _
  // init each section of rootPane
  private var ribbonController: RibbonBarGameController = _




  def initiate(): Unit =
    val loader = new FXMLLoader(getClass.getResource("/views/fxml/RibbonDisplayGame.fxml"))

//    loader.load()
//    ribbonController = loader.getController[RibbonBarGameController]
//    if ribbonController != null then println("ribbonController is null")
//    // Example usage
//    ribbonController.UpdateLabels(42, 3.14)
    //println(f"rootPane is initialized :${rootPane != null}")
  end initiate

  // define a method to update contents of
  @FXML def StartGame(): Unit =
    // load game components
    // 1 - load the ribbon display for in-game statistics & navigation between pages
    val ribbonLoader : FXMLLoader = new FXMLLoader(getClass.getResource("/views/fxml/RibbonDisplayGame.fxml"))
    // 2 - load the action bar display for Left pane
    val actionBarLoader : FXMLLoader = new FXMLLoader(getClass.getResource("/views/fxml/ActionBar.fxml"))
    // 3 - load the gameMap
    val gameMap : VBox = BiomeMap.ReturnMapWrapper()

    // set the content for each region of the border pane
    rootPane.setTop(ribbonLoader.load[Pane]())
    rootPane.setCenter(gameMap)
    rootPane.setLeft(actionBarLoader.load[Pane]())
    // start the game cycle
    //MainApp.StartGameCycle()
  end StartGame

  def RefreshMapDisplay(): Unit=
    // thread to slow down the game processing
    Thread.sleep(2000)
    // to store hvalue and vvalue
    var vPointer: Double = BiomeMap.gameMap.vvalue.toDouble
    var hPointer: Double = BiomeMap.gameMap.hvalue.toDouble
    val gameMap: VBox = BiomeMap.ReturnMapWrapper()
    //println(f"$vPointer,$hPointer")
    //gameMap.vvalue =  vPointer
    //gameMap.hvalue =  hPointer
    Platform.runLater(
      ()=> rootPane.setCenter(gameMap)
    )
    //vPointer = gameMap.vvalue.toDouble
    //hPointer = gameMap.hvalue.toDouble
    //println(f"$hPointer, $vPointer")
  end RefreshMapDisplay


  def StatsToCenterPane(): Unit =
    Thread.sleep(1000)
    val statsLoader : FXMLLoader = new FXMLLoader(getClass.getResource("/views/fxml/StatisticPage.fxml"))
    rootPane.setCenter(statsLoader.load[Pane]())
  end StatsToCenterPane

  def MapToCenterPane(): Unit =
    Thread.sleep(1000)
    val mapLoader : ScrollPane = BiomeMap.loadBiomeMap
    rootPane.setCenter(mapLoader)
  end MapToCenterPane



end RootController

