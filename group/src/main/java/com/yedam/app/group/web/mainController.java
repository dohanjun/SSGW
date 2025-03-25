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
import com.yedam.app.group.service.PaymentService;
import com.yedam.app.group.service.PaymentVO;
import com.yedam.app.group.service.SubscriberService;
import com.yedam.app.group.service.SubscriberVO;
import com.yedam.app.group.service.SubscriptionSummaryVO;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.Element;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.http.HttpSession;
import java.util.Map;


/** 외부페이지 컨트롤
 * @author 조성민
 * @since 2025-03-20
 * <pre>
 * <pre>
 * 수정일자      수정자    수정내용
 * --------------------------------
 * 2025-03-24  조성민
 * </pre>
 * */


@Controller
@RequiredArgsConstructor
public class mainController {
	
	private final ModuleService moduleService;
	private final BoardPostService boardPostService;
	private final SubscriberService subscriberService;
	private final PaymentService paymentService;
	
	
	
	/**
	 * 메인페이지로 이동
	 * @return group/mainPage
	 */
	@GetMapping("main")
	public String mainPage(HttpSession session, Model model) {
	    Map<String, Object> userDepInfo = (Map<String, Object>) session.getAttribute("loginUserDepInfo");
	    model.addAttribute("userDepInfo", userDepInfo);
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


}
