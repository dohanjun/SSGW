package com.yedam.app.group.web;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.yedam.app.group.service.ModuleService;
import com.yedam.app.group.service.ModuleVO;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class mainController {
	
	private final ModuleService moduleService;
	
	@GetMapping("main")
	public String mainPage() {
		return "group/mainPage";
	}

	@GetMapping("/manual")
	public String manualPage() {
		return "externalPages/manualPage";
	}

	@GetMapping("/login")
	public String loginPage() {
		return "externalPages/loginPage";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/";
	}
	
	@GetMapping("/module")
	public String subscribePage(Model model) {
		List<ModuleVO> modules = moduleService.getAllModules();
		model.addAttribute("modules", modules);
		return "externalPages/modulePage";
	}
}
