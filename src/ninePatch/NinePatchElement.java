package ninePatch;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderRepeat;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import tools.helper.NinePatchLoader;

public class NinePatchElement extends GridPane
{
	public NinePatchElement()
	{
		super();
		Image image = NinePatchLoader.getNinePatchLoader("lcd_schwarz");
		
		//Breite des Randes ist abhängig von dem verwendeten Image
		BorderWidths regionWidth = new BorderWidths(8);
		//Die Stücke 
		BorderWidths sliceWidth = new BorderWidths(9);
		//Füllung der Darstellungsfläche
		boolean filled = true;
		//Streckung ind beiden Richtungen zulassen
		BorderRepeat repeatX = BorderRepeat.STRETCH;
		BorderRepeat repeatY = BorderRepeat.STRETCH;
		//Border Image aufbauen
		BorderImage bi = new BorderImage(image, regionWidth, new Insets(0), sliceWidth, filled, repeatX, repeatY);
		Border border = new Border(bi);
		
		//Uebernahme des Borders für diese VBox
		this.setBorder(border);
		
	}

}
