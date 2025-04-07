package model;

import java.util.Date;
import java.text.SimpleDateFormat;
import org.bson.Document;

public abstract class Employee {
	private String id;
	private String fullName;
	private Date startDate;
	private String position;
	protected double baseSalary;
	private double overtimeSalary; // Thêm lương OT

	public Employee(String id, String fullName, Date startDate, String position, double baseSalary,
			double overtimeSalary) {
		this.id = id;
		this.fullName = fullName;
		this.startDate = startDate;
		this.position = position;
		this.baseSalary = baseSalary;
		this.overtimeSalary = overtimeSalary;
	}

	// Constructor from MongoDB Document
	public Employee(Document doc) {
		this.id = doc.getString("id");
		this.fullName = doc.getString("fullName");
		this.position = doc.getString("position");
		this.baseSalary = doc.getDouble("baseSalary");
		this.overtimeSalary = doc.getDouble("overtimeSalary");

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			this.startDate = sdf.parse(doc.getString("startDate"));
		} catch (Exception e) {
			this.startDate = new Date();
		}
	}

	// Getters và Setters
	public String getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public String getPosition() {
		return position;
	}

	public double getBaseSalary() {
		return baseSalary;
	}

	public double getOvertimeSalary() {
		return overtimeSalary;
	}

	public void setOvertimeSalary(double overtimeSalary) {
		this.overtimeSalary = overtimeSalary;
	}

	// Convert to MongoDB Document (base implementation)
	public Document toDocument() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Document doc = new Document("id", this.id)
				.append("fullName", this.fullName)
				.append("startDate", sdf.format(this.startDate))
				.append("position", this.position)
				.append("baseSalary", this.baseSalary)
				.append("overtimeSalary", this.overtimeSalary)
				.append("salary", ((Payable) this).calculateSalary());

		return doc;
	}
}
