package control.universaldisplay;

import javafx.scene.paint.Paint;

public class PointXYR 
{
	private double x;
	
	private double y;
	
	private double r;
	
	private Paint fillPaint;
	
	public PointXYR(double x, double y, double r)
	{
		this.x = x;
		this.y = y;
		this.r = r;
	}

	public double getR() {
		return r;
	}

	public double getY() {
		return y;
	}

	public double getX() {
		return x;
	}
	
	public void setPaint(Paint fillPaint)
	{
		this.fillPaint = fillPaint;
	}
	
	public Paint getfillPaint()
	{
		return fillPaint;
	}
	
	public String toString()
	{
		return "x: " + x + " y: " + y +  " r: " + r;
	}

}
