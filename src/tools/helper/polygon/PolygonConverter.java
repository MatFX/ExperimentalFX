package tools.helper.polygon;

import java.util.Map.Entry;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tools.helper.GenericPair;




public class PolygonConverter extends Application 
{
	private TextField initBreite, initHoehe;
   
	private TableView<DoubleContainer> svgWerteTabelle;
	
	private TableView<DoubleContainer> ergebnisWerteTabelle;
	
	/**
	 * Inhalt die in den zweite TableViews dargestellt werden.
	 */
	private TreeMap<Integer, GenericPair<DoubleContainer, DoubleContainer>> tableMap = new TreeMap<Integer, GenericPair<DoubleContainer, DoubleContainer>>();

	/**
	 * Der Bereich fÃ¼r das Ã¼bernehmen von svg polygon werte
	 */
	private TextArea importArea, exportArea;
	
    @Override 
    public void start(Stage stage) 
    {
        BorderPane pane = new BorderPane();
        
        VBox vBox = new VBox(3);
        vBox.setPadding(new Insets(5,5,5,5));
        HBox initLinie = new HBox(5);
        initLinie.setPadding(new Insets(5,5,5,5));
        //Breite/Hoehe kann verändert werden; Änderung werden übernommen durch button resize
        initBreite = new TextField("46.0");
        initHoehe = new TextField("64.0");
        
        
        Button importButton = new Button("Import/Resize");
        importButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) 
			{
				double calcW = Double.parseDouble(initBreite.getText());
		    	double calcH = Double.parseDouble(initHoehe.getText());
		    	clearView();
				
				//werte aus area zerlegen und aufbereitet in map eblegen
				String valueFromArea = importArea.getText();
				
				int nextLine = 0;
				//leerzeichen und komma sind trenner
				
				//erstmal die paare herausfinden durch komma weghauen
				String[] splitedValues = valueFromArea.split(" ");
				for(int i = 0; i < splitedValues.length; i++)
				{
					String tempValue = splitedValues[i];
					GenericPair<DoubleContainer, DoubleContainer> lineContainer = null;
					if(tempValue.contains(","))
					{
						String[] xyValues = tempValue.split(",");
						
						try
						{
							Double xValue = Double.parseDouble(xyValues[0]);
							Double yValue = Double.parseDouble(xyValues[1]);
							lineContainer = new GenericPair<DoubleContainer, DoubleContainer>();
							DoubleContainer leftValue = new DoubleContainer(xValue, yValue);
							lineContainer.setLeft(leftValue);
							
							double calculatedXFactor = 100D / calcW * leftValue.getXValue() / 100D;
							double calculatedYFactor = 100D / calcH * leftValue.getYValue() / 100D;
							
							lineContainer.setRight(new DoubleContainer(calculatedXFactor, calculatedYFactor));
							
							
							
						}
						catch(Exception e)
						{
							//wenn es scheppern sollte? alles verwerfen?
						}
					}
					
					if(lineContainer != null)
					{
						
						tableMap.put(nextLine, lineContainer);
						nextLine++;
					}
					
					
				}
				
				if(tableMap.size() > 0)
				{
					ObservableList<DoubleContainer> leftSideList = FXCollections.<DoubleContainer>observableArrayList();
					ObservableList<DoubleContainer> rightSideList = FXCollections.<DoubleContainer>observableArrayList();
					//TODO 
					for(Entry<Integer, GenericPair<DoubleContainer, DoubleContainer>> entry : tableMap.entrySet())
					{
						System.out.println(" ergebnisWerteTabelle " + ergebnisWerteTabelle);
						
						entry.getValue().getLeft().getDoubleXProperty().addListener(new ChangeListener<Number>()
						{

							@Override
							public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) 
							{
								System.out.println("new Value " + arg2.toString());
								//changeXValue(arg2);
							}
							
						});
						leftSideList.add(entry.getValue().getLeft());
						rightSideList.add(entry.getValue().getRight());
						
						
					}
					//svgWerteTabelle.getColumns().get(0).setVisible(false);
					//svgWerteTabelle.getColumns().get(0).setVisible(true);
					
					svgWerteTabelle.getItems().addAll(leftSideList);
					ergebnisWerteTabelle.getItems().addAll(rightSideList);
					
					exportCalculatedValues(rightSideList);
					
				}
			}

        	
        });
        //Loescht den Inhalt der Tabellen
        Button clear = new Button("Loeschen");
        clear.setOnAction(new EventHandler<ActionEvent>()
        {

			@Override
			public void handle(ActionEvent event) 
			{
				clearView();
				importArea.clear();
				exportArea.clear();
			}
        	
        });
        
        initLinie.getChildren().addAll(new Label("Breite: "), initBreite, new Label("Hoehe: "), initHoehe, importButton, clear);
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
    
		
		
		Label labelTableImport = new Label("Tabelle SVG:");
        Label labelTableExport = new Label("Tabelle Export-Daten:");
        
        gridPane.add(labelTableImport, 0, 0);
        gridPane.add(labelTableExport, 1, 0);
		
		ScrollPane scrollLeft = new ScrollPane();
		scrollLeft.setFitToWidth(true);
	    ScrollPane scrollRight = new ScrollPane();
	    scrollRight.setFitToWidth(true);
        svgWerteTabelle = new TableView<DoubleContainer>();
        //set editable on svg table
        svgWerteTabelle.setEditable(true);
        ergebnisWerteTabelle = new TableView<DoubleContainer>();
      
        svgWerteTabelle.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
       // HBox.setHgrow(svgWerteTabelle, Priority.ALWAYS);
       
        //zwingend erfoderlich dass jede Tabelle seine eigene ColumnDefintion erhÃ¤lt
        TableColumn<DoubleContainer, Double> xSVGCol = new TableColumn<DoubleContainer, Double>("x");
        xSVGCol.setMinWidth(200);
        xSVGCol.setCellValueFactory(new PropertyValueFactory<DoubleContainer, Double>("xValue"));
        
		TableColumn<DoubleContainer, Double> ySVGCol = new TableColumn<DoubleContainer, Double>("y");
		ySVGCol.setMinWidth(200);
		ySVGCol.setCellValueFactory(new PropertyValueFactory<DoubleContainer, Double>("yValue"));  
		

		 
        TableColumn<DoubleContainer, Double> xCol = new TableColumn<DoubleContainer, Double>("x");
        xCol.setMinWidth(200);
        xCol.setCellValueFactory(new PropertyValueFactory<DoubleContainer, Double>("xValue"));
		
		TableColumn<DoubleContainer, Double> yCol = new TableColumn<DoubleContainer, Double>("y");
		yCol.setMinWidth(200);
		yCol.setCellValueFactory(new PropertyValueFactory<DoubleContainer, Double>("yValue"));  
		
		
		
		
		svgWerteTabelle.getColumns().addAll(xSVGCol, ySVGCol);
		
		
		
		ergebnisWerteTabelle.getColumns().addAll(xCol, yCol);
        
        scrollLeft.setContent(svgWerteTabelle);
        scrollRight.setContent(ergebnisWerteTabelle);
		
		gridPane.add(scrollLeft, 0, 1);
        gridPane.add(scrollRight, 1, 1);
        
        
        Label labelImport = new Label("Import SVG:");
        Label labelExport = new Label("Export Double[]:");
        
        gridPane.add(labelImport, 0, 2);
        gridPane.add(labelExport, 1, 2);
        
        
        importArea = new TextArea();
        exportArea = new TextArea();
        
        gridPane.add(importArea, 0, 3);
        gridPane.add(exportArea, 1, 3);
        
        vBox.getChildren().add(gridPane);
        pane.setCenter(vBox);
      
        Scene scene = new Scene(pane, 800, 800);
   

        stage.setTitle("Konvertierung SVG Polygon Punkte");
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
    

	private void exportCalculatedValues(ObservableList<DoubleContainer> rightSideList) 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("new Double[]\n");
		sb.append("{\n");
		for(int i = 0; i < rightSideList.size(); i++)
		{
			DoubleContainer value = rightSideList.get(i);
			sb.append("\t w * " + value.getXValue() + ", h * "  + value.getYValue());
			//letzter Wert?
			if((i + 1) < rightSideList.size())
			{
				//alles i.o es kommt noch was
				sb.append(",\n");
			}
			else
			{
				//beim letzten Wert kein komma
				sb.append("\n");
			}
		}
		sb.append("}\n");
		exportArea.setText(sb.toString());
	}
    
    
    public void clearView()
    {
    	tableMap.clear();
		ergebnisWerteTabelle.getItems().clear();
		svgWerteTabelle.getItems().clear();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
    
  
}


