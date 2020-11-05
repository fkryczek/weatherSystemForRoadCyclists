package weathersystemforcyclists.gui.mainview;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import weathersystemforcyclists.gui.mainview.clothesselection.ClothesSelectionController;
import weathersystemforcyclists.log.LOG;
import weathersystemforcyclists.serial.SerialPortCommunication;
import weathersystemforcyclists.serial.WeatherStation;

public class MainViewController {

	Pane[] paneList = new Pane[7];

	@FXML
	private Pane pane;
	@FXML
	private AnchorPane root;
	@FXML
	private Rectangle stationCommunicationBackground;
	@FXML
	private JFXButton stationCommunicationButton;
	@FXML
	private JFXButton clothesSelectButton;
	@FXML
	private JFXButton showAllTrainingButton;
	@FXML
	private JFXButton addTrainingButton;
	@FXML
	private JFXButton editTrainingButton;
	@FXML
	private JFXButton deleteTrainingButton;
	@FXML
	private JFXButton helpButton;

	private void setDisableAllMenuButton(boolean disable) {
		stationCommunicationButton.setDisable(disable);
		clothesSelectButton.setDisable(disable);
		showAllTrainingButton.setDisable(disable);
		addTrainingButton.setDisable(disable);
		editTrainingButton.setDisable(disable);
		deleteTrainingButton.setDisable(disable);
		helpButton.setDisable(disable);
	}

	@FXML
	void addTraining(MouseEvent event) {

	}

	@FXML
	void deleteTraining(MouseEvent event) {

	}

	@FXML
	void editTraining(MouseEvent event) {

	}

	@FXML
	void help(MouseEvent event) {

	}

