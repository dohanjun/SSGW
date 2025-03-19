package com.yedam.app.group.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.DownloadVO;

@Mapper
public interface DownloadMapper {
	
	// 다운로드 기록 등록
    int insertDownloadRecord(DownloadVO downloadVO);
}
