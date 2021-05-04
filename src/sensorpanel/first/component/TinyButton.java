package sensorpanel.first.component;


import java.util.HashMap;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import sensorpanel.first.component.TinyButtonText.TextValue;
import tools.helper.UIToolBox;

public class TinyButton extends Circle
{
	
	private Circle button_top;
	
	private double radius_ratio;
	
	private double cx_ratio;
	
	private double cy_ratio;
	
	private HashMap<StopIndizes, Stop[]> stopMap = new HashMap<StopIndizes, Stop[]>();
	
	private double cx_ratio_top_highlight;

	private double cy_ratio_top_highlight;
	
	private double radius_ratio_top_highlight;
	
	/**
	 * Kommandos können hier empfangen werden (listener anschluss)
	 * <br>to listen from outside connect via {@link #getCommandProperty()}
	 */
	private SimpleObjectProperty<Command> commandProperty = new SimpleObjectProperty<Command>();

	private boolean isMousePressed = false;
	
	private Canvas textCanvas;
	
	
	
	
	public enum Command
	{
		BUTTON_PRESSED, BUTTON_RELEASED,
		/**
		 * der wird dann von außerhalb gesetzt, damit auch das aktuelle Kommando nochmal gesetzt werden kann.
		 * <br>you need the reset as a "acknowledge" from outside.
		 */
		RESET_COMMAND;
	}
	
	public enum StopIndizes
	{
		BUTTON_TOP_HIGHLIGHT,
		
		
		
		;
		
	}
	
	
	public TinyButton(double start_cx, double start_cy, double start_w, double start_h, double start_radius,
			double start_cx_top_highlight, double start_cy_top_highlight, double radius_top_highlight)
	{
		super();
		
		this.cx_ratio = UIToolBox.getPointRatio(start_w, start_cx); //100D/start_w * start_cx / 100D;
		this.cy_ratio = UIToolBox.getPointRatio(start_h, start_cy); //100D/start_h * start_cy / 100D;
		this.radius_ratio = UIToolBox.getAreaRatio(start_w, start_h, start_radius); //((100D/(start_w * start_h)) * (Math.pow(start_radius, 2) * Math.PI)) / 100D;
		
		this.cx_ratio_top_highlight = UIToolBox.getPointRatio(start_w, start_cx_top_highlight); //100D/start_w * start_cx_top_highlight / 100D;
		this.cy_ratio_top_highlight = UIToolBox.getPointRatio(start_h, start_cy_top_highlight); //100D/start_h * start_cy_top_highlight / 100D;
		this.radius_ratio_top_highlight = UIToolBox.getAreaRatio(start_w, start_h, radius_top_highlight);// ((100D/(start_w * start_h)) * (Math.pow(radius_top_highlight, 2) * Math.PI)) / 100D;
		
		
		this.initGraphics();
		registerListener();
	}
	
	private void registerListener() 
	{
		this.setOnMousePressed(e -> setNodeMouseEvent(button_top, Command.BUTTON_PRESSED, e));
		this.setOnMouseReleased(e -> setNodeMouseEvent(button_top,Command.BUTTON_RELEASED, e));
		
		this.setOnMouseEntered(e -> System.out.println("guck, wo bischd`?"));
	}
	
	private void initGraphics() {
		
		this.setMouseTransparent(false);
		//button_top = new Circle();
		
		Stop[] stopArray = new Stop[]{
				new Stop(0.57444, Color.web("#16141200")),
				new Stop(0.58364, Color.web("#2725241F")),
				new Stop(0.60244, Color.web("#42403f51")),
				new Stop(0.62466, Color.web("#57555577")),
				new Stop(0.65213, Color.web("#65646492")),
				new Stop(0.69069, Color.web("#6e6d6dA1")),
				new Stop(0.79771, Color.web("#706f6fA6")),
				new Stop(0.82478, Color.web("#616060B2")),
				new Stop(0.93813, Color.web("#2b2927E1")),
				new Stop(1, Color.web("#161412F2"))
			};
		stopMap.put(StopIndizes.BUTTON_TOP_HIGHLIGHT, stopArray);
		
		
		
		//this.getChildren().add(button_top);
		
	}

	
	public void setResizeValues(double w, double h) 
	{
		this.setCenterX(w * this.cx_ratio);
		this.setCenterY(h * this.cy_ratio);
		//button_top.setCenterX(w * this.cx_ratio);
		//button_top.setCenterY(h * this.cy_ratio);
		
		
		double radius = UIToolBox.getRadiusFromRatio(w, h, radius_ratio);
		//button_top.setRadius(radius);
		this.setRadius(radius);
		
		radius = UIToolBox.getRadiusFromRatio(w, h, radius_ratio_top_highlight);
		RadialGradient rg = new RadialGradient(0D, 0D, w * this.cx_ratio_top_highlight, h * this.cy_ratio_top_highlight, 
				radius, false, CycleMethod.NO_CYCLE, stopMap.get(StopIndizes.BUTTON_TOP_HIGHLIGHT));
	
		
		//button_top.setFill(rg);
		this.setFill(rg);
	
	
	}
	
	private void setNodeMouseEvent(Node node, Command commandValue, MouseEvent e) 
	{
		if(commandValue == Command.BUTTON_PRESSED)
		{
			isMousePressed = true;
			this.setEffect(new InnerShadow());
		}
		else if(commandValue == Command.BUTTON_RELEASED)
		{
			isMousePressed = false;
			this.setEffect(null);
		}
		
		commandProperty.set(commandValue);
		e.consume();
	}


	
}
