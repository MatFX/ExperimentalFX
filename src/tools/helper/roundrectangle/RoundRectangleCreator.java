package tools.helper.roundrectangle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class RoundRectangleCreator extends Application
{
	//sind die Maße der kompletten Zeichenfläche
	private TextField initBreite, initHoehe;
	//Eingabebereich 
	private TextField x, y, w, h, arcW;//, arcH;
	
	private TextArea exportArea;

	public static void main(String[] args) 
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception 
	{
		
		BorderPane pane = new BorderPane();
		VBox vBox = new VBox(3);
        vBox.setPadding(new Insets(5,5,5,5));
        HBox initLinie = new HBox(5);
        initLinie.setPadding(new Insets(5,5,5,5));
        
        initBreite = new TextField("150.0");
        initHoehe = new TextField("60.0");
        
        Button convertButton = new Button("create");
        convertButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) 
			{
				try
				{
					double xVal = Double.parseDouble(x.getText());
					double yVal = Double.parseDouble(y.getText());
					double wVal = Double.parseDouble(w.getText());
					double hVal = Double.parseDouble(h.getText());
					double arcWVal = Double.parseDouble(arcW.getText());
					//wird nicht benötigt für meine rechtecke
					//double arcHVal = Double.parseDouble(arcH.getText());
					
					
					double oneHundredW = Double.parseDouble(initBreite.getText());
					double oneHundredH = Double.parseDouble(initHoehe.getText());
					
					
					//anschließend die Prozentwerteberechnen
					
					double xPercent = 100D / oneHundredW * xVal / 100D;
					double yPercent = 100D / oneHundredH * yVal / 100D;
					double wPercent = 100D / oneHundredW * wVal/ 100D; 
					double hPercent = 100D / oneHundredH * hVal/ 100D;
					
					double arcWPercent = 100D / oneHundredW * arcWVal / 100D;
					
					StringBuilder sb = new StringBuilder("rectangle = new Rectangle();\n");
					sb.append("rectangle.setX(w * "  + xPercent+");\n");
					sb.append("rectangle.setY(h * "  + yPercent+");\n");
					sb.append("rectangle.setWidth(w * "  + wPercent+");\n");
					sb.append("rectangle.setHeight(h * "  + hPercent+");\n");
					sb.append("rectangle.setArcWidth(w * "  + arcWPercent+");\n");
					sb.append("rectangle.setArcHeight(w * "  + arcWPercent+");\n");
					
					exportArea.setText(sb.toString());
					
				}
				catch(Exception e)
				{
				}
				
				
			}});
        
        Button clear = new Button("clear");
        clear.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) 
			{
				x.clear();
				y.clear();
				w.clear();
				h.clear();
				arcW.clear();
				//arcH.clear();
				exportArea.clear();
			}
        });
		
		
		initLinie.getChildren().addAll(new Label("Breite: "), initBreite, new Label("Hoehe: "), initHoehe, convertButton, clear);
	    vBox.getChildren().add(initLinie);
		
	   
	    GridPane gridPane = new GridPane();
    	gridPane.setHgap(5);
		gridPane.setVgap(5);
        RowConstraints row = new RowConstraints();
        row.setVgrow(Priority.ALWAYS);
		gridPane.getRowConstraints().add(0, row);
		gridPane.getRowConstraints().add(1, row);
		gridPane.getRowConstraints().add(2, row);
		gridPane.getRowConstraints().add(3, row);	
    
		
		HBox boxX = new HBox(5);
		boxX.setPadding(new Insets(5,5,5,5));
		x = new TextField();
		
		boxX.getChildren().addAll(new Label("     x:"), x);
		
		
		y = new TextField();
		HBox boxY = new HBox(5);
		boxY.setPadding(new Insets(5,5,5,5));
		boxY.getChildren().addAll(new Label("     y:"), y);
		
        gridPane.add(boxX, 0, 0);
        gridPane.add(boxY, 1, 0);
		
    	HBox boxW = new HBox(5);
		boxW.setPadding(new Insets(5,5,5,5));
		w = new TextField();
		boxW.getChildren().addAll(new Label("     w:"), w);
		
		HBox boxH = new HBox(5);
		boxH.setPadding(new Insets(5,5,5,5));
		h = new TextField();
		boxH.getChildren().addAll(new Label("     h:"), h);
        
		gridPane.add(boxW, 0, 1);
		gridPane.add(boxH, 1, 1);
	    
		
		HBox boxArcW = new HBox(5);
		boxArcW.setPadding(new Insets(5,5,5,5));
		arcW = new TextField();
		boxArcW.getChildren().addAll(new Label("arcW:"), arcW);
		
		
		HBox boxArcH = new HBox(5);
		boxArcH.setPadding(new Insets(5,5,5,5));
		//arcH = new TextField();
		//boxArcH.getChildren().addAll(new Label("arcH:"), arcH);
		
		gridPane.add(boxArcW, 0, 2);
		//gridPane.add(boxArcH, 1, 2);
		
		
		exportArea = new TextArea();
		
	    
	    vBox.getChildren().addAll(gridPane, exportArea);
	    
	    
	   
	    
	    
	    
	    pane.setCenter(vBox);
		Scene scene = new Scene(pane, 600, 350);
   

        stage.setTitle("Erzeugt Rundes Rechteck");
        stage.setScene(scene);
        stage.show();
        
        //closing event with stop animation
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
        {
            @Override
            public void handle(WindowEvent event) {
            	stage.close();
            }
        });
		
		
		
		
	}

}
