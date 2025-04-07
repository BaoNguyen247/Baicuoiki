package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import exception.ApplicationException;
import exception.DatabaseException;
import exception.DuplicateIdException;
import exception.ValidationException;
import model.Employee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;
import service.EmployeeManager;

/**
 * Controller class that handles employee-related business logic and acts as a
 * mediator between the GUI and service layers.
 */
public class EmployeeController {
    private EmployeeManager employeeManager;

    public EmployeeController() {
        this.employeeManager = new EmployeeManager();
    }

    /**
     * Validates employee ID format
     * 
     * @param id The employee ID to validate
     * @throws ValidationException if the ID is invalid
     */
    public void validateEmployeeId(String id) throws ValidationException {
        if (id.isEmpty()) {
            throw new ValidationException("Mã nhân viên", "Mã nhân viên không được để trống!");
        }
        if (!id.matches("\\d+")) {
            throw new ValidationException("Mã nhân viên", "Mã nhân viên chỉ được chứa số!");
        }
        if (id.length() > 4) {
            throw new ValidationException("Mã nhân viên", "Mã nhân viên không được quá 4 chữ số!");
        }
    }

    /**
     * Checks if an employee ID already exists
     * 
     * @param id The employee ID to check
     * @return true if ID exists, false otherwise
     */
    public boolean isEmployeeIdExists(String id) {
        return employeeManager.findEmployeeById(id) != null;
    }

    /**
     * Validates employee name
     * 
     * @param name The name to validate
     * @throws ValidationException if the name is invalid
     */
    public void validateEmployeeName(String name) throws ValidationException {
        if (name.isEmpty()) {
            throw new ValidationException("Họ tên", "Vui lòng nhập họ tên!");
        }
    }

    /**
     * Validates start date format
     * 
     * @param dateStr The date string to validate
     * @throws ValidationException if the date format is invalid
     */
    public void validateStartDate(String dateStr) throws ValidationException {
        if (!dateStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
            throw new ValidationException("Ngày vào làm", "Ngày vào làm phải có định dạng dd/MM/yyyy!");
        }
    }

