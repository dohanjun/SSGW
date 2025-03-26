// ✅ WorkController.java
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

    // ✅ 관리자 차트 페이지 (HTML)
    @GetMapping("/chartsManagerPage")
    public String showChartsManagerPage() {
        return "group/workPage/chartsManager";
    }

    // ✅ 오늘 부서원 출퇴근 현황 (테이블용)
    @GetMapping("/todayAttendance")
    @ResponseBody
    public List<AttendanceManagementVO> getTodayDeptAttendance() {
        EmpVO emp = empService.getLoggedInUserInfo();
        if (emp == null || emp.getDepartmentNo() == null) {
            return Collections.emptyList();
        }
        return attendanceService.getTodayAttendanceByDept(emp.getDepartmentNo());
    }

    // ✅ 부서 출결 요약 차트 데이터
    @GetMapping("/chartsManager")
    @ResponseBody
    public List<AttendanceSummaryDTO> getChartData() {
        EmpVO emp = empService.getLoggedInUserInfo();
        if (emp == null || emp.getDepartmentNo() == null) {
            return Collections.emptyList();
        }
        return attendanceService.getDepartmentAttendanceSummary(emp.getDepartmentNo());
    }

    // ✅ 차트에서 클릭한 사원의 출결 리스트 조회
    @GetMapping("/employeeRecord/{employeeNo}")
    @ResponseBody
    public List<AttendanceManagementVO> getEmployeeRecord(@PathVariable("employeeNo") int employeeNo) {
        return attendanceService.selectInfo(employeeNo);
    }
} 