	@FXML
	void selectClothes(MouseEvent event) {

		closeAll.setVisible(true);
		setDisableAllMenuButton(true);
		clothesSelectButton.setVisible(true);
		pane.setVisible(true);

		try {
			ClothesSelectionController.setMainStationObject(weatherStation);
			paneList[0] = FXMLLoader.load(getClass().getResource("clothesselection/ClothesSelection.fxml"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pane.getChildren().setAll(paneList[0]);

	}

	@FXML
	void showAllTraining(MouseEvent event) {
		closeAll.setVisible(true);
		setDisableAllMenuButton(true);
		clothesSelectButton.setVisible(true);
		pane.setVisible(true);

		try {
			paneList[1] = FXMLLoader.load(getClass().getResource("showall/showAll.fxml"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pane.getChildren().setAll(paneList[1]);
	}

//--------------------------------------------------
//					MAIN WINDOW
//--------------------------------------------------	       
	@FXML
	private Label mainStationConnectedStatus;
	@FXML
	private Label remoteStationConnectedStatus;
	@FXML
	private Label temperatureIn;
	@FXML
	private Label temperatureOut;
	@FXML
	private Label relativePressure;
	@FXML
	private Label absolutePressure;
	@FXML
	private Label altitude;
	@FXML
	private Label batteryIn;
	@FXML
	private Label batteryInP;
	@FXML
	private Label batteryOut;
	@FXML
	private Label batteryOutP;
	@FXML
	private Label syncTime;
	@FXML
	private Label humidityOut;
//--------------------------------------------------
//				STATIONS COMMUNICATION 
//--------------------------------------------------
	@FXML
	private Pane connectionPane;
	@FXML
	private Text closeAll;
	@FXML
	private JFXListView<String> serialPortListView;
	@FXML
	private Label mainStationConnectionStatusLabel;
	@FXML
	private TextField serialPortNameTextField;
	@FXML
	private JFXButton disconnectMainStationButton;
	@FXML
	private JFXButton connectMainStationButton;
	@FXML
	private Label remoteStationConnectionStatusLabel;
	@FXML
	private ProgressBar connectingProgressBar;
	@FXML
	private Label connectionProgressInfoLabel;

	private SerialPortCommunication mainStation = new SerialPortCommunication();
	private WeatherStation weatherStation = new WeatherStation();
	private boolean serialBusy = false, isSyncTime = false;
	private LOG log = new LOG("MVC");

	@FXML
	void showCommunicationPane(MouseEvent event) {
		setDisableAllMenuButton(true);
		closeAll.setVisible(true);
		connectionPane.setVisible(true);

		int numberOfAllPorts = mainStation.getNumberOfAllPorts();
		String[] portsDescription = mainStation.showAllPorts();

		for (int x = 0; x < numberOfAllPorts; x++)
			serialPortListView.getItems().add(portsDescription[x]);

		if (weatherStation.isMainStationConnected()) {
			mainStationConnectionStatusLabel.setTextFill(Color.GREEN);
			mainStationConnectionStatusLabel.setText("CONNECTED");
			connectMainStationButton.setDisable(true);
			disconnectMainStationButton.setDisable(false);

		} else {
			mainStationConnectionStatusLabel.setTextFill(Color.RED);
			mainStationConnectionStatusLabel.setText("DISCONNECTED");
			connectMainStationButton.setDisable(false);
			disconnectMainStationButton.setDisable(true);
		}
		if (weatherStation.isRemoteStationConnected()) {
			remoteStationConnectionStatusLabel.setTextFill(Color.GREEN);
			remoteStationConnectionStatusLabel.setText("CONNECTED");
		} else {
			remoteStationConnectionStatusLabel.setTextFill(Color.RED);
			remoteStationConnectionStatusLabel.setText("DISCONNECTED");
		}
	}

	@FXML
	void closeActualView(MouseEvent event) {
		connectionPane.setVisible(false);
		pane.setVisible(false);
		serialPortListView.getItems().clear();
		setDisableAllMenuButton(false);
		closeAll.setVisible(false);
	}

	@FXML
	void getSerialPortNameCell(MouseEvent event) {
		String serialPortName = serialPortListView.getSelectionModel().getSelectedItem();
		int startWordIndex = serialPortName.indexOf("COM");
		if (startWordIndex > 0) {
			serialPortName = serialPortName.substring(startWordIndex);
			serialPortName = serialPortName.substring(0, serialPortName.indexOf(")"));
			serialPortNameTextField.setText(serialPortName);
		} else
			serialPortNameTextField.setText("not allowed");

	}

//==================================================
	@FXML
	void connectMainStation(MouseEvent event) {
		log.addAction("sasdaasdsads");
		log.addAction("2sasdaasdsads");
		log.addAction("3sasdaasdsads");
		log.addAction("4sasdaasdsads");
		log.addAction("sas5daasdsads");
		log.addAction("4sasdaasdsads");
		log.addAction("sas5daasdsads");
		new Thread(() -> { // Lambda Expression
			// WALIDACJA WPISYWANYCH RZECZY
			serialBusy = true;
			String serialPortName = serialPortNameTextField.getText();
			connectingProgressBar.setVisible(true);
			connectMainStationButton.setDisable(true);
			closeAll.setVisible(false);
			try {
				setTextInLabel(connectionProgressInfoLabel, "trying to connect to main station");
				if (weatherStation.connectMainStation(serialPortName)) {
					setConnectionText(mainStationConnectionStatusLabel, true);
					setConnectionText(mainStationConnectedStatus, true);
					setTextInLabel(connectionProgressInfoLabel, "trying to connect to remote station");
					if (weatherStation.isRemoteStationConnected()) {
						setConnectionText(remoteStationConnectionStatusLabel, true);
						setConnectionText(remoteStationConnectedStatus, true);
						setTextInLabel(connectionProgressInfoLabel, "trying to sync all data");
						isSyncTime = true;
						syncAllData(event);
						isSyncTime = false;
					}
					connectMainStationButton.setDisable(true);
					disconnectMainStationButton.setDisable(false);
				}

			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setTextInLabel(connectionProgressInfoLabel, "");
			connectingProgressBar.setVisible(false);
			closeAll.setVisible(true);
			serialBusy = false;
		}).start();

	}

	@FXML
	void disconnectMainStation(MouseEvent event) {
		if (weatherStation.getBusyStatus() == false) {
			new Thread(() -> { // Lambda Expression
				serialBusy = true;
				connectingProgressBar.setVisible(true);
				closeAll.setVisible(false);
				try {
					setTextInLabel(connectionProgressInfoLabel, "trying to disconnect stations");
					if (weatherStation.disconnectMainStation()) {
						setConnectionText(mainStationConnectionStatusLabel, false);
						setConnectionText(mainStationConnectedStatus, false);
						setConnectionText(remoteStationConnectionStatusLabel, false);
						setConnectionText(remoteStationConnectedStatus, false);
						connectMainStationButton.setDisable(false);
						disconnectMainStationButton.setDisable(true);
					}
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				connectingProgressBar.setVisible(false);
				closeAll.setVisible(true);
				serialBusy = false;
				setTextInLabel(connectionProgressInfoLabel, "");
			}).start();
		}

	}

	private void setConnectionText(Label label, boolean connected) {
		Platform.runLater(new Runnable() {
			public void run() {
				if (connected) {
					label.setTextFill(Color.GREEN);
					label.setText("CONNECTED");
				} else {
					label.setTextFill(Color.RED);
					label.setText("DISCONNECTED");
				}
			}
		});

	}

	private void setTextInLabel(Label label, String text) {
		Platform.runLater(new Runnable() {
			public void run() {
				label.setText(text);
			}
		});
	}

	@FXML
	void syncAllData(MouseEvent event) {
		if ((!serialBusy || isSyncTime) && weatherStation.isMainStationConnected()
				&& weatherStation.isRemoteStationConnected() && !weatherStation.getBusyStatus()) {
			try {
				connectMainStationButton.setDisable(true);
				disconnectMainStationButton.setDisable(true);
				String[] data = weatherStation.syncAllData();
				setTextInLabel(temperatureIn, data[0]);
				setTextInLabel(temperatureOut, data[1]);
				setTextInLabel(humidityOut, data[2]);
				setTextInLabel(relativePressure, data[3]);
				setTextInLabel(absolutePressure, data[4]);
				setTextInLabel(altitude, data[5]);
				setTextInLabel(batteryIn, data[6]);
				setTextInLabel(batteryOut, data[7]);

				// if(Float.parseFloat(data[6])<3.0) //sprawdzic minimalne bezpieczne napiecei
				// na 18650
				setTextInLabel(batteryInP,
						String.format("%.0f", ((Float.parseFloat(data[6]) - 2.5) / 1.7) * 100) + "%");
				// if(Float.parseFloat(data[7])<7.0)
				setTextInLabel(batteryOutP,
						String.format("%.0f", ((Float.parseFloat(data[7]) - 2.5) / 1.7) * 100) + "%");

				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				setTextInLabel(syncTime, dtf.format(now));
				disconnectMainStationButton.setDisable(false);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@FXML
	void initialize() {

	}
}