package list.overlay;

import java.util.TreeMap;

import javafx.scene.Node;
import list.ListCellFactory.SWIPE_USE;

public class SwipeBothOverlayPane extends OverlayContentPane
{

	public SwipeBothOverlayPane(SWIPE_USE swipeUse, TreeMap<Integer, Node> contentNodeTree)
	{
		super(swipeUse, contentNodeTree);
		
	}

}
