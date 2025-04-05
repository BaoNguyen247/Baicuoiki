package service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import exception.DuplicateIdException;
import model.Employee;

public class EmployeeManager {
	private List<Employee> employees;

	public EmployeeManager() {
		employees = new ArrayList<>();
	}

	// Thêm nhân viên và duy trì thứ tự theo id
	public void addEmployee(Employee emp) throws DuplicateIdException {
		for (Employee e : employees) {
			if (e.getId().equals(emp.getId())) {
				throw new DuplicateIdException("Mã nhân viên đã tồn tại!");
			}
		}
		employees.add(emp);
		// Sắp xếp lại danh sách theo id
		employees.sort(Comparator.comparing(Employee::getId));
	}

	// Xóa nhân viên (không cần thay đổi nhiều vì danh sách vẫn sắp xếp sau khi xóa)
	public void removeEmployee(String id) {
		employees.removeIf(emp -> emp.getId().equals(id));
	}

	// Tìm kiếm nhị phân theo id
	public Employee findEmployeeById(String id) {
		int left = 0;
		int right = employees.size() - 1;

		while (left <= right) {
			int mid = left + (right - left) / 2;
			Employee midEmployee = employees.get(mid);
			int comparison = midEmployee.getId().compareToIgnoreCase(id);

			if (comparison == 0) {
				return midEmployee; // Tìm thấy
			} else if (comparison < 0) {
				left = mid + 1; // Tìm ở nửa bên phải
			} else {
				right = mid - 1; // Tìm ở nửa bên trái
			}
		}
		return null; // Không tìm thấy
	}

	// Tìm kiếm nhân viên theo tên (giữ nguyên vì không áp dụng nhị phân được)
	public List<Employee> searchEmployeeByName(String name) {
		return employees.stream()
				.filter(emp -> emp.getFullName().toLowerCase().contains(name.toLowerCase()))
				.collect(Collectors.toList());
	}

	// Lọc nhân viên theo chức vụ (giữ nguyên)
	public List<Employee> filterByPosition(String position) {
		return employees.stream()
				.filter(emp -> emp.getPosition().equalsIgnoreCase(position))
				.collect(Collectors.toList());
	}

	// Lọc nhân viên theo lương (giữ nguyên)
	public List<Employee> filterBySalary(double minSalary) {
		return employees.stream()
				.filter(emp -> ((model.Payable) emp).calculateSalary() >= minSalary)
				.collect(Collectors.toList());
	}

	// Sửa thông tin nhân viên
	public boolean updateEmployee(String id, Employee updatedEmployee) throws DuplicateIdException {
		Employee existingEmployee = findEmployeeById(id);
		if (existingEmployee == null) {
			return false;
		}
		if (!updatedEmployee.getId().equals(id)) {
			for (Employee e : employees) {
				if (e.getId().equals(updatedEmployee.getId())) {
					throw new DuplicateIdException("Mã nhân viên mới đã tồn tại!");
				}
			}
		}
		int index = employees.indexOf(existingEmployee);
		employees.remove(index); // Xóa nhân viên cũ
		employees.add(updatedEmployee); // Thêm nhân viên mới
		employees.sort(Comparator.comparing(Employee::getId)); // Sắp xếp lại
		return true;
	}

	// Sắp xếp theo lương tăng dần (giữ nguyên)
	public void sortBySalaryAscending() {
		employees.sort(Comparator.comparingDouble(emp -> ((model.Payable) emp).calculateSalary()));
	}

	// Sắp xếp theo lương giảm dần (giữ nguyên)
	public void sortBySalaryDescending() {
		employees.sort(Comparator.comparingDouble(emp -> ((model.Payable) emp).calculateSalary()).reversed());
	}

	// Lưu vào file (giữ nguyên)
	public void saveToFile(String filename) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
			oos.writeObject(employees);
		}
	}

	// Đọc từ file (sắp xếp lại sau khi đọc)
	public void loadFromFile(String filename) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			employees = (List<Employee>) ois.readObject();
			employees.sort(Comparator.comparing(Employee::getId)); // Sắp xếp sau khi đọc
		}
	}

	public List<Employee> getEmployees() {
		return employees;
	}
}