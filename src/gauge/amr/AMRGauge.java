package gauge.amr;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import control.dimmer.IActivationIcon.Pos;
import control.dimmer.OptionalImageBox;
import control.universaldisplay.SensorValue;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import tools.helper.ImageLoader;
import tools.helper.Tools;


/**
 * Illustrator Abmaße 128*128 und centerX/Y 64/64
 * @author m.goerlich
 *
 */
public class AMRGauge extends Region
{
	private double centerX = 64;
	private double centerY = 64;
	
	private Thread animThread = null;
	private boolean isAnimation = false;
	/**
	 * percent range needle
	 */
	private double RANGE_MIN = 0D;
	
	private double RANGE_MAX = 100D;
	
	/**
	 * needle value is always percent
	 */
	private double percentValueNeedle = 50D;
	
	//evtl. hintergrund nicht benötigt
	private Circle hintergrund;
	
	private Circle rahmen_hintergrundfarbe;
	
	private Circle rahmen_glanz;
	
	private Circle basis_farbe;
	
	private Arc greenSegment, yellowSegment, redSegment;
	
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	private float STARTING_ANGLE_RED = 0f;
	
	private float startingAngleYellow = 30f;
	
	private float endingAngleYellow = 90f;
	
	private Circle inlayRand;
	
	private Circle deckflaecheBegrenzer;
	
	private Rectangle deckflaecheRechteck;
	
	private Arc segementInlay;
	
	
	private Polygon backgroundNeedle, foregroundNeedle;
	
	private Circle backgroundNeedlePick;
	
	private DropShadow dropNeedleShadow;
	
	/**
	 * Rotation of the needle. 
	 */
	private Rotate needleRotate;
	
	private Circle basisAnzeige, anzeigeGlanzRahmen, anzeigeHintergrund, anzeigeGlanz;
	
	/**
	 * majorvalue is represented with needle and the middle view
	 */
	private SensorValue majorValue;
	
	/**
	 * minorvalue for the rectangle view.
	 */
	private SensorValue minorValue;
	
	private Canvas textCanvas;
	
	/**
	 * optional images 
	 */
	private OptionalImageBox optionalImageBox;
	
	private DoubleProperty scaleableFontSize = null;
	
	private Font fontVorgabe = null;
	
	private static final double GAP_PERCENT = 0.1;
	
	/**
	 * first Value or main value; Bigger from font size
	 */
	private Text textFirstValue;
	
	private Text textFirstMeasuringUnit;
	
	
	private Rectangle rectangleBackgroundLCD, rectangleLCD, rectLCDOverlay;
	
	
	private Text textCounterValue;
	
	/**
	 * Hier wird nur die Masseinheit abgelegt.
	 */
	private Text textMeasurement;
	
	private Canvas canvasCounterValue;
	
	
	
	
	//Enums für die gespeicherten Farben
	public enum StopIndizes
	{
		/**
		 * outside border
		 */
		RAHMEN_GLANZ,
		
		INLAY_BORDER,
		
		INLAY_SEGMENT, 
		
		/**
		 * Shiny border for the middle view
		 */
		VIEW_BORDER,
		
		VIEW_TOP_SHINY, BACKGROUND_LCD, LCD_SHINY;
	}

	public AMRGauge()
	{

		this.initGraphics();
		this.registerListener();
	}
	

	private void registerListener() 
	{
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
	}

