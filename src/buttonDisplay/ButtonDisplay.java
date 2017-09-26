package buttonDisplay;

import java.util.HashMap;

import firstgauge.CustomCircle.StopIndizes;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

/**
 * Eine Anzeige mit einer Schaltflaeche die einen toggle ermöglicht
 * <br>Die Ausdehnung des Kreises lag bei Erstellung bei 71px * 71px
 * @author m.goerlich
 *
 */
public class ButtonDisplay extends Region
{
	
	public enum StopIndizes
	{
		COMPONENT_BASIS_RAHMEN,
		
		COMPONENT_BASIS_GLANZ;
		
		
		
	}
	
	
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	private Circle componentBasis, componentBasisRahmen, componentBasisGlanz, componentBasisInlay;
	
	private RadialGradient rgComponentBasisRahmen;
	
	private LinearGradient rgComponentBasisGlanz;
	
	private double centerX = 36, centerY = 36;
	
	private double width = 71, height = 71;
	
	
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
		
	
		rgComponentBasisGlanz = new LinearGradient(centerX - radius, centerY, (centerX - radius) + width, centerY, false, CycleMethod.NO_CYCLE, stopArray);
		
		
		componentBasisGlanz = new Circle();
		componentBasisGlanz.setRadius(35.5);
		componentBasisGlanz.setCenterX(centerX);
		componentBasisGlanz.setCenterY(centerY);
		componentBasisGlanz.setFill(rgComponentBasisGlanz);
		
		componentBasisInlay = new Circle();
		//1.5 kleiner als normal
		componentBasisInlay.setRadius(34);
		componentBasisInlay.setCenterX(centerX);
		componentBasisInlay.setCenterY(centerY);
		componentBasisInlay.setFill(Color.web("#0C0C0C"));
		
		
		
		
		this.getChildren().addAll(componentBasis, componentBasisRahmen, componentBasisGlanz, componentBasisInlay);
	}
	

	private void resize() 
	{
		// TODO Auto-generated method stub
		
	}


}
