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
}
