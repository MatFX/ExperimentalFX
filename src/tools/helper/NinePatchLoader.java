package tools.helper;

import java.io.InputStream;
import javafx.scene.image.Image;

public class NinePatchLoader 
{
	/**
	 * Nine patch Ordner für die Bilder
	 */
	private static final String GLOBAL_NINEPATCH_ICONS = "/resources/ninepatch/";
	
	public static Image getNinePatchLoader(String fileName)
	{
		Image image = null;
		
		if(!fileName.contains(ImageLoader.SUFFIX_FILE))
			fileName = fileName + ImageLoader.SUFFIX_FILE;
		try
		{
			InputStream ins = ImageLoader.getResourceStream(GLOBAL_NINEPATCH_ICONS, fileName);
			image = new Image(ins);
			ins.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return image;
	}

}
