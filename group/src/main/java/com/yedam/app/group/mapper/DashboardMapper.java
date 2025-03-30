package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.ApprovalVO;
import com.yedam.app.group.service.BoardVO;
import com.yedam.app.group.service.MailVO;
import com.yedam.app.group.service.RepositoryPostVO;
import com.yedam.app.group.service.ScheduleVO;

public interface DashboardMapper {
    List<ScheduleVO> selectTodaySchedule(Integer employeeNo);
    List<RepositoryPostVO> selectRecentRepositoryPosts();
    List<ApprovalVO> selectRecentApprovalList(Integer employeeNo);
    List<BoardVO> selectRecentBoardList(int suberNo);
    List<MailVO> selectRecentMailList(String employeeId);

}
