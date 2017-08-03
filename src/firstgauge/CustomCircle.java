package firstgauge;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;


public class CustomCircle extends Region
{
	
	public enum StopIndizes
	{
		/**
		 * Farbwerte für den Hintergrund
		 */
		BACKGROUND_CIRCLE,
		
		/**
		 * In der mittleren Ebene wird nochmals ein linearer Verlauf abgelegt
		 */
		OVERLAY_BRIGHTER_CIRCLE, 
		
		/**
		 * Das oberste Overlay ist eine radialer verlauf
		 */
		TOP_OVERLAY,
		
		/**
		 * Auesserer Rand mit einem overlay LinearGradient
		 */
		OVERLAY_OUTER_BORDER,
		
		BG_KNOB_OVERLAY,
		
		OVERLAY_HALF_BACKGROUND, 
		/**
		 * Hintergrund für LCD was aber eher bei Sicht ein Rahmen ist.
		 */
		BACKGROUND_LCD, 
		/**
		 * Overlay über das LCD
		 */
		OVERLAY_LCD;
		
		
		
		
	}
	/**
	 * Range ist von bis der auch angepasst werden kann.
	 */
	private double RANGE_MIN = 0D;
	
	private double RANGE_MAX = 100D;
	
	private double currentValue = 50D;
	
	private double nodeX, nodeY, nodeW, nodeH;
	
	private double width = 128, height = 128;
	
	private double centerX = 64, centerY = 64;
	
	
	private Circle backgroundCircle, backgroundOverlay, overlayOuterBorder, overlayBrighterOuterBorder, innerBorder, topOverlay,
		bgKnob, bgKnobOverlay, innerBgKnob, centerKnob, innerBorderKnob;
	
	private LinearGradient linearGradientBackground, linearGradientBrighterOuterBorder, ovlerlayOuterBorderGradient;
	
	private RadialGradient topOverlayRadialGradient;
	
	/**
	 * Farbmap weil die Werte unveränderlich sind und bei resize wieder bei den Gradienten neu gesetzt werden müssen.
	 */
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	/**
	 * Großen Striche auf der Anzeige, werden nach Erzeugung in Map abgelegt um später bei Skalierung diese anzupassen.
	 */
	private HashMap<Integer, Line> majorMap = new HashMap<Integer, Line>();
	
	/**
	 * Kleine Striche und zwekcs späterer Skalierung Ablage in Map
	 */
	private HashMap<Integer, Line> minorMap = new HashMap<Integer, Line>();
	
	private Polygon backgroundNeedle, foregroundNeedle;
	
	private Circle backgroundNeedlePick;
	
	/**
	 * Rotation der Nadel zu Beginn ist der Anfangswert der Rotation 0 und die Nadel steht auf 12 Uhr.
	 * <br>Eine Bewegung findet von -90° zu +90° statt => 180°
	 * <br>Ursprünglich waren es drei Rotation Objekte, jedoch brauche ich doch nur eines für alle Nadelkomponenten
	 */
	private Rotate needleRotate;
	
	private boolean isAnimation = false;
	
	private Thread animThread = null;
	
	private Arc backgroundHalf, innerHalf, overlayHalf;
	
	private Rectangle rectangleBackgroundLCD, rectangleLCD, rectLCDOverlay;
	
	private Text textCurrentValue;
	
	/**
	 * Hier wird nur die Masseinheit abgelegt.
	 */
	private Text textMasseinheit;
	
	private Canvas textCanvas;
	
	private DropShadow dropNeedleShadow;
	
	
	public CustomCircle()
	{
		this.initGraphics();
		
		this.registerListener();
	
		
	}

	private void registerListener() 
	{
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
		
	}

	public void resize() 
	{
		double size  = getWidth() < getHeight() ? getWidth() : getHeight();
		
		double breitenDifferenz = 0, hoehenDifferenz = 0;
		
		if(getHeight() > 0)
			centerY = getHeight() / 2d;
		
		if(getWidth() > 0)
			centerX = getWidth() / 2d;
		
		
		
		
		double radius = size / 2d;
		
		
		linearGradientBackground = new LinearGradient(centerX, (getHeight() * -0.1171875 ) , centerX, (getHeight()  * 0.9375), false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.BACKGROUND_CIRCLE));
		
		backgroundCircle.setRadius(1 * radius); 
		backgroundCircle.setCenterX(centerX);
		backgroundCircle.setCenterY(centerY);
		
		backgroundOverlay.setRadius(1 * radius);
		backgroundOverlay.setCenterX(centerX);
		backgroundOverlay.setCenterY(centerY);
		backgroundOverlay.setFill(linearGradientBackground);
		
		
		
