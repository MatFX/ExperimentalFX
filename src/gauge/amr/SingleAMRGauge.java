package gauge.amr;

import control.temperature.TemperatureControl;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SingleAMRGauge extends Application
{
	

	@Override
	public void start(Stage stage) throws Exception {
		 BorderPane pane = new BorderPane();
	      
		 //center of borderpane, initialize
		 AMRGauge tempControl = new AMRGauge();
		
		 pane.setCenter(tempControl);
	        
	        
	     Scene scene = new Scene(pane);
	        

	     stage.setTitle("JavaFX Single AMR Gauge");
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
