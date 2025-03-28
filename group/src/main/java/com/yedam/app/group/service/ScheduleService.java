package com.yedam.app.group.service;

import java.util.List;

public interface ScheduleService {
	
	// 일정등록
	public int createSchedule(ScheduleVO scheduleVO);
	
	// 일정조회
	public List<ScheduleVO> findAllSchedules(ScheduleVO scheduleVO);
	
	// 일정수정
	public int modifySchedule(ScheduleVO scheduleVO);
	
	// 일정삭제
	public int removeSchedule(ScheduleVO scheduleVO);
	

    // 통합 일정 조회
	List<ScheduleVO> getAllSchedules();

}
