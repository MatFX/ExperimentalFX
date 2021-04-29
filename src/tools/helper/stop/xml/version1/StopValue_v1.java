package tools.helper.stop.xml.version1;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import tools.helper.GenericPair;
import tools.helper.Tools;
import tools.helper.stop.interfaces.IStopValue;


@XmlAccessorType(XmlAccessType.FIELD)
public class StopValue_v1 implements Serializable, IStopValue
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
	 * style="stop-color:#FDFDFD"
	 * <br>oder
	 * <br>style="stop-color:#FFFFFF;stop-opacity:0.4"
	 */
	@XmlAttribute(name = "style")
	private String style;
	
	
	public StopValue_v1()
	{
		
	}
	
	public StopValue_v1(String val1, String val2) {
		this.offset = val1;
		this.style = val2;
	}
	
	public String getOffset()
	{
		return offset;
	}
	
	public String getStyle()
	{
		return style;
	}


	public String toString()
	{
		return "Stop "+ offset + " " + style;
	}

	@Override
	public String getFinalColorValue() 
	{
		String finalColorValue = "";
		String styleString = getStyle();
		int indexLastStyleTuple = styleString.indexOf("stop-opacity:");
		if(indexLastStyleTuple != -1)
		{
			
			//anschließend :
			//<stop  offset="0.2006963" style="stop-color:#B7B7B7;stop-opacity:0.4446974"/>
			String rawColor = "";
			String[] splitValue = styleString.split(";");
			GenericPair<String, String> pair = new GenericPair<String, String>(splitValue[0], splitValue[1]);
			
			//wir gehen davon aus, dass immer die Farbe 6 stellig ist (SVG kann auch anders!)
			String stopColorValue = pair.getLeft().split(":")[1];
			
			//die muss berechnet werdne
			String stopOpactiy = pair.getRight().split(":")[1];
			double percentValue = Double.parseDouble(stopOpactiy);
			percentValue = 255D * percentValue;
			
			int value = (int) Math.round(percentValue);
			
			String alphaHex = Tools.toHexString(value, false);
			System.out.println("alphaHex " + alphaHex);
			
			if(alphaHex != null && alphaHex.length() == 2)
			{
				finalColorValue  = stopColorValue + alphaHex;
			}
			//in dem Fall wird die opacity auf 00 gesetzt
			else
			{
				finalColorValue  = stopColorValue + "00";
				
			}
		}
		else
		{
			String[] splitValue = styleString.split(":");
			finalColorValue = splitValue[1];
		}
		return finalColorValue;
	}

	
	
}
