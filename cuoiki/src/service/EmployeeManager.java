// service/EmployeeManager.java
package service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import exception.DuplicateIdException;
import model.Employee;

public class EmployeeManager {
	private List<Employee> employees;

	public EmployeeManager() {
		employees = new ArrayList<>();
	}

	// Thêm nhân viên (đã có)
	public void addEmployee(Employee emp) throws DuplicateIdException {
		for (Employee e : employees) {
			if (e.getId().equals(emp.getId())) {
				throw new DuplicateIdException("Mã nhân viên đã tồn tại!");
			}
		}
		employees.add(emp);
	}

	// Xóa nhân viên (đã có)
	public void removeEmployee(String id) {
		employees.removeIf(emp -> emp.getId().equals(id));
	}

	// Tìm kiếm nhân viên theo mã
	public Employee findEmployeeById(String id) {
		return employees.stream().filter(emp -> emp.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
	}

	// Tìm kiếm nhân viên theo tên (gần đúng)
	public List<Employee> searchEmployeeByName(String name) {
		return employees.stream().filter(emp -> emp.getFullName().toLowerCase().contains(name.toLowerCase()))
				.collect(Collectors.toList());
	}

	// Lọc nhân viên theo chức vụ
	public List<Employee> filterByPosition(String position) {
		return employees.stream().filter(emp -> emp.getPosition().equalsIgnoreCase(position))
				.collect(Collectors.toList());
	}

	// Lọc nhân viên theo mức lương (lớn hơn hoặc bằng một giá trị)
	public List<Employee> filterBySalary(double minSalary) {
		return employees.stream().filter(emp -> ((model.Payable) emp).calculateSalary() >= minSalary)
				.collect(Collectors.toList());
	}

	// Sửa thông tin nhân viên
	public boolean updateEmployee(String id, Employee updatedEmployee) throws DuplicateIdException {
		Employee existingEmployee = findEmployeeById(id);
		if (existingEmployee == null) {
			return false; // Không tìm thấy nhân viên
		}
		// Kiểm tra mã mới có trùng với nhân viên khác không
		if (!updatedEmployee.getId().equals(id)) {
			for (Employee e : employees) {
				if (e.getId().equals(updatedEmployee.getId())) {
					throw new DuplicateIdException("Mã nhân viên mới đã tồn tại!");
				}
			}
		}

		// Cập nhật thông tin
		int index = employees.indexOf(existingEmployee);
		employees.set(index, updatedEmployee);
		return true;
	}

	// Sắp xếp nhân viên theo lương (tăng dần)
	public void sortBySalaryAscending() {
		employees.sort(Comparator.comparingDouble(emp -> ((model.Payable) emp).calculateSalary()));
	}

	// Sắp xếp nhân viên theo lương (giảm dần)
	public void sortBySalaryDescending() {
		employees.sort(Comparator.comparingDouble(emp -> ((model.Payable) emp).calculateSalary()).reversed());
	}

	// Lưu vào file (đã có)
	public void saveToFile(String filename) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
			oos.writeObject(employees);
		}
	}

	// Đọc từ file (đã có)
	public void loadFromFile(String filename) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			employees = (List<Employee>) ois.readObject();
		}
	}

	// Lấy danh sách nhân viên (đã có)
	public List<Employee> getEmployees() {
		return employees;
	}
}