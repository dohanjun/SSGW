package com.yedam.app.group.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.yedam.app.group.service.BasketService;
import com.yedam.app.group.service.BasketVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.FileService;
import com.yedam.app.group.service.PostService;
import com.yedam.app.group.service.RepositoryFileVO;
import com.yedam.app.group.service.RepositoryPostVO;
import com.yedam.app.group.service.RepositoryService;
import com.yedam.app.group.service.RepositoryVO;

@Controller
public class RepositoryController {

	private final RepositoryService repositoryService;
    private final EmpService empService;
    private final PostService postService;
	private final FileService fileService;
	private final BasketService basketService;
    
	public RepositoryController(RepositoryService repositoryService,
								EmpService empService, 
								PostService postService,
								FileService fileService,
								BasketService basketService
	) {
		this.repositoryService = repositoryService;
		this.empService = empService;
		this.postService = postService;
		this.fileService = fileService;
		this.basketService = basketService;
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
	    System.out.println("최종 전달할 자료실 ID: " + totalRepository.getFileRepositoryId());
	    System.out.println("최종 전달할 게시글 개수: " + (totalRepositoryList != null ? totalRepositoryList.size() : "null"));
	    
	    // ull 방지 (리스트가 null이면 빈 리스트로 초기화)
	    if (totalRepositoryList == null) {
	        totalRepositoryList = new ArrayList<>();
	    }
	    
	    boolean isAdmin = (loggedInUser.getRightsId() != null && loggedInUser.getRightsId() == 3)
                || (loggedInUser.getRightsLevel() != null && loggedInUser.getRightsLevel() == 5);
	    
	    // Model에 추가하여 HTML에서 사용할 수 있도록 설정
	    model.addAttribute("loginUser", loggedInUser);
	    model.addAttribute("repository", totalRepository);
	    model.addAttribute("totalRepositoryList", totalRepositoryList);
	    model.addAttribute("isAdmin", isAdmin);
	    model.addAttribute("loggedInEmpNo", loggedInUser.getEmployeeNo());
	    
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
	    
	    boolean isManager = loggedInUser.getManager() == null ? false : true;
	    
	    model.addAttribute("loginUser", loggedInUser);
	    model.addAttribute("repository", departmentRepository);
	    model.addAttribute("departmentRepositoryList", departmentRepositoryList);
	    model.addAttribute("loggedInEmpNo", loggedInUser.getEmployeeNo());
	    model.addAttribute("isManager", isManager);
	    
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
	    
	    model.addAttribute("loginUser", loggedInUser);
	    model.addAttribute("repository", individualRepository);
	    model.addAttribute("individualRepositoryList", individualRepositoryList);
	    model.addAttribute("loggedInEmpNo", loggedInUser.getEmployeeNo());
	    
	    return "group/repository/individualRepository";
	}

	@GetMapping("/detailPost/{writingId}")
	public String detailPost(@PathVariable Long writingId, Model model) {
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    RepositoryPostVO post = postService.getPostDetail(writingId);
	    List<RepositoryFileVO> fileList = fileService.getFilesByWritingId(writingId);
	    
	    Integer writerId = post.getEmployeeNo();
	    Integer currentUserId = loggedInUser.getEmployeeNo();
	    
	    boolean isOwner = writerId != null && writerId.equals(currentUserId);
	    boolean isAdmin = 
	        (loggedInUser.getRightsId() != null && loggedInUser.getRightsId() == 3) ||
	        (loggedInUser.getRightsLevel() != null && loggedInUser.getRightsLevel() == 5);
	    
	    // 로그인한 사람이 해당 게시글 작성자의 부서장인지 확인
	    boolean isManager = writerId != null && writerId.equals(loggedInUser.getManager());
	    
	    boolean isEditable = false;
	    switch (post.getRepositoryType()) {
	        case "전체" -> isEditable = isOwner || isAdmin;
	        case "부서" -> isEditable = isOwner || isManager;
	        case "개인" -> isEditable = isOwner;
	    }
	    
	    System.out.println("작성자 ID: " + post.getEmployeeNo());
	    System.out.println("현재 로그인 ID: " + loggedInUser.getEmployeeNo());
	    System.out.println("isOwner: " + isOwner);
	    System.out.println("isAdmin: " + isAdmin);
	    System.out.println("isManager: " + isManager);
	    System.out.println("최종 isEditable: " + isEditable);

	    model.addAttribute("post", post);
	    model.addAttribute("fileList", fileList);
	    model.addAttribute("isEditable", isEditable);
	    model.addAttribute("canEditOrDelete", isEditable);
	    
	    return "group/repository/detailPost";
	}

	@GetMapping("/detailBasket/{writingId}")
	public String detailBasket(@PathVariable Long writingId, Model model) {
	    BasketVO basket = basketService.getBasketPostDetail(writingId); // ← 이 메서드 구현했는지 확인
	    List<RepositoryFileVO> fileList = fileService.getFilesByWritingId(writingId);

	    model.addAttribute("post", basket);
	    model.addAttribute("fileList", fileList);
	    model.addAttribute("isEditable", false); // 휴지통에서는 수정/삭제 제어

	    return "group/repository/detailBasket"; // 📄 templates/group/repository/detailBasket.html
	}

}
