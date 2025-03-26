package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.AprvFileVO;

public interface AprvFileMapper {
	
	//
	public void insertFile(AprvFileVO fileVO);
	
	//
	public List<AprvFileVO> selectFilesByDraftNo(int draftNo);
	
	//
	public AprvFileVO selectFileById(int fileId);
	
}