		overlayOuterBorder.setRadius(0.9375 * radius);
		overlayOuterBorder.setCenterX(centerX);
		overlayOuterBorder.setCenterY(centerY);
		ovlerlayOuterBorderGradient = new LinearGradient(centerX, height - (height * 0.8046875), centerX, (height * 0.015625 * -1), false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.OVERLAY_OUTER_BORDER));
		overlayOuterBorder.setFill(ovlerlayOuterBorderGradient);
		
		
		linearGradientBrighterOuterBorder = new LinearGradient(centerX, (getHeight() - (getHeight() *  0.9375)), centerX, (getHeight() *  0.9375), false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.OVERLAY_BRIGHTER_CIRCLE));
		overlayBrighterOuterBorder.setRadius(0.9375 * radius);
		overlayBrighterOuterBorder.setCenterX(centerX);
		overlayBrighterOuterBorder.setCenterY(centerY);
		overlayBrighterOuterBorder.setFill(linearGradientBrighterOuterBorder);
	
		//0.90625
		innerBorder.setRadius(0.90625 * radius);
		innerBorder.setCenterX(centerX);
		innerBorder.setCenterY(centerY);
		
		
	
		//im Orginal 114x114; 100 / 128 * 114 = 89,0625% => 0.890625
		topOverlay.setRadius(0.890625 * radius);
		topOverlay.setCenterX(centerX);
		topOverlay.setCenterY(centerY);
				
		//der Radius kann sich verändern 120 orginal; 100 / 128 * 120 = 93,75% = 0.9375
		topOverlayRadialGradient = new RadialGradient(0D, 0D, centerX, centerY, 0.9375 * radius, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.TOP_OVERLAY));
		topOverlay.setFill(topOverlayRadialGradient);
		
		
		
		drawMinorTick(size, false);
		drawMajorTick(size, false);
		drawNeedle(size, false);
		
		drawBottomGauge(size, false);
		
		
		drawNeedleKnob(size, false);
				
		this.setCurrentValue(this.currentValue, false);
		
		
		
	}

	private void initGraphics() 
	{
		backgroundCircle = new Circle();
		backgroundCircle.setRadius(64);
		backgroundCircle.setCenterX(centerX);
		backgroundCircle.setCenterY(centerY);
		backgroundCircle.setFill(Color.web("#292828"));
	
		
		Stop[] stopArray = new Stop[]{
				new Stop(0, Color.web("#1A1A1AE6")),
				new Stop(0.297, Color.web("#000000")),
				new Stop(0.345, Color.web("#0F0F0FE6")),
				new Stop(0.4718, Color.web("#313131A6")),
				new Stop(0.5767, Color.web("#45454570")),
				new Stop(0.646, Color.web("#4D4D4D4D")),
				new Stop(0.6524, Color.web("#4B4B4B4F")),
				new Stop(0.7756, Color.web("#30303080")),
				new Stop(0.8848, Color.web("#202020AC")),
				new Stop(0.9678, Color.web("#1A1A1ACD")),
		};
		
		
		
		stopMap.put(StopIndizes.BACKGROUND_CIRCLE, stopArray);
		//x = haelfte der breite, y = 100 / 128 * 15 = 11,71875% * -1 => -0.1171875 
		//endX = haelfte der Breite, endY = 100 / 128 * 120 = 93,75% => 0.9375
		//linearGradientBackground = new LinearGradient(64, -15 , 64, 120, false, CycleMethod.NO_CYCLE, stopArray);
		
		linearGradientBackground = new LinearGradient(centerX, (height * -0.1171875 ) , centerX, (height  * 0.9375), false, CycleMethod.NO_CYCLE, stopArray);
		
		backgroundOverlay = new Circle();
		backgroundOverlay.setRadius(64);
		backgroundOverlay.setCenterX(centerX);
		backgroundOverlay.setCenterY(centerY);
		backgroundOverlay.setFill(linearGradientBackground);

		
		//ist von der breite und höhe um 8 kleiner bei init 100 / 128 * 120 =  93,75% => 0.9375
		overlayOuterBorder = new Circle();
		overlayOuterBorder.setRadius(60);
		overlayOuterBorder.setCenterX(centerX);
		overlayOuterBorder.setCenterY(centerY);
		stopArray = new Stop[]{ new Stop(0, Color.web("#4D4D4D")),
				new Stop(1, Color.web("#666666"))
				
		};
		//orginal init y1 bei ca. 103 => 100/128 * 103 = 80,46875 => 0,8046875
		//orignal inti y2 bei ca -2 => 100/128 * (-2) = 1,5625 => 0.015625
		ovlerlayOuterBorderGradient = new LinearGradient(centerX, height - (height * 0.8046875), centerX, (height * 0.015625 * -1), false, CycleMethod.NO_CYCLE, stopArray);
		stopMap.put(StopIndizes.OVERLAY_OUTER_BORDER, stopArray);
		overlayOuterBorder.setFill(ovlerlayOuterBorderGradient);
	
		
		stopArray = new Stop[]{ new Stop(0, Color.web("#999999")),
				new Stop(0.297, Color.web("#CCCCCC")),
				new Stop(0.5371, Color.web("#CCCCCC")),
				new Stop(0.5669, Color.web("#C9C9C9F7")),
				new Stop(0.7944, Color.web("#B9B9B9B4")),
				new Stop(0.9678, Color.web("#B3B3B380"))
				};
		stopMap.put(StopIndizes.OVERLAY_BRIGHTER_CIRCLE, stopArray);
		
		//lg = new LinearGradient(63, 8 , 63, 110, false, CycleMethod.NO_CYCLE, stopArray);
		linearGradientBrighterOuterBorder = new LinearGradient(centerX, (height - (height *  0.9375)), centerX, (height *  0.9375), false, CycleMethod.NO_CYCLE, stopArray);
		
		overlayBrighterOuterBorder = new Circle();
		overlayBrighterOuterBorder.setRadius(60);
		overlayBrighterOuterBorder.setCenterX(centerX);
		overlayBrighterOuterBorder.setCenterY(centerY);
		overlayBrighterOuterBorder.setFill(linearGradientBrighterOuterBorder);
		
		//ein Rand der als kreis darüber gelegt wird
		innerBorder = new Circle();
		//116x116 ist das Orginal 100 / 128 * 116 = 90.625% => 0.90625
		innerBorder.setRadius(58);
		innerBorder.setCenterX(centerX);
		innerBorder.setCenterY(centerY);
		innerBorder.setFill(Color.web("#232323"));
		
		stopArray = new Stop[]{ new Stop(0, Color.web("#F2F2F2")),
			new Stop(0.4381, Color.web("#F0F0F0")),
			new Stop(0.5959, Color.web("#E9E9E9")),
			new Stop(0.7083, Color.web("#DEDEDE")),	
			new Stop(0.7993, Color.web("#CDCDCD")),	
			new Stop(0.8771, Color.web("#B7B7B7")),	
			new Stop(0.9448, Color.web("#9C9C9C")),	
			new Stop(1, Color.web("#808080"))
		};
		
		
		stopMap.put(StopIndizes.TOP_OVERLAY, stopArray);
		
		topOverlay = new Circle();
		//im Orginal 114x114; 100 / 128 * 114 = 89,0625% => 0.890625
		topOverlay.setRadius(57);
		topOverlay.setCenterX(centerX);
		topOverlay.setCenterY(centerY);
				
		//der Radius kann sich verändern 120 orginal; 100 / 128 * 120 = 93,75% = 0.9375
		topOverlayRadialGradient = new RadialGradient(0D, 0D, centerX, centerY, 120D, false, CycleMethod.NO_CYCLE, stopArray);
		topOverlay.setFill(topOverlayRadialGradient);
		
		this.getChildren().addAll(backgroundCircle, backgroundOverlay, overlayOuterBorder, overlayBrighterOuterBorder, innerBorder, topOverlay);

		drawMinorTick(width, true);
		//major und minor ticks sind ausgelagert
		drawMajorTick(width, true);
		
		//Für den unteren Bereich wir ein Halbkreis mit LCD Display sichtbar sein.
		drawBottomGauge(width, true);
		
		
	
		drawNeedle(width, true);
		 
		drawNeedleKnob(width, true);
		//Vorbereitung der Rotation 
		needleRotate = new Rotate(0);
		
		//needlePickRotate = new Rotate(0);
		//foregroundNeedleRotate = new Rotate(0);
		
		this.setCurrentValue(this.currentValue, true);
		
	}
	
	private void drawBottomGauge(double gaugeSize, boolean isInit)
	{
		if(isInit)
		{
			backgroundHalf = new Arc();
			innerHalf = new Arc();
			overlayHalf = new Arc();
			rectangleBackgroundLCD = new Rectangle();
			rectangleLCD = new Rectangle();
			textCurrentValue = new Text();
			textMasseinheit = new Text();
			rectLCDOverlay = new Rectangle();
			textCanvas = new Canvas();
		}
		
		backgroundHalf.setCenterX(centerX);
		//3 px Abstand zu der gruenen Flaeche 100/128 * 3 = 2.34375 = 0.0234375
		//2 px 1,5625 = 0.015635
		backgroundHalf.setCenterY(centerY + (gaugeSize * 0.015635));
		
		//r = 60 = 100/64 * 60 = 93,75 = 0.9375
		// r = 56 = 100/ 64 * 56 = 87,5 = 0.875
		// r = 58 = 100/64 * 58 = 90,625 = 0.90625
		double radius = gaugeSize / 2;
		backgroundHalf.setRadiusX(radius *  0.90625);
		//hier wird noch die Zugabe von Y wieder abgezogen
		backgroundHalf.setRadiusY(radius * 0.90625 - (gaugeSize * 0.015635));
		backgroundHalf.setStartAngle(180f);
		backgroundHalf.setLength(180.0f);
		backgroundHalf.setType(ArcType.ROUND);
		//backgroundHalf.setFill(Color.RED);
		backgroundHalf.setFill(Color.web("#212121"));
		
		//innerhalf ist ein wenig kleiner als backgroundHalf
		//schaut so nach ca. 2 px Abstand aus
		//2 px 1,5625 = 0.015635
		//Hinweis: backgroundHalf centerY ist der punkt von dem weitergerechnet wird.
		
		//radius könnte ein 56 sein
		// r = 56 = 100/ 64 * 56 = 87,5 = 0.875
		innerHalf.setRadiusX(radius *  0.875);
		//radius muss sich verringern, weil bei y noch aufgeschlagen wird.
		innerHalf.setRadiusY(backgroundHalf.getRadiusY() -  (gaugeSize * 0.0234375));
		//ist gleich
		innerHalf.setCenterX(backgroundHalf.getCenterX());
		//von ermittelten backgroundHalf noch zwei drauf geben
		innerHalf.setCenterY(backgroundHalf.getCenterY() + (gaugeSize * 0.015635));
		
		innerHalf.setStartAngle(180f);
		innerHalf.setLength(180.0f);
		innerHalf.setType(ArcType.ROUND);
		innerHalf.setFill(Color.web("#a5aaaa"));
		
		overlayHalf.setRadiusX(innerHalf.getRadiusX());
		overlayHalf.setRadiusY(innerHalf.getRadiusY());
		overlayHalf.setCenterX(innerHalf.getCenterX());
		overlayHalf.setCenterY(innerHalf.getCenterY());
		overlayHalf.setStartAngle(innerHalf.getStartAngle());
		overlayHalf.setLength(innerHalf.getLength());
		overlayHalf.setType(innerHalf.getType());
		
		Stop[] stopArray = stopMap.get(StopIndizes.OVERLAY_HALF_BACKGROUND);
		if(stopArray == null)
		{
			 stopArray = new Stop[]{
						//Deckkraft 60% 
						new Stop(0, Color.web("#F2F2F29A")),
						new Stop(0.3333, Color.web("#F2F2F2")),
						new Stop(0.8786, Color.web("#EAEAEAB5")),
						//Deckkraft 60% pos 99,44%
						new Stop(0.9944, Color.web("#E6E6E69A"))
				};
			 stopMap.put(StopIndizes.OVERLAY_HALF_BACKGROUND, stopArray);
		}
		
		//x1 = 7 y1 = 93,5 x2 = 121 y2 = y1
		//die Punkte müssen unter berücksichtigung von centerx und centery ermittelt werden
		
		// x1 = 64 - 7 = 57 Strecke 100/128 * 57 = 44,53125 = 0.4453125
		
		//y2 = 93.5 - 64 = 29,5 Strecke 100/128 * 29,5 = 23,046875 = 0.23046875
		
		//x2 = 121 - 64 = 57 Strecke 100/128 * 57 = 44,53125 = 0.4453125
		
		LinearGradient overlayLinGradient = new LinearGradient(centerX - (gaugeSize * 0.4453125), centerY + (gaugeSize * 0.23046875),
				centerX + (gaugeSize * 0.4453125), centerY + (gaugeSize * 0.23046875), false, CycleMethod.NO_CYCLE,stopArray);
		overlayHalf.setFill(overlayLinGradient);
		
		//hintergrund für das LCDDisplay
		//es ist ein rechteck
		
		//x1 = 26 y1 = 74 w = 76 h = 24
		//x1: 64 - 26 = 38 abzug von Centerx 100/128 * 38 = 28,90625 = 0.2890625
		//y1: 74 - 64 = 10 addition zu centery 100/128 * 10 = 7,8125 = 0.078125
		//w: 76 = 100/128 * 76 = 59,375 = 0.59375
		//h: 24 = 100/128 * 24 = 18,75 = 0.1875
		
		rectangleBackgroundLCD.setX(centerX - (gaugeSize * 0.2890625));
		rectangleBackgroundLCD.setY(centerY + (gaugeSize * 0.078125));
		rectangleBackgroundLCD.setWidth(gaugeSize * 0.59375);
		rectangleBackgroundLCD.setHeight(gaugeSize * 0.1875);
	
		Stop[] stopValueBackground = stopMap.get(StopIndizes.BACKGROUND_LCD);
		if(stopValueBackground == null)
		{
			stopValueBackground = new Stop[]{
					new Stop(0, Color.web("#1A1A1A90")),
					new Stop(0.067, Color.web("#0F0F0FB1")),
					new Stop(0.1792, Color.web("#040404D7")),
					new Stop(0.297, Color.web("#000000")),
					new Stop(0.3399, Color.web("#080808EA")),
					new Stop(0.4071, Color.web("#1E1E1EC7")),
					new Stop(0.49, Color.web("#4343439D")),
					new Stop(0.5847, Color.web("#7575756C")),
					new Stop(0.646, Color.web("#9999994D")),
					new Stop(0.676, Color.web("#8A8A8A59")),
					new Stop(0.7627, Color.web("#6565657B")),
					new Stop(0.843, Color.web("#4949499B")),
					new Stop(0.9139, Color.web("#393939B7")),
					new Stop(0.9678, Color.web("#333333CD"))
			};
			stopMap.put(StopIndizes.BACKGROUND_LCD, stopValueBackground);
		}
		
		//der lineargradient dazu
		//x1="64" y1="98" x2="64" y2="74"
		//x1 = centerx
		//y1 = 98 - 64 = 34 = addition zu centery = 100/128 * 34 = 26,5625 = 0.265625
		//x2 = centerx
		//y2 = 74 - 64 = 10 = addition zu centery = 100/128 * 10 = 7,8125 = 0.078125
		LinearGradient lcdLG = new LinearGradient(centerX, centerY + (gaugeSize * 0.265625), 
				centerX, centerY + (gaugeSize *  0.078125), false, CycleMethod.NO_CYCLE, stopValueBackground);
		rectangleBackgroundLCD.setFill(lcdLG);
		
		//x="27" y="75" style="fill:#7CBA0F;" width="74" height="22"/
		//w = 74 = 100 / 128 * 74 = 57,8125 = 0.578125
		//h = 22 = 100 / 128 * 22 = 17,1875 = 0.171875
		//Bei x und y wird der Hintergrund als Basis verwendet.
		//1px 100/128 = 0,78125 = 0.0078125
		rectangleLCD.setX(rectangleBackgroundLCD.getX() + (gaugeSize * 0.0078125));
		rectangleLCD.setY(rectangleBackgroundLCD.getY() + (gaugeSize * 0.0078125));
		rectangleLCD.setWidth(gaugeSize * 0.578125); 
		rectangleLCD.setHeight(gaugeSize * 0.171875);
		rectangleLCD.setFill(Color.web("#6F7C3E"));
		
		Glow glow = new Glow();
		glow.setLevel(0.1);
		InnerShadow inner = new InnerShadow();
		inner.setOffsetX(1.0f);
		inner.setOffsetY(-1.0f);
		inner.setColor(Color.BLACK);
		inner.setInput(glow);
		rectangleLCD.setEffect(inner);
		
		//eine Canvas mit Zahl
		
		//Startwert
		//x28 y 75 w 74 h 22
		
		//hoehe der Schrift lag zu Beginn bei ca. 16px
		//100 / 128 * 16 = 12,5 = 0.125
		
		//als erstes die Flaeche loeschen
		textCanvas.getGraphicsContext2D().clearRect(textCanvas.getLayoutX(), textCanvas.getLayoutY(), textCanvas.getWidth(), textCanvas.getHeight());
		//textCanvas.setOpacity(0.5);
		//resize der Flaeche
		double w = gaugeSize * 0.578125;
		double h = gaugeSize * 0.171875;
		double x = rectangleBackgroundLCD.getX() + (gaugeSize * 0.0078125);
		double y = rectangleBackgroundLCD.getY() + (gaugeSize * 0.0078125);
		textCanvas.setWidth(w);
		textCanvas.setHeight(h);
	
		textCanvas.relocate(x, y);
		
		//ab hier beginnt die Zeichnung des eigentlichen Inhalts
		drawTextValues(false);
		
		//x="27" y="75" width="74" height="22"/>
		//Die Größe muss wie das rectangleLCD sein, weil es nur drüber gelegt wird.
		rectLCDOverlay.setX(rectangleLCD.getX());
		rectLCDOverlay.setY(rectangleLCD.getY());
		rectLCDOverlay.setWidth(rectangleLCD.getWidth());
		rectLCDOverlay.setHeight(rectangleLCD.getHeight());
		
		Stop[] stopOverlayLCD = stopMap.get(StopIndizes.OVERLAY_LCD);
		if(stopOverlayLCD == null)
		{
			stopOverlayLCD = new Stop[]{
					new Stop(0.0056, Color.web("#FFFFFF80")),
					new Stop(1, Color.web("#FFFFFF00"))
			};
			
			stopMap.put(StopIndizes.OVERLAY_LCD, stopOverlayLCD);
		}
		//x1 = 64, y1 68.3333 x2 = x1 y2 = 111.3337
		
		//x1 und x2 jeweils centerx
		//y1 68.3333 - 64 = 4.3333 addition zu centery 100/128*4.3333 = 3,385390625 = 0.03385390625
		//y2 111.3337 - 64 = 47,3337 addition zu centery  100/128 * 47,3337  = 36,979453125 = 0.36979453125
		
		LinearGradient overlayLCDGradient = new LinearGradient(centerX, centerY + (gaugeSize * 0.03385390625),
				centerX, centerY + (gaugeSize *  0.36979453125),  false, CycleMethod.NO_CYCLE, stopOverlayLCD);
		
		rectLCDOverlay.setFill(overlayLCDGradient);
		
		
		if(isInit)
		{
			this.getChildren().addAll(backgroundHalf, innerHalf, overlayHalf, rectangleBackgroundLCD, rectangleLCD, textCanvas, rectLCDOverlay);
		}
	
		
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
		//Dieses ist dann zu vollziehen, wenn nur der Wert sich geändert hat.
		if(clearing)
		{
		
			gc.clearRect(0, 0, w, h);
		}
		gc.setFill(Color.BLACK);
		
		Font masseinheitFont = new Font("Verdana", gaugeSize * 0.12);
		
		textMasseinheit.setText("%");
		textMasseinheit.setFont(masseinheitFont);
		
		gc.setFont(masseinheitFont);
		
		double masseinheitX = w - (textMasseinheit.getLayoutBounds().getWidth() + (gaugeSize * 0.015635));
		
		//keien Ahnung wieso ich nicht den Mittelpunkt von H als Basis nehmen kann.
		double masseinheitY = textMasseinheit.getLayoutBounds().getHeight() -  (gaugeSize * 0.015635);
		gc.fillText(textMasseinheit.getText(), masseinheitX, masseinheitY);
		
		Font valueFont = new Font("Verdana", gaugeSize * 0.115);
		textCurrentValue.setText(String.format("%.2f", currentValue));
		textCurrentValue.setFont(valueFont);
		
		double valueX = masseinheitX - (textCurrentValue.getLayoutBounds().getWidth()  + (gaugeSize * 0.015635));
		double valueY = masseinheitY;
		
		gc.setFont(valueFont);
		gc.fillText(textCurrentValue.getText(), valueX, valueY);
	}

	private void drawNeedle(double gaugeSize, boolean isInit)
	{
		if(isInit)
		{
			//init der canvas
			backgroundNeedle = new Polygon();
			backgroundNeedlePick = new Circle();
			foregroundNeedle = new Polygon();
			
		}
		
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
				//cx - 1, cy - 42
				centerX - (gaugeSize *  0.0078125) , centerY - (gaugeSize * 0.40625),
				//cx + 1, cy - 42
				centerX + (gaugeSize *  0.0078125), centerY - (gaugeSize * 0.40625),
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
		backgroundNeedlePick.setCenterY(centerY - (gaugeSize * 0.40625)); 
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
		
		if(isInit)
		{
			//setzen der canvas
			this.getChildren().addAll(backgroundNeedle, backgroundNeedlePick, foregroundNeedle);
		}
		
		
	}

	/** 
	 * 
	 * @param gaugeSizeNow
	 * @param isInit
	 */
	private void drawNeedleKnob(double gaugeSizeNow, boolean isInit) 
	{
		
		
		if(isInit)
		{
			bgKnob = new Circle();
			bgKnobOverlay = new Circle();
			innerBgKnob = new Circle();
			centerKnob = new Circle();
			innerBorderKnob = new Circle();
		}
		
		
		
		//init size war 11 => 100 / 128 * 11 = 8,59375 => 0,0859375
		double sizeBgKnob =  gaugeSizeNow * 0.0859375;
		bgKnob.setRadius(sizeBgKnob / 2d);
		bgKnob.setCenterX(centerX);
		bgKnob.setCenterY(centerY);
		
		DropShadow inner = new DropShadow();
		inner.setColor(Color.BLACK);
		bgKnob.setEffect(inner);
		
		//init size war 10 => 100/128*10 = 7,8125 = 0.078125
		double sizeKnobOverlay = gaugeSizeNow * 0.078125;
		
		bgKnobOverlay.setRadius(sizeKnobOverlay / 2);
		bgKnobOverlay.setCenterX(centerX);
		bgKnobOverlay.setCenterY(centerY);
		
		//TODO der Verlauf muss nochmal geändert werde, irgendwie kommt dieser nicht zum tragen
		//entweder ist der Renderer von FX nicht so gut, die Grafikkarte von der Mühle nicht i.O. oder 
		//es liegt am Export zu SVG
		Stop[] stopArray = new Stop[]{
				new Stop(0, Color.web("#1A1A1A00")),
				new Stop(0.0921, Color.web("#22222217")),
				new Stop(0.311, Color.web("#2F2F2F4D")),
				new Stop(0.5149, Color.web("#33333380")),
				new Stop(0.7291, Color.web("#30303047")),
				new Stop(0.8848, Color.web("#2727271E")),
				new Stop(1, Color.web("#1A1A1A00")),
		};
		
		//der Radius kann sich verändern 120 orginal; 100 / 128 * 120 = 93,75% = 0.9375
		RadialGradient bgKnobOverlayRadial = new RadialGradient(0D, 0D, centerX, centerY, gaugeSizeNow * 0.9375, false, CycleMethod.NO_CYCLE, stopArray);
		bgKnobOverlay.setFill(bgKnobOverlayRadial);
		
		//initSize = 8 => 100/128*8 => 6,25 => 0,0625
		double sizeInnerBgKnob =  gaugeSizeNow * 0.0625;
		innerBgKnob.setRadius(sizeInnerBgKnob / 2d);
		innerBgKnob.setCenterX(centerX);
		innerBgKnob.setCenterY(centerY);
		
		stopArray = new Stop[]{
				new Stop(0.4455, Color.web("#000000")),
				new Stop(0.6667, Color.web("#0404049A")),
				new Stop(0.8728, Color.web("#0F0F0F3B")),
				new Stop(1, Color.web("#1A1A1A00")),
		};
		
		//radius beim gradienten von 3 => 100/128 * 3 => 2,34375 => 0.0234375
		RadialGradient innerBgRadialGradient = new RadialGradient(0D, 0D, centerX, centerY, gaugeSizeNow * 0.0234375, false, CycleMethod.NO_CYCLE, stopArray);
		innerBgKnob.setFill(innerBgRadialGradient);
		
		//orginal 3 => 100/128 *3 = 2,34375 = 0.0234375
		double sizeCenterKnob = gaugeSizeNow * 0.0234375;
	
		centerKnob.setRadius(sizeCenterKnob / 2D);
		centerKnob.setCenterX(centerX);
		centerKnob.setCenterY(centerY);
		
		stopArray = new Stop[]{
				new Stop(0, Color.web("#333333")),
				new Stop(0.0399, Color.web("#2E2E2EF6")),
				new Stop(0.192, Color.web("#232323CF")),
				new Stop(0.4058, Color.web("#1C1C1C98")),
				new Stop(1, Color.web("#1A1A1A00")),
		};
		
		//Radial auch mit 3 als Initi wert
		RadialGradient centerKnobRadialGradient = new RadialGradient(0D, 0D, centerX, centerY, gaugeSizeNow * 0.0234375, false, CycleMethod.NO_CYCLE, stopArray);
		centerKnob.setFill(centerKnobRadialGradient);
		
		//orginal 3 => 100/128 *3 = 2,34375 = 0.0234375
		double innerBorderKnobSize = gaugeSizeNow * 0.0234375;
		innerBorderKnob.setRadius(innerBorderKnobSize / 2D);
		innerBorderKnob.setCenterX(centerX);
		innerBorderKnob.setCenterY(centerY);
		
		stopArray = new Stop[]{
				new Stop(0, Color.web("#1A1A1A00")),
				new Stop(0.5895, Color.web("#18181897")),
				new Stop(0.8016, Color.web("#111111CD")),
				new Stop(0.9525, Color.web("#060606F4")),
				new Stop(1, Color.web("#000000")),
		};
		
		
		//auch hier die 3 beim init
		RadialGradient innerBorderKnobRadialGradient = new RadialGradient(0D, 0D, centerX, centerY, gaugeSizeNow * 0.0234375, false, CycleMethod.NO_CYCLE, stopArray);
		innerBorderKnob.setFill(innerBorderKnobRadialGradient);
		
		
		
		if(isInit)
		{
			this.getChildren().addAll(bgKnob);
			this.getChildren().addAll(bgKnobOverlay);
			this.getChildren().addAll(innerBgKnob);
			this.getChildren().addAll(centerKnob);
			this.getChildren().addAll(innerBorderKnob);
		}
		
	}

	public void setNewInitialEventCoordinates(MouseEvent t) 
	{
		 nodeX = nodeX();
		 nodeY = nodeY();
		 nodeH = nodeH();
		 nodeW = nodeW();
	}
	
	private double nodeX() 
	{
		return this.getBoundsInParent().getMinX();
	}
	
	private double nodeY() {
		return this.getBoundsInParent().getMinY();
	}

	private double nodeW() 
	{
		//return this.getWidth();
		return 0;
	}

	private double nodeH() 
	{
		//return this.getHeight();
		return 0;
	}

	public void moveComponent(double newTranslateX, double newTranslateY)
	{
		
		//Die Koordinate liegt schon im richtigen Format vor, deswegen direkt an die Layout Methoden übergeben
		this.setLayoutX(newTranslateX);
        this.setLayoutY(newTranslateY);
        
        
   
     
		
	}

	
	private double parentX(double localX) 
	{
		return nodeX() + localX;
	}

	private double parentY(double localY) 
	{
		return nodeY() + localY;
	}

	public boolean isMovePossible(MouseEvent t) 
	{
		return true;
	}
	
	
	 
	 private void drawMinorTick(double gaugeSizeNow, boolean isInit)
	 {
		 //size zu beginn ist 110 => 100 / 128 * 110 = 85,9375 => 0.859375
		 double r = gaugeSizeNow * 0.859375 / 2d;
		 
		 //tick länge orginal war 3 => 100 / 128 * 3 => 2,34375 => 0.0234375
		 double tickLenMinor = gaugeSizeNow * 0.0234375;
		 
		 //strokewidth orginal 2 = 100 / 128 *2 => 1,5625 => 0.015625
		 double strokeWidth = gaugeSizeNow * 0.015625;
		 
		 double START_ANGLE = 0;
		 double stepAngle = 6; //(180 / (n-1))
		 double nextAngle = START_ANGLE;
		 double cosValue;
		 double sinValue;
		 
		 for(int i = 1; i <= 31; i++)
		 {
			 sinValue = Math.sin(Math.toRadians(nextAngle));
			 cosValue = Math.cos(Math.toRadians(nextAngle));
		
			 Line line = null;
			 if(isInit)
				 line = new Line();
			 else
				 line = minorMap.get(i);
			 
		     line.setStartX((float)(centerX - cosValue*r));
		     line.setStartY( (float)(centerY - sinValue*r));
		     line.setEndX((float)(centerX - cosValue*(r - tickLenMinor)));
		     line.setEndY((float)(centerY - sinValue*(r - tickLenMinor)));
		     line.setStrokeWidth(strokeWidth);
		     line.setStroke(Color.web("#434444"));
		     Glow glow = new Glow();
		     glow.setLevel(0.2);
		     line.setEffect(glow);
		     //TODO evtl. die 36er Werte nicht zeichnen...mal sehen
		     if(isInit)
		     {
		    	 this.getChildren().add(line);
		    	  minorMap.put(i, line);
		     }
		     nextAngle = nextAngle + stepAngle;
		   
		 }
		 
	 }
	 
	 /**
	  * Zeichnet die major ticks
	  * @param gaugeSizeNow ist immer die Berechnungsgröße 100% 
	  * @param isInit = true; dann ist ein Objekt zu erzeugen und es in die Map zulegen bzw. auf die Oberfläche;
	  * <br>bei false wird das OBjekt nur aus der Oberfläche geholt und angepasst
	  */
	 private void drawMajorTick(double gaugeSizeNow, boolean isInit)
	 {
		 //size zu beginn ist 110 => 100 / 128 * 110 = 85,9375 => 0.859375
		 double r = gaugeSizeNow * 0.859375 / 2d;
		 //tick len 7 im Orginal bei 128 breite = (100 / 128) * 7 = 5,46875% => 0.0546875
		 double tickLen = gaugeSizeNow * 0.0546875;
		
		 //war zu beginn 3 pixel; 100 / 128 * 3 = 2,34375% => 0.0234375
		 double strokeWidth = gaugeSizeNow * 0.0234375;
		 
		 double START_ANGLE = 0;
		 double stepAngle = 36; //(180 / (n-1))
		 double nextAngle = START_ANGLE;
		 double cosValue;
		 double sinValue;
		 for(int i = 1; i <= 6; i++)
		 {
			 sinValue = Math.sin(Math.toRadians(nextAngle));
			 cosValue = Math.cos(Math.toRadians(nextAngle));
			 Line line = null;
			 if(isInit)
				 line = new Line();
			 else
				 line = majorMap.get(i);
			 
			 line.setStartX((float)(centerX - cosValue * r));
		     line.setStartY( (float)(centerY - sinValue * r));
		     line.setEndX((float)(centerX - cosValue*(r - tickLen)));
		     line.setEndY((float)(centerY - sinValue*(r - tickLen)));
		     line.setStrokeWidth(strokeWidth);
		     line.setStroke(Color.web("#007884"));
		     Glow glow = new Glow();
		     glow.setLevel(0.4);
		     line.setEffect(glow);
		     if(isInit)
		     {
		    	 this.getChildren().add(line);
			     //Wird für den resize benötigt
			     majorMap.put(i, line);
		     }
		     
		     nextAngle = nextAngle + stepAngle;
		 }
	 }
	 
	 public void setCurrentValue(double currentValue, boolean isInit)
	 {
		this.currentValue = currentValue;
		 
		double gaugeSize  = getWidth() < getHeight() ? getWidth() : getHeight();
		 
		double r = gaugeSize * 0.90625 / 2d;
		 
		double laenge = gaugeSize * 0.40625;
		 
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
		this.drawTextValues(true);
					 
	
		 
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
				
				
				
				int minValue = (int)RANGE_MIN * 10;
				int maxValue = (int)RANGE_MAX * 10;
				
				//TODO muss noch auseinander gedrösselt werden aus Testbereich und Bereich der auch in der ANwendung
				//benötigt wird.
				
				while(isAnimation)
				{
					//wert per Zufall ermitteln ein Wert von rangeMin bis rangeMax
					Random ran = new Random();
					int zufallszahl = ran.nextInt((maxValue - minValue) + 1);
					double neuerWert = (double)zufallszahl / 10D;
					if(neuerWert != currentValue)
					{
						double differenz = 0;
						double startWert = currentValue;
						boolean vorwaertsImmer = true;
						if(currentValue > neuerWert)
						{
							differenz = (currentValue - neuerWert);
							vorwaertsImmer = false;
						}
						else
						{
							differenz = (neuerWert - currentValue);
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
						Platform.runLater(() -> setCurrentValue(neuerWert, false));
					}
					
					
					
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
}
