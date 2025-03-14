package com.yedam.app.group.service;

import java.util.List;

public interface ApprovalService {
	
	// 결재대기함
	public List<ApprovalVO> findAllList();
	
	// 도장등록
	public int createStamp(ApprovalVO aprvVO);
	
	// 양식불러오기
	public ApprovalVO findBaicsForm(ApprovalVO aprvVO);
}
