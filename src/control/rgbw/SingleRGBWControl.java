package control.rgbw;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SingleRGBWControl extends Application
{
	
	
	
	

	@Override
	public void start(Stage stage) throws Exception 
	{
		 BorderPane pane = new BorderPane();
		 
		 RGBWDimmerControl rgbwDimmerControl = new RGBWDimmerControl();
		 
		 pane.setCenter(rgbwDimmerControl);
		 
		 
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
