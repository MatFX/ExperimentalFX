package chatGPT_test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Source code generated from chatGPT
 * @author chatGBT
 *
 */
public class NewmorphismButton extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("newmorphism");

        Button button = new Button("Button");
      	     // Create a canvas
	     Canvas canvas = new Canvas(50, 50);
	     GraphicsContext gc = canvas.getGraphicsContext2D();
	
	     // Draw the lamp on the canvas
	     gc.setFill(Color.YELLOW);
	     gc.fillOval(10, 10, 30, 30);
	     gc.setStroke(Color.BLACK);
	     gc.strokeOval(10, 10, 30, 30);
	     gc.setFill(Color.BLACK);
	     gc.fillRect(22, 20, 6, 20);
	
	     //Add the canvas to the button
	     button.setGraphic(canvas);
        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
