package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.ApprovalMapper;
import com.yedam.app.group.service.ApprovalFormVO;
import com.yedam.app.group.service.ApprovalService;
import com.yedam.app.group.service.ApprovalVO;

@Service
public class ApprovalServiceImpl implements ApprovalService {

	private ApprovalMapper approvalMapper;
	
	@Autowired
	public ApprovalServiceImpl(ApprovalMapper approvalMapper) {
		this.approvalMapper = approvalMapper;
	}
	
	@Override
	public int createStamp(ApprovalVO aprvVO) {
		
	    if (aprvVO.getStampImgPath() == null || aprvVO.getStampImgPath().isEmpty()) {
	    	
	        throw new IllegalArgumentException("파일 경로가 필요합니다.");
	    }
	    return approvalMapper.insertStamp(aprvVO);
	}
	
	// 전체조회
	@Override
	public List<ApprovalVO> findAllList() {
		return approvalMapper.selectAprvList();
	}

	@Override
	public List<ApprovalVO> searchApprovalList(ApprovalVO aprv) {
	    return approvalMapper.searchAprvList(aprv);
	}
	
	
	// 결재 상세 조회
	@Override
	public ApprovalVO findAprvInfo(ApprovalVO aprvVO) {
		return approvalMapper.selectAprvInfo(aprvVO);
	}
	
	
	// 회사 전자결재 양식 등록
	@Override
	public int createForm(ApprovalFormVO aprvformVO) {
		int result = approvalMapper.insertForm(aprvformVO);
		return result == 1 ? aprvformVO.getFormId() : -1;
	}

	

	
	
}
