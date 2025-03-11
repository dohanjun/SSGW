package com.yedam.app.group.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.yedam.app.group.service.MailService;

@Controller
public class MailController {

	private MailService mailService;
	
//	@Autowired
//	public MailController(MailService mailservice) {
//		this.MailService = mailService;
//	}
	
	

	
	//메일목록
	 @GetMapping("/mail")
	    public String mainPage() {
	        return "group/mail";  // mainPage.html을 반환
	    }	
	
	//Service
	
	
	//Service => view
	
	
	
	// prefix : classpath:/templates/
	// suffix : .html
	// classpath:/templates/group/.html
	
	
	//등록 - 페이지
	
	
	//Service
	
	
	//Service => view
	
	
	
	// prefix : classpath:/templates/
	// suffix : .html
	// classpath:/templates/group/.html
	
	
	
	//등록 - 처리
	
	
	//Service
	
	
	//Service => view
	
	
	
	// prefix : classpath:/templates/
	// suffix : .html
	// classpath:/templates/group/.html
	
	
	//메일목록
	
	
	//Service
	
	
	//Service => view
	
	
	
	// prefix : classpath:/templates/
	// suffix : .html
	// classpath:/templates/group/.html
}
