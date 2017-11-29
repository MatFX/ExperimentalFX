package control.dimmer;

import control.dimmer.DimmerControl.Command;
import control.dimmer.IActivationIcon.Pos;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tools.helper.ImageLoader;

/**
 * Simple example to show some characteristic from this DimmerControl
 * @author m.goerlich
 *
 */
public class SingleDimmerControl extends Application 
{
	/**
	 * enum for the second picture change
	 *
	 */
	public enum Man
	{
		STAND,
		RUNNING,
		SHRUG;
	}
	
	/**
	 * state helper for switchting the pics.
	 */
	private Man state = Man.STAND;
   

    @Override 
    public void start(Stage stage) 
    {
        BorderPane pane = new BorderPane();
        //center of borderpane, initialize
        DimmerControl customCircle = new DimmerControl();
        //Voreinstellungen die später mal von der Anwendung kommen
        //some preset values for the quick selection.
        customCircle.setPresetValues(new double[]{0d, 25d, 33d, 78d, 100d});
        //Blockierungsbild setzen auf die rechte Position (later deactivation/activation)
        //one image to show the activation/deactivation mode
        customCircle.initImage(Pos.RIGHT, ImageLoader.getImageFromIconFolder("schloss_schwarz"));
        //another pic for the next mode
        customCircle.initImage(Pos.MIDDLE, ImageLoader.getImageFromIconFolder("man"));
        //the middle pic change only the image content not the activation mode
        customCircle.setActivation(Pos.MIDDLE);
        
        
        
        Label kommandoLabel = new Label();
        //pseudo simulation, was später mal die eigentliche Anwendung übernimmt, also physikalisches Senden
        //this listener is needed to do something in your application
        customCircle.getCommandProperty().addListener(new ChangeListener<Command>()
        {

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue)
			{
				if(newValue != Command.RESET_COMMAND)
				{
					switch(newValue)
					{
						case ON:
							customCircle.setAnimatiedCurrentValue(100);
							break;
						case OFF:
							customCircle.setAnimatiedCurrentValue(0);
							break;
						case SEND_PRESET:
							customCircle.setAnimatiedCurrentValue(customCircle.getSelectedPresetValue());
							break;
					}
					kommandoLabel.setText(newValue.toString());
					
					//reset the commandproperty! no reset no change to send the same command again 
					//(example: send ON and the radiosignal dont receive the actuator, the user will try again with ON
					customCircle.getCommandProperty().set(Command.RESET_COMMAND);
				}
			}
        	
        });
        
        VBox vBox = new VBox(2);
        vBox.setPadding(new Insets(5, 5,0,0));
        
        //start and stop animation with random values
        //it simulate incoming signals from the actuator
        ToggleButton test = new ToggleButton("Start");
        test.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) 
			{
				if(test.isSelected())
				{
					
					customCircle.startAnimation();
					test.setText("Ende");
				}
				else
				{
					customCircle.stopAnimation();
					test.setText("Start");
				}
			
			}
        	
        });
        
        //Aktivierungs-/Deaktivierungsbeispiel
        //activition/deactivation mode for the right pic on screen
        ToggleButton lockState = new ToggleButton("Change Lock");
        lockState.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) 
			{
				if(lockState.isSelected())
				{
					
					customCircle.setActivation(Pos.RIGHT);
					
				}
				else
				{
					customCircle.setDeactivation(Pos.RIGHT);
				}
			
			}
        	
        });
        
        //change the image at the middle imageview on screen...the other possible mode.
        Button threeState = new Button("Change Pic");
        threeState.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) 
			{
				if(state == Man.STAND)
				{
					state = Man.RUNNING;
					customCircle.initImage(Pos.MIDDLE, ImageLoader.getImageFromIconFolder("runningMan"));
					
				}
				else if(state == Man.RUNNING)
				{
					state = Man.SHRUG;
					customCircle.initImage(Pos.MIDDLE, ImageLoader.getImageFromIconFolder("schulterzucken"));
				}
				else
				{
					state = Man.STAND;
					 customCircle.initImage(Pos.MIDDLE, ImageLoader.getImageFromIconFolder("man"));
				}
			
			}
        	
        });
        
        
        vBox.getChildren().addAll(test, lockState, threeState);
        
        pane.setCenter(customCircle);
        pane.setRight(vBox);
        
        VBox commandArea = new VBox(2);
        commandArea.setPadding(new Insets(5, 5, 5, 5));
        commandArea.getChildren().addAll(kommandoLabel);
        
        pane.setBottom(commandArea);
        
        //Damit die Control nicht am Rand klebt
        VBox emptyLeftArea = new VBox(0);
        emptyLeftArea.setPadding(new Insets(0,15,0,0));
        pane.setLeft(emptyLeftArea);
        
        
        
        Scene scene = new Scene(pane);
        

        stage.setTitle("JavaFX First Dimmer Control");
        stage.setScene(scene);
        stage.setWidth(400);
        stage.setHeight(400);
        
        stage.show();
        
        //closing event with stop animation
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
        {
            @Override
            public void handle(WindowEvent event) {
            	customCircle.stopAnimation();
            }
        });
    }

    public static void main(String[] args) {
        Application.launch(args);
    }


}


