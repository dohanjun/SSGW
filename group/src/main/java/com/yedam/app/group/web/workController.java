package com.yedam.app.group.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class workController {
	 @GetMapping("/charts")
	    public String mainPage() {
	        return "group/workPage/chartsManager";  // mainPage.html을 반환
	    }
	 @GetMapping("/table")
	    public String tables() {
	        return "group/workPage/tables";  // mainPage.html을 반환
	    }
	 @GetMapping("/blank")
	    public String blank() {
	        return "group/workPage/blank";  // mainPage.html을 반환
	    }
}
