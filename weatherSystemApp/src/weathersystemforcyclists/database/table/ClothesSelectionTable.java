package weathersystemforcyclists.database.table;

import java.sql.Timestamp;

public class ClothesSelectionTable {
	private int tainingID;
	public int getTainingID() {
		return tainingID;
	}
	public void setTainingID(int tainingID) {
		this.tainingID = tainingID;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
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
	public int getComfortRate() {
		return comfortRate;
	}
	public void setComfortRate(int comfortRate) {
		this.comfortRate = comfortRate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Timestamp startTime;
	public float temperature;
	public float humidity;
	public float airPressure;
	public int comfortRate;
	public String comment;
}
