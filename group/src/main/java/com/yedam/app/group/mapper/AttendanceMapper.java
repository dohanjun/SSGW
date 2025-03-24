package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.AttendanceManagementVO;

@Mapper
public interface AttendanceMapper {
    
    // ✅ 모든 출결 데이터 조회
    List<AttendanceManagementVO> selectAll();

    // ✅ 특정 사원의 출결 데이터 조회
    List<AttendanceManagementVO> selectInfo(Integer employeeNo);

    // ✅ 초과 근무 시간 계산
    Integer calculateTotalOvertime(@Param("employeeNo") Integer employeeNo);

    // ✅ 총 근무시간 및 초과근무시간 조회
    AttendanceManagementVO getAttendanceSummary(@Param("employeeNo") Integer employeeNo);

    // ✅ 출근 등록
    Integer insertClockIn(AttendanceManagementVO vo);

    // ✅ 퇴근 등록 (update)
    Integer updateClockOut(AttendanceManagementVO vo);

    // ✅ 오늘 출근 여부 확인
    Integer countTodayClockIn(@Param("employeeNo") int employeeNo);

    // ✅ 오늘 퇴근 여부 확인
    Integer countTodayClockOut(@Param("employeeNo") int employeeNo);
}
