package sensorpanel.first.component;

import java.util.HashMap;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import tools.helper.UIToolBox;

/**
 * 
 * @author m.goerlich
 *
 */
public class LED_Component extends Region
{
	private Circle led_base_component, base_border, base_color, base_color_shine, color_glow;
	
	private double cx_ratio, bg_x1_ratio, bg_x2_ratio;
	
	private double cy_ratio, bg_y1_ratio, bg_y2_ratio;
	
	private double radius_ratio, radius_ratio_base_color, radius_ratio_color_shine, radius_ratio_color_glow_circle;
	
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	//variable to recoloring the glow
	private double last_cx_ratio_glow, last_cy_ratio_glow, last_radius_glow;
	
	
	public enum ColorValue
	{
		OFF(Color.web("#777777")),
		
		YELLOW(Color.web("#baba0b")),
		
		GREEN(Color.web("#65b32e")),
		
		RED(Color.web("#e5251c"));
		
		
		private Color color;
		
		private ColorValue(Color color)
		{
			this.color = color;
		}
		
		public Color getColor()
		{
			return this.color;
		}
	}
	
	private ColorValue selectedColor = ColorValue.OFF;
	
	
	public enum StopIndizes
	{
		BASE_BORDER_GRADIENT,
		
		BASE_COLOR_SHINE,
		
		;
	}
	
	public LED_Component(double start_cx, double start_cy, double start_w, double start_h, double start_radius,
			//color gradient for the border
			double border_gradient_x1, double border_gradient_y1, double border_gradient_x2, double border_gradient_y2,
			//radius for base-color circle; same cx/cy coordiante as base component
			double start_radius_base_color,
			//radius for the shine circle gradient over the base color
			double start_radius_color_shine,
			//radius for the glow color circle
			double start_radius_color_glow_circle,
			//
			String shortName)
	{
		super();
		this.setWidth(50);
		this.setHeight(50);
		
		this.cx_ratio = UIToolBox.getPointRatio(start_w, start_cx); 
		this.cy_ratio = UIToolBox.getPointRatio(start_h, start_cy); 
		this.radius_ratio = UIToolBox.getAreaRatio(start_w, start_h, start_radius); 
		
		this.bg_x1_ratio =  UIToolBox.getPointRatio(start_w, border_gradient_x1); 
		this.bg_y1_ratio =  UIToolBox.getPointRatio(start_h, border_gradient_y1); 
		this.bg_x2_ratio =  UIToolBox.getPointRatio(start_w, border_gradient_x2); 
		this.bg_y2_ratio =  UIToolBox.getPointRatio(start_h, border_gradient_y2); 
		
		this.radius_ratio_base_color = UIToolBox.getAreaRatio(start_w, start_h, start_radius_base_color); 
		this.radius_ratio_color_shine = UIToolBox.getAreaRatio(start_w, start_h, start_radius_color_shine); 
		this.radius_ratio_color_glow_circle = UIToolBox.getAreaRatio(start_w, start_h, start_radius_color_glow_circle);
		
		
		this.initGraphics();
	}

	
	
	
	private void initGraphics() 
	{
		led_base_component = new Circle();
		led_base_component.setFill(Color.web("#353535"));
		
		base_border = new Circle();
		Stop[] stopArray = new Stop[]{
				new Stop(0, Color.web("#ffffffCC")),
				new Stop(0.13896, Color.web("#fafafaCB")),
				new Stop(0.28628, Color.web("#eaeaeaCA")),
				new Stop(0.43744, Color.web("#d0d0d0C7")),
				new Stop(0.59126, Color.web("#abababC3")),
				new Stop(0.7472, Color.web("#7c7c7cBD")),
				new Stop(0.90262, Color.web("#434343B7")),
				new Stop(1, Color.web("#1b1b1bB3"))
			};
		stopMap.put(StopIndizes.BASE_BORDER_GRADIENT, stopArray);
		
		
		base_color = new Circle();
		//TODO Farbe muss tauschbar sein das ist die Farbe wenn aus.
		//base_color.setFill(Color.web("#777777"));
		//base_color.setFill(Color.web("#FF0000"));
		base_color.setFill(selectedColor.getColor());
		
		base_color_shine = new Circle();
		//TODO evtl. noch mal an die Werte ran
		stopArray = new Stop[]{
					new Stop(0, Color.web("#ffffffAF")),
					new Stop(0.17334, Color.web("#ffffffA4")),
					new Stop(0.33475, Color.web("#ffffffA0")),
					new Stop(0.49153, Color.web("#ffffff91")),
					//new Stop(0.64538, Color.web("#ffffff76")),
					//new Stop(0.79715, Color.web("#ffffff54")),
					new Stop(0.94534, Color.web("#ffffff2B")),
					new Stop(1, Color.web("#ffffff00"))
		};
		stopMap.put(StopIndizes.BASE_COLOR_SHINE, stopArray);
		
		
		color_glow = new Circle();
		this.getChildren().addAll(led_base_component, base_border, base_color, base_color_shine, color_glow);
	}




