package weathersystemforcyclists.gui.mainview.clothesselection;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import weathersystemforcyclists.database.DatabaseCommunication;
import weathersystemforcyclists.database.table.Data;
import weathersystemforcyclists.database.table.ClothesSelectionTable;
import weathersystemforcyclists.serial.WeatherStation;

public class ClothesSelectionController {

	private static WeatherStation weatherStation = new WeatherStation();
	private ObservableList<ClothesSelectionTable> dataList = FXCollections.observableArrayList();
	private DatabaseCommunication databaseCommunication = new DatabaseCommunication();
	private Data[] data = new Data[1];

	@FXML
	private Pane clothesSelectionPane;

	@FXML
	private Label outdoorTemperatureLabel;

	@FXML
	private Label outdoorHumidityLabel;

	@FXML
	private Label airPressureLabel;

	@FXML
	private JFXButton getActualOutdoorDataButton;

	@FXML
	private JFXButton getBestMatchButton;

	@FXML
	private TableView<ClothesSelectionTable> table;

	@FXML
	private TableColumn<ClothesSelectionTable, Integer> IDColumn;

	@FXML
	private TableColumn<ClothesSelectionTable, Timestamp> dateColumn;

	@FXML
	private TableColumn<ClothesSelectionTable, Float> tempColumn;

	@FXML
	private TableColumn<ClothesSelectionTable, Float> humColumn;

	@FXML
	private TableColumn<ClothesSelectionTable, Float> pressColumn;

	@FXML
	private TableColumn<ClothesSelectionTable, Integer> rateColumn;

	@FXML
	private TableColumn<ClothesSelectionTable, String> commentColumn;

	@FXML
	private Label onHeadLabel;

	@FXML
	private Label onThoraxLabel;

	@FXML
	private Label onHandsLabel;

	@FXML
	private Label onLegsLabel;

	@FXML
	private Label onFeetLabel;

	@FXML
	private Label selectedIDLabel;

	@FXML
	private Label selectedRateLabel;

	@FXML
	private ProgressBar progressBar;

	public static void setMainStationObject(WeatherStation ws) {
		weatherStation = ws;
	}

	private void setTextInLabel(Label label, String text) {
		Platform.runLater(new Runnable() {
			public void run() {
				label.setText(text);
			}
		});
	}

	private Data[] getBestClothes(Float[] weatherData) {

		int tempTolerance = 0, humTolerance = 0, pressTolerance = 0;
		Data[] processData;
		Data temporaryData = new Data();

		for (int x = 0; x < 10; x++) {
			tempTolerance = tempTolerance + 1;
			humTolerance = humTolerance + 3;
			pressTolerance = pressTolerance + 15;

			processData = databaseCommunication.getBestMatch(weatherData[0] - tempTolerance,
					weatherData[0] + tempTolerance, weatherData[1] - humTolerance, weatherData[1] + humTolerance,
					weatherData[2] - pressTolerance, weatherData[2] + pressTolerance);
			if (processData.length > 1) {
				for (int y = 0; y < processData.length - 1; y++)
					for (int z = 0; z < processData.length - 1; z++) {
						if (processData[z].clothes.getComfortRate() > processData[z + 1].clothes.getComfortRate()) {
							temporaryData = processData[z];
							processData[z] = processData[z + 1];
							processData[z + 1] = temporaryData;

						}
					}
				return processData;
			}

		}

		processData = databaseCommunication.getAllData();
		if (processData.length > 2) {
			for (int y = 0; y < processData.length - 1; y++)
				for (int z = 0; z < processData.length - 1; z++) {
					if (processData[z].clothes.getComfortRate() > processData[z + 1].clothes.getComfortRate()) {
						temporaryData = processData[z];
						processData[z] = processData[z + 1];
						processData[z + 1] = temporaryData;

					}
				}
		}
		return processData;
	}

