package control.universaldisplay;

import java.util.HashMap;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

public class UniversalDisplay extends Region
{
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
	

	public UniversalDisplay()
	{

		this.initGraphics();
		this.registerListener();
	}

	private void registerListener() {
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
		
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
		
		//x1 100/128 * 52.6787 = 41,155234375 = 0.41155234375
		//y1 100/128 * 2.2106 = 1,72703125 = 0.0172703125
		//x2 100/128 * 62.7276 = 49,0059375 = 0.490059375
		//x3 100/128 * 60,4944 = 47,26125 = 0.4726125
		
		
		LinearGradient lgTopShiny = new LinearGradient(
				(size * 0.41155234375),
				(size * 0.0172703125),
				(size * 0.490059375),
				 (size  *  0.4726125), 
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
	   
	   
	   topRegion = new TopRegion(new Circle(), new Circle(), new Circle());
	   //TODO gradient wie läuft es hier?
	   
	   this.getChildren().addAll(background, backgroundCircle, innerBackground, innerBackgroundCircle, innerBorder, topoverlay,
			   rahmenInnenring, lcdDisplay, scheinLCD, glanzUnten, topRegion);
	}


}
