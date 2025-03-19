package com.yedam.app.group.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RepositoryController {
	@GetMapping("/totalRepository")
    public String totalRepository() {
        return "group/repository/totalRepository";
    }
	
	@GetMapping("/departmentRepository")
    public String departmentRepository() {
        return "group/repository/departmentRepository";
    }
	
	@GetMapping("/individualRepository")
    public String individualRepository() {
        return "group/repository/individualRepository";
    }
	
	@GetMapping("/detailPost")
    public String detailPost() {
        return "group/repository/detailPost";
    }
	
	@GetMapping("/repositoryInsert")
    public String repositoryInsert() {
        return "group/repository/repositoryInsert";
    }
	
	@GetMapping("/basket")
    public String basket() {
        return "group/repository/basket";
    }
	
	@GetMapping("/detailBasket")
    public String detailBasket() {
        return "group/repository/detailBasket";
    }
	
}
