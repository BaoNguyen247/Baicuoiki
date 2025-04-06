	// gui/EmployeeGUI.java
	package gui;

	import java.awt.BorderLayout;
	import java.awt.GridLayout;
	import java.util.Date;

	import javax.swing.JButton;
	import javax.swing.JComboBox;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JOptionPane;
	import javax.swing.JPanel;
	import javax.swing.JScrollPane;
	import javax.swing.JTextArea;
	import javax.swing.JTextField;

	import exception.DuplicateIdException;
	import model.Employee;
	import model.FullTimeEmployee;
	import model.PartTimeEmployee;
	import service.EmployeeManager;

	public class EmployeeGUI extends JFrame {
		private EmployeeManager manager = new EmployeeManager();
		private JTextArea displayArea;
		private JTextField idField, nameField, baseSalaryField;
		private JTextField coefficientField, workingDaysField, bonusField; // Full-time
		private JTextField hoursWorkedField, hourlyRateField; // Part-time
		private JTextField startDateField;
		private JTextField searchField; // Trường tìm kiếm theo tên
		private JComboBox<String> employeeTypeCombo;
		private JComboBox<String> positionCombo;
		private JPanel dynamicPanel;
		private JTextField overtimeSalaryField; // Trường nhập lương OT
		public EmployeeGUI() {
			setTitle("Quản lý nhân viên");
			setSize(800, 600);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLayout(new BorderLayout());

			// Panel nhập liệu chung
			JPanel inputPanel = new JPanel(new GridLayout(6, 2));
			idField = new JTextField();
			nameField = new JTextField();
			baseSalaryField = new JTextField();
			employeeTypeCombo = new JComboBox<>(new String[] { "Full-time", "Part-time" });
			positionCombo = new JComboBox<>(new String[] { "Giám đốc", "Nhân viên", "Quản lý" });
			startDateField = new JTextField("dd/MM/yyyy"); // Định dạng ngày
			searchField = new JTextField();


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
			inputPanel.add(new JLabel("Tìm kiếm theo tên:"));
			inputPanel.add(searchField);


			// Panel động (thay đổi theo loại nhân viên)
			dynamicPanel = new JPanel(new GridLayout(3, 2));
			coefficientField = new JTextField();
			workingDaysField = new JTextField();
			bonusField = new JTextField();
			hoursWorkedField = new JTextField();
			hourlyRateField = new JTextField();
			updateDynamicPanel();

			// Thay đổi giao diện khi chọn loại nhân viên
			employeeTypeCombo.addActionListener(e -> {
				if (!positionCombo.getSelectedItem().equals("Giám đốc")) {
					updateDynamicPanel();
				} else {
					employeeTypeCombo.setSelectedItem("Full-time");
				}
			});

			// Kiểm soát loại nhân viên khi chọn chức vụ
			positionCombo.addActionListener(e -> {
				if (positionCombo.getSelectedItem().equals("Giám đốc")) {
					employeeTypeCombo.setSelectedItem("Full-time");
					employeeTypeCombo.setEnabled(false);
					updateDynamicPanel();
				} else {
					employeeTypeCombo.setEnabled(true);
					updateDynamicPanel();
				}
			});

			// Panel chức năng
			JPanel buttonPanel = new JPanel();
			JButton addButton = new JButton("Thêm");
			JButton updateButton = new JButton("Sửa");
			JButton deleteButton = new JButton("Xóa");
			JButton searchButton = new JButton("Tìm kiếm theo tên");
			JButton filterButton = new JButton("Lọc theo chức vụ");
			JButton sortAscButton = new JButton("Sắp xếp lương tăng");
			JButton sortDescButton = new JButton("Sắp xếp lương giảm");
			JButton displayButton = new JButton("Hiển thị danh sách"); // Nút hiển thị danh sách

			buttonPanel.add(addButton);
			buttonPanel.add(updateButton);
			buttonPanel.add(deleteButton);
			buttonPanel.add(searchButton);
			buttonPanel.add(filterButton);
			buttonPanel.add(sortAscButton);
			buttonPanel.add(sortDescButton);
			buttonPanel.add(displayButton);

			// Khu vực hiển thị
			displayArea = new JTextArea();
			displayArea.setEditable(false); // Không cho chỉnh sửa trực tiếp trên textarea
			add(new JScrollPane(displayArea), BorderLayout.CENTER);
			add(inputPanel, BorderLayout.NORTH);
			add(dynamicPanel, BorderLayout.WEST);
			add(buttonPanel, BorderLayout.SOUTH);

			// Xử lý sự kiện
			addButton.addActionListener(e -> addEmployee());
			updateButton.addActionListener(e -> updateEmployee());
			deleteButton.addActionListener(e -> deleteEmployee());
			searchButton.addActionListener(e -> searchEmployee());
			filterButton.addActionListener(e -> filterEmployee());
			sortAscButton.addActionListener(e -> sortAscending());
			sortDescButton.addActionListener(e -> sortDescending());
			displayButton.addActionListener(e -> displayEmployees()); // Gắn sự kiện cho nút hiển thị
		}

		private void updateDynamicPanel() {
			dynamicPanel.removeAll();
			if (positionCombo.getSelectedItem().equals("Giám đốc")
					|| employeeTypeCombo.getSelectedItem().equals("Full-time")) {
				dynamicPanel.add(new JLabel("Hệ số:"));
				dynamicPanel.add(coefficientField);
				dynamicPanel.add(new JLabel("Ngày công:"));
				dynamicPanel.add(workingDaysField);
				dynamicPanel.add(new JLabel("Thưởng:"));
				dynamicPanel.add(bonusField);
			} else { // Part-time
				dynamicPanel.add(new JLabel("Số giờ làm:"));
				dynamicPanel.add(hoursWorkedField);
				dynamicPanel.add(new JLabel("Lương giờ:"));
				dynamicPanel.add(hourlyRateField);
			}
			// Thêm trường lương OT cho cả hai loại nhân viên
			overtimeSalaryField = new JTextField();
			dynamicPanel.add(new JLabel("Lương OT:"));
			dynamicPanel.add(overtimeSalaryField);
			dynamicPanel.revalidate();
			dynamicPanel.repaint();
		}

		private void addEmployee() {
			try {
				String id = idField.getText().trim();
				if (!isValidId(id)) {
					return;
				}
				Employee emp = createEmployeeFromInput();
				manager.addEmployee(emp);
				JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
				clearFields();
			} catch (DuplicateIdException ex) {
				JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại!");
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
			}
		}

		private boolean isValidId(String id) {
			// Kiểm tra ID chỉ chứa số và không quá 4 chữ số
			if (!id.matches("\\d+")) { // Chỉ chứa số
				JOptionPane.showMessageDialog(this, "Mã nhân viên chỉ được chứa số!");
				return false;
			}
			if (id.length() > 4) { // Không quá 4 chữ số
				JOptionPane.showMessageDialog(this, "Mã nhân viên không được quá 4 chữ số!");
				return false;
			}
			return true;
		}
		private void updateEmployee() {
			try {
				String id = idField.getText().trim();
				if (!isValidId(id)) {
					return;
				}
				Employee emp = createEmployeeFromInput();
				if (manager.updateEmployee(emp.getId(), emp)) {
					JOptionPane.showMessageDialog(this, "Sửa nhân viên thành công!");
					clearFields();
				} else {
					JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên!");
				}
			} catch (DuplicateIdException ex) {
				JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại!");
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
			}
		}

		private void deleteEmployee() {
			String id = idField.getText().trim();
			if (id.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhân viên để xóa!");
				return;
			}
			manager.removeEmployee(id);
			JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
			clearFields();
		}

		private void searchEmployee() {
			String searchText = searchField.getText().trim();
			if (searchText.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập tên để tìm kiếm!");
				return;
			}
			displayArea.setText("Kết quả tìm kiếm theo tên '" + searchText + "':\n");
			var results = manager.searchEmployeeByName(searchText);
			if (results.isEmpty()) {
				displayArea.append("Không tìm thấy nhân viên nào.\n");
			} else {
				results.forEach(this::appendEmployeeToDisplay);
			}
		}

		private void filterEmployee() {
			String position = (String) positionCombo.getSelectedItem();
			displayArea.setText("Danh sách nhân viên theo chức vụ '" + position + "':\n");
			var results = manager.filterByPosition(position);
			if (results.isEmpty()) {
				displayArea.append("Không có nhân viên nào thuộc chức vụ này.\n");
			} else {
				results.forEach(this::appendEmployeeToDisplay);
			}
		}

		private void sortAscending() {
			manager.sortBySalaryAscending();
			displayEmployees();
		}

		private void sortDescending() {
			manager.sortBySalaryDescending();
			displayEmployees();
		}

		private void displayEmployees() {
			displayArea.setText("Danh sách nhân viên:\n");
			if (manager.getEmployees().isEmpty()) {
				displayArea.append("Chưa có nhân viên nào trong danh sách.\n");
			} else {
				manager.getEmployees().forEach(this::appendEmployeeToDisplay);
			}
		}

		private void appendEmployeeToDisplay(Employee emp) {
			String type = (emp instanceof FullTimeEmployee) ? "Full-time" : "Part-time";
			displayArea.append(emp.getId() + " - " + emp.getFullName() + " - " + emp.getPosition() + " - " + type
					+ " - Lương: " + ((model.Payable) emp).calculateSalary() + "\n");
		}

		private Employee createEmployeeFromInput() {
			String id = idField.getText().trim();
			String name = nameField.getText().trim();
			String position = (String) positionCombo.getSelectedItem();
			double baseSalary;
			switch (position) {
				case "Giám đốc":
					baseSalary = 150000000;
					break;
				case "Quản lý":
					baseSalary = 15000000;
					break;
				case "Nhân viên":
					baseSalary = 10000000;
					break;
				default:
					baseSalary = 0;
			}
			double overtimeSalary = Double.parseDouble(overtimeSalaryField.getText().trim());
			if (position.equals("Giám đốc")) {
				double coefficient = Double.parseDouble(coefficientField.getText().trim());
				int workingDays = Integer.parseInt(workingDaysField.getText().trim());
				double bonus = Double.parseDouble(bonusField.getText().trim());
				return new FullTimeEmployee(id, name, new Date(), position, baseSalary, coefficient, workingDays, bonus, overtimeSalary);
			} else {
				if (employeeTypeCombo.getSelectedItem().equals("Full-time")) {
					double coefficient = Double.parseDouble(coefficientField.getText().trim());
					int workingDays = Integer.parseInt(workingDaysField.getText().trim());
					double bonus = Double.parseDouble(bonusField.getText().trim());
					return new FullTimeEmployee(id, name, new Date(), position, baseSalary, coefficient, workingDays,
							bonus, overtimeSalary);
				} else { // Part-time
					int hoursWorked = Integer.parseInt(hoursWorkedField.getText().trim());
					double hourlyRate = Double.parseDouble(hourlyRateField.getText().trim());
					return new PartTimeEmployee(id, name, new Date(), position, baseSalary, hoursWorked, hourlyRate, overtimeSalary);
				}

			}
		}

		private void clearFields() {
			idField.setText("");
			nameField.setText("");
			coefficientField.setText("");
			workingDaysField.setText("");
			bonusField.setText("");
			hoursWorkedField.setText("");
			hourlyRateField.setText("");
			overtimeSalaryField.setText(""); // Xóa trường lương OT
			startDateField.setText("dd/MM/yyyy"); // Đặt lại định dạng ngày
			searchField.setText("");
			positionCombo.setSelectedIndex(0);
			employeeTypeCombo.setEnabled(true);
			employeeTypeCombo.setSelectedIndex(0);
		}
	}