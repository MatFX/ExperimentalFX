package list;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import list.ListCellFactory.SWIPE_USE;

public class ExampleCellFactoryList extends Application  {

	
	private ListView<SampleItem> listView;
	
	private ComboBox<String> selectedGateway;
	
	
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		
		
		BorderPane borderPane = new BorderPane();
		
		VBox contentBox = new VBox(5);
		contentBox.setPadding(new Insets(5,5,5,5));
		
		selectedGateway = new ComboBox<String>();
		List<String> itemList = new ArrayList<String>();
		itemList.add("Gateway 1");
		itemList.add("Gateway 2");
		ObservableList<String> obsList = FXCollections.observableArrayList(itemList);
		selectedGateway.getItems().addAll(obsList);
		selectedGateway.getSelectionModel().select(1);
		
		selectedGateway.valueProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue != null)
				{
					refreshListView();
					
				}
				
			}
			
		});
	
		
		contentBox.getChildren().add(selectedGateway);
		
		
		listView = new ListView<SampleItem>();
		listView.setCellFactory(c -> new ListCellFactory(SWIPE_USE.SWIPE_LEFT_ALLOWED));
	
		VBox.setVgrow(listView, Priority.ALWAYS);
		contentBox.getChildren().add(listView);
		
		
		borderPane.setCenter(contentBox);
		
		
		refreshListView();
		Scene scene = new Scene(borderPane, 300, 300);
		
		primaryStage.setTitle("Image Colorizer");
		primaryStage.setScene(scene);
		
		primaryStage.show();
		
	}

	protected void refreshListView() 
	{
		
		//TODO
		
		listView.getItems().clear();

		ObservableList<SampleItem> obsList = null;
		if(selectedGateway.getSelectionModel().getSelectedIndex() == 0)
		{
			obsList = FXCollections.emptyObservableList();
		}
		else
		{
			List<SampleItem> itemList = new ArrayList<SampleItem>();
			itemList.add(new SampleItem(1));
			itemList.add(new SampleItem(2));
			itemList.add(new SampleItem(3));
			itemList.add(new SampleItem(4));
			
			obsList = FXCollections.observableArrayList(itemList);
			
			
		}
		
		listView.getItems().addAll(obsList);
		
	}
	
	
	
	

}
