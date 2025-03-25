package com.yedam.app.group.web;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yedam.app.group.service.AttendanceManagementVO;
import com.yedam.app.group.service.AttendanceService;
import com.yedam.app.group.service.AttendanceSummaryDTO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WorkController {

    private final AttendanceService attendanceService;
    private final EmpService empService;

    // ✅ 개인 출결 조회 페이지
    @GetMapping("/blank")
    public String attendanceRecords(HttpSession session, Model model) {
        EmpVO loggedInUser = empService.getLoggedInUserInfo();

        if (loggedInUser != null) {
            Integer employeeNo = loggedInUser.getEmployeeNo();
            List<AttendanceManagementVO> attendanceList = attendanceService.selectInfo(employeeNo);

            int totalWorkedHours = attendanceList.stream()
                    .mapToInt(att -> Math.max(0, att.getTotalWorkingHours() - 1)) // 점심 제외
                    .sum();

            int normalWorkHoursPerDay = 9;
            int workingDaysPerMonth = 22;
            int monthlyTotalWorkHours = workingDaysPerMonth * normalWorkHoursPerDay;

            int totalOvertimeMinutes = attendanceService.getTotalOvertimeMinutes(employeeNo);

            model.addAttribute("attendanceList", attendanceList);
            model.addAttribute("monthlyTotalWorkHours", monthlyTotalWorkHours);
            model.addAttribute("totalWorkedHours", totalWorkedHours);
            model.addAttribute("overtimeHoursCalculated", totalOvertimeMinutes);
        }

        return "group/workPage/blank";
    }

    // ✅ 차트 + 테이블 페이지
    @GetMapping("/chartsManagerPage")
    public String showChartsManagerPage() {
        return "group/workPage/chartsManager";
    }

    // ✅ 차트용 JSON
    @GetMapping("/chartsManager")
    @ResponseBody
    public List<AttendanceSummaryDTO> getChartData() {
        EmpVO emp = empService.getLoggedInUserInfo();
        if (emp == null || emp.getDepartmentNo() == null) {
            return Collections.emptyList();
        }
        return attendanceService.getDepartmentAttendanceSummary(emp.getDepartmentNo());
    }

    // ✅ 특정 사원의 출퇴근 기록 조회 (그래프 클릭용)
    @GetMapping("/employeeRecord/{empNo}")
    @ResponseBody
    public List<AttendanceManagementVO> getEmployeeRecords(@PathVariable int empNo) {
        return attendanceService.selectInfo(empNo);
    }
}
