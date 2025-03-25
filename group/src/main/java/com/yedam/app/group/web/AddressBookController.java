package com.yedam.app.group.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yedam.app.group.service.AddressBookService;
import com.yedam.app.group.service.AddressBookVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.PageListVO;
import com.yedam.app.group.service.Paging;

/** 메일
 *  @author 이신영
 *  @since 2025-01-02
 *  <pre>
 *  <pre>
 *  수정일자 수정자 수정내용
 *  ~~~~~~~~~~~~~~~~~~~~~~
 *  
 *  
 *  
 *  
 *  </pre>
 */


@Controller
public class AddressBookController {

	private AddressBookService addressBookService;
	private EmpService empService;
	
	@Autowired
	public AddressBookController(AddressBookService addressBookService, EmpService empService) {
		this.addressBookService = addressBookService;
		this.empService = empService;
	}
	
	/**
	 * 
	 * 메일 목록 출력하기
	 * @param vo : 메일번호
	 * @return 조회 페이지명
	 */
	
	//메일목록
		 @GetMapping("bookList")
		    public String list(Model model, PageListVO vo, Paging paging) {
			 //페이징처리
			 vo.setStart(paging.getFirst());
			 vo.setEnd(paging.getLast());
			 paging.setTotalRecord(addressBookService.pageGetCount(vo));
			 model.addAttribute("paging", paging);
			// model.addAttribute("vo", vo);
			 List<AddressBookVO> list = addressBookService.AddressBookSelectAll(vo);
			 model.addAttribute("books", list);
		     return "group/book/list";  // mainPage.html을 반환
		    }	
		
			//나의메일목록
		 @GetMapping("myBookList")
		    public String myBookList(Model model, PageListVO vo, Paging paging) {
			 //페이징처리
			 vo.setStart(paging.getFirst());
			 vo.setEnd(paging.getLast());
			 paging.setTotalRecord(addressBookService.pageGetCount(vo));
			 model.addAttribute("paging", paging);
			// model.addAttribute("vo", vo);
			 List<AddressBookVO> list = addressBookService.AddressBookSelectAll(vo);
			 model.addAttribute("books", list);
		     return "group/book/myList";  // mainPage.html을 반환
		    }	

		 //메일상세보기
		 @GetMapping("bookSelect")
		    public String BookSelect(AddressBookVO addressBookVO, Model model) {
			 AddressBookVO findVO = addressBookService.AddressBookSelectInfo(addressBookVO);
		     model.addAttribute("book", findVO);
			 return "group/book/select";  // mainPage.html을 반환
		    }	
		
		 
		 //내 메일상세보기
		 @GetMapping("myBookSelect")
		    public String myBookSelect(AddressBookVO addressBookVO, Model model) {
			 AddressBookVO findVO = addressBookService.MyAddressBookSelectInfo(addressBookVO);
		     model.addAttribute("book", findVO);
			 return "group/book/mySelect";  // mainPage.html을 반환
		    }	
		 
		 
		//등록 - 페이지
		
		//메일등록
		 @GetMapping("/bookInsert")
		    public String bookInsertForm() {
			 return "group/book/insert";
		    }	
		
		//등록 - 처리
		
		//메일등록처리
		 @PostMapping("/bookInsert")
		    public String bookInsertProcess(AddressBookVO addressBookVO) {
		     int did = addressBookService.AddressBookInsert(addressBookVO);
			 String url = null;
			 if (did > -1) {
				 url = "redirect:myBookSelect?addressBookId="+did;
			 }
			 else {
				 url = "redirect:myList";
			 }
			 return url;
		    }	
		
		//수정 - 페이지
			
		//메일수정
		@GetMapping("/bookUpdate")
			public String bookUpdate(AddressBookVO addressBookVO, Model model) {
			AddressBookVO findVO = addressBookService.AddressBookSelectInfo(addressBookVO);
				model.addAttribute("book", findVO);
				return "group/book/update";
			}
		
		//수정 - 처리
		
		//메일수정처리
		 @PostMapping("/bookUpdate")
		 @ResponseBody
		 public Map<String, Object>
		 	bookUpdateAJAXJSON(@RequestBody AddressBookVO addressBookVO) {
			 return addressBookService.AddressBookUpdate(addressBookVO);
		    }	
		
		 
		 //메일삭제
		 
		 @GetMapping("bookDelete")
		    public String bookDelete(int AddressBookId) {
			 addressBookService.AddressBookDelete(AddressBookId);
		      return "redirect:bookList";
		    }
}