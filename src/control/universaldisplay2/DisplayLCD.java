package control.universaldisplay2;

import control.dimmer.OptionalImageBox;
import control.dimmer.IActivationIcon.Pos;
import control.universaldisplay.SensorValue;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderRepeat;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import tools.helper.ImageLoader;
import tools.helper.NinePatchLoader;

public class DisplayLCD extends GridPane
{
	//private Label valueLabel, setpointLabel, presetLabel;
	
	private Label headlineValue, headlineSetpoint, headlinePreset;
	
	private OptionalImageBox optionalImageBox;
	
	/**
	 * value a.e. temperature, humidity
	 */
	private SensorValue majorValue;
	
	/**
	 * setpoint 
	 */
	private SensorValue minorValue;
	
	private Canvas presetCanvas, valueCanvas, setpointCanvas;
		
	public DisplayLCD()
	{
		super();
		
		this.initGraphics();
		this.registerListener();
		this.resize();
		
		
	}
	
	private void registerListener() {
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
		
	}

	private void resize() 
	{
		double w = this.getWidth();
		double h = this.getHeight();
		System.out.println("w " + w + " h " + h); 
	
		//352*169
		//w 100/352 * 169 = 0.4801136363636364
		//H 100/169 * 32 = 0.1893491124260355
		
		double canvasW = w * 0.4801136363636364;
		double canvasH = h * 0.1893491124260355;
		
		
		valueCanvas.setWidth(canvasW);
		valueCanvas.setHeight(canvasH);
		
		presetCanvas.setWidth(canvasW);
		presetCanvas.setHeight(canvasH);
		
		setpointCanvas.setWidth(canvasW);
		setpointCanvas.setHeight(canvasH);
		
		drawTextValues(true);
		drawSecondTextValue(true);
		
		
	}

	private void initGraphics() {
		this.setGridLinesVisible(true);
		Image image = NinePatchLoader.getNinePatchLoader("lcd_schwarz");
		
		//Breite des Randes ist abhängig von dem verwendeten Image
		BorderWidths regionWidth = new BorderWidths(8);
		//Die Stücke 
		BorderWidths sliceWidth = new BorderWidths(9);
		//Füllung der Darstellungsfläche
		boolean filled = true;
		//Streckung ind beiden Richtungen zulassen
		BorderRepeat repeatX = BorderRepeat.STRETCH;
		BorderRepeat repeatY = BorderRepeat.STRETCH;
		//Border Image aufbauen
		BorderImage bi = new BorderImage(image, regionWidth, new Insets(0), sliceWidth, filled, repeatX, repeatY);
		Border border = new Border(bi);
		
		//Uebernahme des Borders für diese VBox
		this.setBorder(border);
	
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHgrow(Priority.ALWAYS);
		
		col1.setPercentWidth(50);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(50);
		
		headlineValue = new Label("headlineValue");
		headlineSetpoint = new Label("headlineSetpoint");
		headlinePreset = new Label("headlinePreset");
		
		//valueLabel = new Label();
		//setpointLabel = new Label();
		//presetLabel = new Label();
		
		optionalImageBox = new OptionalImageBox();
		
		presetCanvas = new Canvas();
		valueCanvas =  new Canvas();
		setpointCanvas =  new Canvas();
		
		this.add(headlineValue, 1, 0);
		this.add(valueCanvas, 1, 1);
		
		this.add(headlinePreset, 0, 2);
		this.add(presetCanvas, 0, 3);
		
		this.add(headlineSetpoint, 1, 2);
		this.add(setpointCanvas, 1, 3);
		
		optionalImageBox = new OptionalImageBox();
		//links kommt das rotate zeichen
		   
		optionalImageBox.initImage(Pos.LEFT, ImageLoader.getImageFromIconFolder("img_rotation_a"));
		optionalImageBox.setDeactivation(Pos.LEFT);
		
		this.add(optionalImageBox, 0, 4);
		
		RowConstraints rc1 = new RowConstraints();
		rc1.setFillHeight(true);
		
		RowConstraints rc2 = new RowConstraints();
		rc2.setVgrow(Priority.ALWAYS);
		
		RowConstraints rc3 = new RowConstraints();
		
		RowConstraints rc4 = new RowConstraints();
		rc4.setVgrow(Priority.ALWAYS);
		
		RowConstraints rc5 = new RowConstraints();
		
		this.getRowConstraints().addAll(rc1, rc2, rc3, rc4, rc5);
		this.getColumnConstraints().addAll(col1, col2);
		
	}

	public void setMajorValue(SensorValue majorValue)
	{
		this.majorValue = majorValue;
	}

	public void setMinorValue(SensorValue minorValue)
	{
		this.minorValue = minorValue;
	}

	public void repaintValues() 
	{
		//TODO
		//this.setOpacityOfAdjustButton();
		this.drawTextValues(true);
		this.drawSecondTextValue(true);
		this.drawImages();
		
	}

	private void drawTextValues(boolean b) 
	{
		//TODO die size muss zu beginn schon stehen
		
		System.out.println("valueCanvas " + valueCanvas.getWidth());
		//valueCanvas.setWidth(50);
		//valueCanvas.setHeight(50);
		GraphicsContext gc = valueCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, valueCanvas.getWidth(), valueCanvas.getHeight());
		gc.setFill(Color.RED);
		gc.fillRect(0, 0, valueCanvas.getWidth(), valueCanvas.getHeight());
	
		
	}

	private void drawSecondTextValue(boolean b) 
	{
		
		GraphicsContext gc = presetCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, presetCanvas.getWidth(), presetCanvas.getHeight());
		gc.setFill(Color.BLUE);
		gc.fillRect(0, 0, presetCanvas.getWidth(), presetCanvas.getHeight());
	
		GraphicsContext gc2 = setpointCanvas.getGraphicsContext2D();
		gc2.clearRect(0, 0, setpointCanvas.getWidth(), setpointCanvas.getHeight());
		gc2.setFill(Color.GREEN);
		gc2.fillRect(0, 0, setpointCanvas.getWidth(), setpointCanvas.getHeight());
	
		
		
		
		
		//TODO
		//valueLabel.setText(String.format("%.1f", majorValue.getCurrentValue()));
		
	}

	private void drawImages() {
		// TODO Auto-generated method stub
		
	}



}
