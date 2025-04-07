package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MenuGUI extends JFrame {
	public MenuGUI() {
		setTitle("Menu Quản lý nhân viên");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(4, 1, 10, 10));

		// Tiêu đề
		JLabel titleLabel = new JLabel("Chọn chức năng", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		add(titleLabel);

		// Các nút lựa chọn
		JButton addEmployeeButton = new JButton("1. Nhập thông tin nhân viên");
		JButton calculateSalaryButton = new JButton("2. Tính lương nhân viên");
		JButton manageEmployeeButton = new JButton("3. Quản lý nhân viên");

		// Tùy chỉnh giao diện nút
		addEmployeeButton.setBackground(new Color(50, 205, 50)); // Màu xanh lá
		addEmployeeButton.setForeground(Color.WHITE);
		calculateSalaryButton.setBackground(new Color(30, 144, 255)); // Màu xanh dương
		calculateSalaryButton.setForeground(Color.WHITE);
		manageEmployeeButton.setBackground(new Color(147, 112, 219)); // Màu tím
		manageEmployeeButton.setForeground(Color.WHITE);

		add(addEmployeeButton);
		add(calculateSalaryButton);
		add(manageEmployeeButton);

		// Xử lý sự kiện
		addEmployeeButton.addActionListener(e -> {
			setVisible(false);
			new AddEmployeeGUI(this).setVisible(true);
		});

		calculateSalaryButton.addActionListener(e -> {
			setVisible(false);
			new SelectEmployeeGUI(this).setVisible(true); // GUI để chọn nhân viên tính lương
		});

		manageEmployeeButton.addActionListener(e -> {
			setVisible(false);
			new ManageEmployeeGUI(this).setVisible(true);
		});
	}
}