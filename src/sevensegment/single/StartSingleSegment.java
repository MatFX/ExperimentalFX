package sevensegment.single;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;




public class StartSingleSegment extends Application 
{
   

    @Override 
    public void start(Stage stage) 
    {

    	
        BorderPane pane = new BorderPane();
        //one digit
        SevenSegmentDigit sevenSegDigit = new SevenSegmentDigit();
        
        //start und stop von Zufallswerte Darstellung
        ToggleButton test = new ToggleButton("Start");
       
        test.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) 
			{
				if(test.isSelected())
				{
					
					sevenSegDigit.startAnimation();
					test.setText("Ende");
				}
				else
				{
					sevenSegDigit.stopAnimation();
					test.setText("Start");
				}
			
			}
        	
        });
        
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
            	sevenSegDigit.stopAnimation();
            }
        });
    }

    public static void main(String[] args) {
        Application.launch(args);
    }


}


