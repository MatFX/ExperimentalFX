package gauge.amr;


import org.controlsfx.control.RangeSlider;

import control.universaldisplay.SensorValue;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SingleAMRGauge extends Application
{
	

	@Override
	public void start(Stage stage) throws Exception {
		 BorderPane pane = new BorderPane();
	     
		 SensorValue wattValue = new SensorValue(1250D, 0D , 2500D, "W", "");
		 SensorValue kwhValue = new SensorValue(4711D, 0D, 60000D, "kWh", "");
		 
		 
		 //center of borderpane, initialize
		 AMRGauge amrGauge = new AMRGauge();
		 amrGauge.setMajorValue(wattValue);
		 amrGauge.setMinorValue(kwhValue);
		
		 pane.setCenter(amrGauge);
	        
		 VBox vBox = new VBox(2);
	     vBox.setPadding(new Insets(5, 5, 5, 5));
	        
	        //start and stop animation with random values
	        //it simulate incoming signals from the actuator
		 ToggleButton test = new ToggleButton("Start");
		 test.setOnAction(new EventHandler<ActionEvent>(){
		
			@Override
			public void handle(ActionEvent event) 
			{
				if(test.isSelected())
				{
					
					amrGauge.startAnimation();
					test.setText("Ende");
				}
				else
				{
					amrGauge.stopAnimation();
					test.setText("Start");
				}
			
			}
	    	
	    });
		 
		//range as percent
		//starting value from yellow = 90f and ending value 30f angle
		//100/180 * 30 = 16,666666 = invertiert = 100 -16,6666
		//100/180 * 90 = 50
		
		
		
		final RangeSlider hSlider = new RangeSlider(0, 100, 33.444, 83.3334);
		hSlider.setShowTickMarks(true);
		hSlider.setShowTickLabels(true);
		hSlider.setBlockIncrement(10);
		
		//listening on low and high changes
		hSlider.lowValueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				amrGauge.setNewLowPercentValue(newValue.doubleValue());
				
				
			}
			
		});
		
		hSlider.highValueProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				amrGauge.setNewHighPercentValue(newValue.doubleValue());
			}
			
		});
		
		
		 
	      
	    vBox.getChildren().addAll(test, hSlider);
	        
	    pane.setRight(vBox);
	        
	    Scene scene = new Scene(pane);
	        

	     stage.setTitle("JavaFX Single AMR Gauge");
	     stage.setScene(scene);
	     stage.setWidth(600);
	     stage.setHeight(600);
	        
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
