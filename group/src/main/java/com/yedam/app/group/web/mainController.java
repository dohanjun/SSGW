package com.yedam.app.group.web;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.yedam.app.group.service.BoardPostService;
import com.yedam.app.group.service.BoardPostVO;
import com.yedam.app.group.service.ModuleService;
import com.yedam.app.group.service.ModuleVO;
import com.yedam.app.group.service.PaymentVO;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class mainController {
	
	private final ModuleService moduleService;
	private final BoardPostService boardPostService;
	
	@GetMapping("main")
	public String mainPage() {
		return "group/mainPage";
	}

	@GetMapping("/manual")
	public String manualPage() {
		return "externalPages/manualPage";
	}

	@GetMapping("/login")
	public String loginPage() {
		return "externalPages/loginPage";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/";
	}
	
	@GetMapping("/qna")
	public String boardPage(@RequestParam(defaultValue = "1") int page,
	                        @RequestParam(required = false) String keyword,
	                        Model model) {
	    int pageSize = 10;
	    int totalCount;
	    List<BoardPostVO> boardList;

	    if (keyword != null && !keyword.trim().isEmpty()) {
	        // 검색어가 있을 경우
	        boardList = boardPostService.getPagedPostsByKeyword(keyword, page);
	        totalCount = boardPostService.getTotalCountByKeyword(keyword);
	    } else {
	        // 검색어가 없을 경우 전체 리스트 가져오기
	        boardList = boardPostService.getBoardList(page);
	        totalCount = boardPostService.getTotalCount();
	    }

	    int totalPages = (int) Math.ceil((double) totalCount / pageSize);

	    model.addAttribute("boardList", boardList);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("keyword", keyword);

	    return "externalPages/qnaPage";
	}

	
	@GetMapping("/qna/detail")
	public String getBoardDetail(@RequestParam("postId") int postId, Model model) {
	    BoardPostVO boardPost = boardPostService.getBoardDetail(postId);
	    model.addAttribute("boardPost", boardPost);
	    return "externalPages/qnaDetail"; 
	}
	
	@GetMapping("/module")
	public String subscribePage(Model model) {
		List<ModuleVO> modules = moduleService.getAllModules();
		model.addAttribute("modules", modules);
		return "externalPages/modulePage";
	}
	
	@PostMapping("/fixed")
	public ResponseEntity<String> modifyFixed(@RequestParam("postId") int postId) {
		boardPostService.modifyBoartFixed(postId);
	    return ResponseEntity.ok("내역 저장 성공");
	}
	
	@DeleteMapping("/deletePost")
	public ResponseEntity<String> removeBoradPost(@RequestParam("postId") int postId) {
		boardPostService.removeBoradPost(postId);
	    return ResponseEntity.ok("삭제 성공");
	}
	
	@PostMapping("/insertBoardPost")
	public ResponseEntity<String> insertBoardPost(@RequestBody BoardPostVO boardPost) {
	    System.out.println("받은 데이터: " + boardPost);
	    boardPostService.createBoard(boardPost);
	    return ResponseEntity.ok("게시글이 성공적으로 등록되었습니다.");
	}

	
	
}
