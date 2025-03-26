package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class AttendanceManagementVO {
    private Integer workAttitudeId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date attendanceDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date clockInTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date clockOutTime;

    private String workAttitudeType;
    private String approvalDatetime;
    private int employeeNo;
    private String reason;
    private int totalWorkingHours;

    // ✅ 부서/사원 정보
    private String departmentName;
    private String employeeName;

    // ✅ 초과근무 정보
    private Integer totalOvertimeTime;
    private OvertimeVO overtimeList;
    private Double overtimeHours;
    // ✅ getter 변환 (optional)
    public Double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(Double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }
}
