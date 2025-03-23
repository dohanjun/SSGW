package com.yedam.app.group.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yedam.app.group.service.BasketService;
import com.yedam.app.group.service.BasketVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

@Controller
@RequestMapping("/basket")
public class BasketController {
	private final BasketService basketService;
	private final EmpService empService;

	public BasketController(BasketService basketService, EmpService empService) {
		this.basketService = basketService;
		this.empService = empService;
	}

	@PostMapping("/moveToBasket")
	public String moveToBasket(@RequestParam(value = "writingIds", required = false) List<Long> writingIds,
			@RequestParam("repositoryType") String repositoryType) {

		if (writingIds == null || writingIds.isEmpty()) {
			return switch (repositoryType) {
			case "전체" -> "redirect:/totalRepository";
			case "부서" -> "redirect:/departmentRepository";
			case "개인" -> "redirect:/individualRepository";
			default -> "redirect:/";
			};
		}

		basketService.moveToBasket(writingIds);

		// 어떤 자료실에서 왔는지에 따라 다시 리다이렉트
		return switch (repositoryType) {
		case "전체" -> "redirect:/totalRepository";
		case "부서" -> "redirect:/departmentRepository";
		case "개인" -> "redirect:/individualRepository";
		default -> "redirect:/"; // fallback
		};
	}

	// 휴지통 목록 조회
	@GetMapping("")
	public String viewBasket(@RequestParam(value = "repositoryType", required = false) String repositoryType,
			Model model) {

		// 로그인한 사용자 정보 조회
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		if (loggedInUser == null) {
			throw new IllegalStateException("로그인한 사용자 정보를 찾을 수 없습니다.");
		}

		List<BasketVO> basketList;

		if (repositoryType == null || repositoryType.isEmpty()) {
			// repositoryType 없으면 전체 목록 (회사 기준 전체 휴지통)
			basketList = basketService.getAllBasketPostsByEmp(loggedInUser);
		} else {
			// repositoryType 있을 경우 필터링된 휴지통 목록 조회
			basketList = basketService.getBasketPostsByType(loggedInUser, repositoryType);
		}

		model.addAttribute("basketList", basketList);
		model.addAttribute("repositoryType", repositoryType); // 선택값 유지용
		return "group/repository/basket";
	}

	// 선택 게시글 복원
	@PostMapping("/restore")
	public String restoreSelected(@RequestParam("writingIds") List<Long> writingIds) {
		basketService.restoreSelectedPosts(writingIds);
		return "redirect:/basket";
	}

	// 선택 게시글 완전 삭제
	@PostMapping("/delete")
	public String deleteSelected(@RequestParam("writingIds") List<Long> writingIds) {
		basketService.permanentlyDeletePosts(writingIds);
		return "redirect:/basket";
	}
}
