package drag;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import drag.cell.ImagePreviewCell;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tools.helper.ImageLoader;
import tools.helper.UIToolBox;

public class TestPreviewCellDragNDrop extends Application
{
	private static final String BACKGROUND_IMAGE_ALLOCATION = "background_image_allocation";
	
	
	private static final DataFormat DataFormatImageAllocation = new DataFormat(BACKGROUND_IMAGE_ALLOCATION);
	

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		
		List<ImagePreviewCell> kompletteBilderListe = new ArrayList<ImagePreviewCell>();
		kompletteBilderListe.add(new ImagePreviewCell(ImageLoader.getImageFromIconFolder("hi_schloss.png"), "hi_schloss.png"));
		kompletteBilderListe.add(new ImagePreviewCell(ImageLoader.getImageFromIconFolder("hi_schulterZucken.png"), "hi_schulterZucken.png"));
		kompletteBilderListe.add(new ImagePreviewCell(ImageLoader.getImageFromIconFolder("hi_bewegung.png"), "hi_bewegung.png"));
		kompletteBilderListe.add(new ImagePreviewCell(ImageLoader.getImageFromIconFolder("hi_keineBewegung.png"), "hi_keineBewegung.png"));
		kompletteBilderListe.add(new ImagePreviewCell(ImageLoader.getImageFromIconFolder("hi_temp.png"), "hi_temp.png"));
		
		BorderPane contentPane = new BorderPane();
		
		
		GridPane gridPane = new GridPane();
		gridPane.setStyle("-fx-background-color: #897542;");
		
		gridPane.setOnDragDetected(new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event) {
				ArrayList<ImagePreviewCell> selectedCellList = new ArrayList<ImagePreviewCell>();
				
				for(int i = 0; i < gridPane.getChildren().size(); i++)
				{
					Node node = gridPane.getChildren().get(i);
					if(node instanceof ImagePreviewCell)
					{
						ImagePreviewCell previewCell = (ImagePreviewCell)node;
						if(previewCell.isSelected())
						{
							selectedCellList.add(previewCell);
						}
						
					}
				}
				
				System.out.println("selectedCellList " + selectedCellList.size());
				if(selectedCellList.size() > 0)
				{
					int anzahlDerVorschauBilder = selectedCellList.size();
					
					final double W = 32;
					final double H = 32;
					final double GAP = 5;
					
					double MAX_W = W;
					double MAX_H = H;
					
					if(selectedCellList.size() > 1)
					{
						MAX_W = W + (GAP * (anzahlDerVorschauBilder-1));
						MAX_H = H + (GAP * (anzahlDerVorschauBilder-1));
					}
					System.out.println("MAX_W " + MAX_W);
					System.out.println("MAX_H " + MAX_H);
			
					Canvas canvas = new Canvas(MAX_W, MAX_H);
				
					
					GraphicsContext gc = canvas.getGraphicsContext2D();
				
					gc.setFill(Color.TRANSPARENT);
					gc.fillRect(0, 0, MAX_W, MAX_H);
					//eigentliche Weitergabe für den drag transfer
					List<String> cellNamesList = new ArrayList<String>();
					
					double calculatedGap_w = MAX_W;
					double calculatedGap_h = MAX_H;
					
					
				//	ImageView testView = new ImageView();
					for(int i = selectedCellList.size()-1; i >= 0; i--)
					{
						//gc.setFill(Color.RED);
					//	testView.setImage(selectedCellList.get(i).getImage());
						
						//testView.setFitWidth(W);
						//testView.setFitHeight(H);
					
					//	System.out.println ("testimage " + testView.getImage().getWidth());
						
						
						//Image scaledImage = UIToolBox.getScaledImage(selectedCellList.get(i).getImage(), W, H);
						
						Image scaledImage = ImageLoader.getImageFromIconFolder(selectedCellList.get(i).getFileName(), 32, 32, false, true);
						
						//gc.drawRectangle(0,0, 32, 32);
						//gc.fillRect(0, 0, W,H);
						gc.drawImage(scaledImage, calculatedGap_w - 32d , calculatedGap_h - 32d);
						
						calculatedGap_w = calculatedGap_w - GAP;
						calculatedGap_h = calculatedGap_h - GAP;
					}
					
					
					
					
					//Reihenfolge wieder herstellen
					Collections.reverse(cellNamesList);
					
					
					Dragboard dragboard = gridPane.startDragAndDrop(TransferMode.MOVE);
					 SnapshotParameters param = new SnapshotParameters();
					 param.setFill(Color.TRANSPARENT);
					ClipboardContent content = new ClipboardContent();
					content.put(DataFormatImageAllocation, cellNamesList);
					dragboard.setDragView(canvas.snapshot(param, null));
					
					dragboard.setContent(content);
					
				}
				
				event.consume();
				
			}
			
		});
		

		
		int size = kompletteBilderListe.size();
		int rows = size / 3;
		if(size % 3 != 0)
			rows++;
		
		int columnIndex = 0;
		int rowIndex = 0;
		for(int i = 0; i < kompletteBilderListe.size(); i++)
		{
			gridPane.add(kompletteBilderListe.get(i), columnIndex, rowIndex);
			
			
			
			//Zeilenumbruch erforderlich?
			if(columnIndex == 2)
			{
				columnIndex = 0;
				rowIndex++;
			}
			else
				columnIndex++;
		}
		
		
		
		
		
		contentPane.setCenter(gridPane);
		
		primaryStage.setScene(new Scene(contentPane));
		primaryStage.setWidth(350);
		primaryStage.setHeight(350);
		primaryStage.show();
		
	}

}
