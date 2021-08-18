package colorized;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ColorizedImage extends ImageView
{
	
	private Image orginalImage;

	
	public ColorizedImage(Image imageFromIconFolder) 
	{
		this.orginalImage = imageFromIconFolder;
		
		int maxX = (int) orginalImage.getWidth();
		int maxY = (int) orginalImage.getHeight();
		
		PixelReader pixelReader =   orginalImage.getPixelReader();
		
		WritableImage writableImage = new WritableImage(maxX, maxY);
		PixelWriter pixelWriter = writableImage.getPixelWriter();
		
		for (int y = 0; y < maxY; y++) 
		{
			for (int x = 0; x < maxX; x++) 
			{
				Color color = pixelReader.getColor(x, y);
				System.out.println("color " + color.getOpacity());
				Color blendedColor = Color.CORAL;
				
				Color newColor = new Color(blendedColor.getRed(), blendedColor.getGreen(), blendedColor.getBlue(), color.getOpacity());
				
				
				
				pixelWriter.setColor(x, y, newColor);
			}
		}
		
		this.setImage(writableImage);
		
		
	}

}
