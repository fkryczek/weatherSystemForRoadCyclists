package weathersystemforcyclists.gui.mainview.deletetraining;

import java.sql.Timestamp;

import com.jfoenix.controls.JFXButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import weathersystemforcyclists.database.DatabaseCommunication;
import weathersystemforcyclists.database.table.Training;

public class DeleteController {
	private DatabaseCommunication databaseCommunication = new DatabaseCommunication();
	private ObservableList<Training> trainingList = FXCollections.observableArrayList();
	private int trainingIDNumber;

	private Integer tryParse(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	@FXML
	private TextField trainingIDTextField;

	@FXML
	private JFXButton searchButton;

	@FXML
	private JFXButton deleteButton;

	@FXML
	private Label infoLabel;

	@FXML
	private TableView<Training> table;

	@FXML
	private TableColumn<Training, Integer> trainingID;

	@FXML
	private TableColumn<Training, Integer> weatherID;

	@FXML
	private TableColumn<Training, Integer> clothesID;

	@FXML
	private TableColumn<Training, Timestamp> startTime;

	@FXML
	private TableColumn<Training, Timestamp> endTime;

	@FXML
	void deleteTraining(MouseEvent event) {
		databaseCommunication.deleteTraining(trainingIDNumber);
		if (databaseCommunication.checkRecord("training", trainingIDNumber, "trainingID") > 0)
			infoLabel.setText("(deletion failed)");
		else {
			deleteButton.setDisable(true);
			infoLabel.setText("(training deleted)");
			table.getItems().clear();
		}
	}

	@FXML
	void searchTraining(MouseEvent event) {
		trainingIDNumber = tryParse(trainingIDTextField.getText());
		if (trainingIDNumber == -1) {
			infoLabel.setText("(wrong number)");
		} else {
			if (databaseCommunication.checkRecord("training", trainingIDNumber, "trainingID") > 0) {
				deleteButton.setDisable(false);

				Training data = databaseCommunication.getTrainingData(trainingIDNumber);
				trainingList.add(data);
				trainingID.setCellValueFactory(new PropertyValueFactory<Training, Integer>("tainingID"));
				weatherID.setCellValueFactory(new PropertyValueFactory<Training, Integer>("measurementID"));
				clothesID.setCellValueFactory(new PropertyValueFactory<Training, Integer>("clothesID"));
				startTime.setCellValueFactory(new PropertyValueFactory<Training, Timestamp>("startTime"));
				endTime.setCellValueFactory(new PropertyValueFactory<Training, Timestamp>("endTime"));
				table.setItems(trainingList);
			} else
				infoLabel.setText("(training not found)");
		}
	}
}
