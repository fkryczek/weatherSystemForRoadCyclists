package weathersystemforcyclists.gui.mainview.addtraining;

import java.sql.Timestamp;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import java.time.*;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import weathersystemforcyclists.database.DatabaseCommunication;
import weathersystemforcyclists.database.table.Clothes;
import weathersystemforcyclists.database.table.Training;
import weathersystemforcyclists.database.table.Weather;
import weathersystemforcyclists.log.LOG;

public class AddTrainingController {
	private ObservableList<Weather> weatherList = FXCollections.observableArrayList();
	private DatabaseCommunication databaseCommunication = new DatabaseCommunication();
	private Training training = new Training();
	private LOG log = new LOG("ATC");
	private Integer tryParse(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	private boolean checkTimestrampParse (String text) {
		try {
			Timestamp.valueOf(text);
			return true; 
		} catch(Exception e) { 
			return false;
		}
	}
	
    @FXML
    private Pane clothesSelectionPane;

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
    private TextField trainingIDTextField;

    @FXML
    private JFXButton fillInButton;

    @FXML
    private JFXButton addTrainingButton;

    @FXML
    void addTraining(MouseEvent event) {
    	
    	if(checkTimestrampParse(trainingDate.getValue() + " " + trainingStartTime.getText()) || checkTimestrampParse(trainingDate.getValue() + " " + trainingEndTime.getText())) {
    			if(training.getMeasurementID()>0) {
    				training.setStartTime(Timestamp.valueOf(trainingDate.getValue() + " " + trainingStartTime.getText()));
        	    	training.setEndTime(Timestamp.valueOf(trainingDate.getValue() + " " + trainingEndTime.getText()));
        	    	databaseCommunication.setClothesData(false, 0, rateSlider.getValue(), headTextField.getText(), thoraxTextField.getText(),handsTextField.getText(), legsTextField.getText(), feetTextField.getText(), commentTextField.getText());
        	    	training.setClothesID(databaseCommunication.size("clothes")+1);
        	    	databaseCommunication.setTraining(training);
        	    	trainingStartTime.setStyle("-fx-text-inner-color: black");
        	    	trainingEndTime.setStyle("-fx-text-inner-color: black");
        	    	trainingDate.setStyle("-fx-text-inner-color: black");
        	    	weatherTable.setStyle("-fx-background-color: transparent");
        	    	addTrainingButton.setTextFill((Color.GREEN));
        	    	addTrainingButton.setDisable(true);
    			}   
    			else
    				weatherTable.setStyle("-fx-background-color: red");
    		}	
    	else {
    		trainingEndTime.setStyle("-fx-text-inner-color: red");
    		trainingStartTime.setStyle("-fx-text-inner-color: red");
    		trainingDate.setStyle("-fx-text-inner-color: red");
    		
    	}
  
    }

    @FXML
    void fillIn(MouseEvent event) {
    	
    	int trainingIDNumber = tryParse(trainingIDTextField.getText());
		if (trainingIDNumber == -1) {
			trainingIDTextField.setStyle("-fx-text-inner-color: red");
		} else {
			trainingIDTextField.setStyle("-fx-text-inner-color: black");
			if (databaseCommunication.checkRecord("training", trainingIDNumber, "trainingID") > 0) {

				Training trainingData = databaseCommunication.getTrainingData(trainingIDNumber);
				Clothes clothesData = databaseCommunication.getClothesData(trainingData.getClothesID());
				headTextField.setText(clothesData.getHeadClothes());
				thoraxTextField.setText(clothesData.getThoraxClothes());
				handsTextField.setText(clothesData.getHandsClothes());
				legsTextField.setText(clothesData.getLegsClothes());
				feetTextField.setText(clothesData.getFeetClothes());
				commentTextField.setText(clothesData.getComment());
				rateSlider.setValue(clothesData.getComfortRate());
				}
			}

    }
    
    @FXML
    void setSelectedWeather(MouseEvent event) {
    	if(!weatherList.isEmpty())
    		training.setMeasurementID(weatherTable.getSelectionModel().getSelectedItem().getMeasurementID());
     }
    @FXML
    void getNowData(MouseEvent event) {
    	LocalDate localDate = LocalDate.now();
    	LocalDateTime localTime = LocalDateTime.now();
    	DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
    	trainingStartTime.setText(timeFormat.format(localTime));
    	trainingEndTime.setText(timeFormat.format(localTime));
    	trainingDate.setValue(localDate);
    	
    	weatherTable.getItems().clear();
    	Weather[] data = databaseCommunication.getTodayWeatherData();

		weatherList.addAll(data);

		id.setCellValueFactory(new PropertyValueFactory<Weather, Integer>("measurementID"));
		temperature.setCellValueFactory(new PropertyValueFactory<Weather, Float>("temperature"));
		humidity.setCellValueFactory(new PropertyValueFactory<Weather, Float>("humidity"));
		pressure.setCellValueFactory(new PropertyValueFactory<Weather, Float>("airPressure"));
		date.setCellValueFactory(new PropertyValueFactory<Weather, Timestamp>("measurementDate"));

		weatherTable.setItems(weatherList);
    	
    }
    
    @FXML
	void initialize() {
    	
    	Weather[] data = databaseCommunication.getAllWeatherData();

		 weatherList.addAll(data);

		id.setCellValueFactory(new PropertyValueFactory<Weather, Integer>("measurementID"));
		temperature.setCellValueFactory(new PropertyValueFactory<Weather, Float>("temperature"));
		humidity.setCellValueFactory(new PropertyValueFactory<Weather, Float>("humidity"));
		pressure.setCellValueFactory(new PropertyValueFactory<Weather, Float>("airPressure"));
		date.setCellValueFactory(new PropertyValueFactory<Weather, Timestamp>("measurementDate"));

		weatherTable.setItems(weatherList);
		date.setSortType(TableColumn.SortType.DESCENDING);
		weatherTable.sort();

	}

}
