package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.ApprovalVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApprovalMapper {

	// 전체조회(결재대기함)
	public List<ApprovalVO> selectAprvList();

	// 결재대기함(검색)
	public List<ApprovalVO> searchAprvList(ApprovalVO aprvVO);

	// 결재상세조회(승인창)
	public ApprovalVO selectAprvInfo(ApprovalVO aprvVO);

	// 도장 등록
	public int insertStamp(ApprovalVO aprvVO);

	// 도장 이미지 저장
	void updateStampImage(ApprovalVO aprvVO);
	
	

}
