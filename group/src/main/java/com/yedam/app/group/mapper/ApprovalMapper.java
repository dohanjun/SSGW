package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.ApprovalVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ApprovalMapper {
	
	// 전체조회(결재대기함)
	public List<ApprovalVO> selectAprvList();
	
	// 결재대기함(검색)
	public List<ApprovalVO> searchAprvList(ApprovalVO aprvVO);
	
	// 결재 상세 조회
	public ApprovalVO selectAprvInfo(ApprovalVO aprvVO);
	
	// 양식 불러오기
	public ApprovalVO selectBasicsForm(ApprovalVO aprvVO);
	
	// 도장 등록
	public int insertStamp(ApprovalVO aprvVO);

	// 수정: 도장 이미지 저장
		void updateStampImage(@Param("employeeNo") int employeeNo, 
		                      @Param("stampImgPath") String stampImgPath,
		                      @Param("active") String active,
		                      @Param("stampOrder") int stampOrder);

		// 수정: 특정 사원의 활성화된 도장 조회
		String selectStampImage(@Param("employeeNo") int employeeNo);
}
