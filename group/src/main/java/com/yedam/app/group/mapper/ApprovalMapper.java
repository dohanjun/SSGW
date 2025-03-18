package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.ApprovalFormVO;
import com.yedam.app.group.service.ApprovalVO;

@Mapper
public interface ApprovalMapper {

	
	// 결재문서함
	public List<ApprovalVO> selectAprvListByStatus(ApprovalVO aprvVO);

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
	

	

}
