package control.dimmer;

import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class TopRegion extends Region
{
	private Shape toShow;
	
	private Circle backgroundCircle;
	
	private Circle overlaySubtract;
	
	/**
	 * Dieser wird von außerhalb übergeben und muss mit jedem resize auf den Kreisen neu gesetzt werden.
	 */
	private double radius;
	
	private double centerX, centerY, verschiebungYOverlay;
	
	
	public TopRegion(Circle backgroundCircle, Circle overlaySubtract)
	{
		this.backgroundCircle = backgroundCircle;
		this.overlaySubtract = overlaySubtract;
		
		
		
		this.resize();
	}
	
	public void setNewValues(double centerX, double centerY, double radius, double verschiebungYOverlay)
	{
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.verschiebungYOverlay = verschiebungYOverlay;
	}
	
	public void resize()
	{
		
		if(this.getChildren().size() == 1)
			this.getChildren().remove(0);
		
		backgroundCircle.setCenterX(centerX);
		backgroundCircle.setCenterY(centerY);
		backgroundCircle.setRadius(radius);
		

		overlaySubtract.setCenterX(centerX);
		overlaySubtract.setCenterY(centerY + verschiebungYOverlay);
		overlaySubtract.setRadius(radius);
		
		
		toShow =  Shape.subtract(backgroundCircle, overlaySubtract);
		//Farbe noch setzen vom backgroundcircle
		toShow.setFill(backgroundCircle.getFill());
		this.getChildren().add(toShow);
		
		
		
	}

}
