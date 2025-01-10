package controller

// import relevant classes'
import javafx.fxml.{FXML, FXMLLoader}
import model.BiomeMap
import javafx.scene.control.ScrollPane

// define the controller instances
class MapPanelController:
  
  // define variable from the FXML file
  @FXML var MapScrollPane : ScrollPane = _
  
  @FXML def initialize() : Unit =
    // loads map from BiomeMap
    val BiomeMapScrollPane : ScrollPane = BiomeMap.loadBiomeMap
    // set the content
    MapScrollPane.setContent(BiomeMapScrollPane.getContent)
  end initialize
  
  
  
