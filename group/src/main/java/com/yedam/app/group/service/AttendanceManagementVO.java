package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class AttendanceManagementVO {
	private Integer workAttitudeId;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date attendanceDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date clockInTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date clockOutTime;
	private String workAttitudeType;
	private String approvalDatetime;
	private int employeeNo;
	private String reason;
	private int totalWorkingHours;
}
