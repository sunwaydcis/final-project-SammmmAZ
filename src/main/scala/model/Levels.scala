package model

trait Levels:
  var hasLevels: Boolean // implemented by classes extending levels
  var isUpgradeable : Boolean = true
  var currentLevel : Int = 1
  var upgradePrice: Int 
  
  // define a value to be defined at random
  lazy val maxLevel : Int = if hasLevels then 5 else 0
  
  def Upgrade(): Unit=
    if isUpgradeable && currentLevel != maxLevel then
      currentLevel += 1
      else if (!hasLevels) then
        None
      else if currentLevel == maxLevel then
        isUpgradeable = false
        else 
          None
    end if
  end Upgrade
  
        
  