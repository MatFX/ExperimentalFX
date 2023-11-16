package list.overlay;

import java.util.TreeMap;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import list.overlay.OverlayContentPane.SWIPE;



public class SwipeRightOverlayPane extends OverlayContentPane
{

	public SwipeRightOverlayPane(SWIPE_USE swipeUse, TreeMap<Integer, Node> contentNodeTree)
	{
		super(swipeUse,  contentNodeTree);
		
		this.setOnMouseDragged(new EventHandler<MouseEvent>() 
		{

			@Override
			public void handle(MouseEvent event) {
				
				if(SwipeRightOverlayPane.this.getCursorInfo() == Cursor.MOVE)
				{
					SwipeRightOverlayPane.this.setMovingPoint(event.getSceneX());
					
					double result = 0d;
					if(SwipeRightOverlayPane.this.getStartingPoint() > SwipeRightOverlayPane.this.getMovingPoint())
					{
						setSwipe(SWIPE.SWIPE_LEFT);

						
					}
					else if(SwipeRightOverlayPane.this.getStartingPoint() < SwipeRightOverlayPane.this.getMovingPoint())
					{
						System.out.println("Rechtsbewegung");
						SwipeRightOverlayPane.this.setSwipe(SWIPE.SWIPE_RIGHT);
						result = SwipeRightOverlayPane.this.getMovingPoint() - SwipeRightOverlayPane.this.getStartingPoint();
						System.out.println("result " + result);
					}
					else
					{
						setSwipe(SWIPE.NO_DETECTION);
						event.consume();
						return;
					}
					double newPositon = 0;
					
					if(SwipeRightOverlayPane.this.getSwipe() == SWIPE.SWIPE_RIGHT)
					{
						System.out.println(">> " + !isMinOneNodeInOverlay(SWIPE.SWIPE_RIGHT));
						if(!isMinOneNodeInOverlay(SWIPE.SWIPE_RIGHT))
						{
							
							newPositon = contentNodeTree.get(FIRST_LEFT_CONTENT_INDEX).getLayoutX() + contentNodeTree.get(FIRST_LEFT_CONTENT_INDEX).getLayoutBounds().getWidth()
									+ SwipeRightOverlayPane.this.getComponentGap();
							SwipeRightOverlayPane.this.setSwippedEndPosition(SWIPE.SWIPE_RIGHT);
							
							
						}
						else
							newPositon = result;
						
					}
					//Nach rechts gehts nur wenn vorher nach Links geswipped wurde
					else if(SwipeRightOverlayPane.this.getSwipe() == SWIPE.SWIPE_LEFT && SwipeRightOverlayPane.this.getSwippedEndPosition() == SWIPE.SWIPE_RIGHT)
					{
						newPositon = SwipeRightOverlayPane.this.getLayoutX() + result;
						newPositon = 0;
						SwipeRightOverlayPane.this.setSwippedEndPosition(SWIPE.NO_DETECTION);
					}
					
					SwipeRightOverlayPane.this.setTranslateX(newPositon);
					event.consume();
					
					if(SwipeRightOverlayPane.this.getSwippedEndPosition() != SWIPE.NO_DETECTION)
					{
						SwipeRightOverlayPane.this.setCursorInfo(Cursor.NONE);
						SwipeRightOverlayPane.this.setSwipe(SWIPE.NO_DETECTION);
					}
					
				}
				
				
				
				
			}
			
		});
		
		this.setOnMouseReleased(new EventHandler<MouseEvent>() 
		{

			@Override
			public void handle(MouseEvent event) {

				if(SwipeRightOverlayPane.this.getCursorInfo() == Cursor.MOVE && SwipeRightOverlayPane.this.getSwippedEndPosition() == SWIPE.NO_DETECTION)
				{
					SwipeRightOverlayPane.this.setCursorInfo(Cursor.NONE);
					SwipeRightOverlayPane.this.setSwipe(SWIPE.NO_DETECTION);
					SwipeRightOverlayPane.this.setTranslateX(0);
				}
				
			}
			
		});
	}

}
