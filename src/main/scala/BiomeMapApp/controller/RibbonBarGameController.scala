package BiomeMapApp.controller

import javafx.fxml.FXML
import javafx.scene.control.{Button, ProgressBar}
import javafx.scene.image.Image
import javafx.scene.image.ImageView

import scala.language.postfixOps

class RibbonBarGameController:
  @FXML private var populationIcon : ImageView = _
  @FXML private var moneyIcon : ImageView = _
  @FXML private var foodIcon : ImageView = _
  // get labels for Population, Money , Food
  @FXML 
  private var  buttonToMap : Button = _
  @FXML 
  private var buttonToStats : Button = _
  @FXML
  private var progressBar : ProgressBar = _ 

  def initialize(): Unit =
    // place Image for ImageView
    populationIcon.setImage( new Image(getClass.getResource("/image/icons/gui_labels/population_label.svg").toExternalForm))
    moneyIcon.setImage(new Image(getClass.getResource("/image/icons/gui_labels/money_label.svg").toExternalForm))
    foodIcon.setImage(new Image(getClass.getResource("/image/icons/gui_labels/food_label.svg").toExternalForm))
    println("Image has been set for Icons")
  end initialize
  
  //def OnActionToMap(): Unit = calls the button to load map onto centerPane
  // def OnActionToStats(): Unit = calls the button to load statistics to CenterPane
  
  
  
  
  
  

