package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.ApprovalMapper;
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

	@Override
	public ApprovalVO findBaicsForm(ApprovalVO aprvVO) {
		return approvalMapper.selectBasicsForm(aprvVO);
	}
	
	
	// 전체조회
	@Override
	public List<ApprovalVO> findAllList() {
		return approvalMapper.selectAprvList();
	}


}
