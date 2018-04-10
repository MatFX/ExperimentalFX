package control.rgbw;

import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import control.universaldisplay.SensorValue;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


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
	
	/**
	 * current rgb color at the middle round view
	 */
	private Color currentColorRGB = Color.web("#1A1A96");
	
	private Canvas textCanvas;
	
	private Text textFirstValue, textFirstMeasuringUnit;
	
	private DoubleProperty scaleableFontSize = null;
	
	private Font fontVorgabe = null;
	
	/**
	 * white colored light
	 */
	private SensorValue majorValue;
	
	private static final double GAP_PERCENT = 0.1;
	
	private boolean isAnimation = false;
	
	private Thread animThread = null;
	
	private int startIndex = 0;
	
	/**
	 * compare object
	 */
	private Color EMPTY_COLOR = Color.web("#00000000");

	public RGBWDimmerControl()
	{
		this.initGraphics();
		this.registerListener();
	}

	private void registerListener() {
		
		
		canvasColoredCircle.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event)
			{
				PickResult result = event.getPickResult();
			
				SnapshotParameters params = new SnapshotParameters();
				params.setFill(Color.TRANSPARENT);
				Image snapshot = canvasColoredCircle.snapshot(params, null);
				
				
				
				/* TODO raus
				File outputFile = new File("./test.png");
			    BufferedImage bImage = SwingFXUtils.fromFXImage(snapshot, null);
			    //try {
			      try {
					ImageIO.write(bImage, "png", outputFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				
				PixelReader pixReader = snapshot.getPixelReader();
				
				Color selectedColor = pixReader.getColor((int)result.getIntersectedPoint().getX(), (int)result.getIntersectedPoint().getY());
				
				System.out.println("value " + selectedColor.toString());
				//"empty" pixel found?
				if(!selectedColor.toString().equals(EMPTY_COLOR.toString()))
				{
					currentColorRGB = selectedColor;
					anzeigeRGB.setFill(currentColorRGB);
				}
			}
			
		});
		
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
	//	canvasColoredCircle.setMouseTransparent(true);
		
		stopArray = new Stop[]{
				new Stop(0.7414634, Color.web("#00000000")),
				new Stop(0.9379081, Color.web("#00000083")),
				new Stop(0.9702439, Color.web("#00000099")),
				new Stop(1.0, Color.web("#000000"))
			};
		stopMap.put(StopIndizes.INLAY, stopArray);
		
		begrenzerInlay = new Circle();
		begrenzerInlay.setMouseTransparent(true);
		
		deckflaechenBegrenzung = new Circle();
		deckflaechenBegrenzung.setFill(Color.web("#282828"));
		
		stopArray = new Stop[]{
				new Stop(0.005618, Color.web("#666666B3")),
				new Stop(0.3678385, Color.web("#4D4D4DCE")),
				new Stop(1.0, Color.web("#1A1A1A"))
			};
		stopMap.put(StopIndizes.OVERLAY_INNER_CIRCLE, stopArray);
		
		deckflaechenBegrenzungOverlay = new Circle();
				
		this.getChildren().addAll(hintergrund, rahmen_grundfarbe, rahmen_glanz, basisFarbe, wahlkreis, canvasColoredCircle, begrenzerInlay,
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
		
		textCanvas = new Canvas();
		textFirstValue = new Text();
		textFirstValue.setText("");
		textFirstMeasuringUnit = new Text();
		textFirstMeasuringUnit.setText("");
		
		
		this.getChildren().addAll(basisAnzeige, anzeigeGlanzRahmen, anzeigeRGB, textCanvas, anzeigeGlanz);
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
		
		//45 = 100/128 * 45 = 0.3515625
		double w = size * 0.3515625;
		double h = size * 0.1;
		double x = centerX - (w/2); 
		double y = centerY - (h/2);
		textCanvas.setWidth(w);
		textCanvas.setHeight(h);
		textCanvas.relocate(x, y);
		

		this.drawTextValues(true);
		
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
		
		double valueX = masseinheitX - ((textFirstValue.getLayoutBounds().getWidth())  + (gaugeSize * 0.005635));
		double valueY = masseinheitY;
		
		gc.setFont(font);
		
		gc.setFill(Color.web("#282828"));
		gc.fillText(textFirstValue.getText(), valueX+2, valueY+2);
	
		gc.setFill(Color.WHITE);
		gc.fillText(textFirstValue.getText(), valueX, valueY);
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

	public void setMajorValue(SensorValue majorValue)
	{
		this.majorValue = majorValue;
		
	}
	public void startAnimation() 
	{
		if(animThread != null && animThread.isAlive())
			animThread.stop();
		
		isAnimation = true;
		int minValue = (int)majorValue.getVon();
		int maxValue = (int)majorValue.getBis();
		Runnable runnable = new Runnable(){

			@Override
			public void run() 
			{
				Random ran = new Random();
			
				while(isAnimation)
				{
					int neuerWert = ran.nextInt((maxValue - minValue) + 1);
					
					if(neuerWert != majorValue.getCurrentValue())
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
	
	/**
	 * Methode für das Sezten des aktuellen Zustands
	 * @param neuerWert
	 */
	public void setCurrentValue(double newPercentValue, boolean isInit) 
	{
		//wir haben 31 Ticks für die Beleuchtung
		
		//ermitteln wieviele ticks wir benötigen
		int showTickNumber =  (int) Math.round((double)37D/100d * (double)newPercentValue);
		int showTickNumberAktuell = (int) Math.round((double)37D/100d * (double)majorValue.getCurrentValue());
	
		double wertVonEinemTickInProzent = 100/37D;
		
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
					///this.currentValue = (int)Math.round(tempValue);
					majorValue.setCurrentValue(tempValue);
					this.drawTextValues(true);
					
					//TODO überlegen ob man ständig mitaktualisiert oder erst zum schluss
					//this.setRotation(currentValue);
					
					
					
					try 
					{
						TimeUnit.MILLISECONDS.sleep(25);
					} 
					catch (InterruptedException e) 
					{
					
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
					//this.currentValue = (int)Math.round(tempValue);
					majorValue.setCurrentValue(tempValue);
					this.drawTextValues(true);
					
				//	this.setRotation(currentValue);
					try 
					{
						TimeUnit.MILLISECONDS.sleep(25);
					} 
					catch (InterruptedException e) {
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
		majorValue.setCurrentValue(newPercentValue);
		//this.setRotation(majorValue.getCurrentValue());
		this.drawTextValues(true);
		
		
		
	
	
	}


}
