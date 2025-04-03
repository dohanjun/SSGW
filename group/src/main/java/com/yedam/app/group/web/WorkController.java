package com.yedam.app.group.web;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/attendance")
    public String attendance(@RequestParam(required = false) String yearMonth, HttpSession session, Model model) {
        EmpVO loggedInUser = empService.getLoggedInUserInfo();

        if (loggedInUser != null) {
            Integer employeeNo = loggedInUser.getEmployeeNo();
            List<AttendanceManagementVO> attendanceList = attendanceService.selectInfo(employeeNo);

            List<AttendanceManagementVO> filteredList = filterByYearMonthAndCalculate(attendanceList, yearMonth);

            int totalWorkedHours = filteredList.stream()
                    .mapToInt(att -> Math.max(0, att.getTotalWorkingHours() - 1))
                    .sum();

            int normalWorkHoursPerDay = 9;
            int workingDaysPerMonth = 22;
            int monthlyTotalWorkHours = normalWorkHoursPerDay * workingDaysPerMonth;

            int totalOvertimeMinutes = filteredList.stream()
                    .filter(vo -> vo.getTotalOvertimeTime() != null)
                    .mapToInt(AttendanceManagementVO::getTotalOvertimeTime)
                    .sum();
            double totalOvertimeHours = totalOvertimeMinutes / 60;

            YearMonth currentYearMonth = (yearMonth != null) ? YearMonth.parse(yearMonth) : YearMonth.now();

            model.addAttribute("attendanceList", filteredList);
            model.addAttribute("monthlyTotalWorkHours", monthlyTotalWorkHours);
            model.addAttribute("totalWorkedHours", totalWorkedHours);
            model.addAttribute("overtimeHoursCalculated", totalOvertimeHours);
            model.addAttribute("selectedYearMonth", currentYearMonth.toString());
        }

        return "group/workPage/blank";
    }

    @GetMapping("/deptAttendance")
    public String deptAttendance(@RequestParam(required = false) String yearMonth,
                                        @RequestParam(required = false) String employeeName,
                                        Model model) {

        EmpVO emp = empService.getLoggedInUserInfo();

        if (emp != null && emp.getDepartmentNo() != null) {
            List<AttendanceManagementVO> allList = attendanceService.getDepartmentAttendanceDetail(emp.getDepartmentNo());

            // 연도, 월에 따른 필터링
            List<AttendanceManagementVO> filteredList = filterByYearMonthAndCalculate(allList, yearMonth);

            if (employeeName != null && !employeeName.isBlank()) {
                filteredList = filteredList.stream()
                        .filter(vo -> employeeName.equals(vo.getEmployeeName()))
                        .collect(Collectors.toList());
            }

            // 근무 시간 및 초과 근무 시간 계산
            int totalWorkedHours = filteredList.stream()
                    .mapToInt(att -> Math.max(0, att.getTotalWorkingHours() - 1))
                    .sum();

            int normalWorkHoursPerDay = 9;
            int workingDaysPerMonth = 22;
            int monthlyTotalWorkHours = normalWorkHoursPerDay * workingDaysPerMonth;
 
            int totalOvertimeMinutes = filteredList.stream()
                    .filter(vo -> vo.getTotalOvertimeTime() != null)
                    .mapToInt(AttendanceManagementVO::getTotalOvertimeTime)
                    .sum();
            double totalOvertimeHours = totalOvertimeMinutes / 60.0;

            // 연도, 월 선택
            YearMonth currentYearMonth = (yearMonth != null) ? YearMonth.parse(yearMonth) : YearMonth.now();

            // 직원 이름 리스트
            List<String> employeeNames = allList.stream()
                    .map(AttendanceManagementVO::getEmployeeName)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            // final로 선언된 filteredList를 이용해 summary 생성
            final List<AttendanceManagementVO> finalFilteredList = filteredList;

            // summaryList 생성
            List<AttendanceSummaryDTO> summaryList = employeeNames.stream()
                    .map(name -> {
                        List<AttendanceManagementVO> list = finalFilteredList.stream()
                                .filter(vo -> name.equals(vo.getEmployeeName()))
                                .collect(Collectors.toList());

                        double total = list.stream().mapToDouble(AttendanceManagementVO::getTotalWorkingHours).sum();
                        double overtime = list.stream().mapToDouble(AttendanceManagementVO::getOvertimeHours).sum();

                        return new AttendanceSummaryDTO(name, total, overtime);  // 초과 근무 시간 포함
                    })
                    .collect(Collectors.toList());

            model.addAttribute("attendanceList", filteredList);
            model.addAttribute("monthlyTotalWorkHours", monthlyTotalWorkHours);
            model.addAttribute("totalWorkedHours", totalWorkedHours);
            model.addAttribute("overtimeHoursCalculated", totalOvertimeHours);
            model.addAttribute("selectedYearMonth", currentYearMonth.toString());
            model.addAttribute("summaryList", summaryList);
            model.addAttribute("employeeNames", employeeNames);
            model.addAttribute("selectedEmployeeName", employeeName);
        }

        return "group/workPage/chartsManager";
    }



    // ✅ 관리자용 근태 데이터 (JSON) (필요시 유지)
    @GetMapping("/chartsManagerData")
    @ResponseBody
    public List<AttendanceSummaryDTO> getChartData() {
        EmpVO emp = empService.getLoggedInUserInfo();
        if (emp == null || emp.getDepartmentNo() == null) {
            return Collections.emptyList();
        }
        return attendanceService.getDepartmentAttendanceSummary(emp.getDepartmentNo());
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

    @GetMapping("/employeeRecord/{employeeNo}")
    @ResponseBody
    public List<AttendanceManagementVO> getEmployeeRecord(@PathVariable("employeeNo") int employeeNo) {
        return attendanceService.selectInfo(employeeNo);
    }

    @GetMapping("/deptAttendanceDetail")
    @ResponseBody
    public List<AttendanceManagementVO> getDeptAttendanceDetail(
            @RequestParam(required = false) String yearMonth) {

        EmpVO emp = empService.getLoggedInUserInfo();
        if (emp == null || emp.getDepartmentNo() == null) {
            return Collections.emptyList();
        }

        List<AttendanceManagementVO> list = attendanceService.getDepartmentAttendanceDetail(emp.getDepartmentNo());
        return filterByYearMonthAndCalculate(list, yearMonth);
    }

    // ✅ 공통 필터 + 초과근무시간 계산
    private List<AttendanceManagementVO> filterByYearMonthAndCalculate(List<AttendanceManagementVO> list, String yearMonth) {
        YearMonth ym = (yearMonth != null) ? YearMonth.parse(yearMonth) : YearMonth.now();

        List<AttendanceManagementVO> filtered = list.stream()
                .filter(att -> {
                    LocalDate date = att.getAttendanceDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    return YearMonth.from(date).equals(ym);
                })
                .collect(Collectors.toList());

        for (AttendanceManagementVO vo : filtered) {
            if (vo.getTotalOvertimeTime() != null) {
                vo.setOvertimeHours(vo.getTotalOvertimeTime() / 60.0); // 초과 근무 시간 계산
            } else {
                vo.setOvertimeHours(0.0);
            }
        }

        return filtered;
    }
}
