package com.yedam.app.group.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface AprvFileService {
	
	public void insertFiles(int draftNo, MultipartFile[] files);
	
	public List<AprvFileVO> findFilesByDraftNo(int draftNo);
	
	public AprvFileVO findFileById(int fileId);
}
