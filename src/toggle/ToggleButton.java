package toggle;


import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ToggleButton extends Application {

	 private final Color grayColor = Color.web("#797979");
	 
	 private final Color lightGrayColor = Color.web("#ACACAC");
	 
	 private final Color selectionColor = Color.web("#27A6D1");
	 
	 private final RadialGradient RADIAL_GRADIENT = new RadialGradient(
				0.0, 0.0, 0.3011, 0.3009, 0.6954, true, CycleMethod.NO_CYCLE,
				new Stop(0.0, new Color(0.8289, 0.8289, 0.8289, 1.0)),
				new Stop(0.0067, new Color(0.8289, 0.8289, 0.8289, 1.0)),
				new Stop(0.928, new Color(0.3104, 0.32, 0.3109, 1.0)),
				new Stop(1.0, new Color(0.2674, 0.2763, 0.2677, 1.0)));
	 
	 
	
	
	
    private Parent createContent() {
    	


    	
    	
        BorderPane root = new BorderPane();
        root.setPrefSize(150, 150);
        
        VBox vBox = new VBox();
   
        
        HBox hBox = new HBox(5);
        

        ToggleSwitch toggle = new ToggleSwitch();
        hBox.getChildren().addAll(createHSpacer(), toggle, createHSpacer());
        
        vBox.getChildren().addAll(createVSpacer(), hBox, createVSpacer());
        
        root.setCenter(vBox);
        
        toggle.switchedOnProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				
				System.out.println("newValue: " + newValue);
				
			}
        	
        });
        
        
       
        return root;
    }

    private class ToggleSwitch extends Parent {

        private BooleanProperty switchedOn = new SimpleBooleanProperty(false);

        private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
        private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));

        private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);

        public BooleanProperty switchedOnProperty() {
            return switchedOn;
        }

        public ToggleSwitch() {
            Rectangle background = new Rectangle(50, 25);
            background.setArcWidth(25);
            background.setArcHeight(25);
            background.setFill(Color.WHITE);
            background.setStroke(Color.LIGHTGRAY);
            
            

            Circle trigger = new Circle(12.5);
            trigger.setCenterX(12.5);
            trigger.setCenterY(12.5);
            trigger.setFill(RADIAL_GRADIENT);
            trigger.setStroke(Color.DARKGREY);

            DropShadow shadow = new DropShadow();
            shadow.setRadius(2);
            trigger.setEffect(shadow);

            translateAnimation.setNode(trigger);
            fillAnimation.setShape(background);

            getChildren().addAll(background, trigger);

            switchedOn.addListener((obs, oldState, newState) -> {
                boolean isOn = newState.booleanValue();
                System.out.println(">> " + (isOn ? 50 - 25 : 0));
                translateAnimation.setToX(isOn ? 50 - 25 : 0);
                fillAnimation.setFromValue(isOn ? Color.WHITE : selectionColor);
                fillAnimation.setToValue(isOn ? selectionColor : Color.WHITE);

                animation.play();
            });

            setOnMouseClicked(event -> {
                switchedOn.set(!switchedOn.get());
            });
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    

	private Node createHSpacer() 
	{
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}
	
	private Node createVSpacer() 
	{
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		return spacer;
	}
	
}