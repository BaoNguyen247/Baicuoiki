package gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.Admin;

public class LoginGUI extends JFrame {
	private JTextField usernameField;
	private JPasswordField passwordField;
	private final Admin admin = new Admin("admin", "123456");

	public LoginGUI() {
		setTitle("Đăng nhập quản trị viên");
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(3, 2, 10, 10));

		JLabel usernameLabel = new JLabel("Tên đăng nhập:");
		usernameField = new JTextField();
		JLabel passwordLabel = new JLabel("Mật khẩu:");
		passwordField = new JPasswordField();
		JButton loginButton = new JButton("Đăng nhập");

		add(usernameLabel);
		add(usernameField);
		add(passwordLabel);
		add(passwordField);
		add(new JLabel());
		add(loginButton);

		loginButton.addActionListener(e -> handleLogin());
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