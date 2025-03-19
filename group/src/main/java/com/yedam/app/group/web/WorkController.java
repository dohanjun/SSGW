package com.yedam.app.group.web;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.yedam.app.group.service.AttendanceManagementVO;
import com.yedam.app.group.service.AttendanceService;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import jakarta.servlet.http.HttpSession;
import lombok.Data;

@Data
@Controller
public class WorkController {

    private final AttendanceService attendanceService;
    private final EmpService empService;

    @GetMapping("/blank")
    public String attendanceRecords(HttpSession session, Model model) {
        EmpVO loggedInUser = empService.getLoggedInUserInfo();

        if (loggedInUser != null) {
            Integer employeeNo = loggedInUser.getEmployeeNo();
            List<AttendanceManagementVO> attendanceList = attendanceService.selectList(employeeNo);
            
            // 점심시간 제외한 총 근무시간 계산
            int totalWorkedHours = attendanceList.stream()
                .mapToInt(att -> Math.max(0, att.getTotalWorkingHours() - 1)) // 점심시간 1시간 제외
                .sum();
            
            int normalWorkHoursPerDay = 9;
            int workingDaysPerMonth = 22;
            int monthlyTotalWorkHours = workingDaysPerMonth * normalWorkHoursPerDay;
            int overtimeHours = Math.max(0, totalWorkedHours - (workingDaysPerMonth * normalWorkHoursPerDay));

            model.addAttribute("attendanceList", attendanceList);
            model.addAttribute("monthlyTotalWorkHours", monthlyTotalWorkHours);
            model.addAttribute("totalWorkedHours", totalWorkedHours);
            model.addAttribute("overtimeHours", overtimeHours);
        }
        return "group/workPage/blank";
    }
    
}
