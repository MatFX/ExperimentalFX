package tools.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.scene.image.Image;

public class ImageLoader 
{
	
	public static final String ICONS = "/resources/icons";
	
	public static final String SUFFIX_FILE = ".png";
	
	public static InputStream getResourceStream(String pkname, String fname) throws FileNotFoundException
	{
		String resname = "" + pkname + "/" + fname;
		File file = new File("");
		
		file = new File(file.getAbsoluteFile() + resname);
		InputStream inputStream = new FileInputStream(file.getAbsolutePath());
	
		return inputStream;
	
	
	}
	
	public static Image getImageFromIconFolder(String fileName)
	{
		Image image = null;
		
		if(!fileName.contains(ImageLoader.SUFFIX_FILE))
			fileName = fileName + ImageLoader.SUFFIX_FILE;
		try
		{
			InputStream ins = ImageLoader.getResourceStream(ICONS, fileName);
			image = new Image(ins);
			ins.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return image;
	}

	public static Image getImageFromIconFolder(String fileName, double newW, double newH, boolean preservationRatio, boolean smooth)
	{
		Image image = null;
		
		if(!fileName.contains(ImageLoader.SUFFIX_FILE))
			fileName = fileName + ImageLoader.SUFFIX_FILE;
		try
		{
			InputStream ins = ImageLoader.getResourceStream(ICONS, fileName);
			image = new Image(ins, newW, newH, preservationRatio, smooth);
			ins.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return image;
	}


}
