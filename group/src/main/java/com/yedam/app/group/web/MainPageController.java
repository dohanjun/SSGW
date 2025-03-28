package com.yedam.app.group.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.ScheduleService;
import com.yedam.app.group.service.ScheduleVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainPageController {

    @Autowired
    private EmpService empService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/mainPage")
    public String mainPage(HttpSession session, Model model) {
        EmpVO loggedInUser = empService.getLoggedInUserInfo();

        if (loggedInUser != null) {
            Integer employeeNo = loggedInUser.getEmployeeNo();
            model.addAttribute("loginUser", loggedInUser);

            // ✅ 오늘 일정 데이터 조회
            ScheduleVO vo = new ScheduleVO();
            vo.setEmployeeNo(employeeNo);
            vo.setTodayOnly(true); // 오늘 일정 조건
            List<ScheduleVO> todaySchedules = scheduleService.findAllSchedules(vo);

            model.addAttribute("todaySchedules", todaySchedules);
        }

        return "group/mainPage";
    }
}
