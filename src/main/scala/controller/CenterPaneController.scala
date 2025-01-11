package controller

// import relevant libraries
import scalafx.application.Platform
import javafx.fxml.FXML
import scalafx.scene.control.Button
import scalafx.stage.Stage
import scalafx.scene.Scene
import javafx.fxml.FXMLLoader
import scalafx.scene.layout.BorderPane

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
    val gameStage = new Stage():
      title = "Game"
    end gameStage
    //presents the game stage
    // Initialize the game components
    val rootController = FXMLLoader().getController[RootController]
    rootController.InitializeGame()
  end OnClickStartGame

  @FXML
  private def OnClickExitButton(): Unit =
    Platform.exit()
  end OnClickExitButton
  