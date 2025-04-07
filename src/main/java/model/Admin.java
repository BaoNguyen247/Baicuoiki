package model;

import org.bson.Document;
import org.bson.types.ObjectId;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class Admin {
    private ObjectId id;
    private String username;
    private String password; // This will store the hashed password

    // Default constructor needed for MongoDB
    public Admin() {
    }

    public Admin(String username, String password) {
        this.username = username;
        // Encrypt password when creating a new Admin
        this.password = encryptPassword(password);
    }

    // Constructor from MongoDB Document
    public Admin(Document doc) {
        this.id = doc.getObjectId("_id");
        this.username = doc.getString("username");
        this.password = doc.getString("password");
    }

    // Getters and setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Only return the hashed password
    public String getPassword() {
        return password;
    }

    public void setPassword(String rawPassword) {
        this.password = encryptPassword(rawPassword);
    }

    // Password encryption method using SHA-256
    private String encryptPassword(String rawPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }

    // Utility method to convert bytes to hexadecimal string
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Verify if provided password matches the stored hash
    public boolean verifyPassword(String rawPassword) {
        String hashedInput = encryptPassword(rawPassword);
        return this.password.equals(hashedInput);
    }

    // Convert to MongoDB Document
    public Document toDocument() {
        Document doc = new Document();
        if (id != null) {
            doc.append("_id", id);
        }
        doc.append("username", username)
                .append("password", password);
        return doc;
    }
}
