package com.yedam.app.group.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {
	 @GetMapping("/main")
	    public String mainPage() {
	        return "group/mainPage";
	    }
	 @GetMapping("/subscribe")
	    public String subscribePage() {
	        return "externalPages/subscribePage";
	    }
	 @GetMapping("/manual")
	    public String manualPage() {
	        return "externalPages/manualPage";
	    }
	 @GetMapping("/login")
	    public String loginPage() {
	        return "externalPages/loginPage";
	    }
}
