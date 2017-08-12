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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
	/**
	 * Nicht immer vorhanden nur dann wenn von außen angefordert 
	 */
	private TreeMap<Integer, SevenSegmentDigit> nachkommaStellen = new TreeMap<Integer, SevenSegmentDigit>();
	
	private Thread animThread = null;
	
	private boolean isAnimation = false;
	
	/**
	 * Für ein negatives Vorzeichen muss nicht immer vorhanden sein
	 */
	private SevenSegmentDigit negSign = null;
	
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
		ColumnConstraints col = new ColumnConstraints();
		col.setHgrow(Priority.ALWAYS);
		//Aufbau ist: -12345.9090
		int colIndex = 0;
		boolean vorzeichen = false;
		//Ist der aktuelle Wert negative oder einer der Grenzbereich im negativen
		if(this.isNegative || from.doubleValue() < 0 || to.doubleValue() < 0 )
		{
			vorzeichen = true;
			
			//jetzt geht es darum ob der eigentliche Wert Negative ist und nicht ob es möglich wäre
			if(this.isNegative)
				negSign =  new SevenSegmentDigit();
			else 
				negSign =  new SevenSegmentDigit(Integer.MAX_VALUE);
			
			negSign.setOFFColor(Color.web("#787e6e80"));
			negSign.setBACKGROUNDColor(Color.TRANSPARENT);
			
			//initiales malen wird hier nochmals erzwungen, dann brauche ich die o.a. setMethoden mit Leben befüllen
			negSign.draw7Segment();
			this.add(negSign, colIndex, 0);
			//damit die späteren Werte auch in den richtigen Spalten ihr zuhause finden.
			colIndex++;
			this.getColumnConstraints().add(col);
		}
	
		
		
		
		//TODO
		//negatives entfernen 
		
		
		int zahl = 0;
		
		//feststellen die anzahl der stellen im bereich > 0
		
		//fesstellen ob nachkommastellen gefordert dann anzahl stellen bereich < 0
		
		
		//für jede stelle wird eine sieben segment Anzeige benötigt 
		
		zahl = to.intValue();
		System.out.println("zahl vorher " + zahl);
		//negatives vorzeichen von der Zahl entfernen 
		if(zahl < 0)
			zahl *= -1;
		
		
		RowConstraints rc = new RowConstraints();
	    rc.setVgrow(Priority.ALWAYS);
		this.getRowConstraints().add(rc);
		
		int anzahlStellen = laenge(zahl);
		System.out.println(" anzahlStellen " + anzahlStellen);
		int log = (int)Math.log10(zahl);
		
		int currentIntValue = currentValue.intValue();
		if(currentIntValue < 0)
			currentIntValue *= -1;
		
		
		for(int i = 0; i < anzahlStellen; i++)
		{
			System.out.println("wert " + getFigure(currentIntValue, i, log));
			SevenSegmentDigit ssd =  new SevenSegmentDigit(getFigure(currentIntValue, i, log));
			ssd.setOFFColor(Color.web("#787e6e80"));
			ssd.setBACKGROUNDColor(Color.TRANSPARENT);
			//initiales malen wird hier nochmals erzwungen, dann brauche ich die o.a. setMethoden mit Leben befüllen
			ssd.draw7Segment();
			vorkommaStellen.put(i, ssd);
			this.getColumnConstraints().add(col);
			
		}
	
		
		int mapIndex = 0;
	
		for(int i = anzahlStellen; i > 0 ; i--)
		{
			System.out.println("naechste col " + colIndex);
			this.add(vorkommaStellen.get(mapIndex), colIndex, 0);
			colIndex++;
			mapIndex++;
		}
		
		//jetzt fehlen noch die evtl. nachkommastellen
		

		System.out.println("digitsAfterDecimalPoint " + digitsAfterDecimalPoint);
		//Nachkommastellen
		if(digitsAfterDecimalPoint > 0)
		{
			//TODO hier noch ein digit für den punkt
			
			Rectangle circle = new Rectangle(0,0,5,5);
		
			
			this.add(circle, colIndex, 0);
			colIndex++;
			//int zahl = (int) doubleValue;
			
			String doubleValueString = ""+currentValue.doubleValue();
			int nachKommaWert = Integer.parseInt(doubleValueString.substring(doubleValueString.indexOf(".")+1));
		
			System.out.println("nachkommaBereich " + nachKommaWert);
			int anzahlStellenNachkomma = digitsAfterDecimalPoint; // (int) (Math.log10(nachKommaWert)+1);
			System.out.println("anzahlStellenNachkomma " + anzahlStellenNachkomma);
			
			
			for(int i = 0; i < anzahlStellenNachkomma; i++)
			{
				System.out.println("wert " + getFigure(nachKommaWert, i, log));
				SevenSegmentDigit ssd =  new SevenSegmentDigit(getFigure(nachKommaWert, i, log));
				ssd.setOFFColor(Color.web("#787e6e80"));
				ssd.setBACKGROUNDColor(Color.TRANSPARENT);
				//initiales malen wird hier nochmals erzwungen, dann brauche ich die o.a. setMethoden mit Leben befüllen
				ssd.draw7Segment();
				nachkommaStellen.put(i, ssd);
				this.getColumnConstraints().add(col);
				
			}
			mapIndex = 0;
			for(int i = anzahlStellenNachkomma; i > 0 ; i--)
			{
				System.out.println("naechste col " + colIndex);
				this.add(nachkommaStellen.get(mapIndex), colIndex, 0);
				colIndex++;
				mapIndex++;
			}
			
		}
		
		
		
	}
	
	public static void main(String[] args)
	{
		
		double doubleValue = 12654.12;
		System.out.println("Math.log10 " + (Math.log10(doubleValue)+1));
		
		int anzahlStellen =  (int) (Math.log10(doubleValue)+1);
		
		int vorkommaBereich = (int) doubleValue;
		System.out.println("vorkommeBereich " + vorkommaBereich);
		
		String doubleValueString = ""+doubleValue;
		int nachKomma = Integer.parseInt(doubleValueString.substring(doubleValueString.indexOf(".")+1));
	
		System.out.println("nachkommaBereich " + nachKomma);
		int anzahlStellenNachkomma = (int) (Math.log10(nachKomma)+1);
		System.out.println("anzahlStellenNachkomma " + anzahlStellenNachkomma);

		
		
		
		/*
		int test = 101;
		int zahl = 1;
        int log = (int)Math.log10(zahl);
        System.out.println("log " + log);
        for(int i = 0; i <= log; i++)
        {
            System.out.println(getFigure(zahl, i,log));
        }*/
        
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
					
					
					int zufallszahl = (int) getZufallsZahl(from, to);
					/*
					if(minValue < 0)
						minValue *=-1;
					
					if(maxValue < 0)
						maxValue *=-1;
					*/
					
					
					//int zufallszahl = ran.nextInt((maxValue - minValue) + 1);
					System.out.println("ermittelte Zahl " + zufallszahl);
					
					
					if(negSign != null)
					{
						if(zufallszahl < 0)
						{
							negSign.setAndRepaintDigit(-1);
						}
						else
						{
							negSign.setAndRepaintDigit(Integer.MAX_VALUE);
							
						}
					}
					
					
					
					//TODO
					//currentValue = zufallszahl;
					
					//als nächstes muss die Zahl aufgeteilt werden in einzelteile und
					//die einzelnen werte müssen in der map gesetzt werden
					int zahl = to.intValue();
					
					int anzahlStellen = laenge(zahl);
					System.out.println(" anzahlStellen " + anzahlStellen);
					int log = (int)Math.log10(zahl);
					//bevor die eigentliche Zahl gesetzt wird, wieder zurücksetzen des evtl. vorahnden -1
					if(zufallszahl < 0 )
						zufallszahl *=-1;
					
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
	
	
	/**
	 * Ermittelt die nächste Zufallszahl
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	private Number getZufallsZahl(DATATYPE minValue, DATATYPE maxValue) 
	{
		Number returnValue = null;
		if(minValue instanceof Integer)
		{
			Random ran = new Random();
			
			if(minValue.intValue() < 0 && maxValue.intValue() > 0)
			{
				//groesserer Wertebereich
				int minimum = minValue.intValue() * -1;
				
				int wertebereich = minimum + maxValue.intValue();
				int nextValue = (ran.nextInt(wertebereich) + 1) + minValue.intValue();
				return new Integer(nextValue);
			}
			else if(minValue.intValue() < 0 && maxValue.intValue() < 0)
			{
				//*-1
				int minimum = minValue.intValue() * -1;
				int maximum = maxValue.intValue() * -1;
				
				int wertebereich = maximum - minimum;
				int nextValue = (ran.nextInt(wertebereich) + 1);
				return new Integer(nextValue * -1);
				
			}
			else
			{
				int wertebereich = maxValue.intValue() - minValue.intValue();
				int nextValue = (ran.nextInt(wertebereich) + 1);
				return new Integer(nextValue * -1);
			}
			
			
			
		}
		else if(minValue instanceof Double)
		{
			
		}
		
		
		
		return (DATATYPE) returnValue;
	}	

	public void stopAnimation() 
	{
		isAnimation = false;
		if(animThread != null)
			animThread.stop();
	}

}
