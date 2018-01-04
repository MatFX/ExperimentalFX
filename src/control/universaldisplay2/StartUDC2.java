package control.universaldisplay2;

import control.button.combined.CombinedThreeButtonControl;
import control.button.combined.CombinedThreeButtonControl.Command;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartUDC2 extends Application
{
	private UniversalDisplayControl udc;
	

	@Override
	public void start(Stage stage) 
	{
		 BorderPane pane = new BorderPane();
		 pane.setStyle("-fx-background-color: #444444");
		 
		 final Label kommandoLabel = new Label();
		 kommandoLabel.setStyle("-fx-text-fill: #FFFFFF");
			
		 
		 udc = new UniversalDisplayControl();
		 //HBox.setHgrow(udc, Priority.ALWAYS);
		// VBox.setVgrow(udc, Priority.ALWAYS);
		// udc.setMaxHeight(Double.MAX_VALUE);
		// udc.setMaxWidth(Double.MAX_VALUE);
		// udc.setMinHeight(Double.MAX_VALUE);
		// udc.setMinWidth(Double.MAX_VALUE);
		 
		 /*
		 combinedButton.getCommandProperty().addListener(new ChangeListener<Command>(){

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue)
			{
				
				if(newValue != Command.RESET_COMMAND)
				{
					switch(newValue)
					{
						case LEFT_BUTTON:
						case RIGHT_BUTTON:
						case MIDDLE_BUTTON:
						
							break;
					}
					
					
					kommandoLabel.setText(newValue.toString());
					combinedButton.getCommandProperty().set(Command.RESET_COMMAND);
				}
				
				
			}
		});*/
		 
		 pane.setCenter(udc);
		 
		
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
						test.setText("End");
					}
					else
					{
						test.setText("Start");
					}
				
				}

	        });
	     vBox.getChildren().add(test);
	    
	     //pane.setRight(vBox);
	    
		
	     Scene scene = new Scene(pane);
	    
		 stage.setTitle("UniversalDisplayControl 2");
		 stage.setScene(scene);
		 stage.setWidth(400);
		 stage.setHeight(266);
		 stage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
