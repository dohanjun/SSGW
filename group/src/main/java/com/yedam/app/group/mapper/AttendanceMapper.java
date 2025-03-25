package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.AttendanceManagementVO;
import com.yedam.app.group.service.AttendanceSummaryDTO;
import com.yedam.app.group.service.OvertimeVO;

@Mapper
public interface AttendanceMapper {

    // ✅ 전체 출결
    List<AttendanceManagementVO> selectAll();

    // ✅ 사원별 출결 조회
    List<AttendanceManagementVO> selectInfo(@Param("employeeNo") int employeeNo);

    // ✅ 출근/퇴근
    int insertClockIn(AttendanceManagementVO vo);
    int updateClockOut(AttendanceManagementVO vo);

    // ✅ 출근/퇴근 여부 체크
    int countTodayClockIn(@Param("employeeNo") int employeeNo);
    int countTodayClockOut(@Param("employeeNo") int employeeNo);

    // ✅ 근무요약 (총 근무시간/초과근무)
    AttendanceManagementVO getAttendanceSummary(@Param("employeeNo") int employeeNo);
    int calculateTotalOvertime(@Param("employeeNo") int employeeNo);

    // ✅ 초과근무 상세
    OvertimeVO getOvertimeByWorkAttitudeId(@Param("workAttitudeId") int workAttitudeId);

    // ✅ 부서 전체 출퇴근 기록 (테이블용)
    List<AttendanceManagementVO> selectDeptAttendance(@Param("deptNo") int deptNo);

    // ✅ 부서 요약 (차트용)
    List<AttendanceSummaryDTO> getDepartmentAttendanceSummary(@Param("deptNo") int deptNo);
}
