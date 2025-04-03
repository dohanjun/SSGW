package com.yedam.app.group.web;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.yedam.app.group.service.AlarmService;
import com.yedam.app.group.service.AlarmVO;
import com.yedam.app.group.service.ApprovalVO;
import com.yedam.app.group.service.BoardPostService;
import com.yedam.app.group.service.BoardPostVO;
import com.yedam.app.group.service.BoardService;
import com.yedam.app.group.service.BoardVO;
import com.yedam.app.group.service.DashboardService;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.MailVO;
import com.yedam.app.group.service.ModuleService;
import com.yedam.app.group.service.ModuleVO;
import com.yedam.app.group.service.PaymentService;
import com.yedam.app.group.service.PaymentVO;
import com.yedam.app.group.service.RepositoryPostVO;
import com.yedam.app.group.service.ScheduleVO;
import com.yedam.app.group.service.SubscriberService;
import com.yedam.app.group.service.SubscriberVO;
import com.yedam.app.group.service.SubscriptionSummaryVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


/** 외부페이지 컨트롤
 * @author 조성민
 * @since 2025-03-20

 * <pre>
 * 수정일자      수정자    수정내용
 * --------------------------------
 * 2025-03-24  조성민
 * 2025-03-26  조성민   
 * </pre>
 * */


@Controller
@RequiredArgsConstructor
public class mainController {
	
	private final ModuleService moduleService;
	private final BoardPostService boardPostService;
	private final SubscriberService subscriberService;
	private final PaymentService paymentService;
	private final EmpService empService;
	private final AlarmService alarmService;
	private final DashboardService dashboardService;
	private final BoardService boardService;
	/**
	 * 메인페이지로 이동
	 * @return group/mainPage
	 */

	@GetMapping("/main")
	public String mainPage(Model model, Principal principal) {
		EmpVO loginEmployee = empService.getLoggedInUserInfo();
		model.addAttribute("loginEmployee", loginEmployee);

		if (loginEmployee != null) {
			List<ScheduleVO> scheduleList = dashboardService.getTodaySchedule(loginEmployee.getEmployeeNo());
			List<RepositoryPostVO> repositoryList = dashboardService.getRecentRepositoryPosts();
			List<ApprovalVO> approvalList = dashboardService.getRecentApprovalList(loginEmployee.getEmployeeNo());
//			List<BoardVO> boardList = dashboardService.getRecentBoardList(loginEmployee.getSuberNo());
			List<MailVO> mailList = dashboardService.getRecentMailList(loginEmployee.getEmployeeId()); // 메일 추가
			List<BoardPostVO> postList = boardService.getNoticeBoardPostsPaged(loginEmployee.getSuberNo(), null, 10,
					10);
			String base64Image = Base64.getEncoder().encodeToString(loginEmployee.getProfileImageBLOB());
			
			
			model.addAttribute("profileImageBase64", base64Image);
			model.addAttribute("userInfo", loginEmployee);
			model.addAttribute("repositoryList", dashboardService.getRecentRepositoryPosts());
			model.addAttribute("postList", postList);
			model.addAttribute("approvalList", approvalList);
			model.addAttribute("recentMailList", dashboardService.getRecentMailList(loginEmployee.getEmployeeId()));
			model.addAttribute("todaySchedule", dashboardService.getTodaySchedule(loginEmployee.getEmployeeNo()));
			model.addAttribute("recentApprovalList",
					dashboardService.getRecentApprovalList(loginEmployee.getEmployeeNo()));
			// ✅ 디버깅
			System.out.println("✅ 로그인 사원: " + loginEmployee);
			System.out.println("✅ 오늘 일정 수: " + scheduleList.size());
			System.out.println("✅ 최근 자료실 게시글 수: " + repositoryList.size());
		}
		return "group/mainPage";
	}

	@GetMapping("/manual")
	public String manualPage(Model model) {
		List<ModuleVO> modules = moduleService.getAllModules();
		model.addAttribute("modules", modules);
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
		addBoardData(page, keyword, null, model);
	    return "externalPages/qnaPage";
	}

	@GetMapping("/qnaBoard")
	public String page2(@RequestParam(defaultValue = "1") int page,
	                    @RequestParam(required = false) String keyword,
	                    Model model) {
		addBoardData(page, keyword, null, model);
	    return "group/QnA/qnaPage";
	}
	
	@GetMapping("/myQna")
	public String myQnaPage(@RequestParam(defaultValue = "1") int page,
	                        @RequestParam(required = false) String keyword,
	                        HttpSession session,
	                        Model model) {
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    int userNo = loggedInUser.getEmployeeNo();

	    addBoardData(page, keyword, userNo, model);
	    return "group/QnA/qnaPage";
	}
	
