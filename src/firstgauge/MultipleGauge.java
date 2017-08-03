package firstgauge;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 25 Gauges on the screen with optional animation
 * @author m.goerlich
 *
 */
public class MultipleGauge extends Application
{
	private List<CustomCircle> testList = new ArrayList<CustomCircle>();
	

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		
		BorderPane pane = new BorderPane();
		
		//the container for the 25 gauges
		GridPane gridPane = new GridPane();
		RowConstraints row = new RowConstraints();
		row.setVgrow(Priority.ALWAYS);
		gridPane.getRowConstraints().add(0, row);
		gridPane.getRowConstraints().add(1, row);
		gridPane.getRowConstraints().add(2, row);
		gridPane.getRowConstraints().add(3, row);
		gridPane.getRowConstraints().add(4, row);
		
		ColumnConstraints col = new ColumnConstraints();
		col.setHgrow(Priority.ALWAYS);
		gridPane.getColumnConstraints().addAll(col, col, col, col, col);
		
		
		int zeile = 0, spalte = 0;
		for(int i = 0; i < 25; i++)
		{
			CustomCircle custom = new CustomCircle();
			testList.add(custom);
			gridPane.add(custom, spalte, zeile);
			spalte++;
			
			if(spalte > 4)
			{
				zeile++;
				spalte = 0;
			}
		}
		
		pane.setCenter(gridPane);
		
		//start stop animation of all gauges on screen
		ToggleButton test = new ToggleButton("Start");
        test.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) 
			{
				if(test.isSelected())
				{
					for(int i = 0; i < testList.size(); i++)
					{
						testList.get(i).startAnimation();
					}
					test.setText("Ende");
				}
				else
				{
					for(int i = 0; i < testList.size(); i++)
					{
						testList.get(i).stopAnimation();
					}
					test.setText("Start");
				}
			
			}
        	
        });
		
		pane.setRight(test);
		
		
		
		Scene scene = new Scene(pane);

		primaryStage.setTitle("JavaFX Multiple Gauge");
		primaryStage.setScene(scene);
		primaryStage.show();
        
		//window closing event with stop the animation
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
            	for(int i = 0; i < testList.size(); i++)
				{
					testList.get(i).stopAnimation();
				}
            }
        });
	}
	
	public static void main(String[] args) 
	{
	        Application.launch(args);
	}
}
