package weathersystemforcyclists.gui.mainview.showall;

import java.sql.Timestamp;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import weathersystemforcyclists.database.DatabaseCommunication;
import weathersystemforcyclists.database.table.Clothes;
import weathersystemforcyclists.database.table.Training;
import weathersystemforcyclists.database.table.Weather;

public class ShowAllController {
	private DatabaseCommunication databaseCommunication = new DatabaseCommunication();
	private ObservableList<Training> trainingList = FXCollections.observableArrayList();
	private ObservableList<Weather> weatherList = FXCollections.observableArrayList();
	private ObservableList<Clothes> clothesList = FXCollections.observableArrayList();

	@FXML
	private Pane showAllPane;

	@FXML
	private TableView<Training> trainingTable;

	@FXML
	private TableColumn<Training, Integer> trainingID;

	@FXML
	private TableColumn<Training, Integer> trainingWeatherID;

	@FXML
	private TableColumn<Training, Integer> trainingClothesID;

	@FXML
	private TableColumn<Training, Timestamp> trainingStartTime;

	@FXML
	private TableColumn<Training, Timestamp> trainingEndTime;

	@FXML
	private TableView<Weather> weatherTable;

	@FXML
	private TableColumn<Weather, Integer> weatherID;

	@FXML
	private TableColumn<Weather, Timestamp> weatherDate;

	@FXML
	private TableColumn<Weather, Float> weatherTemperature;

	@FXML
	private TableColumn<Weather, Float> weatherHumidity;

	@FXML
	private TableColumn<Weather, Float> weatherPressure;

	@FXML
	private TableView<Clothes> clothesTable;

	@FXML
	private TableColumn<Clothes, Integer> clothesID;

	@FXML
	private TableColumn<Clothes, Integer> clothesComfort;

	@FXML
	private TableColumn<Clothes, String> clothesHead;

	@FXML
	private TableColumn<Clothes, String> clothesThorax;

	@FXML
	private TableColumn<Clothes, String> clothesHands;

	@FXML
	private TableColumn<Clothes, String> clothesLegs;

	@FXML
	private TableColumn<Clothes, String> clothesFeet;

	@FXML
	private TableColumn<Clothes, String> clothesComment;

	@FXML
	private Label trainingIDLabel;

	@FXML
	private Label weatherDLabel;

	@FXML
	private Label clothesIDLabel;

	@FXML
	void setID(MouseEvent event) {
		setTextInLabel(trainingIDLabel,
				String.valueOf("T: " + trainingTable.getSelectionModel().getSelectedItem().getTainingID()));
		setTextInLabel(weatherDLabel,
				String.valueOf("W: " + trainingTable.getSelectionModel().getSelectedItem().getMeasurementID()));
		setTextInLabel(clothesIDLabel,
				String.valueOf("C: " + trainingTable.getSelectionModel().getSelectedItem().getClothesID()));
	}

	@FXML
	void initialize() {
		setTrainingTable();
		setWeatherTable();
		setClothesTable();
	}

	private void setTextInLabel(Label label, String text) {
		Platform.runLater(new Runnable() {
			public void run() {
				label.setText(text);
			}
		});
	}

	private void setTrainingTable() {
		Training[] data = databaseCommunication.getAllTrainingData();

		trainingList.addAll(data);

		trainingID.setCellValueFactory(new PropertyValueFactory<Training, Integer>("tainingID"));
		trainingWeatherID.setCellValueFactory(new PropertyValueFactory<Training, Integer>("measurementID"));
		trainingClothesID.setCellValueFactory(new PropertyValueFactory<Training, Integer>("clothesID"));
		trainingStartTime.setCellValueFactory(new PropertyValueFactory<Training, Timestamp>("startTime"));
		trainingEndTime.setCellValueFactory(new PropertyValueFactory<Training, Timestamp>("endTime"));

		trainingTable.setItems(trainingList);

	}

	private void setWeatherTable() {
		Weather[] data = databaseCommunication.getAllWeatherData();

		weatherList.addAll(data);

		weatherID.setCellValueFactory(new PropertyValueFactory<Weather, Integer>("measurementID"));
		weatherTemperature.setCellValueFactory(new PropertyValueFactory<Weather, Float>("temperature"));
		weatherHumidity.setCellValueFactory(new PropertyValueFactory<Weather, Float>("humidity"));
		weatherPressure.setCellValueFactory(new PropertyValueFactory<Weather, Float>("airPressure"));
		weatherDate.setCellValueFactory(new PropertyValueFactory<Weather, Timestamp>("measurementDate"));

		weatherTable.setItems(weatherList);

	}

	private void setClothesTable() {
		Clothes[] data = databaseCommunication.getAllClothesData();

		clothesList.addAll(data);

		clothesID.setCellValueFactory(new PropertyValueFactory<Clothes, Integer>("clothesID"));
		clothesComfort.setCellValueFactory(new PropertyValueFactory<Clothes, Integer>("comfortRate"));
		clothesHead.setCellValueFactory(new PropertyValueFactory<Clothes, String>("headClothes"));
		clothesThorax.setCellValueFactory(new PropertyValueFactory<Clothes, String>("thoraxClothes"));
		clothesHands.setCellValueFactory(new PropertyValueFactory<Clothes, String>("handsClothes"));
		clothesLegs.setCellValueFactory(new PropertyValueFactory<Clothes, String>("legsClothes"));
		clothesFeet.setCellValueFactory(new PropertyValueFactory<Clothes, String>("feetClothes"));
		clothesComment.setCellValueFactory(new PropertyValueFactory<Clothes, String>("comment"));

		clothesTable.setItems(clothesList);

	}
}
