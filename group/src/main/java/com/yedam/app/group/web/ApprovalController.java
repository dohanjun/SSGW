package com.yedam.app.group.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.service.ApprovalFormVO;
import com.yedam.app.group.service.ApprovalService;
import com.yedam.app.group.service.ApprovalVO;
import com.yedam.app.group.service.AprvRoutesVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

import lombok.Data;



/** 전자결재 관리
 * @author 김상연
 * @since 2025-03-24
 * <pre>
 * <pre>
 * 수정일자     수정자    수정내용
 * ---------------------------
 * </pre>
 * </pre>
 */
@Controller
@Data
public class ApprovalController {

	private final ApprovalService approvalService;
	private final EmpService empService;
	
    @Value("${file.upload-dir}")  
    private String uploadDir;  // 현재 프로젝트 안에서 저장하는 폴더 설정
	
	/** aprv_routes의 사원번호사용(결재자)
	 * aprv_status로 문서함조회(대기, 진행, 완료, 반려)
	 * @param aprvVO
	 * @param model
	 * @return 문서조회페이지명
	 */
	@GetMapping("aprv/list")
	public String aprvList(ApprovalVO aprvVO, Model model) {

	    if (aprvVO.getAprvStatus() == null || aprvVO.getAprvStatus().trim().isEmpty()) {
	        aprvVO.setAprvStatus("대기");
	    }
	    
	    // 로그인한 사용자 정보 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();

	    if (loggedInUser != null) {
	        aprvVO.setEmployeeNo(loggedInUser.getEmployeeNo());  // 로그인한 사용자 정보 설정
	        aprvVO.setSuberNo(loggedInUser.getSuberNo()); // 로그인한 사용자 회사번호
	        
	        // 결재 상태별 문서 조회
	        List<ApprovalVO> list = approvalService.findAprvListByStatus(aprvVO);
	        model.addAttribute("aprvs", list);
	        
	        // 참조면 제외
	        list = list.stream()
	                   .filter(aprv -> !"참조".equals(aprv.getAprvRole())) // '참조'는 필터링
	                   .collect(Collectors.toList());

	        // 활성화된 도장 정보 가져오기
	        ApprovalVO stamp = approvalService.getActiveStamp(aprvVO);
	        model.addAttribute("stampImgPath", (stamp != null) ? stamp.getStampImgPath() : "");
	    }

	    // 만약 aprv_role이 '참조'이면 참조열람함으로 리디렉션
	    if ("참조".equals(aprvVO.getAprvRole())) {
	        return "group/approval/approval_reference";  // 참조열람함 페이지로 리디렉션
	    }

	    // 상태별 페이지 반환
	    if ("완료".equals(aprvVO.getAprvStatus())) {
	        return "group/approval/approval_complete";
	    } else if ("진행".equals(aprvVO.getAprvStatus())) {
	        return "group/approval/approval_progress";
	    } else if ("반려".equals(aprvVO.getAprvStatus())) {
	        return "group/approval/approval_return";
	    } else {
	        return "group/approval/pending_document"; // 기본값 설정
	    }
	}
	
	/**
	 * aprv_routes테이블에서 aprv_role이 '참조'인 사원만 볼 수 있는 페이지
	 * @param aprvVO
	 * @param model
	 * @return 참조열람함페이지
	 */
	@GetMapping("aprv/reference")
	public String aprvReferenceList(ApprovalVO aprvVO, Model model) {
	    // 로그인한 사용자 정보 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    if (loggedInUser != null) {
	        aprvVO.setEmployeeNo(loggedInUser.getEmployeeNo());
	        aprvVO.setSuberNo(loggedInUser.getSuberNo());

	        // '참조' 역할인 문서만 조회
	        aprvVO.setAprvRole("참조");  // 참조자 역할만
	        List<ApprovalVO> list = approvalService.findAprvListByRole(aprvVO); // 새로 만든 메서드 호출
	        model.addAttribute("aprvs", list);

	        // 활성화된 도장 정보 가져오기
	        ApprovalVO stamp = approvalService.getActiveStamp(aprvVO);
	        model.addAttribute("stampImgPath", (stamp != null) ? stamp.getStampImgPath() : "");
	    }
	    
