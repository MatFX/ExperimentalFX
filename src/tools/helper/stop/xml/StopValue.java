package tools.helper.stop.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
public class StopValue implements Serializable
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
	
	
	public StopValue()
	{
		
	}
	
	public StopValue(String val1, String val2) {
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
	
}
