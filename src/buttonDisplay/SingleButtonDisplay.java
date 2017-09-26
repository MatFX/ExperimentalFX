package buttonDisplay;

import firstgauge.CustomCircle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SingleButtonDisplay extends Application
{

	@Override 
    public void start(Stage stage) 
    {
        BorderPane pane = new BorderPane();
        //first gauge; center of borderpane
        ButtonDisplay buttonDisplay = new ButtonDisplay();
        
     
        //start and stop animation with random values
        ToggleButton test = new ToggleButton("Start");
        /* TODO später
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
        */
        pane.setCenter(buttonDisplay);
        pane.setRight(test);

        Scene scene = new Scene(pane);

        stage.setTitle("JavaFX First Gauge");
        stage.setScene(scene);
        stage.show();
        
        /* TODO
        //closing event with stop animation
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
        {
            @Override
            public void handle(WindowEvent event) {
            	customCircle.stopAnimation();
            }
        });*/
    }

    public static void main(String[] args) {
        Application.launch(args);
    }


}
