package list;

import java.util.TreeMap;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import list.overlay.OverlayContentPane;
import list.overlay.OverlayContentPane.*;
import tools.helper.UIToolBox;

public class SecondListCellFactory extends ListCell<SampleItem>
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
	
	private Button minus, add, left_minus, right_add;
	
	/**
	 * node locked in the end position.
	 */
	private SWIPE swippedEndPosition = SWIPE.NO_DETECTION;

	
	private SWIPE_USE swipeUse = SWIPE_USE.SWIPE_BOTH_ALLOWED;
	
	
 	public SecondListCellFactory(SWIPE_USE swipeUse) 
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
			
			TreeMap<Integer, Node> contentNodeTree = new TreeMap<Integer, Node>();

			minus = new Button("-");
			add = new Button("+");
			
			left_minus = new Button("-");
			right_add = new Button("+");
			
			
			if(this.swipeUse == SWIPE_USE.SWIPE_LEFT_ALLOWED  || this.swipeUse == SWIPE_USE.SWIPE_BOTH_ALLOWED)
			{
				contentNodeTree.put(1, minus);
				contentNodeTree.put(2, add);
			}
			else if(this.swipeUse == SWIPE_USE.SWIPE_RIGHT_ALLOWED  || this.swipeUse == SWIPE_USE.SWIPE_BOTH_ALLOWED)
			{
				contentNodeTree.put(-1, left_minus);
				contentNodeTree.put(-2, right_add);
			}
			
			
			
		
			overlayContent = OverlayContentPane.getInstance(swipeUse, contentNodeTree);
			overlayContent.setSpacing(15);
			overlayContent.setMinHeight(25);
			overlayContent.setPadding(new Insets(5,5,5,5));
			labelContainer = new Label();
			overlayContent.getChildren().add(labelContainer);
			
			
			//Der StackPane die zwei verschiedenen Sichten hinzufügen
			stackPane.getChildren().add(0, bottomContent);
			stackPane.getChildren().add(1, overlayContent);
		
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
