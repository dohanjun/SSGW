package com.yedam.app.group.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth

                // 공개 URL: 로그인 페이지, 정적 자원 등
                .requestMatchers("/", "/login", "/subscribe", "/manual", "/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/module","/insertModule").hasAuthority("ROLE_MANAGER")
                // 그 외의 요청은 인증 필요
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(successHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/") 
                .invalidateHttpSession(true)   
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/logout", "/aprv/**","/insertModule","/saveForm"));
        return http.build();
    }

    @Bean
    public JdbcUserDetailsManager userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        
        manager.setUsersByUsernameQuery(
        	    "SELECT username, password, enabled FROM ( " +
        	    "  SELECT MANAGER_ID AS username, MANAGER_PW AS password, 1 AS enabled FROM MANAGER_LOGIN " +
        	    "  UNION ALL " +
        	    "  SELECT SUB_ID AS username, SUB_PW AS password, 1 AS enabled FROM SUBER " +
        	    "  UNION ALL " +
        	    "  SELECT EMPLOYEE_ID AS username, EMPLOYEE_PW AS password, 1 AS enabled FROM EMPLOYEES " +
        	    ") WHERE username = ?"
        	);

        
        manager.setAuthoritiesByUsernameQuery(
        	    "SELECT username, authority FROM ( " +
        	    "  SELECT MANAGER_ID AS username, 'ROLE_MANAGER' AS authority FROM MANAGER_LOGIN " +
        	    "  UNION ALL " +
        	    "  SELECT SUB_ID AS username, 'ROLE_USER' AS authority FROM SUBER " +
        	    "  UNION ALL " +
        	    "  SELECT EMPLOYEE_ID AS username, 'ROLE_USER' AS authority FROM EMPLOYEES " +
        	    ") WHERE username = ?"
        	);

        
        return manager;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
