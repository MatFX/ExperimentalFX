package control.dimmer;

import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import control.dimmer.IActivationIcon;

public class DimmerControl extends Region implements IActivationIcon
{
	public enum Command
	{
		ON, OFF, SEND_PRESET, NEXT_PRESET, PREVIOUS_PRESET, SEND_VALUE, 
		/**
		 * der wird dann von außerhalb gesetzt, damit auch das aktuelle Kommando nochmal gesetzt werden kann.
		 */
		RESET_COMMAND;
	}
	
	
	public enum StopIndizes
	{
		GLANZ_RAND
		//Enums für die gespeicherten Farben
		, DREHRAD_GLANZ, GLANZ_MONITOR, GLANZ_ANFASSER,
		GLANZ_KANTE_OBEN
		
	}
	
	
	/**
	 * Range ist von bis der auch angepasst werden kann.
	 */
	private double RANGE_MIN = 0D;
	
	private double RANGE_MAX = 100D;
	
	private double currentValue = 0D;
	
	private double width = 128, height = 128;
	
	private double centerX = 64, centerY = 64;
	
	private int startIndex = 0;
	
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
	private TreeMap<Integer, Line> tickMap = new TreeMap<Integer, Line>();
	
	private boolean isAnimation = false;
	
	private Thread animThread = null;
	
	private Text textCurrentValue;
	
	private Text masseinheit;
	
	/**
	 * Hier erfolgt die Anzeige des zur Zeit dargestellten Wertes
	 */
	private Canvas textCanvas;
	
	/**
	 * Den Anfasser drehen lassen unter bezugnahme der mittelachse
	 */
	private Rotate anfasserRotate;

	/**
	 * Bereich in dem der Anfasser sich bewegen darf in grad 
	 */
	private final double  ANGLE_RANGE_SELECTOR = 240; 
	
	/**
	 * der obere helle Schein war in der SVG ein komplexe SVG Pfad. Da diese aber nicht so einfach zu skalieren sind
	 * <br>gibt es hier den Behelf diesen so nachzubauen wie er in Illustrater entstanden ist.
	 */
	private TopRegion topRegion;
	
	private Arc button_on, button_off, button_left, button_right, button_send;
	
	private DropShadow dropShadow;
	
	private InnerShadow innerShadow;
	
	/**
	 * 
	 */
	private SimpleObjectProperty<Command> commandProperty = new SimpleObjectProperty<Command>();
	
	/**
	 * Diese sind optional und können von außerhalb gesezt werden.
	 */
	private double[] presetValues = new double[0];
	
	/**
	 * der Index durch klick von vor und zurück wird dieser Wert verändert.
	 */
	private int presetIndex = 0;
	
	/**
	 * Dieser Text wird nur dann dargestellt wenn der Anwender einen der Preset Button verwendet
	 */
	private Text textPreset;
	
	/**
	 * Zeichnungsfläche für textPreset
	 */
	private Canvas textPresetCanvas;
	
	/**
	 * sind presets in er Anzeige sichtbar?
	 */
	private boolean isPresetsOnScreen;
	
	private Text textOn, textOff, textLeft, textRight, textSend;
	
	//private Color textColor = Color.web("#FF0000");
	//wobei rot hat schon sehr gut ausgesehen
	private Color textColor = Color.web("#d2d74b");
	
	/**
	 * Sorgt dafür, dass die gewählte Voreinstellung von der Oberfläche verschwindet wenn nach Zeit X der Anwender
	 * <br>nicht den Sende button gedrückt hat.
	 */
	private Timeline presetViewReset;
	
	/**
	 * für den Text wenn der button gedrückt wurde
	 */
	private Glow textGlow = new Glow(0.3);
	
	private OptionaImageBox optionalImageBox;
	
	public DimmerControl()
	{
		
		this.initGraphics();
		this.registerListener();
	}

