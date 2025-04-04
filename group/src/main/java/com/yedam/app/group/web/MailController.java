package com.yedam.app.group.web;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.MailService;
import com.yedam.app.group.service.MailVO;
import com.yedam.app.group.service.PageListVO;
import com.yedam.app.group.service.Paging;

import jakarta.servlet.http.HttpSession;

@Controller
public class MailController {
	@Value("${file.upload-dir}")
	private String uploadDir;
	// 업로드 디렉토리
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
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setEmployeeNo(loggedInUser.getEmployeeNo());
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
	public String mailSelect(MailVO mailVO, Model model, Integer mailId) {
		// 로그인한 사용자 정보 가져오기
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		mailVO.setEmployeeNo(loggedInUser.getEmployeeNo());

		// 메일 조회
		MailVO findVO = mailService.MailSelectInfo(mailVO);
		model.addAttribute("mail", findVO);

		// 첨부파일 수량 가져오기
		MailVO fileCount = mailService.FileCount(mailVO.getMailId());// mailId를 전달하여 파일 수량을 가져옴
		model.addAttribute("fileCount", fileCount);

		// 첨부파일 다운로드 URL 생성 (첨부파일이 있을 경우)
		if (findVO.getAttachedFileName() != null && !findVO.getAttachedFileName().isEmpty()) {
			model.addAttribute("downloadUrl", "/download/" + findVO.getAttachedFileName());
		}

		// "group/mail/mailSelect" 템플릿 반환
		return "group/mail/mailSelect";
	}

