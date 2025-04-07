package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import exception.DuplicateIdException;
import model.Employee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;
import service.EmployeeManager;

public class MainGUI extends JFrame {
	private EmployeeManager manager;
	private Employee tempEmployee;
	private AddEmployeeGUI addEmployeeGUI;
	private JTextField coefficientField, workingDaysField, bonusField, overtimeHoursField;
	private JTextField hoursWorkedField, hourlyRateField;
	private static final double OVERTIME_RATE = 50000; // Lương OT mặc định: 50,000 VNĐ/giờ
	private JPanel dynamicPanel; // Chuyển khai báo lên cấp độ lớp

	public MainGUI(Employee tempEmployee, EmployeeManager manager, AddEmployeeGUI addEmployeeGUI) {
		this.tempEmployee = tempEmployee;
		this.manager = manager;
		this.addEmployeeGUI = addEmployeeGUI;

		setTitle("Tính lương nhân viên");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		// Panel nhập thông tin lương
		JPanel salaryPanel = new JPanel(new BorderLayout(10, 10));
		salaryPanel.setBorder(BorderFactory.createTitledBorder("Nhập thông tin lương"));
		salaryPanel.setBackground(new Color(240, 248, 255));

		dynamicPanel = new JPanel(new GridLayout(5, 2, 10, 10)); // Khởi tạo tại đây
		coefficientField = new JTextField();
		coefficientField.setEditable(false);
		workingDaysField = new JTextField();
		bonusField = new JTextField();
		overtimeHoursField = new JTextField();
		hoursWorkedField = new JTextField();
		hourlyRateField = new JTextField();
		updateDynamicPanel();

		salaryPanel.add(dynamicPanel, BorderLayout.CENTER);

		// Panel nút
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton backButton = new JButton("Quay lại");
		JButton saveButton = new JButton("Lưu và quay về menu");
		backButton.setBackground(new Color(255, 69, 0));
		backButton.setForeground(Color.WHITE);
		saveButton.setBackground(new Color(50, 205, 50));
		saveButton.setForeground(Color.WHITE);
		buttonPanel.add(backButton);
		buttonPanel.add(saveButton);

		add(salaryPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		// Xử lý sự kiện
		backButton.addActionListener(e -> {
			setVisible(false);
			addEmployeeGUI.setVisible(true);
		});

		saveButton.addActionListener(e -> saveEmployee());
	}

	private void updateDynamicPanel() {
		dynamicPanel.removeAll();
		if (tempEmployee instanceof FullTimeEmployee) {
			String position = tempEmployee.getPosition();
			double coefficient;
			switch (position) {
			case "Giám đốc":
				coefficient = 3;
				break;
			case "Quản lý":
				coefficient = 2;
				break;
			default:
				coefficient = 1;
			}
			coefficientField.setText(String.valueOf(coefficient));

			dynamicPanel.add(new JLabel("Hệ số:"));
			dynamicPanel.add(coefficientField);
			dynamicPanel.add(new JLabel("Ngày công:"));
			dynamicPanel.add(workingDaysField);
			dynamicPanel.add(new JLabel("Thưởng:"));
			dynamicPanel.add(bonusField);
			dynamicPanel.add(new JLabel("Giờ OT:"));
			dynamicPanel.add(overtimeHoursField);
		} else {
			dynamicPanel.add(new JLabel("Số giờ làm:"));
			dynamicPanel.add(hoursWorkedField);
			dynamicPanel.add(new JLabel("Lương giờ:"));
			dynamicPanel.add(hourlyRateField);
		}
		dynamicPanel.revalidate();
		dynamicPanel.repaint();
	}

	private void saveEmployee() {
		try {
			Employee employee;
			if (tempEmployee instanceof FullTimeEmployee) {
				double coefficient = Double.parseDouble(coefficientField.getText().trim());
				int workingDays = Integer.parseInt(workingDaysField.getText().trim());
				double bonus = Double.parseDouble(bonusField.getText().trim());
				double overtimeHours = Double.parseDouble(overtimeHoursField.getText().trim());
				double overtimeSalary = overtimeHours * OVERTIME_RATE;
				employee = new FullTimeEmployee(tempEmployee.getId(), tempEmployee.getFullName(),
						tempEmployee.getStartDate(), tempEmployee.getPosition(), 5000000, coefficient, workingDays,
						bonus, overtimeSalary);
			} else {
				int hoursWorked = Integer.parseInt(hoursWorkedField.getText().trim());
				double hourlyRate = Double.parseDouble(hourlyRateField.getText().trim());
				employee = new PartTimeEmployee(tempEmployee.getId(), tempEmployee.getFullName(),
						tempEmployee.getStartDate(), tempEmployee.getPosition(), tempEmployee.getBaseSalary(),
						hoursWorked, hourlyRate, 0);
			}
			manager.addEmployee(employee);
			JOptionPane.showMessageDialog(this, "Lưu nhân viên thành công!");
			clearFields();
			setVisible(false);
			addEmployeeGUI.getMenuGUI().setVisible(true); // Sử dụng getter để truy cập menuGUI
		} catch (DuplicateIdException ex) {
			JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại!");
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
		}
	}

	private void clearFields() {
		coefficientField.setText("");
		workingDaysField.setText("");
		bonusField.setText("");
		overtimeHoursField.setText("");
		hoursWorkedField.setText("");
		hourlyRateField.setText("");
	}
}