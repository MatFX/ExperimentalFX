package control.universaldisplay2;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import control.button.combined.CombinedThreeButtonControl;
import control.button.combined.CombinedThreeButtonControl.Command;
import control.universaldisplay.SensorValue;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tools.helper.CSSContainer;
import tools.helper.ResourceLoader;

public class StartUDC2 extends Application
{
	private UniversalDisplayControl udc;
	

	public static final int TEMPERATURE = 0;
	
	public static final int HUMIDITY = 1;
	public static final int BRIGHTNESS = 2;
	
	private HashMap<Integer, List<SensorValue>> sensorMap = new HashMap<Integer, List<SensorValue>>();
	@Override
	public void start(Stage stage) 
	{
		
		List<SensorValue> sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(25, 0, 40, "°C", "img_temperatur"));
		sensorList.add(new SensorValue(22.5, 8, 40, "°C", "", new double[]{21.5, 24.5, 30}));
		sensorMap.put(TEMPERATURE, sensorList);
		
		sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(25.4, 0, 100, "%", "img_feuchtigkeit"));
		sensorMap.put(HUMIDITY, sensorList);
		
		sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(2500, 0, 3000, "Lux", "img_helligkeit"));
		sensorMap.put(BRIGHTNESS, sensorList);
		
		
		 BorderPane pane = new BorderPane();
		 pane.setPadding(new Insets(10,10,10,10));
		 pane.setStyle("-fx-background-color: #444444");
		 //pane.setBorder(value);
		 
		 final Label kommandoLabel = new Label();
		 kommandoLabel.setStyle("-fx-text-fill: #FFFFFF");
			
		 
		 udc = new UniversalDisplayControl(sensorMap);
		 
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
	   //  scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	     
	    
	     
	     try 
	     {
			List<CSSContainer> cssList = ResourceLoader.getGlobalCSSContainerList();
			System.out.println("list size " + cssList.size());
			for(int i = 0; i < cssList.size(); i++)
			{
				System.out.println("cssList " + cssList.get(i).toString());
				
				
			}
			
			CSSContainer cssContainer = null;
			if(cssList != null && cssList.size() > 0)
				cssContainer = cssList.get(0);
			
			if(cssContainer != null)
				scene.getStylesheets().setAll(cssContainer.getUrl().toExternalForm());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	     
	    

		 stage.setTitle("UniversalDisplayControl 2");
		 stage.setScene(scene);
		 stage.setWidth(550);
		 stage.setHeight(240);
		 stage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
