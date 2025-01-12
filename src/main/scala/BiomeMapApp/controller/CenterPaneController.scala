package BiomeMapApp.controller

// import relevant libraries
import BiomeMapApp.MainApp.TransitionToGame
import scalafx.application.Platform
import javafx.fxml.FXML
import scalafx.scene.control.Button

class CenterPaneController:
  // load fxml element from file

  // init start button
  @FXML
  private var buttonStart : Button = _

  // init option button
  @FXML
  private var optionButton : Button = _
  // init exit button
  @FXML
  private var exitButton : Button = _
  

  // handle button events
  @FXML
  private def OnClickStartGame(): Unit =
    // loads the game stage to initiate the game cycle
    //presents the game stage
    // Initialize the game components
    TransitionToGame()
  end OnClickStartGame

  @FXML
  private def OnClickExitButton(): Unit =
    Platform.exit()
  end OnClickExitButton
  