package com.yedam.app.group.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.ApprovalMapper;
import com.yedam.app.group.service.ApprovalFormVO;
import com.yedam.app.group.service.ApprovalService;
import com.yedam.app.group.service.ApprovalVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

@Service
public class ApprovalServiceImpl implements ApprovalService {

	private ApprovalMapper approvalMapper;
	private EmpService empService;
	
	@Autowired
    public ApprovalServiceImpl(ApprovalMapper approvalMapper, EmpService empService) {
		this.approvalMapper = approvalMapper;
        this.empService = empService;  
    }
	
	// 도장등록
	@Override
	public int createStamp(ApprovalVO aprvVO) {
	    // 로그인한 사용자의 정보를 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();

	    if (loggedInUser != null) {
	        aprvVO.setEmployeeNo(loggedInUser.getEmployeeNo());  // `employeeNo` 자동 추가
	    } else {
	        throw new IllegalStateException("로그인한 사용자 정보를 찾을 수 없습니다.");
	    }

	    // stamp 테이블에서 활성화된 도장 개수 조회 (`ApprovalVO` 사용)
	    Integer activeStamps = approvalMapper.countActiveStamps(aprvVO);
	    
	    if (activeStamps != null && activeStamps > 0) {  //  `null`이면 `0`으로 처리
	        throw new IllegalStateException("이미 활성화된 도장이 존재합니다.");
	    }

	    // 파일 경로 검증
	    if (aprvVO.getStampImgPath() == null || aprvVO.getStampImgPath().isEmpty()) {
	        throw new IllegalArgumentException("파일 경로가 필요합니다.");
	    }

	    // 도장 등록
	    return approvalMapper.insertStamp(aprvVO);
	}

	
	// 도장 active '1' 확인하기
	@Override
    public int countActiveStamps(ApprovalVO aprvVO) {
		
        return approvalMapper.countActiveStamps(aprvVO);
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
	
	// 도장 수정
	@Override
	public Map<String, Object> modifyStamp(ApprovalVO aprvVO) {
	    Map<String, Object> response = new HashMap<>();
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    
	    aprvVO.setEmployeeNo(loggedInUser.getEmployeeNo());
	    
	    // 기존 도장 비활성화 (active = 0)
	    approvalMapper.updateStamp(aprvVO);

	    // 새로운 도장 등록
	    int insertCount = approvalMapper.insertStamp(aprvVO);

	    if (insertCount > 0) {
	        response.put("success", true);
	    } else {
	        response.put("success", false);
	        response.put("message", "새로운 도장 등록 실패");
	    }

	    return response;
	}
	
	// 도장 비활성화
	@Override
	public Map<String, Object> deleteStamp(ApprovalVO aprvVO) {
	    Map<String, Object> response = new HashMap<>();
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    
	    if (loggedInUser == null) {
	        response.put("success", false);
	        response.put("message", "로그인한 사용자 정보를 찾을 수 없습니다.");
	        return response;
	    }

	    aprvVO.setEmployeeNo(loggedInUser.getEmployeeNo());
	    
	    // 기존 도장 비활성화 (active = 0)
	    int updateCount = approvalMapper.updateStamp(aprvVO);

	    if (updateCount > 0) {
	        response.put("success", true);
	        response.put("message", "도장이 성공적으로 비활성화되었습니다.");
	    } else {
	        response.put("success", false);
	        response.put("message", "비활성화할 도장이 없습니다.");
	    }

	    return response;
	}

	@Override
	public ApprovalVO getActiveStamp(ApprovalVO aprvVO) {
	    return approvalMapper.selectActiveStamp(aprvVO);
	}
	
	// 전자결재 문서조회
	@Override
	public List<ApprovalVO> findAprvListByStatus(ApprovalVO aprvVO) {
	    return approvalMapper.selectAprvListByStatus(aprvVO);
	}
	
	// 기본양식
	@Override
	public ApprovalVO findBasicsForm(Integer basicsFormId) {
		return approvalMapper.selectBasicsForm(basicsFormId);
	}
	
	// 회사전용양식
	@Override
	public ApprovalFormVO findAprvForm(Integer formId, int suber) {
		return approvalMapper.selectAprvForm(formId, suber);
	}

	
	
}
