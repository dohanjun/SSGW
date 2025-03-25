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
	
	// 결재승인페이지
	public List<AprvRoutesVO> selectAprvRout(AprvRoutesVO routVO);
	
	// 도장 등록
	public int insertStamp(ApprovalVO aprvVO);
	
	// 도장 수정(기존 도장 active = 0, 새로운 도장 등록)
	public int updateStamp(ApprovalVO aprvVO); 
	
	// 활성화된 도장 조회
	public Integer countActiveStamps(ApprovalVO aprvVO);  
	
	// 도장이미지불러오기
	public ApprovalVO selectActiveStamp(ApprovalVO aprvVO);
	
	// 결재페이지 도장
	public Integer selectActiveStampId(Integer employeeNo);
	
	// 업데이트
	public int updateStampId(AprvRoutesVO aprvRoutVO);
	
	// 회사 전자결재양식 등록
	public int insertForm(ApprovalFormVO aprvformVO);
	
	// 문서 상신
	public int insertAprvDocuments(ApprovalVO aprvVO);
	
	// 결재선 등록
	public int insertAprvRoutes(AprvRoutesVO aprvRoutesVO);
	
	// aprv_order가 마지막인지 확인, 마지막이라면 status가 '진행'이 아닌 '완료'로 변경
	public String findMaxAprvOrder(Integer draftNo);
	
	// 결재 staus 변경
	public int updateAprvStatus(AprvRoutesVO aprvRoutesVO);

}
