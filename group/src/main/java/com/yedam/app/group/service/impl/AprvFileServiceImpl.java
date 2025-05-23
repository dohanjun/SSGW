package com.yedam.app.group.service.impl;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.mapper.AprvFileMapper;
import com.yedam.app.group.service.AprvFileVO;
import com.yedam.app.group.service.AprvFileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AprvFileServiceImpl implements AprvFileService {
	
	@Value("${file.upload-dir}")
    private String uploadDir;
	
	private final AprvFileMapper aprvFileMapper;
	
	@Override
	public void insertFiles(int draftNo, MultipartFile[] files) {
	    if (files == null || files.length == 0) return;

	    String draftPath = Paths.get(uploadDir, String.valueOf(draftNo)).toString();
	    File dir = new File(draftPath);
	    if (!dir.exists()) dir.mkdirs();

	    int order = 1;
	    for (MultipartFile file : files) {
	        if (file.isEmpty()) continue;

	        try {
	            String originalFileName = file.getOriginalFilename();
	            String saveName = UUID.randomUUID().toString() + "_" + originalFileName;
	            File dest = new File(draftPath, saveName);
	            file.transferTo(dest);

	            // DB 저장용 웹 경로 생성
	            String fileDbPath = "/uploads/" + draftNo + "/" + saveName;

	            AprvFileVO fileVO = new AprvFileVO();
	            fileVO.setDraftNo(draftNo);
	            fileVO.setFileName(originalFileName);
	            fileVO.setFilePath(fileDbPath); //
	            fileVO.setFileSize(file.getSize());
	            fileVO.setFileOrder(order++);

	            aprvFileMapper.insertFile(fileVO);
	        } catch (Exception e) {
	            throw new RuntimeException("파일 업로드 실패", e);
	        }
	    }
	}


	@Override
	public List<AprvFileVO> findFilesByDraftNo(int draftNo) {
		return aprvFileMapper.selectFilesByDraftNo(draftNo);
	}
	
	@Override
	public AprvFileVO findFileById(int fileId) {
	    return aprvFileMapper.selectFileById(fileId);
	}

}
