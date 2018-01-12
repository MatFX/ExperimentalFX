package control.universaldisplay2;

import control.dimmer.OptionalImageBox;
import control.dimmer.IActivationIcon.Pos;
import control.universaldisplay.SensorValue;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableDoubleValue;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
	
	
	private Font fontBase = null;
	
	private DoubleProperty scaleableFontSize = null;
	
	private static final double GAP_PERCENT = 0.1;
	
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

	private void initGraphics() 
	{
		fontBase = new Font("Verdana",12);
		scaleableFontSize = new SimpleDoubleProperty(12);
		
		
		//TODO raus
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
		
	//	col1.setPercentWidth(50);
		ColumnConstraints col2 = new ColumnConstraints();
	//	col2.setPercentWidth(50);
		
		headlineValue = new Label("headlineValue");
		headlineSetpoint = new Label("headlineSetpoint");
		headlinePreset = new Label("headlinePreset");
		
		//valueLabel = new Label();
		//setpointLabel = new Label();
		//presetLabel = new Label();
		
		
		
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
		rc2.prefHeightProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				System.out.println("newValue " + newValue.toString());
				
			}
			
		});
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

	private void drawTextValues(boolean clearing) 
	{
		//TODO die size muss zu beginn schon stehen
	
		double initalSize =  getWidth() > getHeight() ? getHeight() : getWidth();
		
		double w = valueCanvas.getWidth();
		double h = valueCanvas.getHeight();
		//valueCanvas.setWidth(50);
		//valueCanvas.setHeight(50);
		GraphicsContext gc = valueCanvas.getGraphicsContext2D();
		
		Font font = Font.font(fontBase.getName(), scaleableFontSize.get());
		//Dieses ist dann zu vollziehen, wenn nur der Wert sich geändert hat.
		if(clearing)
		{
		
			gc.clearRect(0, 0, w, h);
		}
		
		//TODO raus
		gc.setFill(Color.RED);
		gc.fillRect(0, 0, valueCanvas.getWidth(), valueCanvas.getHeight());
			
		//text color
		gc.setFill(Color.web("#00000080"));
		
		if(majorValue != null)
		{
			//initial
			 //Ermittlung nach dem maximal möglichen Zustand
			 Bounds maxTextAbmasse = this.getMaxTextWidth(font, this.majorValue);
			 if(maxTextAbmasse.getWidth() < w  && maxTextAbmasse.getHeight() < h)
			 {
				 double tempSize = getGreaterFont(initalSize * 0.12, w, h, majorValue);
					if(tempSize != getFontSize().get())
						getFontSize().set(tempSize);
				 
			 }
			 else
			 {
				 double tempSize = getLesserFont(getFontSize().get(), w, h, majorValue);
					if(tempSize != getFontSize().get())
						getFontSize().set(tempSize);
			 }
			 font = Font.font(fontBase.getName(), getFontSize().get());
		}
		gc.setFont(font);
		
		Text measuringUnit = new Text();
		if(majorValue == null)
			measuringUnit.setText("");
		else
			measuringUnit.setText(" "+majorValue.getMeasurementUnit());
		measuringUnit.setFont(font);
		
		double masseinheitX = w - (measuringUnit.getLayoutBounds().getWidth());// + (gaugeSize * 0.015635));
		
		double haelfte =  measuringUnit.getLayoutBounds().getHeight() / 2d;
		double masseinheitY = h/2d +  (haelfte/2d);
		gc.fillText(measuringUnit.getText(), masseinheitX, masseinheitY);
			
		
		Text valueField = new Text();
		
		if(majorValue == null)
			valueField.setText("");
		else
			valueField.setText(String.format("%.1f", majorValue.getCurrentValue()));
		valueField.setFont(font);
		
		double valueX = masseinheitX - (valueField.getLayoutBounds().getWidth());//  + (gaugeSize * 0.015635));
		double valueY = masseinheitY;
		
		gc.setFont(font);
		gc.fillText(valueField.getText(), valueX, valueY);
		
	}

	public DoubleProperty getFontSize()
	{
		return scaleableFontSize;
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

	

	protected double getGreaterFont(double fontSize, double w, double h, SensorValue sensorValue)
	{	
		double gapBreite = w * getGAPPercent() * 2;
		double gapHoehe = h * getGAPPercent() * 2;
		
		fontSize = fontSize + 1;
		Bounds futureBounds = textWidth(fontSize, sensorValue);
		if((futureBounds.getHeight() + gapHoehe) < h && (futureBounds.getWidth() + gapBreite) < w)
		{
			return getGreaterFont(fontSize, w, h, sensorValue);
		}
		//eines wieder zurück weil die Abfrage nicht gegriffen hat
		return fontSize-1;
	}

	protected double getLesserFont(double fontSize, double w, double h, SensorValue sensorValue)
	{	
		Bounds futureBounds = textWidth(fontSize, sensorValue);
		double gapBreite = w * getGAPPercent() * 2;
		double gapHoehe = h * getGAPPercent() * 2;
		//wenn eines von beiden über das Ziel hinaus ist, so ist eine kleiner Fontgröße zu ermitteln
		if((futureBounds.getHeight() + (gapHoehe)) > h || (futureBounds.getWidth() + (gapBreite)) > w)
		{
			fontSize = fontSize - 1;
			if(fontSize <= 0)
				return 1;
			return getLesserFont(fontSize, w, h, sensorValue);
		}
		return fontSize;
	}
	
	private Bounds textWidth(double size, SensorValue sensorValue)
	{
		//hier muss die bounds aufgebaut werden anhand der zwei darzustellenden Werte 
		
		String showValue = sensorValue.getCurrentValue() + " " + sensorValue.getMeasurementUnit();
		
		if(fontBase == null)
			fontBase = new Font("Verdana", 12);
		Text text = new Text(showValue);
		Font font =  Font.font(fontBase.getFamily(), size);
        text.setFont(font);
        return text.getBoundsInLocal();
	}
	

	public double getGAPPercent()
	{
		return GAP_PERCENT;
	}
	
	private Bounds getMaxTextWidth(Font font, SensorValue valueSensor) 
	{
		String minValue = String.format("%.1f", valueSensor.getVon());
		String maxValue = String.format("%.1f", valueSensor.getBis());
		
		String measuringUnit = " " + valueSensor.getMeasurementUnit();
		
		Text valTextMin = new Text(minValue);
		valTextMin.setFont(font);
		
		Bounds valMinBounds = valTextMin.getBoundsInLocal();
		
		Text valTextMax = new Text(maxValue);
		valTextMax.setFont(font);
		
		Bounds valMaxBounds = valTextMax.getBoundsInLocal();
		
		Text messText = new Text(measuringUnit);
		messText.setFont(font);
		
		Bounds einheitBounds = messText.getBoundsInLocal();
		
		double width = valMinBounds.getWidth();
		if(width < valMaxBounds.getWidth())
			width = valMaxBounds.getWidth();
		
		double height = valMinBounds.getHeight();
		if(height < valMaxBounds.getHeight())
			height = valMaxBounds.getHeight();
		
		if(height < einheitBounds.getHeight())
			height = einheitBounds.getHeight();
		
		width = width + einheitBounds.getWidth();
	
		
		return new BoundingBox(0,0, width, height);
	}



}
