package application;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class mainViewController {

    @FXML
    private AnchorPane root;

    @FXML
    private Rectangle clothesSelectionBackground;

    @FXML
    private Rectangle showAllTrainingBackground;

    @FXML
    private Rectangle addTrainingBackground;

    @FXML
    private Rectangle editTrainingBackground;

    @FXML
    private Rectangle deleteTrainingBackground;

    @FXML
    private Rectangle helpBackground;

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

}
