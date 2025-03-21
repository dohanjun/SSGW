package com.yedam.app.group.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.yedam.app.group.mapper.ScheduleMapper;
import com.yedam.app.group.service.ScheduleService;
import com.yedam.app.group.service.ScheduleVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
	
	private final ScheduleMapper scheduleMapper;
	
	@Override
	public int createSchedule(ScheduleVO scheduleVO) {
		return scheduleMapper.insertSchedule(scheduleVO);
	}

	@Override
	public List<ScheduleVO> findAllSchedules(ScheduleVO scheduleVO) {
		return scheduleMapper.SelectAllScheduleList(scheduleVO);
	}

	@Override
	public int modifySchedule(ScheduleVO scheduleVO) {
		return scheduleMapper.updateSchedule(scheduleVO);
	}

	@Override
	public int removeSchedule(ScheduleVO scheduleVO) {
		return scheduleMapper.deleteSchedule(scheduleVO);
	}

}
