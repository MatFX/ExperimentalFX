package canvas;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ArrowCanvas extends Application 
{
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Test pie canvas");
        Group root = new Group();
        Canvas canvas = new Canvas(300, 250);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
		
	}
	
	private void drawShapes(GraphicsContext gc) 
	{
		gc.beginPath();
		gc.setFill(Color.RED);
		
		
		double widthArrow = 17;
		double heightArrow = 17;
		
		double centerX = widthArrow / 2d;
		double centerY = heightArrow / 2d;
		
		
		double x = 0;
		double y = 0;
		
		double borderGap = 1; // 0.0769
		double xGapArrowCorner = 7; //0.5385
		double xGapTop = 8; // 0.6154
		double yGapArrowCorner = 4; //0.2353
		double arrowBodyHeight = 7; 
		
		//p1
		gc.moveTo(x + borderGap + xGapArrowCorner, y + borderGap);
		//p2
		gc.lineTo(x + borderGap + xGapArrowCorner,  y + borderGap + yGapArrowCorner);
		//p3
		gc.lineTo(x + borderGap, y + borderGap + yGapArrowCorner);
		//p4
		gc.lineTo(x + borderGap, y + borderGap + yGapArrowCorner + arrowBodyHeight);
		//p5
		gc.lineTo(x + borderGap + xGapArrowCorner, y + borderGap + yGapArrowCorner + arrowBodyHeight);
		//p6
		gc.lineTo(x + borderGap + xGapArrowCorner, y + borderGap + yGapArrowCorner + arrowBodyHeight + yGapArrowCorner);
		//p7 
		gc.lineTo(x + borderGap + xGapArrowCorner + xGapTop,  y + borderGap + yGapArrowCorner + yGapArrowCorner);
		gc.closePath();
		
		
		gc.fill();
		gc.stroke();
		
		System.out.println(" " + gc.getCanvas().getWidth());
		
		
		
	}


	public static void main(String[] args) {
	        launch(args);
	}
}
