package com.yedam.app.group.service;

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


}
