package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.AttendanceManagementVO;
import com.yedam.app.group.service.AttendanceSummaryDTO;
import com.yedam.app.group.service.OvertimeVO;

@Mapper
public interface AttendanceMapper {

    // ✅ [관리자용] 전체 사원의 출결 기록을 조회
    // 사용처: 관리자 전체 출결 보기 등
    List<AttendanceManagementVO> selectAll();

    // ✅ [개인] 특정 사원의 출결 기록 조회
    // 사용처: 마이페이지 또는 근무내역 페이지 등
    List<AttendanceManagementVO> selectInfo(@Param("employeeNo") int employeeNo);

    // ✅ 출근 등록 (INSERT)
    // 사용처: 사원이 출근 버튼 눌렀을 때 insert
    int insertClockIn(AttendanceManagementVO vo);

    // ✅ 퇴근 등록 (UPDATE)
    // 사용처: 사원이 퇴근 버튼 눌렀을 때 update
    int updateClockOut(AttendanceManagementVO vo);

    // ✅ 오늘 해당 사원이 출근했는지 확인 (COUNT)
    // 사용처: 출근 여부 판단 후 출근 버튼 비활성화 등
    int countTodayClockIn(@Param("employeeNo") int employeeNo);

    // ✅ 오늘 해당 사원이 퇴근했는지 확인 (COUNT)
    // 사용처: 퇴근 여부 판단
    int countTodayClockOut(@Param("employeeNo") int employeeNo);

    // ✅ 오늘 출결에 대한 요약 정보 조회
    // 내용: 총 근무시간, 초과근무 판단 등
    AttendanceManagementVO getAttendanceSummary(@Param("employeeNo") int employeeNo);

    // ✅ 해당 사원의 초과근무 총합(분 단위) 계산
    // 사용처: 마이페이지에서 누적 초과근무 시간 조회
    int calculateTotalOvertime(@Param("employeeNo") int employeeNo);

    // ✅ 특정 출결 기록에 대한 초과근무 상세 내역 조회
    // 사용처: 상세보기 모달 등에서 초과근무 정보 출력
    OvertimeVO getOvertimeByWorkAttitudeId(@Param("workAttitudeId") int workAttitudeId);

    // ✅ [부서원 전체] 부서에 속한 사원들의 출퇴근 기록 조회
    // 사용처: 관리자 테이블 또는 부서원 리스트
    List<AttendanceManagementVO> selectDeptAttendance(@Param("departmentNo") int departmentNo);

    // ✅ [부서 요약] 부서별 사원의 출결 통계 요약 데이터
    // 내용: 근무시간, 초과근무 시간, 결근 등
    // 사용처: 관리자 차트용 (Bar chart, Pie chart 등)
    List<AttendanceSummaryDTO> getDepartmentAttendanceSummary(@Param("departmentNo") int departmentNo);

    // ✅ [실시간] 오늘 해당 부서의 사원 출근 현황 리스트 조회
    // 사용처: 대시보드 테이블, 실시간 현황판 등
    List<AttendanceManagementVO> selectTodayAttendanceByDept(@Param("departmentNo") int deptNo);

    // ✅ 초과근무 기록 삽입
    // 사용처: 퇴근 시 자동으로 초과근무 기록 insert
    int insertOvertime(OvertimeVO vo);
    
    int updateClockOutAndWorkingHours(AttendanceManagementVO vo);
}
