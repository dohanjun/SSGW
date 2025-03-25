package com.yedam.app.group.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String totalRepository(@RequestParam(value = "page", defaultValue = "1") int page,
	                              @RequestParam(value = "keyword", required = false) String keyword,
	                              Model model) {

	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    if (loggedInUser == null) {
	        throw new IllegalStateException("로그인한 사용자 정보를 찾을 수 없습니다.");
	    }
	    
	    // 고정글 조회 (fix = 'Y')
	    List<RepositoryPostVO> fixedList = postService.getFixedPosts(loggedInUser.getSuberNo(), keyword);

	    // 1. 전체 게시글 수
	    int totalCount = postService.getTotalRepositoryPostCount(loggedInUser.getSuberNo(), keyword);

	    // 2. 페이지 설정
	    int pageSize = 10;
	    int pageGroup = 10;
	    int totalPages = (int) Math.ceil((double) totalCount / pageSize);
	    
	    // ROW_NUMBER 기반 페이징용 offset, limit 계산
	    int offset = (page - 1) * pageSize;
	    int limit = pageSize;

	    // 3. 게시글 목록
	    List<RepositoryPostVO> totalRepositoryList = postService.getTotalRepositoryPostsPaged(
	            loggedInUser.getSuberNo(), keyword, offset, limit
	    );

	    // 4. 권한 체크
	    boolean isAdmin = (loggedInUser.getRightsId() != null && loggedInUser.getRightsId() == 3)
	            || (loggedInUser.getRightsLevel() != null && loggedInUser.getRightsLevel() == 5);

	    // 5. 모델 전달
	    model.addAttribute("loginUser", loggedInUser);
	    model.addAttribute("repository", repositoryService.getTotalRepository(loggedInUser.getSuberNo()));
	    model.addAttribute("fixedList", fixedList); // 고정글 따로 전달
	    model.addAttribute("totalRepositoryList", totalRepositoryList);
	    model.addAttribute("isAdmin", isAdmin);
	    model.addAttribute("loggedInEmpNo", loggedInUser.getEmployeeNo());

	    // 페이징 관련
	    model.addAttribute("page", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("pageGroup", pageGroup); // 선택적 (페이지 그룹 묶음 단위)
	    model.addAttribute("keyword", keyword);

	    return "group/repository/totalRepository";
	}


	@GetMapping("/departmentRepository")
	public String departmentRepository(@RequestParam(value = "page", defaultValue = "1") int page,
	                                   @RequestParam(value = "keyword", required = false) String keyword,
	                                   Model model) {

	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    if (loggedInUser == null) {
	        throw new IllegalStateException("로그인한 사용자 정보를 찾을 수 없습니다.");
	    }

	    // 1. 고정글 조회
	    List<RepositoryPostVO> fixedList = postService.getDepartmentFixedPosts(
	        loggedInUser.getSuberNo(), loggedInUser.getDepartmentNo(), keyword
	    );

	    // 2. 일반글 개수
	    int totalCount = postService.getDepartmentRepositoryPostCount(
	        loggedInUser.getSuberNo(), loggedInUser.getDepartmentNo(), keyword
	    );

	    // 3. 페이징 설정
	    int pageSize = 10;
	    int pageGroup = 10;
	    int totalPages = (int) Math.ceil((double) totalCount / pageSize);
	    int offset = (page - 1) * pageSize;
	    int limit = pageSize;

	    // 4. 일반글 조회 (페이징)
	    List<RepositoryPostVO> departmentRepositoryList = postService.getDepartmentRepositoryPostsPaged(
	        loggedInUser.getSuberNo(), loggedInUser.getDepartmentNo(), keyword, offset, limit
	    );

	    boolean isManager = loggedInUser.getManager() != null;

	    model.addAttribute("loginUser", loggedInUser);
	    model.addAttribute("repository", repositoryService.getDepartmentRepository(
	        loggedInUser.getSuberNo(), loggedInUser.getDepartmentNo()
	    ));
	    model.addAttribute("fixedList", fixedList); // ✅ 고정글
	    model.addAttribute("departmentRepositoryList", departmentRepositoryList); // ✅ 일반글
	    model.addAttribute("isManager", isManager);
	    model.addAttribute("loggedInEmpNo", loggedInUser.getEmployeeNo());

	    model.addAttribute("page", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("pageGroup", pageGroup);
	    model.addAttribute("keyword", keyword);

	    return "group/repository/departmentRepository";
	}

	@GetMapping("/individualRepository")
	public String individualRepository(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

	    EmpVO loggedInUser = empService.getLoggedInUserInfo();

	    if (loggedInUser == null) {
	        throw new IllegalStateException("로그인한 사용자 정보를 찾을 수 없습니다.");
	    }

	    // 1. 전체 게시글 수
	    int totalCount = postService.getIndividualRepositoryPostCount(loggedInUser.getSuberNo(), loggedInUser.getEmployeeNo(), keyword);

	    // 2. 페이지 설정
	    int pageSize = 10;
	    int pageGroup = 10;
	    int totalPages = (int) Math.ceil((double) totalCount / pageSize);
	    int offset = (page - 1) * pageSize;
	    int limit = pageSize;

	    // 3. 게시글 목록
	    List<RepositoryPostVO> individualRepositoryList = postService.getIndividualRepositoryPostsPaged(
	        loggedInUser.getSuberNo(), loggedInUser.getEmployeeNo(), keyword, offset, limit
	    );

	    model.addAttribute("loginUser", loggedInUser);
	    model.addAttribute("repository", repositoryService.getIndividualRepository(loggedInUser.getSuberNo(), loggedInUser.getEmployeeNo()));
	    model.addAttribute("individualRepositoryList", individualRepositoryList);
	    model.addAttribute("loggedInEmpNo", loggedInUser.getEmployeeNo());

	    // 페이징 관련
	    model.addAttribute("page", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("pageGroup", pageGroup);
	    model.addAttribute("keyword", keyword);
	    
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

	    return "group/repository/detailBasket"; // templates/group/repository/detailBasket.html
	}

}
