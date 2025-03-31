package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.DownloadVO;
import com.yedam.app.group.service.RepositoryFileVO;

@Mapper
public interface FileMapper {
	
	// 파일 등록
    int insertFile(RepositoryFileVO fileVO);
    
    List<RepositoryFileVO> selectFilesByWritingId(Long writingId);
    
    // 파일 단건 조회 (다운로드용)
    RepositoryFileVO getFile(Long fileId);

    // 다운로드 기록 등록 (삽입된 행의 수 반환)
    void insertDownloadLog(DownloadVO downloadVO);
    
    // 파일 ID로 단건 조회 (다운로드용)
    RepositoryFileVO selectFileById(Long fileId);
    
    void deleteFilesByWritingId(Long writingId);
    
    void deleteDownloadLogByWritingId(Long writingId);
    
    void deleteFileById(Long fileId);
}
