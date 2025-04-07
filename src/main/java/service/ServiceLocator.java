package service;

/**
 * Service Locator pattern to manage all service instances
 * Ensures services are only initialized after database connection is confirmed
 */
public class ServiceLocator {
    private static AdminService adminService;
    private static EmployeeManager employeeManager;
    private static boolean databaseConnected = false;

    /**
     * Initialize all services after database connection is confirmed
     * 
     * @return true if initialization is successful
     */
    public static boolean initializeServices() {
        if (!databaseConnected) {
            databaseConnected = MongoDBConnection.testConnection();

            if (databaseConnected) {
                adminService = new AdminService();
                adminService.initializeDefaultAdmin();

                employeeManager = new EmployeeManager();

                System.out.println("All services initialized successfully");
            } else {
                System.err.println("Database connection failed, services not initialized");
            }
        }

        return databaseConnected;
    }

    public static AdminService getAdminService() {
        if (!databaseConnected) {
            initializeServices();
        }
        return adminService;
    }

    public static EmployeeManager getEmployeeManager() {
        if (!databaseConnected) {
            initializeServices();
        }
        return employeeManager;
    }

    public static boolean isDatabaseConnected() {
        return databaseConnected;
    }
}