	private void initGraphics() 
	{
		
		hintergrund = new Circle();
		hintergrund.setFill(Color.BLACK);
		
		rahmen_hintergrundfarbe = new Circle();
		rahmen_hintergrundfarbe.setFill(Color.web("#878787"));
		
		
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
		
		basis_farbe = new Circle();
		basis_farbe.setFill(Color.web("#282828"));
		
		greenSegment = new Arc();
		greenSegment.setFill(Color.web("#4B9900"));
		
		yellowSegment = new Arc();
		yellowSegment.setFill(Color.web("#B5A800"));
		
		redSegment = new Arc();
		redSegment.setFill(Color.web("#D80015"));
		
		deckflaecheRechteck = new Rectangle();
		deckflaecheRechteck.setFill(Color.web("#282828"));
		//deckflaecheRechteck.setFill(Color.BLUE);
		
		stopArray = new Stop[]{
				new Stop(0.8, Color.web("#33333300")),
				new Stop(0.90008, Color.web("#31313142")),
				new Stop(0.9361322, Color.web("#2A2A2A59")),
				new Stop(0.9618216, Color.web("#1F1F1F6A")),
				new Stop(0.9824584, Color.web("#0E0E0E78")),
				new Stop(0.994382, Color.web("#00000080"))
			};
		stopMap.put(StopIndizes.INLAY_BORDER, stopArray);
		inlayRand = new Circle();
		
		deckflaecheBegrenzer = new Circle();
		deckflaecheBegrenzer.setFill(Color.web("#282828"));
		
		
		 stopArray = new Stop[]{
					new Stop(0.7414634, Color.web("#00000000")),
					new Stop(0.9379081, Color.web("#00000083")),
					new Stop(0.9702439, Color.web("#00000099")),
					new Stop(1.0, Color.web("#000000"))
				};
		stopMap.put(StopIndizes.INLAY_SEGMENT, stopArray);
		segementInlay = new Arc();
		

		backgroundNeedle = new Polygon();
		backgroundNeedlePick = new Circle();
		foregroundNeedle = new Polygon();
		
		//preparation needle rotation
		needleRotate = new Rotate(0);
		
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
		stopMap.put(StopIndizes.VIEW_BORDER, stopArray);
		
		anzeigeGlanzRahmen = new Circle();
		
		anzeigeHintergrund = new Circle();
		anzeigeHintergrund.setFill(Color.BLACK);
		
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
		stopMap.put(StopIndizes.VIEW_TOP_SHINY, stopArray);
		anzeigeGlanz = new Circle();
		anzeigeGlanz.setOpacity(0.38);
		
		textCanvas = new Canvas();
		textFirstValue = new Text();
		textFirstValue.setText("");
		textFirstMeasuringUnit = new Text();
		textFirstMeasuringUnit.setText("");
		
		optionalImageBox = new OptionalImageBox();
		
		optionalImageBox.initImage(Pos.MIDDLE, ImageLoader.getImageFromIconFolder("amr_electricity"));
		optionalImageBox.setActivation(Pos.MIDDLE);
		
	
		 stopArray = new Stop[]{
				new Stop(0.0, Color.web("#1A1A1A99")),
				new Stop(0.0670049, Color.web("#0F0F0FB0")),
				new Stop(0.1792437, Color.web("#040404D7")),
				new Stop(0.2970297, Color.web("#000000")),
				new Stop(0.3399396, Color.web("#080808E9")),
				new Stop(0.407074, Color.web("#1E1E1EC7")),
				new Stop(0.4900234, Color.web("#4343439C")),
				new Stop(0.5846764, Color.web("#7575756C")),
				new Stop(0.6460396, Color.web("#9999994D")),
				new Stop(0.6759919, Color.web("#8A8A8A58")),
				new Stop(0.7626889, Color.web("#6565657B")),
				new Stop(0.8430177, Color.web("#4949499B")),
				new Stop(0.9138942, Color.web("#393939B7")),
				new Stop(0.9678218, Color.web("#333333CC")),
			
			};
		stopMap.put(StopIndizes.BACKGROUND_LCD, stopArray);
		
		rectangleBackgroundLCD = new Rectangle();
		rectangleLCD = new Rectangle();
		rectangleLCD.setFill(Color.web("#6F7C3E"));

		rectLCDOverlay = new Rectangle();
		stopArray = new Stop[]{
				new Stop(0.005618, Color.web("#FFFFFF80")),
				new Stop(1.0, Color.web("#FFFFFF00"))
			};
		stopMap.put(StopIndizes.LCD_SHINY, stopArray);
		
		
		textCounterValue = new Text();
		textMeasurement = new Text();
		//vor the minor text value
		canvasCounterValue = new Canvas();
		textCounterValue = new Text();
		textMeasurement = new Text();
		
		
		
		this.getChildren().addAll(hintergrund, rahmen_hintergrundfarbe, rahmen_glanz, basis_farbe, 
				greenSegment, yellowSegment, redSegment,  segementInlay, backgroundNeedle, backgroundNeedlePick, foregroundNeedle,
				deckflaecheRechteck,  deckflaecheBegrenzer, inlayRand,
				basisAnzeige, anzeigeGlanzRahmen, anzeigeHintergrund, anzeigeGlanz, optionalImageBox, textCanvas,
				rectangleBackgroundLCD, rectangleLCD,  rectLCDOverlay,  canvasCounterValue);
	}
	

