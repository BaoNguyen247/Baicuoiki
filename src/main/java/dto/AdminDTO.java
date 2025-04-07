package dto;

import model.Admin;
import org.bson.types.ObjectId;

/**
 * Data Transfer Object for Admin entities
 * Used to transfer data between layers without exposing sensitive information
 */
public class AdminDTO {
    private String id;
    private String username;

    // Default constructor
    public AdminDTO() {
    }

    // Constructor with fields
    public AdminDTO(String id, String username) {
        this.id = id;
        this.username = username;
    }

    // Constructor from Admin entity (for service layer)
    public AdminDTO(Admin admin) {
        if (admin.getId() != null) {
            this.id = admin.getId().toHexString();
        }
        this.username = admin.getUsername();
        // Note: password is intentionally not transferred to the DTO
    }

    // Convert DTO to Admin entity (for service layer)
    // Note: This doesn't include password, which should be handled separately
    public Admin toEntity() {
        Admin admin = new Admin();
        if (id != null && !id.isEmpty()) {
            admin.setId(new ObjectId(id));
        }
        admin.setUsername(username);
        return admin;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
