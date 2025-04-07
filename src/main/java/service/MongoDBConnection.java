package service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class MongoDBConnection {
	private static MongoClient mongoClient;
	private static MongoDatabase database;
	private static Properties properties;

	static {
		// Load properties khi class được load
		loadProperties();
	}

	private static void loadProperties() {
		properties = new Properties();
		try (InputStream input = MongoDBConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				System.err.println("Không tìm thấy file config.properties");
				return;
			}
			properties.load(input);
		} catch (IOException ex) {
			System.err.println("Lỗi khi đọc file properties: " + ex.getMessage());
		}
	}

	public static MongoDatabase getDatabase() {
		if (mongoClient == null) {
			String connectionString = properties.getProperty("mongodb.connection.string");

			// Kiểm tra nếu không tìm thấy connection string trong file properties
			if (connectionString == null || connectionString.isEmpty()) {
				System.err.println("Lỗi: Không tìm thấy MongoDB connection string trong file properties");
				return null;
			}

			String databaseName = properties.getProperty("mongodb.database.name", "employee_db");

			mongoClient = MongoClients.create(connectionString);
			database = mongoClient.getDatabase(databaseName);
		}
		return database;
	}

	public static void closeConnection() {
		if (mongoClient != null) {
			mongoClient.close();
		}
	}

	/**
	 * Kiểm tra kết nối đến MongoDB
	 * 
	 * @return true nếu kết nối thành công, false nếu thất bại
	 */
	public static boolean testConnection() {
		try {
			MongoDatabase db = getDatabase();
			// Thực hiện lệnh ping đơn giản để kiểm tra kết nối
			db.runCommand(new Document("ping", 1));
			System.out.println("Kết nối MongoDB thành công!");
			return true;
		} catch (Exception e) {
			System.err.println("Lỗi kết nối MongoDB: " + e.getMessage());
			return false;
		}
	}
}
