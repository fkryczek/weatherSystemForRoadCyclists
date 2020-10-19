package weathersystemforcyclists.serial;

import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;

public class SerialPortCommunication {

	private SerialPort serialPort;
	private String recivedData;

	public int getNumberOfAllPorts() {
		return SerialPort.getCommPorts().length;
	}

	public String[] showAllPorts() {
		SerialPort[] ports = SerialPort.getCommPorts();
		String[] portsDescription = new String[ports.length];
		for (int x = 0; x < ports.length; x++)
			portsDescription[x] = ports[x].getDescriptivePortName();
		return portsDescription;
	}

	public boolean setSerialPort(String portName) {
		serialPort = SerialPort.getCommPort(portName);
		serialPort.setComPortParameters(9600, 8, 1, 0);
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		if (serialPort.openPort())
			return true;
		else
			return false;

	}

	public String[] sendCommand(char command) throws IOException, InterruptedException {
		clearAllRecivedData();
		serialPort.getOutputStream().write(command);
		if (command == 'C')
			for (int x = 0; x < 10; x++)
				if (recivedData.split("\n").length < 4)
					readFromSerial(8000);
				else
					break;
		else
			for (int x = 0; x < 3; x++)
				readFromSerial(1000);
		return recivedData.split("\n");
	}

	public void readFromSerial(int waitingTime) {
		int sleepingTime = 0;
		try {

			while (serialPort.bytesAvailable() == 0) {
				Thread.sleep(10);
				sleepingTime = sleepingTime + 10;
				if (sleepingTime > waitingTime)
					break;
			}
			if (serialPort.bytesAvailable() != 0) {
				byte[] readBuffer = new byte[serialPort.bytesAvailable()];
				serialPort.readBytes(readBuffer, readBuffer.length);
				for (int i = 0; i < readBuffer.length; ++i)
					addChar((char) readBuffer[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addChar(char newChar) {
		recivedData = recivedData + newChar;
	}

	public void clearAllRecivedData() {
		recivedData = "";
	}

	public boolean closeSerialPort() {
		if (serialPort.closePort())
			return true;
		else
			return false;
	}
}
