package control.universaldisplay2;

import control.button.combined.CombinedThreeButtonControl;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class UniversalDisplayControl extends GridPane
{
	private DisplayLCD displayLCD;
	
	private CombinedThreeButtonControl combined;
	
	public UniversalDisplayControl()
	{
		super();
		this.setPadding(new Insets(5, 5, 5, 5));
		this.setHgap(10);
		this.setVgap(10);
		this.setPrefWidth(75);
		
		combined = new CombinedThreeButtonControl();
		combined.resize(130, 55);
		displayLCD = new DisplayLCD();
		
		//wir benötigen ein Display in der linken hälfte 
		
		//auf der rechten seite werden die Buttons abgelegt
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHgrow(Priority.ALWAYS);
		col1.setPercentWidth(75);
		ColumnConstraints col2 = new ColumnConstraints();
		
		this.getColumnConstraints().addAll(col1, col2);
		this.add(displayLCD, 0, 0, 1, 3); 
		
		Button test = new Button("Rechts 2");
		test.setMaxSize(40,  40);
		test.setMinSize(40,  40);
		this.add(new Label("TEST"), 1, 0, 1, 1);
		//this.add(combined, 1, 0, 1, 1); 
		this.add(test, 1, 1, 1, 1); 
		this.add(new Button("Rechts 3"), 1, 2, 1, 1); 
	}

	
}
