package BiomeMapApp.controller

import BiomeMapApp.MainApp
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane
import scalafx.scene.layout.BorderPane as BP
import javafx.fxml.FXMLLoader
import scalafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import model.BiomeMap
import javafx.application.Platform

class RootController:
  // inititate root border pane
  @FXML private var rootPane : BorderPane = _
  // init each section of rootPane


    //println(f"rootPane is initialized :${rootPane != null}")



  // define a method to update contents of
  def LoadGame(): Unit =
    // load game components
    // 1 - load the ribbon display for in-game statistics & navigation between pages
    val ribbonLoader : FXMLLoader = new FXMLLoader(getClass.getResource("/views/fxml/RibbonDisplayGame.fxml"))
    // 2 - load the action bar display for Left pane
    val actionBarLoader : FXMLLoader = new FXMLLoader(getClass.getResource("/views/fxml/ActionBar.fxml"))
    // 3 - load the gameMap
    val gameMap : ScrollPane = BiomeMap.loadBiomeMap

    // set the content for each region of the border pane
    rootPane.setTop(ribbonLoader.load[Pane]())
    rootPane.setCenter(gameMap)
    // start the game cycle
    //MainApp.StartGameCycle()
  end LoadGame

  def RefreshMapDisplay(): Unit=
    // thread to slow down the game processing
    Thread.sleep(2000)
    // to store hvalue and vvalue
    var vPointer: Double = BiomeMap.gameMap.vvalue.toDouble
    var hPointer: Double = BiomeMap.gameMap.hvalue.toDouble
    val gameMap : ScrollPane = BiomeMap.loadBiomeMap
    //println(f"$vPointer,$hPointer")
    gameMap.vvalue =  vPointer
    gameMap.hvalue =  hPointer
    Platform.runLater(
      ()=> rootPane.setCenter(gameMap)
    )
    vPointer = gameMap.vvalue.toDouble
    hPointer = gameMap.hvalue.toDouble
    //println(f"$hPointer, $vPointer")
  end RefreshMapDisplay
end RootController

