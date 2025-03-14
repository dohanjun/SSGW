package com.yedam.app.group.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {
	 @GetMapping("main")
	    public String mainPage() {
	        return "group/mainPage";  
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
