package control.universaldisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import control.universaldisplay.UniversalDisplay.Command;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SingleUniversalDisplay extends Application 
{
	public static final int TEMPERATURE = 0;
	
	public static final int HUMIDITY = 1;
	public static final int BRIGHTNESS = 2;
	
	//simulations map; list index 0 = istwert, index 1 = setpoint wenn vorhanden
	private HashMap<Integer, List<SensorValue>> sensorMap = new HashMap<Integer, List<SensorValue>>();
	
	
	/**
	 * helper class to store and change values
	 * @author m.goerlich
	 *
	 */
	public class SensorValue
	{
		private double von;
		
		private double bis;
		
		private double currentValue;
		
		private String measurementUnit;
		
		public SensorValue(double currentValue, double von, double bis, String measurementUnit)
		{
			this.currentValue = currentValue;
			this.von = von;
			this.bis = bis;
			this.measurementUnit = measurementUnit;
		}

		public double getVon() {
			return von;
		}

		public void setVon(double von) {
			this.von = von;
		}

		public double getBis() {
			return bis;
		}

		public void setBis(double bis) {
			this.bis = bis;
		}

		public double getCurrentValue() {
			return currentValue;
		}

		public void setCurrentValue(double currentValue) {
			this.currentValue = currentValue;
		}

		public String getMeasurementUnit() {
			return measurementUnit;
		}

		public void setMeasurementUnit(String measurementUnit) {
			this.measurementUnit = measurementUnit;
		}
	}
	
	

	
	
	
	@Override
	public void start(Stage stage) 
	{
		List<SensorValue> sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(25, 0, 40, "°C"));
		sensorList.add(new SensorValue(24, 8, 40, "°C"));
		sensorMap.put(TEMPERATURE, sensorList);
		
		sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(75, 0, 100, "%"));
		sensorMap.put(HUMIDITY, sensorList);
		
		sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(1750, 0, 3000, "Lux"));
		sensorMap.put(BRIGHTNESS, sensorList);
		
		
		 BorderPane pane = new BorderPane();
	      
		 //center of borderpane, initialize
		 UniversalDisplay tempControl = new UniversalDisplay(3);
		 
		 
		 pane.setCenter(tempControl);
	     
		 Label kommandoLabel = new Label();
		 
		 tempControl.getCommandProperty().addListener(new ChangeListener<Command>(){

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) 
			{
				if(newValue != Command.RESET_COMMAND)
				{
					kommandoLabel.setText(newValue.toString());
					tempControl.getCommandProperty().set(Command.RESET_COMMAND);
				}
				
				
			}});
		 VBox commandArea = new VBox(2);
	     commandArea.setPadding(new Insets(5, 5, 5, 5));
	     commandArea.getChildren().addAll(kommandoLabel);
	     pane.setBottom(commandArea);
	     
	     
	     VBox vBox = new VBox(2);
	     vBox.setPadding(new Insets(5, 5,0,0));
	     
	     ToggleButton test = new ToggleButton("Start");
	     test.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent event) 
				{
					if(test.isSelected())
					{
						
						//tempControl.startAnimation();
						test.setText("Ende");
					}
					else
					{
						//tempControl.stopAnimation();
						test.setText("Start");
					}
				
				}
	        	
	        });
	    vBox.getChildren().add(test);
	        
	     
	     
	    pane.setRight(vBox);
	     
	        
	     
	     
	     
	     
	     
		 
	     Scene scene = new Scene(pane);
	    
	     stage.setTitle("JavaFX Universelles Display");
	     stage.setScene(scene);
	     stage.setWidth(400);
	     stage.setHeight(400);
	        
	     stage.show();
	        
	        //closing event with stop animation
	     stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
	     {
	            @Override
	            public void handle(WindowEvent event) {
	            	//customCircle.stopAnimation();
	            }
	     });
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
