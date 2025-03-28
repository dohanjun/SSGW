package com.yedam.app.group.service;

import java.time.LocalTime;

import lombok.Data;

@Data // Lombok: getter, setter, toString, equals, hashCode 등을 자동 생성
public class AttendanceSummaryDTO {

    // ✅ 사번 (PK)
    // 사원의 고유 번호. 사원 식별용으로 사용됨
    private Integer employeeNo;

    // ✅ 사원 이름
    private String employeeName;

    // ✅ 부서 이름
    private String departmentName;

    // ✅ 총 근무 시간 (시간 단위, 예: 157.5시간 등)
    // 한 달 또는 기간 내 총 근무 시간의 합산 결과
    private double totalWorkingHours;

    // ✅ 총 초과 근무 시간 (시간 단위)
    // 초과근무로 인정된 시간 총합 (예: 32.5시간 등)
    private double overtimeHours;

    // ✅ 실제 근무일 수
    // 출근 기록이 있는 날의 개수 (예: 20일 출근 → 20)
    private Integer workDayCount;

    // 🔸 Getter & Setter 명시적 정의
    // Lombok이 자동 생성하지만 명확하게 정의되어 있음

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

    // 🔸 현재 이 클래스에서는 사용되지 않지만, 추후 출퇴근 시각 통계에 대비한 메서드로 추정
    public void setClockInTime(LocalTime of) {
        // TODO: 차트에 출근 시각 관련 정보가 필요할 경우 여기에 로직 추가 가능
    }

    public void setClockOutTime(LocalTime of) {
        // TODO: 차트에 퇴근 시각 관련 정보가 필요할 경우 여기에 로직 추가 가능
    }
}
