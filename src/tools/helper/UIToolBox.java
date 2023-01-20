package tools.helper;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class UIToolBox 
{
	public static Node createHorizontalSpacer() 
	{
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}
	
	public static Node createVerticalSpacer()
	{
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		return spacer;
	}
	
	public static Bounds textWidth(double size, String valueToShow, Font fontBase)
	{
		//hier muss die bounds aufgebaut werden anhand der zwei darzustellenden Werte 
		if(fontBase == null)
		{
			fontBase = Font.font("Verdana", FontWeight.BOLD, 12);
		
		}
		Text text = new Text(valueToShow);
		Font font =  Font.font(fontBase.getFamily(), FontWeight.BOLD, size);
        text.setFont(font);
		return text.getBoundsInLocal();
	}
	
	public static Bounds getMaxTextWidth(Font font, String textToShow) 
	{
		Text valTextMax = new Text(textToShow);
		valTextMax.setFont(font);
		Bounds valMaxBounds = valTextMax.getBoundsInLocal();
		return new BoundingBox(0,0, valMaxBounds.getWidth(), valMaxBounds.getHeight());
	}
	
	public static double getGreaterFont(double fontSize, double w, double h, String valueToShow, double gapPercent, Font font)
	{	
		double gapBreite = w * gapPercent * 2;
		double gapHoehe = h * gapPercent * 2;
		
		fontSize = fontSize + 1;
		Bounds futureBounds = textWidth(fontSize, valueToShow, null);
		if((futureBounds.getHeight() + gapHoehe) < h && (futureBounds.getWidth() + gapBreite) < w)
		{
			return UIToolBox.getGreaterFont(fontSize, w, h, valueToShow, gapPercent, font);
		}
		//eines wieder zurück weil die Abfrage nicht gegriffen hat
		return fontSize-1;
	}


	public static double getLesserFont(double fontSize, double w, double h, String valueToShow, double gapPercent, Font font)
	{	
		Bounds futureBounds = textWidth(fontSize, valueToShow, font);
		double gapBreite = w * gapPercent * 2;
		double gapHoehe = h *gapPercent * 2;
		
		//wenn eines von beiden über das Ziel hinaus ist, so ist eine kleiner Fontgröße zu ermitteln
		if((futureBounds.getHeight() + (gapHoehe)) > h || (futureBounds.getWidth() + (gapBreite)) > w)
		{
			fontSize = fontSize - 1;
			
			if(fontSize <= 0)
				return 1;
			return UIToolBox.getLesserFont(fontSize, w, h, valueToShow, gapPercent, font);
		}
		return fontSize;
	}
	
	/**
	 * Calculate the ratio 
	 * @param baseValue
	 * @param partValue
	 * @return
	 */
	public static double getPointRatio(double baseValue, double partValue)
	{
		return 100D / baseValue * partValue / 100D;
	}
	
	/**
	 * Calculate the ratio from a radius
	 * @param rectangle_w
	 * @param rectangle_h
	 * @param circle_radius
	 * @return
	 */
	public static double getAreaRatio(double rectangle_w, double rectangle_h, double circle_radius)
	{
		return ((100D/(rectangle_w * rectangle_h)) * (Math.pow(circle_radius, 2) * Math.PI)) / 100D;
	}

	/**
	 * revert calculation from ratio to the new radius after resizing
	 * @param rectangle_w
	 * @param rectangle_h
	 * @param radius_ratio
	 * @return
	 */
	public static double getRadiusFromRatio(double rectangle_w, double rectangle_h, double radius_ratio)
	{
		//calculation the new area from the circle
		double circle_area = (rectangle_w * rectangle_h) * radius_ratio;
		//calc the new radius from the circle area
		double radius = Math.sqrt(circle_area / Math.PI);
		return radius;
	}

	//test scaling
	public static Image getScaledImage(Image imageFx, double w, double h)
	{
		ImageView imageView = new ImageView(imageFx);
		imageView.setFitWidth(w);
		imageView.setFitHeight(h);
		imageView.setPreserveRatio(false);
		return imageView.snapshot(null, null);
	}
	
	public static Image getColorizedImage(Image imageToRecolor, Color newColor) 
	{
		
		int maxX = (int) imageToRecolor.getWidth();
		int maxY = (int) imageToRecolor.getHeight();
		
		PixelReader pixelReader =   imageToRecolor.getPixelReader();
		
		WritableImage writableImage = new WritableImage(maxX, maxY);
		PixelWriter pixelWriter = writableImage.getPixelWriter();
		
		for (int y = 0; y < maxY; y++) 
		{
			for (int x = 0; x < maxX; x++) 
			{
				Color color = pixelReader.getColor(x, y);
				Color blendedColor = new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), color.getOpacity());
				pixelWriter.setColor(x, y, blendedColor);
			}
		}
		
		return writableImage;
		
		
	}
}
