package glasspane;

import java.util.HashMap;

import glasspane.ButtonRectangle.PositionGradient;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import sensorpanel.first.SensorPanel.Command;
import tools.helper.ImageLoader;
import tools.helper.UIToolBox;

public class GlassPaneSensor extends Region
{
	public enum StopIndizes
	{
		BASE_BACKGROUND_LINEAR_GRADIENT;
	}
	
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	private Rectangle base_background_component;// button_down, button_up;
	
	private Canvas textCanvas;
	
	private Canvas imageCanvas;
	
	private double width_component = 90;
	private double height_component = 100;
	
	private ButtonRectangle button_down, button_up;
	
	/**
	 * Wert der später in der Anzeige erscheint
	 */
	private SimpleStringProperty valueProperty = new SimpleStringProperty();
	
	private SimpleStringProperty imageFileNameProperty = new SimpleStringProperty();
	
	private SimpleObjectProperty<Command> commandProperty = new SimpleObjectProperty<Command>();
	
	private SimpleIntegerProperty blurRadiusProperty = new SimpleIntegerProperty();
	
	private int MIN_BLUR_RADIUS = 0;
	
	private int MAX_BLUR_RADIUS = 10;
	
	private int MIN_GRADIENT_ALPHA_CHANNEL = 0;
	
	private int MAX_GRADIENT_ALPHA_CHANNEL = 0x80;
	
	private SimpleIntegerProperty alphaChannelProperty  = new SimpleIntegerProperty();
	
