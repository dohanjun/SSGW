package com.yedam.app.group.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	// 파일 업로드 (게시글 ID와 연결)
    void insertFile(Long writingId, MultipartFile file);
    
    RepositoryFileVO getFile(Long fileId); // 파일 1개 정보 조회

    void insertDownloadLog(DownloadVO download); // 다운로드 로그 저장
    
    List<RepositoryFileVO> getFilesByWritingId(Long writingId);
    
    void deleteFilesByWritingId(Long writingId);
    
    void deleteDownloadLogByWritingId(Long writingId);
    
    void backupFilesByWritingId(Long writingId);
    
    void restoreFilesByWritingId(Long writingId);
    
    // 게사판 파일 각각 다운로드
    BoardAttachmentVO getBoardAttachmentById(int attachmentId);
    
    // 게시판 파일 전체 다운로드
    List<BoardAttachmentVO> getBoardAttachmentsByPostId(int postId);
}
