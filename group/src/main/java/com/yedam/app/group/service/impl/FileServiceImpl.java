package com.yedam.app.group.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.mapper.FileMapper;
import com.yedam.app.group.service.FileService;
import com.yedam.app.group.service.RepositoryFileVO;
import com.yedam.app.utill.AESUtil;

import jakarta.annotation.PostConstruct;

@Service
public class FileServiceImpl implements FileService {

	private final FileMapper fileMapper;

    @Value("${file.upload-dir}")  
    private String uploadDir;  // 현재 프로젝트 안에서 저장하는 폴더 설정

    @PostConstruct
    public void init() {
        // 최상위 'uploads' 폴더 생성만 처리
        File directory = new File(uploadDir);
        
        // 절대 경로 출력해서 확인 (디버깅용)
        System.out.println("현재 설정된 파일 업로드 경로: " + directory.getAbsolutePath());
        
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("파일 저장 기본 폴더 생성됨: " + directory.getAbsolutePath());
            } else {
                System.out.println("파일 저장 기본 폴더 생성 실패: " + directory.getAbsolutePath());
            }
        }
    }
  
    public FileServiceImpl(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }
    
    // 게시글 ID(writingId)별 폴더 자동 생성 (중복 방지)
    private String getPostFolderPath(Long writingId) {
        String postFolderPath = Paths.get(uploadDir, String.valueOf(writingId)).toString(); 
        File directory = new File(postFolderPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("자료글 폴더 생성됨: " + directory.getAbsolutePath());
            } else {
                throw new RuntimeException("자료글 폴더 생성 실패: " + directory.getAbsolutePath());
            }
        }
        return postFolderPath;
    }

    @Override
    public void insertFile(Long writingId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        try {
            // 저장할 폴더 경로 가져오기
            String postFolderPath = getPostFolderPath(writingId);
            
            // 저장할 파일명 (UUID 사용)
            String savedFileName = UUID.randomUUID().toString() + "." + getFileExtension(file.getOriginalFilename());

            // 저장할 경로 지정 (writingId별 폴더 안에 저장)
            File targetFile = new File(postFolderPath, savedFileName);
            file.transferTo(targetFile);

            // AES 암호화 오류 방지 코드 추가
            String encryptedFileName = (savedFileName != null && !savedFileName.isEmpty()) 
                                       ? AESUtil.encrypt(savedFileName) : null;
            String encryptedFilePath = (targetFile.getAbsolutePath() != null && !targetFile.getAbsolutePath().isEmpty()) 
                                       ? AESUtil.encrypt(targetFile.getAbsolutePath()) : null;

            // 암호화 결과 확인
            if (encryptedFileName == null || encryptedFilePath == null) {
                throw new RuntimeException("AES 암호화 후 파일 정보가 null입니다.");
            }

            // 파일 정보 설정 및 암호화된 값 저장
            RepositoryFileVO fileVO = new RepositoryFileVO();
            fileVO.setWritingId(writingId);
            fileVO.setFileName(file.getOriginalFilename());
            fileVO.setSaveFileName(encryptedFileName);
            fileVO.setFilePath(encryptedFilePath);
            fileVO.setFileSize(file.getSize());
            fileVO.setFileExtension(getFileExtension(file.getOriginalFilename()));
            fileVO.setCreationDate(new Timestamp(System.currentTimeMillis())); // 업로드 날짜 설정

            fileMapper.insertFile(fileVO);
            System.out.println("파일 등록 완료 - 파일명: " + fileVO.getFileName() + ", 저장 경로: " + postFolderPath);
            
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        } catch (Exception e) {
            throw new RuntimeException("AES 암호화 중 오류 발생", e);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return ""; // 확장자가 없는 경우 빈 문자열 반환
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    
    @Override
    public List<RepositoryFileVO> getFilesByWritingId(Long writingId) {
        return fileMapper.selectFilesByWritingId(writingId);
    }
}
