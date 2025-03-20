package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.ScheduleVO;

public interface ScheduleMapper {
	// 일정등록
	public int insertSchedule(ScheduleVO scheduleVO);
	
	// 일정조회
	public List<ScheduleVO> getScheduleList(ScheduleVO scheduleVO);
}
