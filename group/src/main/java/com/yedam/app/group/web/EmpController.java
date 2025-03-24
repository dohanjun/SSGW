package com.yedam.app.group.web;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.mapper.EmpMapper;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor // 리콰이어알규먼트 쓰고 밑에 final
public class EmpController {
	
	private final EmpService empService;
	private final EmpMapper empMapper;
	
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
	   @GetMapping("/empMgmt")
	   public String empMgmt(
	       @RequestParam(value = "page", defaultValue = "1") int page,
	       @RequestParam(value = "size", defaultValue = "12") int size,
	       @RequestParam(value = "category", required = false, defaultValue = "all") String category,
	       @RequestParam(value = "keyword", required = false) String keyword,
	       Model model) {

	       //  로그인한 사용자 정보 가져오기
	       EmpVO loggedInUser = empService.getLoggedInUserInfo();
	       Integer suberNo = loggedInUser.getSuberNo();

	       //  회사 번호(suberNo)를 기준으로 사원 조회
	       List<EmpVO> emps = empService.findAllEmp(page, size, category, keyword, suberNo);
	       int totalRecords = empService.countAllEmp(category, keyword, suberNo);
	       int totalPages = (int) Math.ceil((double) totalRecords / size);

	       //  모델에 값 담기
	       model.addAttribute("emps", emps);
	       model.addAttribute("currentPage", page);
	       model.addAttribute("totalPages", totalPages);
	       model.addAttribute("category", category);
	       model.addAttribute("keyword", keyword);
	       model.addAttribute("loggedInUser", loggedInUser); // 필요시 사용

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
		    //  1) 로그인 사용자에서 suberNo 꺼내기
		    EmpVO loginUser = empService.getLoggedInUserInfo();
		    Integer suberNo = loginUser.getSuberNo();

		    //  2) 사원번호 + suberNo로 EmpVO 생성
		    EmpVO empVO = new EmpVO();
		    empVO.setEmployeeNo(employeeNo);
		    empVO.setSuberNo(suberNo);

		    //  3) 사원 조회
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
	    public String updateEmpInfo(@ModelAttribute EmpVO empVO, @RequestParam(value = "profileImageFile", required = false) MultipartFile file) {
	        try {
	            // 1) 프로필 이미지 업로드 처리
	            if (file != null && !file.isEmpty()) {
	                empVO.setProfileImageBLOB(file.getBytes()); // 업로드된 파일을 BLOB으로 변환하여 저장
	            } else {
	                // 기존 이미지 유지 (DB에서 현재 이미지 가져오기)
	                EmpVO existingEmp = empService.findempInfo(empVO);
	                if (existingEmp != null) {
	                    empVO.setProfileImageBLOB(existingEmp.getProfileImageBLOB());
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        // 로그인한 사용자 회사번호
	        EmpVO loginUser = empService.getLoggedInUserInfo();
	        empVO.setSuberNo(loginUser.getSuberNo());
	        
	        // 퇴사에서 재직 되면 퇴사일 지움
	        if ("N".equals(empVO.getResignationStatus())) {
	            empVO.setExitDate(null);
	        }

	        // 2) 사원 정보 수정
	        empService.modifyEmpInfo(empVO);

	        // 3) 수정 후 사원 상세 페이지로 이동
	        return "redirect:/empInfo?employeeNo=" + empVO.getEmployeeNo();
	    }
	    
	    
	    // 비밀번호 초기화 API
	    @PostMapping("/resetPassword")
	    public String resetPassword(@RequestParam int employeeNo) {
	        empService.resetPassword(employeeNo);
	        return "비밀번호가 초기화되었습니다.";
	    }
	    
	    // 조직도
		@GetMapping("orgChart")
		public String orgChart() {
			return "group/personnel/orgChart";
		}
	   
}