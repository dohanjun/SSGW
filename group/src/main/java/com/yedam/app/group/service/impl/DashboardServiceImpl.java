package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.DashboardMapper;
import com.yedam.app.group.service.ApprovalVO;
import com.yedam.app.group.service.BoardVO;
import com.yedam.app.group.service.DashboardService;
import com.yedam.app.group.service.MailVO;
import com.yedam.app.group.service.RepositoryPostVO;
import com.yedam.app.group.service.ScheduleVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

	private final DashboardMapper dashboardMapper;

	@Override
	public List<ScheduleVO> getTodaySchedule(Integer employeeNo) {
		return dashboardMapper.selectTodaySchedule(employeeNo);
	}

	@Override
	public List<RepositoryPostVO> getRecentRepositoryPosts() {
		return dashboardMapper.selectRecentRepositoryPosts();
	}

	@Override
	public List<ApprovalVO> getRecentApprovalList(Integer employeeNo) {
		return dashboardMapper.selectRecentApprovalList(employeeNo);
	}

	@Override
	public List<BoardVO> getRecentBoardList(int suberNo) {
		return dashboardMapper.selectRecentBoardList(suberNo);
	}
	
	@Override
	public List<MailVO> getRecentMailList(String employeeId) {
	    return dashboardMapper.selectRecentMailList(employeeId);
	}
}
