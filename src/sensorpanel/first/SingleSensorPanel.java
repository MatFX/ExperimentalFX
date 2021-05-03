package sensorpanel.first;

import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sensorpanel.first.component.LED_Component.ColorValue;


public class SingleSensorPanel extends Application
{
	
	private Thread animThread;
	
	private boolean isAnimation;
	
	

	@Override 
    public void start(Stage stage) 
    {
        BorderPane pane = new BorderPane();
        
        SensorPanel sensorPanel = new SensorPanel();
        
     
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
				// TODO Auto-generated method stub
				
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

    public static void main(String[] args) {
        Application.launch(args);
    }


}
