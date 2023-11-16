package list.overlay;

import java.util.TreeMap;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;


public class SwipeBothOverlayPane extends OverlayContentPane
{

	public SwipeBothOverlayPane(SWIPE_USE swipeUse, TreeMap<Integer, Node> contentNodeTree)
	{
		super(swipeUse, contentNodeTree);
		
		this.setOnMouseDragged(new EventHandler<MouseEvent>() 
		{

			@Override
			public void handle(MouseEvent event) {
				
				if(SwipeBothOverlayPane.this.getCursorInfo() == Cursor.MOVE)
				{
					SwipeBothOverlayPane.this.setMovingPoint(event.getSceneX());
					
					double result = 0d;
					
					if(SwipeBothOverlayPane.this.getStartingPoint() > SwipeBothOverlayPane.this.getMovingPoint())
					{
						setSwipe(SWIPE.SWIPE_LEFT);
						result = SwipeBothOverlayPane.this.getStartingPoint() - SwipeBothOverlayPane.this.getMovingPoint();
					}
					else if(SwipeBothOverlayPane.this.getStartingPoint() < SwipeBothOverlayPane.this.getMovingPoint())
					{
						setSwipe(SWIPE.SWIPE_RIGHT);
						result = SwipeBothOverlayPane.this.getMovingPoint() - SwipeBothOverlayPane.this.getStartingPoint();
					
					}
					else
					{
						setSwipe(SWIPE.NO_DETECTION);
						event.consume();
						return;
					}
					

					double newPositon = 0;
					
					if(getSwipe() == SWIPE.SWIPE_LEFT && SwipeBothOverlayPane.this.getSwippedEndPosition() != SWIPE.SWIPE_RIGHT)
					{
						//Positionermittlung für Bewegung nach links aber nur wenn nicht die Endposition erreicht wurde
						if(!isMinOneNodeInOverlay(SWIPE.SWIPE_LEFT) && SwipeBothOverlayPane.this.getSwippedEndPosition() == SWIPE.NO_DETECTION)
						{
							newPositon = contentNodeTree.get(FIRST_RIGHT_CONTENT_INDEX).getLayoutX() - SwipeBothOverlayPane.this.getComponentGap() - SwipeBothOverlayPane.this.getWidth();
							SwipeBothOverlayPane.this.setSwippedEndPosition(SWIPE.SWIPE_LEFT);
						}
						else
							newPositon = SwipeBothOverlayPane.this.getLayoutX() - result;
					}
					//rechts und keine keine Endposition links (=> Ermittlung der Position )
					else if(getSwipe() == SWIPE.SWIPE_RIGHT && SwipeBothOverlayPane.this.getSwippedEndPosition() != SWIPE.SWIPE_LEFT)
					{
						if(!isMinOneNodeInOverlay(SWIPE.SWIPE_RIGHT))
						{
							newPositon = contentNodeTree.get(FIRST_LEFT_CONTENT_INDEX).getLayoutX() + contentNodeTree.get(FIRST_LEFT_CONTENT_INDEX).getLayoutBounds().getWidth()
									+ SwipeBothOverlayPane.this.getComponentGap();
							SwipeBothOverlayPane.this.setSwippedEndPosition(SWIPE.SWIPE_RIGHT);
						}
						else
							newPositon = result;
					}
					//nach rechts geht es nur wenn in links eingerastet wurde
					else if(SwipeBothOverlayPane.this.getSwipe() == SWIPE.SWIPE_RIGHT && SwipeBothOverlayPane.this.getSwippedEndPosition() == SWIPE.SWIPE_LEFT)
					{
						newPositon = SwipeBothOverlayPane.this.getLayoutX() + result;
						newPositon = 0;
						SwipeBothOverlayPane.this.setSwippedEndPosition(SWIPE.NO_DETECTION);
					}
					//nach links geht es nur wenn in rechts eingerastet wurde
					else if(SwipeBothOverlayPane.this.getSwipe() == SWIPE.SWIPE_LEFT && SwipeBothOverlayPane.this.getSwippedEndPosition() == SWIPE.SWIPE_RIGHT)
					{
						newPositon = SwipeBothOverlayPane.this.getLayoutX() + result;
						newPositon = 0;
						SwipeBothOverlayPane.this.setSwippedEndPosition(SWIPE.NO_DETECTION);
					}
					
					SwipeBothOverlayPane.this.setTranslateX(newPositon);
					event.consume();
					
					
					if(SwipeBothOverlayPane.this.getSwippedEndPosition() != SWIPE.NO_DETECTION)
					{
						SwipeBothOverlayPane.this.setCursorInfo(Cursor.NONE);
						SwipeBothOverlayPane.this.setSwipe(SWIPE.NO_DETECTION);
					}
					
				}
				
				
				
			}
			
		});
		
		this.setOnMouseReleased(new EventHandler<MouseEvent>() 
		{

			@Override
			public void handle(MouseEvent event) {


				if(SwipeBothOverlayPane.this.getCursorInfo() == Cursor.MOVE && SwipeBothOverlayPane.this.getSwippedEndPosition() == SWIPE.NO_DETECTION)
				{
					SwipeBothOverlayPane.this.setCursorInfo(Cursor.NONE);
					SwipeBothOverlayPane.this.setSwipe(SWIPE.NO_DETECTION);
					SwipeBothOverlayPane.this.setTranslateX(0);
				}
				
			}
			
		});
		
		
		
	}

}
