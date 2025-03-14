package com.yedam.app.group.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.yedam.app.group.service.ModuleService;
import com.yedam.app.group.service.ModuleVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ModuleController {
	
	private final ModuleService moduleService;
	
	 @GetMapping("/subscribe")
	    public String subscribePage(Model model) {
	        List<ModuleVO> modules = moduleService.getAllModules();
	        model.addAttribute("modules", modules);
	        return "externalPages/subscribePage";
	    }
}
