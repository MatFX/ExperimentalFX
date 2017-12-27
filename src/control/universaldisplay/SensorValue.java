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
	
	/**
	 * Zusätzliches Feld für die Bildbezeichnung die auch zur Anzeige kommmt
	 */
	private String imageName = "";
	
	private double[] presetValues = null;
	
	
	public SensorValue(double currentValue, double von, double bis, String measurementUnit, String imageName, double[] presetValues)
	{
		this(currentValue, von, bis, measurementUnit, imageName);
		this.presetValues = presetValues;
	}
	
	
	
	public SensorValue(double currentValue, double von, double bis, String measurementUnit, String imageName)
	{
		this.currentValue = currentValue;
		this.von = von;
		this.bis = bis;
		this.measurementUnit = measurementUnit;
		this.imageName = imageName;
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
	
	public String getImageBezeichnung()
	{
		return this.imageName;
	}
	
	public double getStepValue()
	{
		return stepping;
	}
	
	public boolean hasPresetValues()
	{
		if(presetValues != null && presetValues.length > 0)
			return true;
		else
			return false;
	}
}
