package gauge.amr;

import java.util.HashMap;

import control.universaldisplay.UniversalDisplay.StopIndizes;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Illustrator Abmaße 128*128 und centerX/Y 64/64
 * @author m.goerlich
 *
 */
public class AMRGauge extends Region
{
	private double centerX = 64;
	private double centerY = 64;
	
	//evtl. hintergrund nicht benötigt
	private Circle hintergrund;
	
	private Circle rahmen_hintergrundfarbe;
	
	private Circle rahmen_glanz;
	
	private Circle basis_farbe;
	
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	//Enums für die gespeicherten Farben
	public enum StopIndizes
	{
		//für die hintergründe
		RAHMEN_GLANZ,

		
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
		
	
		this.getChildren().addAll(hintergrund, rahmen_hintergrundfarbe, rahmen_glanz, basis_farbe);
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
		
		
		
		
		
	}


}
