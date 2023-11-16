package list.overlay;

import java.util.TreeMap;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;


public class SwipeLeftOverlayPane extends OverlayContentPane
{
	public SwipeLeftOverlayPane(SWIPE_USE swipeUse, TreeMap<Integer, Node> contentNodeTree)
	{
		super(swipeUse, contentNodeTree);
		this.setOnMouseDragged(new EventHandler<MouseEvent>() 
		{

			@Override
			public void handle(MouseEvent event) 
			{
				
				if(SwipeLeftOverlayPane.this.getCursorInfo() == Cursor.MOVE)
				{
					SwipeLeftOverlayPane.this.setMovingPoint(event.getSceneX());
					
					double result = 0d;
					if(SwipeLeftOverlayPane.this.getStartingPoint() > SwipeLeftOverlayPane.this.getMovingPoint())
					{
						setSwipe(SWIPE.SWIPE_LEFT);
						result = SwipeLeftOverlayPane.this.getStartingPoint() - SwipeLeftOverlayPane.this.getMovingPoint();
					}
					else if(SwipeLeftOverlayPane.this.getStartingPoint() < SwipeLeftOverlayPane.this.getMovingPoint())
					{
						SwipeLeftOverlayPane.this.setSwipe(SWIPE.SWIPE_RIGHT);
					}
					else
					{
						setSwipe(SWIPE.NO_DETECTION);
						event.consume();
						return;
					}
				
					double newPositon = 0;
					if(getSwipe() == SWIPE.SWIPE_LEFT)
					{
						
						if(!isMinOneNodeInOverlay(SWIPE.SWIPE_LEFT))
						{
							newPositon = contentNodeTree.get(FIRST_RIGHT_CONTENT_INDEX).getLayoutX() - SwipeLeftOverlayPane.this.getComponentGap() - SwipeLeftOverlayPane.this.getWidth();
							SwipeLeftOverlayPane.this.setSwippedEndPosition(SWIPE.SWIPE_LEFT);
						}
						else
							newPositon = SwipeLeftOverlayPane.this.getLayoutX() - result;
						
					}
					//Nach rechts gehts nur wenn vorher nach Links geswipped wurde
					else if(SwipeLeftOverlayPane.this.getSwipe() == SWIPE.SWIPE_RIGHT && SwipeLeftOverlayPane.this.getSwippedEndPosition() == SWIPE.SWIPE_LEFT)
					{
						
						newPositon = SwipeLeftOverlayPane.this.getLayoutX() + result;
						newPositon = 0;
						SwipeLeftOverlayPane.this.setSwippedEndPosition(SWIPE.NO_DETECTION);
					}
					
					SwipeLeftOverlayPane.this.setTranslateX(newPositon);
					event.consume();
					
					if(SwipeLeftOverlayPane.this.getSwippedEndPosition() != SWIPE.NO_DETECTION)
					{
						SwipeLeftOverlayPane.this.setCursorInfo(Cursor.NONE);
						SwipeLeftOverlayPane.this.setSwipe(SWIPE.NO_DETECTION);
					}
				}
				
				
			}
		});
		
		this.setOnMouseReleased(new EventHandler<MouseEvent>() 
		{

			@Override
			public void handle(MouseEvent event) 
			{
				

				if(SwipeLeftOverlayPane.this.getCursorInfo() == Cursor.MOVE && SwipeLeftOverlayPane.this.getSwippedEndPosition() == SWIPE.NO_DETECTION)
				{
					SwipeLeftOverlayPane.this.setCursorInfo(Cursor.NONE);
					SwipeLeftOverlayPane.this.setSwipe(SWIPE.NO_DETECTION);
					SwipeLeftOverlayPane.this.setTranslateX(0);
				}
		
			}
			
		});
	}

	
	

}
