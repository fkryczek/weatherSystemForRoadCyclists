package weathersystemforcyclists.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("mainview/MainView.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("windowIcon.png")));
			primaryStage.setResizable(false);
			primaryStage.setTitle("Weather system for road cyclists");
			primaryStage.show();
		} catch (Exception e) {}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
