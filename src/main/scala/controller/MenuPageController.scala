package controller

import scalafx.application.Platform
import javafx.fxml.FXML
import scalafx.scene.control.Button
import scalafx.stage.Stage

class MenuPageController:
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
    gameStage.show()
  end OnClickStartGame

  @FXML
  private def OnClickExitButton(): Unit =
    Platform.exit()
  end OnClickExitButton
  