	    return "group/approval/approval_reference";  // 참조열람함 페이지
	}


	
	/**
	 * 사원번호는 aprv_documents의 emplyoee_no(기안자)
	 * @param aprvVO
	 * @param model
	 * @return 결재요청함, 임시저장함
	 */
	// 결재요청함, 임시저장함
	@GetMapping("aprv/request")
	public String aprvRequestList(ApprovalVO aprvVO, Model model) {
	    // 로그인한 사용자 정보 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    
	    if (loggedInUser == null) {
	    	return "redirect:/";  // 로그인하지 않은 경우 홈 페이지로 리디렉션
	    }
	    	
        aprvVO.setEmployeeNo(loggedInUser.getEmployeeNo());  // 로그인한 사용자 정보 설정
        aprvVO.setSuberNo(loggedInUser.getSuberNo());        // 로그인한 사용자 회사번호
        
        // 임시저장 문서일 경우
        if ("임시".equals(aprvVO.getAprvStatus())) {
            List<ApprovalVO> list = approvalService.findAllList(aprvVO);  // 임시저장 문서 조회
            model.addAttribute("aprvs", list);
            return "group/approval/approval_save";  // 임시저장함 페이지로 리디렉션
        } else {
            List<ApprovalVO> list = approvalService.findAllList(aprvVO);  // 결재요청 문서 조회
            model.addAttribute("aprvs", list);
            return "group/approval/approval_request";  // 결재요청함 페이지로 리디렉션
        }
	}


	/**
	 * 기존에 저장된 기본양식, 회사전용 양식을 불러와서 기안문 작성
	 * @param aprvVO
	 * @param aprvformVO
	 * @param model
	 * @return 기안문작성페이지
	 */
	// 기안문 작성 페이지
	@GetMapping("aprv/write")
	public String aprvWrite(ApprovalVO aprvVO, ApprovalFormVO aprvformVO, Model model) {
	    // 임시저장된 문서가 있을 경우 해당 문서의 정보 가져오기
	    if (aprvVO.getDraftNo() != null) {
	        // 임시저장된 문서의 정보 가져오기
	        ApprovalVO savedDocument = approvalService.findAprvInfo(aprvVO);
	        
	        if (savedDocument != null) {
	        	//String rawHtml = StringEscapeUtils.unescapeHtml4(savedDocument.getContent());
	            //savedDocument.setContent(rawHtml);
	            model.addAttribute("aprvVO", savedDocument); // 문서 정보를 모델에 추가
	        }
	    }
	    return "group/approval/approval_writing"; // 기안문 작성 페이지로 이동
	}
	
	/**
	 * 기안문 작성페이지에서 등록 버튼 클릭시 aprv_status가 '대기'로 db에 저장, 임시저장시 aprv_status가 '임시'로 저장
	 * @param approvalVO
	 * @param routeVO
	 * @param status
	 * @return 기안문 상신 기능
	 */
	// 기안문 상신
	@PostMapping("/aprv/writing")
	public String submitApproval(@RequestBody ApprovalVO approvalVO) {
		
	    EmpVO loginUser = empService.getLoggedInUserInfo();
	    approvalVO.setEmployeeNo(loginUser.getEmployeeNo());
	    approvalVO.setSuberNo(loginUser.getSuberNo());
	    
	    // 문서 저장 (임시, 대기)
	    if ("임시".equals(approvalVO.getAprvStatus())) {
	        approvalVO.setAprvStatus("임시"); // 상태를 임시로 설정
	    } else {
	        approvalVO.setAprvStatus("대기"); // 결재 대기 상태로 설정
	    }
	    
	    // 문서 저장
	    approvalService.createAprvDocu(approvalVO);
	    int draftNo = approvalVO.getDraftNo();

	    // 결재자 저장
	     List<Integer> approvers = approvalVO.getApprovers();
	    for (int i = 0; i < approvers.size(); i++) {
	        AprvRoutesVO r = new AprvRoutesVO();
	        r.setDraftNo(draftNo);
	        r.setAprvOrder(String.valueOf(i + 1));
	        r.setAprvRole("결재");
	        r.setAprvStatus("대기");
	        r.setEmployeeNo(approvers.get(i));
	        approvalService.createAprvRout(r);
	    }

	    // 참조자 저장
			
	      List<Integer> reference = approvalVO.getReference();
	      
		  if (approvalVO.getReference() != null && !approvalVO.getReference().isEmpty()) {
		  for (int i = 0; i < reference.size(); i++) { 
		  AprvRoutesVO ref = new AprvRoutesVO(); 
		  ref.setDraftNo(draftNo); ref.setAprvOrder("0");
		  ref.setAprvRole("참조"); ref.setAprvStatus("대기");
		  ref.setEmployeeNo(reference.get(i));
		  approvalService.createAprvRout(ref); } 
		  }
		 

	    return "redirect:/aprv/list";
	}


	/**
	 * 
	 * 양식선택 모달창에서 선택한 양식의 content를 작성페이지 content 영역에 출력
	 * @param id
	 * @param type
	 * @param aprvVO
	 * @param aprvFormVO
	 * @return 양식목록불러오는기능
	 */
	@GetMapping("/aprvWriting/content/{id}")
	public ResponseEntity<Map<String, Object>> getFormContentById(
	        @PathVariable("id") int id,
	        @RequestParam("type") String type,
	        ApprovalVO aprvVO,
	        ApprovalFormVO aprvFormVO) {

	    Map<String, Object> response = new HashMap<>();

	    if ("basic".equalsIgnoreCase(type)) {
	        aprvVO.setBasicsFormId(id);
	        ApprovalVO basicForm = approvalService.findBasicsForm(aprvVO);

	        if (basicForm != null) {
	            response.put("content", basicForm.getContent());
	            response.put("formType", basicForm.getFormType());
	        } else {
	            response.put("error", "해당 기본 양식을 찾을 수 없습니다.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }

	    } else if ("company".equalsIgnoreCase(type)) {
	        aprvFormVO.setFormId(id);
	        EmpVO loginUser = empService.getLoggedInUserInfo();
	        aprvFormVO.setSuberNo(loginUser.getSuberNo()); // 보안상 회사번호 설정
	        ApprovalFormVO companyForm = approvalService.findAprvForm(aprvFormVO);

	        if (companyForm != null) {
	            response.put("content", companyForm.getContent());
	            response.put("formType", companyForm.getFormType());
	        } else {
	            response.put("error", "해당 회사 전용 양식을 찾을 수 없습니다.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }

	    } else {
	        response.put("error", "type 파라미터는 'basic' 또는 'company' 여야 합니다.");
	        return ResponseEntity.badRequest().body(response);
	    }

	    return ResponseEntity.ok(response);
	}
	
	/**
	 * 기안문 작성페이지 양식선택 버튼 클릭시 모달창에 양식 목록을 출력
	 * @param aprvVO
	 * @param aprvformVO
	 * @return 양식목록 모달창에 출력
	 */
	@GetMapping("/aprvWriting/content")
	public ResponseEntity<Map<String, Object>> getFormContent(ApprovalVO aprvVO, ApprovalFormVO aprvformVO) {
	    Map<String, Object> response = new HashMap<>();

	    // 기본 양식 가져오기
	    List<ApprovalVO> basicList = approvalService.findAllBasicsForm(aprvVO);
	    response.put("basicsForms", basicList); // 기본 양식 리스트 반환

	    // 로그인한 사용자 정보 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    aprvformVO.setSuberNo(loggedInUser.getSuberNo());

	    // 회사 전용 양식 가져오기
	    List<ApprovalFormVO> subList = approvalService.findAllAprvForm(aprvformVO);
	    response.put("aprvForms", subList); // 회사 전용 양식 리스트 반환

	    return ResponseEntity.ok(response); // JSON 형식으로 반환
	}


	/**
	 * 도장등록기능 수행 파일은 D:/uploads/ 에 저장
	 * @param file
	 * @return 도장등록모달창 - 도장등록기능
	 */
	@PostMapping("aprv/upload")
	public ResponseEntity<Map<String, Object>> uploadStamp(@RequestParam("file") MultipartFile file) {
		Map<String, Object> response = new HashMap<>();

		if (file.isEmpty()) {
			response.put("success", false);
			response.put("message", "파일이 없습니다.");
			return ResponseEntity.badRequest().body(response);
		}

		try {

			// 파일 저장 경로 설정  
			String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
			Path filePath = Paths.get(uploadDir + "/" + fileName);

			// 파일 저장
			Files.createDirectories(filePath.getParent());
			Files.write(filePath, file.getBytes());

			// DB에 저장할 경로 설정
			String fileDbPath = "/uploads/" + fileName;

			// ApprovalVO 객체 생성 후 데이터 저장
			ApprovalVO aprvVO = new ApprovalVO();
			aprvVO.setStampImgPath(fileDbPath);

			// DB에 저장
			int result = approvalService.createStamp(aprvVO);

			if (result > 0) {
				response.put("success", true);
				response.put("imageUrl", fileDbPath);
			} else {
				response.put("success", false);
				response.put("message", "DB 저장 실패");
			}
			return ResponseEntity.ok(response);

		} catch (IOException e) {
			response.put("success", false);
			response.put("message", "파일 저장 중 오류 발생");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	/**
	 * 업데이트 시 기존에 등록된 도장은 active = '0' 비활성화, 새롭게 수정된 도장은 active = '1' 활성화 
	 * @param file
	 * @return 도장등록모달창 - 도장수정기능
	 */
	@PostMapping("aprv/modify")
	public ResponseEntity<Map<String, Object>> modifyStamp(@RequestParam("file") MultipartFile file) {
		Map<String, Object> response = new HashMap<>();

		if (file.isEmpty()) {
			response.put("success", false);
			response.put("message", "파일이 없습니다.");
			return ResponseEntity.badRequest().body(response);
		}

		try {

			// 파일 저장 경로 설정
			String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
			Path filePath = Paths.get(uploadDir , fileName);

			// 파일 저장
			Files.createDirectories(filePath.getParent());
			Files.write(filePath, file.getBytes());

			// DB에 저장할 경로 설정
			String fileDbPath = "/uploads/" + fileName;

			// ApprovalVO 객체 생성 후 데이터 설정
			ApprovalVO aprvVO = new ApprovalVO();
			aprvVO.setStampImgPath(fileDbPath);

			// 도장 수정 서비스 호출
			Map<String, Object> result = approvalService.modifyStamp(aprvVO);

			if ((boolean) result.get("success")) {
				response.put("success", true);
				response.put("imageUrl", fileDbPath);
			} else {
				response.put("success", false);
				response.put("message", "도장 수정 실패");
			}

			return ResponseEntity.ok(response);

		} catch (IOException e) {
			response.put("success", false);
			response.put("message", "파일 저장 중 오류 발생");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	/**
	 * 도장등록 모달창 삭제 버튼 클릭시 기존 도장 active = '0'
	 * @return 도장등록모달창 - 도장삭제(도장비활성화)
	 */
	@PostMapping("aprv/delete")
	public ResponseEntity<Map<String, Object>> deleteStamp() {
	    Map<String, Object> response = approvalService.removeStamp(new ApprovalVO());
	    return ResponseEntity.ok(response);
	}
	
	/**
	 * 기존에 활성화 된 도장을 확인해서 활성화 된 도장이 있다면 등록기능 사용 불가, 수정,삭제만 가능
	 * @return 도장등록모달창
	 */
	// 활성화된 도장 조회
	@GetMapping("aprv/stamp")
	public ResponseEntity<Map<String, Object>> getActiveStamp() {
	    Map<String, Object> response = new HashMap<>();
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();

	    if (loggedInUser == null) {
	        response.put("success", false);
	        response.put("message", "로그인한 사용자 정보를 찾을 수 없습니다.");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }

	    // ApprovalVO 객체 생성 후 employeeNo 설정
	    ApprovalVO aprvVO = new ApprovalVO();
	    aprvVO.setEmployeeNo(loggedInUser.getEmployeeNo());

	    ApprovalVO stamp = approvalService.getActiveStamp(aprvVO);

	    if (stamp != null && stamp.getStampImgPath() != null) {
	        response.put("success", true);
	        response.put("stampImgPath", stamp.getStampImgPath());
	    } else {
	        response.put("success", false);
	        response.put("message", "활성화된 도장이 없습니다.");
	    }

	    return ResponseEntity.ok(response);
	}
	
	/**
	 * 대기함, 진행함에서 문서 li태그를 클릭시 이동하는 페이지
	 * @param draftNo
	 * @param model
	 * @return 결재페이지
	 */
	@GetMapping("aprv/info")
	public String aprvInfo(@RequestParam("draftNo") Integer draftNo, Model model) {
		ApprovalVO aprvVO = new ApprovalVO();
		aprvVO.setDraftNo(draftNo);

		ApprovalVO findVO = approvalService.findAprvInfo(aprvVO);

		model.addAttribute("aprv", findVO);
		
		// List<AprvRoutesVO> routList = approvalService.find !!!!!! 집에가서 하기 !!!!!!
		

		return "group/approval/approval"; //
	}
	
	/**
	 * CKeditor로 양식을 추가하는 페이지
	 * @return 회사전용양식추가페이지
	 */
	@GetMapping("write")
	public String write() {
		return "group/approval/write";
	}
	
	/**
	 * 
	 * @param aprvformVO
	 * @return 에디터에서 작성한 내용을 DB에 저장하는 기능
	 */
	@PostMapping("/saveForm")
	public String saveForm(ApprovalFormVO aprvformVO) {
		
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		
		aprvformVO.setSuberNo(loggedInUser.getSuberNo());
		
		int formId = approvalService.createForm(aprvformVO);
		
		String url = null;

		if (formId > 0) {
			url = "redirect:/aprv/list"; // 저장 후 결재 대기함 페이지로 리디렉션
		} else {
			url = "redirect:/errorPage"; // 저장 실패 시 에러 페이지로 리디렉션
		}

		return url;
	}

	@GetMapping("approvalRequest")
	public String edmsRequest() {
		return "group/approval/approval_request";
	}
	
	@GetMapping("schedulePage")
	public String scheduleList() {
		return "group/schedule/schedule";
	}
}
