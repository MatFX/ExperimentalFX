package chatGPT_test;

import javafx.application.Application;
import javafx.scene.Scene;
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
public class GlossyButtonExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Glossy Button Example");

        Button button = new Button();
        button.setText("Click Me!");
        button.setTextFill(Color.WHITE);
        button.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        button.setMinSize(150, 50);
        button.setStyle("-fx-background-color: linear-gradient(#ffd65b, #ffa600),\n" +
                "              radial-gradient(center 50% -40%, radius 200%, #ffd65b 45%, #ffa600 50%);\n" +
                "    -fx-background-radius: 6, 5;\n" +
                "    -fx-background-insets: 0, 1;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);\n" +
                "    -fx-text-fill: #1a1a1a;");

        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
