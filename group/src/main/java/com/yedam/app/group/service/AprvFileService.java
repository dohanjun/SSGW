package com.yedam.app.group.service;

import org.springframework.web.multipart.MultipartFile;

public interface AprvFileService {
	
	public void insertFiles(int draftNo, MultipartFile[] files);
}
