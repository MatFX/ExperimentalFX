package animation;

import javafx.animation.KeyFrame; 
import javafx.animation.KeyValue; 
import javafx.animation.Timeline; 
import javafx.application.Application; 
import javafx.geometry.VPos; 
import javafx.scene.Scene; 
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font; 
import javafx.scene.text.Text; 
import javafx.stage.Stage; 
import javafx.util.Duration; 

public class ExampleAnimatedText extends Application { 
    public static void main(String[] args) { 
        Application.launch(args); 
    } 

    @Override 
    public void start(Stage stage) { 
        Text msg = new Text("Fehler bei Zugriff auf den Aktivierungs-Server. Bitte prüfen Sie Ihre Internetverbindung und versuchen die Aktivierung erneut!"); 
        msg.setStyle("-fx-background-color: #FF000080;");
        msg.setTextOrigin(VPos.TOP); 
        msg.setFont(Font.font(24)); 

        Pane root = new Pane(msg); 
        root.setPrefSize(500, 70); 
        Scene scene = new Scene(root); 

        stage.setScene(scene); 
        stage.setTitle("Scrolling Text"); 
        stage.show(); 

        double sceneWidth = scene.getWidth(); 
        double msgWidth = msg.getLayoutBounds().getWidth(); 

        System.out.println("msg.translateXProperty(), " + msg.translateXProperty().get());
        System.out.println("sceneWidth " +sceneWidth);
        KeyValue initKeyValue = new KeyValue(msg.translateXProperty(), sceneWidth); 
        System.out.println("initKeyValue " + initKeyValue);
        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue); 

        KeyValue endKeyValue = new KeyValue(msg.translateXProperty(), -1.0 * msgWidth);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(10), endKeyValue); 

        Timeline timeline = new Timeline(initFrame, endFrame); 

        timeline.setCycleCount(Timeline.INDEFINITE); 

        timeline.play(); 
        
       scene.widthProperty().addListener( (prop, oldValue , newValue) -> { 
           KeyValue kv = 
                new KeyValue(msg.translateXProperty(), scene.getWidth()); 
           KeyFrame kf = new KeyFrame(Duration.ZERO, kv); 
           timeline.stop(); 
           timeline.getKeyFrames().clear(); 
           timeline.getKeyFrames().addAll(kf, endFrame); 
           timeline.play(); 
       }); 
    } 
}
