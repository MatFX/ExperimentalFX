package charts;

import java.util.Random;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

//... Import statements same as above

public class Main extends Application {

public static void main(String[] args) {
   launch(args);
}

@Override
public void start(Stage stage) throws Exception {
   stage.setTitle("JavaFX Chart Demo");
   StackPane pane = new StackPane();
   pane.getChildren().add(createStackedBarChart());
   stage.setScene(new Scene(pane, 400, 200));
   stage.show();
}



public XYChart<CategoryAxis, NumberAxis>
      createStackedBarChart() {
   CategoryAxis xAxis = new CategoryAxis();
   NumberAxis yAxis = new NumberAxis();
   StackedBarChart sbc = new StackedBarChart<>(xAxis, yAxis);
   sbc.setData(getDummyChartData());
   sbc.setTitle("Stacked bar chart on random data");
   return sbc;
}

public static int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
}

	public ObservableList<XYChart.Series<String, Long>> getDummyChartData() {
	ObservableList<XYChart.Series<String, Long>> data =
	FXCollections.observableArrayList();
	Series<String, Long> as = new Series<>();
	Series<String, Long> bs = new Series<>();
	Series<String, Long> cs = new Series<>();
	as.setName("OPENED");
	bs.setName("CLOSED");
	cs.setName("TILTED");
	
	Random r = new Random();
	
	long startTime = System.currentTimeMillis();
	for (int i = 0; i < 2; i++) 
	{
		startTime = startTime + Main.getRandomNumber(1000, 1000*60*60);
		as.getData().add(new XYChart.Data<>("FENSTER", startTime));
		startTime = startTime + Main.getRandomNumber(1000, 1000*60*60);
		bs.getData().add(new XYChart.Data<>("FENSTER", startTime));
		startTime = startTime + Main.getRandomNumber(1000, 1000*60*60);
		cs.getData().add(new XYChart.Data<>("FENSTER", startTime));
	}
	data.addAll(as, bs, cs);
	return data;
	}
}