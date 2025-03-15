package com.yedam.app.group.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.yedam.app.group.service.MailService;
import com.yedam.app.group.service.MailVO;

@Controller
public class MailController {

	private MailService mailService;
	
	@Autowired
	public MailController(MailService mailService) {
		this.mailService = mailService;
	}
	

	
	//메일목록
	 @GetMapping("/mail")
	    public String mailPage(Model model) {
		 List<MailVO> list = mailService.findAll();
		 model.addAttribute("mails", list);
	     return "group/mail/mail";  // mainPage.html을 반환
	    }	
	

		//메일상세보기
	 @GetMapping("/mailSelect")
	    public String mailSelectPage() {
	        return "group/mail/mailSelect";  // mainPage.html을 반환
	    }	
	
	 
		//내 메일상세보기
	 @GetMapping("myMailSelect")
	    public String myMailSelectPage() {
	        return "group/mail/myMailSelect";  // mainPage.html을 반환
	    }	
	 
	 
	//등록 - 페이지
	
	//메일등록
	 @GetMapping("/mailInsert")
	    public String mailInsertPage(Model model) {
	     MailVO mailVO = mailService.findMailId();
	     model.addAttribute("mails", mailVO);
		 return "group/mail/mailInsert";  // mainPage.html을 반환
	    }	
	
	//등록 - 처리
	
	//메일등록처리
	 @PostMapping("/mailInsert")
	    public String mailInsertProcess(MailVO mailVO) {
	     mailService.addInfo(mailVO);
		 return "redirect:mailList";  // mainPage.html을 반환
	    }	
	
	//수정 - 페이지
		
	//메일수정
	@GetMapping("/mailUpdate")
		public String mailUpdatePage(Model model) {
			MailVO mailVO = mailService.findMailId();
			model.addAttribute("mails", mailVO);
			return "group/mail/mailUpdate";
		}
	
	//수정 - 처리
	
	//메일수정처리
	 @PostMapping("/mailUpdate")
	    public String mailUpdateProcess(MailVO mailVO) {
	     mailService.UpdInfo(mailVO);
		 return "redirect:mailList";  // mainPage.html을 반환
	    }	
	 
	//메일답장
	 
	 @GetMapping("/mailReply")
	    public String mailReplyPage(Model model) {
	      MailVO mailVO = mailService.findMailId();
		  model.addAttribute("mails", mailVO);
	      return "group/mail/mailReply";
	    }
	 
		//답장 - 처리
		
		//메일답장처리
		 @PostMapping("/mailReply")
		    public String mailReplyProcess(MailVO mailVO) {
		     mailService.PutInfo(mailVO);
			 return "redirect:mailList";  // mainPage.html을 반환
		    }
	 
	 //메일삭제
	 
	 @GetMapping("/mailDelete")
	    public String mailDeletePage(Model model) {
	      MailVO mailVO = mailService.findMailId();
		  model.addAttribute("mails", mailVO);
	        return "group/mail/mailDelete";  // mainPage.html을 반환
	    }
	 
		//삭제 - 처리
		
		//메일삭제처리
		 @PostMapping("/mailDelete")
		    public String mailDeleteProcess(MailVO mailVO) {
		     mailService.DelInfo(mailVO);
			 return "redirect:mailList";  // mainPage.html을 반환
		    }
}