	private void addBoardData(int page, String keyword, Integer employeeNo, Model model) {
	    int pageSize = 10;
	    int offset = (page - 1) * pageSize;

	    // 공통 서비스 사용
	    List<BoardPostVO> boardList = boardPostService.getBoardListWithOptionalFilter(employeeNo, keyword, offset);
	    int totalCount = boardPostService.getBoardCountWithOptionalFilter(employeeNo, keyword);
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
	
	@GetMapping("/qnaBoard/detail")
	public String getqnaBoardDetail(@RequestParam("postId") int postId, Model model) {
	    BoardPostVO boardPost = boardPostService.getBoardDetail(postId);
	    model.addAttribute("boardPost", boardPost);
	    return "group/QnA/qnaDetail"; 
	}
	
	@GetMapping("/qnaInsertPage")
	public String qnaInsertPage(Model model) {
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    int userNo = loggedInUser.getEmployeeNo();
	    model.addAttribute("userNo", userNo);
	    return "group/QnA/qnaInsert";
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
	
	@DeleteMapping("/deletePostAll/{postId}")
	public ResponseEntity<String> removeAllBoradPost(@PathVariable("postId") int postId) {
		boardPostService.removeBoradPost(postId);
	    return ResponseEntity.ok("삭제 성공");
	}
	
	@DeleteMapping("/deletePost/{postId}")
	public ResponseEntity<String> removeBoradPost(@PathVariable("postId") int postId) {
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
	
	@PostMapping("/payMentDetail")
	public ResponseEntity<List<PaymentVO>> selectAllpayMentDetail(@RequestParam int suberNo) {
		List<PaymentVO> childPost = paymentService.findAllpayMent(suberNo);
	    return ResponseEntity.ok(childPost);
	}
	
	@PostMapping("/download")
	public ResponseEntity<ByteArrayResource> createReceiptFromData(
	        @RequestParam String paymentNo,
	        @RequestParam String paymentDate,
	        @RequestParam String paymentPrice,
	        @RequestParam String paymentType,
	        @RequestParam String paymentStatus,
	        @RequestParam String subPeriod
	) {
	    try {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        Document document = new Document(PageSize.A6, 30, 30, 30, 30);
	        PdfWriter.getInstance(document, baos);
	        document.open();

	        BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/malgun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
	        Font font = new Font(bf, 10);
	        Font boldFont = new Font(bf, 10, Font.BOLD);
	        Font titleFont = new Font(bf, 16, Font.BOLD);

	        Paragraph title = new Paragraph("결제 영수증", titleFont);
	        title.setAlignment(Element.ALIGN_CENTER);
	        document.add(title);
	        document.add(Chunk.NEWLINE);

	        PdfPTable table = new PdfPTable(2);
	        table.setWidthPercentage(100);
	        table.setWidths(new int[]{1, 2});

	        table.addCell(new PdfPCell(new Paragraph("결제번호", boldFont)));
	        table.addCell(new PdfPCell(new Paragraph(paymentNo, font)));

	        table.addCell(new PdfPCell(new Paragraph("결제일자", boldFont)));
	        table.addCell(new PdfPCell(new Paragraph(paymentDate, font)));

	        table.addCell(new PdfPCell(new Paragraph("결제금액", boldFont)));
	        table.addCell(new PdfPCell(new Paragraph(paymentPrice + "원", font)));

	        table.addCell(new PdfPCell(new Paragraph("결제수단", boldFont)));
	        table.addCell(new PdfPCell(new Paragraph(paymentType, font)));

	        table.addCell(new PdfPCell(new Paragraph("결제상태", boldFont)));
	        table.addCell(new PdfPCell(new Paragraph(paymentStatus, font)));

	        table.addCell(new PdfPCell(new Paragraph("구독개월", boldFont)));
	        table.addCell(new PdfPCell(new Paragraph(subPeriod + "개월", font)));

	        document.add(table);
	        document.add(Chunk.NEWLINE);

	        Paragraph thankYou = new Paragraph("이용해 주셔서 감사합니다!", font);
	        thankYou.setAlignment(Element.ALIGN_CENTER);
	        document.add(thankYou);

	        document.close();

	        ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_PDF);
	        headers.setContentDisposition(
	                ContentDisposition.attachment()
	                        .filename("receipt_" + paymentNo + ".pdf", StandardCharsets.UTF_8)
	                        .build()
	        );

	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(resource);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	//아이디 중복 검사
	@GetMapping("/checkDuplicateId")
	@ResponseBody
	public boolean checkDuplicateId(@RequestParam String subId) {
	    return subscriberService.isSubIdExists(subId);
	}
	
	//개인 ip 등록
	@PostMapping("/registerTempIp")
	@ResponseBody
	public ResponseEntity<String> registerTempIp(@RequestBody Map<String, String> map) {
	    String tempIp = map.get("tempIp");
	    String employeeId = map.get("employeeId");
	    int result = subscriberService.insertTempIp(tempIp,employeeId);
	    
	    if (result > 0) {
	        return ResponseEntity.ok("등록 완료");
	    } else {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 실패");
	    }
	}

	@GetMapping("/alerts")
	@ResponseBody
	public List<AlarmVO> getUnreadAlarms(@RequestParam("employeeNo") int employeeNo) {
	    return alarmService.getUnreadAlarmsByEmployeeNo(employeeNo);
	}
	
	@PostMapping("/alerts/markAsRead")
	@ResponseBody
	public ResponseEntity<Map<String, String>> markAsRead(@RequestParam("alertNo") int alertNo) {
	    alarmService.markAsRead(alertNo); 
	    Map<String, String> response = new HashMap<>();
	    response.put("message", "알림 읽음 처리 완료");

	    return ResponseEntity.ok(response); // JSON 응답 반환
	}
	
	@PostMapping("/insertAlarm")
	@ResponseBody
	public String insertAlarm(@RequestBody AlarmVO vo) {
	    alarmService.insertAlarm(vo);
	    return "success";
	}
}
