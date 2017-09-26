package tools.helper.stop.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StopList implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1924013961473852428L;
	
	private List<StopValue> stop = new ArrayList<StopValue>();
	
	public void setStopList(List<StopValue> stopValueList)
	{
		this.stop = stopValueList;
	}
	
	public List<StopValue> getStopValueList()
	{
		return stop;
	}
}
