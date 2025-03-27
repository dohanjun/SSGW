package com.yedam.app.group.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.MailService;
import com.yedam.app.group.service.MailVO;
import com.yedam.app.group.service.PageListVO;
import com.yedam.app.group.service.Paging;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
public class MailController {

    private static final String UPLOAD_DIR = "uploads/"; // 업로드 디렉토리
    private final MailService mailService;
    private final EmpService empService;

    // 생성자 주입 방식
    public MailController(MailService mailService, EmpService empService) {
        this.mailService = mailService;
        this.empService = empService;
    }


	// 메일목록
	@GetMapping("/mail")
	public String mail(Model model, PageListVO vo, Paging paging) {

		// 페이징처리
		vo.setStart(paging.getFirst());
		vo.setEnd(paging.getLast());
		paging.setTotalRecord(mailService.pageGetCount(vo));
		model.addAttribute("paging", paging);
		
		List<MailVO> list = mailService.MailSelectAll(vo);
		model.addAttribute("mails", list);

		return "group/mail/mail";
	}

	// 메일상세보기
	@GetMapping("/mailSelect")
	public String mailSelect(MailVO mailVO, Model model) {
	    // 로그인한 사용자 정보 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    mailVO.setEmployeeId(loggedInUser.getEmployeeId());

	    // 메일 조회
	    MailVO findVO = mailService.MailSelectInfo(mailVO);
	    model.addAttribute("mail", findVO);

	    // 첨부파일 다운로드 URL 생성 (첨부파일이 있을 경우)
	    if (findVO.getAttachedFileName() != null && !findVO.getAttachedFileName().isEmpty()) {
	        model.addAttribute("downloadUrl", "/download/" + findVO.getAttachedFileName());
	    }

	    // "group/mail/mailSelect" 템플릿 반환
	    return "group/mail/mailSelect";
	}

	// 내 메일상세보기
	@GetMapping("/myMailSelect")
	public String myMailSelect(MailVO mailVO, Model model) {
	    try {
	        // 로그인된 사용자 정보 가져오기
	        EmpVO loggedInUser = empService.getLoggedInUserInfo();
	        mailVO.setEmployeeId(loggedInUser.getEmployeeId());

	        // 메일 정보 조회
	        MailVO findVO = mailService.MyMailSelectInfo(mailVO);
	        model.addAttribute("mail", findVO);

	        return "group/mail/myMailSelect";  // 메일 상세 페이지로 이동
	    } catch (Exception e) {
	        model.addAttribute("error", "메일 상세 정보를 가져오는 중 오류가 발생했습니다.");
	        return "group/mail/myMailSelect";  // 오류 발생 시 메일 상세 화면으로 이동
	    }
	}

