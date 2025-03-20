package com.yedam.app.group.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.PostService;
import com.yedam.app.group.service.RepositoryPostVO;
import com.yedam.app.group.service.RepositoryService;
import com.yedam.app.group.service.RepositoryVO;

@Controller
public class RepositoryController {

	private final RepositoryService repositoryService;
    private final EmpService empService;
    private final PostService postService;
    
	public RepositoryController(RepositoryService repositoryService, EmpService empService, PostService postService) {
		this.repositoryService = repositoryService;
		this.empService = empService;
		this.postService = postService;
	}

	@GetMapping("/totalRepository")
	public String totalRepository(Model model) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();

	    if (loggedInUser == null) {
	        throw new IllegalStateException("로그인한 사용자 정보를 찾을 수 없습니다.");
	    }

	    // 자료실 정보 가져오기
	    RepositoryVO totalRepository = repositoryService.getTotalRepository(loggedInUser.getSuberNo());

	    if (totalRepository == null) {
	        throw new IllegalStateException("해당 회사의 자료실을 찾을 수 없습니다.");
	    }

	    // 자료실에 등록된 게시글 목록 가져오기
	    List<RepositoryPostVO> totalRepositoryList = postService.getTotalRepositoryPosts(loggedInUser.getSuberNo());
	    
	    // 디버깅 로그 추가
	    System.out.println("🔹 최종 전달할 자료실 ID: " + totalRepository.getFileRepositoryId());
	    System.out.println("🔹 최종 전달할 게시글 개수: " + (totalRepositoryList != null ? totalRepositoryList.size() : "null"));
	    
	    // ull 방지 (리스트가 null이면 빈 리스트로 초기화)
	    if (totalRepositoryList == null) {
	        totalRepositoryList = new ArrayList<>();
	    }
	    
	    // Model에 추가하여 HTML에서 사용할 수 있도록 설정
	    model.addAttribute("repository", totalRepository);  // 자료실 정보
	    model.addAttribute("totalRepositoryList", totalRepositoryList);  // 게시글 목록
	    
	    return "group/repository/totalRepository";
	}

	@GetMapping("/departmentRepository")
	public String departmentRepository(Model model) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();

	    if (loggedInUser == null) {
	        throw new IllegalStateException("로그인한 사용자 정보를 찾을 수 없습니다.");
	    }

	    RepositoryVO departmentRepository = repositoryService.getDepartmentRepository(
	            loggedInUser.getSuberNo(), loggedInUser.getDepartmentNo());

	    if (departmentRepository == null) {
	        throw new IllegalStateException("해당 부서의 자료실을 찾을 수 없습니다.");
	    }
	    
	    List<RepositoryPostVO> departmentRepositoryList = postService.getDepartmentRepositoryPosts(
                loggedInUser.getSuberNo(), loggedInUser.getDepartmentNo());

	    model.addAttribute("repository", departmentRepository);
	    model.addAttribute("departmentRepositoryList", departmentRepositoryList);
	    
	    return "group/repository/departmentRepository";
	}

	@GetMapping("/individualRepository")
	public String individualRepository(Model model) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();

	    if (loggedInUser == null) {
	        throw new IllegalStateException("로그인한 사용자 정보를 찾을 수 없습니다.");
	    }

	    RepositoryVO individualRepository = repositoryService.getIndividualRepository(
	            loggedInUser.getSuberNo(), loggedInUser.getEmployeeNo());

	    if (individualRepository == null) {
	        throw new IllegalStateException("해당 사원의 자료실을 찾을 수 없습니다.");    
	    }
	    
	    List<RepositoryPostVO> individualRepositoryList = postService.getIndividualRepositoryPosts(
                loggedInUser.getSuberNo(), loggedInUser.getEmployeeNo());
	    
	    model.addAttribute("repository", individualRepository);
	    model.addAttribute("individualRepositoryList", individualRepositoryList);
	    
	    return "group/repository/individualRepository";
	}

	@GetMapping("/detailPost")
	public String detailPost() {
		return "group/repository/detailPost";
	}

	@GetMapping("/basket")
	public String basket() {
		return "group/repository/basket";
	}

	@GetMapping("/detailBasket")
	public String detailBasket() {
		return "group/repository/detailBasket";
	}

}
