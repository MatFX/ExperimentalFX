package control.rgbw;

import java.util.HashMap;
import java.util.TreeMap;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Affine;

/**
 * 128x128 Orginalabmasse
 * @author m.goerlich
 *
 */
public class RGBWDimmerControl extends Region
{
	
	public enum StopIndizes
	{
		RAHMEN_GLANZ,
		
		INLAY,
		
		OVERLAY_INNER_CIRCLE,
		
		BORDER_INNERVIEW, INNERVIEW_SHINY
	}
	
	
	private double width = 128, height = 128;
	
	private double centerX = 64, centerY = 64;
	
	private Circle hintergrund, rahmen_grundfarbe, rahmen_glanz, basisFarbe;
	
	/**
	 * rgb selector
	 */
	private Canvas canvasColoredCircle;

	private Circle wahlkreis;
	
	private Circle begrenzerInlay, deckflaechenBegrenzung, deckflaechenBegrenzungOverlay;
	
	/**
	 * gradient color
	 */
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	

	/**
	 * <br>ticks on screen for the white channel
	 */
	private TreeMap<Integer, Line> tickMap = new TreeMap<Integer, Line>();
	
	/**
	 * Inner circle for the text view
	 */
	private Circle basisAnzeige, anzeigeGlanzRahmen;
	
	/**
	 * show the current rgb value
	 */
	private Circle anzeigeRGB;
	
	private Circle anzeigeGlanz;
	
	private Color currentColorRGB = Color.web("#1A1A96");
	
	
	public RGBWDimmerControl()
	{
		this.initGraphics();
		this.registerListener();
	}

	private void registerListener() {
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
		
	}

	private void initGraphics() 
	{
		
		hintergrund = new Circle();
		hintergrund.setFill(Color.BLACK);
		
		rahmen_grundfarbe = new Circle();
		rahmen_grundfarbe.setFill(Color.web("#878787"));
		
		Stop[] stopArray = new Stop[]{
				new Stop(0.0, Color.web("#FFFFFFB3")),
				new Stop(0.0313697, Color.web("#DDDDDDAF")),
				new Stop(0.0693688, Color.web("#BCBCBCAB")),
				new Stop(0.1083576, Color.web("#A2A2A2A7")),
				new Stop(0.1482032, Color.web("#8F8F8FA2")),
				new Stop(0.1894206, Color.web("#8484849E")),
				new Stop(0.2341463, Color.web("#80808099")),
				new Stop(0.3167808, Color.web("#A0A0A0BD")),
				new Stop(0.4126251, Color.web("#C0C0C0E8")),
				new Stop(0.4658537, Color.web("#CCCCCC")),
				new Stop(0.4788288, Color.web("#D0D0D0F9")),
				new Stop(0.6243902, Color.web("#FFFFFFB3")),
				new Stop(0.7780488, Color.web("#FFFFFF")),
				new Stop(0.8263174, Color.web("#F1F1F1F3")),
				new Stop(0.8858387, Color.web("#E9E9E9E4")),
				new Stop(0.9829268, Color.web("#E6E6E6CC"))
			};
		
		stopMap.put(StopIndizes.RAHMEN_GLANZ, stopArray);
		
		rahmen_glanz = new Circle();
		
		basisFarbe = new Circle();
		basisFarbe.setFill(Color.web("#282828"));
		
		wahlkreis = new Circle();
		
		canvasColoredCircle = new Canvas();
		
		stopArray = new Stop[]{
				new Stop(0.7414634, Color.web("#00000000")),
				new Stop(0.9379081, Color.web("#00000083")),
				new Stop(0.9702439, Color.web("#00000099")),
				new Stop(1.0, Color.web("#000000"))
			};
		stopMap.put(StopIndizes.INLAY, stopArray);
		
		begrenzerInlay = new Circle();
		
		deckflaechenBegrenzung = new Circle();
		deckflaechenBegrenzung.setFill(Color.web("#282828"));
		
		stopArray = new Stop[]{
				new Stop(0.005618, Color.web("#666666B3")),
				new Stop(0.3678385, Color.web("#4D4D4DCE")),
				new Stop(1.0, Color.web("#1A1A1A"))
			};
		stopMap.put(StopIndizes.OVERLAY_INNER_CIRCLE, stopArray);
		
		deckflaechenBegrenzungOverlay = new Circle();
				
		this.getChildren().addAll(hintergrund, rahmen_grundfarbe, rahmen_glanz, basisFarbe, canvasColoredCircle, begrenzerInlay,
				deckflaechenBegrenzung, deckflaechenBegrenzungOverlay);
		
		drawMinorTick(width, true);
		
		basisAnzeige = new Circle();
		basisAnzeige.setFill(Color.web("#191919"));
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#FFFFFF")),
				new Stop(0.5317073, Color.web("#33333300")),
				new Stop(0.5902743, Color.web("#48484823")),
				new Stop(0.7116535, Color.web("#7F7F7F6B")),
				new Stop(0.8838744, Color.web("#D6D6D6D2")),
				new Stop(0.9602357, Color.web("#FFFFFF"))
			};
		