	// 파일 다운로드 처리
	@GetMapping("/download-mail/{filename}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
		try {
			// 다운로드할 파일의 경로 설정
			Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
			File file = filePath.toFile();

			// 파일이 존재하지 않으면 404 반환
			if (!file.exists()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			// 파일을 Resource로 변환 (FileSystemResource로 생성)
			Resource resource = (Resource) new FileSystemResource(file);
			filename = URLEncoder.encode(filename, "UTF-8");
			// 파일을 다운로드하도록 헤더 설정
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
					.body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// 등록 - 페이지
	@GetMapping("/mailInsert")
	public String InsertMailForm(Model model) {
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		model.addAttribute("employeeId", loggedInUser.getEmployeeId());
		return "group/mail/mailInsert";
	}

	// 메일등록
	@PostMapping("/mailInsert")
	public String insertMail(MailVO vo, @RequestParam("file") MultipartFile file, Model model, @RequestParam("domain") String domain) {
		if(!file.isEmpty()) {
		try {
			// 파일 업로드 처리
			String filename = StringUtils.cleanPath(file.getOriginalFilename()); // 파일 이름 정리
			Path targetLocation = Paths.get(uploadDir + "/" + filename); // 저장할 경로

			// 디렉토리가 없으면 생성
			File dir = new File(uploadDir);
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
		}
		MailVO findVO = mailService.MailSelectInfo(vo);
		model.addAttribute("mail", findVO);
		// 로그인한 사용자 정보 가져오기
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setEmployeeNo(loggedInUser.getEmployeeNo());

		// 메일 발송
		mailService.sendMailToUser(vo);

		// 메일 발송 후 mail 목록 페이지로 리다이렉트
		return "redirect:mail"; // 메일 목록 페이지로 리다이렉트 (올바른 경로)
	}

	// 메일답장 페이지
	@GetMapping("/mailReply")
	public String mailReplyForm(MailVO mailVO, Model model) {
		
		MailVO findVO = mailService.MailSelectInfo(mailVO);
		model.addAttribute("mail", findVO);
		
		return "group/mail/mailReply";
	}

	// 메일답장 처리
	@PostMapping("/mailReply")
	public String mailReplyProcess(MailVO vo) {
		// 로그인한 사용자 정보 가져오기
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setEmployeeNo(loggedInUser.getEmployeeNo());

		mailService.sendMailToUser(vo);

		return "redirect:mail";
	}

	// 전달 - 페이지
	@GetMapping("/mailVery")
	public String mailVeryForm(MailVO mailVO, Model model) {
		// 로그인한 사용자 정보 가져오기
		MailVO findVO = mailService.MailSelectInfo(mailVO);
		model.addAttribute("mail", findVO);

		return "group/mail/mailVery";
	}

	// 메일전달처리
	@PostMapping("mailVery")
	public String mailVeryProcess(MailVO vo, Model model) {
		
		MailVO findVO = mailService.MailSelectInfo(vo);
		model.addAttribute("mail", findVO);
		// 로그인한 사용자 정보 가져오기
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setEmployeeNo(loggedInUser.getEmployeeNo());

		// 메일 발송
		mailService.sendMailToUser(vo);

		// 메일 발송 후 mail 목록 페이지로 리다이렉트
		return "redirect:mail"; // 메일 목록 페이지로 리다이렉트 (올바른 경로)
	}

	// 단건휴지통 이동
	@GetMapping("mailDelete")
	public String mailDelete(Integer mailId, MailVO mailVO) {
		
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		mailVO.setEmployeeNo(loggedInUser.getEmployeeNo());
		
		mailService.MailDel(mailId);
		return "redirect:/deleteMail";
	}

	// 여러개의 메일삭제
	@PostMapping("/mail/mailDeletes")
	public String mailDeletes(@RequestParam List<Integer> mailIds, Model model, PageListVO vo, Paging paging) {
		// 여러 메일을 삭제하도록 MailDelete 메서드 호출
		mailService.MailDels(mailIds);
		
		// 페이징처리
		vo.setStart(paging.getFirst());
		vo.setEnd(paging.getLast());
		paging.setTotalRecord(mailService.pageGetCount(vo));
		model.addAttribute("paging", paging);

		List<MailVO> list = mailService.MailSelectAll(vo);
		model.addAttribute("mails", list);
		
		return "redirect:/deleteMail";
	}

//세부메일함

	// 받은메일함
	@GetMapping("getMail")
	public String getMail(@RequestParam("domain") String domain, Model model, PageListVO vo, Paging paging) {
		
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setMailType("받은");
		vo.setGetUser(loggedInUser.getEmployeeId()+domain);
		
		System.out.print(vo.getGetUser());
		
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

		// 페이징처리
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
		vo.setEmployeeNo(loggedInUser.getEmployeeNo());
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

	
	// 임시저장
	@GetMapping("mailTem")
	public String mailTem(Integer mailId, MailVO mailVO) {
		
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		mailVO.setEmployeeNo(loggedInUser.getEmployeeNo());
		
		mailService.MailPro(mailId);
		return "redirect:/temporaryMail";
	}
	
	// 휴지통 -> 30일 뒤 삭제
	@GetMapping("deleteMail")
	public String deleteMail(Model model, PageListVO vo, Paging paging) {
		
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		vo.setMailType("휴지통");
		vo.setEmployeeNo(loggedInUser.getEmployeeNo());
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
	
	// 휴지통상세보기
	@GetMapping("/deleteSelect")
	public String deleteSelect(MailVO mailVO, Model model, Integer mailId) {
		// 로그인한 사용자 정보 가져오기
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		mailVO.setEmployeeNo(loggedInUser.getEmployeeNo());

		// 메일 조회
		MailVO findVO = mailService.MailSelectInfo(mailVO);
		model.addAttribute("mail", findVO);

		// 첨부파일 수량 가져오기
		MailVO fileCount = mailService.FileCount(mailVO.getMailId());// mailId를 전달하여 파일 수량을 가져옴
		model.addAttribute("fileCount", fileCount);

		// 첨부파일 다운로드 URL 생성 (첨부파일이 있을 경우)
		if (findVO.getAttachedFileName() != null && !findVO.getAttachedFileName().isEmpty()) {
			model.addAttribute("downloadUrl", "/download/" + findVO.getAttachedFileName());
		}

		// "group/mail/mailSelect" 템플릿 반환
		return "group/mail/deleteSelect";
	}
	
	
	// 단건메일삭제
	@GetMapping("MailRemove")
	public String MailRemove(Integer mailId, MailVO mailVO) {
		
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		mailVO.setEmployeeNo(loggedInUser.getEmployeeNo());
		
		mailService.MailRemove(mailId);
		return "redirect:/deleteMail";
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