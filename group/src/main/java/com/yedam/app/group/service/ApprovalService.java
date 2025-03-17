package com.yedam.app.group.service;

import java.util.List;

public interface ApprovalService {
	
	// 결재대기함
	public List<ApprovalVO> findAllList();
	
	// 결재대기함(검색)
	public List<ApprovalVO> searchApprovalList(ApprovalVO aprvVO);
	
	// 결재 상세 정보 조회
	public ApprovalVO findAprvInfo(ApprovalVO aprvVO);
	
	// 도장 등록
	public int createStamp(ApprovalVO aprvVO);
	
	// 양식 불러오기
	public ApprovalVO findBaicsForm(ApprovalVO aprvVO);

	// 수정: 도장 이미지 저장
	void saveStampImage(int employeeNo, String stampImgPath);
 

	// 수정: 도장 이미지 조회
	String getStampImage(int employeeNo); 
}
