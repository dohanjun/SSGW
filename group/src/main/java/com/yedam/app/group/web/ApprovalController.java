package com.yedam.app.group.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.service.ApprovalFormVO;
import com.yedam.app.group.service.ApprovalService;
import com.yedam.app.group.service.ApprovalVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

import lombok.Data;

@Controller
@Data
public class ApprovalController {

	private final ApprovalService approvalService;
	private final EmpService empService;
	
	// 문서함조회(대기, 진행, 완료, 반려, 참조)
	@GetMapping("aprv/list")
	public String aprvList(ApprovalVO aprvVO, Model model) {
		
		if (aprvVO.getAprvStatus() == null || aprvVO.getAprvStatus().trim().isEmpty()) {
	        aprvVO.setAprvStatus("대기");
	    }
		
	    // 로그인한 사용자 정보 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();

	    if (loggedInUser != null) {
	        aprvVO.setEmployeeNo(loggedInUser.getEmployeeNo());  //  로그인한 사용자 정보 설정
	        aprvVO.setSuberNo(loggedInUser.getSuberNo()); // 로그인한 사용자 회사번호
	        
	        // 결재 상태별 문서 조회
	        List<ApprovalVO> list = approvalService.findAprvListByStatus(aprvVO);
	        model.addAttribute("aprvs", list);

	        // 활성화된 도장 정보 가져오기
	        ApprovalVO stamp = approvalService.getActiveStamp(aprvVO);
	        model.addAttribute("stampImgPath", (stamp != null) ? stamp.getStampImgPath() : "");
	    }

	    // 상태별 페이지 반환
	    if ("완료".equals(aprvVO.getAprvStatus())) {
	        return "group/approval/approval_complete";
	    } else if ("진행".equals(aprvVO.getAprvStatus())) {
	        return "group/approval/approval_progress";
	    } else if ("참조".equals(aprvVO.getAprvStatus())) {
	    	return "group/approval/approval_reference";
	    } else if ("반려".equals(aprvVO.getAprvStatus())) {
	    	return "group/approval/approval_return";
	    } else if ("임시저장".equals(aprvVO.getAprvStatus())) {
	    	return null;
	    } else {
	        return "group/approval/pending_document"; // 기본값 설정
	    }
	}
	
	// 임시저장, 결재요청
	@GetMapping("aprv/request")
	public String aprvRequestList(ApprovalVO aprvVO, Model model){
		return null;
	}
	
	// 기안문 작성 페이지
	@GetMapping("aprv/write")
	public String aprvWrite(ApprovalVO aprvVO, ApprovalFormVO aprvformVO, Model model) {
			return "group/approval/approval_writing";
	}
	
	// 테스트
	@GetMapping("test1234")
	public String test123() {
		return "group/approval/test12345";
	}
	
	
	// 양식목록
	@GetMapping("/aprvWriting/content/{id}")
	@ResponseBody
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



	
	
	// 양식 불러오기 (기본 양식 + 회사 전용 양식)
	@GetMapping("/aprvWriting/content")
	@ResponseBody
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



	// 도장등록
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
			String uploadDir = "D:/uploads/";
			String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
			Path filePath = Paths.get(uploadDir + fileName);

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

	// 도장 수정
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
			String uploadDir = "D:/uploads/";
			String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
			Path filePath = Paths.get(uploadDir + fileName);

			// 파일 저장
			Files.createDirectories(filePath.getParent());
			Files.write(filePath, file.getBytes());

			// DB에 저장할 경로 설정
			String fileDbPath = "/img/stamp/" + fileName;

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

	// 도장 비활성화
	@PostMapping("aprv/delete")
	public ResponseEntity<Map<String, Object>> deleteStamp() {
	    Map<String, Object> response = approvalService.removeStamp(new ApprovalVO());
	    return ResponseEntity.ok(response);
	}
	
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
	
	// 결재페이지로 이동
	@GetMapping("aprv/info")
	public String aprvInfo(@RequestParam("draftNo") Integer draftNo, Model model) {
		ApprovalVO aprvVO = new ApprovalVO();
		aprvVO.setDraftNo(draftNo);

		ApprovalVO findVO = approvalService.findAprvInfo(aprvVO);

		model.addAttribute("aprv", findVO);

		return "group/approval/approval"; //
	}
	
	
	// 결재 양식 생성 페이지 이동
	@GetMapping("write")
	public String write() {
		return "group/approval/write";
	}
	
	// 전자결재양식 저장 (기본양식아님)
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
	
	@GetMapping("schedule")
	public String scheduleList() {
		return "group/schedule/schedule";
	}
}
