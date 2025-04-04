package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.ScheduleVO;

public interface ScheduleMapper {
	// 일정등록
	public int insertSchedule(ScheduleVO scheduleVO);
	
	// 일정조회
	public List<ScheduleVO> SelectAllScheduleList(ScheduleVO scheduleVO);
	
	// 일정수정
	public int updateSchedule(ScheduleVO scheduleVO);
	
	// 일정 삭제
	public int deleteSchedule(ScheduleVO scheduleVO);
	
	// 공유된 일정 삭제
	public int deleteScheduleSharing(ScheduleVO scheduleVO);
	
	// 일정 공유
	public void insertScheduleSharing(ScheduleVO scheduleVO);
	

	
	// 통합 일정 조회
	List<ScheduleVO> selectAllSchedules();
}
