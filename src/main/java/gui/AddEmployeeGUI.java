package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.EmployeeController;
import model.Employee;
import util.ExceptionHandler;
import exception.ValidationException;
import exception.ApplicationException;
import exception.DuplicateIdException;

public class AddEmployeeGUI extends JFrame {
	private EmployeeController controller;
	private MenuGUI menuGUI;
	private JTextField idField, nameField, startDateField;
	private JComboBox<String> employeeTypeCombo;
	private JComboBox<String> positionCombo;

	public AddEmployeeGUI(MenuGUI menuGUI) {
		this.menuGUI = menuGUI;
		this.controller = new EmployeeController();

		setTitle("Nhập thông tin nhân viên");
		setSize(400, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		// Panel nhập liệu
		JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
		idField = new JTextField();
		nameField = new JTextField();
		startDateField = new JTextField("dd/MM/yyyy");
		employeeTypeCombo = new JComboBox<>(new String[] { "Full-time", "Part-time" });
		positionCombo = new JComboBox<>(new String[] { "Giám đốc", "Nhân viên", "Quản lý" });

		inputPanel.add(new JLabel("Mã NV:"));
		inputPanel.add(idField);
		inputPanel.add(new JLabel("Họ tên:"));
		inputPanel.add(nameField);
		inputPanel.add(new JLabel("Chức vụ:"));
		inputPanel.add(positionCombo);
		inputPanel.add(new JLabel("Loại NV:"));
		inputPanel.add(employeeTypeCombo);
		inputPanel.add(new JLabel("Ngày vào làm:"));
		inputPanel.add(startDateField);

		// Panel nút
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton backButton = new JButton("Quay lại");
		JButton nextButton = new JButton("Tiếp tục");
		backButton.setBackground(new Color(255, 69, 0));
		backButton.setForeground(Color.WHITE);
		nextButton.setBackground(new Color(50, 205, 50));
		nextButton.setForeground(Color.WHITE);
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);

		add(inputPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		// Xử lý sự kiện
		positionCombo.addActionListener(e -> {
			if (positionCombo.getSelectedItem().equals("Giám đốc")) {
				employeeTypeCombo.setSelectedItem("Full-time");
				employeeTypeCombo.setEnabled(false);
			} else {
				employeeTypeCombo.setEnabled(true);
			}
		});

		backButton.addActionListener(e -> {
			setVisible(false);
			menuGUI.setVisible(true);
		});

		nextButton.addActionListener(e -> handleNextButtonClick());
	}

	private void handleNextButtonClick() {
		try {
			// Validate inputs
			String id = idField.getText().trim();
			controller.validateEmployeeId(id);

			String name = nameField.getText().trim();
			controller.validateEmployeeName(name);

			String position = (String) positionCombo.getSelectedItem();
			String employeeType = (String) employeeTypeCombo.getSelectedItem();
			String startDateStr = startDateField.getText().trim();
			controller.validateStartDate(startDateStr);

			// Parse date
			Date startDate = controller.parseDate(startDateStr);

			// Check if ID already exists
			if (controller.isEmployeeIdExists(id)) {
				throw new DuplicateIdException("Mã nhân viên đã tồn tại!");
			}

			// Create temporary employee for salary calculation
			Employee tempEmployee = controller.createTempEmployee(
					id, name, startDate, position, employeeType);

			// Navigate to salary entry screen
			setVisible(false);
			new MainGUI(tempEmployee, controller.getEmployeeManager(), this).setVisible(true);
		} catch (Exception ex) {
			ExceptionHandler.handleException(this, ex);
		}
	}

	// Thêm getter để truy cập menuGUI
	public MenuGUI getMenuGUI() {
		return menuGUI;
	}
}
