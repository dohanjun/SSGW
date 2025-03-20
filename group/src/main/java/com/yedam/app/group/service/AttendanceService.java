package com.yedam.app.group.service;

import java.util.List;

public interface AttendanceService {
    // 모든 출결 데이터 조회
    List<AttendanceManagementVO> selectAll();

    // 특정 사원의 출결 데이터 조회
    List<AttendanceManagementVO> selectInfo(Integer employeeNo);

    // 초과 근무 시간 계산
    int getTotalOvertimeMinutes(Integer employeeNo);

    // 총 근무시간 및 초과근무시간 조회
    AttendanceManagementVO getAttendanceSummary(Integer employeeNo);
}
