package com.yedam.app.group.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
        String currentIp = request.getParameter("clientIp");

        // 권한 확인 및 로그인 정보 저장
        String query = "SELECT * FROM ( " +
            "SELECT MANAGER_ID AS username, MANAGER_PW AS password,'ROLE_MANAGER' AS role FROM MANAGER_LOGIN " +
            "UNION ALL " +
            "SELECT EMPLOYEE_ID AS username, EMPLOYEE_PW AS password, 'ROLE_MANAGERUSER' AS role FROM EMPLOYEES WHERE RANK_ID = 7 and RESIGNATION_STATUS ='N'" +
            "UNION ALL " +
            "SELECT EMPLOYEE_ID AS username, EMPLOYEE_PW AS password, 'ROLE_USER' AS role FROM EMPLOYEES WHERE RANK_ID != 7 and RESIGNATION_STATUS ='N'" +
            ") WHERE username = ?";

        Map<String, Object> userInfo = jdbcTemplate.queryForMap(query, username);
        session.setAttribute("loginUser", userInfo);
        
        try {
            // ROLE_MANAGER면 IP 검사 생략
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
                response.sendRedirect("/");
                return;
            }



            // IP 정보 조회
            String companyInfoQuery = "SELECT s.first_ip, s.second_ip, e.temp_ip, s.suber_no " +
                    "FROM suber s JOIN employees e ON s.suber_no = e.suber_no " +
                    "WHERE e.employee_id = ?";
            Map<String, Object> ipInfo = jdbcTemplate.queryForMap(companyInfoQuery, username);
            String firstIp = (String) ipInfo.get("first_ip");
            String secondIp = (String) ipInfo.get("second_ip");
            String tempIp = (String) ipInfo.get("temp_ip");

            // 1차, 2차 IP 검사
            if (!currentIp.equals(firstIp) && !currentIp.equals(secondIp)) {
                if (tempIp == null || tempIp.isEmpty()) {
                    // tempIp가 없으면 등록 유도
                    response.sendRedirect("/login?ipRegister=1&ip=" + currentIp + "&employeeId=" + username);
                    return;
                } else if (!currentIp.equals(tempIp)) {
                    // tempIp도 다르면 경고 후 로그인 차단
                    response.sendRedirect("/login?ipAlert=1&ip=" + currentIp + "&employeeId=" + username);
                    return;
                }
                // tempIp와 같으면 통과
            }

            // 나머지 사용자 정보 세션에 저장
            String query2 = "SELECT s.* FROM suber s JOIN employees e ON s.suber_no = e.suber_no WHERE e.EMPLOYEE_ID = ?";
            Map<String, Object> userDepInfo = jdbcTemplate.queryForMap(query2, username);
            session.setAttribute("loginUserDepInfo", userDepInfo);

            String rightsQuery = "SELECT RIGHTS_LEVEL FROM EMPLOYEES E JOIN RIGHTS R ON E.RIGHTS_ID = R.RIGHTS_ID WHERE E.EMPLOYEE_ID = ?";
            Integer rightsLevel = jdbcTemplate.queryForObject(rightsQuery, Integer.class, username);
            session.setAttribute("rightsLevel", rightsLevel);

            // 최종 통과 시 메인으로 이동
            response.sendRedirect("/main");

        } catch (Exception e) {
            response.sendRedirect("/login?error=ipcheck");
        }
    }
}
