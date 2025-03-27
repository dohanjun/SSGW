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

        try {
            // 권한 먼저 체크
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
                response.sendRedirect("/");
                return;
            }

            // 권한이 일반 유저거나 매니저 유저라면 → IP 검사 진행
            String query = "SELECT * FROM ( " +
                "SELECT MANAGER_ID AS username, MANAGER_PW AS password,'ROLE_MANAGER' AS role FROM MANAGER_LOGIN " +
                "UNION ALL " +
                "SELECT EMPLOYEE_ID AS username, EMPLOYEE_PW AS password, 'ROLE_MANAGERUSER' AS role FROM EMPLOYEES WHERE RANK_ID = 7 and RESIGNATION_STATUS ='N'" +
                "UNION ALL " +
                "SELECT EMPLOYEE_ID AS username, EMPLOYEE_PW AS password, 'ROLE_USER' AS role FROM EMPLOYEES WHERE RANK_ID != 7 and RESIGNATION_STATUS ='N'" +
                ") WHERE username = ?";

            String companyInfoQuery = "SELECT s.first_ip, s.second_ip, e.temp_ip, s.suber_no " +
                    "FROM suber s JOIN employees e ON s.suber_no = e.suber_no " +
                    "WHERE e.employee_id = ?";

            Map<String, Object> userInfo = jdbcTemplate.queryForMap(query, username);
            session.setAttribute("loginUser", userInfo);

            Map<String, Object> ipInfo = jdbcTemplate.queryForMap(companyInfoQuery, username);
            String firstIp = (String) ipInfo.get("first_ip");
            String secondIp = (String) ipInfo.get("second_ip");
            String tempIp = (String) ipInfo.get("temp_ip");

            if (!currentIp.equals(firstIp) && !currentIp.equals(secondIp) && !currentIp.equals(tempIp)) {
                response.sendRedirect("/login?ipAlert=1&ip=" + currentIp + "&employeeId=" + username);
                return;
            }

            String query2 = "SELECT s.* FROM suber s JOIN employees e ON s.suber_no = e.suber_no WHERE e.EMPLOYEE_ID = ?";
            Map<String, Object> userDepInfo = jdbcTemplate.queryForMap(query2, username);
            session.setAttribute("loginUserDepInfo", userDepInfo);

            String rightsQuery = "SELECT RIGHTS_LEVEL FROM EMPLOYEES E JOIN RIGHTS R ON E.RIGHTS_ID = R.RIGHTS_ID WHERE E.EMPLOYEE_ID = ?";
            Integer rightsLevel = jdbcTemplate.queryForObject(rightsQuery, Integer.class, username);
            session.setAttribute("rightsLevel", rightsLevel);

            response.sendRedirect("/main");

        } catch (Exception e) {
            System.out.println("사용자 정보를 가져오는 중 오류 발생: " + e.getMessage());
            response.sendRedirect("/login?error=ipcheck");
        }
    }

}
