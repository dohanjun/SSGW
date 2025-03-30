package com.yedam.app.group.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomAuthenticationSuccessHandler successHandler;

	public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
		this.successHandler = successHandler;
	}

	// 암호화 빈 추가
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/login", "/subscribe", "/manual", "/css/**", "/js/**", "/img/**",
						"/checkDuplicateId")
				.permitAll()
				.requestMatchers("/aprv/modify", "/aprv/upload", "/saveSubDetail", "/saveSuber", "/savePayment",
						"/savePaymentDetails", "/saveUser", "/insertBoardPost", "/selectBoardPost", "/updateBoardPost")
				.permitAll().requestMatchers("/insertPost", "/basket/**", "/api/**", "/alerts/**", "/insertAlarm")
				.permitAll()
				// 관리자만 접근 가능
				.requestMatchers("/module", "/insertModule", "/updateModule", "/deleteModule/*", "/updateModuleBasic/*",
						"/updateModuleActive/*", "/qna", "/fixed")
				.hasAuthority("ROLE_MANAGER")
				// 로그인시 접근 가능
				.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").successHandler(successHandler)
						.permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout")
						.invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll())
				.csrf(csrf -> csrf.ignoringRequestMatchers("/logout", "/savePayment", "/insertModule", "/updateModule",
						"/deleteModule/*", "/updateModuleBasic/*", "/updateModuleActive/*", "/qna", "/aprv/**",
						"/insertModule", "/saveForm", "/schedule/**", "/qna", "/fixed", "/saveSubDetail",
						"/saveSubDetail", "/saveSuber", "/savePaymentDetails", "/saveUser", "/insertBoardPost",
						"/selectBoardPost", "/updateBoardPost", "/basket/**", "/api/**", "/alerts/**", "/insertAlarm",
						"/bookUpdate"));
		return http.build();
	}

	// 비밀번호 암호화
	@Bean
	public JdbcUserDetailsManager userDetailsService(DataSource dataSource) {
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

		manager.setUsersByUsernameQuery("SELECT username, password, enabled FROM ( "
				+ "  SELECT MANAGER_ID AS username, MANAGER_PW AS password, 1 AS enabled FROM MANAGER_LOGIN "
				+ "  UNION ALL "
				+ "  SELECT EMPLOYEE_ID AS username, EMPLOYEE_PW AS password, 1 AS enabled FROM EMPLOYEES WHERE RANK_ID = 7"
				+ "  UNION ALL "
				+ "  SELECT EMPLOYEE_ID AS username, EMPLOYEE_PW AS password, 1 AS enabled FROM EMPLOYEES "
				+ ") WHERE username = ?");

		manager.setAuthoritiesByUsernameQuery("SELECT username, authority FROM ( "
				+ "  SELECT MANAGER_ID AS username, 'ROLE_MANAGER' AS authority FROM MANAGER_LOGIN " + "  UNION ALL "
				+ "  SELECT EMPLOYEE_ID AS username, 'ROLE_MANAGERUSER' AS authority FROM EMPLOYEES WHERE RANK_ID = 7"
				+ "  UNION ALL " + "  SELECT EMPLOYEE_ID AS username, 'ROLE_USER' AS authority FROM EMPLOYEES "
				+ ") WHERE username = ?");
		return manager;

	}
}
