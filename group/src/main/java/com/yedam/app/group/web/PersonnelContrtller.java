package com.yedam.app.group.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller


public class PersonnelContrtller {
	 
	// 사원등록

	
	// 사원관리
	
	
	// 단건 상세조회

	
	// 사원 정보 수정
	
	
	// 휴가조회
	@GetMapping("vacaList")
	public String leaveStatus() {
		return "group/personnel/vacaList";
	}
	
	// 휴가유형 등록
	@GetMapping("vacaInsert")
	public String vacaInsert() {
		return "group/personnel/vacaInsert";
	}
	
	// 부서관리
	@GetMapping("deptMgmt")
	public String deptMgmt() {
		return "group/personnel/deptMgmt";
	}
	
	// 부서추가
	@GetMapping("deptInsert")
    public String deptInsert() {
        return "group/personnel/deptInsert";
    }
	
		
}
