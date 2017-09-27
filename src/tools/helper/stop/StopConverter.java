package tools.helper.stop;


import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tools.helper.GenericPair;
import tools.helper.Tools;
import tools.helper.stop.xml.StopList;
import tools.helper.stop.xml.StopValue;

public class StopConverter extends Application 
{
	private TextArea importArea, exportArea;
	

	@Override
	public void start(Stage stage) throws Exception {
		BorderPane pane = new BorderPane();
		VBox vBox = new VBox(3);
		vBox.setPadding(new Insets(5,5,5,5));
		
		Button convertButton = new Button("Wandlung");
		convertButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) 
			{
				exportArea.clear();
				
				//Der wird dann später in der anderen textarea ausgeben
				StringBuilder ausgabe = new StringBuilder("Stop[] stopArray = new Stop[]{\n");
				
			
				
				
				/* Test ob xml objekt korrekt sind
				StopList toWrite = new StopList();
				
				List<StopValue> test = new ArrayList<StopValue>();
				test.add(new StopValue("offsetEintrag1", "styleEintrag1"));
				test.add(new StopValue("offsetEintrag2", "styleEintrag2"));
				toWrite.setStopList(test);
				
				writeObjectToFile(toWrite);
				*/
				
				//Übernahme des String aus textarea und einfassen in stoplist
				StringBuilder sb = new StringBuilder("");
				sb.append("<stopList>");
				String valueFromArea = importArea.getText();
				sb.append(valueFromArea);
				sb.append("</stopList>");
				
				//auslesen 
				Object whichObject = (Object) readObject(StopList.class, sb.toString());
				System.out.println("which " +whichObject.toString());
				if(whichObject != null)
				{
					if(whichObject instanceof StopList)
					{
						List<StopValue> stopValueList = ((StopList)whichObject).getStopValueList();
						System.out.println("was ist " + stopValueList.size());
						for(int i = 0; i < stopValueList.size(); i++)
						{
							
							StopValue stopValue = stopValueList.get(i);
							System.out.println("stopValueList " + stopValueList.get(i).toString());
							double orientationOffset = Double.parseDouble(stopValue.getOffset());
							
							String finalColorValue = "";
							//ist opacity vorhanden?
							String styleString = stopValue.getStyle();
							int indexLastStyleTuple = styleString.indexOf("stop-opacity:");
							if(indexLastStyleTuple != -1)
							{
								//dann vorhanden und wir müssen es anders zerlegen
								
								//semikolon zerlegen
								
								//anschließend :
								//<stop  offset="0.2006963" style="stop-color:#B7B7B7;stop-opacity:0.4446974"/>
								String rawColor = "";
								String[] splitValue = styleString.split(";");
								
								GenericPair<String, String> pair = new GenericPair<String, String>(splitValue[0], splitValue[1]);
								
								//wir gehen davon aus, dass immer die Farbe 6 stellig ist (SVG kann auch anders!)
								String stopColorValue = pair.getLeft().split(":")[1];
								
								//die muss berechnet werdne
								String stopOpactiy = pair.getRight().split(":")[1];
								double percentValue = Double.parseDouble(stopOpactiy);
								percentValue = 255D * percentValue;
								
								int value = (int) Math.round(percentValue);
								
								String alphaHex = Tools.toHexString(value, false);
								System.out.println("alphaHex " + alphaHex);
								
								if(alphaHex != null && alphaHex.length() == 2)
								{
									finalColorValue  = stopColorValue + alphaHex;
								}
								//in dem Fall wird die opacity auf 00 gesetzt
								else
								{
									finalColorValue  = stopColorValue + "00";
									
								}
							}
							else
							{
								String[] splitValue = styleString.split(":");
								finalColorValue = splitValue[1];
							}
							
							
							ausgabe.append("\tnew Stop("+orientationOffset+", Color.web(\""+finalColorValue+"\"))");
							//noch weitere vorhanden?
							if((i + 1) < stopValueList.size())
							{
								ausgabe.append(",");
							}
							ausgabe.append("\n");
						}
						ausgabe.append("};");
						exportArea.setText(ausgabe.toString());
					}
					
				
				}
			}
		});
		
		Button clear = new Button("Loeschen");
		clear.setOnAction(new EventHandler<ActionEvent>()
        {

			@Override
			public void handle(ActionEvent event) 
			{
				importArea.clear();
				exportArea.clear();
			}
        	
        });
	    HBox initLinie = new HBox(5);
	    initLinie.setPadding(new Insets(5,5,5,5));
	    initLinie.getChildren().addAll(convertButton, clear);
		
		vBox.getChildren().add(initLinie);
		
		
		GridPane gridPane = new GridPane();
		gridPane.setHgap(5);
		gridPane.setVgap(5);
		RowConstraints row = new RowConstraints();
	    row.setVgrow(Priority.ALWAYS);
	    gridPane.getRowConstraints().add(0, row);
	    gridPane.getRowConstraints().add(1, row);
		//zwei textfelder für eingabe ausgabe
		
		//zwei button; Convert und lösche Textinhalt
	    Label labelImport = new Label("Inhalt SVG:");
        Label labelExport = new Label("Export-Daten:");
		
        gridPane.add(labelImport, 0, 0);
        gridPane.add(labelExport, 1, 0);
        
        
        importArea = new TextArea();
        exportArea = new TextArea();
        
        gridPane.add(importArea, 0, 1);
        gridPane.add(exportArea, 1, 1);
		
        vBox.getChildren().add(gridPane);
        pane.setCenter(vBox);
        
        Scene scene = new Scene(pane, 800, 400);
        

        stage.setTitle("Konvertierung Stop Punkte");
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
	
	/**
	 * Auslesen des Strings und Objekt befüllen
	 * @return
	 */
	public static Object readObject(Class<?> klasse, String value)
	{
		try 
		{
			JAXBContext jc = JAXBContext.newInstance(klasse);
			Unmarshaller u = jc.createUnmarshaller();
			StringReader reader = new StringReader(value);
			Object returnValue = (Object)u.unmarshal(reader);
			return returnValue;
		}
		catch(UnmarshalException e)
		{
			e.printStackTrace();
		}
		catch (JAXBException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean writeObjectToFile(StopList stopList)
	{
		try 
    	{
    		JAXBContext jc = JAXBContext.newInstance(stopList.getClass());
    		Marshaller m = jc.createMarshaller();
    		//Formatierung, damit es übersichtlich ist, falls mal jemand reinschaut
    		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); 
    		m.marshal(stopList, new File("test.xml"));
    		return true;
    		
    	} 
    	catch (JAXBException e) 
    	{
    		e.printStackTrace();
    	}
    	catch(NullPointerException e)
    	{
    		e.printStackTrace();
    	}
		return false;
	}

	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
