package transparent;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TransparentBorderPane extends BorderPane
{
	private double startX = 0, startY = 0;
	
	private Stage stage;
	
	public TransparentBorderPane(Stage primaryStage)
	{
		//this.setId("language_scene");
		super();
		this.setTop(new Label("Testfenster"));
		
		
		
		stage = primaryStage;

		this.setOnMouseMoved(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event)
			{
				//Hier die Prüfung ob der Cursor verändert werden muss bezieht sich auf das resize
				Cursor value = findCursorPosition(event.getSceneX(), event.getSceneY(), event);
				//Alles anzeigen außer move, weil das nervt ansonsten nur auf der Oberfläche
				//nur s, w und sued-west für den resize die andere Seite flackert und ich weiß nicht wieso
				if(value != Cursor.MOVE)
				{
					//setNewInitialEventCoordinates(event);
					stage.getScene().setCursor(value);
				
					
					
				
				}
				
					
			}
		});
		
		this.setOnMousePressed(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				startX = event.getSceneX();
				startY = event.getSceneY();
				//Es gibt bei diesem Dialog nur ein Move
				Cursor value = findCursorPosition(startX, startY, event);
				stage.getScene().setCursor(value);
			}
		});
		
		
		this.setOnMouseDragged(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) 
			{
				
				
				if(stage.getScene().getCursor() == Cursor.MOVE)
				{
					 stage.getScene().getWindow().setX( event.getScreenX() - startX );
					 stage.getScene().getWindow().setY( event.getScreenY() - startY);
				}
			}
			
		});
		
		this.setOnMouseReleased(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				stage.getScene().setCursor(Cursor.DEFAULT);
			}
			
		});
	}
	
	
	//Es wird hier kein resize geben
	private Cursor findCursorPosition(double startX, double startY, MouseEvent event) 
	{
		if(event.getEventType() == MouseEvent.MOUSE_PRESSED)
			return Cursor.MOVE;
		
		return Cursor.DEFAULT;
	}
	
	

}
