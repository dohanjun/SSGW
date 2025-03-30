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

    @GetMapping("/blank")
    public String attendanceRecords(HttpSession session, Model model) {
        EmpVO loggedInUser = empService.getLoggedInUserInfo();

        if (loggedInUser != null) {
            Integer employeeNo = loggedInUser.getEmployeeNo();

            List<AttendanceManagementVO> attendanceList = attendanceService.selectInfo(employeeNo);

            int totalWorkedHours = attendanceList.stream()
                    .mapToInt(att -> Math.max(0, att.getTotalWorkingHours() - 1))
                    .sum();

            int normalWorkHoursPerDay = 9;
            int workingDaysPerMonth = 22;
            int monthlyTotalWorkHours = normalWorkHoursPerDay * workingDaysPerMonth;

            int totalOvertimeMinutes = attendanceService.getTotalOvertimeMinutes(employeeNo);

            for (AttendanceManagementVO vo : attendanceList) {
                if (vo.getTotalOvertimeTime() != null) {
                    double overtimeHours = vo.getTotalOvertimeTime() / 60;
                    vo.setOvertimeHours(overtimeHours);
                } else {
                    vo.setOvertimeHours(0.0);
                }
            }

            model.addAttribute("attendanceList", attendanceList);
            model.addAttribute("monthlyTotalWorkHours", monthlyTotalWorkHours);
            model.addAttribute("totalWorkedHours", totalWorkedHours);
            model.addAttribute("overtimeHoursCalculated", totalOvertimeMinutes / 60);
        }

        return "group/workPage/blank";
    }

    // ✅ 관리자용 차트 페이지 이동 (부장 이상만 접근 가능)
    @GetMapping("/chartsManagerPage")
    public String showChartsManagerPage() {
        EmpVO loginUser = empService.getLoggedInUserInfo();
        if (loginUser == null || loginUser.getRankId() != 7) { // 🔥 추가된 부분
            return "redirect:/access-denied";
        }
        return "group/workPage/chartsManager";
    }

    @GetMapping("/todayAttendance")
    @ResponseBody
    public List<AttendanceManagementVO> getTodayDeptAttendance() {
        EmpVO emp = empService.getLoggedInUserInfo();
        if (emp == null || emp.getDepartmentNo() == null) {
            return Collections.emptyList();
        }
        return attendanceService.getTodayAttendanceByDept(emp.getDepartmentNo());
    }

    @GetMapping("/chartsManager")
    @ResponseBody
    public List<AttendanceSummaryDTO> getChartData() {
        EmpVO emp = empService.getLoggedInUserInfo();
        if (emp == null || emp.getDepartmentNo() == null) {
            return Collections.emptyList();
        }
        return attendanceService.getDepartmentAttendanceSummary(emp.getDepartmentNo());
    }

    @GetMapping("/employeeRecord/{employeeNo}")
    @ResponseBody
    public List<AttendanceManagementVO> getEmployeeRecord(@PathVariable("employeeNo") int employeeNo) {
        return attendanceService.selectInfo(employeeNo);
    }
}
