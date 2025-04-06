package model;

import java.util.Date;

public class PartTimeEmployee extends Employee implements Payable {
	private int hoursWorked; // Số giờ làm
	private double hourlyRate; // Lương giờ

	public PartTimeEmployee(String id, String fullName, Date startDate, String position, double baseSalary,
			int hoursWorked, double hourlyRate, double overtimeSalary) {
		super(id, fullName, startDate, position, baseSalary, overtimeSalary);
		this.hoursWorked = hoursWorked;
		this.hourlyRate = hourlyRate;
	}

	@Override
	public double calculateSalary() {
		return hoursWorked * hourlyRate + getOvertimeSalary();
	}
}