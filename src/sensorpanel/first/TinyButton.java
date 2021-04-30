package sensorpanel.first;


import java.util.HashMap;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import sensorpanel.first.SensorPanel.StopIndizes;

public class TinyButton extends Region
{
	
	private Circle button_top;
	
	private double radius_ratio;
	
	private double cx_ratio;
	
	private double cy_ratio;
	
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	private double cx_ratio_top_highlight;

	private double cy_ratio_top_highlight;
	
	private double radius_ratio_top_highlight;
	
	public enum StopIndizes
	{
		BUTTON_TOP_HIGHLIGHT,
		
		
		
		;
		
	}
	
	
	public TinyButton(double start_cx, double start_cy, double start_w, double start_h, double start_radius,
			double start_cx_top_highlight, double start_cy_top_highlight, double radius_top_highlight)
	{
		super();
		
		this.cx_ratio = 100D/start_w * start_cx / 100D;
		this.cy_ratio = 100D/start_h * start_cy / 100D;
		this.radius_ratio = ((100D/(start_w * start_h)) * (Math.pow(start_radius, 2) * Math.PI)) / 100D;
		
		this.cx_ratio_top_highlight = 100D/start_w * start_cx_top_highlight / 100D;
		System.out.println("> " + cx_ratio_top_highlight);
		this.cy_ratio_top_highlight = 100D/start_h * start_cy_top_highlight / 100D;
		System.out.println("> " + cy_ratio_top_highlight);
		this.radius_ratio_top_highlight = ((100D/(start_w * start_h)) * (Math.pow(radius_top_highlight, 2) * Math.PI)) / 100D;
		
		
		this.initGraphics();
	}
	
	private void initGraphics() {
		
		button_top = new Circle();
		
		Stop[] stopArray = new Stop[]{
				new Stop(0.57444, Color.web("#16141200")),
				new Stop(0.58364, Color.web("#2725241F")),
				new Stop(0.60244, Color.web("#42403f51")),
				new Stop(0.62466, Color.web("#57555577")),
				new Stop(0.65213, Color.web("#65646492")),
				new Stop(0.69069, Color.web("#6e6d6dA1")),
				new Stop(0.79771, Color.web("#706f6fA6")),
				new Stop(0.82478, Color.web("#616060B2")),
				new Stop(0.93813, Color.web("#2b2927E1")),
				new Stop(1, Color.web("#161412F2"))
			};
		stopMap.put(StopIndizes.BUTTON_TOP_HIGHLIGHT, stopArray);
		
		
		
		this.getChildren().add(button_top);
		
	}

	
	public void setResizeValues(double w, double h) 
	{
		button_top.setCenterX(w * this.cx_ratio);
		button_top.setCenterY(h * this.cy_ratio);
		
		//calculation the new area from the circle
		double circle_area = (w * h) * radius_ratio;
		//calc the new radius
		double radius = Math.sqrt(circle_area / Math.PI);
		button_top.setRadius(radius);
		
		
		double circle_area_top_highlight = (w * h) * this.radius_ratio_top_highlight;
		radius = Math.sqrt(circle_area_top_highlight / Math.PI);
		
		
		RadialGradient rg = new RadialGradient(0D, 0D, w * this.cx_ratio_top_highlight, h * this.cy_ratio_top_highlight, 
				radius, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.BUTTON_TOP_HIGHLIGHT));
		
		
		button_top.setFill(rg);
		
	
	
	}

	
}
