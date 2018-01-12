package control.universaldisplay2;

import java.util.HashMap;
import java.util.List;

import control.button.single.metal.SingleMetalButton;
import control.button.single.metal.SingleMetalButton.Command;
import control.universaldisplay.SensorValue;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;


public class UniversalDisplayControl extends GridPane
{
	private DisplayLCD displayLCD;
	
	//private CombinedThreeButtonControl combined;
	
	private SingleMetalButton smbViewLeft, smbAutoView, smbViewRight;
	
	private SingleMetalButton plus, minus, sendValue;
	
	private SingleMetalButton presetLeft, presetRight, sendPreset;
	
	private Color textColor = Color.web("#4d4d4d");
	
	private Color textPressedColor = Color.web("#333333");
	
	private HashMap<Integer, List<SensorValue>> sensorMap;
	
	
	public UniversalDisplayControl(HashMap<Integer, List<SensorValue>> sensorMap)
	{
		super();
		this.sensorMap = sensorMap;
		this.initGraphics();
		//TODO weiß nicht
		this.registerListener();
		this.resize();
		
		
		registerCommandListener();
		
	}

	private void resize() 
	{
		double w = this.getWidth();
		double h = this.getHeight();
		this.setWidth(w);
		this.setHeight(h);
		
	}

	private void registerListener() 
	{
	//	widthProperty().addListener(observable -> resize());
	//	heightProperty().addListener(observable -> resize());
	}

	private void initGraphics() 
	{

		//TODO raus
		this.setGridLinesVisible(true);
	
		
		this.setPadding(new Insets(10, 10, 10, 10));
		this.setHgap(10);
		this.setVgap(10);
		this.setPrefWidth(75);
		
		displayLCD = new DisplayLCD();
		
		displayLCD.setMajorValue(sensorMap.get(StartUDC2.TEMPERATURE).get(0));
		displayLCD.setMinorValue(sensorMap.get(StartUDC2.TEMPERATURE).get(1));
		displayLCD.repaintValues();
		//wir benötigen ein Display in der linken hälfte 
		
		//auf der rechten seite werden die Buttons abgelegt
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHgrow(Priority.ALWAYS);
		col1.setPercentWidth(75);
		ColumnConstraints col2 = new ColumnConstraints();
		
		RowConstraints rc = new RowConstraints();
		rc.setVgrow(Priority.ALWAYS);
		
		this.getColumnConstraints().addAll(col1, col2);
		
		
		
		
		this.getRowConstraints().addAll(rc);
		this.add(displayLCD, 0, 0, 1, 4); 
		
		
		
		smbViewLeft = new SingleMetalButton();
		smbViewLeft.getContentRegion().setTextColor(textColor);
		smbViewLeft.getContentRegion().setTextColorPressed(textPressedColor);
		smbViewLeft.setText("<");
		smbViewLeft.setMinWidth(38);
		smbViewLeft.setMinHeight(38);
		
		smbAutoView = new SingleMetalButton();
		smbAutoView.getContentRegion().setTextColor(textColor);
		smbAutoView.getContentRegion().setTextColorPressed(textPressedColor);
		smbAutoView.setText("A");
		smbAutoView.setMinWidth(38);
		smbAutoView.setMinHeight(38);
		
		smbViewRight = new SingleMetalButton();
		smbViewRight.getContentRegion().setTextColor(textColor);
		smbViewRight.getContentRegion().setTextColorPressed(textPressedColor);
		smbViewRight.setText(">");
		smbViewRight.setMinWidth(38);
		smbViewRight.setMinHeight(38);
		
		this.add(smbViewLeft, 1, 1, 1, 1);
		this.add(smbAutoView, 2,  1, 1, 1);
		this.add(smbViewRight, 3, 1, 1, 1);
		
		
		plus = new SingleMetalButton();
		plus.getContentRegion().setTextColor(textColor);
		plus.getContentRegion().setTextColorPressed(textPressedColor);
		plus.setText("+");
		plus.setMinWidth(38);
		plus.setMinHeight(38);
		
		
		sendValue = new SingleMetalButton();
		sendValue.getContentRegion().setTextColor(textColor);
		sendValue.getContentRegion().setTextColorPressed(textPressedColor);
		sendValue.setText("°");
		sendValue.setMinWidth(38);
		sendValue.setMinHeight(38);
		
		minus = new SingleMetalButton();
		minus.getContentRegion().setTextColor(textColor);
		minus.getContentRegion().setTextColorPressed(textPressedColor);
		minus.setText("-");
		minus.setMinWidth(38);
		minus.setMinHeight(38);
		
		this.add(plus, 1, 2, 1, 1);
		this.add(sendValue, 2, 2, 1, 1);
		this.add(minus, 3, 2, 1, 1);
		
		presetLeft = new SingleMetalButton();
		presetLeft.getContentRegion().setTextColor(textColor);
		presetLeft.getContentRegion().setTextColorPressed(textPressedColor);
		presetLeft.setText("<");
		presetLeft.setMinWidth(38);
		presetLeft.setMinHeight(38);
		
		
		presetRight = new SingleMetalButton();
		presetRight.getContentRegion().setTextColor(textColor);
		presetRight.getContentRegion().setTextColorPressed(textPressedColor);
		presetRight.setText(">");
		presetRight.setMinWidth(38);
		presetRight.setMinHeight(38);
		
		
		sendPreset = new SingleMetalButton();
		sendPreset.getContentRegion().setTextColor(textColor);
		sendPreset.getContentRegion().setTextColorPressed(textPressedColor);
		sendPreset.setText("°");
		sendPreset.setMinWidth(38);
		sendPreset.setMinHeight(38);
		
		

		this.add(presetLeft, 1, 3, 1, 1);
		this.add(sendPreset, 2, 3, 1, 1);
		this.add(presetRight, 3, 3, 1, 1);
		
	}

	private void registerCommandListener() 
	{
		smbViewLeft.getCommandProperty().addListener(new ChangeListener<Command>()
		{

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) 
			{
				System.out.println("view nach links");
			}
			
		});
		
		smbAutoView.getCommandProperty().addListener(new ChangeListener<Command>()
		{

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) 
			{
				System.out.println("view wechselt automatisch");
			}
			
		});
		
		
		smbViewRight.getCommandProperty().addListener(new ChangeListener<Command>()
		{

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) 
			{
				System.out.println("view nach rechts");
			}
			
		});
		
		plus.getCommandProperty().addListener(new ChangeListener<Command>()
		{

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) 
			{
				System.out.println("sollwert erhöhen");
			}
			
		});
		
		sendValue.getCommandProperty().addListener(new ChangeListener<Command>()
		{

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) 
			{
				System.out.println("sollwert senden");
			}
			
		});
		
		minus.getCommandProperty().addListener(new ChangeListener<Command>()
		{

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) 
			{
				System.out.println("sollwert verringern");
			}
			
		});
		
		presetLeft.getCommandProperty().addListener(new ChangeListener<Command>()
		{

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) 
			{
				System.out.println("voreinstellung links");
			}
			
		});
		
		presetRight.getCommandProperty().addListener(new ChangeListener<Command>()
		{

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) 
			{
				System.out.println("voreinstellung rechts");
			}
			
		});
		
		
		sendPreset.getCommandProperty().addListener(new ChangeListener<Command>()
		{

			@Override
			public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) 
			{
				System.out.println("sende voreinstellung");
			}
			
		});
		
		
		
	}

	
}
