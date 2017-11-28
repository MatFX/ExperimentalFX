package control.dimmer;

import control.dimmer.DimmerControl.Command;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;




public class SingleDimmerControl extends Application 
{
   

    @Override 
    public void start(Stage stage) 
    {
        BorderPane pane = new BorderPane();
        //first gauge; center of borderpane
        DimmerControl customCircle = new DimmerControl();
        customCircle.setPresetValues(new double[]{25d, 33d, 78d});
        
        Label kommandoLabel = new Label();
        //pseudo simulation, was später mal die eigentliche Anwendung übernimmt, also physikalisches Senden
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
							//TODO weiß nicht oder direkt setzen
							customCircle.setAnimatiedCurrentValue(100);
							//Platform.runLater(() -> customCircle.setCurrentValue(100, false));
							break;
						case OFF:
							customCircle.setAnimatiedCurrentValue(0);
							//Platform.runLater(() -> customCircle.setCurrentValue(0, false));
							break;
						case SEND_PRESET:
							customCircle.setAnimatiedCurrentValue(customCircle.getSelectedPresetValue());
							
							break;
					}
					
					kommandoLabel.setText(newValue.toString());
					customCircle.getCommandProperty().set(Command.RESET_COMMAND);
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
        
        pane.setCenter(customCircle);
        pane.setRight(test);
        pane.setBottom(kommandoLabel);
        
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


