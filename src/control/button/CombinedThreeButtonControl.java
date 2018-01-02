package control.button;

import java.util.HashMap;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class CombinedThreeButtonControl extends Region
{
	
	public enum StopIndizes
	{
		HORIZ_MAIN_GLANZ_OBEN, HORIZ_MAIN_GLANZ_UNTEN, OVERLAY_RECHTER_KNOPF, OVERLAY_LINKER_KNOPF;
	}
	
	private Rectangle horizontalHintergrund, horizSchwarz, horizMain, horizMainGlanzOben, horizMainGlanzUnten;
	
	//startsize war w: 130 h: 55
	private double w = 130, h = 55;
	
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	/**
	 * Zwei Buttons die neben dem kreis liegen
	 */
	private ButtonRegion rightButton, leftButton, overlayRightButton, overlayLeftButton;
	
	private Circle runderKnopfHintergrund;
	
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
				new Stop(0.0, Color.web("#33333300")),
				new Stop(0.0138623, Color.web("#31313104")),
				new Stop(0.2044517, Color.web("#1B1B1B34")),
				new Stop(0.4134517, Color.web("#0C0C0C69")),
				new Stop(0.6548164, Color.web("#030303A7")),
				new Stop(1.0, Color.web("#000000"))
			};
		stopMap.put(StopIndizes.HORIZ_MAIN_GLANZ_UNTEN, stopArray);
		
		rightButton = new ButtonRegion(new Rectangle(), new Rectangle());
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#1A1A1A")),
				new Stop(1.0, Color.web("#33333300"))
			};
		stopMap.put(StopIndizes.OVERLAY_RECHTER_KNOPF, stopArray);
		
		overlayRightButton = new ButtonRegion(new Rectangle(), new Rectangle());
		
		leftButton = new ButtonRegion(new Rectangle(), new Rectangle());
		overlayLeftButton = new ButtonRegion(new Rectangle(), new Rectangle());
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#1A1A1A")),
				new Stop(1.0, Color.web("#33333300"))
			};
		stopMap.put(StopIndizes.OVERLAY_LINKER_KNOPF, stopArray);
		
		
		runderKnopfHintergrund = new Circle();
		runderKnopfHintergrund.setFill(Color.web("#555555"));
		
		this.getChildren().addAll(horizontalHintergrund, horizSchwarz, horizMain, horizMainGlanzOben, horizMainGlanzUnten, 
				rightButton, overlayRightButton, leftButton, overlayLeftButton,
				runderKnopfHintergrund);
	}
	
	private void resize() 
	{
		w = this.getWidth();
		h = this.getHeight();
		
		//System.out.println("w " + w + " h " + h);
		
		
		
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
		
		
		//x1="65.0088577" y1="28.8131142" x2="65.7588577" y2="56.1881142"
		
		//x1 = 100/130 * 65.0088577 = 50,00681361538462 = 0.5000681361538462
		//y1 = 100/55 * 28.8131142 = 52,38748036363636 = 0.5238748036363636
		//x2 = 100/130 * 65.7588577 =  50,58373669230769 = 0.5058373669230769
		
		//y2 = 100/55 * 56.1881142 = 102,1602076363636 = 1.021602076363636
		
		
		lg = new LinearGradient(getWidth() *  0.5000681361538462, 
				getHeight() * 0.5238748036363636, 
				getWidth() * 0.5058373669230769,
				getHeight() *  1.021602076363636,
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.HORIZ_MAIN_GLANZ_UNTEN));
		
		horizMainGlanzUnten.setFill(lg);
		horizMainGlanzUnten.setX(w * 0.04982307692307693);
		horizMainGlanzUnten.setY(h * 0.28485454545454547);
		horizMainGlanzUnten.setWidth(w * 0.9769230769230769);
		horizMainGlanzUnten.setHeight(h * 0.5272727272727272);
		horizMainGlanzUnten.setArcWidth(w * 0.09234615384615387);
		horizMainGlanzUnten.setArcHeight(w * 0.09234615384615387);
		
		
		//7,4777 16,667 125 27 11,492
		double baseX = w * 0.05752076923076923;
		double baseY = h * 0.30303636363636366;
		double baseW = w * 0.9615384615384616;
		double baseH = h * 0.49090909090909085;
		double arc =w * 0.0884;
		PointXYWHARC basePoint = new PointXYWHARC(baseX, baseY, baseW, baseH, arc, arc);
		//basePoint.setPaint(Color.BLANCHEDALMOND);
		basePoint.setPaint(Color.web("#282828"));
		
		//overlay eigenes objekt wegen der anderen farbe
		PointXYWHARC overlayRightBasePoint = new PointXYWHARC(baseX, baseY, baseW, baseH, arc, arc);
		
		//x1="86.5155563" y1="23.886776" x2="120.7655563" y2="46.636776"
		//x1 = 100/130 * 86.5155563 = 0.6655042792307692 
		//y1 = 100/55 * 23.886776 = 0.4343050181818182
		//x2 = 100/130 * 120.7655563 = 0.9289658176923077
		//y2 = 100/55 * 46,636776 = 0.8479413818181818
		LinearGradient lgOverlayRechts = new LinearGradient(getWidth()*0.6655042792307692, 
				getHeight() * 0.4343050181818182, 
				getWidth() * 0.9289658176923077,
				getHeight() * 0.8479413818181818,
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.OVERLAY_RECHTER_KNOPF));
		overlayRightBasePoint.setPaint(lgOverlayRechts);
		
		
		//nun der abzug für die rechte seite
		double subtractX = w * 0.038461538461538464;
		double subtractY = h * 0.04545454545454545;
		double subtractW = w * 0.499823076923077;
		double subtractH = h * 1.0;
		double subctractArc = 0;
		PointXYWHARC subtractPoint = new PointXYWHARC(subtractX, subtractY, subtractW, subtractH, subctractArc, subctractArc);
		
		rightButton.setNewValues(basePoint, subtractPoint);
		rightButton.resize();
		
		
		
		overlayRightButton.setNewValues(overlayRightBasePoint, subtractPoint);
		overlayRightButton.resize();
		
		
		
		//beim linken werden die gleichen basepoints verwendet...das Beschneidungsrechteck ist aber anders
		subtractX = w * 0.5382846153846155;
		subtractY = h * 0.04545454545454545;
		subtractW = w * 0.5001769230769231;
		subtractH = h * 1.0;
		subctractArc = 0;
		
		//nochmals eines für die andere seite
		PointXYWHARC overlayLeftBasePoint = new PointXYWHARC(baseX, baseY, baseW, baseH, arc, arc);
		//x1="41.9207115" y1="16.8945789" x2="26.4207115" y2="45.3945808"
		//x1  100/130 * 41.9207115 = 0.3224670115384615
		//y1  100/55 * 16.8945789 =  0.3071741618181818
		//x2 100/130 * 26.4207115 = 0.2032362423076923
		//y2 100/130 * 45.3945808 = 34,91890830769231 = 0.3491890830769231
		LinearGradient lgOverlayLinks = new LinearGradient(getWidth()*0.3224670115384615, 
				getHeight() *0.3071741618181818, 
				getWidth() *  0.2032362423076923,
				getHeight() * 0.3491890830769231,
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.OVERLAY_LINKER_KNOPF));
		overlayLeftBasePoint.setPaint(lgOverlayLinks);
		
		
		subtractPoint = new PointXYWHARC(subtractX, subtractY, subtractW, subtractH, subctractArc, subctractArc);
		leftButton.setNewValues(basePoint, subtractPoint);
		leftButton.resize();

		overlayLeftButton.setNewValues(overlayLeftBasePoint, subtractPoint);
		overlayLeftButton.resize();
		
		
		
		
		//cx="64.9774475" cy="27.666666" r="27"/>
		
		//cx 100/130 * 64.9774475 = 0.4998265192307692
		//cy 100/55 * 27.666666 = 0.5030302909090909
		//r  100/130 * 27 = 20,76923076923077 = 0.2076923076923077
		
		//Feststellen was sich verändert hat?
		//brauch ich den Vergleich?
				double size  = getWidth() < getHeight() ? getWidth() : getHeight();
		//		//double radius = size / 2d;
		System.out.println("size " + size);
		
		
		//runderKnopfHintergrund.setCenterX(getWidth() * 0.4998265192307692);
		///runderKnopfHintergrund.setCenterY(getHeight() * 0.5030302909090909);
		runderKnopfHintergrund.setRadius(getWidth() * 0.2076923076923077);
		
		//Über den horizontalen Hintergrund den Mittelpunkt für den Kreis ermitteln
		double centerX = horizontalHintergrund.getWidth() / 2d + horizontalHintergrund.getX();
		double centerY = horizontalHintergrund.getHeight() / 2d + horizontalHintergrund.getY();
		System.out.println("mittelpunkt " + centerX + " " + centerY);
		runderKnopfHintergrund.setCenterX(centerX);
		runderKnopfHintergrund.setCenterY(centerY);
		
		
		
		
		
		
		
	}

}
