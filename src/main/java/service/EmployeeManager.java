package service;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import exception.DuplicateIdException;
import model.Employee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;

public class EmployeeManager {
	private MongoCollection<Document> employeeCollection;

	public EmployeeManager() {
		MongoDatabase database = MongoDBConnection.getDatabase();
		employeeCollection = database.getCollection("employees");
	}

	public void addEmployee(Employee emp) throws DuplicateIdException {
		if (findEmployeeById(emp.getId()) != null) {
			throw new DuplicateIdException("Mã nhân viên đã tồn tại!");
		}
		Document doc = emp.toDocument();
		employeeCollection.insertOne(doc);
	}

	public void removeEmployee(String id) {
		employeeCollection.deleteOne(Filters.eq("id", id));
	}

	public Employee findEmployeeById(String id) {
		Document doc = employeeCollection.find(Filters.eq("id", id)).first();
		return doc != null ? documentToEmployee(doc) : null;
	}

	public List<Employee> searchEmployeeByName(String name) {
		List<Employee> results = new ArrayList<>();
		for (Document doc : employeeCollection.find(Filters.regex("fullName", name, "i"))) {
			results.add(documentToEmployee(doc));
		}
		return results;
	}

	public List<Employee> filterByPosition(String position) {
		List<Employee> results = new ArrayList<>();
		for (Document doc : employeeCollection.find(Filters.eq("position", position))) {
			results.add(documentToEmployee(doc));
		}
		return results;
	}

	public boolean updateEmployee(String id, Employee updatedEmployee) throws DuplicateIdException {
		Employee existing = findEmployeeById(id);
		if (existing == null) {
			return false;
		}
		if (!updatedEmployee.getId().equals(id) && findEmployeeById(updatedEmployee.getId()) != null) {
			throw new DuplicateIdException("Mã nhân viên mới đã tồn tại!");
		}
		Document doc = updatedEmployee.toDocument();
		employeeCollection.replaceOne(Filters.eq("id", id), doc);
		return true;
	}

	public void sortBySalaryAscending() {
		// Để trống vì sẽ xử lý trong getEmployeesSortedBySalary
	}

	public void sortBySalaryDescending() {
		// Để trống vì sẽ xử lý trong getEmployeesSortedBySalary
	}

	public List<Employee> getEmployees() {
		List<Employee> results = new ArrayList<>();
		for (Document doc : employeeCollection.find()) {
			results.add(documentToEmployee(doc));
		}
		return results;
	}

	public List<Employee> getEmployeesSortedBySalary(boolean ascending) {
		List<Employee> results = new ArrayList<>();
		for (Document doc : employeeCollection.find()
				.sort(ascending ? Sorts.ascending("salary") : Sorts.descending("salary"))) {
			results.add(documentToEmployee(doc));
		}
		return results;
	}

	private Employee documentToEmployee(Document doc) {
		String type = doc.getString("type");

		if ("FullTime".equals(type)) {
			return new FullTimeEmployee(doc);
		} else {
			return new PartTimeEmployee(doc);
		}
	}
}
