package sevensegment.single;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class DecimalPoint extends Region
{
	private Rectangle background;
	
	private Color BACKGROUND = Color.web("#cdcdcd");
	
	private Rectangle decimalPoint;
	
	public DecimalPoint()
	{
		this.setWidth(8.6);
		//nicht kleiner!
		this.setMinWidth(2);
		
		this.setHeight(64);
		//TODO interfaces und resize auch so
		this.initGraphics();
		this.registerListener();
	}

	private void registerListener() {
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
	}

	public void resize()
	{
		this.drawDecimalPoint();
		
	}
	private void drawDecimalPoint() {
		
	
		//8.6
		double w = this.getWidth();
		System.out.println("Width bei Decimal " + w);
		//64
		double h = this.getHeight();
		
		//ist 1:1 mit dem kompletten Gebilde
		//width="8.6" height="64"
		background.setX(0D);
		background.setY(0D);
		background.setWidth(w);
		background.setHeight(h);
		background.setFill(BACKGROUND);
		
		
		//x = 100/8.6 * 1.3 = 0,1511627906976744
		//y = 100/64 * 56.9 = 0,8890625
		//w = 100/8.6 * 6 = 0,6976744186046512
		//h = 100/64 * 6 = 0,09375
		
		decimalPoint.setX(w * 0.1511627906976744);
		decimalPoint.setY(h * 0.8890625);
		decimalPoint.setWidth(w * 0.6976744186046512);
		decimalPoint.setHeight(h * 0.09375);
	}

	private void initGraphics() 
	{
		//hier die kompletten Inits
		background = new Rectangle();
		decimalPoint = new Rectangle();


		this.drawDecimalPoint();
		
		//alles auf die Oberfläche ablegen
		this.getChildren().addAll(background, decimalPoint);
	}

	public void setBACKGROUNDColor(Color colorBackground) 
	{
		BACKGROUND = colorBackground;
		
	}

}
