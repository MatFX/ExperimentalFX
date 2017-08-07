package sevensegment.multiple;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class StartMultipleSegment extends Application
{

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		  BorderPane pane = new BorderPane();
		  
		  ValueContainer valueContainer = new ValueContainer<Integer>(75, 0, 200, 0);
		  pane.setCenter(valueContainer);
	
		  ToggleButton test = new ToggleButton("Start");
	      
		  
		  test.setOnAction(new EventHandler<ActionEvent>(){
		
				@Override
				public void handle(ActionEvent event) 
				{
					if(test.isSelected())
					{
						
						valueContainer.startAnimation();
						test.setText("Ende");
					}
					else
					{
						valueContainer.stopAnimation();
						test.setText("Start");
					}
				
				}
		    	
		  });
		  
		  pane.setRight(test);
		  
		  
		  
		  Scene scene = new Scene(pane);


	    
	     primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() 
	     {
	            @Override
	            public void handle(WindowEvent event) {
	            	valueContainer.stopAnimation();
	            }
	     });
	      
	      
	      primaryStage.setTitle("7 Segment Digits");
	      primaryStage.setScene(scene);
	      primaryStage.show();
		
	}
	
    public static void main(String[] args) {
        Application.launch(args);
    }

}
