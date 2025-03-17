package com.yedam.app.group.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.service.ApprovalService;
import com.yedam.app.group.service.ApprovalVO;

@Controller
public class ApprovalController {
	
	private final ApprovalService approvalService;
	
	public ApprovalController(ApprovalService approvalService) {
		this.approvalService = approvalService;
	}
	
	// 결재대기함
	@GetMapping("aprv")
	public String aprvList(Model model) {
		List<ApprovalVO> list = approvalService.findAllList();
		
		model.addAttribute("aprvs", list);
		
		return "group/approval/pending_document";
	}
	
	// 결재대기함(검색)
	@GetMapping("aprv/search")
	public String searchApprovalList(
	    @RequestParam(required = false) String employeeName,
	    @RequestParam(required = false) String title,
	    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date draftDate,
	    Model model
	) {
	    ApprovalVO aprv = new ApprovalVO();
	    aprv.setEmployeeName(employeeName);
	    aprv.setTitle(title);
	    aprv.setDraftDate(draftDate);

	    List<ApprovalVO> searchResults = approvalService.searchApprovalList(aprv);
	    
	    if (searchResults == null || searchResults.isEmpty()) {
	        model.addAttribute("message", "검색 결과가 없습니다.");
	    } else {
	        model.addAttribute("aprvs", searchResults);
	    }
	    
	    return "group/approval/pending_document";
	}
	
	
	// 결재페이지로 이동
	@GetMapping("aprv/info")
	public String aprvInfo(@RequestParam("draftNo") Integer draftNo, Model model) {
	    ApprovalVO aprvVO = new ApprovalVO();
	    aprvVO.setDraftNo(draftNo);
	    
	    ApprovalVO findVO = approvalService.findAprvInfo(aprvVO);
	    
	    if (findVO == null) {
	        return "error/404"; // 문서가 없을 경우 404 페이지로 이동
	    }
	    
	    model.addAttribute("aprv", findVO);

	    // 수정: 결재자의 도장 이미지 가져오기
	    String stampImage = approvalService.getStampImage(findVO.getEmployeeNo());
	    model.addAttribute("stampImage", stampImage); // 도장 이미지 추가

	    return "group/approval/approval"; // 올바른 HTML 파일로 이동
	}

	
	// 도장등록
	@PostMapping("aprv/upload")
	public ResponseEntity<Map<String, Object>> uploadStamp(@RequestParam("file") MultipartFile file, 
                                                           @RequestParam("employeeNo") int employeeNo) {
	    Map<String, Object> response = new HashMap<>();
	    
	    if (file.isEmpty()) {
	        response.put("success", false);
	        response.put("message", "파일이 없습니다.");
	        return ResponseEntity.badRequest().body(response);
	    }

	    try {
	        // 파일 저장 경로 설정
	        String uploadDir = "D:/uploads/";  // 업로드할 경로 (변경 가능)
	        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	        // 객체 생성
	        Path filePath = Paths.get(uploadDir + fileName);

	        // 파일 저장
	        Files.createDirectories(filePath.getParent());
	        Files.write(filePath, file.getBytes());
	        
	        // 수정: DB에 도장 이미지 저장
	        approvalService.saveStampImage(employeeNo, "/uploads/" + fileName);

	        response.put("success", true);
	        response.put("imageUrl", "/uploads/" + fileName); // 클라이언트에서 사용 가능하도록 URL 반환
	        return ResponseEntity.ok(response);
	    } catch (IOException e) {
	        response.put("success", false);
	        response.put("message", "파일 저장 중 오류 발생: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	
	// 수정: 도장 이미지 조회 API 추가
	@GetMapping("/aprv/getStamp")
	@ResponseBody
	public Map<String, Object> getStamp(@RequestParam("employeeNo") Integer employeeNo) {
	    Map<String, Object> response = new HashMap<>();
	    
	    String stampImage = approvalService.getStampImage(employeeNo);
	    response.put("stampImage", (stampImage != null) ? stampImage : null);
	    
	    return response;
	}
	
	// 작성페이지 불러오기
	@GetMapping("aprvWriting")
	public String basicsForm(ApprovalVO aprvVO, Model model) {
		ApprovalVO findVO = approvalService.findBaicsForm(aprvVO);
		
		model.addAttribute("basics", findVO);
		
		return "group/approval/approval_writing";
	}
	
	
	@GetMapping("aprvWriting/content")
    @ResponseBody
    public String getFormContent(@RequestParam String formType, Model model) {
        ApprovalVO aprvVO = new ApprovalVO();
        aprvVO.setFormType(formType);  // formType을 VO에 설정

        // 양식 데이터 가져오기
        ApprovalVO findVO = approvalService.findBaicsForm(aprvVO);

        return findVO.getContent();  // 양식의 내용을 반환
    }
	
	
	@GetMapping("schedule")
	public String scheduleList() {
		return "group/schedule/schedule";
	}
	
	@GetMapping("approvalRequest")
    public String edmsRequest() {
        return "group/approval/approval_request";
    }
	
	@GetMapping("approvalProgress")
    public String edmsProgress() {
        return "group/approval/approval_progress";
    }
	
	@GetMapping("approvalComplete")
    public String edmsComplete() {
        return "group/approval/approval_complete";
    }
	
	@GetMapping("approvalReturn")
    public String edmsReturn() {
        return "group/approval/approval_return";
    }
	
	@GetMapping("approvalReference")
    public String edmsReference() {
        return "group/approval/approval_reference";
    }
}