	@FXML
	void getActualOutdoorData(MouseEvent event) {
		if (weatherStation.getBusyStatus() == false) {
			new Thread(() -> { // Lambda Expression
				int repeat = 0;
				progressBar.setVisible(true);
				getActualOutdoorDataButton.setDisable(true);
				getBestMatchButton.setDisable(true);
				if (weatherStation.isMainStationConnected() && weatherStation.isRemoteStationConnected()) {
					try {

						String[] weatherData;
						do {
							weatherData = weatherStation.getDataToWeatherTable();
							repeat++;
						} while ((weatherData[0].equals("N/A") || weatherData[1].equals("N/A")
								|| weatherData[2].equals("N/A")) && repeat < 5);

						setTextInLabel(outdoorTemperatureLabel, weatherData[0]);
						setTextInLabel(outdoorHumidityLabel, weatherData[1]);
						setTextInLabel(airPressureLabel, weatherData[2]);

						if (!(weatherData[0].equals("N/A") || weatherData[1].equals("N/A")
								|| weatherData[2].equals("N/A"))) {
							LocalDateTime measurementDate = LocalDateTime.now();
							if (!databaseCommunication.setWeatherMeasurement(false, 0, Float.parseFloat(weatherData[0]),
									Float.parseFloat(weatherData[1]), Float.parseFloat(weatherData[2]),
									measurementDate)) {
								getActualOutdoorDataButton.setTextFill(Color.GREEN);
								getActualOutdoorDataButton.setDisable(true);
							}

						} else
							getActualOutdoorDataButton.setDisable(false);

					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				getBestMatchButton.setDisable(false);
				progressBar.setVisible(false);
			}).start();
		}

	}

	@FXML
	void getBestMatch(MouseEvent event) {

		if (outdoorTemperatureLabel.getText().isEmpty() || outdoorHumidityLabel.getText().isEmpty()
				|| airPressureLabel.getText().isEmpty() || outdoorTemperatureLabel.getText().equals("N/A")
				|| outdoorHumidityLabel.getText().equals("N/A") || airPressureLabel.getText().equals("N/A"))
			data = databaseCommunication.getAllData();
		else {
			Float[] weatherData = { Float.parseFloat(outdoorTemperatureLabel.getText()),
					Float.parseFloat(outdoorHumidityLabel.getText()), Float.parseFloat(airPressureLabel.getText()) };
			data = getBestClothes(weatherData);
		}
		if (data.length > 1) {
			ClothesSelectionTable[] tableData = new ClothesSelectionTable[data.length];
			for (int x = 0; x < data.length; x++) {
				tableData[x] = new ClothesSelectionTable();
				tableData[x].setTainingID(data[x].training.getTainingID());
				tableData[x].setStartTime(data[x].training.getStartTime());
				tableData[x].setTemperature(data[x].weather.getTemperature());
				tableData[x].setHumidity(data[x].weather.getHumidity());
				tableData[x].setAirPressure(data[x].weather.getAirPressure());
				tableData[x].setComfortRate(data[x].clothes.getComfortRate());
				tableData[x].setComment(data[x].clothes.getComment());
			}

			dataList.addAll(tableData);

			IDColumn.setCellValueFactory(new PropertyValueFactory<ClothesSelectionTable, Integer>("tainingID"));
			dateColumn.setCellValueFactory(new PropertyValueFactory<ClothesSelectionTable, Timestamp>("startTime"));
			tempColumn.setCellValueFactory(new PropertyValueFactory<ClothesSelectionTable, Float>("temperature"));
			humColumn.setCellValueFactory(new PropertyValueFactory<ClothesSelectionTable, Float>("humidity"));
			pressColumn.setCellValueFactory(new PropertyValueFactory<ClothesSelectionTable, Float>("airPressure"));
			rateColumn.setCellValueFactory(new PropertyValueFactory<ClothesSelectionTable, Integer>("comfortRate"));
			commentColumn.setCellValueFactory(new PropertyValueFactory<ClothesSelectionTable, String>("comment"));

			table.setItems(dataList);
		}

	}

	@FXML
	void setSelectedTraining(MouseEvent event) {
		if (data.length > 1) {
			int x, ID = table.getSelectionModel().getSelectedItem().getTainingID();
			for (x = 0; x < data.length; x++)
				if (data[x].training.getTainingID() == ID)
					break;
			setTextInLabel(onHeadLabel, data[x].clothes.getHeadClothes());
			setTextInLabel(onThoraxLabel, data[x].clothes.getThoraxClothes());
			setTextInLabel(onHandsLabel, data[x].clothes.getHandsClothes());
			setTextInLabel(onLegsLabel, data[x].clothes.getLegsClothes());
			setTextInLabel(onFeetLabel, data[x].clothes.getFeetClothes());
			setTextInLabel(selectedIDLabel, String.valueOf(ID));
			setTextInLabel(selectedRateLabel, String.valueOf(data[x].clothes.getComfortRate()) + "/10");
		}

	}

	@FXML
	void initialize() {
		 getBestMatchButton.setDisable(true);
		if (weatherStation.isMainStationConnected() && weatherStation.isRemoteStationConnected())
			getActualOutdoorDataButton.setDisable(false);
		else
			getActualOutdoorDataButton.setDisable(true);
	}

}