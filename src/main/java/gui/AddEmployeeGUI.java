package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Employee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;
import service.EmployeeManager;

public class AddEmployeeGUI extends JFrame {
	private EmployeeManager manager = new EmployeeManager();
	private MenuGUI menuGUI; // Không cần thay đổi phạm vi truy cập
	private JTextField idField, nameField, startDateField;
	private JComboBox<String> employeeTypeCombo;
	private JComboBox<String> positionCombo;

	public AddEmployeeGUI(MenuGUI menuGUI) {
		this.menuGUI = menuGUI;

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

		nextButton.addActionListener(e -> {
			try {
				String id = idField.getText().trim();
				if (!isValidId(id)) {
					return;
				}
				String name = nameField.getText().trim();
				if (name.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên!");
					return;
				}
				String position = (String) positionCombo.getSelectedItem();
				String employeeType = (String) employeeTypeCombo.getSelectedItem();
				String startDateStr = startDateField.getText().trim();
				if (!startDateStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
					JOptionPane.showMessageDialog(this, "Ngày vào làm phải có định dạng dd/MM/yyyy!");
					return;
				}

				// Parse ngày tháng
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date startDate = sdf.parse(startDateStr);

				// Tạo đối tượng Employee tạm thời
				Employee tempEmployee;
				if (employeeType.equals("Full-time")) {
					tempEmployee = new FullTimeEmployee(id, name, startDate, position, 5000000, 0, 0, 0, 0);
				} else {
					tempEmployee = new PartTimeEmployee(id, name, startDate, position, 10000000, 0, 0, 0);
				}

				// Kiểm tra ID trùng lặp
				if (manager.findEmployeeById(id) != null) {
					JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại!");
					return;
				}

				// Chuyển sang giao diện tính lương
				setVisible(false);
				new MainGUI(tempEmployee, manager, this).setVisible(true);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập dữ liệu hợp lệ! Lỗi: " + ex.getMessage());
			}
		});
	}

	private boolean isValidId(String id) {
		if (!id.matches("\\d+")) {
			JOptionPane.showMessageDialog(this, "Mã nhân viên chỉ được chứa số!");
			return false;
		}
		if (id.length() > 4) {
			JOptionPane.showMessageDialog(this, "Mã nhân viên không được quá 4 chữ số!");
			return false;
		}
		return true;
	}

	// Thêm getter để truy cập menuGUI
	public MenuGUI getMenuGUI() {
		return menuGUI;
	}
}