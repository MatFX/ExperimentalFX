package sensorpanel.second;

import java.util.HashMap;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import sensorpanel.first.component.LED_Component;
import sensorpanel.first.component.LED_Component.ColorValue;
import sensorpanel.first.component.TextCanvas;
import sensorpanel.first.component.TextCanvas.TextValue;
import sensorpanel.first.component.TinyButton;
import tools.helper.UIToolBox;

public class SensorPanel extends Region
{
	//startsize war w: 150 h: 60
	private double w = 150, h = 80;
	
	private Rectangle base_background_component, base_background_shine, base_background_inlay,
		base_background_inlay_shine, frame_component, display_lcd, display_overlay,
		slider_base, slider_base_overlay;
	
	private Polygon frame_left_highlight, frame_right_highlight, frame_bottom_highlight, frame_top_highlight;
	
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	private TinyButton button_up, button_down, button_automatic;
	
	private TinyButton slider_button;
	
	private Canvas info_canvas, middle_text_canvas, left_text_canvas, right_text_canvas, lcd_text_canvas;
	
	private TextCanvas up_arrow_canvas, down_arrow_canvas, auto_canvas;
	
	private LED_Component left_led, middle_led, right_led;
	
	private SimpleStringProperty descriptionProperty = new SimpleStringProperty();
	
	private SimpleStringProperty valueProperty = new SimpleStringProperty();
	
	private SimpleObjectProperty<Command> commandProperty = new SimpleObjectProperty<Command>();
	
	private SimpleBooleanProperty isAutoProperty = new SimpleBooleanProperty();
	
	public enum LED
	{
		LEFT,
		
		MIDDLE,
		
		RIGHT;
		
	}
	
	
	public enum StopIndizes
	{
		BASE_BACKGROUND_SHINE,
		BASE_BACKGROUND_INLAY_SHINE,
		FRAME_LEFT_HIGHLIGHT, 
		FRAME_RIGHT_HIGHLIGHT,
		FRAME_BOTTOM_HIGHLIGHT,
		FRAME_TOP_HIGHLIGHT,
		DISPLAY_OVERLAY_HIGHLIGHT,
		SLIDER_BASE_OVERLAY,
		
		
		
		
		;
	}
	
	public enum Command
	{
		PREVIOUS_SENSOR_VALUE, NEXT_SENSOR_VALUE, AUTO_CHANGE,
		

		/**
		 * der wird dann von außerhalb gesetzt, damit auch das aktuelle Kommando nochmal gesetzt werden kann.
		 * <br>you need the reset as a "acknowledge" from outside.
		 */
		RESET_COMMAND;
	}
	
	
	public SensorPanel()
	{
		super();
		this.initGraphics();
		
		this.registerListener();
		
	}

