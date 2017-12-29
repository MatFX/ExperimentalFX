package control.button;

import java.util.HashMap;

import control.universaldisplay.UniversalDisplay.StopIndizes;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class CombinedThreeButtonControl extends Region
{
	
	public enum StopIndizes
	{
		HORIZ_MAIN_GLANZ_OBEN, HORIZ_MAIN_GLANZ_UNTEN;
	}
	
	private Rectangle horizontalHintergrund, horizSchwarz, horizMain, horizMainGlanzOben, horizMainGlanzUnten;
	
	//startsize war w: 130 h: 55
	private double w = 130, h = 55;
	
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	
	public CombinedThreeButtonControl()
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
		
		horizontalHintergrund = new Rectangle();
		horizontalHintergrund.setFill(Color.web("#555555"));
		
		horizSchwarz = new Rectangle();
		horizSchwarz.setFill(Color.BLACK);
		
		horizMain = new Rectangle();
		horizMain.setFill(Color.web("#212020"));
		
		horizMainGlanzOben = new Rectangle();
		Stop[] stopArray = new Stop[]{
				new Stop(0.0, Color.web("#FFFFFFCC")),
				new Stop(0.358507, Color.web("#ABABAB83")),
				new Stop(0.664289, Color.web("#6A6A6A44")),
				new Stop(0.8855522, Color.web("#42424217")),
				new Stop(1.0, Color.web("#33333300"))
			};
		stopMap.put(StopIndizes.HORIZ_MAIN_GLANZ_OBEN, stopArray);
		
		horizMainGlanzUnten = new Rectangle();
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#000000CC")),
				new Stop(1.0, Color.web("#33333300"))
			};
		stopMap.put(StopIndizes.HORIZ_MAIN_GLANZ_UNTEN, stopArray);
		
		
		
		this.getChildren().addAll(horizontalHintergrund, horizSchwarz, horizMain, horizMainGlanzOben);
	}
	
	private void resize() 
	{
		w = this.getWidth();
		h = this.getHeight();
		
		System.out.println("w " + w + " h " + h);
		
		
		
		//x = 100/130 * 5.4777 = 4,213615384615385 = 0.04213615384615385
		//y = 100/55 * 14.667 = 26,66727272727273 = 0.2666727272727273
		//w = 100/130 * 129 = 99,23076923076923 = 0.9923076923076923
		//h = 100/55 * 31 = 	56,36363636363636 = 0.5636363636363636
		//arcW = 100/130 * 12.509 = 9,622307692307692 = 0.09622307692307692
		//arcH = 100/55 * 12.509 = 22,74363636363636 = 0.2274363636363636
		horizontalHintergrund.setX(w * 0.04213615384615385);
		horizontalHintergrund.setY(h * 0.2666727272727273);
		horizontalHintergrund.setWidth(w *  0.9923076923076923);
		horizontalHintergrund.setHeight(h * 0.5636363636363636);
		horizontalHintergrund.setArcWidth(w * 0.09622307692307692);
		horizontalHintergrund.setArcHeight(w * 0.09622307692307692);
		
		horizSchwarz.setX(w * 0.04598230769230769);
		horizSchwarz.setY(h * 0.2757636363636363);
		horizSchwarz.setWidth(w * 0.9846153846153847);
		horizSchwarz.setHeight(h * 0.5454545454545454);
		horizSchwarz.setArcWidth(w * 0.09429230769230768);
		horizSchwarz.setArcHeight(w * 0.09429230769230768);
		
		horizMain.setX(w * 0.04982307692307693);
		horizMain.setY(h * 0.28485454545454547);
		horizMain.setWidth(w * 0.9769230769230769);
		horizMain.setHeight(h * 0.5272727272727272);
		horizMain.setArcWidth(w * 0.09234615384615387);
		horizMain.setArcHeight(w * 0.09234615384615387);
		
		horizMainGlanzOben.setX(w * 0.04982307692307693);
		horizMainGlanzOben.setY(h * 0.28485454545454547);
		horizMainGlanzOben.setWidth(w * 0.9769230769230769);
		horizMainGlanzOben.setHeight(h * 0.5272727272727272);
		horizMainGlanzOben.setArcWidth(w * 0.09234615384615387);
		horizMainGlanzOben.setArcHeight(w * 0.09234615384615387);
		
		//x1="64.9774475" y1="0.3333333" x2="64.9774475" y2="31.3405018"
		
		//x1 = 100/130 * 64.9774475 = 0,4998265192307692
		//y1 = 100/55 *  0.3333333 = 0,6060605454545455 = 0.006060605454545455
		//x2 = 100/130 * 64.9774475 = 0,4998265192307692
		//y2 = 100/55 * 31.3405018 = 56,98273054545455 = 0.5698273054545455
		
		LinearGradient lg = new LinearGradient(getWidth()*0.4998265192307692, 
				getHeight() * 0.006060605454545455, 
				getWidth() * 0.4998265192307692,
				getHeight() * 0.5698273054545455,
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.HORIZ_MAIN_GLANZ_OBEN));
		
		horizMainGlanzOben.setFill(lg);
		
		
		//x1="65.0225525" y1="0" x2="65.0225525" y2="31.0071678
		//x1 = 100/130 * 65.0225525 = 50,01734807692308 = 0.5001734807692308
		//y1 = 0
		//x2 = 100/130 * 65.0225525 = 50,01734807692308 = 0.5001734807692308
		//y2 = 100/55 * 31.0071678 = 56,37666872727273 = 0.5637666872727273
		
		lg = new LinearGradient(getWidth() * 0.5001734807692308, 
				0, 
				getWidth() * 0.5001734807692308,
				getHeight() *  0.5637666872727273,
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.HORIZ_MAIN_GLANZ_OBEN));
		
		horizMainGlanzUnten.setFill(lg);
		
		
		
		
		
		
	}

}
