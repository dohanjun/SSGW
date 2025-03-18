package com.yedam.app.group.service;

import java.util.List;
import java.util.Map;

public interface ApprovalService {
	
	// 결재대기함
	public List<ApprovalVO> findAllList(Integer employeeNo);
	
	// 결재대기함(검색)
	public List<ApprovalVO> searchApprovalList(ApprovalVO aprvVO);
	
	// 결재 상세 정보 조회
	public ApprovalVO findAprvInfo(ApprovalVO aprvVO);
	
	// 도장 등록
	public int createStamp(ApprovalVO aprvVO);
	
	// 도장 수정(기존 도장 active= '0')
	public Map<String, Object> modifyStramp(ApprovalVO aprvVO);
	
	// 활성화된 도장 개수 조회
	public int countActiveStamps(ApprovalVO aprvVO);  
	
	// 회사 전자결재 양식 등록
	public int createForm(ApprovalFormVO aprvformVO);
	
	

  
}
