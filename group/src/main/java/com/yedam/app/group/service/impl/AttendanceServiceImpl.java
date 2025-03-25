package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yedam.app.group.mapper.AttendanceMapper;
import com.yedam.app.group.service.AttendanceManagementVO;
import com.yedam.app.group.service.AttendanceService;
import com.yedam.app.group.service.AttendanceSummaryDTO;
import com.yedam.app.group.service.OvertimeVO;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceMapper attendanceMapper;

    @Autowired
    public AttendanceServiceImpl(AttendanceMapper attendanceMapper) {
        this.attendanceMapper = attendanceMapper;
    }

    // ✅ 전체 출결 데이터 조회
    @Override
    public List<AttendanceManagementVO> selectAll() {
        return attendanceMapper.selectAll();
    }

    // ✅ 사원 출결 조회
    @Override
    public List<AttendanceManagementVO> selectInfo(Integer employeeNo) {
        return attendanceMapper.selectInfo(employeeNo);
    }

    // ✅ 초과 근무 총합 계산
    @Override
    public int getTotalOvertimeMinutes(Integer employeeNo) {
        Integer totalMinutes = attendanceMapper.calculateTotalOvertime(employeeNo);
        return (totalMinutes != null) ? totalMinutes : 0;
    }

    // ✅ 사원별 근무 요약 조회
    @Override
    public AttendanceManagementVO getAttendanceSummary(Integer employeeNo) {
        return attendanceMapper.getAttendanceSummary(employeeNo);
    }

    // ✅ 출근 등록
    @Override
    public Integer createClockIn(AttendanceManagementVO vo) {
        return attendanceMapper.insertClockIn(vo);
    }

    // ✅ 퇴근 처리
    @Override
    public Integer modifyClockOut(AttendanceManagementVO vo) {
        return attendanceMapper.updateClockOut(vo);
    }

    // ✅ 오늘 출근 여부 체크
    @Override
    public boolean hasClockedInToday(int employeeNo) {
        return attendanceMapper.countTodayClockIn(employeeNo) > 0;
    }

    // ✅ 오늘 퇴근 여부 체크
    @Override
    public boolean hasClockedOutToday(int employeeNo) {
        return attendanceMapper.countTodayClockOut(employeeNo) > 0;
    }

    // ✅ 부서 차트용 근무 요약 데이터 조회
    @Override
    public List<AttendanceSummaryDTO> getDepartmentAttendanceSummary(int departmentNo) {
        return attendanceMapper.getDepartmentAttendanceSummary(departmentNo);
    }

    // ✅ 부서 출퇴근 상세 리스트 조회 (테이블용)
    @Override
    public List<AttendanceManagementVO> selectDeptAttendance(int departmentNo) {
        return attendanceMapper.selectDeptAttendance(departmentNo); // 🔄 수정됨
    }

    // ✅ 특정 출결의 초과근무 상세 데이터 조회
    @Override
    public OvertimeVO getOvertimeByWorkAttitudeId(int workAttitudeId) {
        return attendanceMapper.getOvertimeByWorkAttitudeId(workAttitudeId);
    }
}
