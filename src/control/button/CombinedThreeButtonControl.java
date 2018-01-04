package control.button;

import java.util.HashMap;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class CombinedThreeButtonControl extends Region
{
	
	public enum Command
	{
		LEFT_BUTTON, MIDDLE_BUTTON, RIGHT_BUTTON,
		/**
		 * der wird dann von außerhalb gesetzt, damit auch das aktuelle Kommando nochmal gesetzt werden kann.
		 * <br>you need the reset as a "acknowledge" from outside.
		 */
		RESET_COMMAND;
	}
	
	public enum StopIndizes
	{
		HORIZ_MAIN_GLANZ_OBEN, HORIZ_MAIN_GLANZ_UNTEN, OVERLAY_RECHTER_KNOPF, OVERLAY_LINKER_KNOPF, KNOPF_GLANZ_OBEN, KNOPF_GLANZ_UNTEN, KNOPF_SCHATTEN_INNEN;
	}
	
	private Rectangle horizontalHintergrund, horizSchwarz, horizMain, horizMainGlanzOben, horizMainGlanzUnten;
	
	//startsize war w: 130 h: 55
	private double w = 130, h = 55;
	
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	/**
	 * Zwei Buttons die neben dem kreis liegen
	 */
	private ButtonRegion rightButton, leftButton, overlayRightButton, overlayLeftButton;
	
	private Circle runderKnopfHintergrund, runderKnopfSchwarz, runderKnopfMain, rundKnopfGlanzOben,
		rundKnopfGlanzUnten, runderKnopfSchaltflaeche, runderKnopfSchattenInnen;
	
	private final double HINTERGRUNDFLAECHE = 129D * 31D;
	
	private final double HINTERGRUNDFLAECHE_SCHWARZ = 128D * 30D;

	private final double HINTERGRUNDFLAECHE_HAUPT = 127D * 29D;
	
	private final double KREIS_HINTERGRUND_FLAECHE = Math.PI * (Math.pow(27, 2));
	
	private final double KREIS_HINTERGRUND_SCHWARZ = Math.PI * (Math.pow(26.5, 2));
	
	//auch für den zwei glanz kreisen zu verwenden
	private final double KREIS_HAUPT =  Math.PI * (Math.pow(26, 2));
	
	private final double KREIS_SCHALTFLAECHE = Math.PI * (Math.pow(25, 2));
	
	private InnerShadow innerShadow;
	/**
	 * für den Text wenn der button gedrückt wurde
	 */
	private Glow textGlow = new Glow(0.3);
	
	/**
	 * Kammdos können hier empfangen werden (listener anschluss)
	 */
	private SimpleObjectProperty<Command> commandProperty = new SimpleObjectProperty<Command>();
	
	private DropShadow dropShadow;
	
	
	
	public CombinedThreeButtonControl()
	{
		this.initGraphics();
		this.registerListener();
	}

	private void registerListener() 
	{
		widthProperty().addListener(observable -> resize(true));
		heightProperty().addListener(observable -> resize(false));
		
		//TODO text fehlt hier komplett im Moment
		rightButton.setOnMousePressed(e -> setNodePressed(rightButton, new Text(), Command.RIGHT_BUTTON, e));
		rightButton.setOnMouseReleased(e -> setNodeReleased(rightButton, new Text(), e));
		
		
		runderKnopfSchaltflaeche.setOnMousePressed(e -> setNodePressed(runderKnopfSchaltflaeche, new Text(), Command.MIDDLE_BUTTON, e));
		runderKnopfSchaltflaeche.setOnMouseReleased(e -> setNodeReleased(runderKnopfSchaltflaeche, new Text(), e));
		
		leftButton.setOnMousePressed(e -> setNodePressed(leftButton, new Text(), Command.LEFT_BUTTON, e));
		leftButton.setOnMouseReleased(e -> setNodeReleased(leftButton, new Text(), e));
	}

	private void initGraphics() 
	{
		dropShadow = new DropShadow();
		dropShadow.setColor(Color.web("000000A0")); 
		
		innerShadow = new InnerShadow();
		innerShadow.setBlurType(BlurType.GAUSSIAN);
		innerShadow.setColor(Color.web("#000000A0"));
		        
		
		
		
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
		overlayRightButton.setMouseTransparent(true);
		
		leftButton = new ButtonRegion(new Rectangle(), new Rectangle());
		overlayLeftButton = new ButtonRegion(new Rectangle(), new Rectangle());
		overlayLeftButton.setMouseTransparent(true);
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#1A1A1A")),
				new Stop(1.0, Color.web("#33333300"))
			};
		stopMap.put(StopIndizes.OVERLAY_LINKER_KNOPF, stopArray);
		
		
		runderKnopfHintergrund = new Circle();
		runderKnopfHintergrund.setFill(Color.web("#555555"));
		
		runderKnopfSchwarz = new Circle();
		runderKnopfSchwarz.setFill(Color.BLACK);
		
		runderKnopfMain = new Circle();
		runderKnopfMain.setFill(Color.web("#212020"));
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#FFFFFFCC")),
				new Stop(0.0023868, Color.web("#FEFEFECB")),
				new Stop(0.1384432, Color.web("#C9C9C9AE")),
				new Stop(0.2760799, Color.web("#9B9B9B90")),
				new Stop(0.4127651, Color.web("#75757573")),
				new Stop(0.5482611, Color.web("#58585855")),
				new Stop(0.6823206, Color.web("#44444438")),
				new Stop(0.8141946, Color.web("#3737371C")),
				new Stop(0.9414634, Color.web("#33333300"))
			};
		stopMap.put(StopIndizes.KNOPF_GLANZ_OBEN, stopArray);
		
		rundKnopfGlanzOben = new Circle();
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#33333300")),
				new Stop(0.0138623, Color.web("#31313104")),
				new Stop(0.2044517, Color.web("#1B1B1B34")),
				new Stop(0.4134517, Color.web("#0C0C0C69")),
				new Stop(0.6548164, Color.web("#030303A7")),
				new Stop(1.0, Color.web("#000000"))
			};
		stopMap.put(StopIndizes.KNOPF_GLANZ_UNTEN, stopArray);	
		
		rundKnopfGlanzUnten = new Circle();
		
		runderKnopfSchaltflaeche = new Circle();
		runderKnopfSchaltflaeche.setFill(Color.web("#282828"));
		
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#000000")),
				new Stop(1.0, Color.web("#33333300"))
			};
		stopMap.put(StopIndizes.KNOPF_SCHATTEN_INNEN, stopArray);	
		
		runderKnopfSchattenInnen = new Circle();
		runderKnopfSchattenInnen.setMouseTransparent(true);
		
		this.getChildren().addAll(horizontalHintergrund, horizSchwarz, horizMain, horizMainGlanzOben, horizMainGlanzUnten, 
				rightButton, overlayRightButton, leftButton, overlayLeftButton,
				runderKnopfHintergrund, runderKnopfSchwarz, runderKnopfMain, rundKnopfGlanzOben, 
				rundKnopfGlanzUnten, runderKnopfSchaltflaeche, runderKnopfSchattenInnen);
	}
	
	private void resize(boolean changedWidth) 
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
		
		//Über den horizontalen Hintergrund den Mittelpunkt für den Kreis ermitteln
		double centerX = horizontalHintergrund.getWidth() / 2d + horizontalHintergrund.getX();
		double centerY = horizontalHintergrund.getHeight() / 2d + horizontalHintergrund.getY();
		
		double flaecheRechteck = horizontalHintergrund.getWidth() * horizontalHintergrund.getHeight();
		double verhaeltnis = HINTERGRUNDFLAECHE / KREIS_HINTERGRUND_FLAECHE;
		double flaecheKreis = flaecheRechteck / verhaeltnis;
		double radiusKnopf = Math.sqrt(flaecheKreis / Math.PI);
		
		runderKnopfHintergrund.setCenterX(centerX);
		runderKnopfHintergrund.setCenterY(centerY);
		runderKnopfHintergrund.setRadius(radiusKnopf);
	
		
		flaecheRechteck = horizSchwarz.getWidth() * horizSchwarz.getHeight();
		verhaeltnis = HINTERGRUNDFLAECHE_SCHWARZ / KREIS_HINTERGRUND_SCHWARZ;
		flaecheKreis = flaecheRechteck / verhaeltnis;
		radiusKnopf = Math.sqrt(flaecheKreis / Math.PI);
		
		runderKnopfSchwarz.setCenterX(centerX);
		runderKnopfSchwarz.setCenterY(centerY);
		runderKnopfSchwarz.setRadius(radiusKnopf);
		
		
		flaecheRechteck = horizMain.getWidth() * horizMain.getHeight();
		verhaeltnis = HINTERGRUNDFLAECHE_HAUPT / KREIS_HAUPT;
		flaecheKreis = flaecheRechteck / verhaeltnis;
		radiusKnopf = Math.sqrt(flaecheKreis / Math.PI);
		
		runderKnopfMain.setCenterX(centerX);
		runderKnopfMain.setCenterY(centerY);
		runderKnopfMain.setRadius(radiusKnopf);
		
		//glanz oben und unten 
		//x1="64.9774475" y1="-4.5833335" x2="64.9774475" y2="49.9309998"
		
		//x1 = 100/130 * 64.9774475 = 0.4998265192307692
		//y2 = 100/55 * -4.5833335 = 8,333333636363636 = 0.08333333636363636
		//x2 = 100/130 * 64.9774475  = 0.4998265192307692
		//y2 = 100/55 * 49.9309998 = 90,783636 = 0.90783636
		lg = new LinearGradient(getWidth()* 0.4998265192307692, 
				getHeight() * 0.08333333636363636 * -1, 
				getWidth() * 0.4998265192307692,
				getHeight() * 0.90783636,
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.KNOPF_GLANZ_OBEN));
		
		rundKnopfGlanzOben.setCenterX(centerX);
		rundKnopfGlanzOben.setCenterY(centerY);
		rundKnopfGlanzOben.setRadius(radiusKnopf);
		rundKnopfGlanzOben.setFill(lg);
		
		
		//x1="64.9774475" y1="32.875" x2="64.9774475" y2="56.1303749"
		//x1 = 100/130 * 64.9774475 = 0.4998265192307692
		//y1 = 100/55 * 32.875 = 59,77272727272727 = 0.5977272727272727
		//x2 = 100/130 * 64.9774475 = 0.4998265192307692
		//y1 = 100/55 * 56.1303749 = 102,0552270909091 = 1.020552270909091
		lg = new LinearGradient(getWidth()* 0.4998265192307692, 
				getHeight() * 0.5977272727272727, 
				getWidth() * 0.4998265192307692,
				getHeight() * 1.020552270909091,
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.KNOPF_GLANZ_UNTEN));
		
		rundKnopfGlanzUnten.setCenterX(centerX);
		rundKnopfGlanzUnten.setCenterY(centerY);
		rundKnopfGlanzUnten.setRadius(radiusKnopf);
		rundKnopfGlanzUnten.setFill(lg);
		
		flaecheRechteck = horizMain.getWidth() * horizMain.getHeight();
		verhaeltnis = HINTERGRUNDFLAECHE_HAUPT / KREIS_SCHALTFLAECHE;
		flaecheKreis = flaecheRechteck / verhaeltnis;
		radiusKnopf = Math.sqrt(flaecheKreis / Math.PI);
		
		runderKnopfSchaltflaeche.setCenterX(centerX);
		runderKnopfSchaltflaeche.setCenterY(centerY);
		runderKnopfSchaltflaeche.setRadius(radiusKnopf);
	
		//x1="83.5855408" y1="-5.884028" x2="61.5855408" y2="33.7826385"
		//x1 100/130 * 83.5855408 = 64,29656984615385 => 0.6429656984615385
		//y1 100/55 * -5.884028 = -0.1069823272727273
		//x2 =  100/130 * 61.5855408 = 47,37349292307692 => 0.4737349292307692
		//y2 = 100/55 * 33.7826385 = 61,42297909090909 = 0.6142297909090909
		lg = new LinearGradient(getWidth()* 0.6429656984615385, 
				getHeight() * 0.1069823272727273 *-1, 
				getWidth() * 0.4737349292307692,
				getHeight() * 0.6142297909090909,
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.KNOPF_SCHATTEN_INNEN));
		runderKnopfSchattenInnen.setCenterX(centerX);
		runderKnopfSchattenInnen.setCenterY(centerY);
		runderKnopfSchattenInnen.setRadius(radiusKnopf);
		runderKnopfSchattenInnen.setFill(lg);
		
	}
	
	private void setNodePressed(Node nodeBase , Text textNode, Command command, MouseEvent e) 
	{
		nodeBase.setEffect(innerShadow);
		textNode.setEffect(textGlow);
		commandProperty.set(command);
		e.consume();
		
		
	
	}

	private void setNodeReleased(Node nodeBase, Text textNode, MouseEvent e) 
	{
		nodeBase.setEffect(dropShadow);
		textNode.setEffect(null);
		e.consume();
		
	}

	public SimpleObjectProperty<Command> getCommandProperty()
	{
		return commandProperty;
	}
}
