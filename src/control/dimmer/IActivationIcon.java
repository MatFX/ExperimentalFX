package control.dimmer;


import javafx.scene.image.Image;

/**
 * Eigene Schnisttelle, damit man die Anweisung für Bildwechsel oder Aktivierungswechsel nach außen weitergeben kann.
 * <br>Interface to change image oder activation/deactivation from a outer class/object.
 * @author m.goerlich
 *
 */
public interface IActivationIcon {

	//damit von außerhalb ein Bild an die gewünschte Stelle positioniert werden kann.
	/**
	 * Three positions for the images
	 * @author m.goerlich
	 *
	 */
	public enum Pos
	{
		LEFT,
		MIDDLE,
		RIGHT;
	}
	
	/**
	 * Ein Zustand ist eingenommen worden
	 * <br>Activation make the image darker.
	 * @param position
	 */
	public void setActivation(Pos position);
	
	/**
	 * Ein Zustand ist aufgehoben worden
	 * <br>Deactivation makes the image brighter.
	 * @param position
	 */
	public void setDeactivation(Pos position);
	
	/**
	 * Wechsel Bild an Pos x; Normalerweise einmalig kann aber auch für Bildwechsel verwendet werden.
	 * <br>Needed for initialisation and change the image at position x
	 * @param position
	 * @param image
	 */
	public void initImage(Pos position, Image image);
	
	
}
