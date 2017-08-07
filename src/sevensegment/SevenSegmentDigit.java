package sevensegment;



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
	
	
	public SevenSegmentDigit()
	{
		this.setWidth(46);
		this.setHeight(64);
		//TODO interfaces und resize auch so
		this.initGraphics();
		this.registerListener();
	
		
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
		
		
		this.drawBackground();
		this.draw7Segment();
		
		
		//alles auf die Oberfläche ablegen
		this.getChildren().addAll(background, a, b, c, d, e, f, g);
	}
	
	private void draw7Segment() {
		// TODO Auto-generated method stub
		
	}

	private void drawBackground() 
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
		background.setFill(Color.web("#5B8C3E"));
	
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
		
		
		
		
		a.setFill(Color.WHITE);
		b.setFill(Color.WHITE);
		c.setFill(Color.WHITE);
		d.setFill(Color.WHITE);
		e.setFill(Color.WHITE);
		f.setFill(Color.WHITE);
		g.setFill(Color.WHITE);
		
	}

	public void resize()
	{
		this.drawBackground();
		
	}
	

}
