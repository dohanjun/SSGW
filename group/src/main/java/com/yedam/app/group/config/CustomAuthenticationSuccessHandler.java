package com.yedam.app.group.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JdbcTemplate jdbcTemplate;

    public CustomAuthenticationSuccessHandler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String username = authentication.getName();

        String query = "SELECT * FROM ( " +
                "SELECT MANAGER_ID AS username, MANAGER_PW AS password,'ROLE_MANAGER' AS role FROM MANAGER_LOGIN " +
                "UNION ALL " +
                "SELECT SUB_ID AS username, SUB_PW AS password, 'ROLE_MANAGERUSER' AS role FROM SUBER " +
                "UNION ALL " +
                "SELECT EMPLOYEE_ID AS username, EMPLOYEE_PW AS password, 'ROLE_USER' AS role FROM EMPLOYEES " +
                ") WHERE username = ?";

        try {
            Map<String, Object> userInfo = jdbcTemplate.queryForMap(query, username);
            session.setAttribute("loginUser", userInfo);
        } catch (Exception e) {
            System.out.println("사용자 정보를 가져오는 중 오류 발생: " + e.getMessage());
        }
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
            response.sendRedirect("/");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")) ||
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGERUSER"))){
            response.sendRedirect("/main");
        } else {
            response.sendRedirect("/default");
        }
    }
}