	public void setResizeValues(double w, double h) 
	{
		led_base_component.setCenterX(w * this.cx_ratio);
		led_base_component.setCenterY(h * this.cy_ratio);
		double radius = UIToolBox.getRadiusFromRatio(w, h, radius_ratio);
		led_base_component.setRadius(radius);
		
		//same as base Component
		base_border.setCenterX(w * this.cx_ratio);
		base_border.setCenterY(h * this.cy_ratio);
		base_border.setRadius(radius);
		
		LinearGradient lg = new LinearGradient(w * this.bg_x1_ratio, 
				h * this.bg_y1_ratio, 
				w * this.bg_x2_ratio,
				h * this.bg_y2_ratio,
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.BASE_BORDER_GRADIENT));
		base_border.setFill(lg);
		
		base_color.setCenterX(w * this.cx_ratio);
		base_color.setCenterY(h * this.cy_ratio);
		radius = UIToolBox.getRadiusFromRatio(w, h, radius_ratio_base_color);
		base_color.setRadius(radius);
	
		
		//same values as the base_color
		base_color_shine.setCenterX(w * this.cx_ratio);
		base_color_shine.setCenterY(h * this.cy_ratio);
		base_color_shine.setRadius(radius);
		
		radius = UIToolBox.getRadiusFromRatio(w, h, radius_ratio_color_shine);
		RadialGradient rg = new RadialGradient(0D, 0D, w * this.cx_ratio, h * this.cy_ratio, 
				radius, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.BASE_COLOR_SHINE));
		base_color_shine.setFill(rg);
		
		
		radius = UIToolBox.getRadiusFromRatio(w, h, radius_ratio_color_glow_circle);
		color_glow.setCenterX(w * this.cx_ratio);
		color_glow.setCenterY(h * this.cy_ratio);
		color_glow.setRadius(radius);
		
		last_cx_ratio_glow = w * this.cx_ratio;
		last_cy_ratio_glow = h * this.cy_ratio;
		last_radius_glow = radius;
		
		setColorOnComponent();
	
		
		
	}
	
	
	private void setColorOnComponent() {
		
		base_color.setFill(selectedColor.getColor());
		//Glow setzen
		if(selectedColor != ColorValue.OFF)
		{
			RadialGradient rg = new RadialGradient(0D, 0D, last_cx_ratio_glow, last_cy_ratio_glow, 
					last_radius_glow, false, CycleMethod.NO_CYCLE, getGlowForColor()
					);
			color_glow.setFill(rg);
		}
		else
		{
			//Transparente Farbe setzen
			color_glow.setFill(Color.web("#00000000"));
		}
	}




	private Stop[] getGlowForColor() 
	{
		if(selectedColor == ColorValue.YELLOW)
		{
			return new Stop[]{
					new Stop(0.04865, Color.web("#f9ea32B3")),
					new Stop(0.28125, Color.web("#f9ea31B0")),
					new Stop(0.42771, Color.web("#f9ea2fA7")),
					new Stop(0.55034, Color.web("#f8e92b98")),
					new Stop(0.65984, Color.web("#f8e92583")),
					new Stop(0.76065, Color.web("#f7e81d67")),
					new Stop(0.85502, Color.web("#f6e81345")),
					new Stop(0.94271, Color.web("#f5e7081E")),
					new Stop(1, Color.web("#f4e60000"))
				};
		}
		else if(selectedColor == ColorValue.GREEN)
		{
			return new Stop[]{
					new Stop(0.04865, Color.web("#4fae32B3")),
					new Stop(0.31094, Color.web("#4dad32AF")),
					new Stop(0.47605, Color.web("#48aa32A2")),
					new Stop(0.61431, Color.web("#3fa4338D")),
					new Stop(0.73779, Color.web("#339c346E")),
					new Stop(0.85137, Color.web("#22923447")),
					new Stop(0.95628, Color.web("#0f863517")),
					new Stop(1, Color.web("#05803600"))
				};
			
		}
		else if(selectedColor == ColorValue.RED)
		{
			return new Stop[]{
					new Stop(0.04865, Color.web("#e50b42B3")),
					new Stop(0.28787, Color.web("#e50b42B0")),
					new Stop(0.43849, Color.web("#e50b40A6")),
					new Stop(0.56467, Color.web("#e50c3e96")),
					new Stop(0.67731, Color.web("#e50c3b7F")),
					new Stop(0.78097, Color.web("#e50d3761")),
					new Stop(0.87805, Color.web("#e40e323C")),
					new Stop(0.96827, Color.web("#e40f2c11")),
					new Stop(1, Color.web("#e40f2a00"))
				};
			
		}
		return new Stop[0];
	}




	public void setSelectedColor(ColorValue selectedValue)
	{
		this.selectedColor = selectedValue;
		this.setColorOnComponent();
	}
	

	
	
	
}
