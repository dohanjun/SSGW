package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.ApprovalFormVO;
import com.yedam.app.group.service.ApprovalVO;
import com.yedam.app.group.service.AprvRoutesVO;

@Mapper
public interface ApprovalMapper {

	// 결재문서함(대기, 진행, 완료, 반려)
	public List<ApprovalVO> selectAprvListByStatus(ApprovalVO aprvVO);
	
	// 결재요청함, 임시저장함
	public List<ApprovalVO> selectAllList(ApprovalVO aprvVO);
	
	// 참조열람함
	public List<ApprovalVO> selectAprvListByRole(ApprovalVO aprvVO);
	
	// 양식목록불러오기(기본)
	public List<ApprovalVO> selectAllBasicsForms(ApprovalVO aprvVO);
	
	// 양식목록불러오기(회사전용)
	public List<ApprovalFormVO> selectAllAprvForms(ApprovalFormVO approvalFormVO);
	
	// 양식불러오기(기본)
	public ApprovalVO selectBasicsForm(ApprovalVO aprvVO);
	
	// 양식불러오기(회사전용)
	public ApprovalFormVO selectAprvForm(ApprovalFormVO aprvformVO);
	
	// 결재상세조회(승인창)
	public ApprovalVO selectAprvInfo(ApprovalVO aprvVO);
	
	// 도장 등록
	public int insertStamp(ApprovalVO aprvVO);
	
	// 도장 수정(기존 도장 active = 0, 새로운 도장 등록)
	public int updateStamp(ApprovalVO aprvVO); 
	
	// 활성화된 도장 조회
	public Integer countActiveStamps(ApprovalVO aprvVO);  
	
	// 도장이미지불러오기
	public ApprovalVO selectActiveStamp(ApprovalVO aprvVO);
	
	// 회사 전자결재양식 등록
	public int insertForm(ApprovalFormVO aprvformVO);
	
	// 문서 상신
	public int insertAprvDocuments(ApprovalVO aprvVO);
	
	// 결재선 등록
	public int insertAprvRoutes(AprvRoutesVO aprvRoutesVO);
	

}
