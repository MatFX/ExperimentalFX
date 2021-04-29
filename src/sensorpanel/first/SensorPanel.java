package sensorpanel.first;

import java.util.HashMap;

import control.button.combined.CombinedThreeButtonControl.StopIndizes;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class SensorPanel extends Region
{
	//startsize war w: 150 h: 60
	private double w = 150, h = 60;
	
	private Rectangle base_background_component, base_background_shine, base_background_inlay,
		base_background_inlay_shine, frame_component;	
	
	private Polygon frame_left_highlight;
	
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	public enum StopIndizes
	{
		BASE_BACKGROUND_SHINE,
		BASE_BACKGROUND_INLAY_SHINE,
		FRAME_LEFT_HIGHLIGHT,
		
		
		
		;
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
		
		
		base_background_inlay_shine = new Rectangle();
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#1d1d1b", 0.3)),
				new Stop(0.37573, Color.web("#afafaf", 0.36746)),
				new Stop(0.54749, Color.web("#f6f6f6", 0.4)),
				new Stop(0.69898, Color.web("#b6b6b5", 0.42952)),
				new Stop(1.0, Color.web("#1d1d1b", 0.5)),
				
		};
		stopMap.put(StopIndizes.BASE_BACKGROUND_INLAY_SHINE, stopArray);
		
		//TODO frame_component gehts weiter
		frame_component = new Rectangle();
		frame_component.setFill(Color.web("#090a0a"));
		
		frame_left_highlight = new Polygon();
		
		stopArray = new Stop[]{
				new Stop(0.02244, Color.web("#000000CC")),
				new Stop(0.10764, Color.web("#252524BA")),
				new Stop(0.2603, Color.web("#4F4E4E9A")),
				new Stop(0.4629, Color.web("#7F7F7E70")),
				new Stop(0.70776, Color.web("#BABABA3D")),
				new Stop(0.98589, Color.web("#FCFCFC03")),
				new Stop(1.0, Color.web("#FFFFFF00"))
			};
		stopMap.put(StopIndizes.FRAME_LEFT_HIGHLIGHT, stopArray);
		
		
		
		this.getChildren().addAll(this.base_background_component, base_background_shine, base_background_inlay, base_background_inlay_shine,
				frame_component, frame_left_highlight);
		
		
	}
	
	private void resize() 
	{
		
		w = getWidth();
		h = getHeight();
		//x 0
		//y 0
		
		//w 150 100/150 * 150 = 1 = 
		//h 60 100/60 * 60 = 1 = 
		//arcW = 100/150 * 4 = 2,666666666666667 = 0.0266666666666667
		//arcH = 100/60 * 4 = 6,666666666666667 = 0.0666666666666667
		base_background_component.setWidth(w);
		base_background_component.setHeight(h);
		base_background_component.setArcHeight(w * 0.0666666666666667);
		base_background_component.setArcWidth(w * 0.0666666666666667);
		
		base_background_shine.setWidth(w);
		base_background_shine.setHeight(h);
		base_background_shine.setArcHeight(w * 0.0666666666666667);
		base_background_shine.setArcWidth(w * 0.0666666666666667);
		
		
		//x1 = 0
		//y1 = 100/60 * 30 =  0.5
		//x2 = 100/150 * 150 = 
		//y2 = 100/60 * 30 = 0.5
				
		
		LinearGradient lg = new LinearGradient(getWidth()*0.0, 
				getHeight() * 0.5, 
				getWidth(),
				getHeight() * 0.5, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.BASE_BACKGROUND_SHINE));
		
		base_background_shine.setFill(lg);
		
		//x =2 100/150 * 2 = 0.0133333333333333
		//y=2 100/60 * 2 = 0.0333333333333333
		//w=146 100/150 * 146 = 0.9733333333333333
		//h= 56 100/60 * 56 = 0.9333333333333333
		//arc rx = 4 100/60 * 4 = 6,666666666666667 = 0.0666666666666667
		base_background_inlay.setX(w * 0.0133333333333333); 
		base_background_inlay.setY(h * 0.0333333333333333);
		base_background_inlay.setWidth(w * 0.9733333333333333);
		base_background_inlay.setHeight(h * 0.9333333333333333);
		base_background_inlay.setArcWidth(w * 0.0666666666666667);
		base_background_inlay.setArcHeight(w * 0.0666666666666667);
		
		
		
		
		// <rect class="cls-4" x="2" y="2" width="146" height="56" rx="4"/>
		//x =2 100/150 * 2 = 0.0133333333333333
		//y=2 100/60 * 2 = 0.0333333333333333
		//w=146 100/150 * 146 = 0.9733333333333333
		//h= 56 100/60 * 56 = 0.9333333333333333
		//arc rx = 4 100/60 * 4 = 6,666666666666667 = 0.0666666666666667
		base_background_inlay_shine.setX(w * 0.0133333333333333); 
		base_background_inlay_shine.setY(h * 0.0333333333333333);
		base_background_inlay_shine.setWidth(w * 0.9733333333333333);
		base_background_inlay_shine.setHeight(h * 0.9333333333333333);
		base_background_inlay_shine.setArcWidth(w * 0.0666666666666667);
		base_background_inlay_shine.setArcHeight(w * 0.0666666666666667);
		
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
		//height="29.79554" 100/60 * 29,79554 = 0,4965923333333333
		frame_component.setX(w * 0.1648294);
		frame_component.setY(h * 0.1661416666666667);
		frame_component.setWidth(w * 0.6685039333333333);
		frame_component.setHeight(h * 0.4965923333333333);
		
		
		frame_left_highlight.getPoints().clear();
		
		
		//layout x = 29,5 > 100/150 * 29,5 = 0.1966666666666667 
		//layout y = 15,2191  0.2536516666666667
		
		double layout_x = w * 0.1966666666666667;
		double layout_y = h * 0.2536516666666667;
		
		//x"24.5 100/150 * 24.5
		//y 40.219 
		//x 34.5 
		//y 32.691 
		//x 34.5 
		//y 12.691 
		//x 24.5 
		//y 10.219 
		//x 24.5 
		//y 40.219"
		frame_left_highlight.getPoints().addAll(new Double[]
				{
						 w * 0.16333333333333333, h * 0.6703166666666667,
						 w * 0.23, h * 0.5448500000000001,
						 w * 0.23, h * 0.2115166666666667,
						 w * 0.16333333333333333, h * 0.17031666666666667,
						 w * 0.16333333333333333, h * 0.6703166666666667
					});

		
		
		//x1="29.5" 100/150 * 29,5 = 0.1966666666666667
		//y1="30.2191" 100/60 * 30.2191 = 0,5036516666666667
		//x2="39.5" 100/150 * 39,5 = 0,2633333333333333
		//y2="30.2191" 100/60 * 30,2191 = 0,5036516666666667
		
		lg = new LinearGradient(w * 0.1966666666666667, 
				h * 0.5036516666666667, 
				w * 0.2633333333333333,
				h * 0.5036516666666667, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.FRAME_LEFT_HIGHLIGHT));
		frame_left_highlight.setFill(Color.web("#FF0000"));
		
		
		
		
	}

}
