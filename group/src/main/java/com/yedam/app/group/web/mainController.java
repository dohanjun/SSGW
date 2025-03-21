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
import com.yedam.app.group.service.SubscriberService;
import com.yedam.app.group.service.SubscriberVO;
import com.yedam.app.group.service.SubscriptionSummaryVO;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class mainController {
	
	private final ModuleService moduleService;
	private final BoardPostService boardPostService;
	private final SubscriberService subscriberService;
	
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
	public String page1(@RequestParam(defaultValue = "1") int page,
	                    @RequestParam(required = false) String keyword,
	                    Model model) {
	    addBoardData(page, keyword, model);
	    return "externalPages/qnaPage";
	}

	@GetMapping("/qnaBoard")
	public String page2(@RequestParam(defaultValue = "1") int page,
	                    @RequestParam(required = false) String keyword,
	                    Model model) {
	    addBoardData(page, keyword, model);
	    return "group/QnA/qnaPage";
	}

	private void addBoardData(int page, String keyword, Model model) {
	    int pageSize = 10;
	    int totalCount;
	    List<BoardPostVO> boardList;

	    if (keyword != null && !keyword.trim().isEmpty()) {
	        boardList = boardPostService.getPagedPostsByKeyword(keyword, page);
	        totalCount = boardPostService.getTotalCountByKeyword(keyword);
	    } else {
	        boardList = boardPostService.getBoardList(page);
	        totalCount = boardPostService.getTotalCount();
	    }

	    int totalPages = (int) Math.ceil((double) totalCount / pageSize);

	    model.addAttribute("boardList", boardList);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("keyword", keyword);
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
	    boardPostService.createBoard(boardPost);
	    return ResponseEntity.ok("게시글이 성공적으로 등록되었습니다.");
	}
	
	@PostMapping("/updateBoardPost")
	public ResponseEntity<String> updateBoardPost(@RequestBody BoardPostVO boardPost) {
	    System.out.println("받은 데이터: " + boardPost);
	    boardPostService.modifyBoard(boardPost);
	    return ResponseEntity.ok("게시글이 성공적으로 등록되었습니다.");
	}

	@GetMapping("/suberList")
	public String suberListPage(Model model) {
	    List<SubscriptionSummaryVO> subscribers = subscriberService.findAllSubscribers();
	    model.addAttribute("subscribers", subscribers);
	    return "externalPages/suberListPage";
	}
	
	@PostMapping("/suberInfo")
	public String suberInfoPage(@RequestParam("suberNo") int suberNo, Model model) {
		List<SubscriberVO> suber = subscriberService.findinfoSuberByNo(suberNo);
	    model.addAttribute("suber", suber);
	    return "externalPages/suberInfoPage";
	}

	
	@PostMapping("/selectBoardPost")
	public ResponseEntity<BoardPostVO> selectBoardPost(@RequestBody BoardPostVO boardPost) {
	    BoardPostVO childPost = boardPostService.findinfoChildPostByParentId(boardPost.getParentCommentId());
	    if(childPost != null) {
	        return ResponseEntity.ok(childPost);
	    } else {
	        return ResponseEntity.noContent().build();
	    }
	}

}
