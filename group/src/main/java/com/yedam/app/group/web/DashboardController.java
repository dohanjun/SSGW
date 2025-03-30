package com.yedam.app.group.web;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.yedam.app.group.service.ApprovalVO;
import com.yedam.app.group.service.DashboardService;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.RepositoryPostVO;
import com.yedam.app.group.service.ScheduleVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final EmpService empService;

    @GetMapping("/MainPage")
    public String dashboardPage(Model model, Principal principal) {
        EmpVO loginEmployee = empService.getLoggedInUserInfo();
        model.addAttribute("loginEmployee", loginEmployee);

        if (loginEmployee != null) {
            List<ScheduleVO> scheduleList = dashboardService.getTodaySchedule(loginEmployee.getEmployeeNo());
            List<RepositoryPostVO> repositoryList = dashboardService.getRecentRepositoryPosts();
            List<ApprovalVO> approvalList = dashboardService.getRecentApprovalList(loginEmployee.getEmployeeNo());
            
            

            model.addAttribute("todaySchedule", scheduleList);
            model.addAttribute("repositoryList", repositoryList);
            model.addAttribute("recentApprovalList", approvalList);
            // ✅ 디버깅
            System.out.println("✅ 로그인 사원: " + loginEmployee);
            System.out.println("✅ 오늘 일정 수: " + scheduleList.size());
            System.out.println("✅ 최근 자료실 게시글 수: " + repositoryList.size());
        }

        return "group/mainPage";
    }
}
