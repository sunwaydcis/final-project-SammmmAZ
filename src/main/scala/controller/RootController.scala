package controller

import scalafx.application.JFXApp3
import javafx.fxml.FXML
import scalafx.scene.Scene
import scalafx.scene.image.Image
import scalafx.scene.layout.BorderPane

class RootController extends JFXApp3.PrimaryStage:

  // inititate root border pane
  @FXML
  private var rootPane : BorderPane = _
  
  override def start(): Unit=
    // starts the game
    title = "Civilization Simulation - Alpha Testing"
    // loads scene into root pane
    scene = new Scene:
      root = rootPane
      // add an icon
    icons.add(new Image(getClass.getResource("/image/tiles/plainsTile.png").toExternalForm))
    // enable maximized
    maximized = true
  end start
  
  // define a method to update contents of 
end RootController

