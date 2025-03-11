package com.yedam.app.group.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller


public class PersonnelContrtller {
	 
	// 사원등록
	@GetMapping("empRegister")
	    public String empRegister() {
	        return "group/personnel/empRegister";  // mainPage.html을 반환
	    }
	
	// 사원관리
	@GetMapping("empMgmt")
	    public String empMgmt() {
	        return "group/personnel/empMgmt";
	    }
	
	// 휴가조회
	@GetMapping("leaveStatus")
	    public String leaveStatus() {
	        return "group/personnel/leaveStatus";
	    }
	
	// 부서관리
	@GetMapping("deptMgmt")
	    public String deptMgmt() {
	        return "group/personnel/deptMgmt";
	    }
	
}
