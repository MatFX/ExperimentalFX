package glasspane;

import java.util.Random;

import javafx.application.Application;
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
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import sensorpanel.first.component.LED_Component.ColorValue;


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
	
	
	public static void main(String[] args) {
        Application.launch(args);
	}
	
	@Override 
    public void start(Stage stage) 
    {
		BorderPane pane = new BorderPane();
		
		
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
								
								int range = 110;
								int min = 50;
								
								int r = random.nextInt(range) + min;
								int g = random.nextInt(range) + min;
								int b = random.nextInt(range) + min;
								
								
								
								int nextChannel = random.nextInt(4);
							
								ColorValue colorValue  = ColorValue.NOT_SPECIFIED;
								colorValue.setColor(Color.rgb(r, g, b));
								
								
								/*
								switch(nextChannel)
								{
									case 0:
										sensorPanel.setSelectedColor(FourColorCorner.LED.TOP_LEFT, colorValue);
										break;
									case 1:
										sensorPanel.setSelectedColor(FourColorCorner.LED.TOP_RIGHT, colorValue);
										break;
									case 2:
										sensorPanel.setSelectedColor(FourColorCorner.LED.BOTTOM_LEFT, colorValue);
										break;
									case 3:
										sensorPanel.setSelectedColor(FourColorCorner.LED.BOTTOM_RIGHT, colorValue);
										break;
								
								}*/
								
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
	
	
	
}
