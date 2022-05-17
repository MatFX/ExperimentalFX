package canvas;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

public class PieCanvas extends Application
{

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		
		primaryStage.setTitle("Test pie canvas");
        Group root = new Group();
        Canvas canvas = new Canvas(300, 250);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
		
	}
	
	private void drawShapes(GraphicsContext gc) 
	{
		gc.beginPath();
		//Left corner top
		gc.setFill(Color.RED);
		gc.moveTo(18, 18);
		gc.lineTo(18, 0);
		gc.moveTo(18, 18);
		gc.lineTo(0, 18);
		gc.closePath();
		gc.arc(18, 18, 18, 18, 90, 90);
		gc.fill();
		gc.closePath();
		
		//right corner top
		gc.beginPath();
		gc.setFill(Color.BLUE);
		gc.moveTo(22, 18);
		gc.lineTo(22, 0);
		gc.moveTo(22, 18);
		gc.lineTo(40, 18);
		gc.closePath();
		
		gc.arc(22, 18, 18, 18, 0, 90);
		gc.fill();
		gc.closePath();
		
		
		
		//left corner bottom
		gc.beginPath();
		gc.setFill(Color.GREEN);
		gc.moveTo(18, 22);
		gc.lineTo(18, 40);
		gc.moveTo(18, 22);
		gc.lineTo(0, 22);
		gc.closePath();
		
		gc.arc(18, 22, 18, 18, 180, 90);
		gc.fill();
		gc.closePath();
		
		//right corner bottom
		gc.beginPath();
		gc.setFill(Color.FIREBRICK);
		gc.moveTo(22, 22);
		gc.lineTo(22, 40);
		gc.moveTo(22, 22);
		gc.lineTo(40, 22);
		gc.closePath();
		
		gc.arc(22, 22, 18, 18, 270, 90);
		gc.fill();
		gc.closePath();
		
		
		
		
		
		
		
		//gc.fillArc(0, 0, 200, 200, 0, 90, ArcType.OPEN);
		
		
		//gc.strokeArc(0, 0, 200, 200, 0, 90, ArcType.OPEN);
		
		/*
		gc.beginPath();
		gc.moveTo(18, 18);
		gc.lineTo(18, 0);
		gc.moveTo(18, 18);
		gc.lineTo(0, 18);
		
		gc.moveTo(18, 18);
		gc.arcTo(18, 0, 0, 18, 9);
		//gc.arcTo(0, 0, 75, 72, 4.5);
	
	    gc.stroke();
		
		//gc.moveTo(18, 18);
		//gc.lineTo(18, 0);
		
		//gc.arcTo(18, 0, 0, 18, 9);
		//gc.lineTo(18, 0);
		
		gc.closePath();
		*/
		
		
	}

	public static void main(String[] args) {
	        launch(args);
	}

}
