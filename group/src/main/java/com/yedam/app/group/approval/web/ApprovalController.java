package com.yedam.app.group.approval.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApprovalController {
	
	@GetMapping("approval")
    public String edmsPage() {
        return "group/approval/approval";
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
	
	@GetMapping("approvalWriting")
    public String edmsWriting() {
        return "group/approval/approval_writing";
    }
	
	@GetMapping("test11")
    public String test222() {
        return "etc/register";
    }
}
