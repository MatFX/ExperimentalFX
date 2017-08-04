package sevensegment;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;




public class SingleSegment extends Application 
{
   

    @Override 
    public void start(Stage stage) 
    {

    	
        BorderPane pane = new BorderPane();
        //one digit
        SevenSegmentDigit sevenSegDigit = new SevenSegmentDigit();
        
        ToggleButton test = new ToggleButton("Start");
        /* TODO
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
        	
        });*/
        
        pane.setCenter(sevenSegDigit);
        pane.setRight(test);

        Scene scene = new Scene(pane);

        stage.setTitle("7 Segment Digit");
        stage.setScene(scene);
        stage.show();
        
        //closing event with stop animation
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
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


