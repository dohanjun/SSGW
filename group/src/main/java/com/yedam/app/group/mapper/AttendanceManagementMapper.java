package com.yedam.app.group.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.AttendanceManagementVO;

@Mapper
public interface AttendanceManagementMapper {
	public int insertAttendanceManagement(AttendanceManagementVO attendVO);
}
