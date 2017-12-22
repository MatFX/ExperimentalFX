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
	
	
	

	
	
	
	@Override
	public void start(Stage stage) 
	{
		List<SensorValue> sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(25, 0, 40, "°C"));
		sensorList.add(new SensorValue(22.5, 8, 40, "°C"));
		sensorMap.put(TEMPERATURE, sensorList);
		
		sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(75, 0, 100, "%"));
		sensorMap.put(HUMIDITY, sensorList);
		
		sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(2500, 0, 3000, "Lux"));
		sensorMap.put(BRIGHTNESS, sensorList);
		
		
		 BorderPane pane = new BorderPane();
	      
		 //center of borderpane, initialize
		 UniversalDisplay uniDisplay = new UniversalDisplay(3);
		 
		 
		 pane.setCenter(uniDisplay);
	     
		 Label kommandoLabel = new Label();
		 
		 uniDisplay.getCommandProperty().addListener(new ChangeListener<Command>(){

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) 
			{
				int indexView;
				if(newValue != Command.RESET_COMMAND)
				{
					switch(newValue)
					{
						case PREVIOUS_SENSOR_VALUE:
						case NEXT_SENSOR_VALUE:
							indexView= uniDisplay.getIndexOfView();
							//hole die values und befülle die Anzeige
							List<SensorValue> sensorList = sensorMap.get(indexView);
							uniDisplay.setMainContent(sensorList.get(0));
							if(sensorList.size() > 1)
								uniDisplay.setMinorContent(sensorList.get(1));
							else
								uniDisplay.setMinorContent(null);
							uniDisplay.repaintValues();
							
							
							break;
					}
					
					
					kommandoLabel.setText(newValue.toString());
					uniDisplay.getCommandProperty().set(Command.RESET_COMMAND);
				}
				
				
			}});
		 uniDisplay.setMainContent(sensorMap.get(0).get(0));
		 uniDisplay.setMinorContent(sensorMap.get(0).get(1));
		 
		 
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
