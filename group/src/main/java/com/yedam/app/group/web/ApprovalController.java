package com.yedam.app.group.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.service.ApprovalService;
import com.yedam.app.group.service.ApprovalVO;

@Controller
public class ApprovalController {
	
	private final ApprovalService approvalService;
	
	public ApprovalController(ApprovalService approvalService) {
		this.approvalService = approvalService;
	}
	
	@GetMapping("aprv")
	public String aprvList() {
		return "group/approval/approval";
	}
	
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
	        String uploadDir = "C:/uploads/";  // 업로드할 경로 (변경 가능)
	        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	        Path filePath = Paths.get(uploadDir + fileName);

	        // 파일 저장
	        Files.createDirectories(filePath.getParent());
	        Files.write(filePath, file.getBytes());

	        // DB 저장을 위해 ApprovalVO 생성
	        ApprovalVO aprvVO = new ApprovalVO();
	        aprvVO.setStampImgPath(filePath.toString());
	        aprvVO.setEmployeeNo(12);  // 실제 사용자 ID 적용 필요

	        approvalService.createStamp(aprvVO);

	        response.put("success", true);
	        response.put("message", "파일이 업로드되었습니다.");
	        return ResponseEntity.ok(response);
	    } catch (IOException e) {
	        response.put("success", false);
	        response.put("message", "파일 저장 중 오류 발생: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	
//	@GetMapping("schedule")
//	public String scheduleList() {
//		return "group/schedule/schedule";
//	}
//	
//	// 전자결재 대기함
//	@GetMapping("aprv")
//    public List<ApprovalVO> approvalList(){
//        return approvalService.findAllApproval();
//    }
//	
//	
//	
//	@GetMapping("approvalRequest")
//    public String edmsRequest() {
//        return "group/approval/approval_request";
//    }
//	
//	@GetMapping("approvalProgress")
//    public String edmsProgress() {
//        return "group/approval/approval_progress";
//    }
//	
//	@GetMapping("approvalComplete")
//    public String edmsComplete() {
//        return "group/approval/approval_complete";
//    }
//	
//	@GetMapping("approvalReturn")
//    public String edmsReturn() {
//        return "group/approval/approval_return";
//    }
//	
//	@GetMapping("approvalReference")
//    public String edmsReference() {
//        return "group/approval/approval_reference";
//    }
//	
//	@GetMapping("approvalWriting")
//    public String edmsWriting() {
//        return "group/approval/approval_writing";
//    }
//	
//	@GetMapping("test11")
//    public String test222() {
//        return "etc/register";
//    }
}
