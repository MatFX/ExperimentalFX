package control.dimmer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import tools.helper.UIToolBox;

/**
 * Hier können bis zu drei Bilder abgelegt werden. Diese sind optional und müssen nicht befüllt werden
 * <br>The image container for the max. three pictures on the screen.
 * @author m.goerlich
 *
 */
public class OptionaImageBox extends HBox implements IActivationIcon
{
	
	private ImageView left;
	
	private ImageView right;
	
	private ImageView middle;
	
	//hier sind die Werte die bei der Blickdichtigkeit eingesetzt werden
	private final double DEACTIVATED = 0.1;
	
	private final double ACTIVATED = 0.6;
	
	
	public OptionaImageBox()
	{
		super();
		left = new ImageView();
		left.setOpacity(DEACTIVATED);
		left.setPreserveRatio(true);
	
		middle = new ImageView();
		middle.setOpacity(DEACTIVATED);
		middle.setPreserveRatio(true);
		
		right = new ImageView();
		right.setOpacity(DEACTIVATED);
		right.setPreserveRatio(true);
		
		this.getChildren().addAll(left, UIToolBox.createHorizontalSpacer(), middle, UIToolBox.createHorizontalSpacer(), right);
	}
	
	/**
	 * Die Methode ist einmalig auszuführen beim init auszulösen.
	 * <br>Kann aber auch dann angewendet werden, wenn man anstatt ACTIVATED/DEACTIVATED unterschiedliche Bilder hinterlegen will.
	 * @param position
	 * @param image
	 */
	public void initImage(Pos position, Image image)
	{
		//zur Zeit nur eine begrenzte Unterstützung in Abhängigkeit der Höhe
		if(image == null || image.getHeight() > 32)
			return;
		
		
		switch(position)
		{
			case LEFT:
				left.setImage(image);
				break;
			case MIDDLE:
				middle.setImage(image);
				break;
			case RIGHT:
				right.setImage(image);
				break;
		}
	}
	
	/**
	 * the height is the "master key" for the new size.
	 * @param heightValue
	 */
	public void resize(double heightValue)
	{
		
		left.setFitHeight(heightValue);
		middle.setFitHeight(heightValue);
		right.setFitHeight(heightValue);
	}
	
	public void setActivation(Pos position)
	{
		switch(position)
		{
			case LEFT:
				left.setOpacity(ACTIVATED);
				break;
			case MIDDLE:
				middle.setOpacity(ACTIVATED);
				break;
			case RIGHT:
				right.setOpacity(ACTIVATED);
				break;
		}
	}
	
	public void setDeactivation(Pos position)
	{
		switch(position)
		{
			case LEFT:
				left.setOpacity(DEACTIVATED);
				break;
			case MIDDLE:
				middle.setOpacity(DEACTIVATED);
				break;
			case RIGHT:
				right.setOpacity(DEACTIVATED);
				break;
		}
	}
	

}