		stopMap.put(StopIndizes.BORDER_INNERVIEW, stopArray);
		
		anzeigeGlanzRahmen = new Circle();
		
		anzeigeRGB = new Circle();
		anzeigeRGB.setFill(currentColorRGB);
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#9BA38800")),
				new Stop(0.2615826, Color.web("#9DA58B2F")),
				new Stop(0.436628, Color.web("#A5AC944E")),
				new Stop(0.5863504, Color.web("#B1B8A369")),
				new Stop(0.7217618, Color.web("#C3C8B882")),
				new Stop(0.8475584, Color.web("#DBDED498")),
				new Stop(0.964749, Color.web("#F7F7F5AD")),
				new Stop(0.994382, Color.web("#FFFFFFB3"))
			};
		
		
		stopMap.put(StopIndizes.INNERVIEW_SHINY, stopArray);
		
		anzeigeGlanz = new Circle();
		anzeigeGlanz.setOpacity(0.38);
		
		
		this.getChildren().addAll(basisAnzeige, anzeigeGlanzRahmen, anzeigeRGB, anzeigeGlanz);
	}
	

	private void resize() 
	{
		
		double size  = getWidth() < getHeight() ? getWidth() : getHeight();
		
		if(getHeight() > 0)
			centerY = getHeight() / 2d;
		
		if(getWidth() > 0)
			centerX = getWidth() / 2d;
		
		double radius = size / 2d;
		
		hintergrund.setCenterX(centerX);
		hintergrund.setCenterY(centerY);
		hintergrund.setRadius(radius);
		
		rahmen_grundfarbe.setCenterX(centerX);
		rahmen_grundfarbe.setCenterY(centerY);
		rahmen_grundfarbe.setRadius(radius);
		
		
		
		//linear x1="83.6037292" y1="14.8057451" x2="48.2703934" y2="103.4724121"
		//x1 = 83.6037292 - 64 = 19,6037292 ; 100/128 * 19,6037292 = 0.153154134375
		//y2 = 49,1942549; 100/128 * 49,1942549 = 0.38433011640625
		//x2 = 15,7296066; 100/128 * 15,7296066 = 0.1228875515625
		//y2 = 39,4724121; 100/128 * 39,4724121 = 0.30837821953125
		LinearGradient linearRahmenGlanz = new LinearGradient(centerX + (size * 0.153154134375), 
				centerY - (size * 0.38433011640625),
				centerX - (size * 0.1228875515625), 
				centerY + (size *  0.30837821953125), false, 
				CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.RAHMEN_GLANZ));
		
		
		rahmen_glanz.setCenterX(centerX);
		rahmen_glanz.setCenterY(centerY);
		rahmen_glanz.setRadius(radius);
		rahmen_glanz.setFill(linearRahmenGlanz);

		//r = 61
		//100/64 * 61 = 0.953125
		basisFarbe.setCenterX(centerX);
		basisFarbe.setCenterY(centerY);
		basisFarbe.setRadius(radius * 0.953125);
		
		
		//wird nicht direkt dargestellt sondern wird nur als Schablone für die spätere Zeichnung der Farbe benötigt.
		wahlkreis.setCenterX(centerX);
		wahlkreis.setCenterY(centerY);
		wahlkreis.setRadius(radius*0.9375);
		
		
		//r = 60 100/64*60 = 0.9375
		
		canvasColoredCircle.setWidth(radius*0.9375*2);
		canvasColoredCircle.setHeight(radius*0.9375*2);
		canvasColoredCircle.setLayoutX(centerX - (radius*0.9375));
		canvasColoredCircle.setLayoutY(centerY- (radius*0.9375));
		
		drawColoredCircle();
		
		
		RadialGradient inlayGrad = new RadialGradient(0D, 0D, centerX, centerY, radius * 0.953125, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.INLAY));
		
		begrenzerInlay.setCenterX(centerX);
		begrenzerInlay.setCenterY(centerY);
		//radius von der basisfarbe aus ausgangspunkt
		begrenzerInlay.setRadius(radius * 0.953125);
		begrenzerInlay.setFill(inlayGrad);
		//evtl. noch nachbessern
		begrenzerInlay.setOpacity(0.5f);
		
		
		deckflaechenBegrenzung.setCenterX(centerX);
		deckflaechenBegrenzung.setCenterY(centerY);
		//r = 45 = 100/64 * 45 = 0.703125
		deckflaechenBegrenzung.setRadius(radius * 0.703125);
		
		
		RadialGradient overlayCircleRadial = new RadialGradient(0D, 0D, centerX, centerY, radius * 0.703125, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.OVERLAY_INNER_CIRCLE));
		
		
		deckflaechenBegrenzungOverlay.setCenterX(centerX);
		deckflaechenBegrenzungOverlay.setCenterY(centerY);
		//r = 45 = 100/64 * 45 = 0.703125
		deckflaechenBegrenzungOverlay.setRadius(radius * 0.703125);
		deckflaechenBegrenzungOverlay.setFill(overlayCircleRadial);
		
		
		//ticks for the "white" channel color
		
		drawMinorTick(size, false);
		
		basisAnzeige.setCenterX(centerX);
		basisAnzeige.setCenterY(centerY);
		//r 32.5 = 100/64 * 32.5 = 0.5078125
		basisAnzeige.setRadius(radius * 0.5078125);
		
		
		//x1="42.6201401" y1="45.1309433" x2="84.3724976" y2="81.9800034"
		//x1 = 64 - 42.6201401 = 21,3798599 = 100/128 * 21,3798599 = 16,703015546875
		//y1 = 64 -45.1309433 = 18,8690567 = 100/128 * 18,8690567 = 14,741450546875
		//x2 = 84.3724976 -64 = 20,3724976 = 100/128 * 20,3724976 = 15,91601375
		//y2 = 81.9800034 - 64 = 17,9800034 = 100/128 * 17,9800034 =  14,04687765625
		
		LinearGradient innerViewGradient = new LinearGradient(centerX - (size * 0.16703015546875), 
				centerY - (size * 0.14741450546875),
				centerX + (size * 0.1591601375), 
				centerY + (size *  0.1404687765625), false, 
				CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.RAHMEN_GLANZ));
		
		anzeigeGlanzRahmen.setCenterX(centerX);
		anzeigeGlanzRahmen.setCenterY(centerY);
		//r 31.5 = 100/64 * 31.5 = 49,21875
		anzeigeGlanzRahmen.setRadius(radius * 0.4921875);
		anzeigeGlanzRahmen.setFill(innerViewGradient);
		
		
		anzeigeRGB.setCenterX(centerX);
		anzeigeRGB.setCenterY(centerY);
		//r 30; 100/64  30 = 46,875
		anzeigeRGB.setRadius(radius * 0.46875);
		
		
		//R 25 = 100/64 * 25 = 39,0625
		
		RadialGradient innerShinyGradient = new RadialGradient(0D, 0D, centerX, centerY, radius * 0.390625, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.INNERVIEW_SHINY));
		
		
		anzeigeGlanz.setCenterX(centerX);
		anzeigeGlanz.setCenterY(centerY);
		//r 30; 100/64  30 = 46,875
		anzeigeGlanz.setRadius(radius * 0.46875);
		anzeigeGlanz.setFill(innerShinyGradient);
		
	}
	
	/**
	 * Zeichnet die Ticks für die spätere Helligkeits Darstellung
	 * @param sizeNow
	 * @param isInit
	 */
	private void drawMinorTick(double sizeNow, boolean isInit) 
	{
		double r = sizeNow * 0.3325;
		//tick länge ist 10 ==> 100/128 * 10 = 0.078125
		double tickLenMinor = sizeNow *  0.065125;
		//strokewidth orginal 2 = 100 / 128 *2 => 1,5625 => 0.015625
		double strokeWidth = sizeNow * 0.015625;
		
		double START_ANGLE = 0;
		
		double stepAngle = 5; //(180 / (n-1))
		double nextAngle = START_ANGLE;
		double cosValue;
		double sinValue;
		
		
		 for(int i = 1; i <= 37; i++)
		 {
			 sinValue = Math.sin(Math.toRadians(nextAngle));
			 cosValue = Math.cos(Math.toRadians(nextAngle));
		
			 Line line = null;
			 if(isInit)
			 {
				 line = new Line();
				 //Darf hier nur einmalig gesetzt werden, weil zur Laufzeit sich die Farbe ändern kann.
				 line.setStroke(Color.web("#626262"));
			 }
			 else
				 line = tickMap.get(i);
			 
		     line.setStartX((float)(centerX - cosValue*r));
		     line.setStartY( (float)(centerY - sinValue*r));
		     line.setEndX((float)(centerX - cosValue*(r - tickLenMinor)));
		     line.setEndY((float)(centerY - sinValue*(r - tickLenMinor)));
		     line.setStrokeWidth(strokeWidth);
		     
		     Glow glow = new Glow();
		     glow.setLevel(0.5);
		     line.setEffect(glow);
		     if(isInit)
		     {
		    	 this.getChildren().add(line);
		    	 tickMap.put(i, line);
		     }
		     nextAngle = nextAngle + stepAngle;
		   
		 }
		 
		
	}

	
	

	
	private void drawColoredCircle() 
	{
		GraphicsContext gc = canvasColoredCircle.getGraphicsContext2D();
		gc.clearRect(0, 0, canvasColoredCircle.getWidth(), canvasColoredCircle.getHeight());
	
		//TODO raus
		//gc.setFill(Color.ALICEBLUE);
		//gc.fillRect(0, 0, canvasColoredCircle.getWidth(), canvasColoredCircle.getHeight());

		

		Color color = new Color(0, 0, 0, 0);
		
		java.awt.Color farbe = new java.awt.Color(0, 0, 0, 0);
		
		for (int i = 0; i< 360; i++)
		{
			color = Color.hsb(i, 1f, 1f);
			gc.setFill(color);
			//winkel von 2 wegen moire effekten, ist aber kein problem, da es direkt vom nachfolger überschrieben wird
			gc.fillArc(0, 
					   0,
						wahlkreis.getRadius()*2, wahlkreis.getRadius()*2, i + 90, 2, ArcType.ROUND);
		}
		
		
		
	}


}
