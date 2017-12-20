package control.universaldisplay;

import javafx.scene.layout.Region;

public class UniversalDisplay extends Region
{

	public UniversalDisplay()
	{

		this.initGraphics();
		this.registerListener();
	}

	private void registerListener() {
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
		
	}

	private void resize() {
		// TODO Auto-generated method stub
	}

	private void initGraphics() {
		// TODO Auto-generated method stub
		
	}


}
