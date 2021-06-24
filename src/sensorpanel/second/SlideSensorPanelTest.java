package sensorpanel.second;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class SlideSensorPanelTest extends Application
{
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{

        BorderPane pane = new BorderPane();
        
        SensorPanel sliderPanel = new SensorPanel();
        
        sliderPanel.setPrefWidth(150);
        sliderPanel.setPrefHeight(80);
        pane.setCenter(sliderPanel);
//        pane.setRight(test);

        Scene scene = new Scene(pane);

        primaryStage.setTitle("Sensor Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
		
	}
	
	public static void main(String[] args) {
        Application.launch(args);
    }

}
