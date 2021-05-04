package sensorpanel.first.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import control.universaldisplay.SensorValue;

public class HelperClass
{

	public static final int TEMPERATURE = 0;
	public static final int HUMIDITY = 1;
	public static final int BRIGHTNESS = 2;
	
	
	
	private static HashMap<Integer, List<SensorValue>> sensorMap = new HashMap<Integer, List<SensorValue>>();

	public int currentSensorToShow = 0;

	private List<SensorValue> sensorList;
	
	public HelperClass()
	{
		sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(25, -40, 60, "°C", "img_temperatur", "Temperatur:"));
		//sensorList.add(new SensorValue(22.5, 8, 40, "°C", "", new double[]{21.5, 24.5, 30}));
		sensorMap.put(TEMPERATURE, sensorList);
		
		sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(25.4, 0, 100, "%", "img_feuchtigkeit", "Feuchtigkeit:"));
		sensorMap.put(HUMIDITY, sensorList);
		
		sensorList = new ArrayList<SensorValue>();
		sensorList.add(new SensorValue(2500, 0, 100000, "Lux", "img_helligkeit", "Helligkeit:"));
		sensorMap.put(BRIGHTNESS, sensorList);
		
		
	}

	public int getCurrentSensorToShow() {
		return currentSensorToShow;
	}

	public void addCurrentSensorToShow() {
		currentSensorToShow ++;
		
	}

	public void setCurrentSensorToShow(int val) {
		currentSensorToShow = val;
		
	}

	public int getMapSize() 
	{
		return sensorMap.size();
	}

	public SensorValue getSelectedSensorValue() 
	{
		return sensorMap.get(currentSensorToShow).get(0);
	}

	public void subCurrentSensorToShow() {
		currentSensorToShow --;
		
	}
	
	
	
	

}
