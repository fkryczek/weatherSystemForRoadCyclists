package weathersystemforcyclists.gui.mainview.edittraining;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import weathersystemforcyclists.database.DatabaseCommunication;
import weathersystemforcyclists.database.table.Clothes;
import weathersystemforcyclists.database.table.Training;
import weathersystemforcyclists.database.table.Weather;

public class EditTrainingController {
	private ObservableList<Weather> weatherList = FXCollections.observableArrayList();
	private DatabaseCommunication databaseCommunication = new DatabaseCommunication();
	private Training training = new Training();
	private Clothes clothes;
	private Integer tryParse(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	@FXML
    private DatePicker trainingDate;

    @FXML
    private TextField trainingStartTime;

    @FXML
    private TextField trainingEndTime;

    @FXML
    private TableView<Weather> weatherTable;

    @FXML
    private TableColumn<Weather, Integer> id;

    @FXML
    private TableColumn<Weather, Timestamp> date;

    @FXML
    private TableColumn<Weather, Float> temperature;

    @FXML
    private TableColumn<Weather, Float> humidity;

    @FXML
    private TableColumn<Weather, Float> pressure;

    @FXML
    private TextField headTextField;

    @FXML
    private TextField handsTextField;

    @FXML
    private TextField legsTextField;

    @FXML
    private TextField feetTextField;

    @FXML
    private TextField commentTextField;

    @FXML
    private JFXSlider rateSlider;

    @FXML
    private TextField thoraxTextField;

    @FXML
    private JFXButton editTrainingButton;

    @FXML
    private TextField trainingIDTextField;

    @FXML
    private JFXButton loadTraining;

    @FXML
    void editTraining(MouseEvent event) {
    	training.setStartTime(Timestamp.valueOf(trainingDate.getValue() + " " + trainingStartTime.getText()));
    	training.setEndTime(Timestamp.valueOf(trainingDate.getValue() + " " + trainingEndTime.getText()));
    	databaseCommunication.setClothesData(true, training.getClothesID(), rateSlider.getValue(), headTextField.getText(), thoraxTextField.getText(),handsTextField.getText(), legsTextField.getText(), feetTextField.getText(), commentTextField.getText());
    	databaseCommunication.setTraining(training);
    	editTrainingButton.setDisable(true);
    	editTrainingButton.setTextFill((Color.GREEN));
    }

    @FXML
    void loadTraining(MouseEvent event) {
    	editTrainingButton.setTextFill((Color.BLACK));
    	int trainingIDNumber = tryParse(trainingIDTextField.getText());
		if (trainingIDNumber == -1) {
			trainingIDTextField.setStyle("-fx-text-inner-color: red");
		} else {
			trainingIDTextField.setStyle("-fx-text-inner-color: black");
			
				training = databaseCommunication.getTrainingData(trainingIDNumber);
				clothes = databaseCommunication.getClothesData(training.getClothesID());
				
		    	Date startTime = new Date();
		    	Date endTime= new Date();
		    	startTime.setTime(training.getStartTime().getTime());
		    	endTime.setTime(training.getEndTime().getTime());
		    	
		    	String timeFormatStartTime = new SimpleDateFormat("HH:mm:ss").format(startTime);
		    	String timeFormatEndTime = new SimpleDateFormat("HH:mm:ss").format(endTime);
		    	
		    	trainingStartTime.setText(timeFormatStartTime);
		    	trainingEndTime.setText(timeFormatEndTime);
		    	trainingDate.setValue(training.getEndTime().toLocalDateTime().toLocalDate());
		    	
		    	

				weatherList.addAll(databaseCommunication.getWeatherData(training.getMeasurementID()));

				id.setCellValueFactory(new PropertyValueFactory<Weather, Integer>("measurementID"));
				humidity.setCellValueFactory(new PropertyValueFactory<Weather, Float>("temperature"));
				temperature.setCellValueFactory(new PropertyValueFactory<Weather, Float>("humidity"));
				pressure.setCellValueFactory(new PropertyValueFactory<Weather, Float>("airPressure"));
				date.setCellValueFactory(new PropertyValueFactory<Weather, Timestamp>("measurementDate"));

				weatherTable.setItems(weatherList);
				
				
				headTextField.setText(clothes.getHeadClothes());
				thoraxTextField.setText(clothes.getThoraxClothes());
				handsTextField.setText(clothes.getHandsClothes());
				legsTextField.setText(clothes.getLegsClothes());
				feetTextField.setText(clothes.getFeetClothes());
				commentTextField.setText(clothes.getComment());
				rateSlider.setValue(clothes.getComfortRate());
				editTrainingButton.setDisable(false);
				}
    }
}
