package com.yedam.app.group.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.mapper.FileMapper;
import com.yedam.app.group.service.FileService;
import com.yedam.app.group.service.RepositoryFileVO;
import com.yedam.app.utill.AESUtil;

@Service
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    @Value("${file.upload-dir}")  // 파일 저장 경로
    private String uploadDir;
    
    
    public FileServiceImpl(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }
    
    // 게시글 ID(writingId)별 폴더 자동 생성
    private String getPostFolderPath(int writingId) {
        String postFolderPath = uploadDir + File.separator + writingId; // C:/upload_files/5/
        File directory = new File(postFolderPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("📂 게시글 폴더 생성됨: " + directory.getAbsolutePath());
            } else {
                throw new RuntimeException("⚠ 게시글 폴더 생성 실패: " + directory.getAbsolutePath());
            }
        }
        return postFolderPath;
    }

    @Override
    public void insertFile(int writingId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다.");
        }

        try {
            // 저장할 파일명 (UUID 사용)
            String savedFileName = UUID.randomUUID().toString() + "." + getFileExtension(file.getOriginalFilename());
            // 저장할 경로 지정
            File targetFile = new File(uploadDir, savedFileName);
            file.transferTo(targetFile);
            
         // AES 암호화 오류 방지 코드 추가
            String encryptedFileName = (savedFileName != null && !savedFileName.isEmpty()) 
                                       ? AESUtil.encrypt(savedFileName) : null;
            String encryptedFilePath = (targetFile.getAbsolutePath() != null && !targetFile.getAbsolutePath().isEmpty()) 
                                       ? AESUtil.encrypt(targetFile.getAbsolutePath()) : null;

            // 파일 정보 설정 및 암호화
            RepositoryFileVO fileVO = new RepositoryFileVO();
            fileVO.setWritingId(writingId);
            fileVO.setFileName(file.getOriginalFilename());
            fileVO.setSaveFileName(AESUtil.encrypt(savedFileName));
            fileVO.setFilePath(AESUtil.encrypt(targetFile.getAbsolutePath()));
            fileVO.setFileSize(file.getSize());
            fileVO.setFileExtension(getFileExtension(file.getOriginalFilename()));

            fileMapper.insertFile(fileVO);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        } catch (Exception e) {
            throw new RuntimeException("AES 암호화 중 오류 발생", e);
        }
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
