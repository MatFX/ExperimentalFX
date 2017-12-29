package control.button;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestCombinedThreeButtonControl extends Application
{
	private CombinedThreeButtonControl combinedButton;

	@Override
	public void start(Stage stage) throws Exception
	{
		 BorderPane pane = new BorderPane();
		 pane.setStyle("-fx-background-color: #FF0000");
		 
		 combinedButton = new CombinedThreeButtonControl();
		 
		 pane.setCenter(combinedButton);
		 
		 Label kommandoLabel = new Label();
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
	    
	     pane.setRight(vBox);
	    
		
	     Scene scene = new Scene(pane);
	    
		 stage.setTitle("Combined Three Button");
		 stage.setScene(scene);
		 stage.setWidth(400);
		 stage.setHeight(400);
		 stage.show();
		
	}
	

	public static void main(String[] args) {
		launch(args);
	}

}
