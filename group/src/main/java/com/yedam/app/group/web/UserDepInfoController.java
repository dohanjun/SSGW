package com.yedam.app.group.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@ControllerAdvice
public class UserDepInfoController {

    @Autowired
    private EmpService empService;
    
    @ModelAttribute("userDepInfo")
    public Map<String, Object> addUserDepInfo(HttpSession session) {
        return (Map<String, Object>) session.getAttribute("loginUserDepInfo");
    }
}
