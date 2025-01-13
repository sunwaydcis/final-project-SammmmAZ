package BiomeMapApp.controller

import BiomeMapApp.MainApp
import javafx.fxml.FXML
import javafx.scene.control.{Button, ProgressBar}
import scalafx.application.Platform
import scalafx.scene.control.Dialog
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
  var addCityButton : Button = _
  @FXML
  var buyFarmButton : Button = _
  @FXML
  var buyHospitalButton : Button = _
  @FXML
  private var buyCommercialCenterButton : Button = _
  @FXML
  var quitButton : Button = _
  @FXML
  private var buyWsButton : Button = _
  @FXML
  private var buyEsButton : Button = _
  @FXML
  var buyUniButton : Button = _
  @FXML
  var foodButton : Button = _


  var waterStationPurchased: Boolean = false
  var electricStationPurchased : Boolean = false

  def OnActionBuyWaterStation(): Unit=
    MainApp.BuyWaterStations()
    waterStationPurchased = true
  end OnActionBuyWaterStation

  def OnActionBuyFood(): Unit =
    MainApp.BuyFood()
  end OnActionBuyFood

  def OnActionBuyElectricStation(): Unit =
    MainApp.BuyElectricStations()
    electricStationPurchased = true
  end OnActionBuyElectricStation

  def OnActionAddCity(): Unit =
    MainApp.BuyCity()
    Condition1()
  end OnActionAddCity

  private def Condition1(): Unit =
    if MainApp.condition1 then
      addCityButton.setVisible(false)
      addCityButton.setManaged(false)
    end if
  end Condition1

  def OnActionBuyUni(): Unit=
    MainApp.BuyUniversities()
  end OnActionBuyUni


  def OnActionQuitButton():Unit=
    Platform.exit()
  end OnActionQuitButton

  def OnActionAddHospital(): Unit =
    MainApp.BuyHospitals()
  end OnActionAddHospital



  def OnActionAddFarm(): Unit =
    MainApp.BuyFarms()
  end OnActionAddFarm


  def OnActionBuyCommercialCenter(): Unit =
    MainApp.BuyCommercialCenter()
  end OnActionBuyCommercialCenter


end ActionBarController
