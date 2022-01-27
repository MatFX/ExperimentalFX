package checkbox;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ninePatch.sample1.NinePatchElement;

public class TestCheckBox extends Application
{
	public static void main(String[] args) { 
        Application.launch(args); 
    } 

	
	@Override 
    public void start(Stage stage) 
	{
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(5,5,5,5));
	    
		CheckBox checkBox = new CheckBox();
		checkBox.setAllowIndeterminate(true);
	//	checkBox.setSelected(true);
		pane.setCenter(checkBox);


        Scene scene = new Scene(pane, 330, 100);

        stage.setTitle("Checkbox");
        stage.setScene(scene);
        stage.show();
        
        //closing event with stop animation
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
        {
            @Override
            public void handle(WindowEvent event) {
            	
            }
        });
		

	}

}
