package BiomeMapApp.controller

import javafx.fxml.FXML
import javafx.scene.chart.{LineChart, XYChart}
import model.Population


class StatisticPageController:
  @FXML
  private var lineChart : LineChart[String,Int] = _

  def initialize(): Unit =
    LoadDatatoChart()
  end initialize


  def LoadDatatoChart(): Unit =
    val chart: XYChart.Series[String, Int] = new XYChart.Series[String,Int]()
    chart.setName("Population Growth Over time")

    for (x <- Population.timeData.indices) do
      val xAxis : String = Population.timeData(x).toString
      val yAxis : Int = Population.populationData(x)
      chart.getData.add(new XYChart.Data[String, Int](xAxis, yAxis))
    end for

    lineChart.getData.add(chart)
  end LoadDatatoChart






end StatisticPageController

