package model;

import java.util.Date;

public abstract class Employee {
	private String id;
	private String fullName;
	private Date startDate;
	private String position;
	protected double baseSalary;
	private double overtimeSalary; // Thêm lương OT
	public Employee(String id, String fullName, Date startDate, String position, double baseSalary, double overtimeSalary) {
		this.id = id;
		this.fullName = fullName;
		this.startDate = startDate;
		this.position = position;
		this.baseSalary = baseSalary;
		this.overtimeSalary = baseSalary;
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

}