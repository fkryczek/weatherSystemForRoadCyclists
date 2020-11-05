package weathersystemforcyclists.database.table;

import java.sql.Timestamp;

public class Weather {
	
	private int measurementID;
	private float temperature;
	private float humidity;
	private float airPressure;
	private Timestamp measurementDate;
	
	public int getMeasurementID() {
		return measurementID;
	}
	public void setMeasurementID(int measurementID) {
		this.measurementID = measurementID;
	}
	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	public float getHumidity() {
		return humidity;
	}
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
	public float getAirPressure() {
		return airPressure;
	}
	public void setAirPressure(float airPressure) {
		this.airPressure = airPressure;
	}
	public Timestamp getMeasurementDate() {
		return measurementDate;
	}
	public void setMeasurementDate(Timestamp timestamp) {
		this.measurementDate = timestamp;
	}
}
