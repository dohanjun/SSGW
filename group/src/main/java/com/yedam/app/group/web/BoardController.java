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
		EmpVO loggedInUser = empService.getLoggedInUserInfo();

	    List<BoardVO> noticeBoards = boardService.getBoardsByType("공지", loggedInUser.getSuberNo(), null, null);
	    List<BoardVO> departmentBoards = boardService.getBoardsByType("부서", loggedInUser.getSuberNo(), loggedInUser.getDepartmentNo(), null);
	    List<BoardVO> freeBoards = boardService.getBoardsByType("자유", loggedInUser.getSuberNo(), null, loggedInUser.getEmployeeNo());

	    model.addAttribute("noticeBoards", noticeBoards);
	    model.addAttribute("departmentBoards", departmentBoards);
	    model.addAttribute("freeBoards", freeBoards);

	    return "group/board/boardList";
	}
	
	@GetMapping("/noticeBoard")
	public String noticeBoard(@RequestParam(value = "page", defaultValue = "1") int page,
	                          @RequestParam(value = "keyword", required = false) String keyword,
	                          Model model) {

		EmpVO loggedInUser = empService.getLoggedInUserInfo();

	    int pageSize = 10;
	    int offset = (page - 1) * pageSize;
	    int totalCount = boardService.getNoticeBoardPostCount(loggedInUser.getSuberNo(), keyword);
	    int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / pageSize));

	    List<BoardPostVO> postList = boardService.getNoticeBoardPostsPaged(loggedInUser.getSuberNo(), keyword, offset, pageSize);
	    
	    boolean isAdmin = (loggedInUser.getRightsId() != null && loggedInUser.getRightsId() == 3)
                || (loggedInUser.getRightsLevel() != null && loggedInUser.getRightsLevel() >= 5);
	    
	    model.addAttribute("postList", postList);
	    model.addAttribute("page", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("loggedInUser", loggedInUser);
	    model.addAttribute("isAdmin", isAdmin);

	    return "group/board/noticeBoard";
	}
	
	@GetMapping("/departmentBoard")
    public String departmentBoard(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {
		
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		
		int pageSize = 10;
	    int offset = (page - 1) * pageSize;
	    int totalCount = boardService.getDepartmentBoardPostCount(
	    		loggedInUser.getSuberNo(), loggedInUser.getDepartmentNo(), keyword);
	    int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / pageSize));

	    List<BoardPostVO> postList = boardService.getDepartmentBoardPostsPaged(
	    		loggedInUser.getSuberNo(), loggedInUser.getDepartmentNo(), keyword, offset, pageSize);

	    model.addAttribute("postList", postList);
	    model.addAttribute("page", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("loggedInUser", loggedInUser);

	    return "group/board/departmentBoard";
    }
	
	@GetMapping("/freeBoard")
	public String freeBoard(@RequestParam(value = "page", defaultValue = "1") int page,
	                        @RequestParam(value = "keyword", required = false) String keyword,
	                        Model model) {

		EmpVO loggedInUser = empService.getLoggedInUserInfo();

	    int pageSize = 10;
	    int offset = (page - 1) * pageSize;
	    int totalCount = boardService.getFreeBoardPostCount(loggedInUser.getSuberNo(), keyword);
	    int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / pageSize));

	    List<BoardPostVO> postList = boardService.getFreeBoardPostsPaged(loggedInUser.getSuberNo(), keyword, offset, pageSize);

	    model.addAttribute("postList", postList);
	    model.addAttribute("page", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("loggedInUser", loggedInUser);

	    return "group/board/freeBoard";
	}
	
	@GetMapping("/detailBoard")
    public String detailBoard() {
        return "group/board/detailBoard";
    }
	
	@GetMapping("/insertBoard")
	public String insertBoard(@RequestParam("boardType") String boardType, Model model) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		
		List<BoardVO> boards = boardService.getBoardsByType(
		        boardType,
		        loggedInUser.getSuberNo(),
		        boardType.equals("부서") ? loggedInUser.getDepartmentNo() : null,
		        boardType.equals("자유") ? null : loggedInUser.getEmployeeNo()
		    );

		    if (boards == null || boards.isEmpty()) {
		        throw new IllegalStateException("등록할 게시판이 없습니다.");
		    }

		BoardVO board = boards.get(0);
		
		model.addAttribute("boardId", board.getBoardId());
	    model.addAttribute("writer", loggedInUser.getEmployeeName()); // 로그인한 사용자 이름
        model.addAttribute("employeeNo", loggedInUser.getEmployeeNo()); // 로그인한 사용자 사원번호
	    model.addAttribute("boardType", boardType); // 공지 / 부서 / 자유 판단용
	    
	    // 공지 등록 권한 → 관리자만 가능
	    boolean isAdmin = (loggedInUser.getRightsId() != null && loggedInUser.getRightsId() == 3)
	                   || (loggedInUser.getRightsLevel() != null && loggedInUser.getRightsLevel() == 5);
	    model.addAttribute("isAdmin", isAdmin);

	    // 부서 게시판 등록 권한 → 부서장만 가능 (manager가 null이 아니면 부서장)
	    boolean isDepartmentManager = loggedInUser.getManager() != null;
	    model.addAttribute("isDepartmentManager", isDepartmentManager);

	    return "group/board/insertBoard";
	}
	
	@PostMapping("/insertBoard")
	public String insertBoard(BoardPostVO postVO,
	                          @RequestParam("boardType") String boardType,
	                          @RequestParam("files") List<MultipartFile> files) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();

	    postVO.setEmployeeNo(loggedInUser.getEmployeeNo());
	    postVO.setPostDate(new Date());
	    postVO.setFixed('N');
	    postVO.setFaq('N');
	    postVO.setRead('N');
	    
	    boardService.insertBoardPost(postVO, boardType, loggedInUser, files);
	    
	    // boardType에 따라 리다이렉트 분기
	    return switch (boardType) {
	        case "공지" -> "redirect:/noticeBoard?page=1";
	        case "부서" -> "redirect:/departmentBoard?page=1";
	        case "자유" -> "redirect:/freeBoard?page=1";
	        default -> "redirect:/boardList"; // fallback
	    };
	}

}
