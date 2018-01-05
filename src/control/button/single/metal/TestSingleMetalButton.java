package control.button.single.metal;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TestSingleMetalButton extends Application {

	@Override
	public void start(Stage stage) 
	{
		 BorderPane pane = new BorderPane();
		 pane.setPadding(new Insets(5,5,5,5));
		 pane.setStyle("-fx-background-color: #444444");
		 
		 final Label kommandoLabel = new Label();
		 kommandoLabel.setStyle("-fx-text-fill: #FFFFFF");
		 
		 
		 SingleMetalButton smb = new SingleMetalButton();
		
		 
		 pane.setCenter(smb);
		 
			
		 VBox commandArea = new VBox(2);
	     commandArea.setPadding(new Insets(5, 5, 5, 5));
	     commandArea.getChildren().addAll(kommandoLabel);
	     pane.setBottom(commandArea);
		 
	     VBox vBox = new VBox(2);
	     vBox.setPadding(new Insets(5, 5,0,0));
	     /*
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
	    	*/
	     
	     final ColorPicker colorPicker = new ColorPicker();
	     colorPicker.setValue(Color.web("#707070"));
			colorPicker.setOnAction(new EventHandler<ActionEvent>() {
	            @Override 
	            public void handle(ActionEvent e)
	            {
	            	smb.setInlayFill(colorPicker.getValue());
	            	
	            }
	        });
			
	     
	     
	     vBox.getChildren().add(colorPicker);
	     pane.setRight(vBox);
	    
		
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
