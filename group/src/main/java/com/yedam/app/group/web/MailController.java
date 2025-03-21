package com.yedam.app.group.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yedam.app.group.service.MailService;
import com.yedam.app.group.service.MailVO;
import com.yedam.app.group.service.PageListVO;
import com.yedam.app.group.service.Paging;

@Controller
public class MailController {

	private MailService mailService;
	
	@Autowired
	public MailController(MailService mailService) {
		this.mailService = mailService;
	}
	

	
	 //메일목록
	 @GetMapping("mail")
	    public String mail(Model model, PageListVO vo, Paging paging) {
		 //페이징처리
		 vo.setStart(paging.getFirst());
		 vo.setEnd(paging.getLast());
		 paging.setTotalRecord(mailService.pageGetCount(vo));
		 model.addAttribute("paging", paging);
		// model.addAttribute("vo", vo);
		 List<MailVO> list = mailService.selectAll(vo);
		 model.addAttribute("mails", list);
	     return "group/mail/mail";  // mainPage.html을 반환
	    }	
	

	 //메일상세보기
	 @GetMapping("mailSelect")
	    public String mailSelect(MailVO mailVO, Model model) {
	     MailVO findVO = mailService.selectInfo(mailVO);
	     model.addAttribute("mail", findVO);
		 return "group/mail/mailSelect";  // mainPage.html을 반환
	    }	
	
	 
	 //내 메일상세보기
	 @GetMapping("myMailSelect")
	    public String myMailSelect(MailVO mailVO, Model model) {
	     MailVO findVO = mailService.MySelectInfo(mailVO);
	     model.addAttribute("mail", findVO);
		 return "group/mail/myMailSelect";  // mainPage.html을 반환
	    }	
	 
	 
	//등록 - 페이지
	
	//메일등록
	 @GetMapping("/mailInsert")
	    public String mailInsertForm() {
		 return "group/mail/mailInsert";
	    }	
	
	//등록 - 처리
	
	//메일등록처리
	 @PostMapping("/mailInsert")
	    public String mailInsertProcess(MailVO mailVO) {
	     int mid = mailService.insert(mailVO);
		 String url = null;
		 if (mid > -1) {
			 url = "redirect:mailInfo?mailId="+mid;
		 }
		 else {
			 url = "redirect:mailList";
		 }
		 return url;
	    }	
	
	//수정 - 페이지
		
	//메일수정
	@GetMapping("/mailUpdate")
		public String mailUpdate(MailVO mailVO, Model model) {
			MailVO findVO = mailService.selectInfo(mailVO);
			model.addAttribute("mail", findVO);
			return "group/mail/mailUpdate";
		}
	
	//수정 - 처리
	
	//메일수정처리
	 @PostMapping("/mailUpdate")
	 @ResponseBody
	 public Map<String, Object>
	 	mailUpdateAJAXJSON(@RequestBody MailVO mailVO) {
		 return mailService.update(mailVO);
	    }	
	 
	//메일답장
	 
	 @GetMapping("/mailReply")
	    public String mailReplyForm(MailVO mailVO, Model model) {
		 MailVO findVO = mailService.selectInfo(mailVO);
			model.addAttribute("mail", findVO);
	      return "group/mail/mailReply";
	    }
	 
	//답장 - 처리
		
	//메일답장처리
	@PostMapping("mailReply")
		public String mailReplyProcess(MailVO mailVO) {
		 int mid = mailService.PutInfo(mailVO);
		 String url = null;
		 if(mid > -1) {
			 url = "redirect:mailList?mailId=" + mid;
		 }else {
			 url = "redirect:mailList";
		 }
		 return url;
		    }
	
	//전달 - 페이지
	
	//메일전달
	@GetMapping("/mailVery")
		public String mailVery(MailVO mailVO, Model model) {
			MailVO findVO = mailService.selectInfo(mailVO);
			model.addAttribute("mail", findVO);
			return "group/mail/mailVery";
		}
	
	//전달 - 처리
	
	//메일전달처리
	 @PostMapping("mailVery")
	 @ResponseBody
	 public Map<String, Object>
	 	mailVeryAJAXJSON(@RequestBody MailVO mailVO) {
		 return mailService.VeryInfo(mailVO);
	    } 
	 
	 
	 //메일삭제
	 
	 @GetMapping("mailDelete")
	    public String mailDelete(Integer mailId) {
	      mailService.delete(mailId);
	      return "redirect:mailList";
	    }
	 
//세부메일함
	 
	 //받은메일함
	 @GetMapping("getMail")
	    public String getMail(Model model, PageListVO vo, Paging paging) {
		 
		 //페이징처리
		 vo.setStart(paging.getFirst());
		 vo.setEnd(paging.getLast());
		 paging.setTotalRecord(mailService.pageGetCount(vo));
		 model.addAttribute("paging", paging);
		 
		 List<MailVO> list = mailService.selectAll(vo);
		 model.addAttribute("mails", list);
	     return "group/mail/getMail";  // mainPage.html을 반환
	    }	
	 
	 //보낸메일함
	 @GetMapping("putMail")
	    public String putMail(Model model, PageListVO vo, Paging paging) {
		 
		 //페이징처리
		 vo.setStart(paging.getFirst());
		 vo.setEnd(paging.getLast());
		 paging.setTotalRecord(mailService.pageGetCount(vo));
		 model.addAttribute("paging", paging);
		 
		 List<MailVO> list = mailService.selectAll(vo);
		 model.addAttribute("mails", list);
	     return "group/mail/putMail";  // mainPage.html을 반환
	    }	
	 
	 //임시메일함 -> 7일뒤 삭제
	 @GetMapping("temporaryMail")
	    public String temporaryMail(Model model, PageListVO vo, Paging paging) {
		 
		 //페이징처리
		 vo.setStart(paging.getFirst());
		 vo.setEnd(paging.getLast());
		 paging.setTotalRecord(mailService.pageGetCount(vo));
		 model.addAttribute("paging", paging);
		 
		 List<MailVO> list = mailService.selectAll(vo);
		 model.addAttribute("mails", list);
	     return "group/mail/temporaryMail";  // mainPage.html을 반환
	    }	
	 
	 //휴지통 -> 30일 뒤 삭제
	 @GetMapping("deleteMail")
	    public String deleteMail(Model model, PageListVO vo, Paging paging) {
		 
		 //페이징처리
		 vo.setStart(paging.getFirst());
		 vo.setEnd(paging.getLast());
		 paging.setTotalRecord(mailService.pageGetCount(vo));
		 model.addAttribute("paging", paging);
		 
		 List<MailVO> list = mailService.selectAll(vo);
		 model.addAttribute("mails", list);
	     return "group/mail/deleteMail";  // mainPage.html을 반환
	    }	
	 
	 
	 //메일

	     @GetMapping("authenticate")
	     public ResponseEntity<String> mailTest(@RequestParam("email") String email){
	         return ResponseEntity.ok(mailService.sendMailToUser(email));
	     }
}