package com.yedam.app.approval.service.impl;

import com.yedam.app.approval.mapper.ApprovalMapper;
import com.yedam.app.approval.service.ApprovalService;
import com.yedam.app.approval.service.ApprovalVO;

public class ApprovalServiceImpl implements ApprovalService {

	private ApprovalMapper approvalMapper;
	
	@Override
	public int createStamp(ApprovalVO aprvVO) {
		int result = approvalMapper.insertStamp(aprvVO);
		
		return result;
	}

}
