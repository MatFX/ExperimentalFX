package control.universaldisplay;

import control.universaldisplay.UniversalDisplay.Command;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SingleUniversalDisplay extends Application 
{
	
	

	
	
	
	@Override
	public void start(Stage stage) 
	{
		 BorderPane pane = new BorderPane();
	      
		 //center of borderpane, initialize
		 UniversalDisplay tempControl = new UniversalDisplay();
		 pane.setCenter(tempControl);
	     
		 Label kommandoLabel = new Label();
		 
		 tempControl.getCommandProperty().addListener(new ChangeListener<Command>(){

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) 
			{
				if(newValue != Command.RESET_COMMAND)
				{
					kommandoLabel.setText(newValue.toString());
					tempControl.getCommandProperty().set(Command.RESET_COMMAND);
				}
				
				
			}});
		 VBox commandArea = new VBox(2);
	     commandArea.setPadding(new Insets(5, 5, 5, 5));
	     commandArea.getChildren().addAll(kommandoLabel);
	     pane.setBottom(commandArea);
	     
	     
	     VBox vBox = new VBox(2);
	     vBox.setPadding(new Insets(5, 5,0,0));
	     
	     ToggleButton test = new ToggleButton("Start");
	     test.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent event) 
				{
					if(test.isSelected())
					{
						
						//tempControl.startAnimation();
						test.setText("Ende");
					}
					else
					{
						//tempControl.stopAnimation();
						test.setText("Start");
					}
				
				}
	        	
	        });
	    vBox.getChildren().add(test);
	        
	     
	     
	    pane.setRight(vBox);
	     
	        
	     
	     
	     
	     
	     
		 
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