    /**
     * Parses a date string into a Date object
     * 
     * @param dateStr The date string to parse
     * @return The parsed Date object
     * @throws ApplicationException if the date format is invalid
     */
    public Date parseDate(String dateStr) throws ApplicationException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new ApplicationException("Lỗi khi phân tích ngày: " + e.getMessage(), e);
        }
    }

    /**
     * Validates numeric input
     * 
     * @param value     The string to validate
     * @param fieldName The name of the field for error messages
     * @throws ValidationException if the input is invalid
     */
    public void validateNumericInput(String value, String fieldName) throws ValidationException {
        if (value.isEmpty()) {
            throw new ValidationException(fieldName, fieldName + " không được để trống!");
        }
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new ValidationException(fieldName, fieldName + " phải là một số hợp lệ!");
        }
    }

    /**
     * Validates integer input
     * 
     * @param value     The string to validate
     * @param fieldName The name of the field for error messages
     * @throws ValidationException if the input is invalid
     */
    public void validateIntegerInput(String value, String fieldName) throws ValidationException {
        if (value.isEmpty()) {
            throw new ValidationException(fieldName, fieldName + " không được để trống!");
        }
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ValidationException(fieldName, fieldName + " phải là một số nguyên hợp lệ!");
        }
    }

    /**
     * Creates a temporary employee object for salary calculation
     * 
     * @param id           Employee ID
     * @param name         Employee name
     * @param startDate    Start date
     * @param position     Position
     * @param employeeType Type (Full-time or Part-time)
     * @return A new Employee object
     */
    public Employee createTempEmployee(String id, String name, Date startDate,
            String position, String employeeType) {
        if (employeeType.equals("Full-time")) {
            return new FullTimeEmployee(id, name, startDate, position, 5000000, 0, 0, 0, 0);
        } else {
            return new PartTimeEmployee(id, name, startDate, position, 10000000, 0, 0, 0);
        }
    }

    /**
     * Adds an employee to the database
     * 
     * @param employee The employee to add
     * @throws DuplicateIdException if the employee ID already exists
     * @throws DatabaseException if a database error occurs
     */
    public void addEmployee(Employee employee) throws DuplicateIdException, DatabaseException {
        try {
            employeeManager.addEmployee(employee);
        } catch (DuplicateIdException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error adding employee: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an employee in the database
     * 
     * @param id       Original ID of the employee
     * @param employee Updated employee object
     * @return true if update successful, false otherwise
     * @throws DuplicateIdException if the new ID already exists
     * @throws DatabaseException if a database error occurs
     */
    public boolean updateEmployee(String id, Employee employee) 
            throws DuplicateIdException, DatabaseException {
        try {
            return employeeManager.updateEmployee(id, employee);
        } catch (DuplicateIdException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error updating employee: " + e.getMessage(), e);
        }
    }

    /**
     * Removes an employee from the database
     * 
     * @param id ID of the employee to remove
     * @throws DatabaseException if a database error occurs
     */
    public void removeEmployee(String id) throws DatabaseException {
        try {
            employeeManager.removeEmployee(id);
        } catch (Exception e) {
            throw new DatabaseException("Error removing employee: " + e.getMessage(), e);
        }
    }

    /**
     * Finds an employee by ID
     * 
     * @param id The ID to search for
     * @return The employee if found, null otherwise
     */
    public Employee findEmployeeById(String id) {
        return employeeManager.findEmployeeById(id);
    }

    /**
     * Gets all employees
     * 
     * @return List of all employees
     */
    public List<Employee> getAllEmployees() {
        return employeeManager.getEmployees();
    }

    /**
     * Gets employees sorted by salary
     * 
     * @param ascending true for ascending order, false for descending
     * @return Sorted list of employees
     */
    public List<Employee> getEmployeesSortedBySalary(boolean ascending) {
        return employeeManager.getEmployeesSortedBySalary(ascending);
    }

    /**
     * Searches for employees by name
     * 
     * @param name The name to search for
     * @return List of matching employees
     */
    public List<Employee> searchEmployeeByName(String name) {
        return employeeManager.searchEmployeeByName(name);
    }

    /**
     * Filters employees by position
     * 
     * @param position The position to filter by
     * @return List of employees with the specified position
     */
    public List<Employee> filterByPosition(String position) {
        return employeeManager.filterByPosition(position);
    }

    /**
     * Gets the EmployeeManager instance
     * 
     * @return The EmployeeManager
     */
    public EmployeeManager getEmployeeManager() {
        return employeeManager;
    }

    /**
     * Calculates statistics on employee data
     * 
     * @return A map containing statistics (total employees, total salary, etc.)
     */
    public java.util.Map<String, Object> calculateEmployeeStatistics() {
        List<Employee> employees = getAllEmployees();
        java.util.Map<String, Object> stats = new java.util.HashMap<>();

        if (employees.isEmpty()) {
            stats.put("hasEmployees", false);
            return stats;
        }

        stats.put("hasEmployees", true);
        stats.put("totalEmployees", employees.size());

        double totalSalary = 0;
        Employee highestPaid = employees.get(0);
        Employee lowestPaid = employees.get(0);

        for (Employee emp : employees) {
            double salary = ((model.Payable) emp).calculateSalary();
            totalSalary += salary;

            if (((model.Payable) emp).calculateSalary() > ((model.Payable) highestPaid).calculateSalary()) {
                highestPaid = emp;
            }

            if (((model.Payable) emp).calculateSalary() < ((model.Payable) lowestPaid).calculateSalary()) {
                lowestPaid = emp;
            }
        }

        stats.put("totalSalary", totalSalary);
        stats.put("highestPaid", highestPaid);
        stats.put("lowestPaid", lowestPaid);
        stats.put("highestSalary", ((model.Payable) highestPaid).calculateSalary());
        stats.put("lowestSalary", ((model.Payable) lowestPaid).calculateSalary());
        
        return stats;
    }

    /**
     * Creates a full-time employee with the given parameters
     */
    public FullTimeEmployee createFullTimeEmployee(String id, String name, Date startDate,
            String position, double coefficient,
            int workingDays, double bonus,
            double overtimeHours) {
        double baseSalary = getBaseSalaryByPosition(position);
        double overtimeSalary = overtimeHours * 50000; // Standard overtime rate

        return new FullTimeEmployee(id, name, startDate, position,
                baseSalary, coefficient, workingDays,
                bonus, overtimeSalary);
    }

    /**
     * Creates a part-time employee with the given parameters
     */
    public PartTimeEmployee createPartTimeEmployee(String id, String name, Date startDate,
            String position, int hoursWorked,
            double hourlyRate, double overtimeSalary) {
        double baseSalary = getBaseSalaryByPosition(position);

        return new PartTimeEmployee(id, name, startDate, position,
                baseSalary, hoursWorked, hourlyRate,
                overtimeSalary);
    }

    /**
     * Returns the base salary for a given position
     */
    public double getBaseSalaryByPosition(String position) {
        switch (position) {
            case "Giám đốc":
                return 150000000;
            case "Quản lý":
                return 15000000;
            case "Nhân viên":
                return 10000000;
            default:
                return 0;
        }
    }
}
