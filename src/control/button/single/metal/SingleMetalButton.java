package control.button.single.metal;

import java.util.HashMap;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;

/**
 * a simple button with a lightly metal style at the edge
 * @author m.goerlich
 *
 */
public class SingleMetalButton extends Region
{
	public enum Command
	{
		BUTTON_PRESSED, BUTTON_RELEASED,
		/**
		 * der wird dann von außerhalb gesetzt, damit auch das aktuelle Kommando nochmal gesetzt werden kann.
		 * <br>you need the reset as a "acknowledge" from outside.
		 */
		RESET_COMMAND;
	}
	
	/**
	 * map index for the different stop arrays
	 * @author m.goerlich
	 *
	 */
	public enum StopIndizes
	{
		GRUNDFLAECHE, GRUNDFLAECHE_GLANZ, INLAY_GLANZ1, INLAY_GLANZ2, INNERSHADOW_STRONG, INNERSHADOW_LIGHT;
	}
	
	/**
	 * storage for the different stop arrays
	 */
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	private Circle grundflaeche, grundflaecheGlanz, inlay, inlayGlanz1, innerShadowStrong, innerShadowLight;
	
	private Ellipse inlayGlanz2;
	
	private double centerX = 20, centerY = 20;
	
	/**
	 * the design size from illustrator
	 */
	@SuppressWarnings("unused")
	private double width = 40, height = 40;
	
	/**
	 * switch um auszuwählen welche Innershadow verwendet werden soll.
	 */
	private boolean isMousePressed = false;

	/**
	 * kann sich verändern, evtl brauche ich diese Variable für die Anpassung der Farbverläufe...mal sehen.
	 * <br>starting inlay color from the button; it's changeable {@link #setInlayFill(Color)}
	 */
	private Color inlayColor = Color.web("#707070");
	
	/**
	 * Ein Bereich der individuell mit Inhalt befüllt werden kann
	 * <br>A area to set text or image on the button
	 */
	private ContentRegion contentRegion;
	
	/**
	 * Kommandos können hier empfangen werden (listener anschluss)
	 * <br>to listen from outside connect via {@link #getCommandProperty()}
	 */
	private SimpleObjectProperty<Command> commandProperty = new SimpleObjectProperty<Command>();

	
	public SingleMetalButton()
	{
		super();
		this.initGraphics();
		this.registerListener();
		
		
		
	}


	private void registerListener() 
	{
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
		grundflaeche.setOnMousePressed(e -> setNodeMouseEvent(grundflaeche, Command.BUTTON_PRESSED, e));
		grundflaeche.setOnMouseReleased(e -> setNodeMouseEvent(grundflaeche,Command.BUTTON_RELEASED, e));
	}
	

	private void setNodeMouseEvent(Node node, Command commandValue, MouseEvent e) 
	{
		if(commandValue == Command.BUTTON_PRESSED)
		{
			isMousePressed = true;
			//ui stuff
			innerShadowStrong.setOpacity(1.0);
			innerShadowLight.setOpacity(0.0);
			contentRegion.setMousePressed();
		
		}
		else if(commandValue == Command.BUTTON_RELEASED)
		{
			isMousePressed = false;
			//ui stuff
			innerShadowStrong.setOpacity(0.0);
			innerShadowLight.setOpacity(1.0);
			contentRegion.setMouseReleased();
		}
		
		commandProperty.set(commandValue);
		e.consume();
	}


	private void initGraphics() 
	{
		grundflaeche = new Circle();
		
		Stop[] stopArray = new Stop[]{
				new Stop(0.0, Color.web("#EAEAEA")),
				new Stop(0.3606361, Color.web("#E8E8E8")),
				new Stop(0.4905558, Color.web("#E1E1E1")),
				new Stop(0.5831469, Color.web("#D6D6D6")),
				new Stop(0.6580502, Color.web("#C5C5C5")),
				new Stop(0.722233, Color.web("#AFAFAF")),
				new Stop(0.7790776, Color.web("#939393")),
				new Stop(0.830457, Color.web("#737373")),
				new Stop(0.8777024, Color.web("#4D4D4D")),
				new Stop(0.9197454, Color.web("#242424")),
				new Stop(0.9512195, Color.web("#000000"))
			};
	
		stopMap.put(StopIndizes.GRUNDFLAECHE, stopArray);
		
		grundflaecheGlanz = new Circle();
		grundflaecheGlanz.setMouseTransparent(true);
		
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#FFFFFFCC")),
				new Stop(0.4317073, Color.web("#000000CC")),
				new Stop(0.7365854, Color.web("#F2F2F2")),
				new Stop(0.9926829, Color.web("#666666"))
			};
		stopMap.put(StopIndizes.GRUNDFLAECHE_GLANZ, stopArray);
		
