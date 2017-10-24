package buttonDisplay;

import java.util.HashMap;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Affine;

/**
 * Eine Anzeige mit einer Schaltflaeche die einen toggle ermöglicht
 * <br>Die Ausdehnung des Kreises lag bei Erstellung bei 71px * 71px; Anfang Radius 35.5; Das Komplette SVG war bei 72x72
 * @author m.goerlich
 *
 */
public class ButtonDisplay extends Region
{
	
	public enum StopIndizes
	{
		COMPONENT_BASIS_RAHMEN,
		
		COMPONENT_BASIS_GLANZ,
		
		SCHALTFLAECHE_BASIS,
		
		SCHALTFLAECHE_GLANZ,
		
		GLANZ_LCD_OBEN,
		GLANZ_LCD_LINKS,
		GLANZ_LCD_RECHTS, DIODE_BASIS_GLANZ, DIODE_FARBE_RADIALGRADIENT, DIODE_FARBE_SCHEIN;
		
		
		
	}
	
	
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	private Circle componentBasis, componentBasisRahmen, componentBasisGlanz, componentBasisInlay, componentCircle;
	
	private RadialGradient rgComponentBasisRahmen, rgGlanzDiode, rgFarbeSchein;
	
	private LinearGradient lgComponentBasisGlanz, lgSchaltflaecheBasis, lgSchaltflaecheGlanz,
		lgLCDOben, lgLCDLinks, lgLCDRechts, lgDiodeBasisGlanz;
	
	private double centerX = 36, centerY = 36;
	
	private double width = 72, height = 72;

	private Arc backgroundHalf, backgroundHalfGlanz, backgroundLCDBasis, overlayLCDOben,
		overlayLCDLinks, overlayLCDRechts;
	
	private Circle diodeUntergrund, diodeBasisGlanz;
	
	/**
	 * Die soll später mal austauschbar sein.
	 */
	private Circle diodeFarbe;
	
	private Circle glanzDiode, scheinDiode;
	
	//private SVGPath rahmenOben;
	
	public ButtonDisplay()
	{
		super();
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
		
		double radius = width / 2;
		
		componentBasis = new Circle();
		componentBasis.setRadius(35.5);
		componentBasis.setCenterX(centerX);
		componentBasis.setCenterY(centerY);
		componentBasis.setFill(Color.web("#292828"));
		
		
		
		Stop[] stopArray = new Stop[]{
				new Stop(0.6054054, Color.web("#ffffff")),
				new Stop(0.7524482, Color.web("#fdfdfd")),
				new Stop(0.8054233, Color.web("#f6f6f6")),
				new Stop(0.8431793, Color.web("#ebebeb")),
				new Stop(0.8737268, Color.web("#dadada")),
				new Stop(0.8998759, Color.web("#c4c4c4")),
				new Stop(0.9230438, Color.web("#a8a8a8")),
				new Stop(0.9440075, Color.web("#888888")),
				new Stop(0.9632673, Color.web("#626262")),
				new Stop(0.9811872, Color.web("#373737")),
				new Stop(0.9970878, Color.web("#090909")),
				new Stop(1, Color.web("#000000"))
		};
		//Ablage für das spätere erneute setzen über resize
		stopMap.put(StopIndizes.COMPONENT_BASIS_RAHMEN, stopArray);

		rgComponentBasisRahmen = new RadialGradient(0D, 0D, centerX, centerY, 35.5, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.COMPONENT_BASIS_RAHMEN));
		
