package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yedam.app.group.mapper.AttendanceMapper;
import com.yedam.app.group.service.AttendanceManagementVO;
import com.yedam.app.group.service.AttendanceService;

@Service
@Transactional // ✅ 트랜잭션 처리 추가 (필요한 경우)
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceMapper attendanceMapper;

    @Autowired
    public AttendanceServiceImpl(AttendanceMapper attendanceMapper) {
        this.attendanceMapper = attendanceMapper;
    }

    @Override
    public List<AttendanceManagementVO> selectAllList() {
        return attendanceMapper.selectAllList();
    }

    @Override
    public List<AttendanceManagementVO> selectList(Integer employeeNo) {
        return attendanceMapper.selectList(employeeNo);
    }
}

