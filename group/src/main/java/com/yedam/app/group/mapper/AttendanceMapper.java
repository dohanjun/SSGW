package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.AttendanceManagementVO;

@Mapper // ✅ MyBatis Mapper 어노테이션 추가
public interface AttendanceMapper {

    // 모든 출결 데이터 조회
    List<AttendanceManagementVO> selectAllList();

    // 특정 사원의 출결 데이터 조회
    List<AttendanceManagementVO> selectList(Integer employeeNo);
}
