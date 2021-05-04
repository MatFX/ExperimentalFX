package sensorpanel.first.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class TinyButtonText extends Region
{
	
	private Circle button_circle;
	
	private Canvas text;
	
	private double cx_ratio, cy_ratio, radius_ratio_w, radius_ratio_h;
	

	public static void main (String[] args)
	{
		System.out.println("textValue " + TextValue.UP_ARROW.getContent());
		System.out.println("textValue " + TextValue.DOWN_ARROW.getContent());
		System.out.println("textValue " + TextValue.AUTO.getContent());
	}
	
	public enum TextValue
	{
		UP_ARROW("\u21E7"),
		
		DOWN_ARROW("\u21E9"),
		
		AUTO("A"),
		
		
		
		;
		
		
		private String content;
		
		private TextValue(String content)
		{
			this.content = content;
		}
		
		public String getContent()
		{
			return content;
		}
	}
	
	public TinyButtonText(double start_cx, double start_cy, double start_w, double start_h, double radius, TextValue textValue)
	{
		super();
		
		cx_ratio = 100 / start_w * start_cx / 100D;
		
		cy_ratio = 100 / start_h * start_cy / 100D;
		
		radius_ratio_w = 100 / start_w * radius / 100D;
		
		radius_ratio_h = 100 / start_h * radius / 100D;
		
		
		
		
		initGraphics();
		
		registerListener();
		
	}

	private void registerListener() 
	{
		// TODO Auto-generated method stub
		
	}

	private void initGraphics() 
	{
		button_circle = new Circle();
		button_circle.setFill(Color.CORNSILK);
		this.getChildren().add(button_circle);
		
	}

	public void setResizeValues(double w, double h) 
	{
		this.setStyle("-fx-background-color: #FF000080");
		System.out.println("new w " + w * radius_ratio_w);
		this.setWidth(w * radius_ratio_w);
		//this.setMaxWidth(w * radius_ratio_w );
		//this.setPrefWidth(w * radius_ratio_w );
		//this.setMinWidth(w * radius_ratio_w );
	
		this.setHeight(h * radius_ratio_h);
		
		//this.setPrefHeight(this.getHeight());
		//this.setMinHeight(h * radius_ratio_h);
		//this.setPrefHeight(this.getHeight());
		//this.setMaxHeight(h * radius_ratio_h);
		double x = (w * this.cx_ratio) - (w * radius_ratio_w);
		System.out.println("w " + w * this.cx_ratio);
		System.out.println("x " + x);
		this.setLayoutX(x);
		double y = (h * this.cy_ratio) - (h * radius_ratio_h);
		System.out.println("y " + y);
		this.setLayoutY(y);
	
		
		button_circle.setCenterX(this.getLayoutX() + radius_ratio_w);
		button_circle.setCenterY(this.getLayoutY() + radius_ratio_w);
		button_circle.setRadius(w * radius_ratio_w);
		
		
		
		
		
	}
	
	
	
	

}
