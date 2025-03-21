package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yedam.app.group.mapper.AttendanceMapper;
import com.yedam.app.group.service.AttendanceManagementVO;
import com.yedam.app.group.service.AttendanceService;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceMapper attendanceMapper;

    @Autowired
    public AttendanceServiceImpl(AttendanceMapper attendanceMapper) {
        this.attendanceMapper = attendanceMapper;
    }

    // ✅ 기존 코드 유지 (출근/퇴근 이외의 기능)
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

    // ✅ 출근 처리 (DB에 저장)
    @Override
    public synchronized void clockIn(Integer employeeNo) {
        try {
            System.out.println("✅ [DEBUG] 출근 SQL 실행 시작 - 사원번호: " + employeeNo);
            attendanceMapper.clockIn(employeeNo);
            System.out.println("✅ [DEBUG] 출근 SQL 실행 완료");
        } catch (Exception e) {
            System.err.println("❌ 출근 기록 저장 실패: " + e.getMessage());
            throw new RuntimeException("출근 기록 저장 실패", e);
        }
    }

    // ✅ 퇴근 처리 (DB에 저장)
    @Override
    public synchronized void clockOut(Integer employeeNo) {
        try {
            System.out.println("✅ [DEBUG] 퇴근 SQL 실행 시작 - 사원번호: " + employeeNo);
            attendanceMapper.clockOut(employeeNo);
            System.out.println("✅ [DEBUG] 퇴근 SQL 실행 완료");
        } catch (Exception e) {
            System.err.println("❌ 퇴근 기록 저장 실패: " + e.getMessage());
            throw new RuntimeException("퇴근 기록 저장 실패", e);
        }
    }
}
