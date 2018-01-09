package tools.helper;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ResourceLoader 
{
	/*
	 * Wer sich die stylsheet Laderei bei fx ausgedacht hat, gehört auch eine watschn.
	 */
	private static final String GLOBAL_RESOURCE_DESTINATION = "\\resources\\";
	
	
	/**
	 * globale Resourcen für das Grundlayout
	 */
	private static final String GLOBAL_CSS_RESOURCE_DESTINATION = "\\resources\\global\\css\\";
	
	private static final String PREFIX_FILE = "file:\\";
	
	
	
	public static URL getGlobalResource(String cssFileName) throws MalformedURLException
	{
		File file = new File(GLOBAL_CSS_RESOURCE_DESTINATION+cssFileName);
		String complete = "file:/"+file.getAbsolutePath();
		return  ClassLoader.getSystemResource(complete);
	}
	
	public static ObservableList<CSSContainer> getGlobalCSSContainerList() throws MalformedURLException 
	{
		List<CSSContainer> cssList = new ArrayList<CSSContainer>();
		//War das schon immer so bescheiden, mit der Setzung eines Pfades?
		File file = new File("");
		file = new File(file.getAbsolutePath()+GLOBAL_CSS_RESOURCE_DESTINATION);
		
		File[] fileArray = file.listFiles();
		for(int i = 0; i < fileArray.length; i++)
		{
			String absPath = fileArray[i].getAbsolutePath();
			if(absPath.endsWith(".css"))
			{
				int firstIndex = absPath.lastIndexOf("\\");
				String splitValue = absPath.substring(firstIndex+1, absPath.length());
				if(splitValue.endsWith(".css"))
				{
					String forView = splitValue.replace(".css", "");
					cssList.add(new CSSContainer(forView, new URL(PREFIX_FILE+absPath)));
				}
			}
		}
		return FXCollections.observableArrayList(cssList);
	}
	
	public static URL getResourceProperty(String resourceProperty) throws MalformedURLException
	{
		
		File file = new File(GLOBAL_RESOURCE_DESTINATION+resourceProperty);
		String complete = "file:/"+file.getAbsolutePath();
		return  ClassLoader.getSystemResource(complete);
	}

}
