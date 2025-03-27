package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data // Lombok 어노테이션: getter/setter, toString, equals, hashCode 자동 생성
public class AttendanceManagementVO {

    // ✅ 출결 고유 번호 (기본키, work_attitude_id)
    private Integer workAttitudeId;

    // ✅ 출근 날짜 (yyyy-MM-dd 형식으로 파싱 및 출력)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date attendanceDate;

    // ✅ 출근 시간 (입력 및 출력 시 시간 포함)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date clockInTime;

    // ✅ 퇴근 시간
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date clockOutTime;

    // ✅ 출결 타입 (예: 정규근무, 지각, 조퇴 등)
    private String workAttitudeType;

    // ✅ 결재 완료 시간 (결재된 경우에만 값이 있을 수 있음, 문자열로 저장됨)
    private String approvalDatetime;

    // ✅ 사원 번호 (외래키, employee_no)
    private int employeeNo;

    // ✅ 출결 사유 (지각 사유 등 메모용)
    private String reason;

    // ✅ 총 근무 시간 (단위: 시간) ➝ 퇴근 - 출근 계산 결과
    private int totalWorkingHours;

    // ✅ [조회용] 사원이 속한 부서명 (join 결과)
    private String departmentName;

    // ✅ [조회용] 사원명 (join 결과)
    private String employeeName;

    // ✅ 초과 근무 시간 총합 (단위: 분)
    private Integer totalOvertimeTime;

    // ✅ 초과근무 상세 정보 (1:1 매핑 시 사용)
    private OvertimeVO overtimeList;

    // ✅ 초과 근무 시간 (단위: 시간, Double) ➝ 화면 표시 및 계산용
    private Double overtimeHours;

    // ✅ getter 재정의: Lombok이 만들어주지만 명시적으로 선언해둠
    public Double getOvertimeHours() {
        return overtimeHours;
    }

    // ✅ setter 재정의
    public void setOvertimeHours(Double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }
}
