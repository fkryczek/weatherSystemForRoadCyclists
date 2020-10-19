package weathersystemforcyclists.serial;

import java.io.IOException;

public class WeatherStation {
	private SerialPortCommunication mainStation = new SerialPortCommunication();
	private boolean mainStationConnected = false, remoteStationConnected = false;

	public boolean isMainStationConnected() {
		return mainStationConnected;
	}

	public boolean isRemoteStationConnected() {
		return remoteStationConnected;
	}

	public boolean connectMainStation(String serialPortName) throws InterruptedException, IOException {
		if (mainStation.setSerialPort(serialPortName)) {
			mainStationConnected = true;
			Thread.sleep(6000);
			if (connectRemoteStation())
				return true;
			else
				return false;
		} else
			return false;
	}

	public boolean disconnectMainStation() throws IOException, InterruptedException {
		if (isRemoteStationConnected()) {
			for (int x = 0; x < 3; x++) {
				if (disconnectRemoteStation())
					break;
			}
		}
		if (mainStation.closeSerialPort()) {
			mainStationConnected = false;
			return true;
		} else
			return false;
	}

	public boolean connectRemoteStation() throws IOException, InterruptedException {
		mainStation.readFromSerial(10000);
		mainStation.clearAllRecivedData();
		for (int r = 0; r < 3; r++) {
			String[] recivedData = mainStation.sendCommand('C');
			for (int x = 0; x < recivedData.length; x++) {
				recivedData[x] = recivedData[x].substring(0, recivedData[x].length() - 1);
				if (recivedData[x].equals("7918.00")) {
					remoteStationConnected = true;
					return true;
				}
			}
		}
		return false;
	}

	private boolean disconnectRemoteStation() throws IOException, InterruptedException {
		String[] recivedData = mainStation.sendCommand('D');
		for (int x = 0; x < recivedData.length; x++) {
			recivedData[x] = recivedData[x].substring(0, recivedData[x].length() - 1);
			if (recivedData[x].equals("797070.00")) {
				remoteStationConnected = false;
				return true;
			}
		}
		return false;
	}

	private String getData(char command, String confirmationCode) throws IOException, InterruptedException {
		mainStation.readFromSerial(1000);
		mainStation.clearAllRecivedData();

		String[] recivedData = mainStation.sendCommand(command);
		System.out.println("komenda: " + command);
		for (int x = 0; x < recivedData.length; x++) {
			if (recivedData[x].length() > 2)
				;
			recivedData[x] = recivedData[x].substring(0, recivedData[x].length() - 1);
			System.out.println("[" + x + "] " + recivedData[x] + "/" + recivedData[x].equals(confirmationCode));
			if (recivedData[x].equals(confirmationCode))
				if (x + 1 < recivedData.length)
					return recivedData[x + 1];
		}
		return "N/A";
	}

	public String[] syncAllData() throws IOException, InterruptedException {
		mainStation.readFromSerial(1000);
		mainStation.clearAllRecivedData();

		String[] data = new String[8];

		data[0] = getData('I', "847378.00");
		data[1] = getData('O', "846977.00");
		data[2] = getData('H', "728577.00");
		data[4] = getData('P', "808269.00");
		data[5] = getData('A', "657684.00");
		data[3] = String.format("%.2f",
				(Float.parseFloat(data[4]) / Math.pow(1.0 - (Float.parseFloat(data[5]) / 44330.0), 5.255)));

		String[] recivedData = mainStation.sendCommand('B');
		for (int x = 0; x < recivedData.length; x++) {
			recivedData[x] = recivedData[x].substring(0, recivedData[x].length() - 1);
			if (recivedData[x].equals("666584.00")) {
				data[6] = recivedData[x + 1];
				data[7] = recivedData[x + 2];
				break;
			}
		}
		return data;
	}

}
