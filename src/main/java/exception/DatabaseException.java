package exception;

/**
 * Exception thrown when a database operation fails
 */
public class DatabaseException extends ApplicationException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
