package control.universaldisplay;

import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 * simple highlighter for the top side of the circle.
 * @author m.goerlich
 *
 */
public class TopRegion extends Region
{
	private Shape toShow;
	
	private Circle backgroundCircle;
	
	private Circle overlaySubtract;
	
	private Circle overlaySubtract2;
	
	private PointXYR mainCoords, pointXYRSub1, pointXYRSub2;
	
	public TopRegion(Circle backgroundCircle, Circle overlaySubtract, Circle overlaySubtract2)
	{
		this.backgroundCircle = backgroundCircle;
		this.overlaySubtract = overlaySubtract;
		this.overlaySubtract2 = overlaySubtract2;
		//this.resize();
	}
	
	public void setNewValues(PointXYR mainCoords, PointXYR pointXYRSub1, PointXYR  pointXYRSub2)
	{
		this.mainCoords = mainCoords;
		this.pointXYRSub1 = pointXYRSub1;
		this.pointXYRSub2 = pointXYRSub2;
	}
	
	public void resize()
	{
		
		if(this.getChildren().size() == 1)
			this.getChildren().remove(0);
		
		/*
		if(this.getChildren().size() > 0)
		{
			this.getChildren().remove(2);
			this.getChildren().remove(1);
			this.getChildren().remove(0);
			
		}*/
		
		
		backgroundCircle.setCenterX(mainCoords.getX());
		backgroundCircle.setCenterY(mainCoords.getY());
		backgroundCircle.setRadius(mainCoords.getR());
		//backgroundCircle.setFill(Color.RED);
		
		overlaySubtract.setCenterX(pointXYRSub1.getX());
		overlaySubtract.setCenterY(pointXYRSub1.getY());
		overlaySubtract.setRadius(pointXYRSub1.getR());
		//overlaySubtract.setFill(Color.BISQUE);
		
		overlaySubtract2.setCenterX(pointXYRSub2.getX());
		overlaySubtract2.setCenterY(pointXYRSub2.getY());
		overlaySubtract2.setRadius(pointXYRSub2.getR());
		//overlaySubtract2.setFill(Color.BLUE);
		
		toShow = Shape.subtract(backgroundCircle, overlaySubtract);
		toShow = Shape.subtract(toShow, overlaySubtract2);
		//Farbe noch setzen vom backgroundcircle
		//TODO
		toShow.setFill(mainCoords.getfillPaint());
		//toShow.setFill(Color.web("#FF0000"));
		this.getChildren().add(toShow);
		//this.getChildren().addAll(backgroundCircle, overlaySubtract, overlaySubtract2);
		
	}

}
