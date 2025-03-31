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

import com.yedam.app.group.service.BoardAttachmentVO;
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
        EmpVO loginUser = empService.getLoggedInUserInfo();

        String clientIp = getClientPublicIp();
        String firstIp = empService.getFirstIpByEmployeeNo(loginUser.getSuberNo());
        String secondIp = empService.getSecondIpByEmployeeNo(loginUser.getSuberNo());
        String tempIp = loginUser.getTempIp();

        if (!(clientIp != null &&
              (clientIp.equals(firstIp) || clientIp.equals(secondIp) || clientIp.equals(tempIp)))) {
            throw new AccessDeniedException("허용되지 않은 IP입니다.");
        }

        List<RepositoryFileVO> fileList = fileService.getFilesByWritingId(writingId);
        if (fileList == null || fileList.isEmpty()) {
            throw new RuntimeException("첨부파일이 없습니다.");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (RepositoryFileVO file : fileList) {
                try {
                    String decryptedPath = isBase64(file.getFilePath()) ? AESUtil.decrypt(file.getFilePath()) : file.getFilePath();
                    String originalFileName = file.getFileName();

                    Path path = Paths.get(decryptedPath);
                    if (!Files.exists(path)) {
                        continue;
                    }

                    long fileSize = Files.size(path);

                    zos.putNextEntry(new ZipEntry(originalFileName));
                    Files.copy(path, zos);
                    zos.closeEntry();

                    // 다운로드 로그 저장
                    DownloadVO log = new DownloadVO();
                    log.setFileId((long) file.getFileId());
                    log.setEmployeeNo(loginUser.getEmployeeNo());
                    log.setDownloadDate(new Timestamp(System.currentTimeMillis()));
                    log.setIp(clientIp);

                    fileService.insertDownloadLog(log);
                } catch (Exception e) {
                    e.printStackTrace(); // 디버깅 로그
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("ZIP 파일 생성 실패", e);
        }

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
    
    @GetMapping("/boardFile/{attachmentId}")
    public ResponseEntity<Resource> downloadBoardFile(@PathVariable int attachmentId) {
        // 게시판 파일은 암호화 안되어있으므로 그대로 사용
        BoardAttachmentVO attach = fileService.getBoardAttachmentById(attachmentId); // 새로 만들기
        
        String uploadRoot = "D:/upload_files";
        Path path = Paths.get(attach.getFilePath());
        
        Resource resource = new FileSystemResource(path);
        if (!resource.exists()) {
            throw new RuntimeException("파일이 존재하지 않습니다.");
        }

        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(attach.getFileTitle(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            encodedFileName = attach.getFileTitle();
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }
    
    @GetMapping("/boardFile/zip/{postId}")
    public ResponseEntity<Resource> downloadBoardFilesAsZip(@PathVariable int postId) {
        EmpVO loginUser = empService.getLoggedInUserInfo();
        String clientIp = getClientPublicIp();

        // IP 인증
        String firstIp = empService.getFirstIpByEmployeeNo(loginUser.getSuberNo());
        String secondIp = empService.getSecondIpByEmployeeNo(loginUser.getSuberNo());
        String tempIp = loginUser.getTempIp();
        if (!(clientIp != null &&
              (clientIp.equals(firstIp) || clientIp.equals(secondIp) || clientIp.equals(tempIp)))) {
            throw new AccessDeniedException("허용되지 않은 IP입니다.");
        }

        List<BoardAttachmentVO> fileList = fileService.getBoardAttachmentsByPostId(postId);
        if (fileList == null || fileList.isEmpty()) {
            throw new RuntimeException("첨부파일이 없습니다.");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (BoardAttachmentVO file : fileList) {
                try {
                    Path path = Paths.get(file.getFilePath());
                    if (!Files.exists(path)) continue;

                    // 💡 중복 방지를 위해 파일명 앞에 ID 추가
                    String safeFileName = file.getAttachmentId() + "_" + file.getFileTitle();
                    zos.putNextEntry(new ZipEntry(safeFileName));
                    Files.copy(path, zos);
                    zos.closeEntry();

                    // 로그 저장 (에러 나도 무시하고 계속 진행)
                    try {
                        DownloadVO log = new DownloadVO();
                        log.setFileId((long) file.getAttachmentId());
                        log.setEmployeeNo(loginUser.getEmployeeNo());
                        log.setDownloadDate(new Timestamp(System.currentTimeMillis()));
                        log.setIp(clientIp);
                        fileService.insertDownloadLog(log);
                    } catch (Exception logEx) {
                        System.out.println("다운로드 로그 저장 실패: " + logEx.getMessage());
                    }

                } catch (Exception fileEx) {
                    System.out.println("파일 압축 실패: " + fileEx.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("ZIP 파일 생성 실패", e);
        }

        ByteArrayResource zipResource = new ByteArrayResource(baos.toByteArray());
        String zipName = "게시글파일_" + postId + ".zip";

        String encodedZipName;
        try {
            encodedZipName = URLEncoder.encode(zipName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            encodedZipName = "download.zip";
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedZipName)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(zipResource);
    }
    
    @GetMapping("/view/pdf/{attachmentId}")
    public ResponseEntity<Resource> previewPdf(@PathVariable int attachmentId) {
        // 첨부파일 정보 조회
        BoardAttachmentVO file = fileService.getBoardAttachmentById(attachmentId);

        // 파일 경로 확인
        if (file == null || file.getFilePath() == null) {
            throw new RuntimeException("파일 정보가 존재하지 않습니다.");
        }

        Path path = Paths.get(file.getFilePath());
        Resource resource = new FileSystemResource(path);

        if (!resource.exists()) {
            throw new RuntimeException("파일이 존재하지 않습니다.");
        }

        // 파일명이 .pdf가 아닐 경우 예외처리 (보안/UX용)
        String fileName = file.getFileTitle();
        if (!fileName.toLowerCase().endsWith(".pdf")) {
            throw new RuntimeException("PDF 파일만 미리보기가 가능합니다.");
        }

        String encodedName;
        try {
            encodedName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            encodedName = fileName;
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename*=UTF-8''" + encodedName)
            .contentType(MediaType.APPLICATION_PDF)
            .body(resource);
    }
}
