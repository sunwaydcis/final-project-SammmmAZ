package BiomeMapApp.controller

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
  
end ActionBarController
