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
	    public String mailPage() {
	        return "group/mail/mail";  // mainPage.html을 반환
	    }	
	
	//Service
	
	
	//Service => view
	
	
	
	// prefix : classpath:/templates/
	// suffix : .html
	// classpath:/templates/group/.html
	

		//메일상세보기
	 @GetMapping("/mailSelect")
	    public String mailSelectPage() {
	        return "group/mail/mailSelect";  // mainPage.html을 반환
	    }	
	
	//Service
	
	
	//Service => view
	
	
	
	// prefix : classpath:/templates/
	// suffix : .html
	// classpath:/templates/group/.html
	 
		//내 메일상세보기
	 @GetMapping("/myMailSelect")
	    public String myMailSelectPage() {
	        return "group/mail/myMailSelect";  // mainPage.html을 반환
	    }	
	
	//Service
	
	
	//Service => view
	
	
	
	// prefix : classpath:/templates/
	// suffix : .html
	// classpath:/templates/group/.html
	 
	 
	//등록 - 페이지
	
	//메일등록
	 @GetMapping("/mailInsert")
	    public String mailInsertPage() {
	        return "group/mail/mailInsert";  // mainPage.html을 반환
	    }	
	
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
	
		//수정 - 페이지
		
		//메일수정
		 @GetMapping("/mailUpdate")
		    public String mailUpdatePage() {
		        return "group/mail/mailUpdate";  // mainPage.html을 반환
		    }	
		
		//Service
		
		
		//Service => view
		
		
		
		// prefix : classpath:/templates/
		// suffix : .html
		// classpath:/templates/group/.html
	 
	//답장
	 
	 @GetMapping("/mailReply")
	    public String mailReplyPage() {
	        return "group/mail/mailReply";  // mainPage.html을 반환
	    }	
	 
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
