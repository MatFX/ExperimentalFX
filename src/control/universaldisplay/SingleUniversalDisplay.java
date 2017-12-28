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
import javafx.scene.PerspectiveCamera;
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
	
	
	private static boolean isAnimation = false;
	
	private static Thread animThread = null;
	
	private UniversalDisplay uniDisplay = null;
	
	
	@Override
	public void start(Stage stage) 
	{
		
		List<SensorValue> sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(25, 0, 40, "°C", "img_temperatur"));
		sensorList.add(new SensorValue(22.5, 8, 40, "°C", "", new double[]{21.5, 24.5, 30}));
		sensorMap.put(TEMPERATURE, sensorList);
		
		sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(25.4, 0, 100, "%", "img_feuchtigkeit"));
		sensorMap.put(HUMIDITY, sensorList);
		
		sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(2500, 0, 3000, "Lux", "img_helligkeit"));
		sensorMap.put(BRIGHTNESS, sensorList);
		
		
		 BorderPane pane = new BorderPane();
	      
		 //center of borderpane, initialize
		 uniDisplay = new UniversalDisplay(3, true, true);
		 
		 
		 pane.setCenter(uniDisplay);
	     
		 Label kommandoLabel = new Label();
		 
		 uniDisplay.getCommandProperty().addListener(new ChangeListener<Command>(){

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) 
			{
				int indexView= uniDisplay.getIndexOfView();
				List<SensorValue> sensorList;
				if(newValue != Command.RESET_COMMAND)
				{
					switch(newValue)
					{
						case PREVIOUS_SENSOR_VALUE:
						case NEXT_SENSOR_VALUE:
							
							//hole die values und befülle die Anzeige
							sensorList = sensorMap.get(indexView);
							uniDisplay.setMainContent(sensorList.get(0));
							if(sensorList.size() > 1)
								uniDisplay.setMinorContent(sensorList.get(1));
							else
								uniDisplay.setMinorContent(null);
							uniDisplay.repaintValues();
							
							
							break;
						case UP:
							sensorList = sensorMap.get(indexView);
							if(sensorList.get(1) != null)
							{
								//change setpoint value; one stepü
								double stepping = sensorList.get(1).getStepValue();
								double currentValue = sensorList.get(1).getCurrentValue();
								currentValue = currentValue + stepping;
								if(currentValue > sensorList.get(1).getBis())
									currentValue = sensorList.get(1).getBis();
								
								sensorList.get(1).setCurrentValue(currentValue);
								uniDisplay.repaintValues();
							}
							
							
							
							break;
						case DOWN:
							sensorList = sensorMap.get(indexView);
							if(sensorList.get(1) != null)
							{
								//hier rückwärts um die stepping size
								double stepping = sensorList.get(1).getStepValue();
								double currentValue = sensorList.get(1).getCurrentValue();
								currentValue = currentValue - stepping;
								if(currentValue < sensorList.get(1).getVon())
									currentValue = sensorList.get(1).getBis();
								sensorList.get(1).setCurrentValue(currentValue);
								uniDisplay.repaintValues();
							}
							break;
						case SEND_VALUE:
							//nur auslösen wenn auch wirklich die presets zur Zeit angezeigt werden.
							if(uniDisplay.isShowPresetValue())
							{
								sensorList = sensorMap.get(indexView);
								if(sensorList.get(1) != null)
								{
									
									double presetValue = sensorList.get(1).getPresetValueFrom(uniDisplay.getPresetIndex());
									sensorList.get(1).setCurrentValue(presetValue);
								}
							}
							break;
					}
					
					
					kommandoLabel.setText(newValue.toString());
					uniDisplay.getCommandProperty().set(Command.RESET_COMMAND);
				}
				
				
			}});
		 uniDisplay.setMainContent(sensorMap.get(0).get(0));
		 uniDisplay.setMinorContent(sensorMap.get(0).get(1));
		 uniDisplay.repaintValues();
		 
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
						test.setText("End");
						startRandomValues();
					}
					else
					{
						test.setText("Start");
						endRandomValues();
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
	

	private void startRandomValues() 
	{
		if(animThread != null)
		{
			animThread.stop();
		}
		
		animThread = new Thread(new Runnable(){

			@Override
			public void run() 
			{
				while(true)
				{
					//per zufall index ermitteln
					int zufallsIndex = (int)(Math.random() * sensorMap.size()); 
					
					//dort das element 0 aus liste holen
					SensorValue sensorValue = sensorMap.get(zufallsIndex).get(0);
					//neuen wert ermitteln mit
					
					
					double spanne = sensorValue.getBis() - sensorValue.getVon();
					//rundung ansonsten gibt es Probleme bei der Ansicht, weil die Nachkommstellen auch entscheidend 
					//für die spätere fontsize ist.
					double zufallsWert = Math.round((Math.random() * spanne) * 10D)/10D;
					sensorValue.setCurrentValue(zufallsWert);
					
					//evtl. sicht aktualiseren
					if(zufallsIndex == uniDisplay.getIndexOfView())
					{
						uniDisplay.repaintValues();
					}
					
					
					try 
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}
				
			}});
		animThread.start();
		
		
		
	}
	
	

	private void endRandomValues() 
	{
		if(animThread != null)
			animThread.stop();
	}

	

	public static void main(String[] args) {
		launch(args);
	}
}
