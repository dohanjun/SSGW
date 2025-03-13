package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.ApprovalVO;

public interface ApprovalMapper {
	
	// 전체조회(결재대기함)
	public List<ApprovalVO> selectAprvList();
	
	// 도장등록
	public int insertStamp(ApprovalVO aprvVO);
	
}
