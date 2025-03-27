package com.yedam.app.group.service;

import java.util.List;

public interface AttendanceService {

    // ✅ [관리자] 전체 사원의 출결 데이터 전체 조회
    // 사용처: 관리자 전체 리스트 페이지
    List<AttendanceManagementVO> selectAll();

    // ✅ [개인] 특정 사원의 출결 기록 조회
    // 사용처: 마이페이지, 출결 조회 화면
    List<AttendanceManagementVO> selectInfo(Integer employeeNo);

    // ✅ 해당 사원의 초과 근무 시간(분 단위) 총합 계산
    // 사용처: 개인 통계, 그래프 데이터
    int getTotalOvertimeMinutes(Integer employeeNo);

    // ✅ 해당 사원의 출결 요약 정보 조회
    // 내용: 총 근무 시간, 오늘 출퇴근 여부 등
    AttendanceManagementVO getAttendanceSummary(Integer employeeNo);

    // ✅ 출근 처리 (Clock-In)
    // 사용처: 사원이 출근 버튼 눌렀을 때 insert
    Integer createClockIn(AttendanceManagementVO vo);

    // ✅ 퇴근 처리 (Clock-Out) 및 초과근무 자동 등록
    // 사용처: 사원이 퇴근 버튼 눌렀을 때 update
    Integer modifyClockOut(AttendanceManagementVO vo);

    // ✅ 오늘 출근 여부 확인 (출근 버튼 중복 방지)
    // 사용처: 출근 버튼 비활성화 조건
    boolean hasClockedInToday(int employeeNo); 

    // ✅ 오늘 퇴근 여부 확인 (퇴근 버튼 중복 방지)
    boolean hasClockedOutToday(int employeeNo); 

    // ✅ [관리자 차트용] 부서 전체 사원의 근무 요약 통계 조회
    // 사용처: 차트(막대, 파이 등) 표시용 데이터
    List<AttendanceSummaryDTO> getDepartmentAttendanceSummary(int departmentNo);

    // ✅ [관리자 테이블용] 부서원 전체 출결 상세 리스트
    // 사용처: 부서 출결 현황 테이블
    List<AttendanceManagementVO> selectDeptAttendance(int departmentNo);

    // ✅ [실시간] 오늘 부서원의 출근 현황만 조회
    // 사용처: 관리자 대시보드의 "오늘 출근한 사원" 표시
    List<AttendanceManagementVO> getTodayAttendanceByDept(int deptNo);

    // ✅ [상세 모달용] 특정 출결 ID에 대한 초과근무 상세 정보 조회
    // 사용처: 모달 팝업 등에서 상세 확인
    OvertimeVO getOvertimeByWorkAttitudeId(int workAttitudeId);
}
