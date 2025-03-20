package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.ScheduleMapper;
import com.yedam.app.group.service.ScheduleService;
import com.yedam.app.group.service.ScheduleVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
	
	private final ScheduleMapper scheduleMapper;
	
	@Override
	public int saveSchedule(ScheduleVO scheduleVO) {
		return scheduleMapper.insertSchedule(scheduleVO);
	}

	@Override
	public List<ScheduleVO> getAllSchedules(ScheduleVO scheduleVO) {
		return scheduleMapper.getScheduleList(scheduleVO);
	}

}
