package control.rgbw;

import control.universaldisplay.SensorValue;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SingleRGBWControl extends Application
{
	
	
	
	

	@Override
	public void start(Stage stage) throws Exception 
	{
		 BorderPane pane = new BorderPane();
		 
		 SensorValue majorValue = new SensorValue(0D, 0D , 100D, "%", "");
		 
		 
		 RGBWDimmerControl rgbwDimmerControl = new RGBWDimmerControl();
		 rgbwDimmerControl.setMajorValue(majorValue);
		 
		 pane.setCenter(rgbwDimmerControl);
		 
		 
		    
		 VBox vBox = new VBox(2);
	     vBox.setPadding(new Insets(5, 5, 5, 5));
	        
	        //start and stop animation with random values
	        //it simulate incoming signals from the actuator
		 ToggleButton test = new ToggleButton("Start");
		 test.setOnAction(new EventHandler<ActionEvent>(){
		
			@Override
			public void handle(ActionEvent event) 
			{
				if(test.isSelected())
				{
					
					rgbwDimmerControl.startAnimation();
					test.setText("Ende");
				}
				else
				{
					rgbwDimmerControl.stopAnimation();
					test.setText("Start");
				}
			
			}
	    	
		 });
		 vBox.getChildren().addAll(test);
		 
		 pane.setRight(vBox);
		 
		 
		 
		 
		 
		 Scene scene = new Scene(pane);
	        

	     stage.setTitle("JavaFX RGBW control");
	     stage.setScene(scene);
	     stage.setWidth(600);
	     stage.setHeight(600);
	        
	     stage.show();
	        
	     //closing event with stop animation
	     stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
	     {
	            @Override
	            public void handle(WindowEvent event) 
	            {
	            
	            }
	     });
		
		
	}

	

    public static void main(String[] args) {
        Application.launch(args);
    }
}
