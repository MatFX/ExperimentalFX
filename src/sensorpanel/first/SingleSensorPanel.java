package sensorpanel.first;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import control.universaldisplay.SensorValue;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sensorpanel.first.SensorPanel.Command;
import sensorpanel.first.component.LED_Component.ColorValue;
import sensorpanel.first.helper.HelperClass;


public class SingleSensorPanel extends Application
{
	
	private Thread animThread;
	
	private boolean isAnimation;
	

	@Override 
    public void start(Stage stage) 
    {
		//Storage for different values
		HelperClass helperClass = new HelperClass();
		
		
        BorderPane pane = new BorderPane();
        
        SensorPanel sensorPanel = new SensorPanel();
        
        sensorPanel.getCommandProperty().addListener(new ChangeListener<Command>()
        {

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) {
				
				
				SensorValue sensorValue = null;
				System.out.println("newValue " + newValue);
				switch(newValue)
				{
					case NEXT_SENSOR_VALUE:
						
						helperClass.addCurrentSensorToShow();
						if(helperClass.getCurrentSensorToShow() >= helperClass.getMapSize())
							helperClass.setCurrentSensorToShow(0);
						sensorValue = helperClass.getSelectedSensorValue(); 
						
						sensorPanel.getDescriptionProperty().set(sensorValue.getDescription());
						sensorPanel.getValueProperty().set(getValueAsString(sensorValue, helperClass.getCurrentSensorToShow()));
						
						break;
					case PREVIOUS_SENSOR_VALUE:
						helperClass.subCurrentSensorToShow();
					
						if(helperClass.getCurrentSensorToShow() < 0)
							helperClass.setCurrentSensorToShow(helperClass.getMapSize()-1);
						sensorValue = helperClass.getSelectedSensorValue(); 
						
						sensorPanel.getDescriptionProperty().set(sensorValue.getDescription());
						sensorPanel.getValueProperty().set(getValueAsString(sensorValue, helperClass.getCurrentSensorToShow()));
						
						break;
					case AUTO_CHANGE:
						if(sensorPanel.getAutoProperty().get())
						{
							//wechsle automatisch die Einstellung immer nach vorwärts
						
							
						}
						else
						{
							//schieße Thread ab.
							
						}
						
						break;
				}
				
				
			}
        	
        });
     
        //start and stop animation with random values
        ToggleButton test = new ToggleButton("Start");
        test.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) 
			{
				if(test.isSelected())
				{
					
					startAnimation();
					test.setText("Ende");
				}
				else
				{
					stopAnimation();
					test.setText("Start");
				}
			
			}

			private void stopAnimation() {
				isAnimation = false;
				if(animThread != null)
					animThread.stop();
				
			}

			private void startAnimation() 
			{
				if(animThread != null && animThread.isAlive())
					animThread.stop();
			
				isAnimation = true;
				
				
				Runnable runnable = new Runnable()
				{
					public void run()
					{
						while(isAnimation)
						{
							
							Random random = new Random();
							int nextChannel = random.nextInt(3);
							
							int nextColor = random.nextInt(4);
							ColorValue colorValue = ColorValue.OFF;
							switch(nextColor)
							{ 
								case 1:
									colorValue = ColorValue.GREEN;
									break;
								case 2:
									colorValue = ColorValue.RED;
									break;
								case 3:
									colorValue = ColorValue.YELLOW;
									break;
							}
							
							switch(nextChannel)
							{
								case 0:
									sensorPanel.setSelectedColor(SensorPanel.LED.LEFT, colorValue);
									break;
								case 1:
									sensorPanel.setSelectedColor(SensorPanel.LED.MIDDLE, colorValue);
									break;
								case 2:
									sensorPanel.setSelectedColor(SensorPanel.LED.RIGHT, colorValue);
									break;
							
							}
							
							
							
							
							
							
							
							try 
							{
								Thread.sleep(500);
							}
							catch (InterruptedException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}
					
				};
				animThread = new Thread(runnable);
				animThread.start();
				
			}
        	
        });
     

     
        
        sensorPanel.setPrefWidth(150);
        sensorPanel.setPrefHeight(60);
        pane.setCenter(sensorPanel);
        pane.setRight(test);

        Scene scene = new Scene(pane);

        stage.setTitle("Sensor Panel");
        stage.setScene(scene);
        stage.show();
        
        /* TODO
        //closing event with stop animation
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
        {
            @Override
            public void handle(WindowEvent event) {
            	customCircle.stopAnimation();
            }
        });*/
    }

    protected String getValueAsString(SensorValue sensorValue, int currentSensorToShow)
    {
    	//sensorValue, currentSensorToShow
    	
    	//Feuchtigkeit Ganzzahl
    	if(currentSensorToShow == HelperClass.HUMIDITY)
    	{
    		int roundedHum = (int) Math.round(sensorValue.getCurrentValue());
    		return roundedHum + " " + sensorValue.getMeasurementUnit();
    	}
    	//Rest mit einer Nachkommastelle
    	else
    	{
    		double rounded = (double) Math.round(sensorValue.getCurrentValue()*10D)/10D;
    		return String.format("%1.2f", rounded) + " " + sensorValue.getMeasurementUnit();
    	}
    	
    	
    	
	}

	public static void main(String[] args) {
        Application.launch(args);
    }


}
