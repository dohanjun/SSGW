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
            model.addAttribute("attendanceList", attendanceList);
        }
        return "group/workPage/blank";
    }
    
    
    
//    // 로그인한 사원의 출결 데이터 조회
//    @GetMapping("/blank")
//    public String attendanceRecords(Model model) {
//    	EmpVO loggedInUser = empService.getLoggedInUserInfo();
//       
//
//        if (loggedInUser != null) {
//	        Integer employeeNo = loggedInUser.getEmployeeNo();
//	        List<AttendanceManagementVO> attendanceList = attendanceService.selectList(employeeNo);
//	        model.addAttribute("attendanceList", attendanceList);
//	        
//        }
//
//        return "group/workPage/blank";
//    }
//    @GetMapping("/blank")
//    public String blank() {
//        return "group/workPage/blank"; 
//    }
    @GetMapping("/charts")
    public String chartsManager() {
        return "group/workPage/chartsManager";  
    }
    
    @GetMapping("/cards")
    public String cards() {
        return "etc/register";  
    }
}
