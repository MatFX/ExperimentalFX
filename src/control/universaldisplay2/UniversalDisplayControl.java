package control.universaldisplay2;

import control.button.single.metal.SingleMetalButton;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import tools.helper.ImageLoader;

public class UniversalDisplayControl extends GridPane
{
	private DisplayLCD displayLCD;
	
	//private CombinedThreeButtonControl combined;
	
	private SingleMetalButton smbViewLeft, smbViewRight;
	
	private SingleMetalButton plus, minus;
	
	private SingleMetalButton presetLeft, presetRight, sendValue;
	
	private Color textColor = Color.web("#4d4d4d");
	
	private Color textPressedColor = Color.web("#333333");
	
	public UniversalDisplayControl()
	{
		super();
		this.setPadding(new Insets(10, 10, 10, 10));
		this.setHgap(10);
		this.setVgap(10);
		this.setPrefWidth(75);
		
		//combined = new CombinedThreeButtonControl();
		//combined.resize(130, 55);
		displayLCD = new DisplayLCD();
		
		//wir benötigen ein Display in der linken hälfte 
		
		//auf der rechten seite werden die Buttons abgelegt
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHgrow(Priority.ALWAYS);
		col1.setPercentWidth(75);
		ColumnConstraints col2 = new ColumnConstraints();
		
		this.getColumnConstraints().addAll(col1, col2);
		this.add(displayLCD, 0, 0, 1, 3); 
		
		
		
		smbViewLeft = new SingleMetalButton();
		smbViewLeft.getContentRegion().setTextColor(textColor);
		smbViewLeft.getContentRegion().setTextColorPressed(textPressedColor);
		smbViewLeft.setText("<");
		smbViewLeft.setMinWidth(38);
		smbViewLeft.setMinHeight(38);
		
		
		smbViewRight = new SingleMetalButton();
		smbViewRight.getContentRegion().setTextColor(textColor);
		smbViewRight.getContentRegion().setTextColorPressed(textPressedColor);
		smbViewRight.setText(">");
		smbViewRight.setMinWidth(38);
		smbViewRight.setMinHeight(38);
		
		this.add(smbViewLeft, 1, 0, 1, 1);
		this.add(smbViewRight, 2, 0, 1, 1);
		
		
		plus = new SingleMetalButton();
		plus.getContentRegion().setTextColor(textColor);
		plus.getContentRegion().setTextColorPressed(textPressedColor);
		plus.setText("+");
		plus.setMinWidth(38);
		plus.setMinHeight(38);
		
		minus = new SingleMetalButton();
		minus.getContentRegion().setTextColor(textColor);
		minus.getContentRegion().setTextColorPressed(textPressedColor);
		minus.setText("-");
		minus.setMinWidth(38);
		minus.setMinHeight(38);
		
		this.add(plus, 1, 1, 1, 1);
		this.add(minus, 2, 1, 1, 1);
		
		presetLeft = new SingleMetalButton();
		presetLeft.getContentRegion().setTextColor(textColor);
		presetLeft.getContentRegion().setTextColorPressed(textPressedColor);
		presetLeft.setText("<");
		presetLeft.setMinWidth(38);
		presetLeft.setMinHeight(38);
		
		
		presetRight = new SingleMetalButton();
		presetRight.getContentRegion().setTextColor(textColor);
		presetRight.getContentRegion().setTextColorPressed(textPressedColor);
		presetRight.setText(">");
		presetRight.setMinWidth(38);
		presetRight.setMinHeight(38);
		
		
		sendValue = new SingleMetalButton();
		sendValue.getContentRegion().setTextColor(textColor);
		sendValue.getContentRegion().setTextColorPressed(textPressedColor);
		sendValue.setText("°");
		//sendValue.setImageView(ImageLoader.getImageFromIconFolder("hi_bewegung"));
		sendValue.setMinWidth(38);
		sendValue.setMinHeight(38);
		
		

		this.add(presetLeft, 1, 2, 1, 1);
		this.add(sendValue, 2, 2, 1, 1);
		this.add(presetRight, 3, 2, 1, 1);
	}

	
}
