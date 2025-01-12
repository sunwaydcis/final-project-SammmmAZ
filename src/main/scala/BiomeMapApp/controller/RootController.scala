package BiomeMapApp.controller

import BiomeMapApp.MainApp
import scalafx.application.JFXApp3
import javafx.fxml.FXML
import scalafx.scene.Scene
import scalafx.scene.image.Image
import javafx.scene.layout.BorderPane
import scalafx.scene.layout.BorderPane as BP
import javafx.fxml.FXMLLoader
import scalafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import model.BiomeMap

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


end RootController

