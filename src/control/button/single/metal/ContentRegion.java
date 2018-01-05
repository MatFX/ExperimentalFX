package control.button.single.metal;

import control.button.single.metal.SingleMetalButton.Command;
import control.universaldisplay.SensorValue;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Content container for different viewable things a.e ImageView, Text ...
 * @author m.goerlich
 *
 */
public class ContentRegion extends Region
{
	private Node nodeToShow;
	
	private ImageView imageView;
	
	private double size;
	
	private Font fontVorgabe = null;
	
	private DoubleProperty scaleableFontSize = null;
	
	private static final double GAP_PERCENT = 0.1;
	
	public ContentRegion()
	{
		super();
		
		this.initGraphics();
		this.registerListener();
		
		
		
	}

	private void registerListener() {
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
		
	}

	private void resize() 
	{
		// TODO Auto-generated method stub
		
	}

	private void initGraphics() 
	{
		fontVorgabe = new Font("Verdana",12);
		scaleableFontSize = new SimpleDoubleProperty(12);
	}

	public void setNewSize(double size) 
	{
		this.size = size;
		//this.setWidth(size);
		//this.setHeight(size);
		if(this.getChildren().size() > 0)
		{
			if(this.getChildren().get(0) instanceof ImageView)
			{
				((ImageView)this.getChildren().get(0)).setFitHeight(size);
				((ImageView)this.getChildren().get(0)).setFitWidth(size);
				
			}
			else if(this.getChildren().get(0) instanceof Text)
			{
				
				
				
				//((Text)this.getChildren().get(0)).set(size);
				//((Text)this.getChildren().get(0)).setFitWidth(size);
			}
		}
		
		
	}

	public void setImageView(Image imageForView) 
	{
		if(this.getChildren().size() > 0 && this.getChildren().get(0) != null)
			this.getChildren().remove(0);
		if(imageForView != null)
		{

			ImageView imageView = new ImageView(imageForView);
			imageView.setPreserveRatio(true);
			imageView.setFitHeight(size);
			imageView.setFitHeight(size);
			imageView.setMouseTransparent(true);
			this.getChildren().add(imageView);
			
		}
		
		
	}
	
	public double getGAPPercent()
	{
		return GAP_PERCENT;
	}
	

	private Bounds textWidth(double size, String valueToShow)
	{
		//hier muss die bounds aufgebaut werden anhand der zwei darzustellenden Werte 
		if(fontVorgabe == null)
			fontVorgabe = new Font("Verdana", 12);
		Text text = new Text(valueToShow);
		Font font =  Font.font(fontVorgabe.getFamily(), size);
        text.setFont(font);
        return text.getBoundsInLocal();
	}
	
	private Bounds getMaxTextWidth(Font font, String textToShow) 
	{
		Text valTextMax = new Text(textToShow);
		valTextMax.setFont(font);
		Bounds valMaxBounds = valTextMax.getBoundsInLocal();
		return new BoundingBox(0,0, valMaxBounds.getWidth(), valMaxBounds.getHeight());
	}
	
	protected double getGreaterFont(double fontSize, double w, double h, String valueToShow)
	{	
		double gapBreite = w * getGAPPercent() * 2;
		double gapHoehe = h * getGAPPercent() * 2;
		
		fontSize = fontSize + 1;
		Bounds futureBounds = textWidth(fontSize, valueToShow);
		if((futureBounds.getHeight() + gapHoehe) < h && (futureBounds.getWidth() + gapBreite) < w)
		{
			return getGreaterFont(fontSize, w, h, valueToShow);
		}
		//eines wieder zurück weil die Abfrage nicht gegriffen hat
		return fontSize-1;
	}


	protected double getLesserFont(double fontSize, double w, double h, String valueToShow)
	{	
		Bounds futureBounds = textWidth(fontSize, valueToShow);
		double gapBreite = w * getGAPPercent() * 2;
		double gapHoehe = h * getGAPPercent() * 2;
		//wenn eines von beiden über das Ziel hinaus ist, so ist eine kleiner Fontgröße zu ermitteln
		if((futureBounds.getHeight() + (gapHoehe)) > h || (futureBounds.getWidth() + (gapBreite)) > w)
		{
			fontSize = fontSize - 1;
			if(fontSize <= 0)
				return 1;
			return getLesserFont(fontSize, w, h, valueToShow);
		}
		return fontSize;
	}
	

	public void setText(String textToShow) 
	{
		if(this.getChildren().size() > 0 && this.getChildren().get(0) != null)
			this.getChildren().remove(0);
		
		double contentSize  = getWidth() < getHeight() ? getWidth() : getHeight();
	
		if(textToShow != null)
		{
			Font font = Font.font(fontVorgabe.getName(), scaleableFontSize.get());
			Canvas canvas = new Canvas(size, size);
			GraphicsContext gc = canvas.getGraphicsContext2D();
			double w = canvas.getWidth();
			double h = canvas.getHeight();
			Bounds maxTextAbmasse = this.getMaxTextWidth(font, textToShow);
			System.out.println("w " + w + " h " + h);
			System.out.println("width " + maxTextAbmasse.getWidth() + " height " + maxTextAbmasse.getHeight());
			
			if(maxTextAbmasse.getWidth() < w  && maxTextAbmasse.getHeight() < h)
			{
				System.out.println("greater erwartet");
				double tempSize = getGreaterFont(contentSize * 0.12, w, h, textToShow);
				if(tempSize != getFontSize().get())
					getFontSize().set(tempSize);
			}
			else
			{
				System.out.println("mach es kleiner");
				 double tempSize = getLesserFont(getFontSize().get(), w, h, textToShow);
					if(tempSize != getFontSize().get())
						getFontSize().set(tempSize);
			}
			font = Font.font(fontVorgabe.getName(), getFontSize().get());
			gc.setFont(font);
			
			Text textFirstValue = new Text(textToShow);
			
			double valueX = w - (textFirstValue.getLayoutBounds().getWidth());
			double haelfte =  textFirstValue.getLayoutBounds().getHeight() / 2d;
			double masseinheitY = h/2d +  (haelfte/2d);
			
			System.out.println("valueX " + valueX + " " + masseinheitY);
			System.out.println("font " + getFontSize().get());
			System.out.println("textToShow " + textToShow);
			
			gc.setFill(Color.RED);
			gc.fillText(textToShow, 0, 0);
			
			
			//Label text = new Label(textToShow);
			
			
			//größe ermitteln
			
			
			
			
			//text.setMouseTransparent(true);
			
			this.getChildren().add(canvas);
			
		}
		
	}

	public void setMouseEvent(Command commandValue) 
	{
		//TODO effekte am bild oder Text?
		if(commandValue == Command.BUTTON_PRESSED)
		{
			
			
		
		}
		else if(commandValue == Command.BUTTON_RELEASED)
		{
		
		
		}
		
	}
	

	public DoubleProperty getFontSize()
	{
		return scaleableFontSize;
	}
	


}
