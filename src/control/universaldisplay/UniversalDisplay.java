package control.universaldisplay;

import java.util.HashMap;

import control.dimmer.OptionalImageBox;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class UniversalDisplay extends Region
{
	
	/**
	 * possible command values 
	 *
	 */
	public enum Command
	{
		PREVIOUS_SENSOR_VALUE, NEXT_SENSOR_VALUE, AUTO_CHANGE, SEND_PRESET, NEXT_PRESET, PREVIOUS_PRESET, SEND_VALUE, 
		/**
		 * der wird dann von außerhalb gesetzt, damit auch das aktuelle Kommando nochmal gesetzt werden kann.
		 * <br>you need the reset as a "acknowledge" from outside.
		 */
		RESET_COMMAND;
	}
	
	
	
	//Enums für die gespeicherten Farben
	public enum StopIndizes
	{
		//für die hintergründe
		BACKGROUND_CIRCLE,
		INNER_BACKGROUND_CIRCLE,
		INNER_BACKGROUND_CIRCLE2,
		TOP_OVERLAY_BACKGROUND,
		//für die monitore
		LCD_DISPLAY_SCHEIN,
		GLANZ_UNTEN,
		GLANZ_OBEN
		
	}



	private static final double GAP_PERCENT = 0.1;
	
	
	
	//128 x 128 war die max Ausdehnung bei der Vorlage
	
	private Circle background, backgroundCircle, innerBackground, innerBackgroundCircle, innerBorder, topoverlay, 
		//jetzt die monitorkreise
		rahmenInnenring, lcdDisplay, scheinLCD, glanzUnten, glanzOben;
	
	//hier die Startkoordinaten
	private double width = 128, height = 128;
	
	private double centerX = 64, centerY = 64;
	

	/**
	 * Farbmap weil die Werte unveränderlich sind und bei resize wieder bei den Gradienten neu gesetzt werden müssen.
	 */
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	private TopRegion topRegion;
	
	private Arc buttonTopLeft, buttonTopRight, buttonTopAuto;
	
	private DropShadow dropShadow;
	
	/**
	 * different sensoric states to show
	 */
	private boolean isMultiSensor = true;
	
	private Text textTopLeft, textTopAuto, textTopRight;
	
	private Color textColor = Color.web("#d2d74b");
	
	private InnerShadow innerShadow;
	/**
	 * für den Text wenn der button gedrückt wurde
	 */
	private Glow textGlow = new Glow(0.3);
	
	/**
	 * Kammdos können hier empfangen werden (listener anschluss)
	 */
	private SimpleObjectProperty<Command> commandProperty = new SimpleObjectProperty<Command>();
	
	private OptionalImageBox optionalImageBox;
	

	/**
	 * Hier erfolgt die Anzeige des zur Zeit dargestellten Wertes
	 */
	private Canvas textCanvas;
	
	/**
	 * first Value or main value; Bigger from font size
	 */
	private Text textFirstValue;
	
	private Text textFirstMeasuringUnit;
	
	/**
	 * second value or minor value; top value other main value
	 */
	private Text textSecondValue;
	
	private Text textSecondMeasuringUnit;
	
	/**
	 * Zeichnungsfläche für textPreset
	 */
	private Canvas textSecondValueCanvas;
	
	/**
	 * maxSizeOfViews ist needed to flip to next or previous views.
	 */
	private int maxSizeOfViews = 1;
	
	private int indexOfView = 0;
	
	private SensorValue mainValueToShow;
	
	private SensorValue minorValueToShow;
	
	private Font fontVorgabe = null, fontMinorVorgabe = null;
	
	private DoubleProperty scaleableFontSize = null, scaleableMinorFontSize;
	
	
	public UniversalDisplay(int maxSizeOfViews)
	{
		this.initGraphics();
		this.registerListener();
		this.maxSizeOfViews = maxSizeOfViews;
		
	}

	private void registerListener() 
	{
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
	
		
		System.out.println("isMultiSensor " + isMultiSensor);
		if(this.isMultiSensor)
		{
			
			buttonTopLeft.setOnMousePressed(e -> setPreviousSensorValueNodePressed(buttonTopLeft, textTopLeft, Command.PREVIOUS_SENSOR_VALUE, e));
			buttonTopLeft.setOnMouseReleased(e -> setNodeReleased(buttonTopLeft, textTopLeft, e));
		
			buttonTopAuto.setOnMousePressed(e -> setAutoSensorValueNodePressed(buttonTopAuto, textTopAuto, Command.AUTO_CHANGE, e));
			buttonTopAuto.setOnMouseReleased(e -> setNodeReleased(buttonTopAuto, textTopAuto, e));
			
			buttonTopRight.setOnMousePressed(e -> setNextSensorValueNodePressed(buttonTopRight, textTopRight, Command.NEXT_SENSOR_VALUE, e));
			buttonTopRight.setOnMouseReleased(e -> setNodeReleased(buttonTopRight, textTopRight, e));
		
		}
	}



	private void resize() 
	{
		double size  = getWidth() < getHeight() ? getWidth() : getHeight();
		
		if(getHeight() > 0)
			centerY = getHeight() / 2d;
		
		if(getWidth() > 0)
			centerX = getWidth() / 2d;
		
		double radius = size / 2d;
		
		//cx 64, cy 64, r 63
		background.setCenterX(centerX);
		background.setCenterY(centerY);
		//100/64 * 63 = 98,4375 = 0.984375
		background.setRadius(radius * 0.984375);
		
	
		//x1="64" y1="127" x2="64" y2="1"
		//y1 = 100/128 * 127  = 99,21875 = 0.9921875
		//y2 = 100/128 * 1 = 0,78125 = 0.0078125
		LinearGradient lgBackgroundCircle = new LinearGradient(centerX, (getHeight() * 0.9921875 ) , centerX, 
				(getHeight()  *  0.0078125), false, 
				CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.BACKGROUND_CIRCLE));
		
		//cx="64" cy="64" r="63"
		backgroundCircle.setCenterX(centerX);
		backgroundCircle.setCenterY(centerY);
		backgroundCircle.setRadius(radius * 0.984375);
		backgroundCircle.setFill(lgBackgroundCircle);
		
		
		//x1="64" y1="103.4338" x2="64" y2="-2.0003"
		//y1 100/128 * 103.4338 = 80,80765625 = 0.8080765625
		//y2 100/128 * -2.0003 = -1,56484375 = -0,0156484375
		LinearGradient lgInnerBg = new LinearGradient(centerX, (getHeight() * 0.8080765625 ) , centerX, 
				(getHeight()  *  -0.0156484375), false, 
				CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.INNER_BACKGROUND_CIRCLE));
		
		
		//cx="64" cy="64" r="60"
		//r 100/64 * 60 = 93,75 = 0.9375
		innerBackground.setCenterX(centerX);
		innerBackground.setCenterY(centerY);
		innerBackground.setRadius(radius * 0.9375);
		innerBackground.setFill(lgInnerBg);
		
		//x1="64" y1="103.4338" x2="64" y2="-2.0003"
		LinearGradient innerBG2 = new LinearGradient(centerX, (getHeight() * 0.8080765625 ) , centerX, 
				(getHeight()  *  -0.0156484375), false, 
				CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.INNER_BACKGROUND_CIRCLE2));
		
		//gleiche wie oben
		innerBackgroundCircle.setCenterX(centerX);
		innerBackgroundCircle.setCenterY(centerY);
		innerBackgroundCircle.setRadius(radius * 0.9375);
		innerBackgroundCircle.setFill(innerBG2);
		
		//cx="64" cy="64" r="58"
		//r 100/64 * 58 = 90,625 = 0.90625
		
		innerBorder.setCenterX(centerX);
		innerBorder.setCenterY(centerY);
		innerBorder.setRadius(radius * 0.90625);
		
	
		//r="57"
		//r 100/64 * 57 = 89,0625 = 0.890625
		RadialGradient topOverlayRG = new RadialGradient(0D, 0D, centerX, centerY, radius * 0.890625, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.TOP_OVERLAY_BACKGROUND));
		
		topoverlay.setCenterX(centerX);
		topoverlay.setCenterY(centerY);
		topoverlay.setRadius(radius * 0.890625);
		topoverlay.setFill(topOverlayRG);
		
		
		rahmenInnenring.setCenterX(centerX);
		rahmenInnenring.setCenterY(centerY);
		//45.5 = 100/64 * 45.5 = 71,09375 = 0.7109375
		rahmenInnenring.setRadius(radius *  0.7109375);
		
		lcdDisplay.setCenterX(centerX);
		lcdDisplay.setCenterY(centerY);
		//45 = 100/64 * 45 = 70,3125 = 0.703125
		lcdDisplay.setRadius(radius * 0.703125);
		
		
		//45 = 100/64 * 45 = 70,3125 = 0.703125
		RadialGradient rgScheinLCD = new RadialGradient(0D, 0D, centerX, centerY, radius * 0.703125, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.LCD_DISPLAY_SCHEIN));
		
		scheinLCD.setCenterX(centerX);
		scheinLCD.setCenterY(centerY);
		scheinLCD.setRadius(radius * 0.703125);
		scheinLCD.setFill(rgScheinLCD);
		
		
		
		//Berechnung geht von centerx und y aus!
		//x1 88.4346 - 64 = 24,4346 = 100/128 * 24,4346 = 19,08953125 = 0.1908953125
		//y1 110.0651 - 64 = 46,0651 = 100/128 * 46,0651 = 35,988359375 = 0.35988359375
		//x2 64 - 57,9346 = 6,0654 = 100/128 * 6,0654 = 4,73859375 = 0.0473859375
		//y2 64 - 52,5651 = 11,4349 = 100/128 * 11,4349 = 8,933515625 = 0.08933515625
		
		LinearGradient glanzUntenLG = new LinearGradient(centerX + (size * 0.1908953125), 
				centerY + (size * 0.35988359375),
				centerX - (size * 0.0473859375), 
				centerY - (size * 0.08933515625), false, 
				CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.GLANZ_UNTEN));
		
		glanzUnten.setCenterX(centerX);
		glanzUnten.setCenterY(centerY);
		glanzUnten.setRadius(radius * 0.703125);
		glanzUnten.setFill(glanzUntenLG);
		
		
		
		//punkte und lineargradient für topregion berechnen
		
		/*
		 * <g id="KreisBackground">
			<circle style="fill:#40423B;" cx="64" cy="64" r="45"/>
			</g>
			<g id="KreisLinksSubtract">
				<circle style="fill:#0A7C79;" cx="43" cy="93" r="45"/>
			</g>
			<g id="KreisRechtsSubtract">
				<circle style="fill:#0A7C79;" cx="107" cy="82" r="45"/>
			</g>
		 */
		
		//r 100/64 * 45 =  70,3125 = 0.703125
		PointXYR backgroundPoint = new PointXYR(centerX, centerY, radius * 0.703125);
		
		
		//x1="52.6787" y1="2.2106" x2="62.7276" y2="60.4944"
		//x1 = 64 - 52.6787 = 11,3213 > 100/128 * 11,3213 = 8,844765625 = 0.08844765625
		//y1 = 64 - 2.2106 = 61,7894 > 100/128 * 61,7894 = 48,27296875 = 0.4827296875
		//x2 = 64 - 62.7276 = 1,2724 > 100/128 * 1,2724 = 0,9940625 = 0.009940625
		//y2 = 64 - 60,4944 = 3,5056 > 100/128 * 3,5056 = 2,73875 = 0.0273875
		LinearGradient lgTopShiny = new LinearGradient(
				centerX - (size * 0.08844765625),
				centerY - (size *  0.4827296875),
				centerX - (size * 0.009940625),
				centerY -  (size  *  0.0273875), 
				false, 
				CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.GLANZ_OBEN));
		
		
		
		backgroundPoint.setPaint(lgTopShiny);
		System.out.println("backgroundPaint " + backgroundPoint.toString());
		
		//coords jeweils von den fixen Punkten ermitteln
		//cx = 64 - 43 = 21 davon % 100/128 * 21 = 16,40625 = 0.1640625
		//cy = 93 - 64 = 29 davon % 100/128 * 29 = 22,65625 = 0.2265625
		//r = ist immer gleich
		PointXYR linksPoint = new PointXYR(centerX - (size * 0.1640625), centerY +(size * 0.2265625), radius * 0.703125);
		
		//PointXYR linksPoint = new PointXYR(43, 93, radius * 0.703125);
		
		
		System.out.println("linksPoint " + linksPoint.toString());
		
		//cx 107 - 64 = 43; 100/128 * 43 = 33,59375 = 0.3359375
		//cy 82 - 64 = 18; 100/128 * 18  =14,0625 = 0.140625
		PointXYR rechtsPoint = new PointXYR(centerX + (size * 0.3359375), centerY + (size *  0.140625), radius * 0.703125);
		System.out.println("rechtsPoint " + rechtsPoint.toString());
		
		topRegion.setNewValues(backgroundPoint, linksPoint, rechtsPoint);
		topRegion.resize();
		
		
		//in der Breite soll diese ca. 60 prozent von dem monitorkreis sein.
		//in der höhe wird bei 128 Startsize ein Wert von 32 px skaliert
		
		//r war 37,5 * = 70 > 100/128 * 70  = 54,6875 = 54,6875 * ,6 =  32,4
		double breiteImages = size * 0.546875 * 0.6;
		
		//100/64 * 32 = 25 =0-25
		double hoeheImages = size/2 * 0.25;
		

		
		
		double w = size * 0.5;
		double h = size * 0.171875;
		double x = centerX - (w/2); 
		double y = centerY - (h/2);
		textCanvas.setWidth(w);
		textCanvas.setHeight(h);
		textCanvas.relocate(x, y);
		
		drawTextValues(true);
		
		optionalImageBox.setMinSize(breiteImages, hoeheImages);
		optionalImageBox.setLayoutX(centerX - (breiteImages/2));
		optionalImageBox.setLayoutY(centerY + (textCanvas.getHeight()/2));
		optionalImageBox.resize(hoeheImages);
		
		
		if(isMultiSensor)
		{
			buttonTopLeft.setCenterX(centerX);
			buttonTopLeft.setCenterY(centerY);
			buttonTopLeft.setRadiusX(radius*0.87);
			buttonTopLeft.setRadiusY(radius*0.87);
			buttonTopLeft.setStartAngle(122.0f);
			buttonTopLeft.setLength(-20.0f);
			buttonTopLeft.setType(ArcType.ROUND);
			
			
			buttonTopAuto.setCenterX(centerX);
			buttonTopAuto.setCenterY(centerY);
			buttonTopAuto.setRadiusX(radius*0.87);
			buttonTopAuto.setRadiusY(radius*0.87);
			buttonTopAuto.setStartAngle(100.0f);
			buttonTopAuto.setLength(-20.0f);
			buttonTopAuto.setType(ArcType.ROUND);
			

			buttonTopRight.setCenterX(centerX);
			buttonTopRight.setCenterY(centerY);
			buttonTopRight.setRadiusX(radius*0.87);
			buttonTopRight.setRadiusY(radius*0.87);
			buttonTopRight.setStartAngle(78.0f);
			buttonTopRight.setLength(-20.0f);
			buttonTopRight.setType(ArcType.ROUND);
			
			Font valueFont = Font.font("Verdana", FontWeight.BOLD, size * 0.05);
			
			
			textTopLeft.setFill(textColor);
			textTopLeft.setFont(valueFont);
			textTopLeft.setRotate(-20);
			textTopLeft.relocate(centerX - (radius * 0.345), centerY - (radius * 0.795));
			
			Font valueFontAuto = Font.font("Verdana", FontWeight.BOLD, size * 0.04);
			textTopAuto.setFill(textColor);
			textTopAuto.setFont(valueFontAuto);
			textTopAuto.setRotate(0);
			textTopAuto.relocate(centerX - (radius * 0.1), centerY - (radius * 0.83));
			
			textTopRight.setFill(textColor);
			textTopRight.setFont(valueFont);
			textTopRight.setRotate(20);
			textTopRight.relocate(centerX + (radius * 0.25), centerY - (radius * 0.795));
			
			
		}
		

		//resize der canvas für preset und der Anzeige
		double p_w = size * 0.25;
		double p_h = size * 0.171875;
		
		//auf y komme ich über die text_canvas
		double p_y = y - p_h;
		double p_x = centerX - (p_w/2);
		textSecondValueCanvas.setWidth(p_w);
		textSecondValueCanvas.setHeight(p_h);
		textSecondValueCanvas.relocate(p_x, p_y);
		
		drawSecondTextValue(true);
		
	}

	private void initGraphics() 
	{
		background = new Circle();
		background.setFill(Color.web("#282828"));
	
	    backgroundCircle = new Circle();
		
	    Stop[] stopArray = new Stop[]{
	    		new Stop(0.0, Color.web("#1A1A1AE6")),
	    		new Stop(0.297, Color.web("#000000")),
	    		new Stop(0.345, Color.web("#0F0F0FE6")),
	    		new Stop(0.4718, Color.web("#313131A6")),
	    		new Stop(0.5767, Color.web("#45454570")),
	    		new Stop(0.646, Color.web("#4D4D4D4D")),
	    		new Stop(0.6524, Color.web("#4B4B4B4F")),
	    		new Stop(0.7756, Color.web("#30303080")),
	    		new Stop(0.8848, Color.web("#202020AB")),
	    		new Stop(0.9678, Color.web("#1A1A1ACC"))
	    	};
	    stopMap.put(StopIndizes.BACKGROUND_CIRCLE, stopArray);
	    
	    
	    innerBackground = new Circle();
	    
	    stopArray = new Stop[]{
	    		new Stop(0.0, Color.web("#4D4D4D")),
	    		new Stop(1.0, Color.web("#666666"))
	    	};
	    stopMap.put(StopIndizes.INNER_BACKGROUND_CIRCLE, stopArray); 
	    
	    
	    innerBackgroundCircle = new Circle();
	   
	    stopArray = new Stop[]{
	    		new Stop(0.0, Color.web("#999999")),
	    		new Stop(0.297, Color.web("#CCCCCC")),
	    		new Stop(0.5371, Color.web("#CCCCCC")),
	    		new Stop(0.5669, Color.web("#C9C9C9F6")),
	    		new Stop(0.7944, Color.web("#B9B9B9B3")),
	    		new Stop(0.9678, Color.web("#B3B3B380"))
	    	};
	    stopMap.put(StopIndizes.INNER_BACKGROUND_CIRCLE2, stopArray); 
	    
	    innerBorder = new Circle();
	    innerBorder.setFill(Color.web("#232323"));
	    
	   stopArray = new Stop[]{
	    		new Stop(0.0, Color.web("#F2F2F2")),
	    		new Stop(0.4381, Color.web("#F0F0F0")),
	    		new Stop(0.5959, Color.web("#E9E9E9")),
	    		new Stop(0.7083, Color.web("#DEDEDE")),
	    		new Stop(0.7993, Color.web("#CDCDCD")),
	    		new Stop(0.8771, Color.web("#B7B7B7")),
	    		new Stop(0.9448, Color.web("#9C9C9C")),
	    		new Stop(1.0, Color.web("#808080"))
	    	};
	   stopMap.put(StopIndizes.TOP_OVERLAY_BACKGROUND, stopArray); 
	    
	   topoverlay = new Circle();
	   
	   
	   rahmenInnenring = new Circle();
	   rahmenInnenring.setFill(Color.web("#40423B"));
	   
	   
	   lcdDisplay = new Circle();
	   lcdDisplay.setFill(Color.web("#949C87"));
	  //lcdDisplay.setFill(Color.web("#87989c"));
	  //lcdDisplay.setFill(Color.web("#4d7da3"));
	   
	   stopArray = new Stop[]{
				new Stop(0.0, Color.web("#949C8700")),
				new Stop(0.4297, Color.web("#92998558")),
				new Stop(0.692, Color.web("#8A917F8D")),
				new Stop(0.9087, Color.web("#7D8374B9")),
				new Stop(1.0, Color.web("#757A6DCC"))
			};
	   stopMap.put(StopIndizes.LCD_DISPLAY_SCHEIN, stopArray); 
	   scheinLCD = new Circle();
	   
	   stopArray = new Stop[]{
				new Stop(0.0, Color.web("#FFFFFF80")),
				new Stop(0.3368, Color.web("#D7DAD255")),
				new Stop(0.7829, Color.web("#A7AD9C1C")),
				new Stop(1.0, Color.web("#949C8700"))
			};
	   stopMap.put(StopIndizes.GLANZ_UNTEN, stopArray);
	   glanzUnten = new Circle();
	   
	   //muss die farben nochmals anpassen irgendwas stimmt nicht mit dem oberen Verlauf
	  
	   stopArray = new Stop[]{
				new Stop(0.1431, Color.web("#FFFFFFCC")),
				new Stop(0.2704, Color.web("#F1F2EFA7")),
				new Stop(0.5183, Color.web("#CCD0C65F")),
				new Stop(0.8453, Color.web("#949C8700"))
			};
	   stopMap.put(StopIndizes.GLANZ_OBEN, stopArray);
	   
	   //gradient wird dann immer bei resize gesetzt
	   topRegion = new TopRegion(new Circle(), new Circle(), new Circle());
	   //wird benötigt, ansonsten funktionieren die buttons nicht
	   topRegion.setMouseTransparent(true);
	   

		
		textSecondValueCanvas = new Canvas();
		textSecondValue = new Text();
	
		textCanvas = new Canvas();
		
		textFirstValue = new Text();
		//TODO vertl. stringproperty koppeln den Wert
		textFirstValue.setText("TODO Value");
		
		textFirstMeasuringUnit = new Text();
		//TODO optional und variable machen
		textFirstMeasuringUnit.setText("%");
	   
	   optionalImageBox = new OptionalImageBox();
	   
	   dropShadow = new DropShadow();
	   dropShadow.setColor(Color.web("000000A0")); 
	   
	   innerShadow = new InnerShadow();
	   innerShadow.setBlurType(BlurType.GAUSSIAN);
	   innerShadow.setColor(Color.web("#000000A0"));
	        
	    
	    if(this.isMultiSensor)
	    {
	      //TODO color
	 	   buttonTopLeft = new Arc();
	 	   buttonTopLeft.setFill(Color.web("525252"));
	 	   buttonTopLeft.setType(ArcType.ROUND);
	 	   buttonTopLeft.setEffect(dropShadow);
	 	   
	 	   buttonTopAuto = new Arc();
	 	   //404040
	 	   buttonTopAuto.setFill(Color.web("525252"));
	 	   buttonTopAuto.setType(ArcType.ROUND);
	 	   buttonTopAuto.setEffect(dropShadow);
	 	   
	 	   buttonTopRight = new Arc();
	 	   buttonTopRight.setFill(Color.web("525252"));
	 	   buttonTopRight.setType(ArcType.ROUND);
	 	   buttonTopRight.setEffect(dropShadow);
	 	   
	 	   textTopLeft = new Text("<");
	 	   textTopLeft.setMouseTransparent(true);
	 	   textTopAuto = new Text("Auto");
	 	   textTopAuto.setMouseTransparent(true);
	 	   textTopRight = new Text(">");
	 	   textTopRight.setMouseTransparent(true);
	 	   
		   this.getChildren().addAll(background, backgroundCircle, 
				   innerBackground, innerBackgroundCircle, innerBorder, topoverlay,
				   buttonTopLeft, buttonTopAuto, buttonTopRight,
				   textTopLeft, textTopAuto, textTopRight,
				   rahmenInnenring, lcdDisplay, scheinLCD, glanzUnten,
				   textSecondValueCanvas, textCanvas,
				   topRegion, optionalImageBox
				   
				  
				   
				   );
	    }
	    else
	    {
    	  
		   this.getChildren().addAll(background, backgroundCircle, 
				   innerBackground, innerBackgroundCircle, innerBorder, topoverlay,
				   rahmenInnenring, lcdDisplay, scheinLCD, glanzUnten,
				   textSecondValueCanvas, textCanvas,
				   topRegion, optionalImageBox
				   );
	    }
	    
	    drawTextValues(true);
	    
	 
	 
	}
	
	public void setMultiSensor(boolean isMultiSensor)
	{
		this.isMultiSensor = isMultiSensor;
	}
	
	public boolean isMultiSensor()
	{
		return isMultiSensor;
	}
	
	private void setNextSensorValueNodePressed(Arc nodeBase, Text textNode, Command command, MouseEvent e)
	{
		nextSensorValue();
		setNodePressed(nodeBase, textNode, command, e);
	}
	
	

	public void setPreviousSensorValueNodePressed(Arc nodeBase, Text textNode, Command command, MouseEvent e)
	{
		previousSensorValue();
		setNodePressed(nodeBase, textNode, command,e);
	}
	
	
	public void setAutoSensorValueNodePressed(Arc nodeBase, Text textNode, Command command, MouseEvent e)
	{
		toggleAutoValue();
		setNodePressed(nodeBase, textNode, command,e);
	}
	

	private void toggleAutoValue() {
		// TODO Auto-generated method stub
		
	}

	private void nextSensorValue() 
	{
		this.indexOfView++;
		if(indexOfView >= this.maxSizeOfViews)
			indexOfView = 0;
		
	}
	

	private void previousSensorValue() {
		this.indexOfView--;
		if(indexOfView < 0)
			indexOfView = this.maxSizeOfViews-1;
	}
	
	/**
	 * to get the selected index from view and change the content from outside
	 * @return
	 */
	public int getIndexOfView()
	{
		return this.indexOfView;
	}
	
	

	private void setNodePressed(Arc nodeBase , Text textNode, Command command, MouseEvent e) 
	{
		nodeBase.setEffect(innerShadow);
		textNode.setEffect(textGlow);
		commandProperty.set(command);
		e.consume();
		
		
	
	}

	private void setNodeReleased(Arc nodeBase, Text textNode, MouseEvent e) 
	{
		nodeBase.setEffect(dropShadow);
		textNode.setEffect(null);
		e.consume();
		
	}
	
	public SimpleObjectProperty<Command> getCommandProperty()
	{
		return commandProperty;
	}
	
	private void drawTextValues(boolean clearing) 
	{
		//Size wird für die Ausrichtung benötigt
		double gaugeSize  = getWidth() < getHeight() ? getWidth() : getHeight();
		double w = textCanvas.getWidth();
		double h = textCanvas.getHeight();
		double x = textCanvas.getLayoutX();
		double y = textCanvas.getLayoutY();
		GraphicsContext gc = textCanvas.getGraphicsContext2D();
		
		
		if(scaleableFontSize == null)
		{
			scaleableFontSize = new SimpleDoubleProperty(gaugeSize * 0.12);
		}
		else
			scaleableFontSize.set(gaugeSize * 0.12);
		
		if(fontVorgabe == null)
		{
			fontVorgabe = new Label().getFont();
		}
		
		
		
		Font font = Font.font(fontVorgabe.getName(), scaleableFontSize.get());
		
		
		//Dieses ist dann zu vollziehen, wenn nur der Wert sich geändert hat.
		if(clearing)
		{
		
			gc.clearRect(0, 0, w, h);
		}
		

		//TODO raus
		gc.setFill(Color.RED);
		gc.fillRect(0, 0, w,h);
		
		
		
		
		
		gc.setFill(Color.web("#00000080"));
		
		
		//Fontsize muss ermittelt werden anhand des größten values
		
		
		
		if(mainValueToShow != null)
		{
			
			
			
			
			//initial
			 //Ermittlung nach dem maximal möglichen Zustand
			 Bounds maxTextAbmasse = this.getMaxTextWidth(font, this.mainValueToShow);
			 
			 System.out.println("fontsize vorher " + getFontSize().get());
			 if(maxTextAbmasse.getWidth() < w  && maxTextAbmasse.getHeight() < h)
			 {
				 double tempSize = getGreaterFont(gaugeSize * 0.12, w, h, mainValueToShow);
					if(tempSize != getFontSize().get())
						getFontSize().set(tempSize);
				 
			 }
			 else
			 {
				 double tempSize = getLesserFont(getFontSize().get(), w, h, mainValueToShow);
					if(tempSize != getFontSize().get())
						getFontSize().set(tempSize);
			 }
			 font = Font.font(fontVorgabe.getName(), getFontSize().get());
			
			 
			 System.out.println("textAbmasse " + maxTextAbmasse.getWidth() + " " + maxTextAbmasse.getHeight());
			 System.out.println("textcanvas size w " + w + " h "+ h);
			 System.out.println("fontsize nachher " + getFontSize().get());
			 
			 
			 
			 
			
		}
		gc.setFont(font);
		
		if(mainValueToShow == null)
			textFirstMeasuringUnit.setText("");
		else
			textFirstMeasuringUnit.setText(" "+mainValueToShow.getMeasurementUnit());
		textFirstMeasuringUnit.setFont(font);
		
		//TODO doch aufteilen in zwei Felder?
		
		double masseinheitX = w - (textFirstMeasuringUnit.getLayoutBounds().getWidth());// + (gaugeSize * 0.015635));
	
		double haelfte =  textFirstMeasuringUnit.getLayoutBounds().getHeight() / 2d;
		double masseinheitY = h/2d +  (haelfte/2d);
		gc.fillText(textFirstMeasuringUnit.getText(), masseinheitX, masseinheitY);
			
		if(mainValueToShow == null)
			textFirstValue.setText("");
		else
			textFirstValue.setText(String.format("%.1f", mainValueToShow.getCurrentValue()));
		textFirstValue.setFont(font);
		
		double valueX = masseinheitX - (textFirstValue.getLayoutBounds().getWidth());//  + (gaugeSize * 0.015635));
		double valueY = masseinheitY;
		
		gc.setFont(font);
		gc.fillText(textFirstValue.getText(), valueX, valueY);
	}
	
	
	public DoubleProperty getFontSize()
	{
		return scaleableFontSize;
	}
	

	public double getGAPPercent()
	{
		return GAP_PERCENT;
	}
	

	private Bounds textWidth(double size, SensorValue sensorValue)
	{
		//hier muss die bounds aufgebaut werden anhand der zwei darzustellenden Werte 
		
		String showValue = sensorValue.getCurrentValue() + " " + sensorValue.getMeasurementUnit();
		
		if(fontVorgabe == null)
			fontVorgabe = new Label().getFont();
		
		
		
		
		
		Text text = new Text(showValue);
		Font font =  Font.font(fontVorgabe.getFamily(), size);
        text.setFont(font);
        return text.getBoundsInLocal();
	}

	
	protected double getGreaterFont(double fontSize, double w, double h, SensorValue sensorValue)
	{	
		double gapBreite = w * getGAPPercent() * 2;
		double gapHoehe = h * getGAPPercent() * 2;
		
		fontSize = fontSize + 1;
		Bounds futureBounds = textWidth(fontSize, sensorValue);
		if((futureBounds.getHeight() + gapHoehe) < h && (futureBounds.getWidth() + gapBreite) < w)
		{
			return getGreaterFont(fontSize, w, h, sensorValue);
		}
		//eines wieder zurück weil die Abfrage nicht gegriffen hat
		return fontSize-1;
	}


	protected double getLesserFont(double fontSize, double w, double h, SensorValue sensorValue)
	{	
		Bounds futureBounds = textWidth(fontSize, sensorValue);
		double gapBreite = w * getGAPPercent() * 2;
		double gapHoehe = h * getGAPPercent() * 2;
		//wenn eines von beiden über das Ziel hinaus ist, so ist eine kleiner Fontgröße zu ermitteln
		if((futureBounds.getHeight() + (gapHoehe)) > h || (futureBounds.getWidth() + (gapBreite)) > w)
		{
			fontSize = fontSize - 1;
			if(fontSize <= 0)
				return 1;
			return getLesserFont(fontSize, w, h, sensorValue);
		}
		return fontSize;
	}
	
	
	private Bounds getMaxTextWidth(Font font, SensorValue valueSensor) 
	{
		String minValue = String.format("%.1f", valueSensor.getVon());
		String maxValue = String.format("%.1f", valueSensor.getBis());
		
		String measuringUnit = " " + valueSensor.getMeasurementUnit();
		
		Text valTextMin = new Text(minValue);
		valTextMin.setFont(font);
		
		Bounds valMinBounds = valTextMin.getBoundsInLocal();
		
		Text valTextMax = new Text(maxValue);
		valTextMax.setFont(font);
		
		Bounds valMaxBounds = valTextMax.getBoundsInLocal();
		
		Text messText = new Text(measuringUnit);
		messText.setFont(font);
		
		Bounds einheitBounds = messText.getBoundsInLocal();
		
		double width = valMinBounds.getWidth();
		if(width < valMaxBounds.getWidth())
			width = valMaxBounds.getWidth();
		
		double height = valMinBounds.getHeight();
		if(height < valMaxBounds.getHeight())
			height = valMaxBounds.getHeight();
		
		if(height < einheitBounds.getHeight())
			height = einheitBounds.getHeight();
		
		width = width + einheitBounds.getWidth();
		
		System.out.println("min " + valMinBounds.getWidth() + " max " + valMaxBounds.getWidth());
		
		return new BoundingBox(0,0, width, height);
	}

	/**
	 * Parameter im Regelfall true, außer man will die Sicht gelöscht haben
	 * @param showValues
	 */
	private void drawSecondTextValue(boolean showValues)
	{
		double size  = getWidth() < getHeight() ? getWidth() : getHeight();
		double w = textSecondValueCanvas.getWidth();
		double h = textSecondValueCanvas.getHeight();
		GraphicsContext gc = textSecondValueCanvas.getGraphicsContext2D();
		
		gc.clearRect(0, 0, w, h);
		
		//bei löschung der Sicht gleich wieder zurück
		if(!showValues)
			return;
		
		//TODO raus
		gc.setFill(Color.ALICEBLUE);
		gc.fillRect(0, 0, w,h);

		
		
		if(scaleableMinorFontSize == null)
		{
			scaleableMinorFontSize = new SimpleDoubleProperty(size * 0.06);
		}
		else
			scaleableMinorFontSize.set(size * 0.06);
		
		if(fontMinorVorgabe == null)
		{
			fontMinorVorgabe = new Label().getFont();
		}
		
		Font font = Font.font(fontMinorVorgabe.getName(), scaleableMinorFontSize.get());
		
		gc.setFill(Color.web("#00000080"));
		
		if(minorValueToShow != null)
		{
			//initial
			 //Ermittlung nach dem maximal möglichen Zustand
			 Bounds maxTextAbmasse = this.getMaxTextWidth(font, this.minorValueToShow);
			 
			 System.out.println("fontsize vorher " + getFontSize().get());
			 if(maxTextAbmasse.getWidth() < w  && maxTextAbmasse.getHeight() < h)
			 {
				 double tempSize = getGreaterFont(scaleableMinorFontSize.get(), w, h, mainValueToShow);
					if(tempSize != scaleableMinorFontSize.get())
						scaleableMinorFontSize.set(tempSize);
				 
			 }
			 else
			 {
				 double tempSize = getLesserFont(scaleableMinorFontSize.get(), w, h, mainValueToShow);
					if(tempSize != scaleableMinorFontSize.get())
						scaleableMinorFontSize.set(tempSize);
			 }
			 font = Font.font(fontVorgabe.getName(), getFontSize().get());
			
			 
			
		}
		gc.setFont(font);
		
		String valueToShow = "";
		if(minorValueToShow != null)
		{
			valueToShow = String.format("%.0f", minorValueToShow.getCurrentValue());
			valueToShow = valueToShow + " " + minorValueToShow.getMeasurementUnit();
		}
		
		
		//erstmal zum test preset drei auswählen
		//Font valueFont = new Font("Verdana", size * 0.06);
		
		//String valueToShow = "";
		//if(minorValueToShow != null)
		//{
		//	valueToShow = String.format("%.0f", minorValueToShow.getCurrentValue());
		//	valueToShow = valueToShow + " " + minorValueToShow.getMeasurementUnit();
		//}
		
		
		textSecondValue.setText(valueToShow);
		textSecondValue.setFont(font);
		
		double valueX = w - (textSecondValue.getLayoutBounds().getWidth());//  + (size * 0.018635));
		double haelfte =  textFirstMeasuringUnit.getLayoutBounds().getHeight() / 2d;
		double valueY = h/2d +  (haelfte/2d);
	
		//sdouble valueY = (textSecondValue.getLayoutBounds().getHeight()  + (size * 0.015635));
		
		gc.setFont(font);
		gc.fillText(textSecondValue.getText(), valueX  , valueY);
	}

	

	public void setMainContent(SensorValue sensorValue) {
		mainValueToShow = sensorValue;
	}

	public void setMinorContent(control.universaldisplay.SensorValue sensorValue){
		//kann auch zur Laufzeit null sein, dann wird nichts angezeigt.
		minorValueToShow = sensorValue;
	}

	/**
	 * von außerhalb kann ein repaint der werte erzwungen werden.
	 */
	public void repaintValues() 
	{
		this.drawTextValues(true);
		this.drawSecondTextValue(true);
		
	}
	

}
