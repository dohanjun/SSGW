package com.yedam.app.group.web;

import java.util.List;
import java.util.Map;

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

/** 주소록
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
	
	public AddressBookController(AddressBookService addressBookService, EmpService empService) {
		this.addressBookService = addressBookService;
		this.empService = empService;
	}
	
	/**
	 * 
	 * 주소록 목록 출력하기
	 * @param vo : 메일번호
	 * @return 조회 페이지명
	 */
	
	//주소록목록
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
		
			//개인주소록목록
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

		 //부서주소록 상세보기
		 @GetMapping("bookSelect")
		    public String BookSelect(AddressBookVO addressBookVO, Model model) {
			 AddressBookVO findVO = addressBookService.AddressBookSelectInfo(addressBookVO);
		     model.addAttribute("book", findVO);
			 return "group/book/select";  // mainPage.html을 반환
		    }	
		
		 
		 //개인주소록 상세보기
		 @GetMapping("myBookSelect")
		    public String myBookSelect(AddressBookVO addressBookVO, Model model) {
			 AddressBookVO findVO = addressBookService.MyAddressBookSelectInfo(addressBookVO);
		     model.addAttribute("book", findVO);
			 return "group/book/mySelect";
		    }	
		 
		 
		//개인 주소록 등록 - 페이지
		 @GetMapping("/bookInsert")
		    public String bookInsertForm() {
			 return "group/book/insert";
		    }	
		
		//개인 주소록 등록 - 처리
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
		
		//개인주소록 수정 - 페이지
		@GetMapping("/bookUpdate")
			public String bookUpdate(AddressBookVO addressBookVO, Model model) {
			AddressBookVO findVO = addressBookService.AddressBookSelectInfo(addressBookVO);
				model.addAttribute("book", findVO);
				return "group/book/update";
			}
		
		//개인주소록 수정 - 처리
		@PostMapping("/bookUpdate")
		@ResponseBody
		public boolean bookUpdateForm(@RequestBody AddressBookVO addressBookVO, Model model) {
			try {
		        // 수정할 데이터가 넘어오면 서비스에서 업데이트 처리
		    	Map<String, Object> updateSuccess = addressBookService.AddressBookUpdate(addressBookVO);
		        
		        if (updateSuccess != null) {
		            model.addAttribute("message", "주소록이 성공적으로 수정되었습니다.");
		        } else {
		            model.addAttribute("message", "주소록 수정에 실패했습니다.");
		        }
		        
		        return true;  // 수정 후 주소록 목록으로 리다이렉트

		    } catch (Exception e) {
		        model.addAttribute("error", "서버 오류로 수정에 실패했습니다.");
		        return false;  // 오류 발생 시 메일 상세 화면으로 이동
		    }
		}

		
		 
		 //주소록 삭제
		 
		 @GetMapping("bookDelete")
		    public String bookDelete(Integer addressBookId) {
			 addressBookService.AddressBookDelete(addressBookId);
		      return "redirect:bookList";
		    }
}