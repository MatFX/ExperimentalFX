package rgb;

import java.util.HashMap;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import sensorpanel.first.SensorPanel.LED;
import sensorpanel.first.component.LED_Component;
import sensorpanel.first.component.LED_Component.ColorValue;

/**
 * Vom Aufbau so ähnlich wie das SensorPanel für die Alarmierung.
 * @author m.goerlich
 *
 */
public class FourColorCorner extends Region
{
	private Rectangle base_background_component, base_background_shine, base_background_inlay, base_background_inlay_shine;
	
	private double w = 110;
	private double h = 110;
	
	private LED_Component led_left_top, led_left_bottom, led_right_top, led_right_bottom;

	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();

	public enum LED
	{
		TOP_LEFT,
		
		TOP_RIGHT,
		
		BOTTOM_LEFT,
		
		BOTTOM_RIGHT;
		
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
		
		
		
		
		;
	}
	
	public FourColorCorner()
	{
		this.initGraphics();
		this.registerListener();
	}
	
	private void initGraphics() 
	{
		base_background_component = new Rectangle();
		base_background_component.setFill(Color.web("#121616"));
		
		base_background_shine = new Rectangle();
		
		Stop[] stopArray = new Stop[]{
				new Stop(0, Color.web("#1d1d1bE6")),
				new Stop(.27211, Color.web("#b2b2b233")),
				new Stop(.31916, Color.web("#b5b5b53B")),
				new Stop(.39341, Color.web("#bdbdbd52")),
				new Stop(.48555, Color.web("#cccccc76")),
				new Stop(.59098, Color.web("#dfdfdfA9")),
				new Stop(.65642, Color.web("#edededCC")),
				new Stop(.83862, Color.web("#7c7c7aCC")),
				new Stop(1, Color.web("#1d1d1bCC"))
			};
		stopMap.put(StopIndizes.BASE_BACKGROUND_SHINE, stopArray);
		
		
		base_background_inlay = new Rectangle();
		base_background_inlay.setFill(Color.web("#111110"));
		
		base_background_inlay_shine = new Rectangle();
		
		stopArray = new Stop[]{
				new Stop(0, Color.web("#5757561A")),
				new Stop(.00982, Color.web("#5c5c5b1E")),
				new Stop(.09474, Color.web("#88888744")),
				new Stop(.18262, Color.web("#acacac63")),
				new Stop(.27296, Color.web("#c9c9c87B")),
				new Stop(.36678, Color.web("#dddddd8C")),
				new Stop(.46627, Color.web("#e9e9e996")),
				new Stop(.58101, Color.web("#ededed99")),
				new Stop(.64591, Color.web("#e7e7e795")),
				new Stop(.72494, Color.web("#d5d5d58B")),
				new Stop(.81132, Color.web("#b9b9b97A")),
				new Stop(.90232, Color.web("#92919162")),
				new Stop(.96927, Color.web("#706f6f4D"))
			};
		stopMap.put(StopIndizes.BASE_BACKGROUND_INLAY_SHINE, stopArray);
		
		
		led_left_top = new LED_Component(19.6811, 20.94882, w, h, 9.23917,
				//gradient für den Border
				19.6811, 30.18799, 19.6811, 11.70965,
				//base_color cirle
				7.69931,
				//radius RadialGradient for the shine over the color
				7.69931,
				//radius color glow circle
				12.5);
			
		led_right_top = new LED_Component(91.6811, 20.94882, w, h, 9.23917,
				//gradient border
				91.6811, 30.18799, 91.6811, 11.709656,
				//base_color cirle
				7.69931,
				//radius RadialGradient for the shine over the color
				7.69931,
				//radius color glow circle
				12.5);
	
		led_right_bottom = new LED_Component(91.6811, 86.94882, w, h, 9.23917,
				//gradient border
				91.6811, 96.18799, 91.6811, 77.70965,
				//base_color cirle
				7.69931,
				//radius RadialGradient for the shine over the color
				7.69931,
				//radius color glow circle
				12.5);
		
		led_left_bottom = new LED_Component(19.6811, 86.94882, w, h, 9.23917,
				//gradient border
				19.6811, 96.18799, 19.6811, 77.70965,
				//base_color cirle
				7.69931,
				//radius RadialGradient for the shine over the color
				7.69931,
				//radius color glow circle
				12.5);
		
		this.getChildren().addAll(base_background_component, base_background_shine, base_background_inlay, base_background_inlay_shine, 
				led_left_top, led_right_top, led_right_bottom, led_left_bottom);
	}

