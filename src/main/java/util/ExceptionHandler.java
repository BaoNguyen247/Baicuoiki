package util;

import java.awt.Component;

import javax.swing.JOptionPane;

import exception.ApplicationException;
import exception.DuplicateIdException;
import exception.ValidationException;
import exception.DatabaseException;

/**
 * Utility class for handling exceptions consistently throughout the application
 */
public class ExceptionHandler {

    /**
     * Handles an exception by showing appropriate messages to the user
     * 
     * @param parent The parent component for dialog display
     * @param ex     The exception to handle
     */
    public static void handleException(Component parent, Exception ex) {
        if (ex instanceof ValidationException) {
            handleValidationException(parent, (ValidationException) ex);
        } else if (ex instanceof DuplicateIdException) {
            handleDuplicateIdException(parent, (DuplicateIdException) ex);
        } else if (ex instanceof DatabaseException) {
            handleDatabaseException(parent, (DatabaseException) ex);
        } else if (ex instanceof ApplicationException) {
            handleApplicationException(parent, (ApplicationException) ex);
        } else {
            handleGenericException(parent, ex);
        }

        // Log the exception
        logException(ex);
    }

    private static void handleValidationException(Component parent, ValidationException ex) {
        JOptionPane.showMessageDialog(
                parent,
                ex.getUserMessage(),
                "Lỗi dữ liệu nhập vào",
                JOptionPane.WARNING_MESSAGE);
    }

    private static void handleDuplicateIdException(Component parent, DuplicateIdException ex) {
        JOptionPane.showMessageDialog(
                parent,
                ex.getMessage(),
                "Lỗi trùng mã",
                JOptionPane.ERROR_MESSAGE);
    }

    private static void handleDatabaseException(Component parent, DatabaseException ex) {
        JOptionPane.showMessageDialog(
                parent,
                "Lỗi cơ sở dữ liệu: " + ex.getMessage() +
                        "\nVui lòng thử lại sau hoặc liên hệ quản trị viên.",
                "Lỗi cơ sở dữ liệu",
                JOptionPane.ERROR_MESSAGE);
    }

    private static void handleApplicationException(Component parent, ApplicationException ex) {
        JOptionPane.showMessageDialog(
                parent,
                ex.getMessage(),
                "Lỗi ứng dụng",
                JOptionPane.ERROR_MESSAGE);
    }

    private static void handleGenericException(Component parent, Exception ex) {
        JOptionPane.showMessageDialog(
                parent,
                "Đã xảy ra lỗi không mong muốn: " + ex.getMessage() +
                        "\nVui lòng thử lại sau hoặc liên hệ quản trị viên.",
                "Lỗi hệ thống",
                JOptionPane.ERROR_MESSAGE);
    }

    private static void logException(Exception ex) {
        // In a production environment, you would log to a file or logging service
        System.err.println("Exception occurred: " + ex.getClass().getName());
        System.err.println("Message: " + ex.getMessage());
        ex.printStackTrace();
    }
}
