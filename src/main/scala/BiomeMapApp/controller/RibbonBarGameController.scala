package BiomeMapApp.controller

import BiomeMapApp.MainApp
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, ProgressBar}
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import model.Population


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

  // update labels next:
  @FXML
  private var populationLabel : Label = _
  @FXML
  private var moneyLabel : Label = _

  def initialize(): Unit =
    // place Image for ImageView
    populationIcon.setImage( new Image(getClass.getResource("/image/icons/gui_labels/population_label.svg").toExternalForm))
    moneyIcon.setImage(new Image(getClass.getResource("/image/icons/gui_labels/money_label.svg").toExternalForm))
    foodIcon.setImage(new Image(getClass.getResource("/image/icons/gui_labels/food_label.svg").toExternalForm))
    println("Image has been set for Icons")
    populationLabel.setText("15000")
    moneyLabel.setText("50000")

  end initialize
  
  def OnActionToStats(): Unit =
     //calls the button to load map onto centerPane
     MainApp.LoadStatToCenter()
  end OnActionToStats

  def UpdateLabels(): Unit =
    val newTotal : Int = Population.population_total
    populationLabel.setText(newTotal.toString)
    val newMoney : Double = Population.totalMoney
    moneyLabel.setText(newMoney.toString)
  end UpdateLabels