	private void registerListener() 
	{
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
		
		drehradGlanz.setOnMouseReleased(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				drehung(event);

				//jetzt Signalisierung an die Außenwelt, damit die nun die gewünschte Veränderung durchführt.
				commandProperty.set(Command.SEND_VALUE);
				
			}
			
		});
		
		drehradGlanz.setOnMouseDragged(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) 
			{
				drehung(event);
			}
			
		});
		
		
		//direkt auf den Anfasser geklickt, dann gehts hier weiter
		anfasserGlanz.setOnMouseDragged(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) 
			{
				drehung(event);
				
			}
			
		});
		
		anfasserGlanz.setOnMouseReleased(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				drehung(event);
				//jetzt Signalisierung an die Außenwelt, damit die nun die gewünschte Veränderung durchführt.
				commandProperty.set(Command.SEND_VALUE);
				
			}
			
		});
		
		
		//remark: Problems with change style on button, when the user will aplly touch functionality
		//link: https://bugs.openjdk.java.net/browse/JDK-8139118
		
		//action listener für die Buttons
		button_on.setOnMousePressed(e -> setNodePressed(button_on, textOn, Command.ON));
		button_on.setOnMouseReleased(e -> setNodeReleased(button_on, textOn));

		button_off.setOnMousePressed(e -> setNodePressed(button_off, textOff, Command.OFF));
		button_off.setOnMouseReleased(e -> setNodeReleased(button_off, textOff));
		
		button_left.setOnMousePressed(e -> setPreviousPresetNodePressed(button_left, textLeft, Command.PREVIOUS_PRESET));
		button_left.setOnMouseReleased(e -> setNodeReleased(button_left, textLeft));
		
		button_right.setOnMousePressed(e -> setNextPresetNodePressed(button_right, textRight, Command.NEXT_PRESET));
		button_right.setOnMouseReleased(e -> setNodeReleased(button_right, textRight));
		
		button_send.setOnMousePressed(e -> setNodePressed(button_send, textSend, Command.SEND_PRESET));
		button_send.setOnMouseReleased(e -> setNodeReleased(button_send, textSend));
	}
	
	
	
	
	private void drehung(MouseEvent event)
	{
		
		//Berücksichtigt wird bereits min ist aber zur Zeit noch nicht einstellbar
		double schrittweite = ANGLE_RANGE_SELECTOR / (RANGE_MAX - RANGE_MIN);
		
		double deltaX = event.getSceneX() - (centerX);
		double deltaY = event.getSceneY() - (centerY);
	    double radius = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    
	    double  nx     = deltaX / radius;
        double  ny     = deltaY / radius;
        double  theta  = Math.atan2(ny, nx);
        
        theta = Double.compare(theta, 0.0) >= 0 ? Math.toDegrees(theta) : Math.toDegrees((theta)) + 360.0;
        double angle  = (theta + 210) % 360;
        if (angle > 320 && angle < 360) 
        {
            angle = 0;
        }
        else if (angle <= 320 && angle > ANGLE_RANGE_SELECTOR) 
        {
            angle = ANGLE_RANGE_SELECTOR;
        }
        
        double valueToSet = (angle / schrittweite + RANGE_MIN);
        setCurrentValue((int) valueToSet, false);
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
		
		//glanz für Kante oben w
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#00000099")),
				new Stop(0.145946, Color.web("#B3B3B3")),
				new Stop(0.2731459, Color.web("#7C7C7C")),
				new Stop(0.4117662, Color.web("#474747")),
				new Stop(0.5160054, Color.web("#262626")),
				new Stop(0.572973, Color.web("#1A1A1A")),
				new Stop(0.6123148, Color.web("#202020")),
				new Stop(0.6671478, Color.web("#323232")),
				new Stop(0.7307787, Color.web("#505050")),
				new Stop(0.7702703, Color.web("#666666")),
				new Stop(1.0, Color.web("#1A1A1A99"))
			};
		stopMap.put(StopIndizes.GLANZ_KANTE_OBEN, stopArray);
		
	
		anfasser = new Circle();
		anfasser.setFill(Color.web("#282828"));
		
		stopArray = new Stop[]{
				new Stop(0.0195122, Color.web("#66666680")),
				new Stop(0.3291745, Color.web("#55555557")),
				new Stop(1.0, Color.web("#33333300"))
			};
		stopMap.put(StopIndizes.GLANZ_ANFASSER, stopArray);
		
		anfasserGlanz = new Circle();
	
		textPresetCanvas = new Canvas();
		textPreset = new Text();
	
		textCanvas = new Canvas();
		
		textCurrentValue = new Text();
		//TODO vertl. stringproperty koppeln den Wert
		textCurrentValue.setText(""+currentValue);
		
		masseinheit = new Text();
		//TODO optional und variable machen
		masseinheit.setText("%");
		
		Circle highlightColor = new Circle();
		//diese Farbe ist später entscheidend für die Darstellung
		highlightColor.setFill(Color.web("848484"));
		Circle maskingCircle = new Circle();
		
		
		//TODO evtl. verfeinern
		dropShadow = new DropShadow();
	    dropShadow.setColor(Color.web("000000A0"));
	   
	    

		innerShadow = new InnerShadow();
		innerShadow.setBlurType(BlurType.GAUSSIAN);
		innerShadow.setColor(Color.web("#000000A0"));
	        
		
		button_on = new Arc();
		//button_on = new Arc(centerX, centerY, 64, 64, 0, 120);
		button_on.setFill(Color.web("404040"));
		button_on.setType(ArcType.ROUND);
		button_on.setEffect(dropShadow);

	        
		button_off = new Arc();
		button_off.setFill(Color.web("404040"));
		button_off.setType(ArcType.ROUND);
		button_off.setEffect(dropShadow);
		
		button_left = new Arc();
		button_left.setFill(Color.web("404040"));
		button_left.setType(ArcType.ROUND);
		button_left.setEffect(dropShadow);
		
		button_right = new Arc();
		button_right.setFill(Color.web("404040"));
		button_right.setType(ArcType.ROUND);
		button_right.setEffect(dropShadow);
		
		button_send = new Arc();
		button_send.setFill(Color.web("404040"));
		button_send.setType(ArcType.ROUND);
		button_send.setEffect(dropShadow);
		
		//TODO wie mache ich es mit den zwei unterschiedlichen Sprachen?
		textOn = new Text("On");
		textOn.setMouseTransparent(true);
		textOff = new Text("Off");
		textOff.setMouseTransparent(true);
		textLeft = new Text("<");
		textLeft.setMouseTransparent(true);
		textRight = new Text(">");
		textRight.setMouseTransparent(true);
		textSend = new Text("°");
		textSend.setMouseTransparent(true);
		
		optionalImageBox = new OptionaImageBox();
		
		
		topRegion = new TopRegion(highlightColor, maskingCircle);
		
		
		this.getChildren().addAll(basis1, basis2, basis3,
				
				glanzRand, 
				button_on, button_off, button_left, button_right, button_send,
				textOn, textOff, textLeft, textRight, textSend,
				//TODO hier die textfelder
				schattierungDrehrad, drehrad,
				//zwei kreise der masking anteil ist ein wenig abgesetzt vom highlight Anteil
				//weiterhin ist der Maskingteil mit der Grundfarbe des drehrades belegt..alles in TopRegion
				topRegion, 
				drehradGlanz, /* glanzKante ? */
				inhaltMonitor, inhaltMonitorKopie,  glanzMonitor, textPresetCanvas, textCanvas
				,optionalImageBox
				//TODO evtl. textCanvas noch nach vorne ziehen unterhalb von glanzMonitor
				,anfasser, anfasserGlanz);
		
		drawTextValues(true);
		
		//ticks setzen, dass geht erst immmer dann wenn alles andere hinzugefügt worden ist.
		drawMinorTick(width, true);
		
		//Vorbereitung der Rotation
		//TODO keine Ahnung was zu beginn gesetzt werden muss
		anfasserRotate = new Rotate(0);
		this.anfasser.getTransforms().add(anfasserRotate);
		this.anfasserGlanz.getTransforms().add(anfasserRotate);
		
		
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
		
		double START_ANGLE = 330;
		
		double stepAngle = 7; //(180 / (n-1))
		double nextAngle = START_ANGLE;
		double cosValue;
		double sinValue;
		
		
		 for(int i = 1; i <= 35; i++)
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
		
		button_on.setCenterX(centerX);
		button_on.setCenterY(centerY);
		
		//abstand 62r beim Start
		//62 = 100/64 * 62 = 96,875
		button_on.setRadiusX(radius*0.96875);
		button_on.setRadiusY(radius*0.96875);
		button_on.setStartAngle(-120.0f);
		button_on.setLength(-25.0f);
		button_on.setType(ArcType.ROUND);
		
		Font valueFont = new Font("Verdana", size * 0.05);
		textOn.setFill(textColor);
		textOn.setFont(valueFont);
		textOn.setRotate(35);
		
		textOn.relocate(centerX - (radius * 0.675), centerY + (radius * 0.61));

		
		textLeft.setFill(textColor);
		textLeft.setFont(valueFont);
		textLeft.setRotate(20);
		textLeft.relocate(centerX - (radius * 0.345), centerY + (radius * 0.795));
		
		textSend.setFill(textColor);
		textSend.setFont(valueFont);
		textSend.relocate(centerX - (radius * 0.03), centerY + (radius * 0.87));
		
		textRight.setFill(textColor);
		textRight.setFont(valueFont);
		textRight.setRotate(-20);
		textRight.relocate(centerX + (radius * 0.25), centerY + (radius * 0.795));

		textOff.setFill(textColor);
		textOff.setFont(valueFont);
		textOff.setRotate(-45);
		textOff.relocate(centerX + (radius * 0.54), centerY + (radius * 0.61));
		
		
		button_off.setCenterX(centerX);
		button_off.setCenterY(centerY);
		button_off.setRadiusX(radius*0.96875);
		button_off.setRadiusY(radius*0.96875);
		button_off.setStartAngle(-35.0f);
		button_off.setLength(-25.0f);
		button_off.setType(ArcType.ROUND);
		
		
		button_left.setCenterX(centerX);
		button_left.setCenterY(centerY);
		button_left.setRadiusX(radius*0.96875);
		button_left.setRadiusY(radius*0.96875);
		button_left.setStartAngle(-98.5f);
		button_left.setLength(-20.0f);
		button_left.setType(ArcType.ROUND);
		
		
		button_right.setCenterX(centerX);
		button_right.setCenterY(centerY);
		button_right.setRadiusX(radius*0.96875);
		button_right.setRadiusY(radius*0.96875);
		button_right.setStartAngle(-61.5f);
		button_right.setLength(-20.0f);
		button_right.setType(ArcType.ROUND);
		
		button_send.setCenterX(centerX);
		button_send.setCenterY(centerY);
		button_send.setRadiusX(radius*0.96875);
		button_send.setRadiusY(radius*0.96875);
		button_send.setStartAngle(-82.5f);
		button_send.setLength(-15.0f);
		button_send.setType(ArcType.ROUND);
		
		
		
		
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
		
		//die Berechnung muss vom Mittelpunkt erfolgen
		//folglich ist die Entfernung festzustellen
		
		//cx = 64 - 23,75 = 40,25 = das ist die Strecke die von x abgezogen werden soll
		//differenz von center zu position
		//100/128 * 40,25 = 31,4453125 = 0.314453125
		
		//Pivot immer anpassen damit der Anfasser nicht einmal in die Wallachei verscwindet
		anfasserRotate.setPivotX(centerX);
		anfasserRotate.setPivotY(centerY);
		//cxNeu = 64 - 24,96 = 39,04 
		//100/128 * 39,04 = 30,5 = 0,305
		anfasser.setCenterX(centerX - (size * 0.305));
		//83.2325363  - 64 = 19,2325363
		//100/128 * 19,2325363 = 15,025418984375 = 0.15025418984375
		//86,548 - 64 = 22,548 
		//100/128 * 22,548 = 17,615625 = 0.17615625
		anfasser.setCenterY(centerY + (size * 0.17615625));
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
		anfasserGlanz.setCenterX(centerX - (size * 0.305));
		anfasserGlanz.setCenterY(centerY + (size * 0.17615625));
		anfasserGlanz.setRadius(radius * 0.078125);
		anfasserGlanz.setFill(linearGradientAnfasser);
		
		
		topRegion.setNewValues(centerX, centerY, radius * 0.828125, +5d);
		topRegion.resize();

		
		
		
		
		double w = size * 0.5;
		double h = size * 0.171875;
		double x = centerX - (w/2); 
		double y = centerY - (h/2);
		textCanvas.setWidth(w);
		textCanvas.setHeight(h);
		textCanvas.relocate(x, y);
		drawTextValues(true);
		
		
		//in der Breite soll diese ca. 60 prozent von dem monitorkreis sein.
		//in der höhe wird bei 128 Startsize ein Wert von 32 px skaliert
		
		//r war 37,5 * = 70 > 100/128 * 70  = 54,6875 = 54,6875 * ,6 =  32,4
		double breiteImages = size * 0.546875 * 0.6;
		
		
		//100/64 * 32 = 25 =0-25
		double hoeheImages = size/2 * 0.25;
		System.out.println("opt " + breiteImages + "  " + hoeheImages);
		//optionalImageBox.setLayoutX(value);
		
		optionalImageBox.setMinSize(breiteImages, hoeheImages);
		optionalImageBox.setLayoutX(centerX - (breiteImages/2));
		optionalImageBox.setLayoutY(centerY + (textCanvas.getHeight()/2));
		optionalImageBox.resize(hoeheImages);
		//optionalImageBox.setStyle("-fx-background-color: #FF0000");
		
		
		//resize der canvas für preset und der Anzeige
		double p_w = size * 0.3;
		double p_h = size * 0.171875;
		
		//auf y komme ich über die text_canvas
		double p_y = y - p_h;
		double p_x = centerX - (p_w/2);
		textPresetCanvas.setWidth(p_w);
		textPresetCanvas.setHeight(p_h);
		textPresetCanvas.relocate(p_x, p_y);
		
		drawMinorTick(size, false);
	}
	
	/**
	 * Parameter im Regelfall true, außer man will die Sicht gelöscht haben
	 * @param showValues
	 */
	private void drawTextPresetValue(boolean showValues)
	{
		double size  = getWidth() < getHeight() ? getWidth() : getHeight();
		double w = textPresetCanvas.getWidth();
		double h = textPresetCanvas.getHeight();
		GraphicsContext gc = textPresetCanvas.getGraphicsContext2D();
		
		gc.clearRect(0, 0, w, h);
		
		//bei löschung der Sicht gleich wieder zurück
		if(!showValues)
			return;
		
		gc.setFill(Color.web("#00000080"));
		
		//erstmal zum test preset drei auswählen
		Font valueFont = new Font("Verdana", size * 0.06);
		
		
		String valueToShow = String.format("%.0f", presetValues[presetIndex]);
		valueToShow = valueToShow + " %";
		
		textPreset.setText(valueToShow);
		textPreset.setFont(valueFont);
		
		double valueX = (textPreset.getLayoutBounds().getWidth()  + (size * 0.018635));
		double valueY = (textPreset.getLayoutBounds().getHeight()  + (size * 0.015635));
		
		gc.setFont(valueFont);
		gc.fillText(textPreset.getText(), w - valueX  , valueY);
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
		gc.setFill(Color.web("#00000080"));
		
		Font masseinheitFont = new Font("Verdana", gaugeSize * 0.12);
		
		//leerzeichen oder keines?
		masseinheit.setText(" %");
		masseinheit.setFont(masseinheitFont);
		
		gc.setFont(masseinheitFont);
		
		double masseinheitX = w - (masseinheit.getLayoutBounds().getWidth() + (gaugeSize * 0.015635));
		
		//keien Ahnung wieso ich nicht den Mittelpunkt von H als Basis nehmen kann.
		double masseinheitY = masseinheit.getLayoutBounds().getHeight() -  (gaugeSize * 0.015635);
		gc.fillText(masseinheit.getText(), masseinheitX, masseinheitY);
		
		Font valueFont = new Font("Verdana", gaugeSize * 0.115);
		//TODO evtl. noch verschieben ohne komma schaut es so weit rechtsläufig aus.
		textCurrentValue.setText(String.format("%.0f", currentValue));
		textCurrentValue.setFont(valueFont);
		
		double valueX = masseinheitX - (textCurrentValue.getLayoutBounds().getWidth()  + (gaugeSize * 0.015635));
		double valueY = masseinheitY;
		
		gc.setFont(valueFont);
		gc.fillText(textCurrentValue.getText(), valueX, valueY);
	}
	
	/**
	 * Methode für das Sezten des aktuellen Zustands
	 * @param neuerWert
	 */
	public void setCurrentValue(double neuerProzentWert, boolean isInit) 
	{
		//wir haben 31 Ticks für die Beleuchtung
		
		//ermitteln wieviele ticks wir benötigen
		int showTickNumber =  (int) Math.round((double)35d/100d * (double)neuerProzentWert);
		int showTickNumberAktuell = (int) Math.round((double)35d/100d * (double)currentValue);
	
		double wertVonEinemTickInProzent = 100/35;
		
		if(!isInit)
		{
			if(showTickNumber > showTickNumberAktuell)
			{
				
				startIndex = showTickNumberAktuell + 1 ;
				do
				{
					
					tickMap.get(startIndex).setStroke(Color.web("#CCC200"));
					//jeder tick spiegelt einen Wert wieder der in auch zur Anzeige kommen muss
					
					double tempValue = wertVonEinemTickInProzent * (double)startIndex;
					this.currentValue = (int)Math.round(tempValue);
					this.drawTextValues(true);
					
					//TODO überlegen ob man ständig mitaktualisiert oder erst zum schluss
					this.setRotation(currentValue);
					
					
					
					try 
					{
						TimeUnit.MILLISECONDS.sleep(25);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startIndex ++;
				}
				while(startIndex <= showTickNumber);
			}
			else if(showTickNumber < showTickNumberAktuell)
			{
				//hier werden die Ticks abgedunkelt, deswegen beginnen mir mit den vorhergehenden Wert
				startIndex = showTickNumberAktuell;
				
				do
				{
					tickMap.get(startIndex).setStroke(Color.web("#626262"));
					
					double tempValue = wertVonEinemTickInProzent * (double)startIndex;
					this.currentValue = (int)Math.round(tempValue);
					this.drawTextValues(true);
					
					this.setRotation(currentValue);
					try {
						TimeUnit.MILLISECONDS.sleep(25);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startIndex--;
					
				}
				while(startIndex > showTickNumber);
				
				
			}
			//isAnimation =false;

			//bei gleich wird nichts gemacht
		}
	
		//Übernahme des aktuellen Wertes
		this.currentValue = neuerProzentWert;
		this.setRotation(currentValue);
		this.drawTextValues(true);
		
		
		
	
	
	}

	/**
	 * Parameterübergabe ist der gewünschte Wert der zur Darstellung kommen soll.
	 * <br>Im Regelfall der aktuelle Zustand.
	 * @param valueToSet
	 */
	private void setRotation(double valueToSet)
	{
		//Der Abzug von max und min spielt erstmal keine Rolle solange die Werte noch nicht variabel veränder bar sind...evtl. später
		double schrittweite = ANGLE_RANGE_SELECTOR / (RANGE_MAX - RANGE_MIN);
		anfasserRotate.setAngle(((valueToSet - RANGE_MIN) * schrittweite ));
	}
	
	public void setAnimatiedCurrentValue(double value)
	{
		
		Runnable runnable = new Runnable()
		{
			@Override
			public void run() 
			{
				isAnimation = true;
				setCurrentValue(value, false);
				
				
			}
			
		};
		
		animThread = new Thread(runnable);
		animThread.start();
		
	}
	

	public void startAnimation() 
	{
		if(animThread != null && animThread.isAlive())
			animThread.stop();
		
		isAnimation = true;
		int minValue = (int)RANGE_MIN;
		int maxValue = (int)RANGE_MAX;
		Runnable runnable = new Runnable(){

			@Override
			public void run() 
			{
				Random ran = new Random();
			
				while(isAnimation)
				{
					int neuerWert = ran.nextInt((maxValue - minValue) + 1);
					
					if(neuerWert != currentValue)
					{
						setCurrentValue(neuerWert, false);
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
	
	public SimpleObjectProperty<Command> getCommandProperty()
	{
		return commandProperty;
	}
	
	public double getCurrentValue()
	{
		return currentValue;
	}

	/**
	 * von außerhalb können die Voreinstellungswerte übergeben werden.
	 * @param presetValues
	 */
	public void setPresetValues(double ... presetValues) 
	{
		this.presetValues = presetValues;
	}
	
	private void nextPreset()
	{
		//noch nicht auf screen, dann den aktuellen wert erstmal anzeigen
		if(!isPresetsOnScreen)
		{
			drawTextPresetValue(true);
			isPresetsOnScreen = true;
			
		}
		else
		{
			
			
			//im anderen Fall hochsetzen und nächsten wert anzeigen
			presetIndex++;
			//wenn er größer als die Länge ist, dann wieder zurück auf den ersten Index
			if(presetIndex >= presetValues.length)
				presetIndex = 0;
			drawTextPresetValue(true);
		}
		
		
		restartPresetReset();
		
		
		
		
	}
	
	private void stopPresetViewReset()
	{
		if(presetViewReset != null)
		{
			presetViewReset.stop();
		}
		
	}
	
	private void restartPresetReset() 
	{
		stopPresetViewReset();
		presetViewReset = new Timeline();
		KeyFrame key = new KeyFrame(Duration.millis(3000));
		presetViewReset.getKeyFrames().add(key);
		presetViewReset.setOnFinished(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event) 
			{
				System.out.println("bin durch gelaufen");
				drawTextPresetValue(false);
			}
	
		});
		presetViewReset.play();
		
	}

	private void previousPreset()
	{
		//noch nicht auf screen, dann den aktuellen wert erstmal anzeigen
		if(!isPresetsOnScreen)
		{
			drawTextPresetValue(true);
			isPresetsOnScreen = true;
		}
		else
		{
			presetIndex--;
			//wenn der preset unter einem gültigen index fällt
			//dann zurück auf maximum
			if(presetIndex < 0)
			{
				presetIndex = presetValues.length-1;
			}
			drawTextPresetValue(true);
		}
		restartPresetReset();
		
	}
	
	public double getSelectedPresetValue()
	{
		return presetValues[presetIndex];
	}

	@Override
	public void setActivation(Pos position) {
		optionalImageBox.setActivation(position);
		
	}

	@Override
	public void setDeactivation(Pos position) {
		optionalImageBox.setDeactivation(position);
		
	}

	@Override
	public void initImage(Pos position, Image image) {
		optionalImageBox.initImage(position, image);
		
	}
	
	
	/**
	 * Die einzelnen Buttons werden alle gleich behandelt, war notwendig als ich feststelle, das ein Touch-Event 
	 * <br>nicht hunderprozentig als MouseEvent interpretiert wird.
	 * @param nodeBase
	 * @param textNode
	 */
	public void setNodeReleased(Arc nodeBase, Text textNode)
	{
		nodeBase.setEffect(dropShadow);
		textNode.setEffect(null);
	}
	
	/**
	 * analog zur o.a Erklärung
	 * @param nodeBase
	 * @param textNode
	 * @param command
	 */
	public void setNodePressed(Arc nodeBase, Text textNode, Command command)
	{
		nodeBase.setEffect(innerShadow);
		textNode.setEffect(textGlow);
		commandProperty.set(command);
		
		
	}
	
	/**
	 * Hier muss vor der Visualisierung noch auf die nächste Voreinstellung gegangen werden
	 * @param nodeBase
	 * @param textNode
	 * @param command
	 */
	public void setNextPresetNodePressed(Arc nodeBase, Text textNode, Command command)
	{
		nextPreset();
		setNodePressed(nodeBase, textNode, command);
		
	}
	
	
	public void setPreviousPresetNodePressed(Arc nodeBase, Text textNode, Command command)
	{
		previousPreset();
		setNodePressed(nodeBase, textNode, command);
	}
	
}
