package drag.cell;

import java.io.Serializable;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;



/**
 * Diese Vbox stellt ein Vorschaubild mit Text für die Gridpane bereit.
 * @author m.goerlich
 *
 */
public class ImagePreviewCell extends VBox implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6349015938135354904L;

	/**
	 * Nicht jede Zelle ist zukünftig selektierbar, deswegen ein weiterer Wert der von außen gesetzt werden kann
	 */
	private boolean isSelectAble = true;
	
	/**
	 * Selektiert ist diese Bild dann wenn der Anwender es per Mausklick ausgewählt hat,
	 * <br>dann ist dieses auch einer Gruppe zuweisbar/entfernbar.
	 */
	private SimpleBooleanProperty isSelected = new SimpleBooleanProperty();
	
	private Image image;
	
	private Canvas canvas;
	
	/**
	 * Dieser boolean ist dann gesetzt wenn die Zelle selektiert ist und auch mit der selektierten Gruppe verbunden ist.
	 */
	private boolean isZugewiesen = false;
	
	private Label imageBezeichnung;
	
	/**
	 * Verkleinertes Vorschaubild
	 */
	private ImageView imageView;
	
	/**
	 * Diese Box kann von einer erweiterten Klasse manipuliert werden. In dieser befinden sich die Anzeigewerte.
	 */
	protected VBox verticalBox;
	
	private String dateiName;

	
	public ImagePreviewCell(Image bild, String text)
	{
		this.image = bild;
		
		//Diese Vbox besteht aus dem verkleinerten Bild/ImageView
		//und einem label mit der Bezeichnung
		verticalBox = new VBox(1);
		verticalBox.setAlignment(Pos.CENTER);
		verticalBox.setPadding(new Insets(5,5,5,5));
	
		
		StackPane stackPane = new StackPane();
		
		imageView = new ImageView();
		imageView.setImage(bild);
		
		
		
		imageView.setFitWidth(64);
		imageView.setFitHeight(64);
		
		canvas = new Canvas();
		canvas.setWidth(64);
		canvas.setHeight(64);
		
		
		verticalBox.setOnMousePressed(new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event) 
			{
				//Ist eine Selektierbarkeit überhaupt möglich
				if(isSelectAble)
				{
					//Selektion wurde vom Anwender angefordert
					setSelected(!isSelected.get());
				}
				
			}
		
		});
		
		stackPane.getChildren().add(0, imageView);
		stackPane.getChildren().add(1, canvas);
		
		imageBezeichnung = new Label();
		imageBezeichnung.setId("label_directory");
		imageBezeichnung.setTextFill(Color.web("#FFFFFF"));
		
		dateiName = text;
		//im Tooltip ist immer die volle Bezeichnung hinterlegt
		imageBezeichnung.setTooltip(new Tooltip(dateiName));
		
	
		if(dateiName.length() > 16)
		{
			dateiName = dateiName.substring(0, 5) + "..." + dateiName.substring(dateiName.length()-8 , dateiName.length());
		}
		
		imageBezeichnung.setText(dateiName);
		
		
		verticalBox.getChildren().addAll(stackPane, imageBezeichnung);
		
		this.getChildren().add(verticalBox);
		
		
	}
	
	
	public void setSelected(boolean isSelected)
	{
		this.isSelected.set(isSelected);
		if(isSelected)
			drawRectangle(canvas.getGraphicsContext2D());
		else
			clearRectangle(canvas.getGraphicsContext2D());
	}
	
	public boolean isSelected()
	{
		return isSelected.get();
	}
	
	public void drawRectangle(GraphicsContext gc)
	{
		gc.setStroke(Color.RED);
		double strokeWidth = 5;
		gc.setLineWidth(strokeWidth);
		gc.strokeRect(0+(strokeWidth/2), 0+(strokeWidth/2), canvas.getWidth()-(strokeWidth), canvas.getHeight()-(strokeWidth));
	}
	
	public void clearRectangle(GraphicsContext gc)
	{
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}


	public boolean isZugewiesen() {
		return isZugewiesen;
	}


	public void setZugewiesen(boolean isZugewiesen) {
		
		this.isZugewiesen = isZugewiesen;
		if(isZugewiesen)
		{
			//TODO schriftfarbe ändern
			DropShadow ds = new DropShadow();
			ds.setOffsetY(1.0f);
			ds.setColor(Color.color(0, 0, 0));
			imageBezeichnung.setTextFill(Color.web("#FF0000"));
			imageBezeichnung.setEffect(ds);
			imageView.setEffect(ds);
		}
		else
		{
			//TODO schriftfarbe wieder auf weiß setzen
			imageBezeichnung.setTextFill(Color.web("#FFFFFF"));
			imageBezeichnung.setEffect(null);
			imageView.setEffect(null);
		}
	}
	
	/**
	 * Das ist die komplette Dateibezeichnung die hier zurückkommt.
	 * @return
	 */
	public String getName()
	{
		return dateiName;
	}
	
	/**
	 * Die Breite wird vorgegeben und die Hoehe passt sich im Verhaeltnis dazu an.
	 * @param gewuenschteBreite
	 */
	public void useVerhaeltnis(double gewuenschteBreite)
	{
		
			
		
		double verhaeltnisWertBreite = image.getHeight() / image.getWidth();
		double hoeheGewuenscht = gewuenschteBreite * verhaeltnisWertBreite;
		this.imageView.setFitWidth(gewuenschteBreite);
		this.imageView.setFitHeight(hoeheGewuenscht);
		canvas.setWidth(gewuenschteBreite);
		canvas.setHeight(hoeheGewuenscht);
		
	}


	public boolean isSelectAble() {
		return isSelectAble;
	}


	public void setSelectAble(boolean isSelectAble) {
		this.isSelectAble = isSelectAble;
	}
	
	/**
	 * Damit kann ich von außerhalb lauschen ob diese Zelle selektiert worden ist.
	 * @return
	 */
	public SimpleBooleanProperty getSelectionProperty()
	{
		return isSelected;
	}
	
	public Image getImage()
	{
		return image;
	}
	


	
	

}
