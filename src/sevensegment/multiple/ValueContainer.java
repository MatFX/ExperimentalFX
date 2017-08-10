package sevensegment.multiple;

import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javafx.geometry.Insets;
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
import sevensegment.single.SevenSegmentDigit;
import tools.helper.NinePatchLoader;

public class ValueContainer<DATATYPE extends Number> extends GridPane
{
	private DATATYPE currentValue;
	private DATATYPE from;
	private DATATYPE to;
	private int digitsAfterDecimalPoint = 0;
	
	private boolean isNegative = false;
	
	private TreeMap<Integer, SevenSegmentDigit> vorkommaStellen = new TreeMap<Integer, SevenSegmentDigit>();
	
	private Thread animThread = null;
	
	private boolean isAnimation = false;
	
	public ValueContainer(DATATYPE currentValue, DATATYPE from, DATATYPE to, int digitsAfterDecimalPoint)
	{
		this.currentValue = currentValue;
		if(this.currentValue.doubleValue() < 0)
			isNegative = true;
		
		this.from = from;
		this.to = to;
		this.digitsAfterDecimalPoint = digitsAfterDecimalPoint;
		
		
		
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
		
		this.initGraphics();
		
		
		//this.setPrefWidth(USE_PREF_SIZE);
		//this.setPrefHeight(USE_PREF_SIZE);
	}


	private void initGraphics() 
	{
		//Aufbau ist: -12345.9090
		
		//negatives entfernen 
		
		//feststellen die anzahl der stellen im bereich > 0
		
		//fesstellen ob nachkommastellen gefordert dann anzahl stellen bereich < 0
		
		
		//für jede stelle wird eine sieben segment Anzeige benötigt 
		
		int zahl = to.intValue();
		if(this.isNegative)
			zahl *= -1;
		
		
		RowConstraints rc = new RowConstraints();
	    rc.setVgrow(Priority.ALWAYS);
		this.getRowConstraints().add(rc);
		
		int anzahlStellen = laenge(zahl);
		System.out.println(" anzahlStellen " + anzahlStellen);
		int log = (int)Math.log10(zahl);
		ColumnConstraints col = new ColumnConstraints();
		col.setHgrow(Priority.ALWAYS);
		for(int i = 0; i < anzahlStellen; i++)
		{
			System.out.println("wert " + getFigure(currentValue.intValue(), i, log));
			SevenSegmentDigit ssd =  new SevenSegmentDigit(getFigure(currentValue.intValue(), i, log));
			ssd.setOFFColor(Color.web("#787e6e80"));
			ssd.setBACKGROUNDColor(Color.TRANSPARENT);
			//initiales malen wird hier nochmals erzwungen, dann brauche ich die o.a. setMethoden mit Leben befüllen
			ssd.draw7Segment();
			vorkommaStellen.put(i, ssd);
			this.getColumnConstraints().add(col);
			
		}
	
		int colIndex = 0;
		
		for(int i = anzahlStellen; i > 0 ; i--)
		{
			this.add(vorkommaStellen.get(colIndex), colIndex, 0);
			colIndex++;
		}
	}
	
	public static void main(String[] args)
	{
		int test = 101;
		int zahl = 1;
        int log = (int)Math.log10(zahl);
        System.out.println("log " + log);
        for(int i = 0; i <= log; i++)
        {
            System.out.println(getFigure(zahl, i,log));
        }
        
	}
	
	private static int getFigure(int zahl, int i, int log) {
        return (int) ((zahl / Math.pow(10.0, log - i))) % 10;
    }
	
	public static int laenge(int zahl) 
	{	
		return (int) (Math.log10(zahl)+1);
	}


	public DATATYPE getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(DATATYPE currentValue) {
		this.currentValue = currentValue;
	}

	public DATATYPE getFrom() {
		return from;
	}

	public void setFrom(DATATYPE from) {
		this.from = from;
	}

	public DATATYPE getTo() {
		return to;
	}

	public void setTo(DATATYPE to) {
		this.to = to;
	}

	public int getDigitsAfterDecimalPoint() {
		return digitsAfterDecimalPoint;
	}

	public void setDigitsAfterDecimalPoint(int digitsAfterDecimalPoint) {
		this.digitsAfterDecimalPoint = digitsAfterDecimalPoint;
	}


	public boolean isNegative() {
		return isNegative;
	}


	public void setNegative(boolean isNegative) {
		this.isNegative = isNegative;
	}


	//testbereich
	public void startAnimation() 
	{
		if(animThread != null && animThread.isAlive())
			animThread.stop();
		
		
		isAnimation = true;
		Runnable runnable = new Runnable(){

			@Override
			public void run() 
			{
				int maxValue = to.intValue();
				int minValue = from.intValue();
				
				while(isAnimation)
				{
					
					
					
					Random ran = new Random();
					int zufallszahl = ran.nextInt((maxValue - minValue) + 1);
					System.out.println("ermittelte Zahl " + zufallszahl);
					
					//TODO
					//currentValue = zufallszahl;
					
					//als nächstes muss die Zahl aufgeteilt werden in einzelteile und
					//die einzelnen werte müssen in der map gesetzt werden
					int zahl = to.intValue();

					int anzahlStellen = laenge(zahl);
					System.out.println(" anzahlStellen " + anzahlStellen);
					int log = (int)Math.log10(zahl);
					for(int i = 0; i < anzahlStellen; i++)
					{
						
						vorkommaStellen.get(i).setAndRepaintDigit(getFigure(zufallszahl, i, log));
						System.out.println("wert " + getFigure(zufallszahl, i, log));
						//SevenSegmentDigit ssd =  new SevenSegmentDigit(getFigure(currentValue.intValue(), i, log));
						
						//initiales malen wird hier nochmals erzwungen, dann brauche ich die o.a. setMethoden mit Leben befüllen
						//ssd.draw7Segment();
						//vorkommaStellen.put(i, ssd);
					}
					

					//Platform.runLater(() -> setAndRepaintDigit(zufallszahl));

					try 
					{
						TimeUnit.MILLISECONDS.sleep(500);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					//TODO raus test
					//isAnimation = false;
					
				}
			}	
			
		};
		
		animThread = new Thread(runnable);
		animThread.start();
		
		
	
		
	}

	public void stopAnimation() 
	{
		isAnimation = false;
		if(animThread != null)
			animThread.stop();
	}

}