	private void resize() 
	{
	
		double size  = getWidth() < getHeight() ? getWidth() : getHeight();
		
		if(getHeight() > 0)
			centerY = getHeight() / 2d;
		
		if(getWidth() > 0)
			centerX = getWidth() / 2d;
		
		double radius = size / 2d;
		
		//volle Größe
		hintergrund.setCenterX(centerX);
		hintergrund.setCenterY(centerY);
		hintergrund.setRadius(radius);
		
		//volle Größe
		rahmen_hintergrundfarbe.setCenterX(centerX);
		rahmen_hintergrundfarbe.setCenterY(centerY);
		rahmen_hintergrundfarbe.setRadius(radius);
		
		//x1="83.6037292" y1="14.8057451" x2="48.2703934" y2="103.4724121"
		
		//x1 = 83.6037292 - 64 = 19,6037292 = 100/128 * 19,6037292  = 0.153154134375
		//y1 = 64 - 14.8057451 = 49,1942549 = 100/128 * 49,1942549 = 0.38433011640625
		//x2 = 64 - 48.2703934 = 15,7296066 = 100/128 * 15,7296066 = 0.1228875515625
		//y2 = 103.4724121 - 64 =  39,4724121 = 100/128 * 39,4724121 = 0.30837821953125
		
		LinearGradient rahmenGlanzGradient = new LinearGradient(centerX + (size * 0.153154134375) , 
						centerY - (size * 0.38433011640625), 
						centerX - (size * 0.1228875515625), 
						centerY + (size * 0.30837821953125), 
						false, 
						CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.RAHMEN_GLANZ));
		
		rahmen_glanz.setCenterX(centerX);
		rahmen_glanz.setCenterY(centerY);
		rahmen_glanz.setRadius(radius);
		rahmen_glanz.setFill(rahmenGlanzGradient);
		
		//r=58
		// 100/64 = 90,625 = .90625
		basis_farbe.setCenterX(centerX);
		basis_farbe.setCenterY(centerY);
		basis_farbe.setRadius(radius * .90625);
		
		
		//hier der begrenzer für die drei farben
		greenSegment.setCenterX(centerX);
		greenSegment.setCenterY(centerY);
		//radius = 50
		//100/64 * 50 = 0.78125
		//greenSegment.setRadiusX(radius * 0.78125);
		greenSegment.setRadiusX(radius *  0.78125);
		//hier wird noch die Zugabe von Y wieder abgezogen
		greenSegment.setRadiusY(radius * 0.78125);
		greenSegment.setStartAngle(0f);
		greenSegment.setLength(180.0f);
		greenSegment.setType(ArcType.ROUND);
		
		
		yellowSegment.setCenterX(centerX);
		yellowSegment.setCenterY(centerY);
		yellowSegment.setRadiusX(radius *  0.78125);
		//hier wird noch die Zugabe von Y wieder abgezogen
		yellowSegment.setRadiusY(radius * 0.78125);
		yellowSegment.setStartAngle(startingAngleYellow);
		yellowSegment.setLength(endingAngleYellow);
		yellowSegment.setType(ArcType.ROUND);
		
		
		redSegment.setCenterX(centerX);
		redSegment.setCenterY(centerY);
		redSegment.setRadiusX(radius *  0.78125);
		//hier wird noch die Zugabe von Y wieder abgezogen
		redSegment.setRadiusY(radius * 0.78125);
		redSegment.setStartAngle(STARTING_ANGLE_RED);
		redSegment.setLength(startingAngleYellow);
		redSegment.setType(ArcType.ROUND);
		
