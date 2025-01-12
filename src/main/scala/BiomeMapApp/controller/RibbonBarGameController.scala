package BiomeMapApp.controller

import BiomeMapApp.MainApp
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, ProgressBar}
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import model.Population

import scala.language.postfixOps

class RibbonBarGameController:
  @FXML
  private var  buttonToMap : Button = _
  @FXML 
  private var buttonToStats : Button = _

  @FXML
  def OnActionToStats(): Unit =
     //calls the button to load map onto centerPane
     MainApp.LoadStatToCenter()
  end OnActionToStats

  @FXML
  def OnActionToMaps(): Unit =
    MainApp.LoadGameToCenter()
  end OnActionToMaps
