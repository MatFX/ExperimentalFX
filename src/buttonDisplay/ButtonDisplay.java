package buttonDisplay;

import java.util.HashMap;

import javafx.scene.effect.BlurType;
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
import javafx.scene.shape.SVGPath;

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
		
		GLANZ_LCD_OBEN;
		
		
		
	}
	
	
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	private Circle componentBasis, componentBasisRahmen, componentBasisGlanz, componentBasisInlay;
	
	private RadialGradient rgComponentBasisRahmen;
	
	private LinearGradient lgComponentBasisGlanz, lgSchaltflaecheBasis, lgSchaltflaecheGlanz;
	
	private double centerX = 36, centerY = 36;
	
	private double width = 72, height = 72;

	private Arc backgroundHalf, backgroundHalfGlanz, backgroundLCDBasis;
	
	
	
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
		
		
		this.getChildren().addAll(componentBasis, componentBasisRahmen, componentBasisGlanz, componentBasisInlay, backgroundHalf, backgroundHalfGlanz, backgroundLCDBasis);
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
		
		backgroundLCDBasis.setFill(Color.web("#808964FF"));
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
		
		
		backgroundLCDBasis.setEffect(innerShadow);
		
		
		
		
	}


}
