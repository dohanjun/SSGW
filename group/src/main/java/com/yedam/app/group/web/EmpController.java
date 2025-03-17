package com.yedam.app.group.web;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    public String empinsert(Model model) {
        model.addAttribute("suberNo", 1); 									// 회사번호 자동 입력
        model.addAttribute("employeeNo", empService.getNextEmployeeNo()); // 사원번호 자동 설정
        return "group/personnel/empinsert";
    }
	
	
	// 등록 처리
	   @PostMapping("empinsert")
	    public String empInsertProcess(EmpVO empVO, @RequestParam("profileImageFile") MultipartFile file) {
	        try {
	            if (!file.isEmpty()) {
	                empVO.setProfileImageBLOB(file.getBytes()); //  파일을 byte[]로 변환하여 저장
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        empService.createEmpInfo(empVO);
	        return "redirect:main";
	    }
	   
	// 사원관리
	   @GetMapping("empMgmt")
		 public String empMgmt(Model model) {
		// 2) Service
		   List<EmpVO> list = empService.findAllEmp();
		// 2-1) Service의 결과를 View에 전달
			model.addAttribute("emps", list);
			// 3) View
		    return "group/personnel/empMgmt";
		    }
	   
	// 단건 상세조회
	   @GetMapping("empInfo")
	    public String empInfo(EmpVO empVO, Model model) {
	         // 2) Service
		     EmpVO findVO = empService.findempInfo(empVO);	
		     // 2-1) Service의 결과를 View에 전달
		     model.addAttribute("emp", findVO); // addAttribute 애드 어트러뷰트 화면 출력때 사용
		     // 3) View
	        return "group/personnel/empInfo";
	    }
	}

   

