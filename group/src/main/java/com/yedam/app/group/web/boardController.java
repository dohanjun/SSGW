package com.yedam.app.group.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class boardController {
	
	@GetMapping("/noticeBoard")
    public String noticeBoard() {
        return "group/board/noticeBoard";
    }
	
	@GetMapping("/departmentBoard")
    public String departmentBoard() {
        return "group/board/departmentBoard";
    }
	
	@GetMapping("/freeBoard")
    public String freeBoard() {
        return "group/board/freeBoard";
    }
	
	@GetMapping("/detailBoard")
    public String detailBoard() {
        return "group/board/detailBoard";
    }
	
	@GetMapping("/insertBoard")
    public String insertBoard() {
        return "group/board/insertBoard";
    }
	
	
}