	private void registerListener() 
	{
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
	}
	
	
	private void resize() 
	{
		

		w = getWidth();
		h = getHeight();
		
		//w und h immer auf die volle zugewiesene Breite und Höhe
		base_background_component.setWidth(w);
		base_background_component.setHeight(h);
		//4px
		base_background_component.setArcWidth(w * 0.03636363636363636);
		base_background_component.setArcHeight(w * 0.03636363636363636);
		
		
		
		base_background_shine.setWidth(w);
		base_background_shine.setHeight(h);
		base_background_shine.setArcWidth(w * 0.03636363636363636);
		base_background_shine.setArcHeight(w * 0.03636363636363636);
		
		
		LinearGradient lg = new LinearGradient(getWidth()*0.0, 
				getHeight() *  0.5,
				getWidth() * 1.0,
				getHeight() * 0.5, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.BASE_BACKGROUND_SHINE));
		
		base_background_shine.setFill(lg);
		
		
		base_background_inlay.setX(w * 0.022727272727272724);
		base_background_inlay.setY(h * 0.022727272727272724);
		base_background_inlay.setWidth(w * 0.9545454545454546);
		base_background_inlay.setHeight(h * 0.9545454545454546);
		base_background_inlay.setArcWidth(w * 0.03636363636363636);
		base_background_inlay.setArcHeight(w * 0.03636363636363636);
		
		
		//base_background_inlay_shine.
		base_background_inlay_shine.setX(w * 0.022727272727272724);
		base_background_inlay_shine.setY(h * 0.022727272727272724);
		base_background_inlay_shine.setWidth(w * 0.9545454545454546);
		base_background_inlay_shine.setHeight(h * 0.9545454545454546);
		base_background_inlay_shine.setArcWidth(w * 0.03636363636363636);
		base_background_inlay_shine.setArcHeight(w * 0.03636363636363636);
		
		
		
		lg = new LinearGradient(getWidth()*0.5, 
				getHeight() *  0.9772727272727272,
				getWidth() * 0.5,
				getHeight()  *  0.022727272727272724, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.BASE_BACKGROUND_INLAY_SHINE));
		
		base_background_inlay_shine.setFill(lg);
		
		led_left_top.setResizeValues(w, h);
		led_left_top.setSelectedColor(ColorValue.YELLOW);
		
		led_right_top.setResizeValues(w, h);
		led_right_top.setSelectedColor(ColorValue.GREEN);
		
		led_right_bottom.setResizeValues(w, h);
		led_right_bottom.setSelectedColor(ColorValue.RED);
		
		led_left_bottom.setResizeValues(w, h);
		led_left_bottom.setSelectedColor(ColorValue.OFF);
	
	}
	
	public void setSelectedColor(LED led, ColorValue colorValue)
	{
		switch(led)
		{
			case TOP_LEFT:
				led_left_top.setSelectedColor(colorValue);
				break;
			case TOP_RIGHT:
				led_right_top.setSelectedColor(colorValue);
				break;
			case BOTTOM_LEFT:
				led_right_bottom.setSelectedColor(colorValue);
				break;
			case BOTTOM_RIGHT:
				led_left_bottom.setSelectedColor(colorValue);
				break;
		}
	}

	

}
