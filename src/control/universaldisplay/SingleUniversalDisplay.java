package control.universaldisplay;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SingleUniversalDisplay extends Application {

	@Override
	public void start(Stage stage) 
	{
		 BorderPane pane = new BorderPane();
	      
		 //center of borderpane, initialize
		 UniversalDisplay tempControl = new UniversalDisplay();
		
		 pane.setCenter(tempControl);
	        
	        
	     Scene scene = new Scene(pane);
	        

	     stage.setTitle("JavaFX Universelles Display");
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
