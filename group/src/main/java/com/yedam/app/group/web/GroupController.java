package com.yedam.app.group.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.yedam.app.group.mapper.GroupMapper;
import com.yedam.app.group.service.GroupVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class GroupController {
	
	private final GroupMapper groupMapper;
	
	@GetMapping("/test")
	public String empList(Model model) {
		List<GroupVO> list = groupMapper.selectAllList();
		model.addAttribute("list", list);
		return "test";
	}
}
