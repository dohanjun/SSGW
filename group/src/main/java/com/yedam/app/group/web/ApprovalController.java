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
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.service.ApprovalFormVO;
import com.yedam.app.group.service.ApprovalService;
import com.yedam.app.group.service.ApprovalVO;

import lombok.Data;

@Controller
@Data
public class ApprovalController {
	
	private final ApprovalService approvalService;
	
//	public ApprovalController(ApprovalService approvalService) {
//		this.approvalService = approvalService;
//	}
	
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
	    
	    model.addAttribute("aprv", findVO);

	    return "group/approval/approval"; // 
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
	
	@PostMapping("aprv/modify")
	public ResponseEntity<Map<String, Object>> modifyStamp(@RequestParam("file") MultipartFile file){
		Map<String, Object> response = new HashMap<>();
		
		return null;
	}
	
	
	
	
	@GetMapping("write")
	public String write() {
		return "group/approval/write1";
	}
	
	@PostMapping("/saveForm")
	public String saveForm(ApprovalFormVO aprvformVO) { 
	    int formId = approvalService.createForm(aprvformVO);
	    String url = null;

	    if (formId > 0) {
	        url = "redirect:/aprv";  // 저장 후 결재 대기함 페이지로 리디렉션
	    } else {
	        url = "redirect:/errorPage";  // 저장 실패 시 에러 페이지로 리디렉션
	    }

	    return url;
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
