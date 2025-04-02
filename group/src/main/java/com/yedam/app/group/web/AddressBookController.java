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
import com.yedam.app.group.service.EmpVO;
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

	private final AddressBookService addressBookService;
	private final EmpService empService;
	
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
			 
			 EmpVO loggedInUser = empService.getLoggedInUserInfo();
			 vo.setEmployeeNo(loggedInUser.getEmployeeNo());
			 
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
			 
			 EmpVO loggedInUser = empService.getLoggedInUserInfo();
			 vo.setEmployeeId(loggedInUser.getEmployeeId());
			 
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
		 public String bookInsertProcess(AddressBookVO addressBookVO, Model model) {
		     String url = ""; // 초기 URL 값 설정
		     
		     try {
		         // 주소록 등록 처리
		         int did = addressBookService.AddressBookInsert(addressBookVO);

		         // 정상적으로 등록된 경우
		         if (did > -1) {
		             url = "redirect:myBookList?addressBookId=" + did;
		             model.addAttribute("alertMessage", "정상적으로 등록되었습니다!");
		         } else {
		             // 등록되지 않은 경우
		             url = "group/book/insert";
		             model.addAttribute("alertMessage", "값을 정확하게 입력해주세요!");
		         }
		     } catch (Exception e) {
		         // 예외가 발생한 경우
		         url = "group/book/insert";
		     }

		     return url;
		 }

		
		//개인주소록 수정 - 페이지
		@GetMapping("/bookUpdate")
			public String bookUpdate(AddressBookVO addressBookVO, Model model) {
			AddressBookVO findVO = addressBookService.MyAddressBookSelectInfo(addressBookVO);
				model.addAttribute("book", findVO);
				return "group/book/update";
			}
		
		//개인주소록 수정 - 처리
		@PostMapping("/bookUpdate")
		@ResponseBody
		public boolean bookUpdateForm(@RequestBody AddressBookVO addressBookVO) {
		    try {
		        // 수정할 데이터가 넘어오면 서비스에서 업데이트 처리
		        Map<String, Object> updateSuccess = addressBookService.AddressBookUpdate(addressBookVO);
		        
		        // 정상적으로 수정되었으면 true 반환
		        return updateSuccess != null && (boolean) updateSuccess.get("result");

		    } catch (Exception e) {
		        e.printStackTrace();
		        return false; // 오류가 발생하면 false 반환
		    }
		}


		 //주소록 삭제
		 
		 @GetMapping("bookDelete")
		    public String bookDelete(Integer addressBookId) {
			 addressBookService.AddressBookDelete(addressBookId);
		      return "redirect:/bookList";
		    }
		 
}