		//cx="64" cy="64" r="58" g
		//100/64 * 58 = 0.90625
		RadialGradient radialInalyBorder = new RadialGradient(0D, 0D, centerX, centerY, radius *  0.90625, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.INLAY_BORDER));
		
		//14x64 u. 100x3
		//x1 = 64 - 14 = 50 = 100/128 * 50 =  0.390625
		deckflaecheRechteck.setX(centerX - (size * 0.390625));
		//y1 = 64
		deckflaecheRechteck.setY(centerY);
		//78,125
		deckflaecheRechteck.setWidth((size * 0.78125));
		//h = 3 = 100/128 * 3 = 0.0234375
		deckflaecheRechteck.setHeight(size * 0.0234375);
		
		
		//gleiche maße
		inlayRand.setCenterX(centerX);
		inlayRand.setCenterY(centerY);
		inlayRand.setRadius(radius *  0.90625);
		inlayRand.setFill(radialInalyBorder);
		
		
		//cx="63.2343216" cy="62.8514862" r="58"
		
		RadialGradient radialInlaySegment = new RadialGradient(0D, 0D, centerX, centerY, radius *  0.90625, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.INLAY_SEGMENT));
		
		segementInlay.setCenterX(centerX);
		segementInlay.setCenterY(centerY);
		//radius = 50
		//100/64 * 50 = 0.78125
		//greenSegment.setRadiusX(radius * 0.78125);
		segementInlay.setRadiusX(radius *  0.78125);
		//hier wird noch die Zugabe von Y wieder abgezogen
		segementInlay.setRadiusY(radius * 0.78125);
		segementInlay.setStartAngle(0f);
		segementInlay.setLength(180.0f);
		segementInlay.setType(ArcType.ROUND);
		segementInlay.setFill(radialInlaySegment);
		
		
		//cx="64" cy="64" r="30"
		deckflaecheBegrenzer.setCenterX(centerX);
		deckflaecheBegrenzer.setCenterY(centerY);
		//100/64 * 30 = 0.46875
		deckflaecheBegrenzer.setRadius(radius * 0.46875);
		
		drawNeedle(size);
		
		
		basisAnzeige.setCenterX(centerX);
		basisAnzeige.setCenterY(centerY);
		//r = 26
		//100/64 * 26 = 0.40625
		basisAnzeige.setRadius(radius * 0.40625);
		
		//x1="46.0137711" y1="48.12603" x2="81.1387711" y2="79.12603"
		//x1 64 - 46.0137711 = 17,9862289 = 100/128 * 17,9862289  = 0.14051741328125
		//y1 64 - 48.12603 =  15,87397 = 100/128 * 15,87397 = 0.124015390625
		//x2 81.1387711 - 64 = 17,1387711 = 100/128 * 17,1387711 = 0.13389664921875
		//y2 79.1260- 64 = 15,126 = 100/128 * 15,126 = 11,8171875 = 0.118171875
		LinearGradient innerViewGradient =  new LinearGradient(centerX - (size *  0.14051741328125) , 
				centerY - (size * 0.124015390625), 
				centerX + (size * 0.13389664921875), 
				centerY + (size * 0.118171875), 
				false, 
				CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.VIEW_BORDER));
		
		anzeigeGlanzRahmen.setCenterX(centerX);
		anzeigeGlanzRahmen.setCenterY(centerY);
		
		//r 26.5
		//100/64 * 26.5 = 0.4140625
		anzeigeGlanzRahmen.setRadius(radius * 0.4140625);
		anzeigeGlanzRahmen.setFill(innerViewGradient);
		
		anzeigeHintergrund.setCenterX(centerX);
		anzeigeHintergrund.setCenterY(centerY);
		//r = 25
		//100/64 * 25 = 39,0625
		anzeigeHintergrund.setRadius(radius * 0.390625);
		

		//45 = 100/128 * 45 = 0.3515625
		double w = size * 0.3515625;
		double h = size * 0.1;
		double x = centerX - (w/2); 
		double y = centerY - (h/2);
		textCanvas.setWidth(w);
		textCanvas.setHeight(h);
		textCanvas.relocate(x, y);
		
		
		
		//40px 100/128 * 40 = 0.3125
		double breiteImages = size * 0.3125;
		
		
		//10 px 100/128 * 10 = 7,8125
		double hoeheImages = size * 0.078125;
		
		optionalImageBox.setMinSize(breiteImages, hoeheImages);
		optionalImageBox.setLayoutX(centerX - (breiteImages/2));
		optionalImageBox.setLayoutY(centerY + (textCanvas.getHeight()/2));
		optionalImageBox.resize(hoeheImages);
		
		RadialGradient radialAnzeigeGlanz = new RadialGradient(0D, 0D, centerX, centerY, radius * 0.390625, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.VIEW_TOP_SHINY));
		
		
		
		anzeigeGlanz.setCenterX(centerX);
		anzeigeGlanz.setCenterY(centerY);
		//100/64 * 25 = 39,0625
		anzeigeGlanz.setRadius(radius * 0.390625);
		anzeigeGlanz.setFill(radialAnzeigeGlanz);
		
		
		//x="38" y="92.5"  width="52" height="11"/
		//x = 64 - 38 = 100/128 * 26 = 20,3125 = 0.203125
		//y1 = 92,5 -64 = 28,5 = 100/128 * 28,5 = 0.22265625
		//100/128 * 52 = 40,625 = 0.40625
		//100/128 * 11 = 0.0859375
		rectangleBackgroundLCD.setLayoutX(centerX - (size *  0.203125));
		rectangleBackgroundLCD.setLayoutY(centerY + (size * 0.22265625));
		rectangleBackgroundLCD.setWidth(size *  0.40625);
		rectangleBackgroundLCD.setHeight(size * 0.0859375);
		
		//100/128 * 5 = 
		LinearGradient backgroundLCD =  new LinearGradient(0, 
				-(size * 0.0490625),  
				0, 				
				rectangleBackgroundLCD.getHeight(),
				false, 
				CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.BACKGROUND_LCD));
		rectangleBackgroundLCD.setFill(backgroundLCD);
		

		//x="38.8218193" y="93.2256927" width="50.0230255" height="9.5486107"
		//64 - 38.8218193 = 25,1781807 = 100/128 * 25,1781807 = 0.19670453671875
		//93,2256927 - 64 = 29,2256927 = 100/128 * 29,2256927= 0.22832572421875
		//100/128 * 50.0230255 = 0.39080488671875
		//100/128 * 9.5486107 = 0.07459852109375
		rectangleLCD.setLayoutX(centerX - (size *  0.19670453671875));
		rectangleLCD.setLayoutY(centerY + (size *  0.22832572421875));
		rectangleLCD.setWidth(size *  0.39080488671875);
		rectangleLCD.setHeight(size * 0.07459852109375);
		
		
		rectLCDOverlay.setLayoutX(centerX - (size *  0.19670453671875));
		rectLCDOverlay.setLayoutY(centerY + (size *  0.22832572421875));
		rectLCDOverlay.setWidth(size *  0.39080488671875);
		rectLCDOverlay.setHeight(size * 0.07459852109375);
		
		LinearGradient lcdShiny =  new LinearGradient(
				0, 
				0,  
				0, 				
				rectLCDOverlay.getHeight(),
				false, 
				CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.LCD_SHINY));
		rectLCDOverlay.setFill(lcdShiny);
		
		
		canvasCounterValue.relocate(centerX - (size *  0.19670453671875), centerY + (size *  0.22832572421875));
		canvasCounterValue.setWidth(size *  0.39080488671875);	
		canvasCounterValue.setHeight(size * 0.07459852109375);
				
		
		//Muss immer gesetzt werden, damit auch die Nadel an der richtigen Position anliegt.
		this.setCurrentValue(this.percentValueNeedle, false);
		
		this.drawTextValues(true);
		
		this.drawMinorTextValue(true);
		
	}
	
	private void drawNeedle(double gaugeSize)
	{
		
		double radius = gaugeSize / 2d;
		//nadel beschreibung
		
		//canvas muss clearable sein damit später die Nadelbewegung dargestellt werden kann.
		
		//breite unteres ende 4px und oberes ende 2px
		//die Hoehe liegt zu beginn bei 52 wobei 50 nur der Zeiger sind
		
		//4px = 100/128 * 4 = 3,125% => 0,03125
		//2px = 100/128 * 2 = 1,5625% => 0,015625
		
		//abzug
		//1px = 100/128 * 1 = 0,78125 => 0,0078125
		
		//hoehe 
		//52px = 100/128 * 52 = 40,625 = 0.40625
		
		//50px = 100/128 * 50 = 39,0625	=> 0.390625
		
		//erst die alten entfernen
		backgroundNeedle.getPoints().clear();
		
		backgroundNeedle.getPoints().addAll(new Double[]{
				//cx - 1, cy - 44
				centerX - (gaugeSize *  0.0078125) , centerY - (gaugeSize * 0.34375),
				//cx + 1, cy - 44
				centerX + (gaugeSize *  0.0078125), centerY - (gaugeSize * 0.34375),
				  //cx + 2, cy
				centerX + (gaugeSize *  0.015625), centerY,
				//cx - 2, cy 
				centerX - (gaugeSize *  0.015625), centerY,
			  });
		backgroundNeedle.setFill(Color.RED);
		dropNeedleShadow = new DropShadow();
		dropNeedleShadow.setOffsetY(4.0f);
		dropNeedleShadow.setColor(Color.web("#6d6d6d"));
		backgroundNeedle.setEffect(dropNeedleShadow);
		
		backgroundNeedlePick.setRadius((radius * 0.015625));
		
		backgroundNeedlePick.setCenterX(centerX);
		backgroundNeedlePick.setCenterY(centerY - (gaugeSize * 0.34375)); 
		backgroundNeedlePick.setFill(Color.RED);
		
		//die forground ist nur eine kleine grafische Abstufung von der Nadel
		//zu beginn die Points alle löschen
		foregroundNeedle.getPoints().clear();
		//44px in der höhe 100/128 * 44 = 34,375% => 0.34375
		foregroundNeedle.getPoints().addAll(new Double[]{
				centerX, centerY - (gaugeSize * 0.34375),
				//2px bei y abstand
				centerX + (gaugeSize *  0.0078125), centerY - (gaugeSize * 0.015625),
				centerX -  (gaugeSize *  0.0078125), centerY  - (gaugeSize * 0.015625)
		});
		foregroundNeedle.setFill(Color.web("#333333"));
	}


	/**
	 * Testschleife; Es werden Zufallszahlen ermittelt und dann die Bewegung ausgeführt.
	 */
	public void startAnimation() 
	{
		if(animThread != null && animThread.isAlive())
			animThread.stop();
		
		isAnimation = true;
		Runnable runnable = new Runnable(){

			@Override
			public void run() 
			{
				double minWatt = majorValue.getVon();
				double maxWatt = majorValue.getBis();
				//double currentWatt = majorValue.getCurrentValue();
				
				double newPercentValue = 50D;
				while(isAnimation)
				{
					Random ran = new Random();
					double randomDouble = ran.nextDouble();
					double randomValue = minWatt + (maxWatt - minWatt) * randomDouble;
					
					double rangeWatt = Tools.getRange(minWatt, maxWatt);
					
					//convert to percent 
					newPercentValue = 100D/rangeWatt * randomValue;
					//movement of the needle
					
					if(newPercentValue != percentValueNeedle)
					{
						double differenz = 0;
						double startWert = percentValueNeedle;
						boolean vorwaertsImmer = true;
						if(percentValueNeedle > newPercentValue)
						{
							differenz = (percentValueNeedle - newPercentValue);
							vorwaertsImmer = false;
						}
						else
						{
							differenz = (newPercentValue - percentValueNeedle);
						}
						differenz = Math.round(differenz * 10D) /10D;
						double anzahlSchritte = (double) (differenz * 10D);
						
						for(int i = 1; i <= anzahlSchritte; i++)
						{
							if(vorwaertsImmer)
								startWert = startWert + 0.1;
							else
								startWert = startWert - 0.1;
							double valueToSet = startWert;
							
							//convert percent value to watt value for the middleview
							
							double wattValue = (rangeWatt / 100D * valueToSet) - (rangeWatt - maxWatt);
							
							majorValue.setCurrentValue(wattValue);
							
							Platform.runLater(() -> setCurrentValue(valueToSet, false));
							try 
							{
								TimeUnit.MILLISECONDS.sleep(2);
							} 
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
						}
						
						double wattValue = (rangeWatt / 100D * newPercentValue) - (rangeWatt - maxWatt);
						majorValue.setCurrentValue(wattValue);
						final double percentToSet = newPercentValue;
						Platform.runLater(() -> setCurrentValue(percentToSet, false));
						
					}
					
					
					int zahl = (int)(Math.random() * 9 + 1);
					double addKwh = zahl / 10D;
					minorValue.setCurrentValue(minorValue.getCurrentValue()+addKwh);
					drawMinorTextValue(true);
					
					
					try 
					{
						TimeUnit.SECONDS.sleep(2);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					
					
				}
			}
			
		};
		
		animThread = new Thread(runnable);
		animThread.start();
		
		
	
		
	}

	public void stopAnimation() 
	{
		isAnimation = false;
		if(animThread != null)
			animThread.stop();
	}
	
	 public void setCurrentValue(double currentValue, boolean isInit)
	 {
		this.percentValueNeedle = currentValue;
		 
		double percentValueToCalc = (100D / (RANGE_MAX - RANGE_MIN) * currentValue) / 100D;
		
		double angleValue = 180 * percentValueToCalc - 90;
		
		
		//gleichen Wert nochmals setzen, damit ein neuzeichnen erzwungen wird.
		dropNeedleShadow.setOffsetY(4.0f);
		
		needleRotate.setAngle(angleValue);
		needleRotate.setPivotX(centerX);
		needleRotate.setPivotY(centerY);
		 
		
		//clear ist wichtig, ansonsten wird beim letzten bekannten Punkt die neue Drehung vorgenommen
		this.backgroundNeedle.getTransforms().clear();
		this.backgroundNeedlePick.getTransforms().clear();
		this.foregroundNeedle.getTransforms().clear();
		
		this.backgroundNeedle.getTransforms().add(needleRotate);
		this.backgroundNeedlePick.getTransforms().add(needleRotate);
		this.foregroundNeedle.getTransforms().add(needleRotate);
		
		//Aktualisierung des Textes für die genauere Darstellung
		//TODO
		this.drawTextValues(true);
	}


	public void setNewLowPercentValue(double doubleInPercent)
	{
		double newValue = 180d/100d*doubleInPercent;
		
		
		double invertAngle = 180d-newValue;
		invertAngle = invertAngle - this.startingAngleYellow;
		this.endingAngleYellow = (float)invertAngle;
		this.resize();
	}


	public void setNewHighPercentValue(double doubleInPercent) 
	{
		double newValue = 180d/100d*doubleInPercent;
		float invertAngle = (float) (180d - newValue);
		//neuer Wert ist kleiner als der bisherige
		if(invertAngle < startingAngleYellow)
		{
			
			float diff = startingAngleYellow - invertAngle;
			//correct the ending angle
			this.endingAngleYellow = this.endingAngleYellow + diff;
			

			this.startingAngleYellow = (float)invertAngle;
			this.resize();
		}
		else if(invertAngle > startingAngleYellow)
		{
			float diff = invertAngle - startingAngleYellow;
			
			this.endingAngleYellow = this.endingAngleYellow - diff;

			this.startingAngleYellow = (float)invertAngle;
			this.resize();
		}
		
	}


	public void setMajorValue(SensorValue majorValue) 
	{
		this.majorValue  = majorValue;
	}
	
	public void setMinorValue(SensorValue minorValue)
	{
		this.minorValue = minorValue;
		if(this.minorValue != null)
		{
			this.minorValue.getCurrentValueProperty().addListener(new ChangeListener<Number>(){

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
				{
					drawMinorTextValue(true);
				}
				
			});
		}
	}
	

	
	private void drawTextValues(boolean clearing) 
	{
		//Size wird für die Ausrichtung benötigt
		double gaugeSize  = getWidth() < getHeight() ? getWidth() : getHeight();
		double w = textCanvas.getWidth();
		double h = textCanvas.getHeight();
		
		GraphicsContext gc = textCanvas.getGraphicsContext2D();
	
		
		if(scaleableFontSize == null)
		{
			scaleableFontSize = new SimpleDoubleProperty(gaugeSize * 0.12);
		}
		else
			scaleableFontSize.set(gaugeSize * 0.12);
		
		if(fontVorgabe == null)
		{
			fontVorgabe = new Font("Verdana", 12);
		}
		
		
		
		Font font = Font.font(fontVorgabe.getName(), scaleableFontSize.get());
		
		//Dieses ist dann zu vollziehen, wenn nur der Wert sich geändert hat.
		if(clearing)
		{
		
			gc.clearRect(0, 0, w, h);
		}
		
		gc.setFill(Color.web("#FFFFFF"));
		
		
		//Fontsize muss ermittelt werden anhand des größten values
		if(majorValue != null)
		{
			//initial
			 //Ermittlung nach dem maximal möglichen Zustand
			 Bounds maxTextAbmasse = this.getMaxTextWidth(font, this.majorValue);
			 if(maxTextAbmasse.getWidth() < w  && maxTextAbmasse.getHeight() < h)
			 {
				 double tempSize = getGreaterFont(gaugeSize * 0.12, w, h, majorValue);
				 if(tempSize != getFontSize().get())
						getFontSize().set(tempSize);
				 
			 }
			 else
			 {
				 double tempSize = getLesserFont(getFontSize().get(), w, h, majorValue);
				 if(tempSize != getFontSize().get())
						getFontSize().set(tempSize);
			 }
			 font = Font.font(fontVorgabe.getName(), getFontSize().get());
		}
		gc.setFont(font);
		
		if(majorValue == null)
			textFirstMeasuringUnit.setText("");
		else
			textFirstMeasuringUnit.setText(" "+majorValue.getMeasurementUnit());
		textFirstMeasuringUnit.setFont(font);
		
		
		double masseinheitX = w - (textFirstMeasuringUnit.getLayoutBounds().getWidth());// + (gaugeSize * 0.015635));
	
		double haelfte =  textFirstMeasuringUnit.getLayoutBounds().getHeight() / 2d;
		double masseinheitY = h/2d +  (haelfte/2d);
		
		
		gc.setFill(Color.web("#282828"));
		gc.fillText(textFirstMeasuringUnit.getText(), masseinheitX+2, masseinheitY+2);
		
		gc.setFill(Color.WHITE);
		gc.fillText(textFirstMeasuringUnit.getText(), masseinheitX, masseinheitY);
		
		
			
		if(majorValue == null)
			textFirstValue.setText("");
		else
			textFirstValue.setText(String.format("%.1f", majorValue.getCurrentValue()));
		
		textFirstValue.setFont(font);
		
		double valueX = masseinheitX - (textFirstValue.getLayoutBounds().getWidth());//  + (gaugeSize * 0.015635));
		double valueY = masseinheitY;
		
		gc.setFont(font);
		
		gc.setFill(Color.web("#282828"));
		gc.fillText(textFirstValue.getText(), valueX+2, valueY+2);
	
		gc.setFill(Color.WHITE);
		gc.fillText(textFirstValue.getText(), valueX, valueY);
	}
	
	private void drawMinorTextValue(boolean clearing) 
	{
		double gaugeSize  = getWidth() < getHeight() ? getWidth() : getHeight();
		double w = canvasCounterValue.getWidth();
		double h = canvasCounterValue.getHeight();
		
		GraphicsContext gc = canvasCounterValue.getGraphicsContext2D();
		
		if(scaleableFontSize == null)
		{
			scaleableFontSize = new SimpleDoubleProperty(gaugeSize * 0.12);
		}
		else
			scaleableFontSize.set(gaugeSize * 0.12);
		
		if(fontVorgabe == null)
		{
			fontVorgabe = new Font("Verdana", 12);
		}
		
		Font font = Font.font(fontVorgabe.getName(), scaleableFontSize.get());
		
		//Dieses ist dann zu vollziehen, wenn nur der Wert sich geändert hat.
		if(clearing)
		{
		
			gc.clearRect(0, 0, w, h);
		}

		gc.setFill(Color.web("#00000080"));
	
		
		//Fontsize muss ermittelt werden anhand des größten values
		if(minorValue != null)
		{
			//initial
			 //Ermittlung nach dem maximal möglichen Zustand
			 Bounds maxTextAbmasse = this.getMaxTextWidth(font, this.minorValue);
			 if(maxTextAbmasse.getWidth() < w  && maxTextAbmasse.getHeight() < h)
			 {
				 double tempSize = getGreaterFont(gaugeSize * 0.12, w, h, minorValue);
				 if(tempSize != getFontSize().get())
						getFontSize().set(tempSize);
				 
			 }
			 else
			 {
				 double tempSize = getLesserFont(getFontSize().get(), w, h, minorValue);
				 if(tempSize != getFontSize().get())
						getFontSize().set(tempSize);
			 }
			 font = Font.font(fontVorgabe.getName(), getFontSize().get());
		}
		gc.setFont(font);
		

		if(minorValue == null)
			textMeasurement.setText("");
		else
			textMeasurement.setText(" "+minorValue.getMeasurementUnit());
		textMeasurement.setFont(font);
	
		
		double masseinheitX = w - (textMeasurement.getLayoutBounds().getWidth()) - (gaugeSize * 0.015635);
		
		double haelfte =  textMeasurement.getLayoutBounds().getHeight() / 2d;
		double masseinheitY = h/2d +  (haelfte/2d);
		
		
		gc.fillText(textMeasurement.getText(), masseinheitX, masseinheitY);
		
		
			
		if(minorValue == null)
			textCounterValue.setText("");
		else
			textCounterValue.setText(String.format("%.1f", minorValue.getCurrentValue()));
		
		textCounterValue.setFont(font);
		
		
		double valueX = masseinheitX - (textCounterValue.getLayoutBounds().getWidth());// + (gaugeSize * 0.015635);
		double valueY = masseinheitY;
		
		gc.setFont(font);
		gc.fillText(textCounterValue.getText(), valueX, valueY);
		
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
		
		return new BoundingBox(0,0, width, height);
	}
	

	public double getGAPPercent()
	{
		return GAP_PERCENT;
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
	

	public DoubleProperty getFontSize()
	{
		return scaleableFontSize;
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
	

	private Bounds textWidth(double size, SensorValue sensorValue)
	{
		//hier muss die bounds aufgebaut werden anhand der zwei darzustellenden Werte 
		
		double valueRounded = Math.round(sensorValue.getCurrentValue()*10)/10;
		String eineNachkomma = String.format("%.1f", valueRounded);
		String showValue = eineNachkomma + " " + sensorValue.getMeasurementUnit();
		
		if(fontVorgabe == null)
			fontVorgabe = new Font("Verdana", 12);
		Text text = new Text(showValue);
		Font font =  Font.font(fontVorgabe.getFamily(), size);
        text.setFont(font);
        return text.getBoundsInLocal();
	}

}
