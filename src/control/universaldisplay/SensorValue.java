package control.universaldisplay;


/**
 * helper class to store and change values
 * @author m.goerlich
 *
 */
public class SensorValue
{
	private double von;
	
	private double bis;
	
	private double currentValue;
	
	private String measurementUnit;

	/**
	 * ist nur interresant bei Veränderung durch Anwender
	 */
	private double stepping = 0.5;
	
	public SensorValue(double currentValue, double von, double bis, String measurementUnit)
	{
		this.currentValue = currentValue;
		this.von = von;
		this.bis = bis;
		this.measurementUnit = measurementUnit;
	}

	public double getVon() {
		return von;
	}

	public void setVon(double von) {
		this.von = von;
	}

	public double getBis() {
		return bis;
	}

	public void setBis(double bis) {
		this.bis = bis;
	}

	public double getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}

	public String getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}
}
