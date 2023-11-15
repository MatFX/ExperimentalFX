package list;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
	
	private Button minus, add;
	
	/**
	 * node locked in the end position.
	 */
	private SWIPE swippedEndPosition = SWIPE.NO_DETECTION;

	
	private SWIPE_USE swipeUse = SWIPE_USE.SWIPE_BOTH_ALLOWED;
	
	
	/**
	 * 
	 * which swipe gesture is allowed 
	 */
	public enum SWIPE_USE
	{
		SWIPE_LEFT_ALLOWED,
		
		SWIPE_RIGHT_ALLOWED,
		
		SWIPE_BOTH_ALLOWED,
		
	}
	
	
 	public enum SWIPE
 	{
 		NO_DETECTION,
 		
 		SWIPE_LEFT,
 		
 		SWIPE_RIGHT;
 		
 	}
	
 	public ListCellFactory(SWIPE_USE swipeUse) 
 	{
 		this.swipeUse = swipeUse;
 		//the swipeUse create the handler for mouseDragged and so on
	}

	@Override
	protected void updateItem(SampleItem item, boolean empty)
	{
 		super.updateItem(item, empty);
		
		
		if(stackPane == null)
		{
			System.out.println("AUFRUF stackpane");
			stackPane = new StackPane();
				
			bottomContent = new HBox(15);
			bottomContent.setMinHeight(25);
			bottomContent.setPadding(new Insets(5,5,5,5));
			bottomContent.setMaxWidth(Double.MAX_VALUE);
			
		
			minus = new Button("-");
			add = new Button("+");
		
			
			overlayContent = new HBox(15);
			overlayContent.setMinHeight(25);
			overlayContent.setPadding(new Insets(5,5,5,5));
			labelContainer = new Label();
			overlayContent.getChildren().add(labelContainer);
			
			
			//Der StackPane die zwei verschiedenen Sichten hinzufügen
			stackPane.getChildren().add(0, bottomContent);
			stackPane.getChildren().add(1, overlayContent);
			
			
			
			overlayContent.setOnMousePressed(new EventHandler<MouseEvent>() 
			{

				@Override
				public void handle(MouseEvent event) {
					

						
						//sceneWidth = overlayContent.getScene().getWidth();
						//sceneHeight = overlayContent.getScene().getHeight();
						//System.out.println("scene w: " + sceneWidth + " h: " + sceneHeight);
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
							if(!overlayContent.getBoundsInParent().contains(minus.getBoundsInParent())
									&& !overlayContent.getBoundsInParent().contains(add.getBoundsInParent())) 
							{
								newPositon = minus.getLayoutX() - 10 - overlayContent.getWidth();
								swippedEndPosition = SWIPE.SWIPE_LEFT;
								
							}
							else
								newPositon = overlayContent.getLayoutX() - result;
							
						}
						//Nach rechts gehts nur wenn vorher nach Links geswipped wurde
						else if(swipe == SWIPE.SWIPE_RIGHT && swippedEndPosition == SWIPE.SWIPE_LEFT)
						{
							newPositon = overlayContent.getLayoutX() + result;
							newPositon = 0;
							swippedEndPosition = SWIPE.NO_DETECTION;
						}
						
						overlayContent.setTranslateX(newPositon);
						event.consume();
						
						if(swippedEndPosition != SWIPE.NO_DETECTION)
						{
							cursor = Cursor.NONE;
							swipe = SWIPE.NO_DETECTION;
						}
					}
				}
				
			});
			

			overlayContent.setOnMouseReleased(new EventHandler<MouseEvent>() 
			{

				@Override
				public void handle(MouseEvent event) 
				{

					if(cursor == Cursor.MOVE && swippedEndPosition == SWIPE.NO_DETECTION)
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
			
			overlayContent.setStyle("-fx-background-color: #66ccff;"); 
			
			add.setAlignment(Pos.BASELINE_RIGHT);
			
			System.out.println("bottomContent " + bottomContent.getWidth() + " " + bottomContent.getHeight());
			//wird benötigt für Gluon da funktioniert das Rendering anscheinend anders
			if(!bottomContent.getChildren().contains(minus) && !bottomContent.getChildren().contains(add))
				bottomContent.getChildren().addAll(UIToolBox.createHorizontalSpacer(), minus, add);
		
			labelContainer.setText(shownItem.getDescription());
		}

		setText(null);
		setGraphic(stackPane);
	}

}
