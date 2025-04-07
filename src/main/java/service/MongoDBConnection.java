package service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
	private static MongoClient mongoClient;
	private static MongoDatabase database;

	public static MongoDatabase getDatabase() {
		if (mongoClient == null) {
			mongoClient = MongoClients.create("mongodb://localhost:27017");
			database = mongoClient.getDatabase("employee_db"); // Tên database
		}
		return database;
	}

	public static void closeConnection() {
		if (mongoClient != null) {
			mongoClient.close();
		}
	}
}