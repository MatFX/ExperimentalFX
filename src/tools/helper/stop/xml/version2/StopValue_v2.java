package tools.helper.stop.xml.version2;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import tools.helper.Tools;
import tools.helper.stop.interfaces.IStopValue;

/**
 * With my new Illustrator Version I have a different export file as in the past.
 * @author m.goerlich
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StopValue_v2 implements Serializable, IStopValue
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5401778360374945763L;

	/**
	 * offset="0.6054054"
	 */
	@XmlAttribute(name = "offset")
	private String offset;
	
	/**
	 * stop-color="#8e8e8d"
	 */
	@XmlAttribute(name = "stop-color")
	private String stop_color;
	
	/**
	 * stop-opacity="0.8"
	 */
	@XmlAttribute(name = "stop-opacity")
	private String stop_opacity;
	
	
	public StopValue_v2()
	{
		
	}
	
	public String getOffset()
	{
		return offset;
	}
	
	public String getStopColor()
	{
		return this.stop_color;
	}
	
	public String getStopOpacity()
	{
		return this.stop_opacity;
	}


	public String toString()
	{
		return "StopValue offset "+ offset + " stop-color " + stop_color + " stop-opacity " + stop_opacity;
	}

	@Override
	public String getFinalColorValue() 
	{
		String finalColorValue = "";
		
		String sixDigitColor = Tools.getSixDigitColor(stop_color);
		
		if(stop_opacity != null && stop_opacity.length() > 0)
		{
			
			
			
			//String stopColorValue
			double percentValue = Double.parseDouble(stop_opacity);
			percentValue = 255D * percentValue;
			
			int value = (int) Math.round(percentValue);
			
			String alphaHex = Tools.toHexString(value, false);
			if(alphaHex != null && alphaHex.length() == 2)
			{
				finalColorValue  = sixDigitColor + alphaHex;
			}
			//in dem Fall wird die opacity auf 00 gesetzt
			else
			{
				finalColorValue  = sixDigitColor + "00";
				
			}
			
		}
		else
		{
			finalColorValue = sixDigitColor;
		}
		
		
		
		
		
		
		
		return finalColorValue;
	}
	
}
