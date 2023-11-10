package list;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import tools.helper.UIToolBox;

public class ListCellFactory extends ListCell<SampleItem>
{
	/**
	 * Soll den eigentlichen Inhalt überlagern.
	 */
	private HBox overlayContent;
	
	private HBox bottomContent;
	
	
	/**
	 * hier wird bottomContent und overlayContent abgelegt.
	 */
	private StackPane stackPane;
	
	private SWIPE swipe;
	
	private Label labelContainer;
	
	private SampleItem shownItem;

	private double sceneWidth, sceneHeight, startingPoint, movedPoint;
	
	private Cursor cursor;
	
 	private enum SWIPE
 	{
 		NO_DETECTION,
 		
 		SWIPE_LEFT,
 		
 		SWIPE_RIGHT;
 		
 	}
	
 	@Override
	protected void updateItem(SampleItem item, boolean empty)
	{
 		super.updateItem(item, empty);
		
		
		if(stackPane == null)
		{
			stackPane = new StackPane();
			
			
		
			
			bottomContent = new HBox(15);
			bottomContent.setMinHeight(25);
			bottomContent.setPadding(new Insets(5,5,5,5));
			bottomContent.setMaxWidth(Double.MAX_VALUE);
			
			
			overlayContent = new HBox(15);
			overlayContent.setMinHeight(25);
			overlayContent.setPadding(new Insets(5,5,5,5));
			labelContainer = new Label();
			overlayContent.getChildren().add(labelContainer);
			
			
			//Der StackPane die zwei verschiedenen Sichten hinzufügen
			stackPane.getChildren().add(0, bottomContent);
			//TODO rein 
			//stackPane.getChildren().add(1, overlayContent);
			
			
			
			overlayContent.setOnMousePressed(new EventHandler<MouseEvent>() 
			{

				@Override
				public void handle(MouseEvent event) {
					sceneWidth = overlayContent.getScene().getWidth();
					sceneHeight = overlayContent.getScene().getHeight();
					System.out.println("scene w: " + sceneWidth + " h: " + sceneHeight);
					cursor = Cursor.MOVE;
					//Punkt muss von der Scene genommen werden weil später das layoutx von HBoxContent angepasst wird.
					startingPoint = event.getSceneX();
				}
				
			});
			
			overlayContent.setOnMouseDragged(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) 
				{
					if(cursor == Cursor.MOVE)
					{
					
						movedPoint = event.getSceneX();
						
						double result;
						if(startingPoint > movedPoint)
						{
							swipe = SWIPE.SWIPE_LEFT;
							result = startingPoint - movedPoint;
						}
						else if(startingPoint < movedPoint)
						{
							swipe = SWIPE.SWIPE_RIGHT;
							result = movedPoint - startingPoint;
						}
						else
						{
							swipe = SWIPE.NO_DETECTION;
							event.consume();
							return;
						}
					
						double newPositon = 0;
						if(swipe == SWIPE.SWIPE_LEFT)
						{
							newPositon = overlayContent.getLayoutX() - result;
							
						}
						else if(swipe == SWIPE.SWIPE_RIGHT)
						{
							newPositon = overlayContent.getLayoutX() + result;
							
						}
						
						overlayContent.setTranslateX(newPositon);
						event.consume();
					}
				}
				
			});
			

			overlayContent.setOnMouseReleased(new EventHandler<MouseEvent>() 
			{

				@Override
				public void handle(MouseEvent event) 
				{

					if(cursor == Cursor.MOVE)
					{
						cursor = Cursor.NONE;
						swipe = SWIPE.NO_DETECTION;
						overlayContent.setTranslateX(0);
						
					}
			
				}
				
			});
		
		}
		
		if(item != null)
		{
			System.out.println("call Item " );
			this.shownItem = item;
			stackPane.setStyle("-fx-background-color: #00334d;");
			bottomContent.setStyle("-fx-background-color: #0088cc;");
			bottomContent.setPrefWidth(150D);
			bottomContent.setMinWidth(150D);
//
//			bottomContent.setMinWidth(UIToolBox.calculateWidthFromView(90, 500));
//			bottomContent.setMaxWidth(UIToolBox.calculateWidthFromView(92, 500));
//			bottomContent.setPrefWidth(UIToolBox.calculateWidthFromView(90, 500));
			
			
			overlayContent.setStyle("-fx-background-color: #66ccff;"); 
			Button minus = new Button("-");
			Button add = new Button("+");
			add.setAlignment(Pos.BASELINE_RIGHT);
			
			System.out.println("bottomContent " + bottomContent.getWidth() + " " + bottomContent.getHeight());
			
			
			bottomContent.getChildren().addAll(UIToolBox.createVerticalSpacer(), minus, add);
			labelContainer.setText(shownItem.getDescription());
			
	
			
		
		}

		setText(null);
		setGraphic(stackPane);
	}

}
