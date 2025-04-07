package com.yedam.app.group.web;

import java.io.IOException;
import java.util.Base64;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.mapper.EmpMapper;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.EmpserchVO;

import lombok.RequiredArgsConstructor;

/** 
 *  그룹웨어 인사관리 등록,조회,상세조회,수정,조직도 등 Employees 사원관련 컨트롤러   
 *  @author EMP관리자 개발팀 김예찬
 *  @serial 2025-03-24
 *  <pre>
 *  <pre>
 *  	수정일자		수정자		수정내용
 * ---------------------------------------------------
 *  </pre>
 */
@Controller
@RequiredArgsConstructor
public class EmpController {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final EmpService empService;
	
	
	/**
	 * 등록 페이지로 이동
	 * @param vo : 등록
	 * @return 등록 페이지명
	 */
	
	// 등록 페이지
	@GetMapping("empinsert")
	public String empinsert(Model model) {
	    // 로그인한 사용자 정보에서 회사번호 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    Integer suberNo = loggedInUser.getSuberNo();

	    // 새로운 사원 등록용 VO 생성 후 회사번호 설정
	    EmpVO empVO = new EmpVO();
	    empVO.setSuberNo(suberNo); // 로그인한 사용자의 회사번호로 세팅
	    empVO.setEmployeeNo(empService.getNextEmployeeNo()); // 사원번호 자동 채번

	    model.addAttribute("empVO", empVO); // thymeleaf form과 바인딩할 객체
	    return "group/personnel/empinsert"; // 사원 등록 페이지로 이동
	}
	
    /**
     * 사원 등록 처리
     * @param empVO 등록할 사원 정보
     * @param file 프로필 이미지 파일
     * @return 사원관리 화면 리다이렉트
     */

	// 등록 처리
	@PostMapping("empinsert")
	public String empInsert(EmpVO empVO,
            @RequestParam("profileImageFile") MultipartFile file) {
			try {
			if (!file.isEmpty()) {
			// 이미지 등록한 경우: 업로드된 이미지 저장
			empVO.setProfileImageBLOB(file.getBytes());
			} else {
			// 이미지 미등록한 경우: 기본 이미지 BLOB으로 저장
			ClassPathResource resource = new ClassPathResource("static/img/default-profile.jpg");
			byte[] defaultImageBytes = resource.getInputStream().readAllBytes();
			empVO.setProfileImageBLOB(defaultImageBytes);
			}
			} catch (IOException e) {
			e.printStackTrace();
			}
			
			// 비밀번호 암호화
			String hash = bCryptPasswordEncoder.encode(empVO.getEmployeePw());
			empVO.setEmployeePw(hash);
			
			// 사원 정보 등록
			empService.createEmpInfo(empVO);
			
			return "redirect:empMgmt";
			}
	
    /**
     * 사원 목록 조회
     * @param empsVO 검색조건
     * @param model View 전달용 모델
     * @return 사원목록 화면
     */

	// 사원관리
	@GetMapping("/empMgmt")
	public String empMgmt(EmpserchVO empsVO, 
						Model model) {

		// 로그인한 사용자 정보 가져오기
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		Integer suberNo = loggedInUser.getSuberNo();

		empsVO.setSuberNo(suberNo);
		// 회사 번호(suberNo)를 기준으로 사원 조회
		List<EmpVO> emps = empService.findAllEmp(empsVO);
		int totalRecords = empService.countAllEmp(empsVO);
		int totalPages = (int) Math.ceil((double) totalRecords / empsVO.getSize());

		// 모델에 값 담기
		model.addAttribute("emps", emps);
		model.addAttribute("currentPage", empsVO.getPage());
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("loggedInUser", loggedInUser); // 필요시 사용	

		return "group/personnel/empMgmt";
	}
	
    /**
     * 사원 상세정보 조회
     * @param employeeNo 사원번호
     * @param model View 전달용 모델
     * @return 상세정보 화면
     */

	// 사원 상세조회
	@GetMapping("empInfo")
	public String empInfo(@RequestParam("employeeNo") int employeeNo, EmpserchVO empserchVO, Model model) {
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
		model.addAttribute("search", empserchVO);

		return "group/personnel/empInfo";
	}
	
    /**
     * 사원 상세정보 조회
     * @param employeeNo 사원번호
     * @param model View 전달용 모델
     * @return 상세정보 화면
     */

	// 사원 정보 수정 페이지 이동
	@GetMapping("empUpdate")
	public String empUpdate(@RequestParam("employeeNo") int employeeNo, 
			                @ModelAttribute("search") EmpserchVO search, Model model) {
		// 1) 로그인 사용자에서 suberNo 꺼내기
		EmpVO loginUser = empService.getLoggedInUserInfo();
		Integer suberNo = loginUser.getSuberNo();

		// 2) 사원번호 + suberNo로 EmpVO 생성
		EmpVO empVO = new EmpVO();
		empVO.setEmployeeNo(employeeNo);
		empVO.setSuberNo(suberNo);

		// 3) 사원 조회
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
		model.addAttribute("search", search);

		return "group/personnel/empUpdate"; // 뷰 반환
	}
	
    /**
     * 사원 정보 수정 처리
     * @param empVO 수정할 사원 정보
     * @param file 새 프로필 이미지 파일
     * @return 상세페이지 리다이렉트
     */

	// 사원 정보 수정
	@PostMapping("empUpdate")
	public String updateEmpInfo(@ModelAttribute EmpVO empVO,
			@RequestParam(value = "profileImageFile", required = false) MultipartFile file) {
		try {
			
			if (empVO.getEmployeePw() != null && !empVO.getEmployeePw().isEmpty()){
				
				String hash = bCryptPasswordEncoder.encode(empVO.getEmployeePw());
				empVO.setEmployeePw(hash);
			}
			 
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
	
    /**
     * 비밀번호 초기화 처리 (Ajax)
     * @param employeeNo 초기화 대상 사원번호
     * @return 메시지 문자열
     */

	// 비밀번호 초기화 API
	@PostMapping("/api/resetPassword")
	@ResponseBody
	public String resetPassword(@RequestParam int employeeNo) {
	    String defaultPw = "123456"; // 기본 비번 고정
	    empService.resetPassword(employeeNo, defaultPw); //  매개변수 추가
	    return "비밀번호가 123456으로 초기화되었습니다.";
	}
	
    /**
     * 조직도 화면 이동
     * @return View
     */

    // 조직도 화면 이동
    @GetMapping("/orgChart")
    public String orgChartPage() {
        return "group/personnel/orgChart";
    }
    
    /**
     * 사원 ID 중복 체크 (Ajax)
     * @param employeeId 입력 ID
     * @return duplicate / available
     */
    
    // 아이디 중복 체크 (Ajax 호출용)
    @GetMapping("/checkEmployeeId")
    @ResponseBody
    public String checkEmployeeId(@RequestParam String employeeId) {
        boolean isDuplicate = empService.isEmployeeIdDuplicate(employeeId);
        return isDuplicate ? "duplicate" : "available";
    }

}
