package control.dimmer;


import javafx.scene.image.Image;

/**
 * Eigene Schnisttelle, damit man die Anweisung für Bildwechsel oder Aktivierungswechsel nach außen weitergeben kann.
 * @author m.goerlich
 *
 */
public interface IActivationIcon {

	//damit von außerhalb ein Bild an die gewünschte Stelle positioniert werden kann.
	public enum Pos
	{
		LEFT,
		MIDDLE,
		RIGHT;
	}
	
	/**
	 * Ein Zustand ist eingenommen worden
	 * @param position
	 */
	public void setActivation(Pos position);
	
	/**
	 * Ein Zustand ist aufgehoben worden
	 * @param position
	 */
	public void setDeactivation(Pos position);
	
	/**
	 * Wechsel Bild an Pos x; Normalerweise einmalig kann aber auch für Bildwechsel verwendet werden.
	 * @param position
	 * @param image
	 */
	public void initImage(Pos position, Image image);
	
	
}
