package list;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.stream.Stream;


public class SlideMenuDemo extends Application {
    /* THE ONLY DRAWBACK OF THIS APPROACH IS YOU HAVE TO KNOW THE WIDTH OF THE SUBMENU AHEAD*/
    double subMenuWidth = 140;

    HBox root;
    VBox subMenu;
    VBox menuPane;
    StackPane subMenuPane;

    private Rectangle clipRect;
    private Timeline timelineHide;
    private Timeline timelineShow;

    private final Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.LAVENDER, Color.PINK};
    private final String[] shapes = {"cirlce", "triangle", "square", "rectangle"};

    @Override
    public void start(Stage primaryStage) {
        root = new HBox();
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Sliding Demo");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Main menu pane
        menuPane = new VBox();
        menuPane.setStyle("-fx-background-color:#DDDDDD;");
        Stream.of(shapes).forEach(shape -> menuPane.getChildren().addAll(buildMenuButton(shape, Color.BLACK, null)));

        // Sub menu pane
        subMenu = new VBox();
        subMenuPane = new StackPane(subMenu);
        subMenuPane.setMinWidth(0);
        subMenuPane.setPrefWidth(0);

        StackPane subMenuContainer = new StackPane(subMenuPane);
        subMenuContainer.setStyle("-fx-background-color:#AAAAAA");
        HBox menuBox = new HBox(menuPane, subMenuContainer);
        menuBox.setOnMouseExited(e -> hideSubMenu());

        // Content Pane
        StackPane contentPane = new StackPane(new Text("Hello Slide Checking"));
        contentPane.setAlignment(Pos.TOP_LEFT);
        contentPane.setPadding(new Insets(15));
        HBox.setHgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color:#0000FF70,#FFFFFF;-fx-background-insets:0,1;");

        root.getChildren().addAll(menuBox, contentPane);
        setAnimation();
    }

    private void hideSubMenu() {
        timelineHide.play();
    }

    private void showSubMenu() {
        timelineShow.play();
    }

    private void setAnimation() {
        clipRect = new Rectangle();
        clipRect.setWidth(0);
        clipRect.heightProperty().bind(root.heightProperty());
        clipRect.translateXProperty().set(subMenuWidth);
        subMenuPane.setClip(clipRect);
        subMenuPane.translateXProperty().set(-subMenuWidth);

        /* Event handler hide is finished. */
        EventHandler<ActionEvent> onFinished = e -> {
            menuPane.getChildren().stream().forEach(n -> n.setStyle(null));
            subMenu.getChildren().clear();
        };

        timelineShow = new Timeline();
        timelineHide = new Timeline();

        /* Animation for show. */
        timelineShow.setCycleCount(1);
        final KeyValue kvDwn1a = new KeyValue(clipRect.widthProperty(), subMenuWidth);
        final KeyValue kvDwn1b = new KeyValue(subMenuPane.prefWidthProperty(), subMenuWidth);
        final KeyValue kvDwn1c = new KeyValue(subMenuPane.minWidthProperty(), subMenuWidth);
        final KeyValue kvDwn2 = new KeyValue(clipRect.translateXProperty(), 0);
        final KeyValue kvDwn3 = new KeyValue(subMenuPane.translateXProperty(), 0);
        final KeyFrame kfDwn = new KeyFrame(Duration.millis(200), kvDwn1a, kvDwn1b, kvDwn1c, kvDwn2, kvDwn3);
        timelineShow.getKeyFrames().add(kfDwn);

        /* Animation for hide. */
        timelineHide.setCycleCount(1);
        final KeyValue kvUp1a = new KeyValue(clipRect.widthProperty(), 0);
        final KeyValue kvUp1b = new KeyValue(subMenuPane.prefWidthProperty(), 0);
        final KeyValue kvUp1c = new KeyValue(subMenuPane.minWidthProperty(), 0);
        final KeyValue kvUp2 = new KeyValue(clipRect.translateXProperty(), subMenuWidth);
        final KeyValue kvUp3 = new KeyValue(subMenuPane.translateXProperty(), -subMenuWidth);
        final KeyFrame kfUp = new KeyFrame(Duration.millis(200), onFinished, kvUp1a, kvUp1b, kvUp1c, kvUp2, kvUp3);
        timelineHide.getKeyFrames().add(kfUp);
    }

    private StackPane buildMenuButton(String type, Color color, String text) {
        double size = 50;
        double sSize = (size / 5) * 4;
        double hSize = sSize / 2;
        StackPane menuButton = new StackPane();
        menuButton.setPadding(new Insets(0, 5, 0, 5));
        menuButton.setMaxHeight(size);
        menuButton.setMinHeight(size);

        Node shape = null;
        switch (type) {
            case "triangle":
                StackPane s = new StackPane();
                s.setPrefSize(sSize, sSize);
                s.setMaxSize(sSize, sSize);
                s.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                s.setStyle("-fx-shape:\"M0 1 L1 1 L.5 0 Z\";");
                s.setPadding(new Insets(hSize));
                shape = s;
                break;
            case "square":
                shape = new Rectangle(sSize, sSize, color);
                break;
            case "rectangle":
                shape = new Rectangle(sSize, hSize, color);
                break;
            default:
                shape = new Circle(hSize, color);
                break;
        }
        HBox hb = new HBox(shape);
        hb.setAlignment(Pos.CENTER_LEFT);
        if (text != null) {

            hb.setSpacing(10);
            hb.getChildren().add(new Label(text));
        }
        menuButton.getChildren().add(hb);

        if (text == null) {
            // Main menu button
            menuButton.setOnMouseEntered(e -> {
                menuPane.getChildren().stream().forEach(n -> n.setStyle(null));
                subMenu.getChildren().clear();
                menuButton.setStyle("-fx-background-color:#AAAAAA;");
                Stream.of(colors).forEach(c -> subMenu.getChildren().addAll(buildMenuButton(type, c, c.toString())));
                if (subMenuPane.getWidth() == 0) {
                    showSubMenu();
                }
            });
        } else {
            // Sub menu button
            menuButton.setPrefWidth(subMenuWidth);
            menuButton.setMinWidth(subMenuWidth);
            menuButton.setOnMouseEntered(e -> menuButton.setStyle("-fx-background-color:#777777;"));
            menuButton.setOnMouseExited(e -> menuButton.setStyle(null));
        }
        return menuButton;
    }

    public static void main(String[] args) {
        launch(args);
    }
}