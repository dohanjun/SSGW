package com.yedam.app.group.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

   @Override
   public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
           Authentication authentication) throws IOException, ServletException {
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