	private void registerListener() {
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
		

		descriptionProperty.addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				System.out.println("newValue " + newValue);
				double refresh_w = lcd_text_canvas.getWidth();
				double refresh_h = lcd_text_canvas.getHeight();
				
				refreshLCDContent(refresh_w, refresh_h);
				
			}
			
		});
		
		valueProperty.addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				double refresh_w = lcd_text_canvas.getWidth();
				double refresh_h = lcd_text_canvas.getHeight();
				
				refreshLCDContent(refresh_w, refresh_h);
				
				
			}
			
		});
		
		
	}

	private void initGraphics() {
		

		base_background_component = new Rectangle();
		base_background_component.setFill(Color.web("#121616"));

		
		base_background_shine = new Rectangle();
		Stop[] stopArray = new Stop[]{
				new Stop(0.0, Color.web("#1d1d1b", 0.9)),
				new Stop(0.27211, Color.web("#b2b2b2", 0.2)),
				new Stop(0.31916, Color.web("#b5b5b5", 0.23227)),
				new Stop(0.39341, Color.web("#bebebe", 0.32055)),
				new Stop(0.48555, Color.web("#ccc", 0.46466)),
				new Stop(0.59098, Color.web("#e0e0e0", 0.66273)),
				new Stop(0.65642, Color.web("#ededed", 0.8)),
				new Stop(0.83862, Color.web("#7c7c7b", 0.8)),
				new Stop(1, Color.web("#1d1d1b", 0.8)),
		};
		
		stopMap.put(StopIndizes.BASE_BACKGROUND_SHINE, stopArray);
		
		base_background_inlay = new Rectangle();
		base_background_inlay.setFill(Color.web("#111110"));
		
		slider_base = new Rectangle();
		slider_base.setFill(Color.web("#000000"));
		
		slider_base_overlay = new Rectangle();
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#1d1d1b")),
				new Stop(0.2043, Color.web("#1d1d1b", 0.67331)),
				new Stop(0.63698, Color.web("#1d1d1b", 0.17312)),
				new Stop(1.0, Color.web("#1d1d1b", 0)),
		};
		stopMap.put(StopIndizes.SLIDER_BASE_OVERLAY, stopArray);
		
		slider_button = new TinyButton(28.94882, 68.51018, 150d, 80d, 3.66535,
				28.94882, 68.51018, 3.66535);
		//TODO slider_button changelistener
		
		lcd_text_canvas = new Canvas();
		
		base_background_inlay_shine = new Rectangle();
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#1d1d1b", 0.3)),
				new Stop(0.37573, Color.web("#afafaf", 0.36746)),
				new Stop(0.54749, Color.web("#f6f6f6", 0.4)),
				new Stop(0.69898, Color.web("#b6b6b5", 0.42952)),
				new Stop(1.0, Color.web("#1d1d1b", 0.5)),
				
		};
		stopMap.put(StopIndizes.BASE_BACKGROUND_INLAY_SHINE, stopArray);
		
		
		
		
		
		frame_component = new Rectangle();
		frame_component.setFill(Color.web("#090a0a"));
		
		frame_left_highlight = new Polygon();
		
		stopArray = new Stop[]{
				new Stop(0.02244, Color.web("#1d1d1bCC")),
				new Stop(0.58653, Color.web("#8e8e8d66")),
				new Stop(1, Color.web("#ffffff00"))
			};
		stopMap.put(StopIndizes.FRAME_LEFT_HIGHLIGHT, stopArray);
		
		frame_right_highlight = new Polygon();
		
		stopArray = new Stop[]{
				new Stop(0, Color.web("#ffffff00")),
				new Stop(0.41347, Color.web("#8e8e8d66")),
				new Stop(0.97756, Color.web("#1d1d1bCC"))
			};
		stopMap.put(StopIndizes.FRAME_RIGHT_HIGHLIGHT, stopArray);
		
		
		frame_bottom_highlight = new Polygon();
		stopArray = new Stop[]{
				//TOOD Ursprungs Farbe war weiß mit Deckkraft 0 % ...evtl. noch ein wenig spielen
				new Stop(0.00559, Color.web("#ffffff1A")),
				new Stop(1, Color.web("#1d1d1bCC"))
			};
		stopMap.put(StopIndizes.FRAME_BOTTOM_HIGHLIGHT, stopArray);
		
		
		frame_top_highlight = new Polygon();
		stopArray = new Stop[]{
				new Stop(0.26398, Color.web("#1d1d1bFF")),
				new Stop(0.63141, Color.web("#ffffff20"))
			};
		stopMap.put(StopIndizes.FRAME_TOP_HIGHLIGHT, stopArray);
		
		display_lcd = new Rectangle();
		//TODO color cornflower looks pretty
		display_lcd.setFill(Color.web("#7cb928"));
		
		display_overlay = new Rectangle();
		
		
		stopArray = new Stop[]{
				new Stop(0.00562, Color.web("#ffffff80")),
				new Stop(0.19031, Color.web("#ffffff5D")),
				new Stop(0.43688, Color.web("#ffffff35")),
				new Stop(0.66252, Color.web("#ffffff18")),
				new Stop(0.85762, Color.web("#ffffff06")),
				new Stop(1, Color.web("#ffffff00"))
			};
		stopMap.put(StopIndizes.DISPLAY_OVERLAY_HIGHLIGHT, stopArray);
		
		up_arrow_canvas = new TextCanvas(12.5077, 15.88609, 150d, 80d, 6d, TextValue.UP_ARROW);
		up_arrow_canvas.setMouseTransparent(true);
		
		button_up = new TinyButton(12.5077, 15.88609, 150d, 80d, 6d, 12.5077, 15.88609, 6d);
		button_up.getSimpleObjectPropertyCommand().addListener(new ChangeListener<TinyButton.Command>()
		{

			@Override
			public void changed(ObservableValue<? extends sensorpanel.first.component.TinyButton.Command> observable,
					sensorpanel.first.component.TinyButton.Command oldValue,
					sensorpanel.first.component.TinyButton.Command newValue) 
			{
				if(newValue == sensorpanel.first.component.TinyButton.Command.BUTTON_RELEASED)
				{
					commandProperty.set(Command.RESET_COMMAND);
					commandProperty.set(Command.NEXT_SENSOR_VALUE);
				}
				
				
			}
			
		});
		
		down_arrow_canvas = new TextCanvas(12.74803, 32.96063, 150d, 80d, 6d, TextValue.DOWN_ARROW);
		down_arrow_canvas.setMouseTransparent(true);
		
		button_down = new TinyButton(12.74803, 32.96063, 150d, 80d, 6d, 12.74803, 32.96063, 6d);
		button_down.getSimpleObjectPropertyCommand().addListener(new ChangeListener<TinyButton.Command>()
		{

			@Override
			public void changed(ObservableValue<? extends sensorpanel.first.component.TinyButton.Command> observable,
					sensorpanel.first.component.TinyButton.Command oldValue,
					sensorpanel.first.component.TinyButton.Command newValue) 
			{
				if(newValue == sensorpanel.first.component.TinyButton.Command.BUTTON_RELEASED)
				{
					commandProperty.set(Command.RESET_COMMAND);
					commandProperty.set(Command.PREVIOUS_SENSOR_VALUE);
				}
				
				
			}
			
		});
		
		
		auto_canvas = new TextCanvas(137.04313, 15.50814, 150d, 80D, 6d, TextValue.AUTO);
		auto_canvas.setMouseTransparent(true);
		//cx="137.04313" cy="15.50814" r="6"
		
		button_automatic = new TinyButton(137.04313, 15.50814, 150d, 80d, 6d, 137.04313, 15.50814, 6d);
		button_automatic.getSimpleObjectPropertyCommand().addListener(new ChangeListener<TinyButton.Command>()
		{

			@Override
			public void changed(ObservableValue<? extends sensorpanel.first.component.TinyButton.Command> observable,
					sensorpanel.first.component.TinyButton.Command oldValue,
					sensorpanel.first.component.TinyButton.Command newValue) 
			{
				if(newValue == sensorpanel.first.component.TinyButton.Command.BUTTON_RELEASED)
				{
					isAutoProperty.set(!isAutoProperty.get());
					if(isAutoProperty.get())
						auto_canvas.setColor(Color.web("#53ff1aAA"));
					else
						auto_canvas.resetColor();
					
					commandProperty.set(Command.RESET_COMMAND);
					commandProperty.set(Command.AUTO_CHANGE);
					
				}
		
				
			}
			
		});
		left_led = new LED_Component(38.5, 50.54331, 150d, 60d, 4.5,
				//gradient für den Border
				38.5, 55.04331, 38.5, 46.04331,
				//base_color cirle
				3.75,
				//radius RadialGradient for the shine over the color
				3.25,
				//radius color glow circle
				6);
	
		
		middle_led = new LED_Component(80.75657, 50.54226, 150D, 60D, 4.5,
				//gradient für Border
				80.75657, 55.04226, 80.75657, 46.04226,
				//base_color cirle
				3.75,
				//radius RadialGradient for the shine over the color
				3.25,
				//radius color glow circle
				6);
		
		
		right_led = new LED_Component(120D, 50.54331, 150D, 60D, 4.5,
				//gradient für den Border
				120D, 55.04331D, 120D, 46.04331D,
				//base_color cirle
				3.75,
				//radius RadialGradient for the shine over the color
				3.25,
				//radius color glow circle
				6);
		
		
		
		info_canvas = new Canvas();
		info_canvas.setMouseTransparent(true);
		left_text_canvas = new Canvas();
		left_text_canvas.setMouseTransparent(true);
		middle_text_canvas = new Canvas();
		middle_text_canvas.setMouseTransparent(true);
		right_text_canvas = new Canvas();
		right_text_canvas.setMouseTransparent(true);
		
	
		
		this.getChildren().addAll(this.base_background_component, base_background_shine, base_background_inlay, slider_base,
				slider_base_overlay, slider_button, base_background_inlay_shine, frame_component, frame_left_highlight,
				frame_right_highlight, frame_bottom_highlight, frame_top_highlight, display_lcd, lcd_text_canvas, display_overlay,
				up_arrow_canvas, button_up, down_arrow_canvas, button_down, auto_canvas, button_automatic
				
				
				
				
				
				/*
				   auto_canvas, button_automatic, left_led,  middle_led, right_led, left_text_canvas, middle_text_canvas, right_text_canvas
				*/
				);
		
		
		this.getChildren().addAll(info_canvas);
		
		
	}
	
	private void resize() 
	{
		
		w = getWidth();
		h = getHeight();
		
		//button_top.resize(w, h);
		
		//x 0
		//y 0
		
		//w 150 100/150 * 150 = 1 = 
		//h 80 100/80 * 80 = 1 = 
		//arcW = 100/150 * 4 = 2,666666666666667 = 0.0266666666666667
		//arcH = 100/80 * 4 = 5 = 0.05
		base_background_component.setWidth(w);
		base_background_component.setHeight(h);
		base_background_component.setArcHeight(w * 0.05);
		base_background_component.setArcWidth(w * 0.05);
		
		base_background_shine.setWidth(w);
		base_background_shine.setHeight(h);
		base_background_shine.setArcHeight(w * 0.05);
		base_background_shine.setArcWidth(w * 0.05);
		
		
		//x1 = 0
		//y1 = 100/80 * 40 =  0.5
		//x2 = 100/150 * 150 = 
		//y2 = 100/80 * 40 = 0.5
				
		
		LinearGradient lg = new LinearGradient(getWidth()*0.0, 
				getHeight() * 0.5, 
				getWidth(),
				getHeight() * 0.5, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.BASE_BACKGROUND_SHINE));
		
		base_background_shine.setFill(lg);
		
		//x =2 100/150 * 2 = 0.0133333333333333
		//y=2 100/80 * 2.66667 = 0.0333333333333333
		//w=146 100/150 * 146 = 0.9733333333333333
		//h= 56 100/80 * 74.66667 = 0.9333333333333333
		//arc rx = 4 100/80 * 4 = 6,666666666666667 = 
		base_background_inlay.setX(w * 0.0133333333333333); 
		base_background_inlay.setY(h * 0.033333375);
		base_background_inlay.setWidth(w * 0.9733333333333333);
		base_background_inlay.setHeight(h * 0.933333375);
		base_background_inlay.setArcWidth(w * 0.026666666666666665);
		base_background_inlay.setArcHeight(w * 0.026666666666666665);
		
		
		slider_base.setX(w * 0.16333333333333333);
		slider_base.setY(h * 0.808438125);
		slider_base.setWidth(w * 0.6733333333333333);
		slider_base.setHeight(h * 0.09587837500000002);
		slider_base.setArcWidth(w * 0.02);
		slider_base.setArcHeight(w * 0.02);
		
		slider_base_overlay.setX(w * 0.16504379999999996);
		slider_base_overlay.setY(h * 0.808438125);
		slider_base_overlay.setWidth(w * 0.6733333333333333);
		slider_base_overlay.setHeight(h * 0.09587837500000002);
		slider_base_overlay.setArcWidth(w * 0.02);
		slider_base_overlay.setArcHeight(w * 0.02);
		
		
		
		//x1="75.26704" 100/150 * 75.26704 = 0,5017802666666667
		//y1="72.47088" 100/80 * 72.47088 = 0,905886
		//x2="75.2461" 100/150 * 75.2461 = 0,5016406666666667
		//y2="64.54949" 100/80 * 64.54949 =  0,806868625
		lg = new LinearGradient(w * 0.5017802666666667,
				h * 0.905886,
				w * 0.5016406666666667,
				h * 0.806868625,
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.SLIDER_BASE_OVERLAY));
		slider_base_overlay.setFill(lg);
		
		
		//<rect x="2" y="2.66667" width="146" height="74.66667" rx="4"
		base_background_inlay_shine.setX(w * 0.013333333333333332);
		base_background_inlay_shine.setY(h * 0.033333375);
		base_background_inlay_shine.setWidth(w * 0.9733333333333333);
		base_background_inlay_shine.setHeight(h * 0.933333375);
		base_background_inlay_shine.setArcWidth(w * 0.026666666666666665);
		base_background_inlay_shine.setArcHeight(w * 0.026666666666666665);
		
		//x1="75.07833" 100/150 * 75.07833 = 0.5005222 
		//y1="59.62849" 100/60 * 59.62849 = 0.9938081666666667
		//x2="74.92071" 100/150 *74.92071 = 0.4994714
		//y2="0.00715" 100/60 * 0.00715 = 0.000119166666666667
		
		lg = new LinearGradient(w * 0.5005222,
				h * 0.9938081666666667,
				w * 0.4994714,
				h * 0.000119166666666667,
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.BASE_BACKGROUND_INLAY_SHINE));
		base_background_inlay_shine.setFill(lg);
		
		//x="24.72441" 100/150 * 24.72441 = 60 = 0.1648294
		//y="9.9685" 100/60 * 9.9685 = 0.1661416666666667
		
		
		//width="100.27559" 100/150 * 100.27559 = 0.6685039333333333
		//height="29.79554" 100/80 * 29,79554 = 0,4965923333333333
		//x="" 100/150 * 24.72441  = 0,1648294
		//y="" 100/80 * 9.86846 = 0,12335575
		//width="" 100/150 * 100.27559 =  0,6685039333333333
		//height="" 100/80 * 29.49651 = 0,368706375
		frame_component.setX(w * 0.1648294);
		frame_component.setY(h *  0.12335575);
		frame_component.setWidth(w * 0.6685039333333333);
		frame_component.setHeight(h * 0.368706375);
		
		
		frame_left_highlight.getPoints().clear();
		frame_left_highlight.getPoints().addAll(new Double[]
		{
						 w * 0.16333333333333333, h * 0.49768749999999995,
						 w * 0.23, h * 0.4045375,
						 w * 0.23, h * 0.15705,
						 w * 0.16333333333333333, h * 0.12646250000000003,
						 w * 0.16333333333333333, h * 0.49768749999999995
		});

		
		//x1="24.5" 100/150 * 24.5 = 0,1633333333333333
		//y1="25.2191" 100/60 = 25.2191 = 0,4203183333333333
		//x2="34.5" 100/150 * 34,5 = 0,23
		//y2="25.2191" 100/60 * 25,2191 = 0.4203183333333333
		
		
		//x1="24.5" 
		//y1="24.966" 100/80 * 24.966 = 0.312075
		//x2="34.5"
		//y2="24.966" 100/80 * 24.966 = 0.312075
		
	
		lg = new LinearGradient(w * 0.1633333333333333, 
				h * 0.312075, 
				w * 0.2633333333333333,
				h * 0.312075, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.FRAME_LEFT_HIGHLIGHT));
		frame_left_highlight.setFill(lg);
		
		
		frame_right_highlight.getPoints().clear();
		frame_right_highlight.getPoints().addAll(new Double[]
				{
						 w * 0.77, h * 0.4045375,
						 w * 0.8366666666666666, h * 0.49768749999999995,
						 w * 0.8366666666666666, h * 0.12646250000000003,
						 w * 0.77, h * 0.15705,
						 w * 0.77, h * 0.4045375
				}
		);
		
		//x1="115.5" 100/150 * 115.5 = 0.77
		//y1="25.2191"  100/60 * 25,2191 = 0.4203183333333333
		//x2="125.5" 100/150 * 125,5 = 0.8366666666666667
		//y2="25.2191" 100/60 * 25,2191 = 0.4203183333333333
		
		//x1="115.5" 
		//y1="24.966" 100/80 * 24.966 = 0.312075
		//x2="125.5" 
		//y2="24.966" 100/80 * 24.966 = 0.312075
		lg = new LinearGradient(w * 0.77, 
				h * 0.312075, 
				w * 0.8366666666666667,
				h * 0.312075, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.FRAME_RIGHT_HIGHLIGHT));
		frame_right_highlight.setFill(lg);
		
		frame_bottom_highlight.getPoints().clear();
		frame_bottom_highlight.getPoints().addAll(new Double[]
				{
						 w * 0.2333333333333333, h * 0.40508749999999993,
						 w * 0.16666666666666663, h * 0.49825,
						 w * 0.8333333333333333, h * 0.49825,
						 w * 0.7666666666666666, h * 0.40508749999999993,
						 w * 0.2333333333333333, h * 0.40508749999999993
				}
		);
		
		
		//x1="75" 100/150 * 75 =  0.5
		//y1="33.34891" 100/60 * 33.34891 = 0.5558301666666667
		//x2="75" 100/150 * 75 =  0.5 
		//y2="46.38965"  100/60 * 46.38965 = 0.7731608333333333
		//x1="75" 
		//y1="33.01422" 100/80 = 0,41267775
		//x2="75"
		//y2="45.92408" 100/80 = 0,574051
		lg = new LinearGradient(w * 0.5, 
				h * 0.41267775, 
				w * 0.5,
				h * 0.574051, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.FRAME_BOTTOM_HIGHLIGHT));
		frame_bottom_highlight.setFill(lg);
		
		
		
		frame_top_highlight.getPoints().clear();
		frame_top_highlight.getPoints().addAll(new Double[]
				{
						 w * 0.16666666666666663, h * 0.12646250000000003,
						 w * 0.2333333333333333, h * 0.15705,
						 w * 0.7666666666666666, h * 0.15705,
						 w * 0.8333333333333333, h * 0.12646250000000003,
						 w * 0.16666666666666663, h * 0.12646250000000003
				}
		);
		
		//x1="75" 100/150 * 75 =  0.5
		//y1="7.29758" 100/60 * 7.29758 = 0.1216263333333333
		//x2="75" 100/150 * 75 =  0.5
		//y2="16.81263" 100/60 * 16.81263 = 0,2802105
		lg = new LinearGradient(w * 0.5, 
				h * 0.1216263333333333, 
				w * 0.5,
				h * 0.2802105, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.FRAME_TOP_HIGHLIGHT));
		frame_top_highlight.setFill(lg);
		
		
		//x="34.347" 100/150 * 34,347 = 0.22898
		//y="12.85039"  100/60 * 12.85039 = 0.2141731666666667
		//width="80.33016"  100/150 * 80,33016 = 0.5355344
		//height="20.16535" 100/60 * 20,16535 = 0.3360891666666667
		
		//x="34.347" 
		//y="" 100/80 * 12.72143 = 0,159017875
		//width="80.33016" 
		//height="" 100/80 * 19.96297 = 0,249537125
		display_lcd.setX(w * 0.22898);
		display_lcd.setY(h * 0.159017875);
		display_lcd.setWidth(w * 0.5355344);
		display_lcd.setHeight(h * 0.249537125);
		
		//Werte müssen gesichert werden damit beim stpäteren zeichnen erstmal die alten Werte gelöscht werden können.
		double refresh_w = lcd_text_canvas.getWidth();
		double refresh_h = lcd_text_canvas.getHeight();
		
		lcd_text_canvas.relocate(w * 0.22898, h * 0.159017875);
		lcd_text_canvas.setWidth(w * 0.5355344);
		lcd_text_canvas.setHeight(h * 0.249537125);

		
		refreshLCDContent(refresh_w, refresh_h);
		
		
		
		//x="34.347" 100/150 * 34,347 = 0.22898
		//y="12.85039" 100/60 * 12.85039 = 0.2141731666666667
		//width="80.33016"  100/150 * 80,33016 = 0.5355344
		//height="20.16535"100/60 * 20,16535 = 0.3360891666666667
		
		// y="12.85039" 100/80 * 12.85039 = 
		//height="20.16535" 100/80 * 20.16535 = 0,25204375
		display_overlay.setX(w * 0.22898);
		display_overlay.setY(h * 0.160629875);
		display_overlay.setWidth(w * 0.5355344);
		display_overlay.setHeight(h * 0.25204375);
		
		
		//x1="74.51208" 100/150  * 74.51208 = 0.4967472
		//y1="6.73968" 100/60 * 6.73968 = 0.112328
		//x2="74.51208" 100/150 * 74.51208 = 0.4967472
		//y2="46.15408" 100/60 * 46.15408 = 0,7692346666666667
		
		//x1="74.51208" 
		//y1="6.73968" 100/80 * 6.73968 = 0,084246
		//x2="74.51208" 
		//y2="46.15408" 100/80 * 46.15408 = 0,576926
		
		lg = new LinearGradient(w * 0.4967472, 
				h * 0.084246, 
				w * 0.4967472,
				h * 0.576926, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.DISPLAY_OVERLAY_HIGHLIGHT));
		display_overlay.setFill(lg);
		
		
		slider_button.setResizeValues(w, h);
		
		up_arrow_canvas.setResizeValues(w, h);
		button_up.setResizeValues(w, h);
		
		down_arrow_canvas.setResizeValues(w, h);
		button_down.setResizeValues(w, h);
		
		auto_canvas.setResizeValues(w, h);
		button_automatic.setResizeValues(w, h);

		
		left_led.setResizeValues(w, h);
		middle_led.setResizeValues(w, h);
		right_led.setResizeValues(w, h);
		
		
		
		
		//X = cx - radius - gap - width
		//Y = cy - radius
		
		double radius_w_ratio = 100D/150D * 6D / 100D;
		double radius_h_ratio = 100D/60D * 6D / 100D;
		double gap_w_ratio = 100D/150D * 4D / 100D;
		
		double left_cx_ratio = 100D/150D * 38.5 / 100D;
		double left_cy_ratio = 100D / 60D * 50.54331 / 100D;
		
		double mid_cx_ratio = 100D/150D * 80.75657 / 100D;
		double mid_cy_ratio = 100D/60D * 50.54226 / 100D;
		
		double right_cx_ratio = 100D / 150D * 120D / 100D;
		double right_cy_ratio = 100D / 60D * 50.54331 / 100D; 
		
		
		drawLEDText(radius_w_ratio, radius_h_ratio, gap_w_ratio, left_cx_ratio, left_cy_ratio, left_text_canvas, "X");
		drawLEDText(radius_w_ratio, radius_h_ratio, gap_w_ratio, mid_cx_ratio, mid_cy_ratio, middle_text_canvas, "Y");
		drawLEDText(radius_w_ratio, radius_h_ratio, gap_w_ratio, right_cx_ratio, right_cy_ratio, right_text_canvas, "Z");
		
		
		
		
		
		//x 24,5689 100/150 * 24,5689 = 0,16379266666666666666666666666667
		//y 2,7, 100/60 * 2,7 = 0,05
		//w 100,04888 = 100/150 * 100,0488 = 0,666992
		//h 7,0005 = 100/60 * 7,0005 = 0,11675
		GraphicsContext gc = info_canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, info_canvas.getWidth(), info_canvas.getHeight());
		info_canvas.relocate(w * 0.16379266666666666666666666666667, h * 0.045);
		info_canvas.setWidth(w * 0.666992);
		info_canvas.setHeight(h * 0.11675);
		
		
		
		
		Font font = Font.font("Verdana", FontWeight.BOLD ,12);
		//TODO variable
		String stringText = "Monet; Die japanische Brücke";
		Bounds maxTextAbmasse = UIToolBox.getMaxTextWidth(font, stringText);
		double tempSize;
		if(maxTextAbmasse.getWidth() < info_canvas.getWidth()  && maxTextAbmasse.getHeight() < info_canvas.getHeight())
		{
			 tempSize = UIToolBox.getGreaterFont(font.getSize()+1, info_canvas.getWidth(), info_canvas.getHeight(), stringText, 0.01, font);
		}
		else
		{
			 tempSize = UIToolBox.getLesserFont(font.getSize(), info_canvas.getWidth(), info_canvas.getHeight(), stringText,  0.01, font);
		}
		
		font = Font.font(font.getName(), tempSize);
		gc.setFill(Color.web("#FFFFFF80"));
		
		gc.setFont(font);
		
		Text testText = new Text();
		testText.setText(stringText);
		testText.setFont(font);
		
		double masseinheitX = info_canvas.getWidth() - (testText.getLayoutBounds().getWidth());// + (gaugeSize * 0.015635));
		
		double haelfte =  testText.getLayoutBounds().getHeight() / 2d;
		double masseinheitY =  info_canvas.getHeight()/2d +  (haelfte/2d);
		gc.fillText(testText.getText(), masseinheitX, masseinheitY);
	
	}
	
	//TODO param nicht benötigt?
	private void refreshLCDContent(double previous_w, double previous_h) 
	{
		GraphicsContext gcLcd = lcd_text_canvas.getGraphicsContext2D();
		gcLcd.clearRect(0, 0, previous_w, previous_h);

		Font fontLcd = Font.font("Verdana", 10);
		Bounds maxTextAbmasseLCD = UIToolBox.getMaxTextWidth(fontLcd, descriptionProperty.get());
		double tempSizeLCD;
		if(maxTextAbmasseLCD.getWidth() < lcd_text_canvas.getWidth()*0.97  && maxTextAbmasseLCD.getHeight() < lcd_text_canvas.getHeight() *0.33)
		{
			tempSizeLCD = UIToolBox.getGreaterFont(fontLcd.getSize()+1, lcd_text_canvas.getWidth()*0.97, lcd_text_canvas.getHeight()*0.33, descriptionProperty.get(), 0.01, fontLcd);
		}
		else
		{
			tempSizeLCD = UIToolBox.getLesserFont(fontLcd.getSize(), lcd_text_canvas.getWidth()*0.97, lcd_text_canvas.getHeight()*0.33, descriptionProperty.get(),  0.01, fontLcd);
		}
		
		fontLcd = Font.font(fontLcd.getName(), tempSizeLCD);
		gcLcd.setFill(Color.web("#333333"));
		gcLcd.setFont(fontLcd);
		Text descriptionText = new Text();
		descriptionText.setText(descriptionProperty.get());
		descriptionText.setFont(fontLcd);
		
		double masseinheitXLCD = lcd_text_canvas.getWidth()*0.97 - (descriptionText.getLayoutBounds().getWidth());
		gcLcd.fillText(descriptionText.getText(), masseinheitXLCD, descriptionText.getLayoutBounds().getHeight());
		
		
		maxTextAbmasseLCD = UIToolBox.getMaxTextWidth(fontLcd, valueProperty.get());
		if(maxTextAbmasseLCD.getWidth() < lcd_text_canvas.getWidth()*0.97  && maxTextAbmasseLCD.getHeight() < lcd_text_canvas.getHeight() *0.6)
		{
			tempSizeLCD = UIToolBox.getGreaterFont(fontLcd.getSize()+1, lcd_text_canvas.getWidth()*0.97, lcd_text_canvas.getHeight()*0.6, valueProperty.get(), 0.01, fontLcd);
		}
		else
		{
			tempSizeLCD = UIToolBox.getLesserFont(fontLcd.getSize(), lcd_text_canvas.getWidth()*0.97, lcd_text_canvas.getHeight()*0.6, valueProperty.get(),  0.01, fontLcd);
		}
		
		fontLcd = Font.font(fontLcd.getName(), tempSizeLCD);
		gcLcd.setFill(Color.web("#333333"));
		gcLcd.setFont(fontLcd);
		Text valueText = new Text();
		valueText.setText(valueProperty.get());
		valueText.setFont(fontLcd);
		masseinheitXLCD = lcd_text_canvas.getWidth()*0.97 - (valueText.getLayoutBounds().getWidth());
		
		gcLcd.fillText(valueText.getText(), masseinheitXLCD,  descriptionText.getLayoutBounds().getHeight() + valueText.getLayoutBounds().getHeight() );
		
		
	}

	private void drawLEDText(double radius_w_ratio, double radius_h_ratio, double gap_w_ratio, double mid_cx_ratio, double mid_cy_ratio, Canvas canvas_text, String textToDraw) 
	{
			
		canvas_text.setWidth(w * radius_w_ratio);
		canvas_text.setHeight(h * radius_h_ratio);
		
		double mid_text_x = (w * mid_cx_ratio) - ( w * radius_w_ratio + w * gap_w_ratio + canvas_text.getWidth());
		double mid_text_y = (h * mid_cy_ratio)  - ((h * radius_h_ratio)/2D);
		
		canvas_text.relocate(mid_text_x, mid_text_y);
		
		GraphicsContext gc_text =  canvas_text.getGraphicsContext2D();
		gc_text.clearRect(0, 0, canvas_text.getWidth(), canvas_text.getHeight());
		
		Font font = Font.font("Verdana", FontWeight.BOLD ,12);
		
		Bounds maxTextAbmasse = UIToolBox.getMaxTextWidth(font, textToDraw);
		double tempSize;
		
		if(maxTextAbmasse.getWidth() < canvas_text.getWidth()  && maxTextAbmasse.getHeight() < canvas_text.getHeight())
		{
			 tempSize = UIToolBox.getGreaterFont(font.getSize()+1, canvas_text.getWidth(), canvas_text.getHeight(), textToDraw, 0.01, font);
		}
		else
		{
			  tempSize = UIToolBox.getLesserFont(font.getSize(), canvas_text.getWidth(), canvas_text.getHeight(), textToDraw,  0.01, font);
		}
		
		font = Font.font(font.getName(), FontWeight.BOLD, tempSize);
		gc_text.setFill(Color.web("#FFFFFFA0"));
		
		gc_text.setFont(font);
		
		Text midText = new Text();
		midText.setText(textToDraw);
		midText.setFont(font);
		
		double masseinheitX = canvas_text.getWidth() - (midText.getLayoutBounds().getWidth());// + (gaugeSize * 0.015635));
		
		double haelfte =  midText.getLayoutBounds().getHeight() / 2d;
		double masseinheitY =  canvas_text.getHeight()/2d +  (haelfte/2d);
		gc_text.fillText(midText.getText(), masseinheitX, masseinheitY);
		
	}
	
	public void setSelectedColor(LED led, ColorValue colorValue)
	{
		switch(led)
		{
			case LEFT:
				left_led.setSelectedColor(colorValue);
				break;
			case MIDDLE:
				middle_led.setSelectedColor(colorValue);
				break;
			case RIGHT:
				right_led.setSelectedColor(colorValue);
				break;
		
		}
	}

	/**
	 * for listing the commands
	 * @return
	 */
	public SimpleObjectProperty<Command> getCommandProperty() 
	{
		return this.commandProperty;
	}
	
	public SimpleStringProperty getDescriptionProperty()
	{
		return descriptionProperty;
	}
	
	public SimpleStringProperty getValueProperty()
	{
		return valueProperty;
	}
	
	public SimpleBooleanProperty getAutoProperty()
	{
		return isAutoProperty;
	}
	
	
	
}
