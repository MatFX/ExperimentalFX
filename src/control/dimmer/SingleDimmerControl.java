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
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tools.helper.ImageLoader;




public class SingleDimmerControl extends Application 
{
   

    @Override 
    public void start(Stage stage) 
    {
        BorderPane pane = new BorderPane();
        //first gauge; center of borderpane
        DimmerControl customCircle = new DimmerControl();
        //Voreinstellungen die später mal von der Anwendung kommen
        customCircle.setPresetValues(new double[]{0d, 25d, 33d, 78d, 100d});
        //Blockierungsbild setzen auf die rechte Position
        customCircle.initImage(Pos.RIGHT, ImageLoader.getImageFromIconFolder("schloss_schwarz"));
        
        
        
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
        
        VBox vBox = new VBox(2);
        vBox.setPadding(new Insets(5, 5,0,0));
        
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
        
        //Aktivierungs-/Deaktivierungsbeispiel
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
        
        
        vBox.getChildren().addAll(test, lockState);
        
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


