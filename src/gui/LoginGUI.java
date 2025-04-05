package gui;

import javax.swing.*;
import java.awt.*;
import model.Admin;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private EmployeeGUI employeeGUI;

    // Hardcode thông tin quản trị viên (có thể thay bằng cơ sở dữ liệu sau này)
    private final Admin admin = new Admin("admin", "123456");

    public LoginGUI() {
        setTitle("Đăng nhập quản trị viên");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Căn giữa màn hình
        setLayout(new GridLayout(3, 2, 10, 10));

        // Khởi tạo các thành phần giao diện
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Đăng nhập");

        // Thêm các thành phần vào giao diện
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Ô trống để căn chỉnh
        add(loginButton);

        // Xử lý sự kiện nút đăng nhập
        loginButton.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            // Ẩn màn hình đăng nhập và mở giao diện chính
            setVisible(false);
            employeeGUI = new EmployeeGUI();
            employeeGUI.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng!");
        }
    }
}