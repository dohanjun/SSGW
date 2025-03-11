package com.yedam.app.group.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class workController {
	 @GetMapping("/charts")
	    public String mainPage() {
	        return "group/workPage/charts";  // mainPage.html을 반환
	    }
}
