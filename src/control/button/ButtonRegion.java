package control.button;

import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class ButtonRegion extends Region
{
	private Rectangle baseRect;
	
	private Rectangle toSubtract;
	
	private PointXYWHARC pointBase;
	
	private PointXYWHARC pointToSubtract;
	
	private Shape toShow;
	
	public ButtonRegion(Rectangle baseRect, Rectangle toSubtract)
	{
		this.baseRect = baseRect;
		this.toSubtract = toSubtract;
	}
	
	public void setNewValues(PointXYWHARC mainCoords, PointXYWHARC pointSub1)
	{
		this.pointBase = mainCoords;
		this.pointToSubtract = pointSub1;
	}
	
	public void resize()
	{
		
		if(this.getChildren().size() == 1)
			this.getChildren().remove(0);
		
		baseRect.setX(pointBase.getX());
		baseRect.setY(pointBase.getY());
		baseRect.setWidth(pointBase.getW());
		baseRect.setHeight(pointBase.getH());
		baseRect.setArcWidth(pointBase.getArcW());
		baseRect.setArcHeight(pointBase.getArcH());
		
		toSubtract.setX(pointToSubtract.getX());
		toSubtract.setY(pointToSubtract.getY());
		toSubtract.setWidth(pointToSubtract.getW());
		toSubtract.setHeight(pointToSubtract.getH());
		toSubtract.setArcWidth(pointToSubtract.getArcW());
		toSubtract.setArcHeight(pointToSubtract.getArcH());
		
	
	
		
		toShow = Shape.subtract(baseRect, toSubtract);
		
		//Farbe immer von main
		toShow.setFill(pointBase.getFillPaint());
		//baseRect.setFill(pointBase.getFillPaint());
		this.getChildren().add(toShow);
	}
	
	
	public Shape getCuttedShape()
	{
		return toShow;
	}
	
	
	
	
	
	
	
	
}
