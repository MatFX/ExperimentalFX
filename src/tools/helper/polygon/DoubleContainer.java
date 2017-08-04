package tools.helper.polygon;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * Datencontaienr für die Werte die in der Tabelle angezeigt und sich dynamisch verändern
 * @author m.goerlich
 *
 */
public class DoubleContainer 
{
	/**
	 * 
	 */
	private SimpleDoubleProperty xValue = new SimpleDoubleProperty();
	
	private SimpleDoubleProperty yValue = new SimpleDoubleProperty();

	public DoubleContainer()
	{
		
	}
	
	public DoubleContainer(double newXValue, double newYValue)
	{
		System.out.println("neuerwert " + newXValue + " " + newYValue);
		xValue.set(newXValue);
		yValue.set(newYValue);
	}

	//TODO tutorial? getxValue wird nicht akzeptiert unbedingt groß schreiben
	//TestValue testValue gleiches Phänomän
	public Double getXValue() {
		return xValue.get();
	}

	public void setXValue(double xDoubleValue) {
		this.xValue.set(xDoubleValue);
	}

	
	public Double getYValue() {
		return yValue.get();
	}

	public void setYValue(double yDoubleValue) {
		this.yValue.set(yDoubleValue);
	}
	
	public SimpleDoubleProperty getDoubleXProperty()
	{
		return xValue;
	}
	
	public SimpleDoubleProperty getDoubleYProperty()
	{
		return yValue;
	}
	
	
}
