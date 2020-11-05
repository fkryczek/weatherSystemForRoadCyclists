package weathersystemforcyclists.database.table;

import java.sql.Timestamp;

public class Training {

	private int tainingID;
	private int measurementID;
	private int clothesID;
	private Timestamp startTime;
	private Timestamp endTime;

	public int getTainingID() {
		return tainingID;
	}
	public void setTainingID(int tainingID) {
		this.tainingID = tainingID;
	}
	public int getMeasurementID() {
		return measurementID;
	}
	public void setMeasurementID(int measurementID) {
		this.measurementID = measurementID;
	}
	public int getClothesID() {
		return clothesID;
	}
	public void setClothesID(int clothesID) {
		this.clothesID = clothesID;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

}
