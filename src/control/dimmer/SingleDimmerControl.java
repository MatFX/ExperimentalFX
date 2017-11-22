package control.dimmer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
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
        
        //start and stop animation with random values
        ToggleButton test = new ToggleButton("Start");
        test.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) 
			{
				/* TODO ?
				if(test.isSelected())
				{
					
					customCircle.startAnimation();
					test.setText("Ende");
				}
				else
				{
					customCircle.stopAnimation();
					test.setText("Start");
				}*/
			
			}
        	
        });
        
        pane.setCenter(customCircle);
        pane.setRight(test);
       
        
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
            	//TODO?
            	//customCircle.stopAnimation();
            }
        });
    }

    public static void main(String[] args) {
        Application.launch(args);
    }


}


