package sensorpanel.first.component;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import tools.helper.UIToolBox;

public class TextCanvas extends Canvas
{
	
	private double center_x_ratio, center_y_ratio, radius_ratio_w, radius_ratio_h;
	
	private TextValue textValue;
	
	private Color STANDARD_TEXT_COLOR = Color.web("#FFFFFFAA");
	
	private Color colorToSet = STANDARD_TEXT_COLOR;
	
	public enum TextValue
	{
		UP_ARROW("\u2B06"),
		
		DOWN_ARROW("\u2B07"),
		
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

	public TextCanvas(double center_x, double center_y, double w, double h, double radius, TextValue textValue)
	{
		super();
		this.setManaged(false);
		center_x_ratio = 100D/w * center_x / 100D;
		center_y_ratio = 100D/h * center_y / 100D;
		radius_ratio_w = 100D/w * radius / 100D;
		radius_ratio_h = 100D/h * radius / 100D;
		
		this.textValue = textValue;
	
	}
	
	
	public void setResizeValues(double w, double h)
	{
		
		double previous_w = this.getWidth();
		double previous_h = this.getHeight();
		
		
	
		//calculate the new pos and size
		double new_center_x = w * center_x_ratio;
		double new_center_y = h * center_y_ratio;
		double new_radius = w * radius_ratio_w;
		
		double new_radius_h = h * radius_ratio_h;
		//System.out.println("w rad " + new_radius + " > " + new_radius_h);
		
		this.setLayoutX(new_center_x - new_radius);  
		this.setLayoutY(new_center_y - new_radius);  
		
		//besser, aber noch nicht das gelbe vom Ei
		if(new_radius <= new_radius_h)
		{
			//Basis Seitenverhältnis wäre besser dann skaliert es auch gleichmäßiger
			this.setWidth(new_radius*2);
			this.setHeight(new_radius*2);
			this.setLayoutX(new_center_x - new_radius);  
			this.setLayoutY(new_center_y - new_radius);  
			
		}
		else
		{
			this.setWidth(new_radius_h*2);
			this.setHeight(new_radius_h*2);
			this.setLayoutX(new_center_x - new_radius_h);  
			this.setLayoutY(new_center_y - new_radius_h);  
			
		}
		refreshText(previous_w, previous_h);
	}
	
	private void refreshText(double previous_w, double previous_h) 
	{


		// delete the content
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.clearRect(0, 0, previous_w, previous_h);
		
		Font font = Font.font("Verdana", FontWeight.BOLD ,12);
		String stringText = textValue.getContent();
		Bounds maxTextAbmasse = UIToolBox.getMaxTextWidth(font, stringText);
		double tempSize;
		if(maxTextAbmasse.getWidth() < this.getWidth()  && maxTextAbmasse.getHeight() < this.getHeight())
		{
			 tempSize = UIToolBox.getGreaterFont(font.getSize()+1, this.getWidth(), this.getHeight(), stringText, 0.01, font);
		}
		else
		{
			  tempSize = UIToolBox.getLesserFont(font.getSize(), this.getWidth(), this.getHeight(), stringText,  0.01, font);
		}
		
		font = Font.font(font.getName(), FontWeight.BOLD , tempSize);
		gc.setFill(colorToSet);
		
		gc.setFont(font);
	
	
		Text testText = new Text();
		testText.setText(stringText);
		testText.setFont(font);
		
		double masseinheitX = this.getWidth()/2d - (testText.getLayoutBounds().getWidth()/2d);
		double haelfte =  testText.getLayoutBounds().getHeight() / 2d;
		double masseinheitY =  this.getHeight()/2d +  (haelfte/2d);
	
		gc.fillText(testText.getText(), masseinheitX, masseinheitY);
	}
	
	public void setColor(Color newColor)
	{
		this.colorToSet = newColor;
		double previous_w = this.getWidth();
		double previous_h = this.getHeight();
		this.refreshText(previous_w, previous_h);
	}
	
	public void resetColor()
	{
		this.setColor(STANDARD_TEXT_COLOR);
	}

}
