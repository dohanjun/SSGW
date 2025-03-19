package com.yedam.app.group.service;

import java.util.List;

public interface ScheduleService {
	
	// 일정등록
	public int saveSchedule(ScheduleVO scheduleVO);
	
	// 일정조회
	public List<ScheduleVO> getAllSchedules();
}
