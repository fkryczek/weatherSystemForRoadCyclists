package weathersystemforcyclists.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import weathersystemforcyclists.database.table.Clothes;
import weathersystemforcyclists.database.table.Data;
import weathersystemforcyclists.database.table.Training;
import weathersystemforcyclists.database.table.Weather;

public class DatabaseCommunication {

	private String databaseName = "weathersystemdatabase";
	private String databaseUser = "root";
	private String databasePassword = "pdFK2020.";

	private String fullDatabaseName = "jdbc:mysql://localhost:3306/" + databaseName
			+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CET";

	public void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(fullDatabaseName, databaseUser, databasePassword);

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM weathersystemdatabase.weather;");
			while (rs.next())
				System.out.println(rs.getInt(1) + "  " + rs.getFloat(2) + "  " + rs.getFloat(3));
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public boolean setWeatherMeasurement(boolean exist, int measurementID, float temperature, float humidity,
			float airPressure, LocalDateTime measurementDate) {
		boolean execute = false;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(fullDatabaseName, databaseUser, databasePassword);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
			Statement statement = con.createStatement();
			if (exist) {
				execute = statement.execute("UPDATE `weathersystemdatabase`.`weather`" + "SET" + "`temperature` = '"
						+ temperature + "'," + "`humidity` = '" + humidity + "'," + "`airPressure` = '" + airPressure
						+ "'," + "`measurementData` = '" + dtf.format(measurementDate) + "'" + "WHERE measurementID = "
						+ measurementID + ";");
			} else {
				execute = statement.execute(
						"INSERT INTO `weathersystemdatabase`.`weather` (`temperature`, `humidity`, `airPressure`, `measurementData`)"
								+ "VALUES" + "('" + temperature + "','" + humidity + "','" + airPressure + "','"
								+ dtf.format(measurementDate) + "');");
			}
			return execute;
		} catch (Exception e) {
			System.out.println(e);
		}
		return execute;
	}

	public void deleteTraining(int ID) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(fullDatabaseName, databaseUser, databasePassword);

			Statement stmt = con.createStatement();
			stmt.executeUpdate(
					"DELETE FROM weathersystemdatabase.training WHERE trainingID = " + ID + ";");
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public Data[] getBestMatch(float f,float g, float h, float i, float j, float k) {
		try {
			Data[] data = new Data[size(f,g,h, i, j, k)];
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(fullDatabaseName, databaseUser, databasePassword);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * " + "FROM weathersystemdatabase.training "
					+ "INNER JOIN weathersystemdatabase.weather USING (measurementID)"
					+ "INNER JOIN weathersystemdatabase.clothes USING (clothesID)"
					+ "WHERE weather.temperature >= " + f +" AND weather.temperature <=" + g
					+ " AND weather.humidity >= "+ h +" AND weather.humidity <="+ i
					+ " AND weather.airPressure >="+ j +" AND weather.airPressure <="+ k+";");
			int x = 0;
			while (rs.next()) {
				data[x] = new Data();
				data[x].training.setClothesID(rs.getInt(1));
				data[x].training.setMeasurementID(rs.getInt(2));
				data[x].training.setTainingID(rs.getInt(3));
				data[x].training.setStartTime(rs.getTimestamp(4));
				data[x].training.setEndTime(rs.getTimestamp(5));

				data[x].weather.setTemperature(rs.getFloat(6));
				data[x].weather.setHumidity(rs.getFloat(7));
				data[x].weather.setAirPressure(rs.getFloat(8));
				data[x].weather.setMeasurementDate(rs.getTimestamp(9));

				data[x].clothes.setComfortRate(rs.getInt(10));
				data[x].clothes.setHeadClothes(rs.getString(11));
				data[x].clothes.setThoraxClothes(rs.getString(12));
				data[x].clothes.setHandsClothes(rs.getString(13));
				data[x].clothes.setLegsClothes(rs.getString(14));
				data[x].clothes.setFeetClothes(rs.getString(15));
				data[x].clothes.setComment(rs.getString(16));

				x++;
			}
			rs.close();
			con.close();
			return data;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;

	}
	
	public int checkRecord (String tableName, int ID, String columnName) {
		int size = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(fullDatabaseName, databaseUser, databasePassword);

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM weathersystemdatabase." + tableName + " "
					+ "WHERE " + columnName + " = " + ID + " ;");
			rs.next();
			size = rs.getInt(1);
			rs.close();
			con.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return size;
	}

	public int size(String tableName) {
		int size = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(fullDatabaseName, databaseUser, databasePassword);

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM weathersystemdatabase." + tableName + ";");
			rs.next();
			size = rs.getInt(1);
			rs.close();
			con.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return size;
	}
	private int size(float f,float g, float h, float i, float j, float k) {
		int size = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(fullDatabaseName, databaseUser, databasePassword);

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM weathersystemdatabase.training "
					+ "INNER JOIN weathersystemdatabase.weather USING (measurementID)"
					+ "INNER JOIN weathersystemdatabase.clothes USING (clothesID)"
					+ "WHERE weather.temperature >= " + f +" AND weather.temperature <=" + g
					+ " AND weather.humidity >= "+ h +" AND weather.humidity <="+ i
					+ " AND weather.airPressure >="+ j +" AND weather.airPressure <="+ k+";");
			rs.next();
			size = rs.getInt(1);
			rs.close();
			con.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return size;
	}

	public Data[] getAllData() {
		try {
			Data[] data = new Data[size("training")];
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(fullDatabaseName, databaseUser, databasePassword);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * " + "FROM weathersystemdatabase.training "
					+ "INNER JOIN weathersystemdatabase.weather USING (measurementID) "
					+ "INNER JOIN weathersystemdatabase.clothes USING (clothesID);");
			int x = 0;
			while (rs.next()) {
				data[x] = new Data();
				data[x].training.setClothesID(rs.getInt(1));
				data[x].training.setMeasurementID(rs.getInt(2));
				data[x].training.setTainingID(rs.getInt(3));
				data[x].training.setStartTime(rs.getTimestamp(4));
				data[x].training.setEndTime(rs.getTimestamp(5));

				data[x].weather.setTemperature(rs.getFloat(6));
				data[x].weather.setHumidity(rs.getFloat(7));
				data[x].weather.setAirPressure(rs.getFloat(8));
				data[x].weather.setMeasurementDate(rs.getTimestamp(9));

				data[x].clothes.setComfortRate(rs.getInt(10));
				data[x].clothes.setHeadClothes(rs.getString(11));
				data[x].clothes.setThoraxClothes(rs.getString(12));
				data[x].clothes.setHandsClothes(rs.getString(13));
				data[x].clothes.setLegsClothes(rs.getString(14));
				data[x].clothes.setFeetClothes(rs.getString(15));
				data[x].clothes.setComment(rs.getString(16));

				x++;
			}
			rs.close();
			con.close();
			return data;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;

	}

	public Training[] getAllTrainingData() {
		try {
			Training[] data = new Training[size("training")];
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(fullDatabaseName, databaseUser, databasePassword);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * " + "FROM weathersystemdatabase.training;");
			int x = 0;
			while (rs.next()) {
				data[x] = new Training();
				data[x].setClothesID(rs.getInt(1));
				data[x].setMeasurementID(rs.getInt(2));
				data[x].setTainingID(rs.getInt(3));
				data[x].setStartTime(rs.getTimestamp(4));
				data[x].setEndTime(rs.getTimestamp(5));

				x++;
			}
			rs.close();
			con.close();
			return data;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	public Training getTrainingData(int ID) {
		try {
			Training data = new Training();
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(fullDatabaseName, databaseUser, databasePassword);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * " + "FROM weathersystemdatabase.training "
					+ "WHERE trainingID = "+ ID + " ;");
			rs.next();
			data.setClothesID(rs.getInt(1));
			data.setMeasurementID(rs.getInt(2));
			data.setTainingID(rs.getInt(3));
			data.setStartTime(rs.getTimestamp(4));
			data.setEndTime(rs.getTimestamp(5));
			
			rs.close();
			con.close();
			return data;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public Weather[] getAllWeatherData() {
		try {
			Weather[] data = new Weather[size("weather")];
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(fullDatabaseName, databaseUser, databasePassword);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * " + "FROM weathersystemdatabase.weather;");
			int x = 0;
			while (rs.next()) {
				data[x] = new Weather();

				data[x].setMeasurementID(rs.getInt(1));
				data[x].setTemperature(rs.getFloat(2));
				data[x].setHumidity(rs.getFloat(3));
				data[x].setAirPressure(rs.getFloat(4));
				data[x].setMeasurementDate(rs.getTimestamp(5));

				x++;
			}
			rs.close();
			con.close();
			return data;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public Clothes[] getAllClothesData() {
		try {
			Clothes[] data = new Clothes[size("weather")];
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(fullDatabaseName, databaseUser, databasePassword);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * " + "FROM weathersystemdatabase.clothes;");
			int x = 0;
			while (rs.next()) {
				data[x] = new Clothes();

				data[x].setClothesID(rs.getInt(1));
				data[x].setComfortRate(rs.getInt(2));
				data[x].setHeadClothes(rs.getString(3));
				data[x].setThoraxClothes(rs.getString(4));
				data[x].setHandsClothes(rs.getString(5));
				data[x].setLegsClothes(rs.getString(6));
				data[x].setFeetClothes(rs.getString(7));
				data[x].setComment(rs.getString(8));

				x++;
			}
			rs.close();
			con.close();
			return data;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

}
