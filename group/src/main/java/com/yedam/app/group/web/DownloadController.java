package com.yedam.app.group.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yedam.app.group.service.DownloadVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.FileService;
import com.yedam.app.group.service.RepositoryFileVO;
import com.yedam.app.utill.AESUtil;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/download")
public class DownloadController {
	 private final EmpService empService;
	 private final FileService fileService;

	 @Autowired
	 public DownloadController(EmpService empService, FileService fileService) {
		 this.empService = empService;
	     this.fileService = fileService;
	 }
	 
	 	@GetMapping("/{fileId}")
	    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId,
	                                                 HttpServletRequest request) {
	        // 1. 로그인 사용자 정보
	        EmpVO loggedInUser = empService.getLoggedInUserInfo();

	        if (loggedInUser == null) {
	            throw new IllegalStateException("로그인 정보 없음");
	        }

	        // 2. 클라이언트 IP
	        String clientIp = getClientIp(request);

	        // 3. 회사 IP 2개 + 사원 예비 IP
	        String firstIp = empService.getFirstIpByEmployeeNo(loggedInUser.getSuberNo());     // suber 테이블에서
	        String secondIp = empService.getSecondIpByEmployeeNo(loggedInUser.getSuberNo());   // suber 테이블에서
	        String tempIp = loggedInUser.getTempIp();

	        // 4. IP 검증
	        if (!(clientIp.equals(firstIp) || clientIp.equals(secondIp) || clientIp.equals(tempIp))) {
	            throw new AccessDeniedException("허용되지 않은 IP입니다.");
	        }
	        

	        // 5. 파일 정보 가져오기 및 복호화
	        RepositoryFileVO fileVO = fileService.getFile(fileId);
	        String decryptedPath;
	        String decryptedSaveName;
	        
	        try {
	        	System.out.println("filePath: " + fileVO.getFilePath());
	            System.out.println("saveFileName: " + fileVO.getSaveFileName());

	            decryptedPath = isBase64(fileVO.getFilePath()) 
	                ? AESUtil.decrypt(fileVO.getFilePath()) 
	                : fileVO.getFilePath();

	            decryptedSaveName = isBase64(fileVO.getSaveFileName()) 
	                ? AESUtil.decrypt(fileVO.getSaveFileName()) 
	                : fileVO.getSaveFileName();
	        } catch (Exception e) {
	            throw new RuntimeException("파일 복호화 중 오류 발생", e);
	        }

	        // 6. 다운로드 기록 INSERT
	        DownloadVO download = new DownloadVO();
	        download.setFileId(fileId);
	        download.setEmployeeNo(loggedInUser.getEmployeeNo());
	        download.setDownloadDate(new Timestamp(System.currentTimeMillis()));
	        download.setIp(clientIp);
	        fileService.insertDownloadLog(download);

	        // 7. 파일 존재 여부 확인
	        Path filePath = Paths.get(decryptedPath);
	        Resource resource = new FileSystemResource(filePath);
	        if (!resource.exists()) {
	            throw new RuntimeException("파일이 존재하지 않습니다: " + decryptedPath);
	        }

	        // 8. 다운로드 응답 반환
	        String originalFileName = fileVO.getFileName();
	        String encodedFileName;
	        try {
	            encodedFileName = URLEncoder.encode(fileVO.getFileName(), "UTF-8").replaceAll("\\+", "%20");
	        } catch (UnsupportedEncodingException e) {
	            throw new RuntimeException("파일명 인코딩 실패", e);
	        }

	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource);
	    }
	 	
	    private boolean isBase64(String value) {
	        try {
	            Base64.getDecoder().decode(value);
	            return true;
	        } catch (IllegalArgumentException e) {
	            return false;
	        }
	    }
	 
	 	private String getClientIp(HttpServletRequest request) {
	 	    String ip = request.getHeader("X-Forwarded-For");
	 	    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
	 	        ip = request.getRemoteAddr();
	 	    }

	 	    // ::1 또는 0:0:0:0:0:0:0:1 ➜ 127.0.0.1 로 변환
	 	    if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
	 	        ip = "127.0.0.1";
	 	    }

	 	    return ip;
	 	}
	 
}
