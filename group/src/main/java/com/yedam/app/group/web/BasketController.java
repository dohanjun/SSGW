package com.yedam.app.group.web;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

		basketService.moveToBasket(writingIds, repositoryType);

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
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "keyword", required = false) String keyword,
                              Model model) {

        EmpVO loggedInUser = empService.getLoggedInUserInfo();
        if (loggedInUser == null) {
            throw new IllegalStateException("로그인한 사용자 정보를 찾을 수 없습니다.");
        }

        int pageSize = 10;
        int pageGroup = 10;
        page = Math.max(page, 1);
        int offset = (page - 1) * pageSize;
        int limit = pageSize;

        List<BasketVO> basketList;
        int totalCount = 0;

        if ("전체".equals(repositoryType)) {
            boolean isAdmin = (loggedInUser.getRightsId() != null && loggedInUser.getRightsId() == 3)
                            || (loggedInUser.getRightsLevel() != null && loggedInUser.getRightsLevel() == 5);

            if (isAdmin) {
                totalCount = basketService.countAllBasketPosts(keyword, loggedInUser.getSuberNo());
                basketList = basketService.getAllBasketPostsPaged(keyword, loggedInUser.getSuberNo(), offset, limit);
            } else {
                totalCount = basketService.countOwnTotalBasketPosts(keyword, loggedInUser.getEmployeeNo());
                basketList = basketService.getOwnTotalBasketPostsPaged(keyword, loggedInUser.getEmployeeNo(), offset, limit);
            }

        } else if ("부서".equals(repositoryType)) {
            totalCount = basketService.countDepartmentBasketPosts(keyword,
                    loggedInUser.getSuberNo(),
                    loggedInUser.getDepartmentNo(),
                    loggedInUser.getEmployeeNo(),
                    loggedInUser.getManager());

            basketList = basketService.getDepartmentBasketPostsPaged(keyword,
                    loggedInUser.getSuberNo(),
                    loggedInUser.getDepartmentNo(),
                    loggedInUser.getEmployeeNo(),
                    loggedInUser.getManager(),
                    offset, limit);

        } else if ("개인".equals(repositoryType)) {
            totalCount = basketService.countIndividualBasketPosts(keyword, loggedInUser.getEmployeeNo());
            basketList = basketService.getIndividualBasketPostsPaged(keyword, loggedInUser.getEmployeeNo(), offset, limit);

        } else {
            // 기본 전체 처리
            totalCount = basketService.countOwnTotalBasketPosts(keyword, loggedInUser.getEmployeeNo());
            basketList = basketService.getOwnTotalBasketPostsPaged(keyword, loggedInUser.getEmployeeNo(), offset, limit);
        }

        int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / pageSize));

        model.addAttribute("basketList", basketList);
        model.addAttribute("repositoryType", repositoryType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageGroup", pageGroup);

        return "group/repository/basket";
    }

	// 선택 게시글 복원
	@PostMapping("/restore")
	public String restoreSelected(@RequestParam("writingIds") List<Long> writingIds,
	                              @RequestParam("repositoryType") String repositoryType) {

	    basketService.restoreSelectedPosts(writingIds);

	    String encoded = URLEncoder.encode(repositoryType, StandardCharsets.UTF_8);
	    return "redirect:/basket?repositoryType=" + encoded;
	}

	@PostMapping("/delete")
	public String deleteSelected(@RequestParam("writingIds") List<Long> writingIds,
	                             @RequestParam("repositoryType") String repositoryType) {

	    basketService.permanentlyDeletePosts(writingIds);

	    String encoded = URLEncoder.encode(repositoryType, StandardCharsets.UTF_8);
	    return "redirect:/basket?repositoryType=" + encoded;
	}
	
	@GetMapping("/detailBasket/{writingId}")
	public String detailBasket(@PathVariable("writingId") Long writingId, Model model) {
	    BasketVO basket = basketService.getBasketPostDetail(writingId);
	    
	    if (basket == null) {
	        throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
	    }

	    model.addAttribute("post", basket);
	    return "group/repository/detailBasket";
	}
}
