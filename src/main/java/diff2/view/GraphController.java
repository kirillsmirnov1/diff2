package diff2.view;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.la4j.Vector;

public class GraphController {
    @FXML
    public LineChart lineChart;

    public void addSeries(String seriesName, Vector xSeries, Vector ySeries){
        XYChart.Series series = new XYChart.Series();
        series.setName(seriesName);

        for(int i=0; i < xSeries.length(); ++i){
            series.getData().add(new XYChart.Data(xSeries.get(i), ySeries.get(i)));
        }

        lineChart.getData().add(series);
    }
}
