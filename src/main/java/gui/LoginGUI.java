package gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import model.Admin;
import service.MongoDBConnection;

public class LoginGUI extends JFrame {
	private JTextField usernameField;
	private JPasswordField passwordField;
	private final Admin admin = new Admin("admin", "123456");
	private JLabel connectionStatusLabel;

	public LoginGUI() {
		setTitle("Đăng nhập quản trị viên");
		setSize(300, 230); // Tăng kích thước chiều cao để thêm label kết nối
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(4, 2, 10, 10)); // Thêm 1 hàng để hiển thị status

		JLabel usernameLabel = new JLabel("Tên đăng nhập:");
		usernameField = new JTextField();
		JLabel passwordLabel = new JLabel("Mật khẩu:");
		passwordField = new JPasswordField();
		JButton loginButton = new JButton("Đăng nhập");

		// Thêm label hiển thị trạng thái kết nối
		JLabel dbLabel = new JLabel("Trạng thái DB:");
		connectionStatusLabel = new JLabel("Đang kiểm tra...", SwingConstants.LEFT);
		connectionStatusLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

		add(usernameLabel);
		add(usernameField);
		add(passwordLabel);
		add(passwordField);
		add(dbLabel);
		add(connectionStatusLabel);
		add(new JLabel());
		add(loginButton);

		loginButton.addActionListener(e -> handleLogin());

		// Kiểm tra kết nối MongoDB khi form được khởi tạo
		checkConnection();
	}

	/**
	 * Kiểm tra kết nối MongoDB và cập nhật trạng thái
	 */
	private void checkConnection() {
		new Thread(() -> {
			boolean isConnected = MongoDBConnection.testConnection();

			// Cập nhật UI trên EDT
			javax.swing.SwingUtilities.invokeLater(() -> {
				if (isConnected) {
					connectionStatusLabel.setText("Đã kết nối");
					connectionStatusLabel.setForeground(new Color(0, 128, 0)); // Màu xanh lá
				} else {
					connectionStatusLabel.setText("Không thể kết nối");
					connectionStatusLabel.setForeground(Color.RED);
				}
			});
		}).start();
	}

	private void handleLogin() {
		String username = usernameField.getText().trim();
		String password = new String(passwordField.getPassword()).trim();

		if (username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
			JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
			setVisible(false);
			new MenuGUI().setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng!");
		}
	}
}
