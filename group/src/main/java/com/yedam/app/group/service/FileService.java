package com.yedam.app.group.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	// 파일 업로드 (게시글 ID와 연결)
    void insertFile(int writingId, MultipartFile file);
    
}
