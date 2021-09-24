package transparent;

import java.net.MalformedURLException;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tools.helper.CSSContainer;
import tools.helper.ResourceLoader;

public class TransparentExample extends Application
{
	public static void main(String[] args) 
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		TransparentBorderPane tbp = new TransparentBorderPane(primaryStage);
		
		Scene scene = new Scene(tbp, 400,400);
		
		List<CSSContainer> cssList = ResourceLoader.getGlobalCSSContainerList();
		CSSContainer toSetCSS=null;
		
		for(int i = 0; i < cssList.size(); i++)
		{
			System.out.println("cssList " + cssList.get(i).getForView());
			if(cssList.get(i).getForView().contains("udp"))
			{
				toSetCSS = cssList.get(i);
				break;
			}
			
		}
		
		
		if(toSetCSS != null)
		{
			System.out.println("setze");
			scene.getStylesheets().setAll(toSetCSS.getUrl().toExternalForm());
		}

     
		

		primaryStage.setOpacity(0.5);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

}