	public GlassPaneSensor()
	{
		this.initGraphics();
		this.registerListener();
	}
	
	
	
	
	private void initGraphics() 
	{
		
		Stop[] stopArray = new Stop[]{
				new Stop(0, Color.web("#ffffff00")),
				//TODO hier evtl. noch am Alpha Kanal fummeln.
				new Stop(1, Color.web("#ffffff33"))
			};
		stopMap.put(StopIndizes.BASE_BACKGROUND_LINEAR_GRADIENT, stopArray);
		alphaChannelProperty.addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(newValue != null)
				{
					
					double alphaValueAsDouble = 0;
					if(newValue.intValue() > 0)
						alphaValueAsDouble = newValue.intValue() / 255D;
					System.out.println("alphaValueAsDouble "+ alphaValueAsDouble);
				
					Stop endStop = stopMap.get(StopIndizes.BASE_BACKGROUND_LINEAR_GRADIENT)[1];
					Color colorValue = endStop.getColor();
					String webColorString = UIToolBox.getWebColorString(colorValue.getRed(), colorValue.getGreen(), colorValue.getBlue(), alphaValueAsDouble);
				
					stopMap.get(StopIndizes.BASE_BACKGROUND_LINEAR_GRADIENT)[1] = new Stop(1, Color.web(webColorString));
					
					//wird der init umschifft
					if(base_background_component != null)
						resize();
					
				
				}
				
			}
			
		});
		alphaChannelProperty.set(0x33);
		
		
		
		base_background_component = new Rectangle();
		//base_background_component.setFill(Color.TRANSPARENT);
		//TODO
		base_background_component.setStroke(Color.web("#5abaa0"));
		//base_background_component.setSmooth(false);
		
		
        //Von außerhalb kann dann verändert werden.
    	blurRadiusProperty.addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
			{
				if(newValue != null)
				{
					if(newValue.intValue() < MIN_BLUR_RADIUS)
						newValue = new Integer(MIN_BLUR_RADIUS);
					else if(newValue.intValue()  > MAX_BLUR_RADIUS)
						newValue = new Integer(MAX_BLUR_RADIUS);
					
					
					GaussianBlur g = new GaussianBlur();  
			        g.setRadius(newValue.intValue());  
			        base_background_component.setEffect(g);
				}
			
				
			}
    		
    	});
    	//initial blur
		blurRadiusProperty.set(4);
        
		
		textCanvas = new Canvas();
		imageCanvas = new Canvas();
		
		button_down = new ButtonRectangle(PositionGradient.FROM_UP_TO_DOWN, 0.23555555562222225, 0.92000000001, 0.5288888887222222, 0.07999999995, 0.022222222222222223);
		button_down.setFill(Color.web("#5abaa0"));
		button_down.getCommandProperty().addListener(new ChangeListener<ButtonRectangle.Command>(){

			@Override
			public void changed(ObservableValue<? extends glasspane.ButtonRectangle.Command> observable, glasspane.ButtonRectangle.Command oldValue, glasspane.ButtonRectangle.Command newValue) {
				if(newValue == glasspane.ButtonRectangle.Command.BUTTON_RELEASED)
				{
					commandProperty.set(Command.RESET_COMMAND);
					commandProperty.set(Command.NEXT_SENSOR_VALUE);
					
				}
				
			}
			
		});
		
		
		
		button_up = new ButtonRectangle(PositionGradient.FROM_DOWN_TO_UP, 0.23555555563333336, 0.0, 0.5288888887222222, 0.07999999995, 0.022222222222222223);
		button_up.setFill(Color.web("#5abaa0"));
		button_up.getCommandProperty().addListener(new ChangeListener<ButtonRectangle.Command>(){

			@Override
			public void changed(ObservableValue<? extends glasspane.ButtonRectangle.Command> observable, glasspane.ButtonRectangle.Command oldValue, glasspane.ButtonRectangle.Command newValue) {
				if(newValue == glasspane.ButtonRectangle.Command.BUTTON_RELEASED)
				{
					commandProperty.set(Command.RESET_COMMAND);
					commandProperty.set(Command.PREVIOUS_SENSOR_VALUE);
					
				}
				
			}
			
		});
	
		this.getChildren().addAll(base_background_component, textCanvas, imageCanvas, button_down, button_up);
		
		imageFileNameProperty.set("hi_temp");
		valueProperty.set("22.5°C");
		valueProperty.addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				resize();
				
			}
			
		});
		
	}
	
	private void registerListener() 
	{
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
	}

	private void resize() {
		width_component = getWidth();
		height_component = getHeight();
		
		//w und h immer auf die volle zugewiesene Breite und Höhe
		base_background_component.setWidth(width_component);
		base_background_component.setHeight(height_component);
		//4px
		base_background_component.setArcWidth(width_component * 0.044444444444444446);
		base_background_component.setArcHeight(width_component * 0.044444444444444446);
		base_background_component.setStrokeWidth(width_component * 0.022222222222222223);
		
		LinearGradient lg = new LinearGradient(width_component * 0.8452471254888888, 
				height_component * 1.03818700626, 
				width_component * 0.15475287451111114,
				height_component *  -0.03818700626, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.BASE_BACKGROUND_LINEAR_GRADIENT));
		base_background_component.setFill(lg);
		
		
		//als erstes die gezeichnete Fläche leeren
		//imageCanvas.getGraphicsContext2D().clearRect(imageCanvas.getLayoutX(), imageCanvas.getLayoutY(), imageCanvas.getWidth(), imageCanvas.getHeight());
		
		double newX = width_component * 0.23555555562222225;
		double newY = height_component * 0.09619787962;
		
		
		double refresh_w_i = imageCanvas.getWidth();
		double refresh_h_i = imageCanvas.getHeight();
		
	
		double newWidth = width_component * 0.5288888887333334;
		double newHeight = height_component * 0.27541007289999997;
		imageCanvas.setWidth(newWidth);
		imageCanvas.setHeight(newHeight);
		imageCanvas.relocate(newX, newY);
		
		refreshImageContent(refresh_w_i, refresh_h_i);
		
		//als erstes die gezeichnete Fläche leeren
		//TODO prüfen wegen dem Clearing eigentlich müsste es dohc funktionieren
		//textCanvas.getGraphicsContext2D().clearRect(textCanvas.getLayoutX(), textCanvas.getLayoutY(), textCanvas.getWidth(), textCanvas.getHeight());
		
		double refresh_w = textCanvas.getWidth();
		double refresh_h = textCanvas.getHeight();
		
		
		newX = width_component * 0.01666649998888889;
		newY = height_component * 0.38780583212999997;
		newWidth = width_component * 0.966667;
		newHeight = height_component * 0.51865887269;
		
		//System.out.println("new Width " + newWidth + " newHeight " + newHeight);
		textCanvas.setWidth(newWidth);
		textCanvas.setHeight(newHeight);
		textCanvas.relocate(newX, newY);
		
		refreshLCDContent(refresh_w, refresh_h);

		button_down.refreshSize(width_component, height_component);
		button_up.refreshSize(width_component, height_component);
	}
	
	private void refreshImageContent(double previous_w, double previous_h)
	{
		GraphicsContext gc = imageCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, previous_w, previous_h);
		
		double x = imageCanvas.getLayoutX();
		double y = imageCanvas.getLayoutY();
		double newCanvasWidth = imageCanvas.getWidth();
		double newCanvasHeight = imageCanvas.getHeight();
	
		System.out.println("x: " + x + " y: " +y + " w: " + newCanvasWidth + " h: "+newCanvasHeight);
		
		double middle_x = newCanvasWidth/2d;
		double middle_y = newCanvasHeight/2d;
		
		
		//TODO raus text;
		//gc.setFill(Color.ROSYBROWN);
		//gc.fillRect(0, 0, newCanvasWidth, newCanvasHeight);
		
		
		
		Image rawImage = ImageLoader.getImageFromIconFolder(imageFileNameProperty.get());
		//System.out.println("rawImage " + rawImage.getWidth() + " rawImage "  + rawImage.getHeight());
		//bei Gleichheit 1
		//Breite größer dann Wert > 1
		//Höhe größer dann Wert < 1
		double ratio = rawImage.getWidth() / rawImage.getHeight();
	
		double newIconWidth = newCanvasWidth * 0.9;
		double newIconHeight = newCanvasHeight * 0.9;
		//System.out.println("newIconWidth: " + newIconWidth + " newIconHeight: " +newIconHeight );
		if(ratio == 1)
		{
			if(newCanvasWidth <= newCanvasHeight)
			{
				newIconHeight = newIconWidth;
			}
			else
			{
				//im anderen Fall ist die Zeichenfläche breiter als höher
				newIconWidth = newIconHeight;
			}
		}
		//TODO ich glaube ist immer gleich
		else if(ratio < 1)
		{
			if(newCanvasWidth <= newCanvasHeight)
			{
				newIconHeight = (newIconWidth / ratio);
			}
			else
			{
				newIconWidth = newIconHeight * ratio;
			}
		}
		else if(ratio > 1)
		{
			if(newCanvasWidth <= newCanvasHeight)
			{
				newIconHeight = (newIconWidth / ratio);
			}
			else
			{
				newIconWidth = newIconHeight * ratio;
			}
		}
		
		//System.out.println("after recalculate " + newIconWidth + "  X " + newIconHeight);
		
		//x und y berechnen damit es in der Mitte liegt
		
		double halfedWidth = newIconWidth/2d;
		
		double newXLocation = middle_x - halfedWidth;
		
		double halfedHeight = newIconHeight / 2d;
		
		double newYLocation = middle_y - halfedHeight;
		
		
		Color colorImage = Color.web("#5abaa0");

		Image scaledImage = ImageLoader.getImageFromIconFolder(imageFileNameProperty.get(), newIconWidth, newIconHeight, false, true);
		
		Image coloredImage = UIToolBox.getColorizedImage(scaledImage, colorImage);
		
		gc.drawImage(coloredImage, newXLocation, newYLocation);
		
	}

	private void refreshLCDContent(double previous_w, double previous_h) 
	{
		
		GraphicsContext gc = textCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, previous_w, previous_h);
		
		Font fontLcd = Font.font("Verdana", 10);
		
		Bounds maxTextAbmasseLCD = UIToolBox.getMaxTextWidth(fontLcd, valueProperty.get());
		double tempSizeLCD;
		if(maxTextAbmasseLCD.getWidth() < textCanvas.getWidth()*0.97  && maxTextAbmasseLCD.getHeight() < textCanvas.getHeight() *0.97)
		{
			tempSizeLCD = UIToolBox.getGreaterFont(fontLcd.getSize()+1, textCanvas.getWidth()*0.97, textCanvas.getHeight()*0.97, valueProperty.get(), 0.01, fontLcd);
		}
		else
		{
			tempSizeLCD = UIToolBox.getLesserFont(fontLcd.getSize(), textCanvas.getWidth()*0.97, textCanvas.getHeight()*0.97, valueProperty.get(),  0.01, fontLcd);
		}
		
		fontLcd = Font.font(fontLcd.getName(), tempSizeLCD);
		gc.setFill(Color.web("#5abaa0"));
		gc.setFont(fontLcd);
		Text valueText = new Text();
		valueText.setText(valueProperty.get());
		valueText.setFont(fontLcd);
		double masseinheitXLCD = textCanvas.getWidth()*0.97 - (valueText.getLayoutBounds().getWidth());
		gc.fillText(valueText.getText(), masseinheitXLCD,   valueText.getLayoutBounds().getHeight() );
		
	}


	/**
	 * value property to set text from outer the class
	 * @return
	 */
	public SimpleStringProperty getValueProperty() {
		return valueProperty;
	}
	
	/**
	 * image property to set icon from outer the class.
	 * @return
	 */
	public SimpleStringProperty getImageProperty() 
	{
		return imageFileNameProperty;
	}

	/**
	 * for listing the commands
	 * @return
	 */
	public SimpleObjectProperty<Command> getCommandProperty() 
	{
		return this.commandProperty;
	}

	public SimpleIntegerProperty getBlurRadiusProperty()
	{
		return blurRadiusProperty;
	}

	public int getMinBlurRadiusValue() {
		return MIN_BLUR_RADIUS;
	}

	public int getMaxBlurRadiusValue() {
		return MAX_BLUR_RADIUS;
	}

	public double getMinGradientAlphaChannel() {
		return MIN_GRADIENT_ALPHA_CHANNEL;
	}

	public double getMaxGradientAlphaChannel() {
		return MAX_GRADIENT_ALPHA_CHANNEL;
	}
	
	public SimpleIntegerProperty getAlphaChannelProperty()
	{
		return alphaChannelProperty;
	}
	
}
