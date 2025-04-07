package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.Employee;
import model.FullTimeEmployee;
import service.EmployeeManager;
import util.ExceptionHandler;

public class ManageEmployeeGUI extends JFrame {
	private EmployeeManager manager = new EmployeeManager();
	private MenuGUI menuGUI;
	private JTextField searchField;
	private JTable employeeTable;
	private DefaultTableModel tableModel;

	public ManageEmployeeGUI(MenuGUI menuGUI) {
		this.menuGUI = menuGUI;

		setTitle("Quản lý nhân viên");
		setSize(900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		// Bảng hiển thị nhân viên
		String[] columnNames = { "Mã NV", "Họ tên", "Chức vụ", "Loại NV", "Lương" };
		tableModel = new DefaultTableModel(columnNames, 0);
		employeeTable = new JTable(tableModel);
		employeeTable.setRowHeight(25);
		employeeTable.setGridColor(Color.LIGHT_GRAY);
		employeeTable.setBackground(Color.WHITE);
		employeeTable.setSelectionBackground(new Color(173, 216, 230));
		refreshTable(manager.getEmployees());
		JScrollPane tableScrollPane = new JScrollPane(employeeTable);
		tableScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên"));

		// Panel tìm kiếm và chức năng
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		searchPanel.setBackground(new Color(245, 245, 220));
		searchField = new JTextField(20);
		JButton searchButton = new JButton("Tìm kiếm");
		JButton deleteButton = new JButton("Xóa");
		JButton editButton = new JButton("Sửa");
		JButton sortAscButton = new JButton("Sắp xếp lương tăng");
		JButton sortDescButton = new JButton("Sắp xếp lương giảm");
		JButton statsButton = new JButton("Thống kê");
		JButton backButton = new JButton("Quay lại");

		// Tùy chỉnh màu sắc cho các nút
		searchButton.setBackground(new Color(30, 144, 255));
		searchButton.setForeground(Color.WHITE);
		deleteButton.setBackground(new Color(255, 69, 0));
		deleteButton.setForeground(Color.WHITE);
		editButton.setBackground(new Color(255, 215, 0));
		editButton.setForeground(Color.BLACK);
		sortAscButton.setBackground(new Color(147, 112, 219));
		sortAscButton.setForeground(Color.WHITE);
		sortDescButton.setBackground(new Color(147, 112, 219));
		sortDescButton.setForeground(Color.WHITE);
		statsButton.setBackground(new Color(0, 128, 128));
		statsButton.setForeground(Color.WHITE);
		backButton.setBackground(new Color(255, 69, 0));
		backButton.setForeground(Color.WHITE);

		searchPanel.add(new JLabel("Tìm kiếm theo tên:"));
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		searchPanel.add(deleteButton);
		searchPanel.add(editButton);
		searchPanel.add(sortAscButton);
		searchPanel.add(sortDescButton);
		searchPanel.add(statsButton);
		searchPanel.add(backButton);

		add(tableScrollPane, BorderLayout.CENTER);
		add(searchPanel, BorderLayout.SOUTH);

		// Xử lý sự kiện
		searchButton.addActionListener(e -> searchEmployee());
		deleteButton.addActionListener(e -> deleteEmployee());
		editButton.addActionListener(e -> editEmployee());
		sortAscButton.addActionListener(e -> sortAscending());
		sortDescButton.addActionListener(e -> sortDescending());
		statsButton.addActionListener(e -> showStatistics());
		backButton.addActionListener(e -> {
			setVisible(false);
			menuGUI.setVisible(true);
		});
	}

	private void searchEmployee() {
		String searchText = searchField.getText().trim();
		if (searchText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập tên để tìm kiếm!");
			return;
		}
		List<Employee> results = manager.searchEmployeeByName(searchText);
		refreshTable(results);
	}

	private void deleteEmployee() {
		try {
			int selectedRow = employeeTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để xóa!");
				return;
			}
			String id = (String) tableModel.getValueAt(selectedRow, 0);

			// Confirm before deletion
			int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên này?", "Xác nhận xóa",
					JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				manager.removeEmployee(id);
				JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
				refreshTable(manager.getEmployees());
			}
		} catch (Exception ex) {
			ExceptionHandler.handleException(this, ex);
		}
	}

	private void editEmployee() {
		int selectedRow = employeeTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để sửa!");
			return;
		}
		String id = (String) tableModel.getValueAt(selectedRow, 0);
		Employee existing = manager.findEmployeeById(id);
		if (existing == null) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên!");
			return;
		}
		setVisible(false);
		new AddEmployeeGUI(menuGUI).setVisible(true);
	}

	private void sortAscending() {
		List<Employee> sorted = manager.getEmployeesSortedBySalary(true);
		refreshTable(sorted);
	}

	private void sortDescending() {
		List<Employee> sorted = manager.getEmployeesSortedBySalary(false);
		refreshTable(sorted);
	}

	private void showStatistics() {
		List<Employee> employees = manager.getEmployees();
		if (employees.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Chưa có nhân viên nào trong danh sách!");
			return;
		}

		double totalSalary = 0;
		Employee highestPaid = employees.get(0);
		Employee lowestPaid = employees.get(0);

		for (Employee emp : employees) {
			double salary = ((model.Payable) emp).calculateSalary();
			totalSalary += salary;
			if (((model.Payable) emp).calculateSalary() > ((model.Payable) highestPaid).calculateSalary()) {
				highestPaid = emp;
			}
			if (((model.Payable) emp).calculateSalary() < ((model.Payable) lowestPaid).calculateSalary()) {
				lowestPaid = emp;
			}
		}

		String stats = String.format("Thống kê nhân viên:\n" + "Tổng số nhân viên: %d\n" + "Tổng lương: %s\n"
				+ "Nhân viên lương cao nhất: %s (Lương: %s)\n" + "Nhân viên lương thấp nhất: %s (Lương: %s)",
				employees.size(),
				formatSalary(totalSalary),
				highestPaid.getFullName(),
				formatSalary(((model.Payable) highestPaid).calculateSalary()),
				lowestPaid.getFullName(),
				formatSalary(((model.Payable) lowestPaid).calculateSalary()));
		JOptionPane.showMessageDialog(this, stats);
	}

	private void refreshTable(List<Employee> employees) {
		tableModel.setRowCount(0);
		for (Employee emp : employees) {
			String type = (emp instanceof FullTimeEmployee) ? "Full-time" : "Part-time";
			tableModel.addRow(new Object[] {
					emp.getId(),
					emp.getFullName(),
					emp.getPosition(),
					type,
					formatSalary(((model.Payable) emp).calculateSalary())
			});
		}
	}

	/**
	 * Formats a salary value with thousand separators and appropriate units
	 * 
	 * @param salary The salary value to format
	 * @return The formatted salary string
	 */
	private String formatSalary(double salary) {
		java.text.NumberFormat formatter = java.text.NumberFormat.getNumberInstance(new java.util.Locale("vi", "VN"));

		if (salary >= 1_000_000_000) {
			return formatter.format(salary / 1_000_000_000) + " tỉ đồng";
		} else if (salary >= 1_000_000) {
			return formatter.format(salary / 1_000_000) + " triệu đồng";
		} else {
			return formatter.format(salary / 1_000) + " ngàn đồng";
		}
	}
}