		inlay = new Circle();
		inlay.setFill(inlayColor);
		inlay.setMouseTransparent(true);
		
		
		inlayGlanz1 = new Circle();
		inlayGlanz1.setMouseTransparent(true);
		
		stopArray = new Stop[]{
				new Stop(0.0073171, Color.web("#B3B3B300")),
				new Stop(0.2430577, Color.web("#B7B7B712")),
				new Stop(0.4672107, Color.web("#C2C2C223")),
				new Stop(0.6867312, Color.web("#D5D5D534")),
				new Stop(0.9020432, Color.web("#F0F0F045")),
				new Stop(1.0, Color.web("#FFFFFF4D"))
			};
		stopMap.put(StopIndizes.INLAY_GLANZ1, stopArray);
		
		
		inlayGlanz2 = new Ellipse();
		inlayGlanz2.setMouseTransparent(true);
		stopArray = new Stop[]{
				new Stop(0.0, Color.web("#FFFFFF99")),
				new Stop(0.0979568, Color.web("#F0F0F08A")),
				new Stop(0.3132688, Color.web("#D5D5D569")),
				new Stop(0.5327894, Color.web("#C2C2C247")),
				new Stop(0.7569423, Color.web("#B7B7B724")),
				new Stop(0.9926829, Color.web("#B3B3B300"))
			};
		stopMap.put(StopIndizes.INLAY_GLANZ2, stopArray);
		
		innerShadowStrong = new Circle();
		innerShadowStrong.setMouseTransparent(true);
		
		stopArray = new Stop[]{
				new Stop(0.0073171, Color.web("#B3B3B300")),
				new Stop(0.4172025, Color.web("#B1B1B15F")),
				new Stop(0.5648299, Color.web("#AAAAAA81")),
				new Stop(0.670073, Color.web("#9F9F9F99")),
				new Stop(0.7551594, Color.web("#8E8E8EAD")),
				new Stop(0.8280339, Color.web("#787878BE")),
				new Stop(0.89261, Color.web("#5C5C5CCD")),
				new Stop(0.9495308, Color.web("#3D3D3DDA")),
				new Stop(1.0, Color.web("#1A1A1AE6"))
			};
		
		stopMap.put(StopIndizes.INNERSHADOW_STRONG, stopArray);
		
		
		innerShadowLight = new Circle();
		innerShadowLight.setMouseTransparent(true);
		stopArray = new Stop[]{
				new Stop(0.0073171, Color.web("#B3B3B300")),
				new Stop(0.4519936, Color.web("#B1B1B15B")),
				new Stop(0.6121601, Color.web("#AAAAAA7C")),
				new Stop(0.7263085, Color.web("#9F9F9F94")),
				new Stop(0.8186446, Color.web("#8E8E8EA7")),
				new Stop(0.8977138, Color.web("#787878B7")),
				new Stop(0.9664453, Color.web("#5D5D5DC5")),
				new Stop(1.0, Color.web("#4D4D4DCC"))
			};
		
		stopMap.put(StopIndizes.INNERSHADOW_LIGHT, stopArray);
		
		//to visulize text or image on the button
		contentRegion = new ContentRegion();
		contentRegion.setMouseTransparent(true);
		
