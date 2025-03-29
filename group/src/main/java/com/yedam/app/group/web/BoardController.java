package com.yedam.app.group.web;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.service.BoardAttachmentVO;
import com.yedam.app.group.service.BoardPostVO;
import com.yedam.app.group.service.BoardService;
import com.yedam.app.group.service.BoardVO;
import com.yedam.app.group.service.CommentService;
import com.yedam.app.group.service.CommentVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

@Controller
public class BoardController {
	
	private final BoardService boardService;
    private final EmpService empService;
    private final CommentService commentService;

    public BoardController(BoardService boardService, EmpService empService, CommentService commentService) {
        this.boardService = boardService;
        this.empService = empService;
        this.commentService = commentService;
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
                || (loggedInUser.getRightsLevel() != null && loggedInUser.getRightsLevel() == 5);
	    
	    model.addAttribute("postList", postList);
	    model.addAttribute("page", page);
	    model.addAttribute("totalCount", totalCount);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("loggedInUser", loggedInUser);
	    model.addAttribute("isAdmin", isAdmin);
	    
	    System.out.println("postList: " + postList);

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
	    model.addAttribute("totalCount", totalCount);
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
	    model.addAttribute("totalCount", totalCount);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("loggedInUser", loggedInUser);

	    return "group/board/freeBoard";
	}
	
	@GetMapping("/detailBoard/{postId}")
	public String detailBoard(@PathVariable int postId, Model model) {
	    BoardPostVO post = boardService.getPostDetail(postId);
	    
	    List<BoardAttachmentVO> attachments = boardService.getAttachments(postId);
	    
	    List<CommentVO> comments = commentService.getCommentsByPostId(postId);
	    
	    boolean hasPdf = false;
	    Integer firstPdfId = null;

	    for (BoardAttachmentVO file : attachments) {
	        if (file.getFileTitle().toLowerCase().endsWith(".pdf")) {
	            hasPdf = true;
	            firstPdfId = file.getAttachmentId();
	            break;
	        }
	    }
	    
	    model.addAttribute("attachments", attachments);
	    model.addAttribute("post", post);
	    model.addAttribute("hasPdf", hasPdf);
	    model.addAttribute("firstPdfId", firstPdfId);
	    model.addAttribute("comments", comments);
	    return "group/board/detailBoard";
	}
	
	@GetMapping("/insertBoard")
	public String insertBoard(@RequestParam("boardType") String boardType, Model model) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		
		List<BoardVO> boards = boardService.getBoardsByType(
		        boardType,
		        loggedInUser.getSuberNo(),
		        boardType.equals("부서") ? loggedInUser.getDepartmentNo() : null,
		        null
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
	
	@PostMapping("/uploadImage")
	@ResponseBody
	public String uploadImage(@RequestParam("file") MultipartFile file) {
		try {
	        String uploadDir = "D:/upload_files/";
	        String uuid = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	        Path savePath = Paths.get(uploadDir + uuid);
	        file.transferTo(savePath.toFile());

	        // 업로드된 파일의 URL 경로 반환 (정적 매핑 필요)
	        return "/uploads/" + uuid;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "error";
	    }
	}
	
	@PostMapping("/comment/add")
	@ResponseBody
	public ResponseEntity<?> addComment(@RequestParam("postId") int postId, 
	                                    @RequestParam("content") String content,
	                                    Model model) {
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();

	    CommentVO newComment = new CommentVO();
	    newComment.setPostId(postId);
	    newComment.setContent(content);
	    newComment.setEmployeeNo(loggedInUser.getEmployeeNo());
	    newComment.setBoardDate(new Date());
	    newComment.setParentCommentId(null); // 댓글은 부모가 없으므로 null 설정

	    commentService.addComment(newComment);

	    // 댓글 추가 후 해당 게시글 상세페이지로 리다이렉션
	    return ResponseEntity.ok().body("댓글이 추가되었습니다.");
	}

    // 대댓글 작성
    @PostMapping("/comment/reply")
    @ResponseBody
    public ResponseEntity<?> addReplyToComment(@RequestParam("postId") int postId,
                                               @RequestParam("content") String content,
                                               @RequestParam("parentCommentId") int parentCommentId) {
        CommentVO replyComment = new CommentVO();
        replyComment.setPostId(postId);
        replyComment.setContent(content);
        replyComment.setParentCommentId(parentCommentId); // 부모 댓글 ID를 설정
        commentService.addReplyToComment(replyComment);
        return ResponseEntity.ok().body("대댓글이 추가되었습니다.");
    }

    // 댓글 수정
    @PostMapping("/comment/edit")
    @ResponseBody
    public ResponseEntity<?> editComment(@RequestParam("commentId") int commentId, 
                                         @RequestParam("content") String content) {
        commentService.editComment(commentId, content);
        return ResponseEntity.ok().body("댓글이 수정되었습니다.");
    }

    // 댓글 삭제
    @PostMapping("/comment/delete")
    @ResponseBody
    public ResponseEntity<?> deleteComment(@RequestParam("commentId") int commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().body("댓글이 삭제되었습니다.");
    }
	
}
