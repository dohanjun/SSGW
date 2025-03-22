package com.yedam.app.group.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.yedam.app.group.mapper.AddressBookMapper;
import com.yedam.app.group.service.AddressBookService;
import com.yedam.app.group.service.BookVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.PageListVO;
import com.yedam.app.group.service.Paging;

@Controller
public class AddressBookController {

	private AddressBookMapper addressBookMapper;
	private EmpService empService;
	
	@Autowired
	public AddressBookController(AddressBookMapper addressBookMapper, JavaMailSender javaMailSender) {
		this.addressBookMapper = addressBookMapper;
		this.empService = empService;
	}
	

	
//	 //메일목록
//	 @GetMapping("mail")
//	    public String mail(Model model, PageListVO vo, Paging paging) {
//		 //페이징처리
//		 vo.setStart(paging.getFirst());
//		 vo.setEnd(paging.getLast());
//		 paging.setTotalRecord(AddressBookService.pageGetCount(vo));
//		 model.addAttribute("paging", paging);
//		// model.addAttribute("vo", vo);
//		 List<BookVO> list = addressBookMapper.BookfindAll(vo);
//		 model.addAttribute("books", list);
//	     return "group/mail/mail";  // mainPage.html을 반환
//	    }	
}
