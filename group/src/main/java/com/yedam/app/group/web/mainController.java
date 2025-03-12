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
	 @GetMapping("/subscribe")
	    public String subscribePage(Model model) {
	        List<String> basic = Arrays.asList("기본1", "기본2");
	        List<String> select = Arrays.asList("항목1", "항목2", "항목3","항목1", "항목2", "항목3");
	        model.addAttribute("basic", basic);
	        model.addAttribute("select", select);
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
