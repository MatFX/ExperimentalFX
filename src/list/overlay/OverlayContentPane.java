package list.overlay;

import java.util.TreeMap;
import java.util.Map.Entry;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;


public abstract class OverlayContentPane extends HBox
{
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
	
	
	private Cursor cursor;
	
	private double startingPoint;
	
	private double movingPoint;
	
	private int componentGap = 10;
	
	/**
	 * current swipe
	 */
	private SWIPE swipe;
	
	/**
	 * node locked in the end position.
	 */
	private SWIPE swippedEndPosition = SWIPE.NO_DETECTION;

	
	private SWIPE_USE swipeUse = SWIPE_USE.SWIPE_BOTH_ALLOWED;
	
	protected TreeMap<Integer, Node> contentNodeTree;
	
	protected int FIRST_RIGHT_CONTENT_INDEX = 1;
	
	protected int FIRST_LEFT_CONTENT_INDEX = -1;
	
	protected OverlayContentPane(SWIPE_USE swipeUse, TreeMap<Integer, Node> contentNodeTree)
	{
		this.swipeUse = swipeUse;
		this.contentNodeTree = contentNodeTree;
		this.setOnMousePressed(new EventHandler<MouseEvent>() 
		{

			@Override
			public void handle(MouseEvent event) {
					cursor = Cursor.MOVE;
					//Punkt muss von der Scene genommen werden weil später das layoutx von HBoxContent angepasst wird.
					startingPoint = event.getSceneX();
			}
		});
	}
	

	public static OverlayContentPane getInstance(SWIPE_USE swipeUse, TreeMap<Integer, Node> contentNodeTree)
	{
		switch(swipeUse)
		{
			case SWIPE_BOTH_ALLOWED:
				return new SwipeBothOverlayPane(swipeUse, contentNodeTree);
				
			case SWIPE_LEFT_ALLOWED:
				return new SwipeLeftOverlayPane(swipeUse, contentNodeTree);
			
			case SWIPE_RIGHT_ALLOWED:
				return new SwipeRightOverlayPane(swipeUse, contentNodeTree);
		}
		return null;
	}
		
	public Cursor getCursorInfo() {
		return cursor;
	}


	public double getStartingPoint() {
		return startingPoint;
	}


	public double getMovingPoint() {
		return movingPoint;
	}


	public SWIPE getSwippedEndPosition() {
		return swippedEndPosition;
	}


	public SWIPE_USE getSwipeUse() {
		return swipeUse;
	}


	public void setStartingPoint(double startingPoint) {
		this.startingPoint = startingPoint;
	}


	public void setMovingPoint(double movingPoint) {
		this.movingPoint = movingPoint;
	}


	public void setSwippedEndPosition(SWIPE swippedEndPosition) {
		this.swippedEndPosition = swippedEndPosition;
	}


	public void setSwipeUse(SWIPE_USE swipeUse) {
		this.swipeUse = swipeUse;
	}


	public SWIPE getSwipe() {
		return swipe;
	}


	public void setSwipe(SWIPE swipe) {
		this.swipe = swipe;
	}


	public int getComponentGap() {
		return componentGap;
	}


	public void setComponentGap(int componentGap) {
		this.componentGap = componentGap;
	}
	

	public void setCursorInfo(Cursor cursor) 
	{
		this.cursor = cursor;
		
	}
	
	/**
	 * Every node in the bottom pane is in the overlay, wenn the overlay has the layout x coordinate 0
	 * @param swipePosition 
	 * @return
	 */
	protected boolean isMinOneNodeInOverlay(SWIPE toSwipePosition) 
	{
		//if to left the nodes with positive keys are needed
		
		//if to right the nodes with negative keys are needed to check
		
		for(Entry<Integer, Node> entry : contentNodeTree.entrySet())
		{
			if(entry.getKey() > 0 && toSwipePosition == SWIPE.SWIPE_LEFT )
			{
				if(getBoundsInParent().contains(entry.getValue().getBoundsInParent()))
				{
					return true;
				}
			}
			else if(entry.getKey() < 0 && toSwipePosition == SWIPE.SWIPE_RIGHT )
			{
				if(getBoundsInParent().contains(entry.getValue().getBoundsInParent()))
				{
					return true;
				}
			}
			
		}
		return false;
	}

}
