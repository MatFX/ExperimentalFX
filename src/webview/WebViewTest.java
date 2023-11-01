package webview;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
import netscape.javascript.JSObject;

public class WebViewTest extends Application {

	public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX WebView Example");

        WebView webView = new WebView();
        webView.getEngine().setJavaScriptEnabled(true);
        webView.getEngine().setUserAgent("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0");
      
      
        webView.getEngine().load("http://192.168.150.142/login?pw=bscgmbh");
     // Update the stage title when a new web page title is available
        webView.getEngine().locationProperty().addListener((obs, oldLocation, newLocation) -> 
        {
        	
        	System.out.println("newLocation " + newLocation);
        	
        	
            if (newLocation != null && newLocation.contains("login?")) {
            	 webView.getEngine().load(newLocation);
            }
        });
        
        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) ->
        {
        	System.out.println("newVlaue " + newValue);
//            JSObject window = (JSObject)  webView.getEngine().executeScript("window");
//            window.setMember("java", webView);
//            webView.getEngine().executeScript("console.log = function(message)\n" +
//                "{\n" +
//                "    java.log(message);\n" +
//                "};");
        });
        
        VBox vBox = new VBox(webView);
        Scene scene = new Scene(vBox, 960, 600);

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}