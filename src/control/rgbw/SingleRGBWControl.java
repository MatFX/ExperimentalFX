package control.rgbw;

import java.util.ArrayList;
import java.util.List;


import control.rgbw.RGBWDimmerControl.Command;
import control.universaldisplay.SensorValue;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tools.helper.GenericPair;

public class SingleRGBWControl extends Application
{
	
	private int indexPreset = 0;
	
	private List<GenericPair<String, Integer>> presetList;
	
	private RGBWDimmerControl rgbwDimmerControl;
	
	

	@Override
	public void start(Stage stage) throws Exception 
	{
		 BorderPane pane = new BorderPane();
		 
		 SensorValue majorValue = new SensorValue(0D, 0D , 100D, "%", "");
		 
		 //presetlist for the example
		 presetList = new ArrayList<GenericPair<String, Integer>>();
		 
		 presetList.add(new GenericPair<String, Integer>("#FF0000", 100));
		 presetList.add(new GenericPair<String, Integer>("#454585", 75));
		 presetList.add(new GenericPair<String, Integer>("#FF1225", 0));
		 presetList.add(new GenericPair<String, Integer>("#00FFFF", 33));
		 
		 
		 
		 rgbwDimmerControl = new RGBWDimmerControl();
		 rgbwDimmerControl.setMajorValue(majorValue);
		 
		 rgbwDimmerControl.getCommandProperty().addListener(new ChangeListener<Command>()
	        {

				@Override
				public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) {
					if(newValue != Command.RESET_COMMAND)
					{
						switch(newValue)
						{
							case PREVIOUS_PRESET:
								previousPreset();
								break;
							case NEXT_PRESET:
								nextPreset();
								break;
							case RGB_OFF:
								rgbwDimmerControl.setColorValue(Color.BLACK);
								break;
							case W_OFF:
								rgbwDimmerControl.setCurrentValue(0, false);
								break;
						}
						rgbwDimmerControl.getCommandProperty().set(Command.RESET_COMMAND);
					}
					
					
				}

				
	        	
	        });
		 
		 
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

	

    protected void nextPreset() 
    {
		indexPreset++;
    	if(indexPreset >= presetList.size())
    		indexPreset = 0;
    	rgbwDimmerControl.setPresetOnScreen(presetList.get(indexPreset).getLeft(), presetList.get(indexPreset).getRight());
	
    }



	protected void previousPreset() 
	{
		indexPreset--;
		if(indexPreset < 0)
			indexPreset = presetList.size()-1;
		
		rgbwDimmerControl.setPresetOnScreen(presetList.get(indexPreset).getLeft(), presetList.get(indexPreset).getRight());
	}



	public static void main(String[] args) {
        Application.launch(args);
    }
}
