package sevensegment.single;



import java.util.Random;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class SevenSegmentDigit extends Region
{
	
	
	private Rectangle background;
	
	/**
	 * Benennung nach dem wikipedia eintrag
	 */
	private Polygon a,b,c,d,e,f,g;
	
	//Keine richtige Konstante, kann später vom Inhalt noch verändert werden.
	private Color OFF = Color.web("#969e8945");
	
	private Color ON = Color.web("#000000");
	
	private Color BACKGROUND = Color.web("#cdcdcd");
	
	private Thread animThread = null;
	
	private boolean isAnimation = false;
	
	//zu beginn werden alle polygon auf OFF gesetzt
	private int currentValue = -1;
	
	
	public SevenSegmentDigit(int currentValue)
	{
		this.currentValue = currentValue;
		this.setWidth(46);
		this.setHeight(64);
		//TODO interfaces und resize auch so
		this.initGraphics();
		this.registerListener();
		
	}
	
	
	
	public SevenSegmentDigit()
	{
		this(-1);
	}

	private void registerListener() {
		widthProperty().addListener(observable -> resize());
		heightProperty().addListener(observable -> resize());
	}

	private void initGraphics() 
	{
		//hier die kompletten Inits
		background = new Rectangle();
		a = new Polygon();
		b = new Polygon();
		c = new Polygon();
		d = new Polygon();
		e = new Polygon();
		f = new Polygon();
		g = new Polygon();
		
		this.draw7Segment();
		
		
		//alles auf die Oberfläche ablegen
		this.getChildren().addAll(background, a, b, c, d, e, f, g);
	}

	
	public void setONColor(Color onColor)
	{
		ON = onColor;
	}
	
	public void setOFFColor(Color offColor)
	{
		OFF = offColor;
	}
	
	public void setBACKGROUNDColor(Color backgroundColor)
	{
		BACKGROUND = backgroundColor;
	}

	public void draw7Segment() 
	{
		//46
		double w = this.getWidth();
		//64
		double h = this.getHeight();
		
		
		//ist 1:1 mit dem kompletten Gebilde
		//width="46" height="64"
		background.setX(0D);
		background.setY(0D);
		background.setWidth(w);
		background.setHeight(h);
		background.setFill(BACKGROUND);
	
		//Erstmal die Punkte leeren; Vorbereitung für die wiederholte Zeichnung
		a.getPoints().clear();
		b.getPoints().clear();
		c.getPoints().clear();
		d.getPoints().clear();
		e.getPoints().clear();
		f.getPoints().clear();
		g.getPoints().clear();
		
		a.getPoints().addAll(new Double[]
		{
				
				//x 100/46 *33.106 =  71,9695652173913 = 0.719695652173913
				w *  0.719695652173913,
				//y 100/64 * 7,28 =  11,375 = 0.11375
				//x 100/46 * 38,967 = 84,71086956521739 = 0.8471086956521739
				h * 0.11375, w * 0.8471086956521739,
				//y 100/64 * 1.418 = 2,215625 = 0.02215625
				//x 100/46 * 38.967 = 84,71086956521739 = 0.8471086956521739
				h * 0.02215625, w * 0.8471086956521739,
				//y 100/64 * 1,359 =  2,1234375 = 0.021234375
				//x 100/46 * 7,077 = 15,38478260869565 = 0.1538478260869565
				h * 0.021234375, w * 0.1538478260869565,
				//y 100/64 * 1.359 = 2,1234375 = 0.021234375
				// x 100/46 * 28,22173913043478 = 0.2822173913043478
				h * 0.021234375,  w * 0.2822173913043478,
				//y 100/64 * 7,263 = 11,3484375 = 0.113484375
				//x 100/46 * 12,965 = 28,18478260869565 = 0.2818478260869565
				h * 0.113484375, w * 0.2818478260869565,
				//y 100/64 * 7,28 =  11,375 = 0.11375
				h *  0.11375,
				
		});
		
		b.getPoints().addAll(new Double[]
		{
				//x 100/46 * 41.967 = 91,23260869565217 = 0.9123260869565217
				w * 0.9123260869565217,
				//y 100/64 * 30.895 = 48,2734375 = 0.482734375
				//x 100/46 * 41,967 = 91,23260869565217 = 0.9123260869565217
				h * 0.482734375, w * 0.9123260869565217,
				//y 100/64 * 3,39 = 5,296875 = 0.05296875
				//x 100/46 * 36,078 = 78,4304347826087 = 0.784304347826087
				h * 0.05296875, w * 0.784304347826087,
				//y 100/64 * 9,28 = 14,5 = 0.145
				//x =  100/46 * 36,046 = 78,36086956521739 = 0.7836086956521739
				h *  0.145 , w * 0.7836086956521739,
				//y = 100/64 * 9,248 = 14,45 = 0.1445
				//x = 100/46 * 36.046 = 78,36086956521739 = 0.7836086956521739
				h * 0.145, w * 0.7836086956521739,
				//y 100/ 64 * 27,954 = 43,678125 = 0.43678125
				h * 0.43678125
		});
		
		c.getPoints().addAll(new Double[]
				{
						 w * 0.7836086956521738, h * 0.591484375,
						 w * 0.7836086956521738, h * 0.85965625,
						 w * 0.911586956521739, h * 0.951640625,
						 w * 0.9123260869565216, h * 0.951640625,
						 w * 0.9123260869565216, h * 0.54553125
					});
		d.getPoints().addAll(new Double[]
				{
						 w * 0.718391304347826, h * 0.890671875,
						 w * 0.7188043478260869, h * 0.8903750000000001,
						 w * 0.27991304347826085, h * 0.8903750000000001,
						 w * 0.15145652173913043, h * 0.9826875000000002,
						 w * 0.15145652173913043, h * 0.982890625,
						 w * 0.846695652173913, h * 0.982890625
					});
		e.getPoints().addAll(new Double[]
				{
						 w * 0.08623913043478261, h * 0.951640625,
						 w * 0.08650000000000001, h * 0.951640625,
						 w * 0.21495652173913044, h * 0.859296875,
						 w * 0.21495652173913044, h * 0.591640625,
						 w * 0.08623913043478261, h * 0.5456875
					}
				);
		f.getPoints().addAll(new Double[]
				{
						 w * 0.21495652173913044, h * 0.43662500000000004,
						 w * 0.21495652173913044, h * 0.145359375,
						 w * 0.08623913043478261, h * 0.052843749999999995,
						 w * 0.08623913043478261, h * 0.482515625,
						 w * 0.08641304347826086, h * 0.482515625
					});
		g.getPoints().addAll(new Double[]
				{
						 w * 0.7185652173913043, h * 0.46787500000000004,
						 w * 0.2798478260869565, h * 0.46787500000000004,
						 w * 0.15145652173913043, h * 0.514046875,
						 w * 0.15145652173913043, h * 0.514453125,
						 w * 0.279804347826087, h * 0.560390625,
						 w * 0.7183695652173913, h * 0.560390625,
						 w * 0.8471086956521738, h * 0.51409375,
						 w * 0.8471086956521738, h * 0.513890625
					});
		
		
		
		this.repaintDigit();
	}
	
	public void setAndRepaintDigit(int newValue)
	{
		this.currentValue = newValue;
		this.repaintDigit();
	}
	
	public void repaintDigit()
	{
		
		
		switch(currentValue)
		{
			case 0:
				a.setFill(ON);
				b.setFill(ON);
				c.setFill(ON);
				d.setFill(ON);
				e.setFill(ON);
				f.setFill(ON);
				g.setFill(OFF);
				break;
			
			case 1:
				a.setFill(OFF);
				b.setFill(ON);
				c.setFill(ON);
				d.setFill(OFF);
				e.setFill(OFF);
				f.setFill(OFF);
				g.setFill(OFF);
				break;
			
			case 2:
				a.setFill(ON);
				b.setFill(ON);
				c.setFill(OFF);
				d.setFill(ON);
				e.setFill(ON);
				f.setFill(OFF);
				g.setFill(ON);
				break;
			
			case 3:
				a.setFill(ON);
				b.setFill(ON);
				c.setFill(ON);
				d.setFill(ON);
				e.setFill(OFF);
				f.setFill(OFF);
				g.setFill(ON);
				break;
			case 4:
				a.setFill(OFF);
				b.setFill(ON);
				c.setFill(ON);
				d.setFill(OFF);
				e.setFill(OFF);
				f.setFill(ON);
				g.setFill(ON);
				break;
			
			case 5:
				a.setFill(ON);
				b.setFill(OFF);
				c.setFill(ON);
				d.setFill(ON);
				e.setFill(OFF);
				f.setFill(ON);
				g.setFill(ON);
				break;
			
			
			case 6:
				a.setFill(ON);
				b.setFill(OFF);
				c.setFill(ON);
				d.setFill(ON);
				e.setFill(ON);
				f.setFill(ON);
				g.setFill(ON);
			
				break;
				
			case 7:
				a.setFill(ON);
				b.setFill(ON);
				c.setFill(ON);
				d.setFill(OFF);
				e.setFill(OFF);
				f.setFill(OFF);
				g.setFill(OFF);
			
				break;
			case 8:
				a.setFill(ON);
				b.setFill(ON);
				c.setFill(ON);
				d.setFill(ON);
				e.setFill(ON);
				f.setFill(ON);
				g.setFill(ON);
			
				break;
			case 9:
				a.setFill(ON);
				b.setFill(ON);
				c.setFill(ON);
				d.setFill(ON);
				e.setFill(OFF);
				f.setFill(ON);
				g.setFill(ON);
				break;
			//alle anderen Werte schalten ab
			default:
				a.setFill(OFF);
				b.setFill(OFF);
				c.setFill(OFF);
				d.setFill(OFF);
				e.setFill(OFF);
				f.setFill(OFF);
				g.setFill(OFF);
				break;
		}
	}

	public void resize()
	{
		this.draw7Segment();
		
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
				int minValue = 0;
				int maxValue = 9;
				
				while(isAnimation)
				{
					Random ran = new Random();
					int zufallszahl = ran.nextInt((maxValue - minValue) + 1);
					System.out.println("ran " + zufallszahl);
					
					Platform.runLater(() -> setAndRepaintDigit(zufallszahl));
					

					try 
					{
						TimeUnit.MILLISECONDS.sleep(500);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					
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
