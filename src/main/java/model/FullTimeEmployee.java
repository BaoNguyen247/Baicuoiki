package model;

import java.util.Date;
import org.bson.Document;

public class FullTimeEmployee extends Employee implements Payable {
	private double coefficient; // Hệ số lương
	private int workingDays; // Số ngày công
	private double bonus; // Thưởng

	public FullTimeEmployee(String id, String fullName, Date startDate, String position, double baseSalary,
			double coefficient, int workingDays, double bonus, double overtimeSalary) {
		super(id, fullName, startDate, position, baseSalary, overtimeSalary);
		this.coefficient = coefficient;
		this.workingDays = workingDays;
		this.bonus = bonus;
	}

	// Constructor from MongoDB Document
	public FullTimeEmployee(Document doc) {
		super(doc);
		this.coefficient = doc.getDouble("coefficient");
		this.workingDays = doc.getInteger("workingDays");
		this.bonus = doc.getDouble("bonus");
	}

	// Thêm getter
	public double getCoefficient() {
		return coefficient;
	}

	public int getWorkingDays() {
		return workingDays;
	}

	public double getBonus() {
		return bonus;
	}

	@Override
	public double calculateSalary() {
		return baseSalary * coefficient * (workingDays / 26.0) + bonus + getOvertimeSalary();
	}

	@Override
	public Document toDocument() {
		Document doc = super.toDocument();
		doc.append("type", "FullTime")
				.append("coefficient", this.coefficient)
				.append("workingDays", this.workingDays)
				.append("bonus", this.bonus)
				.append("overtimeHours", this.getOvertimeSalary() / 50000);
		return doc;
	}
}
