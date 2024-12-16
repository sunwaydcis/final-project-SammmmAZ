package model

// interact with 3rd party library ZIO
// for multithreading purposes

trait DynamicElement :
  if self.isDynamic == true then
    // calls on object/ class objects to compile their background processes:
    //  var guiProcessList : List[() => Unit]
    