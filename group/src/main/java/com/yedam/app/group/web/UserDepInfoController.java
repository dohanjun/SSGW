package com.yedam.app.group.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@ControllerAdvice
public class UserDepInfoController {

    @ModelAttribute("userDepInfo")
    public Map<String, Object> addUserDepInfo(HttpSession session) {
        return (Map<String, Object>) session.getAttribute("loginUserDepInfo");
    }
}
