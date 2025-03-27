package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.AlarmVO;

@Mapper
public interface AlarmMapper {
	List<AlarmVO> getUnreadAlarmsByEmployeeNo(int employeeNo);

	boolean updateReadStatus(int alertNo);
}
