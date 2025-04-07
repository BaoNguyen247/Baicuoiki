package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
		Document doc = employeeToDocument(emp);
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
		Document doc = employeeToDocument(updatedEmployee);
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

	private Document employeeToDocument(Employee emp) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Document doc = new Document("id", emp.getId()).append("fullName", emp.getFullName())
				.append("startDate", sdf.format(emp.getStartDate())).append("position", emp.getPosition())
				.append("baseSalary", emp.getBaseSalary()).append("overtimeSalary", emp.getOvertimeSalary())
				.append("salary", ((model.Payable) emp).calculateSalary());

		if (emp instanceof FullTimeEmployee) {
			FullTimeEmployee ft = (FullTimeEmployee) emp;
			doc.append("type", "FullTime").append("coefficient", ft.getCoefficient())
					.append("workingDays", ft.getWorkingDays()).append("bonus", ft.getBonus())
					.append("overtimeHours", ft.getOvertimeSalary() / 50000);
		} else if (emp instanceof PartTimeEmployee) {
			PartTimeEmployee pt = (PartTimeEmployee) emp;
			doc.append("type", "PartTime").append("hoursWorked", pt.getHoursWorked()).append("hourlyRate",
					pt.getHourlyRate());
		}
		return doc;
	}

	private Employee documentToEmployee(Document doc) {
		String id = doc.getString("id");
		String fullName = doc.getString("fullName");
		String position = doc.getString("position");
		double baseSalary = doc.getDouble("baseSalary").doubleValue();
		double overtimeSalary = doc.getDouble("overtimeSalary").doubleValue();
		String type = doc.getString("type");

		Date startDate;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			startDate = sdf.parse(doc.getString("startDate"));
		} catch (Exception e) {
			startDate = new Date();
		}

		if ("FullTime".equals(type)) {
			double coefficient = doc.getDouble("coefficient").doubleValue();
			int workingDays = doc.getInteger("workingDays");
			double bonus = doc.getDouble("bonus").doubleValue();
			return new FullTimeEmployee(id, fullName, startDate, position, baseSalary, coefficient, workingDays, bonus,
					overtimeSalary);
		} else {
			int hoursWorked = doc.getInteger("hoursWorked");
			double hourlyRate = doc.getDouble("hourlyRate").doubleValue();
			return new PartTimeEmployee(id, fullName, startDate, position, baseSalary, hoursWorked, hourlyRate,
					overtimeSalary);
		}
	}
}