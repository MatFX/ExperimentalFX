package control.temperature;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TemperatureControl extends Region
{
	
	private Circle backgroundCircle;
	
	
	private Rectangle roundedRectangle;
	
	private double centerX, centerY;
	
	
	public TemperatureControl()
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
		backgroundCircle = new Circle();
		backgroundCircle.setFill(Color.web("#000000"));
		
		
		roundedRectangle = new Rectangle();
		roundedRectangle.setFill(Color.web("#000000"));
	
		this.getChildren().addAll(backgroundCircle, roundedRectangle);
	}


	private void resize() 
	{
		
		double size  = getWidth() < getHeight() ? getWidth() : getHeight();
		
		if(getHeight() > 0)
			centerY = getHeight() / 2d;
		
		if(getWidth() > 0)
			centerX = getWidth() / 2d;
		
		//double radius = size / 2d;
		
		//127 x 127 gesamtgröße der Fläche
		//kreis background war bei x 62,035, y 25,625 r = 51,25/2 => 25,625
		
		
		//r 100/127 * 25,625 = 20,17716535433071 = 0.2017716535433071
		//x 100/127 * 62,035 = 48,84645669291339 = 0.4884645669291339
		//y 100/127 * 101,375 px = 79,82283464566929 = 0.7982283464566929
		
		
		backgroundCircle.setRadius(size * 0.2017716535433071); 
		backgroundCircle.setCenterX(size * 0.4884645669291339);
		backgroundCircle.setCenterY(size * 0.7982283464566929);
		
		//x 48,6 px px, y 0, b 38,26771653543307 px, 0.38,26771653543307
		
		//x 100/127 * 48,6 px = 48,84645669291339 = 0.4884645669291339
		//y = 0;
		//b 100/127 * 26,871 = 21,15826771653543 = 0.2115826771653543
		//h 100/127 * 112,802 = 88,82047244094488 = 0.8882047244094488
		
		roundedRectangle.setX(size * 0.3826771653543307);
		roundedRectangle.setY(0);
		roundedRectangle.setWidth(size * 0.2115826771653543);
		roundedRectangle.setHeight(size * 0.8882047244094488);
		//gleiche wie die Breite bei beiden nehmen
		roundedRectangle.setArcHeight(size * 0.2115826771653543);
		roundedRectangle.setArcWidth(size * 0.2115826771653543);
	
	
	}

}
