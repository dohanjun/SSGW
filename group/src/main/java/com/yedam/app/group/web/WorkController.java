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

    // 출결 서비스 및 사원 서비스 의존성 주입
    private final AttendanceService attendanceService;
    private final EmpService empService;

    // ✅ 개인 출결 조회 페이지
    @GetMapping("/blank")
    public String attendanceRecords(HttpSession session, Model model) {
        // 현재 로그인된 사원 정보 가져오기
        EmpVO loggedInUser = empService.getLoggedInUserInfo();

        if (loggedInUser != null) {
            Integer employeeNo = loggedInUser.getEmployeeNo();

            // 해당 사원의 출결 정보 조회
            List<AttendanceManagementVO> attendanceList = attendanceService.selectInfo(employeeNo);

            // 총 근무 시간 계산 (점심시간 1시간 제외)
            int totalWorkedHours = attendanceList.stream()
                    .mapToInt(att -> Math.max(0, att.getTotalWorkingHours() - 1)) // 점심 제외
                    .sum();

            // 한 달 기준 근무 시간 (9시간 × 22일)
            int normalWorkHoursPerDay = 9;
            int workingDaysPerMonth = 22;
            int monthlyTotalWorkHours = normalWorkHoursPerDay * workingDaysPerMonth;

            // 총 초과 근무 시간(분) 조회
            int totalOvertimeMinutes = attendanceService.getTotalOvertimeMinutes(employeeNo);

            // ✅ 각 출결 레코드에 초과근무 시간(시간 단위) 계산하여 셋팅
            for (AttendanceManagementVO vo : attendanceList) {
                if (vo.getTotalOvertimeTime() != null) {
                    double overtimeHours = vo.getTotalOvertimeTime() / 60.0; // 분 ➝ 시간
                    vo.setOvertimeHours(overtimeHours);
                } else {
                    vo.setOvertimeHours(0.0);
                }
            }

            // 모델에 필요한 데이터 추가
            model.addAttribute("attendanceList", attendanceList);
            model.addAttribute("monthlyTotalWorkHours", monthlyTotalWorkHours);
            model.addAttribute("totalWorkedHours", totalWorkedHours);
            model.addAttribute("overtimeHoursCalculated", totalOvertimeMinutes / 60.0); // 차트용 초과근무 시간(시간 단위)
        }

        return "group/workPage/blank"; // 템플릿 반환
    }

    // ✅ 관리자용 차트 페이지 이동 (HTML 반환)
    @GetMapping("/chartsManagerPage")
    public String showChartsManagerPage() {
        return "group/workPage/chartsManager";
    }

    // ✅ 오늘 부서원의 출퇴근 현황 (JSON 반환, 테이블용)
    @GetMapping("/todayAttendance")
    @ResponseBody
    public List<AttendanceManagementVO> getTodayDeptAttendance() {
        EmpVO emp = empService.getLoggedInUserInfo();
        if (emp == null || emp.getDepartmentNo() == null) {
            return Collections.emptyList(); // 로그인 정보 없거나 부서 정보 없으면 빈 리스트 반환
        }
        return attendanceService.getTodayAttendanceByDept(emp.getDepartmentNo());
    }

    // ✅ 부서 출결 요약 차트 데이터 (JSON 반환)
    @GetMapping("/chartsManager")
    @ResponseBody
    public List<AttendanceSummaryDTO> getChartData() {
        EmpVO emp = empService.getLoggedInUserInfo();
        if (emp == null || emp.getDepartmentNo() == null) {
            return Collections.emptyList();
        }
        return attendanceService.getDepartmentAttendanceSummary(emp.getDepartmentNo());
    }

    // ✅ 차트에서 특정 사원을 클릭했을 때 해당 사원의 출결 리스트 조회
    @GetMapping("/employeeRecord/{employeeNo}")
    @ResponseBody
    public List<AttendanceManagementVO> getEmployeeRecord(@PathVariable("employeeNo") int employeeNo) {
        return attendanceService.selectInfo(employeeNo);
    }
}
