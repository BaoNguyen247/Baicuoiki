# Employee Management System

A Java desktop application for managing employees, calculating salaries, and performing administrative tasks.

## Project Structure

```
Baicuoiki/
├── src/
│   ├── main/
│   │   ├── java/            # Main source code
│   │   │   ├── controller/  # Business logic controllers
│   │   │   ├── dto/         # Data Transfer Objects
│   │   │   ├── exception/   # Custom exceptions
│   │   │   ├── gui/         # User interfaces
│   │   │   ├── main/        # Application entry point
│   │   │   ├── model/       # Data models/entities
│   │   │   ├── service/     # Services for business operations
│   │   │   └── util/        # Utility classes
│   │   └── resources/       # Configuration files
│   └── module-info.java     # Java module declaration
├── pom.xml                  # Maven project configuration
└── README.md                # This file
```

## Application Flow

### 1. Startup Flow

1. **Entry Point**: The application starts from `main.java` in the `main` package.
2. **Database Connection**: On startup, the system attempts to connect to MongoDB using settings from `config.properties`.
3. **Login Screen**: The `LoginGUI` is displayed, showing the database connection status.

### 2. Login Flow

1. **Admin Authentication**:

    - Enter username and password
    - `AdminService` verifies credentials against the database
    - If no admin exists, a default admin (username: "admin", password: "123456") is created

2. **After Successful Login**:
    - `MenuGUI` is displayed with options for employee management

### 3. Employee Management Flow

The application offers three main functionalities accessible from the menu:

#### 3.1. Add Employee Flow

1. **Basic Information Entry**:

    - In `AddEmployeeGUI`, enter employee ID, name, position, type, and start date
    - Input validation is performed by `EmployeeController`
    - Click "Tiếp tục" (Continue)

2. **Salary Information Entry**:
    - In `MainGUI`, enter salary details based on employee type
    - For Full-time: coefficient, working days, bonus, overtime hours
    - For Part-time: hours worked, hourly rate
    - Click "Lưu và quay về menu" (Save and return to menu)

#### 3.2. Calculate Salary Flow

1. **Select Employee**:

    - In `SelectEmployeeGUI`, choose an employee from the list
    - Click "Tiếp tục" (Continue)

2. **View/Update Salary**:
    - In `MainGUI`, view salary information
    - Update details if needed
    - Click "Lưu và quay về menu" (Save and return to menu)

#### 3.3. Manage Employees Flow

1. **View Employee List**:

    - In `ManageEmployeeGUI`, see all employees in a table

2. **Functions Available**:
    - Search: Find employees by name
    - Delete: Remove an employee from the database
    - Edit: Modify employee information
    - Sort: Order employees by salary (ascending/descending)
    - Statistics: View employee statistics (total, highest/lowest salary)

### 4. Data Flow

1. **GUI Layer** (`gui` package):

    - Captures user input
    - Displays data
    - Triggers actions

2. **Controller Layer** (`controller` package):

    - Validates input data
    - Coordinates between GUI and services
    - Handles business logic

3. **Service Layer** (`service` package):

    - Manages database operations
    - Processes business rules
    - Handles data access

4. **Model Layer** (`model` package):

    - Defines data entities
    - Provides business methods (e.g., salary calculation)

5. **Error Handling**:
    - Custom exceptions in `exception` package
    - Unified error handling through `ExceptionHandler` utility

## Employee Types

1. **Full-time Employee**:

    - Salary = Base Salary × Coefficient × (Working Days / 26) + Bonus + Overtime Salary

2. **Part-time Employee**:
    - Salary = Hours Worked × Hourly Rate + Overtime Salary

## Database

The application uses MongoDB to store:

-   Admin accounts
-   Employee information

Connection settings are stored in `config.properties`.

```

This README provides a comprehensive overview of the application structure and flows, making it easier for newcomers to understand how the system works.
```
