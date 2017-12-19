package control.temperature;

import control.dimmer.DimmerControl;
import control.dimmer.DimmerControl.Command;
import control.dimmer.IActivationIcon.Pos;
import control.dimmer.SingleDimmerControl.Man;
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

public class SingleTemperatureControl extends Application {

	@Override
	public void start(Stage stage) 
	{
		 BorderPane pane = new BorderPane();
	      
		 //center of borderpane, initialize
		 TemperatureControl tempControl = new TemperatureControl();
		
		 pane.setCenter(tempControl);
	        
	        
	     Scene scene = new Scene(pane);
	        

	     stage.setTitle("JavaFX SingleTemperaturControl");
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

	public static void main(String[] args) {
		launch(args);
	}
}
