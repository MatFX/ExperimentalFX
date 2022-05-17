package font;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Font scaling after resize the stage
 * @author m.goerlich
 *
 */
public class FontScaling extends Application 
{
	
	private String msg = "Testtext";
	
	private DoubleProperty fontSize = new SimpleDoubleProperty(24);
	
	private Label label;
	
	private final double GAP = 20;
	
	

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) 
	{
		  BorderPane border = new BorderPane();

          Scene scene = new Scene(border);
          primaryStage.setWidth(100);
          primaryStage.setHeight(75);
          label = new Label();
          label.setText(msg);
          label.setAlignment(Pos.CENTER);
          
          
         // HBox.setHgrow(label, Priority.ALWAYS);
         // VBox.setVgrow(label, Priority.ALWAYS);
          
          Color col = Color.rgb(46, 127 , 200);
		  CornerRadii corn = new CornerRadii(2);
		  Background background = new Background(new BackgroundFill(col, corn, Insets.EMPTY));
		  label.setBackground(background);
          border.setCenter(label);

          label.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
          
          scene.heightProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
			{
				label.setPrefHeight(newValue.doubleValue());
				label.setMaxHeight(newValue.doubleValue());
				if(newValue.doubleValue() > oldValue.doubleValue())
				{
					double tempSize = getGreaterFont(fontSize.get());
					if(tempSize != fontSize.get())
						fontSize.set(tempSize);
					
				}
				else if(newValue.doubleValue() < oldValue.doubleValue())
				{
					double tempSize = getLesserFont(fontSize.get());
  					if(tempSize != fontSize.get())
						fontSize.set(tempSize);
				}
				
				
			}

			
          });
          
          scene.widthProperty().addListener(new ChangeListener<Number>(){

  			@Override
  			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
  			{
  				label.setPrefWidth(newValue.doubleValue());
  				label.setMaxWidth(newValue.doubleValue());
  				if(newValue.doubleValue() > oldValue.doubleValue())
  				{
  					double tempSize = getGreaterFont(fontSize.get());
  					if(tempSize != fontSize.get())
						fontSize.set(tempSize);
  				}
  				else if(newValue.doubleValue() < oldValue.doubleValue())
  				{	
  					double tempSize = getLesserFont(fontSize.get());
  					if(tempSize != fontSize.get())
						fontSize.set(tempSize);
  					
  				}
  				
  				
  			}
            });
          
          
          primaryStage.setScene(scene);
          primaryStage.show();

		
	}
	
	private double getLesserFont(double fontSize)
	{	
		Bounds futureBounds = textWidth(fontSize);
		
		//wenn eines von beiden über das Ziel hinaus ist, so ist eine kleiner Fontgröße zu ermitteln
		if(futureBounds.getHeight() + GAP > label.getPrefHeight() || futureBounds.getWidth()+ GAP > label.getPrefWidth())
		{
			fontSize = fontSize - 1;
			if(fontSize <= 0)
				return 1;
			return getLesserFont(fontSize);
		}
		return fontSize;
	}
	
	private double getGreaterFont(double fontSize)
	{	
		fontSize = fontSize + 1;
		Bounds futureBounds = textWidth(fontSize);
		if(futureBounds.getHeight() + GAP < label.getLayoutBounds().getHeight() && futureBounds.getWidth()+GAP < label.getLayoutBounds().getWidth())
		{
			return getGreaterFont(fontSize);
		}
		return fontSize-1;
	}
	
	
	
	private Bounds textWidth(double size) {
		Text text = new Text(msg);
        Font font =  Font.font(label.getFont().getFamily(), size);
        text.setFont(font);
        return text.getBoundsInLocal();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
