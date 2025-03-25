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

    // ✅ 차트 및 리스트에 필요한 부서/사원 정보 추가
    private String departmentName;
    private String employeeName; // ← 요게 없어서 오류 났던 것!

    // ✅ 초과근무 관련
    private Integer totalOvertimeTime;
    private OvertimeVO overtimeList;

    public double getOvertimeHours() {
        if (overtimeList == null) {
            return 0.0;
        }
        return overtimeList.getOvertimeHours();
    }

    // 불필요한 중복 Getter/Setter는 lombok @Data가 해결해줌
}
