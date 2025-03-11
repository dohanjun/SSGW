package com.yedam.app.group.edms.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EdmsController {
	
	@GetMapping("edms")
    public String edmsPage() {
        return "group/edms/edms";
    }
	
	@GetMapping("edmsRequest")
    public String edmsRequest() {
        return "group/edms/edms_request";
    }
	
	@GetMapping("edmsProgress")
    public String edmsProgress() {
        return "group/edms/edms_progress";
    }
	
	@GetMapping("edmsComplete")
    public String edmsComplete() {
        return "group/edms/edms_complete";
    }
	
	@GetMapping("edmsReturn")
    public String edmsReturn() {
        return "group/edms/edms_return";
    }
	
	@GetMapping("edmsReference")
    public String edmsReference() {
        return "group/edms/edms_reference";
    }
	
	@GetMapping("edmsWriting")
    public String edmsWriting() {
        return "group/edms/edms_writing";
    }
	
	@GetMapping("form")
    public String test222() {
        return "group/edms/form";
    }
}
