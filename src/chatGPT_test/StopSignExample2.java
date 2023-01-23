package chatGPT_test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Source code generated from chatGPT
 * @author chatGBT
 *
 */
public class StopSignExample2 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Glossy Button Example");

        Button button = new Button();
     // Create a canvas
        Canvas canvas = new Canvas(50, 50);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw the stop sign on the canvas
        gc.setFill(Color.RED);
        gc.fillPolygon(new double[]{0, 25, 50}, new double[]{0, 50, 0}, 3);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.fillText("STOP", 15, 30);

        //Add the canvas to the button
        button.setGraphic(canvas);
        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
