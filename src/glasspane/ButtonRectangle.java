package glasspane;



import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class ButtonRectangle extends Rectangle
{
	
	/**
	 * Kommandos können hier empfangen werden (listener anschluss)
	 * <br>to listen from outside connect via {@link #getCommandProperty()}
	 */
	private SimpleObjectProperty<Command> commandProperty = new SimpleObjectProperty<Command>();
	
	private PositionGradient positionGradient;
	
	//Werte die auf Basis der ursprünglichen Breite und Höhe des ausgehenden Objekts ermittelt wurden.
	private double percentValue_X, percenValue_Y, percentValue_Width, percentValue_Height,arcWidthAndHeight;
	
	public enum PositionGradient
	{
		FROM_DOWN_TO_UP,
		
		FROM_UP_TO_DOWN;
	}
	
	
	public enum Command
	{
		BUTTON_PRESSED, BUTTON_RELEASED,
		
		RESET_COMMAND;
	}
	
	public ButtonRectangle(PositionGradient positionGradient, double percentValue_X, double percenValue_Y, double percentValue_Width, double percentValue_Height, double arcWidthAndHeight)
	{
		this.positionGradient = positionGradient;
		this.percentValue_X = percentValue_X;
		this.percenValue_Y = percenValue_Y;
		this.percentValue_Width = percentValue_Width;
		this.percentValue_Height = percentValue_Height;
		this.arcWidthAndHeight = arcWidthAndHeight;
		
		
		this.setOnMousePressed(e -> setNodeMouseEvent(Command.BUTTON_PRESSED, e));
		this.setOnMouseReleased(e -> setNodeMouseEvent(Command.BUTTON_RELEASED, e));
	}

	private void setNodeMouseEvent(Command commandValue, MouseEvent e) 
	{
		if(commandValue == Command.BUTTON_PRESSED)
		{
			LinearGradient gradient = null;
			if(positionGradient == PositionGradient.FROM_DOWN_TO_UP)
			{
				gradient = new LinearGradient(0, 0.3, 0, 1, true, CycleMethod.NO_CYCLE,
					    new Stop(0, Color.web("#5abaa0")),
					    new Stop(1, Color.web("#2d6c5b")));
			}
			else
			{
				gradient = new LinearGradient(0, 0, 0, 0.7, true, CycleMethod.NO_CYCLE,
					    new Stop(1, Color.web("#5abaa0")),
					    new Stop(0, Color.web("#2d6c5b")));
			}
			

			this.setFill(gradient);
		}
		else if(commandValue == Command.BUTTON_RELEASED)
		{
			this.setFill( Color.web("#5abaa0"));
		}
		
		commandProperty.set(commandValue);
		e.consume();
	}

	public SimpleObjectProperty<Command> getCommandProperty()
	{
		return commandProperty;
	}

	public void refreshSize(double width_component, double height_component) 
	{
		this.setX(width_component * percentValue_X);
		this.setY(height_component * percenValue_Y);
		this.setWidth(width_component * percentValue_Width);
		this.setHeight(height_component *  percentValue_Height);
		this.setArcWidth(width_component * arcWidthAndHeight);
		this.setArcHeight(width_component * arcWidthAndHeight);
		
	}
	
	
}
