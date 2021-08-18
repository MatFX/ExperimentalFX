package colorized;



import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tools.helper.ImageLoader;

public class TestColorizedImage extends Application
{
	

	public static void main(String[] args) 
	{
		Application.launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		GridPane gridPane = new GridPane();
		
		ImageView imageView = new ImageView(ImageLoader.getImageFromIconFolder("electrical_services_black"));
		
		gridPane.add(imageView, 0, 0);
		
		ColorizedImage newView = new ColorizedImage(ImageLoader.getImageFromIconFolder("electrical_services_black"));
		gridPane.add(newView, 1, 0);
		
		
		Scene scene = new Scene(gridPane);

		primaryStage.setTitle("Image Colorizer");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}

}
