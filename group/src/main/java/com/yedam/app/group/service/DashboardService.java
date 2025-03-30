package com.yedam.app.group.service;

import java.util.List;

public interface DashboardService {
    List<ScheduleVO> getTodaySchedule(Integer employeeNo);
    List<RepositoryPostVO> getRecentRepositoryPosts();
    List<ApprovalVO> getRecentApprovalList(Integer employeeNo);
}
