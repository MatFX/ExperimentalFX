package canvas;

import java.nio.ByteBuffer;

import drag.cell.ImagePreviewCell;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tools.helper.ImageLoader;
import tools.helper.UIToolBox;

public class PixelWriterTest extends Application
{

	public static void main(String[] args) 
	{
		  Application.launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		ImagePreviewCell testCell = new ImagePreviewCell(ImageLoader.getImageFromIconFolder("hi_schloss.png"), "hi_schloss.png");
		
		Image imageTest = ImageLoader.getImageFromIconFolder("hi_schloss.png", 32, 32, false, false);
		double W = 32;
		double H = 32;
		
		Image scaledImage = UIToolBox.getScaledImage(testCell.getImage(), W, H);
		
		PixelReader pixelReader =   imageTest.getPixelReader();
		
		BorderPane borderPane = new BorderPane();
		borderPane.setStyle("-fx-background-color: #897542;");
		final Canvas canvas = new Canvas(500, 500);
		borderPane.setCenter(canvas);
	    
		
	  final PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
	  //  final WritablePixelFormat<ByteBuffer> byteBgraInstance = PixelFormat.getByteBgraInstance();
		
	    
	    for (int y = 0; y < H; y++) 
		{
			for (int x = 0; x < W; x++) 
			{
				Color color = pixelReader.getColor(x, y);
				if(color.getOpacity() != 1.0)
					System.out.println("color " + color.getOpacity());
				//Color blendedColor = Color.CORAL;
				
				Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
				pixelWriter.setColor(x, y, newColor);
			}
		}
	    
	    
	    
	   // pixelWriter.setPixels(0, 0, W, H, byteBgraInstance, pixels, 0, width * 4);
	 
	  
	    primaryStage.setScene(new Scene(borderPane));
        primaryStage.show();
	}

}
