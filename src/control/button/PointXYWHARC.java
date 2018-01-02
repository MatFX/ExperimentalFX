package control.button;

import javafx.scene.paint.Paint;

public class PointXYWHARC
{
	private double x;
	
	private double y;
	
	private double w;
	
	private double h;
	
	private double arcW;
	
	private double arcH;
	
	private Paint colorToPaint;
	
	public PointXYWHARC(double x, double y, double w, double h, double arcW, double arcH)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.arcH = arcH;
		this.arcW = arcW;
		
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public double getArcW() {
		return arcW;
	}

	public void setArcW(double arcW) {
		this.arcW = arcW;
	}

	public double getArcH() {
		return arcH;
	}

	public void setArcH(double arcH) {
		this.arcH = arcH;
	}
	
	public void setPaint(Paint colorToPaint)
	{
		this.colorToPaint = colorToPaint;
	}
		
	public Paint getFillPaint() {
		return colorToPaint;
	}

	
	
}
