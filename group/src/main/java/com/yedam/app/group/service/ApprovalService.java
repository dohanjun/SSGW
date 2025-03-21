package com.yedam.app.group.service;

import java.util.List;
import java.util.Map;

public interface ApprovalService {
	
	// 결재문서조회
	public List<ApprovalVO> findAprvListByStatus(ApprovalVO aprvVO);
	
	// 결재요청함, 임시저장함
	public List<ApprovalVO> findAllList(ApprovalVO aprvVO);
	
	// 결재 상세 정보 조회
	public ApprovalVO findAprvInfo(ApprovalVO aprvVO);
	
	// 기본양식목록 조회
	public List<ApprovalVO> findAllBasicsForm(ApprovalVO aprvVO);
	
	// 회사전용양식목록 조회
	public List<ApprovalFormVO> findAllAprvForm(ApprovalFormVO aprvformVO);
	
	// 기본양식 조회
	public ApprovalVO findBasicsForm(ApprovalVO aprvVO);

	// 회사전용양식 조회
	public ApprovalFormVO findAprvForm(ApprovalFormVO aprvformVO);
	
	// 도장 등록
	public int createStamp(ApprovalVO aprvVO);
	
	// 도장 수정(기존 도장 active= '0')
	public Map<String, Object> modifyStamp(ApprovalVO aprvVO);
	
	// 도장 비활성화
	public Map<String, Object> removeStamp(ApprovalVO aprvVO);
	
	// 활성화된 도장 개수 조회
	public int findActiveStamps(ApprovalVO aprvVO);
	
	// 도장정보 불러오기
	public ApprovalVO getActiveStamp(ApprovalVO aprvVO);
	
	// 회사 전자결재 양식 등록
	public int createForm(ApprovalFormVO aprvformVO);
	
	

  
}