		this.getChildren().addAll(grundflaeche, grundflaecheGlanz, inlay, contentRegion, inlayGlanz1, inlayGlanz2, innerShadowStrong, innerShadowLight);
	}
	

	private void resize() 
	{
		double size  = getWidth() < getHeight() ? getWidth() : getHeight();
		
		
		if(getHeight() > 0)
			centerY = getHeight() / 2d;
		
		if(getWidth() > 0)
			centerX = getWidth() / 2d;
		
		double radius = size / 2d;
		
		grundflaeche.setCenterX(centerX);
		grundflaeche.setCenterY(centerY);
		grundflaeche.setRadius(radius);
		RadialGradient rg = new RadialGradient(0D, 0D, centerX, centerY, radius, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.GRUNDFLAECHE));
		grundflaeche.setFill(rg);
		
		
		grundflaecheGlanz.setCenterX(centerX);
		grundflaecheGlanz.setCenterY(centerY);
		grundflaecheGlanz.setRadius(radius);
		
		//x1="6.5416665" y1="6.5416665" x2="33.7917938" y2="33.7917938"
		
		//x1 = 20 - 6.5416665 = 13,4583335 => 100/40 = 33,64583375
		//y1 = 20 -  6.5416665 = 13,4583335 => 100/40 = 33,64583375
		//x2 = 33.7917938 - 20 = 13.7917938 => 100/40 *  13.7917938 ==> 34,4794845
		//y2 dito
		LinearGradient lg = new LinearGradient(centerX - (size * 0.3364583375), 
				centerY - (size * 0.3364583375),
				centerX + (size *  0.344794845), 
				centerY + (size *  0.344794845), 
				false, 
				CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.GRUNDFLAECHE_GLANZ));
		
		grundflaecheGlanz.setFill(lg);
		
		
		//r = 18 = 100/20 * 18 = 90 = 0.9
		inlay.setCenterX(centerX);
		inlay.setCenterY(centerY);
		inlay.setRadius(radius * 0.9);
		
		
		inlayGlanz1.setCenterX(centerX);
		inlayGlanz1.setCenterY(centerY);
		inlayGlanz1.setRadius(radius * 0.9);
		rg = new RadialGradient(0D, 0D, centerX, centerY, inlayGlanz1.getRadius(), false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.INLAY_GLANZ1));
		inlayGlanz1.setFill(rg);
		
		//cx="19.739584" cy="14.208333" rx="14.239583" ry="12.208333"
		//runter rechnen
		
		//cx 20 - 19.739584 = 0,260416 ==> 100/40 * 0,260416 = 0,65104 = 0.0065104
		//cx 20 - 14.208333 = 5,791667 ==> 100/40 * 5,791667 = 14,4791675 = 0.144791675
		//rx 100/20 * 14.239583 = 71,197915 = 0.71197915
		//ry 100/20 * 12.208333 = 61,041665 = 0.61041665
		 
		inlayGlanz2.setCenterX(centerX - (size * 0.0065104));
		inlayGlanz2.setCenterY(centerY - (size * 0.144791675));
		inlayGlanz2.setRadiusX(radius * 0.71197915);
		inlayGlanz2.setRadiusY(radius * 0.61041665 );
		
		//x1 20 - 13.7767458 = 6,2232542 = 6,2232542 = 100/40 * 6,2232542 = 15,5581355 = 0.155581355
		//y1 20 - 6.3435564 = 13,6564436 = 100/40 * 13,6564436 = 34,141109 = 0.34141109
		//x2 26.3401184 -20 = 6.3401184 = 100/40 * 6.3401184 = 15,850296 = 0.15850296
		//y2 22.9142132 - 20  = 2,9142132 = 100/40 * 2,9142132 = 7,285533 = 0.07285533
		

		lg = new LinearGradient(centerX - (size * 0.155581355), 
				centerY - (size * 0.34141109),
				centerX + (size *  0.15850296), 
				centerY + (size *  0.07285533), 
				false, 
				CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.INLAY_GLANZ2));
		
		
		
		inlayGlanz2.setFill(lg);
		//dominanz senken irgendwie ist das rendern anders als bei illustrator
		//inlayGlanz2.setOpacity(0.7);
		
		
		
		innerShadowStrong.setCenterX(centerX);
		innerShadowStrong.setCenterY(centerY);
		//r war 18 wie bei den anderen
		innerShadowStrong.setRadius(radius * 0.9);
		rg = new RadialGradient(0D, 0D, centerX, centerY, innerShadowStrong.getRadius(), false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.INNERSHADOW_STRONG));
		innerShadowStrong.setFill(rg);
		
		
		innerShadowLight.setCenterX(centerX);
		innerShadowLight.setCenterY(centerY);
		innerShadowLight.setRadius(radius * 0.9);
		
		rg = new RadialGradient(0D, 0D, centerX, centerY, innerShadowLight.getRadius(), false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.INNERSHADOW_LIGHT));
		innerShadowLight.setFill(rg);
		
		if(isMousePressed)
		{
			innerShadowStrong.setOpacity(1.0);
			innerShadowLight.setOpacity(0.0);
		}
		else
		{
			innerShadowStrong.setOpacity(0.0);
			innerShadowLight.setOpacity(1.0);
		}
		
		//Die Contentsize war im Orignal 20x20 also die hälfte zur Gesamtfläche
		//x = 10 / 100 / 40 * 10 = 0.25
		//y = 10 wie oben
		contentRegion.setLayoutX(centerX - (size * 0.25));
		contentRegion.setLayoutY(centerY - (size * 0.25));
		
		double contentSize = size / 2;
		
		
		//at last change the size from the content region
		contentRegion.setNewSize(contentSize);
	}
	
	/**
	 * change color from outside
	 * @param color
	 */
	public void setInlayFill(Color color)
	{
		inlayColor = color;
		inlay.setFill(inlayColor);
	}

	/**
	 * set a image on the content region
	 * @param imageForView
	 */
	public void setImageView(Image imageForView)
	{
		contentRegion.setImageView(imageForView);
	}

	/**
	 * set a text value on the content region
	 * @param textToShow
	 */
	public void setText(String textToShow) {
		contentRegion.setText(textToShow);
		
	}

	public ContentRegion getContentRegion() {
		return contentRegion;
	}

	/**
	 * to connect listener from outside and react on the commands
	 * @return
	 */
	public SimpleObjectProperty<Command> getCommandProperty(){
		return commandProperty;
	}
	

}
