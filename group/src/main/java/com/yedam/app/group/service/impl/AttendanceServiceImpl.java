package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yedam.app.group.mapper.AttendanceMapper;
import com.yedam.app.group.service.AttendanceManagementVO;
import com.yedam.app.group.service.AttendanceService;
import com.yedam.app.group.service.EmpVO;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceMapper attendanceMapper;

    @Autowired
    public AttendanceServiceImpl(AttendanceMapper attendanceMapper) {
        this.attendanceMapper = attendanceMapper;
    }

    // ✅ 기존 출결 데이터 관련 메서드
    @Override
    public List<AttendanceManagementVO> selectAll() {
        return attendanceMapper.selectAll();
    }

    @Override
    public List<AttendanceManagementVO> selectInfo(Integer employeeNo) {
        return attendanceMapper.selectInfo(employeeNo);
    }

    @Override
    public int getTotalOvertimeMinutes(Integer employeeNo) {
        Integer totalMinutes = attendanceMapper.calculateTotalOvertime(employeeNo);
        return (totalMinutes != null) ? totalMinutes : 0;
    }

    @Override
    public AttendanceManagementVO getAttendanceSummary(Integer employeeNo) {
        return attendanceMapper.getAttendanceSummary(employeeNo);
    }

    // ✅ 출근 처리
    @Override
    public Integer createClockIn(AttendanceManagementVO vo) {
        return attendanceMapper.insertClockIn(vo);
    }

    // ✅ 퇴근 처리
    @Override
    public Integer modifyClockOut(AttendanceManagementVO vo) {
        return attendanceMapper.updateClockOut(vo);
    }

    // ✅ 출근 여부 체크 (하루 1회 제한용)
    @Override
    public boolean hasClockedInToday(int employeeNo) {
        return attendanceMapper.countTodayClockIn(employeeNo) > 0;
    }

    // ✅ 퇴근 여부 체크 (하루 1회 제한용)
    @Override
    public boolean hasClockedOutToday(int employeeNo) {
        return attendanceMapper.countTodayClockOut(employeeNo) > 0;
    }
    
    @Override
    public List<EmpVO> getDepartmentAttendanceSummary(Integer departmentNo) {
        return attendanceMapper.getDepartmentAttendanceSummary(departmentNo);
    }
}
