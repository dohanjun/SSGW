package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.RepositoryFileVO;

@Mapper
public interface FileMapper {
	
	// 파일 등록
    int insertFile(RepositoryFileVO fileVO);
    
    List<RepositoryFileVO> selectFilesByWritingId(Long writingId);

}
