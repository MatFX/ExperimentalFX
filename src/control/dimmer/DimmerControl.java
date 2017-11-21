package control.dimmer;

import java.util.HashMap;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

public class DimmerControl extends Region
{
	public enum StopIndizes
	{
		GLANZ_RAND
		//Enums für die gespeicherten Farben
		, DREHRAD_GLANZ
		
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
	
	private Circle basis1, basis2, basis3, glanzRand, schattierungDrehrad, drehrad, drehradGlanz;
	
	private LinearGradient linearGradGlanzRand, linearGradDrehradGlanz;
	

	/**
	 * Farbmap weil die Werte unveränderlich sind und bei resize wieder bei den Gradienten neu gesetzt werden müssen.
	 */
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	
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
		/*basis1.setRadius(64);
		basis1.setCenterX(centerX);
		basis1.setCenterY(centerY);*/
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
		//Rest kommt später bei reisze
		
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
		
		
		
		this.getChildren().addAll(basis1, basis2, basis3, glanzRand /* TODO ticks */
				,schattierungDrehrad, drehrad, drehradGlanz);
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
		
		
			
	}
	
}
