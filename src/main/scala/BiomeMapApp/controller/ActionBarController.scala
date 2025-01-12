package BiomeMapApp.controller

import BiomeMapApp.MainApp
import javafx.fxml.FXML
import javafx.scene.control.{Button, ProgressBar}
import scalafx.application.Platform
// doesn't require a model object 
// invokes methods from Building class and BiomeMap class 
// ActionBarController class

// Binds events invoked from action bar:
// AddBuildingToMap --> Build Button Click + Drag/ Drop building icon on map
// DeleteBuilding --> Destroy Button Click + Select Buildings on Map

// Event Sequence for Build :
// -> User Click Build Button
// -- Column/ Combo Box Collapse
// -> User scrolls down the panel to find prefered building to add to the map
// -> User will drag building icon to the Map to place it
// -- the addBuilding will invoke IsValidToBuild to check if building is placed in valid tile :
//    conditions: -> Plains tile 
//                -> within 10 / 7 / 5 tile to an OccupiedPlainsTile
//                -> UserHaveEnough money
// -- if Building is added, do the following:
//                -> deduct money --> Add update money value immediately to GuiDisplayUpdate
//                -> Add Population Happiness --> +2 :: Update Population Object
//                -> Increase Money Generation Potential :: Update Population Object
//                -> Log Player action on Log Bar with Green fonts
// -- else, will not allow user to complete the BuildAction
//                -> Display toast on below

class ActionBarController :
  // bind fxml id to variables
  // ensure fxml file is connected to ActionBarController class via fx:controller
  @FXML
  var progressBar : ProgressBar = _
  @FXML
  var addCityButton : Button = _
  @FXML
  var buyFarmButton : Button = _
  @FXML
  var buyHospitalButton : Button = _
  @FXML
  var buyCommercialCenterButton : Button = _
  @FXML
  var quitButton : Button = _

  def initialize(): Unit =
    progressBar.setProgress(0.1)
  end initialize


  def OnActionAddCity(): Unit =
    MainApp.BuyCity()
    //println("Command Executed")
    Condition1()
  end OnActionAddCity

  def Condition1(): Unit =
    if (MainApp.condition1()) then
      addCityButton.setVisible(false)
      addCityButton.setManaged(false)
      progressBar.setProgress(0.20)
    end if
  end Condition1

  def OnActionQuitButton():Unit=
    Platform.exit()
  end OnActionQuitButton

  def OnActionAddHospital(): Unit =
    MainApp.BuyHospitals()
    Condition2()
  end OnActionAddHospital

  def Condition2(): Unit =
    if (MainApp.condition2()) then
      buyHospitalButton.setManaged(false)
      buyHospitalButton.setManaged(false)
      var cProgress : Double = progressBar.getProgress + 0.20
      progressBar.setProgress(cProgress)
  end Condition2
  
  def OnActionAddFarm(): Unit =
    MainApp.BuyFarms()
    Condition3()
  end OnActionAddFarm
  
  def Condition3(): Unit =
    if MainApp.Condition3() then
      buyFarmButton.setManaged(false)
      buyFarmButton.setVisible(false)
      var cProgress: Double = progressBar.getProgress + 0.20
      progressBar.setProgress(cProgress)
  end Condition3
  
  def OnActionBuyCommercialCenter(): Unit =
    MainApp.BuyCommercialCenter()
    Condition4()
  end OnActionBuyCommercialCenter
  
  def Condition4(): Unit =
    if MainApp.condition4() then
      buyCommercialCenterButton.setManaged(false)
      buyCommercialCenterButton.setVisible(false)
      var cProgress: Double = progressBar.getProgress + 0.20
      progressBar.setProgress(cProgress)
  end Condition4
  


end ActionBarController
