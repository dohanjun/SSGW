package com.yedam.app.group.service;

import java.util.List;
import java.util.Map;

public interface AttendanceService {
    // 전체 출퇴근 기록 조회
    List<AttendanceManagementVO> selectAllList();

    // 특정 직원의 출퇴근 기록 조회
    List<AttendanceManagementVO> selectList(Integer employeeNo);
}
