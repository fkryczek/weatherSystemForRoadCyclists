package weathersystemforcyclists.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LOG {
	private String actions[] = new String[5];
	private int actionCounter = 0;
	private String catalogName;

	public LOG(String catalogName) {
		this.catalogName = catalogName;
		checkDirectory();
	}

	public void addAction(String action) {
		if (actionCounter == 5) {
			saveActions();
			actionCounter = 0;
		} else {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss, dd.MM.yyyy");
			LocalDateTime now = LocalDateTime.now();
			actions[actionCounter] = "[" + dtf.format(now) + "] " + action;
			actionCounter++;
		}
	}

	private void checkDirectory() {

		try {
			Path path = Paths.get("log/" + catalogName);
			Files.createDirectories(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveActions() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		LocalDateTime now = LocalDateTime.now();
		try {
			FileWriter file = new FileWriter("log/" + catalogName + "/" + dtf.format(now) + ".txt", true);
			BufferedWriter out = new BufferedWriter(file);
			for (int i = 0; i < actionCounter; i++)
				out.write(actions[i] + "\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
