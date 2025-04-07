package exception;

/**
 * Exception thrown when input validation fails
 */
public class ValidationException extends ApplicationException {

    private String fieldName;

    public ValidationException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getMessage() {
        return "Validation error for " + fieldName + ": " + super.getMessage();
    }

    /**
     * Returns a user-friendly message suitable for display in UI
     */
    public String getUserMessage() {
        return super.getMessage();
    }
}
