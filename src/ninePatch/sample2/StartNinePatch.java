package ninePatch.sample2;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class StartNinePatch extends Application 
{

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(5,5,5,5));
	    //one digit
		NinePatchElement ninePatch = new NinePatchElement();
		
		ToggleButton test = new ToggleButton("Start");
	       
        pane.setCenter(ninePatch);
       

        Scene scene = new Scene(pane, 330, 100);

        primaryStage.setTitle("Nine Patch");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //closing event with stop animation
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() 
        {
            @Override
            public void handle(WindowEvent event) {
            	//TODO
            	//sevenSegDigit.stopAnimation();
            }
        });
		
	}


	public static void main(String[] args) {
        Application.launch(args);
    }
}
