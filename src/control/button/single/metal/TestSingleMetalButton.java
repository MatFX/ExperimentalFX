package control.button.single.metal;


import control.button.single.metal.SingleMetalButton.Command;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tools.helper.ImageLoader;

/**
 * sample application for the button
 * @author m.goerlich
 *
 */
public class TestSingleMetalButton extends Application 
{
	
	private static int imgNumber = 0;
	
	private static int textNumber = 0;
	
	@Override
	public void start(Stage stage) 
	{
		
			
		 BorderPane pane = new BorderPane();
		 pane.setPadding(new Insets(5,5,5,5));
		 pane.setStyle("-fx-background-color: #444444");
		 
		 final Label commandoLabel = new Label();
		 commandoLabel.setStyle("-fx-text-fill: #FFFFFF");
		 
		 
		 SingleMetalButton smb = new SingleMetalButton();
		 //listening on the command property and do anything
		 smb.getCommandProperty().addListener(new ChangeListener<Command>(){

				@Override
				public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue)
				{
					
					if(newValue != Command.RESET_COMMAND)
					{
						switch(newValue)
						{
							case BUTTON_PRESSED:
								//do anything
								break;
							case BUTTON_RELEASED:
								//do anything
								break;
						}
						commandoLabel.setText(newValue.toString());
						smb.getCommandProperty().set(Command.RESET_COMMAND);
					}
					
					
				}
			});
		 pane.setCenter(smb);
		 
			
		 VBox commandArea = new VBox(2);
	     commandArea.setPadding(new Insets(5, 5, 5, 5));
	     commandArea.getChildren().addAll(commandoLabel);
	     pane.setBottom(commandArea);
		 
	     VBox vBox = new VBox(5);
	     vBox.setPadding(new Insets(5, 5,0,0));
	     
	     final ColorPicker colorPicker = new ColorPicker();
	     colorPicker.setValue(Color.web("#707070"));
			colorPicker.setOnAction(new EventHandler<ActionEvent>() {
	            @Override 
	            public void handle(ActionEvent e)
	            {
	            	smb.setInlayFill(colorPicker.getValue());
	            	
	            }
	        });
		
		 Button textButton = new Button("setText");
		 textButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				switch(textNumber)
				{
					case 0:
						smb.setText(">");
						smb.setImageView(null);
						break;
					case 1:
						smb.setText("<");
						smb.setImageView(null);
						break;
					case 2:
						smb.setText("push");
						smb.setImageView(null);
						break;
					case 3:
						smb.setText("fire");
						smb.setImageView(null);
						break;
					case 4:
						smb.setText("long text");
						smb.setImageView(null);
						break;
				}
				textNumber++;
				if(textNumber > 4)
					textNumber = 0;
				
				
			}
			 
		 });
		 
		 Button imgButton = new Button("setImage");
		 imgButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) 
				{
					
					switch(imgNumber)
					{
						case 0:
							smb.setImageView(ImageLoader.getImageFromIconFolder("hi_keineBewegung"));
							smb.setText(null);
							break;
						case 1:
							smb.setImageView(ImageLoader.getImageFromIconFolder("hi_bewegung"));
							smb.setText(null);
							break;
						case 2:
							smb.setImageView(ImageLoader.getImageFromIconFolder("hi_schulterZucken"));
							smb.setText(null);
							break;
						case 3:
							smb.setImageView(ImageLoader.getImageFromIconFolder("hi_schloss"));
							smb.setText(null);
							break;
						case 4:
							smb.setImageView(ImageLoader.getImageFromIconFolder("hi_temp"));
							smb.setText(null);
							break;
					
					}
					imgNumber++;
					if(imgNumber > 4)
						imgNumber = 0;
					
					
					
					
					
					
				}
				 
			 });
		 Label textColorLabel =  new Label("text color:");
		 textColorLabel.setStyle("-fx-text-fill: #FFFFFF");
		 
		 
		 final ColorPicker textColorPicker = new ColorPicker();
		 textColorPicker.setValue(Color.web("#0096ff"));
		 textColorPicker.setOnAction(new EventHandler<ActionEvent>() {
	            @Override 
	            public void handle(ActionEvent e)
	            {
	            	smb.getContentRegion().setTextColor(textColorPicker.getValue());
	            	smb.getContentRegion().setMouseReleased();
	            }
	        });
		 
		 
		
		 Label textPressedColorLabel = new Label("pressed color:");
		 textPressedColorLabel.setStyle("-fx-text-fill: #FFFFFF");
		 
		 final ColorPicker pressedColorPicker = new ColorPicker();
		 pressedColorPicker.setValue(Color.web("#0074c5"));
		 pressedColorPicker.setOnAction(new EventHandler<ActionEvent>() {
	            @Override 
	            public void handle(ActionEvent e)
	            {
	            	smb.getContentRegion().setTextColorPressed(pressedColorPicker.getValue());
	            	
	            }
	        });
		 
		 
	     
		 Button reset = new Button("resetView");
		 reset.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					smb.setInlayFill(Color.web("#707070"));
					smb.setImageView(null);
					smb.setText(null);
				}
				 
			 });
	     
	     
	     vBox.getChildren().addAll(colorPicker, textButton, imgButton, textColorLabel , textColorPicker,
	    		 textPressedColorLabel, pressedColorPicker , reset);
	     
	     pane.setRight(vBox);
	    
		
	     Scene scene = new Scene(pane);
	    
		 stage.setTitle("Single metal button");
		 stage.setScene(scene);
		 stage.setWidth(500);
		 stage.setHeight(350);
		 stage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
