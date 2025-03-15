package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.ApprovalVO;

public interface ApprovalMapper {
	
	// 전체조회(결재대기함)
	public List<ApprovalVO> selectAprvList();
	
	// 결재대기함(검색)
	List<ApprovalVO> searchAprvList(ApprovalVO aprvVO);
	
	// 기본양식불러오기(기안문작성페이지)
	public ApprovalVO selectBasicsForm(ApprovalVO aprvVO);
	
	// 도장등록
	public int insertStamp(ApprovalVO aprvVO);
	
}
