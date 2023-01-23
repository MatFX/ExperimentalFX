package glasspane;

import control.universaldisplay.SensorValue;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sensorpanel.first.SensorPanel.Command;
import sensorpanel.first.helper.HelperClass;


/**
 * test application for an rgb sensor
 * @author m.goerlich
 *
 */
public class GlassPaneSensorTest extends Application {
	
	private GlassPaneSensor sensorPanel = new GlassPaneSensor();
	
	private Thread animThread;
	
	private boolean isAnimation;
	
	private Thread autoThread;
	
	private HelperClass helperClass = new HelperClass();
	
	
	public static void main(String[] args) {
        Application.launch(args);
	}
	
	@Override 
    public void start(Stage stage) 
    {
		BorderPane pane = new BorderPane();
		
		  
        sensorPanel.getCommandProperty().addListener(new ChangeListener<Command>()
        {

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) {
				
				
				SensorValue sensorValue = null;
				System.out.println("newValue " + newValue);
				switch(newValue)
				{
					case NEXT_SENSOR_VALUE:
						nextSensorValue();
						
						break;
					case PREVIOUS_SENSOR_VALUE:
						previousSensorValue();
						
						break;
					case AUTO_CHANGE:
						/*
						System.out.println("sensorPanel " + sensorPanel.getAutoProperty().get());
						if(sensorPanel.getAutoProperty().get())
						{
							//wechsle automatisch die Einstellung immer nach vorwärts
							startAutoThread();
							
						}
						else
						{
							//schieße Thread ab.
							stopAutoThread();
						}
						*/
						break;
				}
				
				
			}
        	
        });
		
		
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
								
								helperClass.addCurrentSensorToShow();
								
								if(helperClass.getCurrentSensorToShow() > 2)
									helperClass.setCurrentSensorToShow(0);
								
								//helperClass.setCurrentSensorToShow(1);
								
								SensorValue sensorValue = helperClass.getSelectedSensorValue();
								sensorPanel.getImageProperty().set(sensorValue.getImageBezeichnung());
								sensorPanel.getValueProperty().set(sensorValue.getCurrentValue() + "" + sensorValue.getMeasurementUnit());
								
								try 
								{
									Thread.sleep(2500);
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
     
        
        sensorPanel.setPrefWidth(120);
        sensorPanel.setPrefHeight(140);
        pane.setCenter(sensorPanel);
        pane.setRight(test);
        pane.setBackground(new Background(new BackgroundFill(Color.DARKSLATEGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane);
       
        stage.setTitle("Sensor Panel");
        stage.setScene(scene);
        stage.show();
		
       
		
    }
	
	 protected void previousSensorValue() {
	    	helperClass.subCurrentSensorToShow();
			
			if(helperClass.getCurrentSensorToShow() < 0)
				helperClass.setCurrentSensorToShow(helperClass.getMapSize()-1);
			
			SensorValue sensorValue = helperClass.getSelectedSensorValue(); 
			sensorPanel.getImageProperty().set(sensorValue.getImageBezeichnung());
			sensorPanel.getValueProperty().set(sensorValue.getCurrentValue() + "" + sensorValue.getMeasurementUnit());
			
			
		}

		protected void nextSensorValue() {

			helperClass.addCurrentSensorToShow();
			if(helperClass.getCurrentSensorToShow() >= helperClass.getMapSize())
				helperClass.setCurrentSensorToShow(0);
			
			SensorValue sensorValue = helperClass.getSelectedSensorValue(); 
			sensorPanel.getImageProperty().set(sensorValue.getImageBezeichnung());
			sensorPanel.getValueProperty().set(sensorValue.getCurrentValue() + "" + sensorValue.getMeasurementUnit());
			
			
		}
	
	
	
}
