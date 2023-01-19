package glasspane;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GlassPaneSensor extends Region
{
	
	private Rectangle base_background_component, button_down, button_up;
	
	private Canvas textCanvas;
	
	private Canvas imageCanvas;
	
	
	private double w = 90;
	private double h = 100;

	public GlassPaneSensor()
	{
		this.initGraphics();
		this.registerListener();
	}
	
	private void initGraphics() 
	{
		base_background_component = new Rectangle();
		base_background_component.setFill(Color.TRANSPARENT);
		//TODO
		base_background_component.setStroke(Color.web("#5abaa0"));
		
		
		textCanvas = new Canvas();
		imageCanvas = new Canvas();
		
		button_down = new Rectangle();
		button_down.setFill(Color.web("#5abaa0"));
		
		button_up = new Rectangle();
		button_up.setFill(Color.web("#5abaa0"));

		this.getChildren().addAll(base_background_component, textCanvas, imageCanvas, button_down, button_up);
	}
	
	private void registerListener() 
	{
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
	}

	private void resize() {
		w = getWidth();
		h = getHeight();
		
		//w und h immer auf die volle zugewiesene Breite und Höhe
		base_background_component.setWidth(w);
		base_background_component.setHeight(h);
		//4px
		base_background_component.setArcWidth(w * 0.044444444444444446);
		base_background_component.setArcHeight(w * 0.044444444444444446);
		base_background_component.setStrokeWidth(w * 0.022222222222222223);
		
		//als erstes die gezeichnete Fläche leeren
		imageCanvas.getGraphicsContext2D().clearRect(imageCanvas.getLayoutX(), imageCanvas.getLayoutY(), imageCanvas.getWidth(), imageCanvas.getHeight());
		
		double newX = w * 0.23555555562222225;
		double newY = h * 0.07619787965000001;
		
		double newWidth = w * 0.5288888887333334;
		double newHeight = h * 0.27541007289999997;
		imageCanvas.setWidth(newWidth);
		imageCanvas.setHeight(newHeight);
		imageCanvas.relocate(newX, newY);
		
		
		//als erstes die gezeichnete Fläche leeren
		//TODO prüfen wegen dem Clearing eigentlich müsste es dohc funktionieren
		//textCanvas.getGraphicsContext2D().clearRect(textCanvas.getLayoutX(), textCanvas.getLayoutY(), textCanvas.getWidth(), textCanvas.getHeight());
		
		newX = w * 0.01666649998888889;
		newY = h * 0.38780583212999997;
		newWidth = w * 0.966667;
		newHeight = h * 0.51865887269;
		System.out.println("new Width " + newWidth + " newHeight " + newHeight);
		textCanvas.setWidth(newWidth);
		textCanvas.setHeight(newHeight);
		textCanvas.relocate(newX, newY);
		
		GraphicsContext gc = textCanvas.getGraphicsContext2D();
		
		
		drawTextValues(true);
		
		
		
		
		button_down.setX(w * 0.23555555562222225);
		button_down.setY(h * 0.9599999999800001);
		button_down.setWidth(w * 0.5288888887222222);
		button_down.setHeight(h * 0.040000000040000004);
		button_down.setArcWidth(w * 0.015406095722222221);
		button_down.setArcHeight(w * 0.015406095722222221);
		
		button_up.setX(w * 0.23555555563333336);
		button_up.setY(h * 0.0);
		button_up.setWidth(w * 0.5288888887222222);
		button_up.setHeight(h * 0.040000000040000004);
		button_up.setArcWidth(w * 0.015406095722222221);
		button_up.setArcHeight(w * 0.015406095722222221);
		
		
	}
	
	private void drawTextValues(boolean clearing) 
	{
		double w = textCanvas.getWidth();
		double h = textCanvas.getHeight();
		double x = textCanvas.getLayoutX();
		double y = textCanvas.getLayoutY();
		System.out.println("textCanvasDraw " + w + " h " + h);
		
		GraphicsContext gc = textCanvas.getGraphicsContext2D();
		//TODO prüfen ob hier oder weiter oben. Dieses ist dann zu vollziehen, wenn nur der Wert sich geändert hat.
		if(clearing)
		{
		
			gc.clearRect(0, 0, w, h);
		}
		//TODO raus nur für die Entwicklung
		gc.setFill(Color.ROSYBROWN);
		gc.fillRect(0, 0, w, h);
		
		
		
		
		gc.setFill(Color.BLACK);
		//Font masseinheitFont = new Font("Verdana", h * 0.12);
		Font masseinheitFont = new Font("Verdana", h * 0.5);
		
		Text textMasseinheit = new Text();
		
		textMasseinheit.setText("°C");
		textMasseinheit.setFont(masseinheitFont);
		
		gc.setFont(masseinheitFont);
		
		double masseinheitX = w - (textMasseinheit.getLayoutBounds().getWidth() + (w * 0.015635));
		
		//keien Ahnung wieso ich nicht den Mittelpunkt von H als Basis nehmen kann.
		double masseinheitY = textMasseinheit.getLayoutBounds().getHeight() -  (h * 0.015635);
		gc.fillText(textMasseinheit.getText(), masseinheitX, masseinheitY);
		
	
	}
}