		componentBasisRahmen = new Circle();
		componentBasisRahmen.setRadius(35.5);
		componentBasisRahmen.setCenterX(centerX);
		componentBasisRahmen.setCenterY(centerY);
		componentBasisRahmen.setFill(rgComponentBasisRahmen);
		
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#333333B3")),
				new Stop(0.1837838, Color.web("#FFFFFF66")),
				new Stop(0.190562, Color.web("#E0E0E06B")),
				new Stop(0.2006963, Color.web("#B7B7B771")),
				new Stop(0.2118152, Color.web("#93939379")),
				new Stop(0.2237999, Color.web("#75757581")),
				new Stop(0.2369228, Color.web("#5D5D5D8A")),
				new Stop(0.2516647, Color.web("#4A4A4A94")),
				new Stop(0.2689193, Color.web("#3D3D3D9F")),
				new Stop(0.2910578, Color.web("#353535AE")),
				new Stop(0.3351351, Color.web("#333333CC")),
				new Stop(0.5, Color.web("#B3B3B366")),
				new Stop(0.520902, Color.web("#9999996B")),
				new Stop(0.548638, Color.web("#7D7D7D72")),
				new Stop(0.5787512, Color.web("#68686879")),
				new Stop(0.6119624, Color.web("#59595981")),
				new Stop(0.6506904, Color.web("#5050508B")),
				new Stop(0.7081081, Color.web("#4D4D4D99")),
				new Stop(1.0, Color.web("#CCCCCC4D"))
		};
		stopMap.put(StopIndizes.COMPONENT_BASIS_GLANZ, stopArray);
		
	
		lgComponentBasisGlanz = new LinearGradient(centerX - radius, centerY, (centerX - radius) + width, centerY, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.COMPONENT_BASIS_GLANZ));
		
		
		componentBasisGlanz = new Circle();
		componentBasisGlanz.setRadius(35.5);
		componentBasisGlanz.setCenterX(centerX);
		componentBasisGlanz.setCenterY(centerY);
		componentBasisGlanz.setFill(lgComponentBasisGlanz);
		
		componentBasisInlay = new Circle();
		//1.5 kleiner als normal
		componentBasisInlay.setRadius(34);
		componentBasisInlay.setCenterX(centerX);
		componentBasisInlay.setCenterY(centerY);
		componentBasisInlay.setFill(Color.web("#0C0C0C"));
		
		//schaltflaeche
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#FFFFFF")),
				new Stop(0.0628211, Color.web("#CBCBCB")),
				new Stop(0.128356, Color.web("#9D9D9D")),
				new Stop(0.1941585, Color.web("#777777")),
				new Stop(0.2590258, Color.web("#595959")),
				new Stop(0.322758, Color.web("#444444")),
				new Stop(0.3847882, Color.web("#373737")),
				new Stop(0.4432432, Color.web("#333333")),
				new Stop(0.5015526, Color.web("#393939")),
				new Stop(0.5828459, Color.web("#4C4C4C")),
				new Stop(0.6777068, Color.web("#696969")),
				new Stop(0.7828168, Color.web("#939393")),
				new Stop(0.8948977, Color.web("#C7C7C7")),
				new Stop(1.0, Color.web("#FFFFFF"))
			};
		stopMap.put(StopIndizes.SCHALTFLAECHE_BASIS, stopArray);
		
		lgSchaltflaecheBasis = new LinearGradient(2.5175171,53.0959778, 69.4824829, 53.0959778, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.SCHALTFLAECHE_BASIS));
		
		
		backgroundHalf = new Arc();
		backgroundHalf.setCenterX(centerX);
		//3 px Abstand zu der gruenen Flaeche 100/128 * 3 = 2.34375 = 0.0234375
		//2 px 1,5625 = 0.015635
		double gaugeSize = 72;
		backgroundHalf.setCenterY(centerY + (gaugeSize * 0.015635));
		
		//r = 60 = 100/64 * 60 = 93,75 = 0.9375
		// r = 56 = 100/ 64 * 56 = 87,5 = 0.875
		// r = 58 = 100/64 * 58 = 90,625 = 0.90625
		double radius2 = gaugeSize / 2;
		backgroundHalf.setRadiusX(radius2 *  0.90625);
		//hier wird noch die Zugabe von Y wieder abgezogen
		backgroundHalf.setRadiusY(radius2 * 0.90625 - (gaugeSize * 0.015635));
		backgroundHalf.setStartAngle(180f);
		backgroundHalf.setLength(180.0f);
		backgroundHalf.setType(ArcType.ROUND);
		//backgroundHalf.setFill(Color.RED);
		backgroundHalf.setFill(lgSchaltflaecheBasis);
		
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#B3B3B399")),
				new Stop(0.1312303, Color.web("#BABABA8F")),
				new Stop(0.3264174, Color.web("#CECECE80")),
				new Stop(0.5603397, Color.web("#EFEFEF6D")),
				new Stop(0.6567568, Color.web("#FFFFFF66")),
				new Stop(0.7233605, Color.web("#FCFCFC57")),
				new Stop(0.7728564, Color.web("#F3F3F34C")),
				new Stop(0.8168375, Color.web("#E2E2E242")),
				new Stop(0.8575974, Color.web("#CCCCCC39")),
				new Stop(0.8961684, Color.web("#AEAEAE31")),
				new Stop(0.9330684, Color.web("#8A8A8A28")),
				new Stop(0.9679725, Color.web("#61616121")),
				new Stop(1.0, Color.web("#3333331A"))
			};
		
		stopMap.put(StopIndizes.SCHALTFLAECHE_GLANZ, stopArray);
		
		//x1="2.5175171" y1="53.0959778" x2="68.9649658" y2="53.0959778">
		lgSchaltflaecheGlanz = new LinearGradient(2.5175171,53.0959778, 68.9649658, 53.0959778, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.SCHALTFLAECHE_GLANZ));
		
		backgroundHalfGlanz = new Arc();
		backgroundHalfGlanz.setCenterX(centerX);
		
		backgroundHalfGlanz.setCenterY(centerY + (gaugeSize * 0.015635));
		
		//r = 60 = 100/64 * 60 = 93,75 = 0.9375
		// r = 56 = 100/ 64 * 56 = 87,5 = 0.875
		// r = 58 = 100/64 * 58 = 90,625 = 0.90625
		backgroundHalfGlanz.setRadiusX(radius2 *  0.90625);
		//hier wird noch die Zugabe von Y wieder abgezogen
		backgroundHalfGlanz.setRadiusY(radius2 * 0.90625 - (gaugeSize * 0.015635));
		backgroundHalfGlanz.setStartAngle(180f);
		backgroundHalfGlanz.setLength(180.0f);
		backgroundHalfGlanz.setType(ArcType.ROUND);
		backgroundHalfGlanz.setFill(lgSchaltflaecheGlanz);
		
		//als nächstes das LCD Display
	
		
		backgroundLCDBasis = new Arc();
		backgroundLCDBasis.setFill(Color.web("#FF0000"));
		backgroundLCDBasis.setCenterX(centerX);
		//3 px Abstand zu der gruenen Flaeche 100/128 * 3 = 2.34375 = 0.0234375
		//2 px 1,5625 = 0.015635
		
		backgroundLCDBasis.setCenterY(centerY + (gaugeSize * 0.015635));
		
		//r = 60 = 100/64 * 60 = 93,75 = 0.9375
		// r = 56 = 100/ 64 * 56 = 87,5 = 0.875
		// r = 58 = 100/64 * 58 = 90,625 = 0.90625
	
		backgroundLCDBasis.setRadiusX(radius2 *  0.90625);
		//hier wird noch die Zugabe von Y wieder abgezogen
		backgroundLCDBasis.setRadiusY(radius2 * 0.90625 - (gaugeSize * 0.015635));
		backgroundLCDBasis.setStartAngle(0f);
		backgroundLCDBasis.setLength(180.0f);
		backgroundLCDBasis.setType(ArcType.ROUND);
		
		
		
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#FFFFFF")),
				new Stop(0.1404483, Color.web("#F9F9F8DB")),
				new Stop(0.3395385, Color.web("#E8EAE6A8")),
				new Stop(0.5735973, Color.web("#CDD0C76D")),
				new Stop(0.8321987, Color.web("#A7AC9D2B")),
				new Stop(1.0, Color.web("8B917E00"))
			};
		stopMap.put(StopIndizes.GLANZ_LCD_OBEN, stopArray);
		
		//x1="36" y1="-2.9166667" x2="36" y2="13.666667"
		//y1 = centerY - (-2.9166667) = 38,9166667 = 100/72 * 38,9166667 = 0,5405092638888889
		//y2 = 13.666667; centerY - 13.66667 =  22,33333 = 100/72 * 22,33333 = 0,3101851388888889
		lgLCDOben = new LinearGradient(centerX, centerY - (gaugeSize * 0.5405092638888889) ,centerX, 
				centerY - (gaugeSize * 0.3101851388888889), false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.GLANZ_LCD_OBEN));
		
		
		overlayLCDOben = new Arc();
		overlayLCDOben.setCenterX(centerX);
		//3 px Abstand zu der gruenen Flaeche 100/128 * 3 = 2.34375 = 0.0234375
		
		overlayLCDOben.setCenterY(centerY - (gaugeSize * 0.015635));
		overlayLCDOben.setRadiusX(radius2 *  0.90625);
		//hier wird noch die Zugabe von Y wieder abgezogen
		overlayLCDOben.setRadiusY(radius2 * 0.90625 - (gaugeSize * 0.015635));
		overlayLCDOben.setStartAngle(45f);
		overlayLCDOben.setLength(90.0f);
		overlayLCDOben.setType(ArcType.ROUND);
		overlayLCDOben.setFill(lgLCDOben);
		
		
		
		stopArray = new Stop[]
		{
				new Stop(0.0, Color.web("#FFFFFF")),
				new Stop(0.2058825, Color.web("#ECEEEACA")),
				new Stop(0.6300251, Color.web("#BAC4B35E")),
				new Stop(1.0, Color.web("8B9B7E00"))
		};
		
		stopMap.put(StopIndizes.GLANZ_LCD_LINKS, stopArray);
	
		//x1="4.6328111" y1="13.7291164" x2="17.5494785" y2="17.7291164">
		//x1 = centerX - 4.6328111 = 31,36718887 Strecke; 100/72 * 31,36718887 = 0.4356554009722222
		//y1 = centerY - 13.7291164 = 22,2708836 Strecke; 100/72 * 22,2708836 = 0.3093178277777778
		///X2 = centerX - 17.5494785 = 18,4505215 Strecke; 100/72 * 18,4505215 = 0.2562572430555556
		//y2 = centerY - 17.7291164 = 18,2708836 Strecke; 100/72 * 18,2708836 = 0.2537622722222222
		lgLCDLinks = new LinearGradient(centerX - (gaugeSize * 0.4356554009722222), 
				centerY - (gaugeSize * 0.3093178277777778),
				centerX - (gaugeSize *  0.2562572430555556), 
				centerY - (gaugeSize * 0.2537622722222222), 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.GLANZ_LCD_OBEN));
		
		
		overlayLCDLinks = new Arc();
		overlayLCDLinks.setCenterX(centerX);
		//3 px Abstand zu der gruenen Flaeche 100/128 * 3 = 2.34375 = 0.0234375
		
		overlayLCDLinks.setCenterY(centerY - (gaugeSize * 0.015635));
		overlayLCDLinks.setRadiusX(radius2 *  0.90625);
		//hier wird noch die Zugabe von Y wieder abgezogen
		overlayLCDLinks.setRadiusY(radius2 * 0.90625 - (gaugeSize * 0.015635));
		overlayLCDLinks.setStartAngle(135f);
		overlayLCDLinks.setLength(90.0f);
		overlayLCDLinks.setType(ArcType.ROUND);
		overlayLCDLinks.setFill(lgLCDLinks);
		
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#FFFFFF00")),
				new Stop(0.2393466, Color.web("#FCFDFC3D")),
				new Stop(0.4112191, Color.web("#F4F5F369")),
				new Stop(0.5620199, Color.web("#E6E7E38F")),
				new Stop(0.7006531, Color.web("#D2D4CDB3")),
				new Stop(0.830981, Color.web("#B8BCB0D4")),
				new Stop(0.9534147, Color.web("#999E8DF3")),
				new Stop(1.0, Color.web("#8B917E00"))
			};
		stopMap.put(StopIndizes.GLANZ_LCD_RECHTS, stopArray);
		
		
		//x1="22.6556263" y1="30.5080204" x2="60.9889603" y2="14.0080194"
		//x1 = centerX - 22.6556263 = 13,3443737 Strecke; 100/72 * 13,3443737 = 0.1853385236111111
		//y1 = centerY - 30.5080204 = 5,4919796 Strecke; 100/72 * 5,4919796 =  0.00762774944444444
		//x2 = 60.9889603 - centerX = 24,9889603 Strecke; 100/72 * 24,9889603 = 0,3470688930555556
		//y1 = centerY - 14.0080194 = 21,9919806 Strecke; 100/72 * 21,9919806 = 0,305444175
		//TODO weiß nich ob das so stimmt, schaut komisch aus.
		
		//TODO es muss von einer äußeren rechten Ecke in den Mittelpunkt führen und nicht umgekehrt
		
		//TODO liegt es an den Stop Farben? 
		lgLCDRechts = new LinearGradient(
				centerX + (gaugeSize *  0.3470688930555556),
				
				centerY - (gaugeSize * 0.00762774944444444),
				//hier plus weil ausgangsX größer als center X war
				//centerX + (gaugeSize *  0.3470688930555556), 
				centerX + (gaugeSize * 0.1853385236111111),
				
				centerY - (gaugeSize * 0.305444175), 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.GLANZ_LCD_RECHTS));
		
		overlayLCDRechts = new Arc();
		overlayLCDRechts.setCenterX(centerX);
		//3 px Abstand zu der gruenen Flaeche 100/128 * 3 = 2.34375 = 0.0234375
		
		overlayLCDRechts.setCenterY(centerY - (gaugeSize * 0.015635));
		overlayLCDRechts.setRadiusX(radius2 *  0.90625);
		//hier wird noch die Zugabe von Y wieder abgezogen
		overlayLCDRechts.setRadiusY(radius2 * 0.90625 - (gaugeSize * 0.015635));
		overlayLCDRechts.setStartAngle(0f);
		overlayLCDRechts.setLength(90.0f);
		overlayLCDRechts.setType(ArcType.ROUND);
		overlayLCDRechts.setFill(lgLCDRechts);
		
		
		//ab hier die leuchdiode
		diodeUntergrund = new Circle();
		//cx 61.5 cy 30.333334 r = 3
		diodeUntergrund.setFill(Color.web("#353535"));
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#FFFFFFCC")),
				new Stop(0.1243167, Color.web("#FBFBFBC9")),
				new Stop(0.2560624, Color.web("#EEEEEEC5")),
				new Stop(0.3913673, Color.web("#D9D9D9C2")),
				new Stop(0.5290745, Color.web("#BBBBBBBF")),
				new Stop(0.6686305, Color.web("#959595BB")),
				new Stop(0.8097913, Color.web("#666666B7")),
				new Stop(0.9495661, Color.web("#303030B4")),
				new Stop(1.0, Color.web("#1A1A1AB3"))
			};
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#FFFFFFCC")),
				new Stop(0.1243167, Color.web("#FBFBFBC9")),
				new Stop(0.2560624, Color.web("#EEEEEEC5")),
				new Stop(0.3913673, Color.web("#D9D9D9C2")),
				new Stop(0.5290745, Color.web("#BBBBBBBF")),
				new Stop(0.6686305, Color.web("#959595BB")),
				new Stop(0.8097913, Color.web("#666666B7")),
				new Stop(0.9495661, Color.web("#303030B4")),
				new Stop(1.0, Color.web("#1A1A1AB3"))
			};
		stopMap.put(StopIndizes.DIODE_BASIS_GLANZ, stopArray);
		
		//x1="61.5" y1="33.3333321" x2="61.5" y2="27.333334"
		lgDiodeBasisGlanz = new LinearGradient(61.5, 33.3333321, 61.5,  27.333334,
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.DIODE_BASIS_GLANZ));
		
		diodeBasisGlanz = new Circle();
		//cx="61.5" cy="30.333334" r="3"
		diodeBasisGlanz.setFill(Color.web("#FF0000"));
		
		diodeFarbe = new Circle();
		diodeFarbe.setFill(Color.web("#BABA00"));
		
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#FFFFFFB3")),
				new Stop(0.9629469, Color.web("#FFFFFF1F")),
				new Stop(1.0, Color.web("#FFFFFF1A"))
			};
	
		
		stopMap.put(StopIndizes.DIODE_FARBE_RADIALGRADIENT, stopArray);
		
		rgGlanzDiode = new RadialGradient(0D, 0D, centerX, centerY, 2.5, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.DIODE_FARBE_RADIALGRADIENT));
		
		glanzDiode = new Circle();
		glanzDiode.setFill(rgGlanzDiode);
		
		
		stopArray = new Stop[]{
				new Stop(0.0486486, Color.web("#FCEE21B3")),
				new Stop(0.4550311, Color.web("#FCEF1E66")),
				new Stop(0.7107612, Color.web("#FDF41636")),
				new Stop(0.9242952, Color.web("#FEFB070E")),
				new Stop(1.0, Color.web("#FFFF0000"))
			};
		stopMap.put(StopIndizes.DIODE_FARBE_SCHEIN, stopArray);
		
		rgFarbeSchein = new RadialGradient(0D, 0D, centerX, centerY, 3.3541667, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.DIODE_FARBE_SCHEIN));
		
		scheinDiode = new Circle();
		scheinDiode.setFill(rgFarbeSchein);
		
		//TODO die Ränder
		//rahmenObenGlanz = new Arc();
		//rahmenOben = new SVGPath();
		//rahmenOben.setFill(Color.web("#FF0000"));
		//rahmenOben.setContent("M36,2.5C17.6349487,2.5,2.7293091,17.2800293,2.5103149,35.5931396h66.9793701"+
		//"C69.2706909,17.2800293,54.3650513,2.5,36,2.5z M36,3.2524414c17.8223267,0,32.2874756,14.1079102,32.5,31.5882568h-65"+
		//"C3.7125244,17.3603516,18.1776733,3.2524414,36,3.2524414z");
		
		
		
		
		
		this.getChildren().addAll(componentBasis, componentBasisRahmen, componentBasisGlanz, componentBasisInlay, backgroundHalf, 
				backgroundHalfGlanz, backgroundLCDBasis, overlayLCDOben, overlayLCDLinks, /* overlayLCDRechts*/
				diodeUntergrund, diodeBasisGlanz, diodeFarbe, glanzDiode, scheinDiode /*, rahmenOben*/);
	}
	

	private void resize() 
	{
		double size  = getWidth() < getHeight() ? getWidth() : getHeight();
		
		double breitenDifferenz = 0, hoehenDifferenz = 0;
		
		if(getHeight() > 0)
			centerY = getHeight() / 2d;
		
		if(getWidth() > 0)
			centerX = getWidth() / 2d;
		
		double radius = size / 2d;
		
		
		componentBasis.setRadius(radius);
		componentBasis.setCenterX(centerX);
		componentBasis.setCenterY(centerY);
		
		rgComponentBasisRahmen = new RadialGradient(0D, 0D, centerX, centerY, radius, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.COMPONENT_BASIS_RAHMEN));
		
		componentBasisRahmen.setRadius(radius);
		componentBasisRahmen.setCenterX(centerX);
		componentBasisRahmen.setCenterY(centerY);
		componentBasisRahmen.setFill(rgComponentBasisRahmen);
		
		
		//y1 = 36 = 100/72 * 36 ==> 0,5
		//y2 = 36 ==> 0,5
		//bei x geht es nur über eine Strecke weiter
		//x1 36 - 0.5 = 35.5 Strecke 100/72 * 35.5 = 49,30555555555556 = 0.4930555555555556
		//x2 = 71.5 - 36  = 35,5 Strecke 100/72 * 35.5 = 49,30555555555556 = 0.4930555555555556
		
		lgComponentBasisGlanz = new LinearGradient(centerX - (size *  0.4930555555555556) , size * 0.5, 
				centerX + (size *  0.4930555555555556), size * 0.5, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.COMPONENT_BASIS_GLANZ));
	
		componentBasisGlanz.setRadius(radius);
		componentBasisGlanz.setCenterX(centerX);
		componentBasisGlanz.setCenterY(centerY);
		componentBasisGlanz.setFill(lgComponentBasisGlanz);
		
		//radius 34 => 100/35.5 * 34 = 0.9577464788732394
		
		componentBasisInlay.setRadius(radius * 0.9577464788732394);
		componentBasisInlay.setCenterX(centerX);
		componentBasisInlay.setCenterY(centerY);
		
		
		//x1 = 36 - 2.5175171 = 33,4824829 Strecke ; 100/72 * 33,4824829 ==> 0,4650344847222222
		//x2 = 69.4824829 - 36 = 33,4824829 Strecke; 100/72 * 33,4824829 ==> 0,4650344847222222
		
		//y1 = 53.0959778; 100/72 * 53.0959778 ==> 0,7374441361111111
		//y2 = 53.0959778; 100/72 * 53.0959778 ==> 0,7374441361111111
		
		//TODO verschiebung von x wenn nicht die Komponentengröße sich verändert hat?
		lgSchaltflaecheBasis = new LinearGradient(centerX - (size * 0.4650344847222222), size * 0.7374441361111111, 
				centerX + (size * 0.4650344847222222), size * 0.7374441361111111, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.SCHALTFLAECHE_BASIS));
		
		backgroundHalf.setCenterX(centerX);
		//3 px Abstand zu der gruenen Flaeche 100/128 * 3 = 2.34375 = 0.0234375
		//2 px 1,5625 = 0.015635
		
		backgroundHalf.setCenterY(centerY + (size * 0.015635));
		
		//r = 60 = 100/64 * 60 = 93,75 = 0.9375
		// r = 56 = 100/ 64 * 56 = 87,5 = 0.875
		// r = 58 = 100/64 * 58 = 90,625 = 0.90625
		//double radius2 = size / 2;
		backgroundHalf.setRadiusX(radius *  0.90625);
		//hier wird noch die Zugabe von Y wieder abgezogen
		backgroundHalf.setRadiusY(radius * 0.90625 - (size * 0.015635));
		backgroundHalf.setStartAngle(180f);
		backgroundHalf.setLength(180.0f);
		backgroundHalf.setType(ArcType.ROUND);
		backgroundHalf.setFill(lgSchaltflaecheBasis);
		
		
		
		//auch hier wieder über die Strecke 
		//x1  36 - 2.5175171 = 33,4824829 Strecke ; 100/72 * 33,4824829 ==> 0,4650344847222222
		//x2 68.9649658 - 36 = 32,9649658 Strecke; 100/72 * 32,9649658 ==> 0,4578467472222222
		
		//y1="53.0959778" 
		//y2="53.0959778">
		lgSchaltflaecheGlanz = new LinearGradient(centerX - (size * 0.4650344847222222), size * 0.7374441361111111, 
				centerX + (size * 0.4578467472222222), size * 0.7374441361111111, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.SCHALTFLAECHE_GLANZ));
		
		
		backgroundHalfGlanz.setCenterX(centerX);
		backgroundHalfGlanz.setCenterY(centerY + (size * 0.015635));
		
		//r = 60 = 100/64 * 60 = 93,75 = 0.9375
		// r = 56 = 100/ 64 * 56 = 87,5 = 0.875
		// r = 58 = 100/64 * 58 = 90,625 = 0.90625
		backgroundHalfGlanz.setRadiusX(radius *  0.90625);
		//hier wird noch die Zugabe von Y wieder abgezogen
		backgroundHalfGlanz.setRadiusY(radius * 0.90625 - (radius * 0.015635));
		backgroundHalfGlanz.setStartAngle(180f);
		backgroundHalfGlanz.setLength(180.0f);
		backgroundHalfGlanz.setType(ArcType.ROUND);
		backgroundHalfGlanz.setFill(lgSchaltflaecheGlanz);
		
		//LCD Bereich
		
		backgroundLCDBasis.setFill(Color.web("#a1a797"));
		backgroundLCDBasis.setCenterX(centerX);
		//3 px Abstand zu der gruenen Flaeche 100/128 * 3 = 2.34375 = 0.0234375
		
		backgroundLCDBasis.setCenterY(centerY - (size * 0.015635));
		backgroundLCDBasis.setRadiusX(radius *  0.90625);
		//hier wird noch die Zugabe von Y wieder abgezogen
		backgroundLCDBasis.setRadiusY(radius * 0.90625 - (size * 0.015635));
		backgroundLCDBasis.setStartAngle(0f);
		backgroundLCDBasis.setLength(180.0f);
		backgroundLCDBasis.setType(ArcType.ROUND);
		
		InnerShadow innerShadow = new InnerShadow();
		innerShadow.setBlurType(BlurType.GAUSSIAN);
		innerShadow.setOffsetX(15);
		innerShadow.setOffsetY(15);
		innerShadow.setColor(Color.web("#b0b7a550"));
		
		//TODO eventl weg
		backgroundLCDBasis.setEffect(innerShadow);
		
		lgLCDOben = new LinearGradient(centerX, centerY - (size * 0.5405092638888889) ,centerX, 
				centerY - (size * 0.3101851388888889), false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.GLANZ_LCD_OBEN));
		
		overlayLCDOben.setFill(lgLCDOben);
		overlayLCDOben.setCenterX(centerX);
		//3 px Abstand zu der gruenen Flaeche 100/128 * 3 = 2.34375 = 0.0234375
		
		overlayLCDOben.setCenterY(centerY - (size * 0.015635));
		overlayLCDOben.setRadiusX(radius *  0.90625);
		//hier wird noch die Zugabe von Y wieder abgezogen
		overlayLCDOben.setRadiusY(radius * 0.90625 - (size * 0.015635));
		overlayLCDOben.setStartAngle(45f);
		overlayLCDOben.setLength(90.0f);
		overlayLCDOben.setType(ArcType.ROUND);
		
		
		lgLCDLinks = new LinearGradient(centerX - (size * 0.4356554009722222), 
				centerY - (size * 0.3093178277777778),
				centerX - (size *  0.2562572430555556), 
				centerY - (size * 0.2537622722222222), 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.GLANZ_LCD_OBEN));
		
		
		
		overlayLCDLinks.setCenterX(centerX);
		//3 px Abstand zu der gruenen Flaeche 100/128 * 3 = 2.34375 = 0.0234375
		
		overlayLCDLinks.setCenterY(centerY - (size * 0.015635));
		overlayLCDLinks.setRadiusX(radius *  0.90625);
		//hier wird noch die Zugabe von Y wieder abgezogen
		overlayLCDLinks.setRadiusY(radius * 0.90625 - (size * 0.015635));
		overlayLCDLinks.setStartAngle(100f);
		overlayLCDLinks.setLength(80.0f);
		overlayLCDLinks.setType(ArcType.ROUND);
		overlayLCDLinks.setFill(lgLCDLinks);
		
		/*
		lgLCDRechts = new LinearGradient(
				centerX + (size *  0.3470688930555556),
				
				
				centerY - (size * 0.00762774944444444),
				//hier plus weil ausgangsX größer als center X war
				//centerX + (gaugeSize *  0.3470688930555556), 
				centerX - (size * 0.1853385236111111),
				
				centerY - (size * 0.305444175), 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.GLANZ_LCD_RECHTS));
		
	
		overlayLCDRechts.setCenterX(centerX);
		//3 px Abstand zu der gruenen Flaeche 100/128 * 3 = 2.34375 = 0.0234375
		
		overlayLCDRechts.setCenterY(centerY - (size * 0.015635));
		overlayLCDRechts.setRadiusX(radius *  0.90625);
		//hier wird noch die Zugabe von Y wieder abgezogen
		overlayLCDRechts.setRadiusY(radius * 0.90625 - (size * 0.015635));
		overlayLCDRechts.setStartAngle(0f);
		overlayLCDRechts.setLength(90.0f);
		overlayLCDRechts.setType(ArcType.ROUND);
		overlayLCDRechts.setFill(lgLCDRechts);
		*/
		
		//cx 61.5 cy 30.333334 r = 3
		diodeUntergrund.setFill(Color.web("#353535"));
		
		//TODO das geht so auch nicht, es muss der Mittelpunkt in Abhängigkeit von "großen" Mittelpunkt berechnet werden
		
		//60.5 - 36 = 24,5 = 100/72 * 24.5 = 0.3402777777777778
		//36 - 30.333334 = 5,666666 = 100/72 * 5,666666 = 0,0787036944444444
		diodeUntergrund.setCenterX(centerX + (size *  0.3402777777777778));
		diodeUntergrund.setCenterY(centerY - (size * 0.0787036944444444));
		//100/72 * 3 = 4,166666666666667 = 0.04166666666666667
		diodeUntergrund.setRadius(size * 0.04166666666666667);
		
		
		//x1="61.5" y1="33.3333321" x2="61.5" y2="27.333334"
		//61.5 - 36 = 25,5 = 100/72 * 25,5 = 0.3541666666666667
		
		//36 - 33.3333321 = 2,6666679 = 100/72 * 2,6666679 = 0.03703705416666667
		
		//36 - 27.333334 = 8,666666 = 100/72 * 8,666666 = 0.1203703611111111
		
		//TODO hier gehts weiter
		lgDiodeBasisGlanz = new LinearGradient(centerX + (size *  0.3541666666666667),
				centerY - (size * 0.03703705416666667), centerX + (size *  0.3541666666666667),  centerY - (size * 0.1203703611111111),
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.DIODE_BASIS_GLANZ));
		
		//diodeBasisGlanz = new Circle();
		//cx="61.5" cy="30.333334" r="3"
		
		//61.5 - 36 = 25,5 = 100/72 * 25,5 = 0,3541666666666667
		//36 - 30.333334 = 5,666666 = 100/72 * 5,666666 = 0,0787036944444444
		
		diodeBasisGlanz.setCenterX(centerX + (size *  0.3402777777777778));
		diodeBasisGlanz.setCenterY(centerY - (size * 0.0787036944444444));
		//100/72 * 3 = 4,166666666666667 = 0.04166666666666667
		diodeBasisGlanz.setRadius(size * 0.04166666666666667);
		diodeBasisGlanz.setFill(lgDiodeBasisGlanz);
		
		//	 cx="61.5" cy="30.333334" r="2.5"/>
		//61.5 - 36 = 25,5 = 100/72 * 25,5 = 0.3541666666666667
		//60.5 - 36 = 24,5 = 100/72 * 24.5 = 0.3402777777777778
		diodeFarbe.setCenterX(centerX + (size *   0.3402777777777778));
		//36 - 30.333334 = 5,666666 = 100/72 * 5,666666 = 0,0787036944444444
		diodeFarbe.setCenterY(centerY - (size * 0.0787036944444444));
		//TODO raus
		//diodeFarbe.setFill(Color.web("#FF0000"));
		//100/72 * 2.5 = 3,472222222222222 = 0.03472222222222222
		diodeFarbe.setRadius(size * 0.03472222222222222);
		//wäre auch eine Möglichkeit für einen schein
		Glow glow = new Glow();
	    glow.setLevel(1);
	   // diodeFarbe.setEffect(glow);
		
		
		rgGlanzDiode = new RadialGradient(0D, 0D, centerX + (size *   0.3402777777777778), 
				centerY - (size * 0.0787036944444444), size * 0.03472222222222222, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.DIODE_FARBE_RADIALGRADIENT));
		
		glanzDiode.setCenterX(centerX + (size *   0.3402777777777778));
		//36 - 30.333334 = 5,666666 = 100/72 * 5,666666 = 0,0787036944444444
		glanzDiode.setCenterY(centerY - (size * 0.0787036944444444));
		//100/72 * 2.5 = 3,472222222222222 = 0.03472222222222222
		glanzDiode.setRadius(size * 0.03472222222222222);
		
		glanzDiode.setFill(rgGlanzDiode);

		
		
		// cx="61.015625" cy="29.848959" r="3.3541667"
		//61.015625 - 36 = 25,015625 = 100/72 * 25,015625 = 34,74392361111111 = 0.3474392361111111
		rgFarbeSchein = new RadialGradient(0D, 0D, centerX + (size *   0.3402777777777778), centerY - (size * 0.0787036944444444),
				size * 0.05555555555555556, 
				false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.DIODE_FARBE_SCHEIN));
		
		//matrix(1.1925466 0 0 1.1925466 -11.2639751 -5.2629399)
		Affine transform = new Affine(1.1925466, 0, 0, 1.1925466, -11.2639751, -5.2629399);
		
		
		scheinDiode.setCenterX(centerX + (size *   0.3402777777777778));
		//36 - 30.333334 = 5,666666 = 100/72 * 5,666666 = 0,0787036944444444
		scheinDiode.setCenterY(centerY - (size * 0.0787036944444444));
		//100/72 * 2.5 = 3,472222222222222 = 0.03472222222222222
		//100/72 * 4 = 5,555555555555556 = 0.05555555555555556
		scheinDiode.setRadius(size * 0.05555555555555556);
		
		scheinDiode.setFill(rgFarbeSchein);
		
	
		
	}


}
