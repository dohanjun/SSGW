package com.yedam.app.group.web;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.service.BoardPostVO;
import com.yedam.app.group.service.BoardService;
import com.yedam.app.group.service.BoardVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

@Controller
public class BoardController {
	
	private final BoardService boardService;
    private final EmpService empService;

    public BoardController(BoardService boardService, EmpService empService) {
        this.boardService = boardService;
        this.empService = empService;
    }
	
	@GetMapping("/boardList")
	public String boardList(Model model) {
	    EmpVO loginUser = empService.getLoggedInUserInfo();

	    List<BoardVO> noticeBoards = boardService.getBoardsByType("공지", loginUser.getSuberNo(), null, null);
	    List<BoardVO> departmentBoards = boardService.getBoardsByType("부서", loginUser.getSuberNo(), loginUser.getDepartmentNo(), null);
	    List<BoardVO> freeBoards = boardService.getBoardsByType("자유", loginUser.getSuberNo(), null, loginUser.getEmployeeNo());

	    model.addAttribute("noticeBoards", noticeBoards);
	    model.addAttribute("departmentBoards", departmentBoards);
	    model.addAttribute("freeBoards", freeBoards);

	    return "group/board/boardList";
	}
	
	@GetMapping("/noticeBoard")
	public String noticeBoard(@RequestParam(value = "page", defaultValue = "1") int page,
	                          @RequestParam(value = "keyword", required = false) String keyword,
	                          Model model) {

	    EmpVO loginUser = empService.getLoggedInUserInfo();

	    int pageSize = 10;
	    int offset = (page - 1) * pageSize;
	    int totalCount = boardService.getNoticeBoardPostCount(loginUser.getSuberNo(), keyword);
	    int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / pageSize));

	    List<BoardVO> postList = boardService.getNoticeBoardPostsPaged(loginUser.getSuberNo(), keyword, offset, pageSize);

	    model.addAttribute("postList", postList);
	    model.addAttribute("page", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("loginUser", loginUser);

	    return "group/board/noticeBoard";
	}
	
	@GetMapping("/departmentBoard")
    public String departmentBoard(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {
		
		EmpVO loginUser = empService.getLoggedInUserInfo();
		
		int pageSize = 10;
	    int offset = (page - 1) * pageSize;
	    int totalCount = boardService.getDepartmentBoardPostCount(
	        loginUser.getSuberNo(), loginUser.getDepartmentNo(), keyword);
	    int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / pageSize));

	    List<BoardVO> postList = boardService.getDepartmentBoardPostsPaged(
	        loginUser.getSuberNo(), loginUser.getDepartmentNo(), keyword, offset, pageSize);

	    model.addAttribute("postList", postList);
	    model.addAttribute("page", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("loginUser", loginUser);

	    return "group/board/departmentBoard";
    }
	
	@GetMapping("/freeBoard")
	public String freeBoard(@RequestParam(value = "page", defaultValue = "1") int page,
	                        @RequestParam(value = "keyword", required = false) String keyword,
	                        Model model) {

	    EmpVO loginUser = empService.getLoggedInUserInfo();

	    int pageSize = 10;
	    int offset = (page - 1) * pageSize;
	    int totalCount = boardService.getFreeBoardPostCount(loginUser.getSuberNo(), keyword);
	    int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / pageSize));

	    List<BoardVO> postList = boardService.getFreeBoardPostsPaged(loginUser.getSuberNo(), keyword, offset, pageSize);

	    model.addAttribute("postList", postList);
	    model.addAttribute("page", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("loginUser", loginUser);

	    return "group/board/freeBoard";
	}
	
	@GetMapping("/detailBoard")
    public String detailBoard() {
        return "group/board/detailBoard";
    }
	
	@GetMapping("/insertBoard")
	public String insertBoard(@RequestParam("boardType") String boardType, Model model) {
	    EmpVO loginUser = empService.getLoggedInUserInfo();

	    model.addAttribute("loginUser", loginUser);
	    model.addAttribute("boardType", boardType); // 공지 / 부서 / 자유 판단용

	    // 공지 등록 권한 → 관리자만 가능
	    boolean isAdmin = (loginUser.getRightsId() != null && loginUser.getRightsId() == 3)
	                   || (loginUser.getRightsLevel() != null && loginUser.getRightsLevel() == 5);
	    model.addAttribute("isAdmin", isAdmin);

	    // 부서 게시판 등록 권한 → 부서장만 가능 (manager가 null이 아니면 부서장)
	    boolean isDepartmentManager = loginUser.getManager() != null;
	    model.addAttribute("isDepartmentManager", isDepartmentManager);

	    return "group/board/insertBoard";
	}
	
	@PostMapping("/insertBoard")
	public String insertBoard(BoardPostVO postVO,
	                          @RequestParam("boardType") String boardType,
	                          @RequestParam("files") List<MultipartFile> files) {
	    EmpVO loginUser = empService.getLoggedInUserInfo();

	    postVO.setEmployeeNo(loginUser.getEmployeeNo());
	    postVO.setPostDate(new Date());
	    postVO.setFixed('N');
	    postVO.setFaq('N');
	    postVO.setRead('N');
	    
	    boardService.insertBoardPost(postVO, boardType, loginUser, files);
	    return "redirect:/boardList";
	}

	
}