	// 파일 다운로드 처리
	@GetMapping("/download-mail/{filename}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
	    try {
	        // 다운로드할 파일의 경로 설정
	        Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
	        File file = filePath.toFile();

	        // 파일이 존재하지 않으면 404 반환
	        if (!file.exists()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	        // 파일을 Resource로 변환 (FileSystemResource로 생성)
	        Resource resource = (Resource) new FileSystemResource(file);

	        // 파일을 다운로드하도록 헤더 설정
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
	                .body(resource);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}

    
    
	// 등록 - 페이지
	@GetMapping("/mailInsert")
	public String InsertMailForm() {
		return "group/mail/mailInsert";
	}


	// 메일등록
	@PostMapping("/mailInsert")
	public String insertMail(MailVO vo, @RequestParam("file") MultipartFile file) {
	    try {
	        // 파일 업로드 처리
	        String filename = StringUtils.cleanPath(file.getOriginalFilename()); // 파일 이름 정리
	        Path targetLocation = Paths.get(UPLOAD_DIR + filename); // 저장할 경로

	        // 디렉토리가 없으면 생성
	        File dir = new File(UPLOAD_DIR);
	        if (!dir.exists()) {
	            dir.mkdirs();
	        }

	        // 파일을 지정된 경로에 복사
	        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	        
	        // 업로드된 파일 경로를 MailVO에 포함 (필요한 경우)
	        vo.setAttachedFileName(filename); // 메일 VO에 파일 이름을 설정

	    } catch (IOException e) {
	        // 파일 업로드 실패 시 처리
	        return "redirect:/error"; // 오류 페이지로 리다이렉트 (필요 시)
	    }

	    // 로그인한 사용자 정보 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    vo.setEmployeeId(loggedInUser.getEmployeeId());

	    // 메일 발송
	    mailService.sendMailToUser(vo);

	    // 메일 발송 후 mail 목록 페이지로 리다이렉트
	    return "redirect:mail"; // 메일 목록 페이지로 리다이렉트 (올바른 경로)
	}


	// 수정 - 페이지
	@GetMapping("/mailUpdate")
	public String mailUpdate(MailVO mailVO, Model model) {
		MailVO findVO = mailService.MailSelectInfo(mailVO);
		model.addAttribute("mail", findVO);
		return "group/mail/mailUpdate";
	}


	// 메일수정처리
	@PostMapping("/mailUpdate")
	@ResponseBody
	public Map<String, Object> mailUpdateAJAXJSON(@RequestBody MailVO mailVO) {
		return mailService.MailUpdate(mailVO);
	}

	// 메일답장 페이지
	@GetMapping("/mailReply")
	public String mailReplyForm(MailVO mailVO, Model model) {
		MailVO findVO = mailService.MailSelectInfo(mailVO);
		model.addAttribute("mail", findVO);
		return "group/mail/mailReply";
	}

	// 메일답장처리
	@PostMapping("mailReply")
	public String mailReplyProcess(MailVO vo) {
		// 메일 발송
	    mailService.sendMailToUser(vo);
	    
	    return "redirect:mail";
	}

	// 전달 - 페이지
	@GetMapping("/mailVery")
	public String mailVery(MailVO mailVO, Model model) {
		MailVO findVO = mailService.MailSelectInfo(mailVO);
		model.addAttribute("mail", findVO);
		return "group/mail/mailVery";
	}


	// 메일전달처리
	@PostMapping("mailVery")
	@ResponseBody
	public Map<String, Object> mailVeryAJAXJSON(@RequestBody MailVO mailVO) {
		return mailService.MailVeryInfo(mailVO);
	}

	// 메일삭제
	@GetMapping("mailDelete")
	public String mailDelete(Integer mailId) {
		mailService.MailDelete(mailId);
		return "redirect:mail";
	}

	
//세부메일함

	// 받은메일함
	@GetMapping("getMail")
	public String getMail(Model model, PageListVO vo, Paging paging) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setMailType("받은");
		vo.setGetUser(loggedInUser.getEmployeeId());
		// 페이징처리
		vo.setStart(paging.getFirst());
		vo.setEnd(paging.getLast());
		paging.setTotalRecord(mailService.pageGetCount(vo));
		model.addAttribute("paging", paging);

		List<MailVO> list = mailService.MailSelectAll(vo);
		model.addAttribute("mails", list);
		return "group/mail/getMail";
	}

	// 보낸메일함
	@GetMapping("putMail")
	public String putMail(Model model, PageListVO vo, Paging paging) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setMailType("보낸");
		vo.setEmployeeId(loggedInUser.getEmployeeId());
		// 페이징처리loggedInUser
		vo.setStart(paging.getFirst());
		vo.setEnd(paging.getLast());
		paging.setTotalRecord(mailService.pageGetCount(vo));
		model.addAttribute("paging", paging);

		List<MailVO> list = mailService.MailSelectAll(vo);
		model.addAttribute("mails", list);
		return "group/mail/putMail"; // mainPage.html을 반환
	}

	// 임시메일함 -> 7일뒤 삭제
	@GetMapping("temporaryMail")
	public String temporaryMail(Model model, PageListVO vo, Paging paging) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setMailType("임시");
		vo.setEmployeeId(loggedInUser.getEmployeeId());
		// 페이징처리
		vo.setStart(paging.getFirst());
		vo.setEnd(paging.getLast());
		paging.setTotalRecord(mailService.pageGetCount(vo));
		model.addAttribute("paging", paging);

		List<MailVO> list = mailService.MailSelectAll(vo);
		model.addAttribute("mails", list);
		return "group/mail/temporaryMail";
	}

	// 휴지통 -> 30일 뒤 삭제
	@GetMapping("deleteMail")
	public String deleteMail(Model model, PageListVO vo, Paging paging) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setMailType("휴지통");
		vo.setEmployeeId(loggedInUser.getEmployeeId());
		// 페이징처리
		vo.setStart(paging.getFirst());
		vo.setEnd(paging.getLast());
		paging.setTotalRecord(mailService.pageGetCount(vo));
		model.addAttribute("paging", paging);

		List<MailVO> list = mailService.MailSelectAll(vo);
		model.addAttribute("mails", list);
		return "group/mail/deleteMail";
	}
	
	 // 메일 검색 폼
    @GetMapping("/searchMailForm")
    public String showSearchForm(HttpSession session, Model model) {
        // 세션에서 기존 검색 조건을 가져옵니다.
        MailVO searchCriteria = (MailVO) session.getAttribute("searchCriteria");

        // 검색 조건이 있으면 모델에 추가
        if (searchCriteria != null) {
            model.addAttribute("searchCriteria", searchCriteria);
        }

        return "group/mail/mailSearch";
    }

    // 검색 처리
    @GetMapping("/searchMail")
    public String searchMail(MailVO mailVO, HttpSession session, Model model) {
        // 검색 조건을 세션에 저장
        session.setAttribute("searchCriteria", mailVO);

        // 메일 목록 조회
        List<MailVO> mailList = mailService.searchMails(mailVO);

        // 검색 결과를 모델에 추가
        model.addAttribute("mailList", mailList);

        return "group/mail/mailSearchResults";
    }
    
}