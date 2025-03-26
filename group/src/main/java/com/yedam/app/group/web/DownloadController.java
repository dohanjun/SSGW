package com.yedam.app.group.web;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.core.io.ByteArrayResource;
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
import org.springframework.web.client.RestTemplate;

import com.yedam.app.group.service.DownloadVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.FileService;
import com.yedam.app.group.service.RepositoryFileVO;
import com.yedam.app.utill.AESUtil;

@Controller
@RequestMapping("/download")
public class DownloadController {
	private final EmpService empService;
	private final FileService fileService;

	public DownloadController(EmpService empService, FileService fileService) {
		this.empService = empService;
		this.fileService = fileService;
	}

	@GetMapping("/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
		// 1. 로그인 사용자 정보
        EmpVO loggedInUser = empService.getLoggedInUserInfo();

        // 2. 공인 IP 조회 (ipinfo.io)
        String clientIp = getClientPublicIp();

        // 3. DB에 저장된 회사/사원 허용 IP 정보 조회
        String firstIp = empService.getFirstIpByEmployeeNo(loggedInUser.getSuberNo());
        String secondIp = empService.getSecondIpByEmployeeNo(loggedInUser.getSuberNo());
        String tempIp = loggedInUser.getTempIp();

        // 4. IP 인증
        if (!(clientIp != null &&
              (clientIp.equals(firstIp) || clientIp.equals(secondIp) || clientIp.equals(tempIp)))) {
            throw new AccessDeniedException("허용되지 않은 IP입니다.");
        }

        // 5. 파일 정보 가져오기 및 복호화
        RepositoryFileVO fileVO = fileService.getFile(fileId);
        String decryptedPath;
        String decryptedSaveName;

        try {
            decryptedPath = isBase64(fileVO.getFilePath()) ? AESUtil.decrypt(fileVO.getFilePath()) : fileVO.getFilePath();
            decryptedSaveName = isBase64(fileVO.getSaveFileName()) ? AESUtil.decrypt(fileVO.getSaveFileName()) : fileVO.getSaveFileName();
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

        // 7. 실제 파일 경로 확인
        Path filePath = Paths.get(decryptedPath);
        Resource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            throw new RuntimeException("파일이 존재하지 않습니다: " + decryptedPath);
        }

        // 8. 파일 다운로드 응답
        String originalFileName = fileVO.getFileName();
        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(originalFileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("파일명 인코딩 실패", e);
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

    // 공인 IP 가져오기
    private String getClientPublicIp() {
        try {
            RestTemplate rtm = new RestTemplate();
            Map<String, Object> map = rtm.getForObject("https://ipinfo.io/json", Map.class);
            if (map != null && map.get("ip") != null) {
                return map.get("ip").toString();
            }
        } catch (Exception e) {
            System.out.println("ipinfo.io 호출 실패: " + e.getMessage());
        }
        return null;
    }

    // Base64 확인용
    private boolean isBase64(String value) {
        try {
            Base64.getDecoder().decode(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    @GetMapping("/zip/{writingId}")
    public ResponseEntity<Resource> downloadAllFilesAsZip(@PathVariable Long writingId) {
        // 1. 로그인 사용자 확인
        EmpVO loginUser = empService.getLoggedInUserInfo();

        // 2. 공인 IP 가져오기
        String clientIp = getClientPublicIp();
        String firstIp = empService.getFirstIpByEmployeeNo(loginUser.getSuberNo());
        String secondIp = empService.getSecondIpByEmployeeNo(loginUser.getSuberNo());
        String tempIp = loginUser.getTempIp();

        // 3. IP 인증
        if (!(clientIp != null &&
              (clientIp.equals(firstIp) || clientIp.equals(secondIp) || clientIp.equals(tempIp)))) {
            throw new AccessDeniedException("허용되지 않은 IP입니다.");
        }

        // 4. 파일 목록 조회
        List<RepositoryFileVO> fileList = fileService.getFilesByWritingId(writingId);
        if (fileList == null || fileList.isEmpty()) {
            throw new RuntimeException("첨부파일이 없습니다.");
        }

        // 5. ZIP 파일 생성
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (RepositoryFileVO file : fileList) {
                String decryptedPath = isBase64(file.getFilePath()) ? AESUtil.decrypt(file.getFilePath()) : file.getFilePath();
                String originalFileName = file.getFileName();

                Path path = Paths.get(decryptedPath);
                if (!Files.exists(path)) continue;

                zos.putNextEntry(new ZipEntry(originalFileName));
                Files.copy(path, zos);
                zos.closeEntry();

                // 6. 다운로드 로그 저장
                DownloadVO log = new DownloadVO();
                log.setFileId((long) file.getFileId());  // <- 여기 오류 수정 (fileId → file.getFileId())
                log.setEmployeeNo(loginUser.getEmployeeNo());
                log.setDownloadDate(new Timestamp(System.currentTimeMillis()));
                log.setIp(clientIp);

                fileService.insertDownloadLog(log);  // <- 잘못된 위치 수정
            }
        } catch (Exception e) {
            throw new RuntimeException("ZIP 파일 생성 실패", e);
        }

        // 7. 파일 응답 반환
        ByteArrayResource zipResource = new ByteArrayResource(baos.toByteArray());
        String zipName = "files_" + writingId + ".zip";

        String encodedZipName;
        try {
            encodedZipName = URLEncoder.encode(zipName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            encodedZipName = zipName;
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedZipName)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(zipResource);
    }
}
