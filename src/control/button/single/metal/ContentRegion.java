package control.button.single.metal;

import control.button.single.metal.SingleMetalButton.Command;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


/**
 * Content container for different viewable things a.e ImageView, Text ...
 * @author m.goerlich
 *
 */
public class ContentRegion extends Region
{
	
	private double size;
	
	private Font fontVorgabe = null;
	
	private DoubleProperty scaleableFontSize = null;
	
	private static final double GAP_PERCENT = 0.1;
	
	private String textToShow;
	
	private Color textColor = Color.web("#0096ff"); 
	
	private Color textPressedColor = Color.web("#0074c5");
	
	private Image imageToShow;
	
	public ContentRegion()
	{
		super();
		
		this.initGraphics();
		this.registerListener();
		
	}

	private void registerListener() {
//		widthProperty().addListener(observable -> resize());
//		heightProperty().addListener(observable -> resize());
		
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
			else if(this.getChildren().get(0) instanceof Canvas)
			{
				//calculate possible text size and draw canvas with the text
				setText(textToShow);
			}
		}
		
		
	}

	public void setImageView(Image imageForView) 
	{
		this.imageToShow = imageForView;
		if(this.getChildren().size() > 0 && this.getChildren().get(0) != null && this.getChildren().get(0) instanceof ImageView)
			this.getChildren().remove(0);
		if(imageForView != null)
		{
			ImageView imageView = new ImageView(imageToShow);
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
		{
			fontVorgabe = Font.font("Verdana", FontWeight.BOLD, 12);
		
		}
		Text text = new Text(valueToShow);
		Font font =  Font.font(fontVorgabe.getFamily(), FontWeight.BOLD, size);
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
		this.textToShow = textToShow;
		if(this.getChildren().size() > 0 && this.getChildren().get(0) != null && this.getChildren().get(0) instanceof Canvas)
			this.getChildren().remove(0);
		
		if(textToShow != null)
		{
			Font font = Font.font(fontVorgabe.getName(), FontWeight.BOLD, scaleableFontSize.get());
			Canvas canvas = new Canvas(size, size);
			//muss transparenz sein wegen der möglichen Schaltung
			canvas.setMouseTransparent(true);
			GraphicsContext gc = canvas.getGraphicsContext2D();
			
			double w = canvas.getWidth();
			double h = canvas.getHeight();
			Bounds maxTextAbmasse = this.getMaxTextWidth(font, textToShow);
			System.out.println("w " + w + " h " + h);
			System.out.println("width " + maxTextAbmasse.getWidth() + " height " + maxTextAbmasse.getHeight());
			
			if(maxTextAbmasse.getWidth() < w  && maxTextAbmasse.getHeight() < h)
			{
				double tempSize = getGreaterFont(size * 0.12, w, h, textToShow);
				if(tempSize != getFontSize().get())
					getFontSize().set(tempSize);
			}
			else
			{
				 double tempSize = getLesserFont(getFontSize().get(), w, h, textToShow);
					if(tempSize != getFontSize().get())
						getFontSize().set(tempSize);
			}
			font = Font.font(fontVorgabe.getName(), FontWeight.BOLD, getFontSize().get());
			
			
			Text textFirstValue = new Text(textToShow);
			textFirstValue.setFont(font);
			
			double centerX = size / 2d;
			double valueX = centerX - (textFirstValue.getLayoutBounds().getWidth() / 2d );
			
			double valueY =  textFirstValue.getLayoutBounds().getHeight() / 2d;
			
			double komischesY = h/2d +  (valueY/2d);
			
			gc.setFill(textColor);
			gc.setFont(font);
			InnerShadow innerShadow = new InnerShadow();
			innerShadow.setOffsetX(1);
			innerShadow.setOffsetY(1);
			innerShadow.setColor(Color.web("0x00000090"));
			gc.setEffect(innerShadow);
			
			gc.fillText(textToShow, valueX, komischesY);
			this.getChildren().add(canvas);
			
		}
	}
	
	public void setMousePressed()
	{
		//egal was war das child muss raus
		if(this.getChildren().size() > 0 && this.getChildren().get(0) != null)
			this.getChildren().remove(0);
		
		if(textToShow != null)
		{
			Font font = Font.font(fontVorgabe.getName(), FontWeight.BOLD, scaleableFontSize.get() - (scaleableFontSize.get()* 0.1));
			Canvas canvas = new Canvas(size, size);
			//muss transparenz sein wegen der möglichen Schaltung
			canvas.setMouseTransparent(true);
			GraphicsContext gc = canvas.getGraphicsContext2D();
			
			double w = canvas.getWidth();
			double h = canvas.getHeight();
			Text textFirstValue = new Text(textToShow);
			textFirstValue.setFont(font);
			
			double centerX = size / 2d;
			double valueX = centerX - (textFirstValue.getLayoutBounds().getWidth() / 2d );
			
			double valueY =  textFirstValue.getLayoutBounds().getHeight() / 2d;
			
			double komischesY = h/2d +  (valueY/2d);
			
			gc.setFill(textPressedColor);
			gc.setFont(font);
			InnerShadow innerShadow = new InnerShadow();
			innerShadow.setOffsetX(1);
			innerShadow.setOffsetY(1);
			innerShadow.setColor(Color.web("0x00000090"));
			gc.setEffect(innerShadow);
			
			gc.fillText(textToShow, valueX, komischesY);
			this.getChildren().add(canvas);
		}
		else if(imageToShow != null)
		{
			//dann das image icon verkleinert darstellen
			
			ImageView imageView = new ImageView(imageToShow);
			imageView.setPreserveRatio(true);
			imageView.setFitHeight(size -(size *0.1));
			imageView.setFitHeight(size- (size *0.1));
			//repositioning 
			imageView.setLayoutX((size * 0.1)/2d);
			imageView.setLayoutY((size * 0.1)/2d);
			imageView.setMouseTransparent(true);
			this.getChildren().add(imageView);
		}
	}
	
	public void setMouseReleased()
	{
		if(this.getChildren().size() > 0 && this.getChildren().get(0) != null)
			this.getChildren().remove(0);
		
		if(textToShow != null)
		{
			Font font = Font.font(fontVorgabe.getName(), FontWeight.BOLD, scaleableFontSize.get());
			Canvas canvas = new Canvas(size, size);
			//muss transparenz sein wegen der möglichen Schaltung
			canvas.setMouseTransparent(true);
			GraphicsContext gc = canvas.getGraphicsContext2D();
			
			double w = canvas.getWidth();
			double h = canvas.getHeight();
			Text textFirstValue = new Text(textToShow);
			textFirstValue.setFont(font);
			
			double centerX = size / 2d;
			double valueX = centerX - (textFirstValue.getLayoutBounds().getWidth() / 2d );
			
			double valueY =  textFirstValue.getLayoutBounds().getHeight() / 2d;
			
			double komischesY = h/2d +  (valueY/2d);
			
			gc.setFill(textColor);
			gc.setFont(font);
			InnerShadow innerShadow = new InnerShadow();
			innerShadow.setOffsetX(1);
			innerShadow.setOffsetY(1);
			innerShadow.setColor(Color.web("0x00000090"));
			gc.setEffect(innerShadow);
			
			gc.fillText(textToShow, valueX, komischesY);
			this.getChildren().add(canvas);
		}
		else if(imageToShow != null)
		{
			//dann das image icon verkleinert darstellen
			
			ImageView imageView = new ImageView(imageToShow);
			imageView.setPreserveRatio(true);
			imageView.setFitHeight(size);
			imageView.setFitHeight(size);
			imageView.setMouseTransparent(true);
			this.getChildren().add(imageView);
			
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
	
	
	public void setTextColor(Color textColor)
	{
		this.textColor = textColor;
	}
	
	public void setTextColorPressed(Color textPressedColor)
	{
		this.textPressedColor = textPressedColor;
	}
}
