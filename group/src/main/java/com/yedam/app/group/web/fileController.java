package com.yedam.app.group.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class fileController {
	@GetMapping("/totalfile")
    public String totalfile() {
        return "group/file/totalfile";
    }
	
	@GetMapping("/departmentfile")
    public String departmentfile() {
        return "group/file/departmentfile";
    }
	
	@GetMapping("/individualfile")
    public String individualfile() {
        return "group/file/individualfile";
    }
	
	@GetMapping("/detailfile")
    public String detailfile() {
        return "group/file/detailfile";
    }
	
	@GetMapping("/repositoryInsert")
    public String repositoryInsert() {
        return "group/file/repositoryInsert";
    }
	
	@GetMapping("/basket")
    public String basket() {
        return "group/file/basket";
    }
	
	@GetMapping("/detailBasket")
    public String detailBasket() {
        return "group/file/detailBasket";
    }
	
}
