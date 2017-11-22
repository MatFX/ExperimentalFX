package control.dimmer;

import java.util.HashMap;

import javafx.scene.effect.Glow;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class DimmerControl extends Region
{
	public enum StopIndizes
	{
		GLANZ_RAND
		//Enums für die gespeicherten Farben
		, DREHRAD_GLANZ, GLANZ_MONITOR, GLANZ_ANFASSER
		
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
	
	private Circle basis1, basis2, basis3, glanzRand, schattierungDrehrad, drehrad, drehradGlanz, inhaltMonitor,
		inhaltMonitorKopie, glanzMonitor, anfasser, anfasserGlanz;
	
	private LinearGradient linearGradGlanzRand, linearGradDrehradGlanz, linearGradientGlanzMonitor, linearGradientAnfasser;
	
	/**
	 * Farbmap weil die Werte unveränderlich sind und bei resize wieder bei den Gradienten neu gesetzt werden müssen.
	 */
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	/**
	 * Die Striche die auf der Oberfläche dargestellt werden sollen
	 */
	private HashMap<Integer, Line> tickMap = new HashMap<Integer, Line>();
	
	
	public DimmerControl()
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
		
		basis1 = new Circle();
		basis1.setFill(Color.web("#383838"));
		
		//r = 63,5
		basis2 = new Circle();
		basis2.setFill(Color.web("#000000"));
		
		//r 63
		basis3 = new Circle();
		basis3.setFill(Color.web("#2D2D2D"));
		
		
		Stop[] stopArray = new Stop[]{
				new Stop(0.0, Color.web("#333333B3")),
				new Stop(0.4390244, Color.web("#666666B3")),
				new Stop(0.4448765, Color.web("#656565B3")),
				new Stop(0.6809008, Color.web("#3C3C3CB3")),
				new Stop(0.8675336, Color.web("#232323B3")),
				new Stop(0.9804878, Color.web("#1A1A1AB3"))
			};
		stopMap.put(StopIndizes.GLANZ_RAND, stopArray);

		glanzRand = new Circle();
		
		
		
		
		schattierungDrehrad = new Circle();
		schattierungDrehrad.setFill(Color.web("#0F0F0F"));
		
		drehrad = new Circle();
		drehrad.setFill(Color.web("#555654"));
		
		stopArray = new Stop[]{
				new Stop(0.0195122, Color.web("#99999980")),
				new Stop(0.1839091, Color.web("#90909084")),
				new Stop(0.4555427, Color.web("#7777778B")),
				new Stop(0.7987309, Color.web("#4E4E4E94")),
				new Stop(1.0, Color.web("#33333399"))
			};
		stopMap.put(StopIndizes.DREHRAD_GLANZ, stopArray);
		
		drehradGlanz = new Circle();
		//TODO drehradObenGlanz
		
		inhaltMonitor = new Circle();
		inhaltMonitor.setFill(Color.web("#0F0F0F"));
		
		inhaltMonitorKopie = new Circle();
		inhaltMonitorKopie.setFill(Color.web("#949C87"));
		
		
		stopArray = new Stop[]{
				new Stop(0.004878, Color.web("#FFFFFF80")),
				new Stop(0.368867, Color.web("#B2B8A9A3")),
				new Stop(0.5341464, Color.web("#949C87B3")),
				new Stop(1.0, Color.web("#949C8700"))
			};
		
		
		stopMap.put(StopIndizes.GLANZ_MONITOR, stopArray);
		glanzMonitor = new Circle();
		
	
		anfasser = new Circle();
		anfasser.setFill(Color.web("#282828"));
		
		stopArray = new Stop[]{
				new Stop(0.0195122, Color.web("#66666680")),
				new Stop(0.3291745, Color.web("#55555557")),
				new Stop(1.0, Color.web("#33333300"))
			};
		stopMap.put(StopIndizes.GLANZ_ANFASSER, stopArray);
		
		anfasserGlanz = new Circle();
		
		
		
		this.getChildren().addAll(basis1, basis2, basis3, glanzRand 
				,schattierungDrehrad, drehrad, drehradGlanz, /*TODO drehrad oben glanz */ 
				inhaltMonitor, inhaltMonitorKopie, glanzMonitor
				,anfasser, anfasserGlanz);
		
		
		//ticks setzen, dass geht erst immmer dann wenn alles andere hinzugefügt worden ist.
		drawMinorTick(width, true);
	}
	
	

	/**
	 * Zeichnet die Ticks für die spätere Helligkeits Darstellung
	 * @param sizeNow
	 * @param isInit
	 */
	private void drawMinorTick(double sizeNow, boolean isInit) 
	{
		//121 breite 100 / 128 * 121 = 94,53125 = 0.9453125
		double r = sizeNow * 0.9453125 / 2d;
		
		//tick länge orginal war 3 => 100 / 128 * 3 => 2,34375 => 0.0234375
		double tickLenMinor = sizeNow * 0.0234375;
		
		//strokewidth orginal 2 = 100 / 128 *2 => 1,5625 => 0.015625
		double strokeWidth = sizeNow * 0.015625;
		
		double START_ANGLE = 315;
		
		double stepAngle = 9; //(180 / (n-1))
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
				 line = tickMap.get(i);
			 
		     line.setStartX((float)(centerX - cosValue*r));
		     line.setStartY( (float)(centerY - sinValue*r));
		     line.setEndX((float)(centerX - cosValue*(r - tickLenMinor)));
		     line.setEndY((float)(centerY - sinValue*(r - tickLenMinor)));
		     line.setStrokeWidth(strokeWidth);
		     line.setStroke(Color.web("#626262"));
		     Glow glow = new Glow();
		     glow.setLevel(0.3);
		     line.setEffect(glow);
		     //TODO evtl. die 36er Werte nicht zeichnen...mal sehen
		     if(isInit)
		     {
		    	 this.getChildren().add(line);
		    	 tickMap.put(i, line);
		     }
		     nextAngle = nextAngle + stepAngle;
		   
		 }
		 
		
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
		basis1.setRadius(1 * radius); 
		basis1.setCenterX(centerX);
		basis1.setCenterY(centerY);
		
		//63,5 = 100/64 * 63,5 = 0,9921875
		basis2.setRadius(0.9921875 * radius);
		basis2.setCenterX(centerX);
		basis2.setCenterY(centerY);
		
		//63 = 100/64 * 63 = 98,4375
		basis3.setRadius(0.984375 * radius);
		basis3.setCenterX(centerX);
		basis3.setCenterY(centerY);
		
		//63 = 100/64 * 63 = 98,4375
		glanzRand.setRadius(0.984375 * radius);
		glanzRand.setCenterX(centerX);
		glanzRand.setCenterY(centerY);
		
		// x1="64" y1="1" x2="64" y2="127"
		//y1 100/128 * 1 = 0.0078125
		//y2 = 100/128 *127 = 0,9921875
		linearGradGlanzRand = new LinearGradient(centerX, (getHeight() * 0.0078125 ) , centerX, (getHeight()  *  0.9921875), false, 
				CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.GLANZ_RAND));
		glanzRand.setFill(linearGradGlanzRand);
		
		
		// cx="64" cy="66" r="53"
		schattierungDrehrad.setCenterX(centerX);
		//100/64 * 66 = 103,125 = 1.03125
		schattierungDrehrad.setCenterY(centerY * 1.03125);
		//r = 53 > 100/64 * 53 = 82,8125 = 0.828125
		schattierungDrehrad.setRadius(radius * 0.828125);
		
		//cx="64" cy="64" r="53"/
		drehrad.setCenterX(centerX);
		drehrad.setCenterY(centerY);
		drehrad.setRadius(radius * 0.828125);
		
		//x1="64" y1="6.75" x2="64" y2="109.6470947"
		//y1 = 100/128 * 6.75 = 5,2734375 = 0.052734375
		//y2 = 100/128 * 109.6470947 = 85,661792734375 = 0.85661792734375
		linearGradDrehradGlanz = new LinearGradient(centerX, (getHeight() * 0.052734375), centerX, (getHeight() * 0.85661792734375), false,
				CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.DREHRAD_GLANZ));
		
		//nochmals  cx="64" cy="64" r="53"
		drehradGlanz.setCenterX(centerX);
		drehradGlanz.setCenterY(centerY);
		drehradGlanz.setRadius(radius * 0.828125);
		drehradGlanz.setFill(linearGradDrehradGlanz);
		
		//TODO DrehradObenRand
		
		//cx="64" cy="64" r="38"
		inhaltMonitor.setCenterX(centerX);
		inhaltMonitor.setCenterY(centerY);
		//100/64 * 38 = 59,375 = 0.59375
		inhaltMonitor.setRadius(radius * 0.59375);
		
		
		inhaltMonitorKopie.setCenterX(centerX);
		inhaltMonitorKopie.setCenterY(centerY);
		//100/64 * 37.5 = 58,59375 = 0.5859375
		inhaltMonitorKopie.setRadius(radius * 0.5859375);

		
		//x1="64" y1="20.25" x2="64" y2="92.3024979"
		//y1 = 100/128 * 20,25 = 15,8203125 = 0.158203125
		//y2 = 100/128 * 92,3024979 = 72,111326484375 = 0.72111326484375
		linearGradientGlanzMonitor = new LinearGradient(centerX, getHeight() * 0.158203125, centerX, getHeight() * 0.72111326484375,
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.GLANZ_MONITOR));
		
		glanzMonitor.setCenterX(centerX);
		glanzMonitor.setCenterY(centerY);
		//r = 37,5
		//100/64 * 37,5 =  58,59375 = 0.5859375
		glanzMonitor.setRadius(radius * 0.5859375);
		glanzMonitor.setFill(linearGradientGlanzMonitor);
		
		//cx="23.75" cy="83.2325363" r="5.5"
		
		//TODO da muss später noch die Drehung mit rein
		
		//die Berechnung muss vom Mittelpunkt erfolgen
		//folglich ist die Entfernung festzustellen
		
		//cx = 64 - 23,75 = 40,25 = das ist die Strecke die von x abgezogen werden soll
		//differenz von center zu position
		//100/128 * 40,25 = 31,4453125 = 0.314453125
		anfasser.setCenterX(centerX - (size * 0.314453125));
		//83.2325363  - 64 = 19,2325363
		//100/128 * 19,2325363 = 15,025418984375 = 0.15025418984375
		anfasser.setCenterY(centerY + (size * 0.15025418984375));
		//r = 5,5  100/64 * 5,5 = 8,59375 = 0.0859375
		anfasser.setRadius(radius * 0.0859375);
		
		//x1="23.75" y1="78.2325363" x2="23.75" y2="88.2325363"
		
		//x1 = 100/128 * 23.75 = 18,5546875 = 0.185546875
		//y1 = 100/128 * 78.2325363 = 61,119168984375 = 0.61119168984375
		//x2 = 100/128 * 23.75 = 18,5546875 = 0.185546875
		//y2 = 100/128 * 88.2325363 = 68,931668984375 = 0.68931668984375
		linearGradientAnfasser = new LinearGradient(getWidth() * 0.185546875, getHeight() * 0.61119168984375,
				getWidth() * 0.185546875, getHeight() * 0.68931668984375, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.GLANZ_ANFASSER));
		
		//cx="23.75" cy="83.2325363" r="5"
		
		//r = 100/64 * 5 = 7,8125 = 0.078125
		anfasserGlanz.setCenterX(centerX - (size * 0.314453125));
		anfasserGlanz.setCenterY(centerY + (size * 0.15025418984375));
		anfasserGlanz.setRadius(radius * 0.078125);
		anfasserGlanz.setFill(linearGradientAnfasser);
		
		
		drawMinorTick(size, false);
		
			
	}
	
}
