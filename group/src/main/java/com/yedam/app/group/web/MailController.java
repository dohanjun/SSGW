package com.yedam.app.group.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.MailService;
import com.yedam.app.group.service.MailVO;
import com.yedam.app.group.service.PageListVO;
import com.yedam.app.group.service.Paging;

@Controller
public class MailController {

	private MailService mailService;
	private EmpService empService;

	@Value("${file.upload-dir}")  
    private String uploadDir;
	
	public MailController(MailService mailService, EmpService empService) {
		this.mailService = mailService;
		this.empService = empService;
	}

	// 메일목록
	@GetMapping("mail")
	public String mail(Model model, PageListVO vo, Paging paging) {

		// 페이징처리
		vo.setStart(paging.getFirst());
		vo.setEnd(paging.getLast());
		paging.setTotalRecord(mailService.pageGetCount(vo));
		model.addAttribute("paging", paging);
		
		List<MailVO> list = mailService.MailSelectAll(vo);
		model.addAttribute("mails", list);

		return "group/mail/mail"; // mainPage.html을 반환
	}

	// 메일상세보기
	@GetMapping("mailSelect")
	public String mailSelect(MailVO mailVO, Model model) {
		
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		mailVO.setEmployeeId(loggedInUser.getEmployeeId());
		
		MailVO findVO = mailService.MailSelectInfo(mailVO);
		model.addAttribute("mail", findVO);
		return "group/mail/mailSelect";
	}

	// 내 메일상세보기
	@GetMapping("myMailSelect")
	public String myMailSelect(MailVO mailVO, Model model) {
		
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		mailVO.setEmployeeId(loggedInUser.getEmployeeId());
		
		MailVO findVO = mailService.MyMailSelectInfo(mailVO);
		model.addAttribute("mail", findVO);
		return "group/mail/myMailSelect";
	}

	// 등록 - 페이지
	@GetMapping("/mailInsert")
	public String mailInsertForm() {
		return "group/mail/mailInsert";
	}


	// 메일등록
	@PostMapping("/mailInsert")
	public String mailInsert(MailVO vo) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setEmployeeId(loggedInUser.getEmployeeId());

		mailService.sendMailToUser(vo);

		return "redirect:mail";
		
	}

	// 수정 - 페이지
	@GetMapping("/mailUpdate")
	public String mailUpdate(MailVO mailVO, Model model) {
		MailVO findVO = mailService.MailSelectInfo(mailVO);
		model.addAttribute("mail", findVO);
		return "group/mail/mailUpdate";
	}


	// 메일수정처리
	@PostMapping("/mailUpdate")
	@ResponseBody
	public Map<String, Object> mailUpdateAJAXJSON(@RequestBody MailVO mailVO) {
		return mailService.MailUpdate(mailVO);
	}

	// 메일답장 페이지
	@GetMapping("/mailReply")
	public String mailReplyForm(MailVO mailVO, Model model) {
		MailVO findVO = mailService.MailSelectInfo(mailVO);
		model.addAttribute("mail", findVO);
		return "group/mail/mailReply";
	}

	// 메일답장처리
	@PostMapping("mailReply")
	public String mailReplyProcess(MailVO mailVO) {
		int mid = mailService.MailPutInfo(mailVO);
		String url = null;
		if (mid > -1) {
			url = "redirect:mailReply?mailId=" + mid;
		} else {
			url = "redirect:mail";
		}
		return url;
	}

	// 전달 - 페이지
	@GetMapping("/mailVery")
	public String mailVery(MailVO mailVO, Model model) {
		MailVO findVO = mailService.MailSelectInfo(mailVO);
		model.addAttribute("mail", findVO);
		return "group/mail/mailVery";
	}


	// 메일전달처리
	@PostMapping("mailVery")
	@ResponseBody
	public Map<String, Object> mailVeryAJAXJSON(@RequestBody MailVO mailVO) {
		return mailService.MailVeryInfo(mailVO);
	}

	// 메일삭제
	@GetMapping("mailDelete")
	public String mailDelete(Integer mailId) {
		mailService.MailDelete(mailId);
		return "redirect:mail";
	}

	
//세부메일함

	// 받은메일함
	@GetMapping("getMail")
	public String getMail(Model model, PageListVO vo, Paging paging) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setMailType("받은");
		vo.setGetUser(loggedInUser.getEmployeeId() +"@whtjdals963123123.com");
		// 페이징처리
		vo.setStart(paging.getFirst());
		vo.setEnd(paging.getLast());
		paging.setTotalRecord(mailService.pageGetCount(vo));
		model.addAttribute("paging", paging);

		List<MailVO> list = mailService.MailSelectAll(vo);
		model.addAttribute("mails", list);
		return "group/mail/getMail"; // mainPage.html을 반환
	}

	// 보낸메일함
	@GetMapping("putMail")
	public String putMail(Model model, PageListVO vo, Paging paging) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setMailType("보낸");
		vo.setEmployeeId(loggedInUser.getEmployeeId() +"@whtjdals963123123.com");
		// 페이징처리loggedInUser
		vo.setStart(paging.getFirst());
		vo.setEnd(paging.getLast());
		paging.setTotalRecord(mailService.pageGetCount(vo));
		model.addAttribute("paging", paging);

		List<MailVO> list = mailService.MailSelectAll(vo);
		model.addAttribute("mails", list);
		return "group/mail/putMail"; // mainPage.html을 반환
	}

	// 임시메일함 -> 7일뒤 삭제
	@GetMapping("temporaryMail")
	public String temporaryMail(Model model, PageListVO vo, Paging paging) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setMailType("임시");
		vo.setEmployeeId(loggedInUser.getEmployeeId() +"@whtjdals963123123.com");
		// 페이징처리
		vo.setStart(paging.getFirst());
		vo.setEnd(paging.getLast());
		paging.setTotalRecord(mailService.pageGetCount(vo));
		model.addAttribute("paging", paging);

		List<MailVO> list = mailService.MailSelectAll(vo);
		model.addAttribute("mails", list);
		return "group/mail/temporaryMail"; // mainPage.html을 반환
	}

	// 휴지통 -> 30일 뒤 삭제
	@GetMapping("deleteMail")
	public String deleteMail(Model model, PageListVO vo, Paging paging) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setMailType("휴지통");
		vo.setEmployeeId(loggedInUser.getEmployeeId() +"@whtjdals963123123.com");
		// 페이징처리
		vo.setStart(paging.getFirst());
		vo.setEnd(paging.getLast());
		paging.setTotalRecord(mailService.pageGetCount(vo));
		model.addAttribute("paging", paging);

		List<MailVO> list = mailService.MailSelectAll(vo);
		model.addAttribute("mails", list);
		return "group/mail/deleteMail"; // mainPage.html을 반환
	}
}