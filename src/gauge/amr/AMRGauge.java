package gauge.amr;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
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
import javafx.scene.transform.Rotate;


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
	 * 
	 */
	private double RANGE_MIN = 0D;
	
	private double RANGE_MAX = 100D;
	
	/**
	 * starting value
	 */
	private double currentValue = 50D;
	
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
	
	private Arc segementInlay;
	
	
	private Polygon backgroundNeedle, foregroundNeedle;
	
	private Circle backgroundNeedlePick;
	
	private DropShadow dropNeedleShadow;
	
	/**
	 * Rotation der Nadel zu Beginn ist der Anfangswert der Rotation 0 und die Nadel steht auf 12 Uhr.
	 * <br>Eine Bewegung findet von -90° zu +90° statt => 180°
	 * <br>Ursprünglich waren es drei Rotation Objekte, jedoch brauche ich doch nur eines für alle Nadelkomponenten
	 */
	private Rotate needleRotate;
	
	
	
	//Enums für die gespeicherten Farben
	public enum StopIndizes
	{
		//für die hintergründe
		RAHMEN_GLANZ,
		
		INLAY_BORDER,
		
		INLAY_SEGMENT,
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
		
		this.getChildren().addAll(hintergrund, rahmen_hintergrundfarbe, rahmen_glanz, basis_farbe, 
				greenSegment, yellowSegment, redSegment, inlayRand, segementInlay, backgroundNeedle, backgroundNeedlePick, foregroundNeedle
				, deckflaecheBegrenzer);
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
		//Muss immer gesetzt werden, damit auch die Nadel an der richtigen Position anliegt.
		this.setCurrentValue(this.currentValue, false);
		
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
	
	 public void setCurrentValue(double currentValue, boolean isInit)
	 {
		this.currentValue = currentValue;
		 
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
		//this.drawTextValues(true);
					 
	
		 
	 }


	public void setNewLowPercentValue(double doubleInPercent)
	{
		double newValue = 180d/100d*doubleInPercent;
		
		System.out.println("newValue + " + newValue);
		
		double invertAngle = 180d-newValue;
		invertAngle = invertAngle - this.startingAngleYellow;
		System.out.println("invert " + invertAngle);
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
		
		
		System.out.println("starting angle " + startingAngleYellow);
		
	}

}
