package com.yedam.app.approval.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.yedam.app.approval.service.ApprovalService;
import com.yedam.app.approval.service.ApprovalVO;

@Controller
public class ApprovalController {
	
//	private final ApprovalService approvalService;
//	
//	@Autowired
//	public ApprovalController(ApprovalService approvalService) {
//		this.approvalService = approvalService;
//	}
	
	@GetMapping("aprv")
	public String aprvList() {
		return "group/approval/approval";
	}
	
//	@PostMapping("aprv")
//	public String empInsertProcess(ApprovalVO aprvVO) {
//		int eid = approvalService.createStamp(aprvVO);
//		String url = null;
//		if(eid > -1) {
//			
//		}else {
//			
//		}
//		return url;
//	}
	
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
