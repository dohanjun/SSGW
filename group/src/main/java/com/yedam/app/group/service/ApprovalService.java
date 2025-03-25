package com.yedam.app.group.service;

import java.util.List;
import java.util.Map;

public interface ApprovalService {
	
	// 결재문서조회
	public List<ApprovalVO> findAprvListByStatus(ApprovalVO aprvVO);
	
	// 결재요청함, 임시저장함
	public List<ApprovalVO> findAllList(ApprovalVO aprvVO);
	
	// 참조열람함
	public List<ApprovalVO> findAprvListByRole(ApprovalVO aprvVO);
	
	// 결재 문서 상세 정보 조회
	public ApprovalVO findAprvInfo(ApprovalVO aprvVO);
	
	// 기본양식목록 조회
	public List<ApprovalVO> findAllBasicsForm(ApprovalVO aprvVO);
	
	// 회사전용양식목록 조회
	public List<ApprovalFormVO> findAllAprvForm(ApprovalFormVO aprvformVO);
	
	// 기본양식 조회
	public ApprovalVO findBasicsForm(ApprovalVO aprvVO);

	// 회사전용양식 조회
	public ApprovalFormVO findAprvForm(ApprovalFormVO aprvformVO);
	
	// 사원의 도장 등록
	public int createStamp(ApprovalVO aprvVO);
	
	// 도장 수정(기존 도장 active= '0')
	public Map<String, Object> modifyStamp(ApprovalVO aprvVO);
	
	// 결재페이지 승인시 도장등록
	public Map<String, Object> modifyStampForRoute(AprvRoutesVO routVO);
	
	// 도장 비활성화
	public Map<String, Object> removeStamp(ApprovalVO aprvVO);
	
	// 활성화된 도장 개수 조회
	public int findActiveStamps(ApprovalVO aprvVO);
	
	// 도장정보 불러오기
	public ApprovalVO getActiveStamp(ApprovalVO aprvVO);
	
	// 회사 전자결재 양식 등록
	public int createForm(ApprovalFormVO aprvformVO);
	
	// 전자결재 문서상신
	public int createAprvDocu(ApprovalVO aprvVO);
	
	// 결재선 등록
	public int createAprvRout(AprvRoutesVO aprvRoutesVO);
	
	// 결재선 출력
	public List<AprvRoutesVO> findRoutes(AprvRoutesVO aprvRoutesVO);
	
	// 결재 승인 처리 / 해당 서비스 메서드가 단순히 DB 상태만 바꾸는 작업이고, 결과 데이터를 별도로 반환할 필요가 없기 때문
    public void processApproval(AprvRoutesVO aprvRoutesVO);
    
    // 결재자 순서 조회
    public String getMaxAprvOrder(int draftNo); 

    // 반려 처리
    public void rejectApproval(AprvRoutesVO aprvRoutesVO);
  
}
