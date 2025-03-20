package com.yedam.app.group.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

@Controller
public class MyPageController {

    private final EmpService empService;

    public MyPageController(EmpService empService) {
        this.empService = empService;
    }

    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        // ✅ 현재 로그인한 사용자의 정보 가져오기
        EmpVO loggedInUser = empService.getLoggedInUserInfo();  

        // ✅ Thymeleaf에서 사용할 수 있도록 model에 추가
        model.addAttribute("emp", loggedInUser);

        return "group/workPage/mypage";
    }
}
