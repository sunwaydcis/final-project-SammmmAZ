package model

trait Levels:
  val hasLevels: Boolean // implemented by classes extending levels
  val isUpgradeable : Boolean = true
  var currentLevel : Int = 1
  val upgradePrice : Int 
  
  // define a value to be defined at random
  lazy val maxLevel : Int = if hasLevels then 5 else 0
  
  
  