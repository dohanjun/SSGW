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

    // 기본 사용자 정보 조회
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
                "SELECT EMPLOYEE_ID AS username, EMPLOYEE_PW AS password, 'ROLE_USER' AS role FROM EMPLOYEES WHERE RESIGNATION_STATUS ='N'" +
                ") WHERE username = ?";

        try {
            Map<String, Object> userInfo = jdbcTemplate.queryForMap(query, username);
            session.setAttribute("loginUser", userInfo);
            
         // EMPLOYEES 테이블에서 RIGHTS_LEVEL 가져오기
            String rightsQuery = "SELECT RIGHTS_LEVEL FROM EMPLOYEES E JOIN RIGHTS R ON E.RIGHTS_ID = R.RIGHTS_ID WHERE E.EMPLOYEE_ID = ?";
            Integer rightsLevel = jdbcTemplate.queryForObject(rightsQuery, Integer.class, username);
            session.setAttribute("rightsLevel", rightsLevel); // 세션에 저장
            
        } catch (Exception e) {
            System.out.println("사용자 정보를 가져오는 중 오류 발생: " + e.getMessage());
        }
        
        // 권한에 따라 리다이렉트
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
