package com.yedam.app.group.web;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // 리콰이어알규먼트 쓰고 밑에 final
@Controller
public class EmpController {
	
	private final EmpService empService;
	
//	@Autowired @Resource @Qualify 등등 더있음
//	public EmpController(EmpService empService) {
//		this.empService = empService;
//	}
	
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
	// 사원 상세조회   
	   @GetMapping("empInfo")
	   public String empInfo(@RequestParam("employeeNo") int employeeNo, Model model) {
	       // 1) EmpVO 객체 생성 후 employeeNo 설정
	       EmpVO empVO = new EmpVO();
	       empVO.setEmployeeNo(employeeNo);

	       // 2) 사원 정보 조회
	       EmpVO findVO = empService.findempInfo(empVO);

	       // 3) BLOB 데이터를 Base64 문자열로 변환
	       if (findVO.getProfileImageBLOB() != null) {
	           String base64Image = Base64.getEncoder().encodeToString(findVO.getProfileImageBLOB());
	           model.addAttribute("profileImageBase64", base64Image);
	       } else {
	           model.addAttribute("profileImageBase64", null); // 이미지가 없으면 null 처리
	       }

	       // 4) 모델에 데이터 추가
	       model.addAttribute("emp", findVO);

	       return "group/personnel/empInfo";
	   }
	   
		//  사원 정보 수정 페이지 이동
	   @GetMapping("empUpdate")
	   public String empUpdate(@RequestParam("employeeNo") int employeeNo, Model model) {
	       // 1) 기본 생성자로 EmpVO 객체 생성 후 employeeNo 설정
	       EmpVO empVO = new EmpVO();
	       empVO.setEmployeeNo(employeeNo);

	       // 2) 사원 정보 조회
	       EmpVO findVO = empService.findempInfo(empVO);
	       
	       // 3) 사원 정보가 없을 경우 리디렉트
	       if (findVO == null) {
	           return "redirect:/empMgmt";
	       }

	       // 4) 이미지 데이터 처리
	       if (findVO.getProfileImageBLOB() != null) {
	           String base64Image = Base64.getEncoder().encodeToString(findVO.getProfileImageBLOB());
	           model.addAttribute("profileImageBase64", base64Image);
	       } else {
	           model.addAttribute("profileImageBase64", null); // 이미지가 없으면 null 처리
	       }

	       // 5) 모델에 데이터 추가
	       model.addAttribute("emp", findVO);

	       return "group/personnel/empUpdate"; // 뷰 반환
	   }

	   // 사원 정보 수정
	   @PostMapping("empUpdate")
	   public String updateEmpInfo(@ModelAttribute EmpVO empVO) {
	       empService.modifyEmpInfo(empVO);
	       return "redirect:/empInfo?employeeNo=" + empVO.getEmployeeNo();
	   }
	   
	   
	   
}