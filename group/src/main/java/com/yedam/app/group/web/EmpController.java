package com.yedam.app.group.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

@Controller
public class EmpController {
	
	private EmpService empService;
	
	@Autowired
	public EmpController(EmpService empService) {
		this.empService = empService;
	}
	
	// 등록 페이지
	@GetMapping("empinsert")
    public String empinsert() {
        return "group/personnel/empinsert";  // mainPage.html을 반환
    }
	
	
	// 등록 처리
	@PostMapping("empinsert")
	public String empInsertProcess(EmpVO empVO) {
		int eno = empService.createEmpInfo(empVO);
		String url = null;
		if(eno > -1) {
			url = "redirect:main";
		}else {
			url = "redirect:empInsert";
		}
		return url;
	}

}
