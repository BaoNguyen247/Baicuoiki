package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import model.Admin;
import dto.AdminDTO;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for Admin entity operations
 * Handles database operations for Admin entities
 */
public class AdminService {
    private final MongoCollection<Document> adminCollection;

    public AdminService() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.adminCollection = database.getCollection("admins");
    }

    /**
     * Initializes a default admin user if no admins exist in the database
     */
    public void initializeDefaultAdmin() {
        // Check if any admin exists
        long count = adminCollection.countDocuments();

        if (count == 0) {
            // No admins exist, create a default one
            System.out.println("No admin users found. Creating default admin user...");
            createAdmin("admin", "123456");
            System.out.println("Default admin created with username: 'admin' and password: '123456'");
        }
    }

    // Create a new admin
    public Admin createAdmin(String username, String password) {
        Admin admin = new Admin(username, password);
        Document doc = admin.toDocument();
        adminCollection.insertOne(doc);
        admin.setId(doc.getObjectId("_id"));
        return admin;
    }

    // Find admin by username
    public Admin findByUsername(String username) {
        Document doc = adminCollection.find(Filters.eq("username", username)).first();
        if (doc == null) {
            return null;
        }
        return new Admin(doc);
    }

    // Find admin by ID
    public Admin findById(String id) {
        Document doc = adminCollection.find(Filters.eq("_id", new ObjectId(id))).first();
        if (doc == null) {
            return null;
        }
        return new Admin(doc);
    }

    // Get all admins as DTOs
    public List<AdminDTO> getAllAdmins() {
        List<AdminDTO> adminDTOs = new ArrayList<>();
        for (Document doc : adminCollection.find()) {
            Admin admin = new Admin(doc);
            adminDTOs.add(new AdminDTO(admin));
        }
        return adminDTOs;
    }

    // Update admin
    public boolean updateAdmin(Admin admin) {
        Document doc = admin.toDocument();
        return adminCollection.replaceOne(
                Filters.eq("_id", admin.getId()),
                doc).getModifiedCount() > 0;
    }

    // Delete admin
    public boolean deleteAdmin(String id) {
        return adminCollection.deleteOne(
                Filters.eq("_id", new ObjectId(id))).getDeletedCount() > 0;
    }

    // Authenticate admin
    public boolean authenticate(String username, String password) {
        Admin admin = findByUsername(username);
        if (admin == null) {
            return false;
        }
        return admin.verifyPassword(password);
    }
}
