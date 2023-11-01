package charts;

import java.util.Arrays; 
import javafx.application.Application; 
import javafx.collections.FXCollections; 
import javafx.scene.Group; 
import javafx.scene.Scene; 
import javafx.scene.chart.CategoryAxis; 
import javafx.stage.Stage; 
import javafx.scene.chart.NumberAxis; 
import javafx.scene.chart.StackedBarChart; 
import javafx.scene.chart.XYChart; 
         
public class StackedBarChartExample extends Application { 
   @Override 
   public void start(Stage stage) {     
      //Defining the axes               
      CategoryAxis xAxis = new CategoryAxis();    
      xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList
         ("Window"))); 
      
      xAxis.setLabel("category");
      NumberAxis yAxis = new NumberAxis(); 
      yAxis.setLabel("Population (In millions)");    
         
      //Creating the Bar chart 
      StackedBarChart<String, Number> stackedBarChart = 
         new StackedBarChart<>(xAxis, yAxis);         
      stackedBarChart.setTitle("Historic World Population by Region"); 
         
      //Prepare XYChart.Series objects by setting data 
      XYChart.Series<String, Number> series1 = new XYChart.Series<>();  
      series1.setName("Window"); 
      series1.getData().add(new XYChart.Data<>("Opened", 51000)); 
      series1.getData().add(new XYChart.Data<>("Closed", 1500));  
      series1.getData().add(new XYChart.Data<>("Tilted", 4800)); 

         
      //Setting the data to bar chart
      stackedBarChart.getData().addAll(series1); 
         
      //Creating a Group object  
      Group root = new Group(stackedBarChart); 
         
      //Creating a scene object 
      Scene scene = new Scene(root, 600, 400);  
      
      //Setting title to the Stage 
      stage.setTitle("stackedBarChart"); 
         
      //Adding scene to the stage 
      stage.setScene(scene); 
         
      //Displaying the contents of the stage 
      stage.show();         
   } 
   public static void main(String args[]){ 
      launch(args); 
   } 
}