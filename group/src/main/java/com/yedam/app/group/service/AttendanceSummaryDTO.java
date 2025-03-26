package com.yedam.app.group.service;

import java.time.LocalTime;

import lombok.Data;

@Data
public class AttendanceSummaryDTO {
	private Integer employeeNo;
	private String employeeName;
	private String departmentName;
	private double totalWorkingHours;
	private double overtimeHours;
	private Integer workDayCount;
    // 🔸 Getters & Setters
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(Integer employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Double getTotalWorkingHours() {
        return totalWorkingHours;
    }

    public void setTotalWorkingHours(Double totalWorkingHours) {
        this.totalWorkingHours = totalWorkingHours;
    }

    public Double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(Double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

	public void setClockInTime(LocalTime of) {
		
		
	}

	public void setClockOutTime(LocalTime of) {
		
		
	}
}
