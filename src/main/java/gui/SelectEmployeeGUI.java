package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.EmployeeController;
import model.Employee;
import model.FullTimeEmployee;

public class SelectEmployeeGUI extends JFrame {
	private EmployeeController controller;
	private MenuGUI menuGUI;
	private JTable employeeTable;
	private DefaultTableModel tableModel;

	public SelectEmployeeGUI(MenuGUI menuGUI) {
		this.menuGUI = menuGUI;
		this.controller = new EmployeeController();

		setTitle("Chọn nhân viên để tính lương");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		// Bảng hiển thị nhân viên
		String[] columnNames = { "Mã NV", "Họ tên", "Chức vụ", "Loại NV" };
		tableModel = new DefaultTableModel(columnNames, 0);
		employeeTable = new JTable(tableModel);
		employeeTable.setRowHeight(25);
		employeeTable.setGridColor(Color.LIGHT_GRAY);
		employeeTable.setBackground(Color.WHITE);
		employeeTable.setSelectionBackground(new Color(173, 216, 230));
		refreshTable(controller.getAllEmployees());
		JScrollPane tableScrollPane = new JScrollPane(employeeTable);
		tableScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên"));

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

		add(tableScrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		// Xử lý sự kiện
		backButton.addActionListener(e -> {
			setVisible(false);
			menuGUI.setVisible(true);
		});

		nextButton.addActionListener(e -> handleNextButton());
	}

	private void handleNextButton() {
		int selectedRow = employeeTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên!");
			return;
		}

		String id = (String) tableModel.getValueAt(selectedRow, 0);
		Employee selectedEmployee = controller.findEmployeeById(id);

		if (selectedEmployee == null) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên!");
			return;
		}

		setVisible(false);
		new MainGUI(selectedEmployee, controller.getEmployeeManager(), null).setVisible(true);
	}

	private void refreshTable(List<Employee> employees) {
		tableModel.setRowCount(0);
		for (Employee emp : employees) {
			String type = (emp instanceof FullTimeEmployee) ? "Full-time" : "Part-time";
			tableModel.addRow(new Object[] {
					emp.getId(),
					emp.getFullName(),
					emp.getPosition(),
					type
			});
		}
	}
}
