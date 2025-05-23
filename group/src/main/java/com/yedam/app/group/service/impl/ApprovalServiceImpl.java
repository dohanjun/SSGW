package com.yedam.app.group.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yedam.app.group.mapper.ApprovalMapper;
import com.yedam.app.group.service.ApprovalFormVO;
import com.yedam.app.group.service.ApprovalService;
import com.yedam.app.group.service.ApprovalVO;
import com.yedam.app.group.service.AprvRoutesVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.VacationRequestVO;

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
    public int findActiveStamps(ApprovalVO aprvVO) {
		
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
	public Map<String, Object> removeStamp(ApprovalVO aprvVO) {
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
		 List<ApprovalVO> list = approvalMapper.selectAprvListByStatus(aprvVO);
	    
	 // '참조' 역할인 문서는 제외
	    return list.stream()
	               .filter(aprv -> !"참조".equals(aprv.getAprvRole()))  // '참조'는 필터링
	               .collect(Collectors.toList());
	}
	
	// 기본양식
	@Override
	public ApprovalVO findBasicsForm(ApprovalVO aprvVO) {
		return approvalMapper.selectBasicsForm(aprvVO);
	}
	
	// 회사전용양식
	@Override
	public ApprovalFormVO findAprvForm(ApprovalFormVO aprvFormVO) {
		return approvalMapper.selectAprvForm(aprvFormVO);
	}
	
	
	// 기본양식목록
	@Override
	public List<ApprovalVO> findAllBasicsForm(ApprovalVO aprvVO) {
		return approvalMapper.selectAllBasicsForms(aprvVO);
	}
	
	// 회사전용활성화된양식목록
	@Override
	public List<ApprovalFormVO> findAllAprvForm(ApprovalFormVO aprvformVO) {
		return approvalMapper.selectAllAprvForms(aprvformVO);
	}
	
	// 회사전용양식목록
	@Override
	public List<ApprovalFormVO> findAllAprvForms(ApprovalFormVO aprvformVO) {
		return approvalMapper.selectAllAprvFormss(aprvformVO);
	}
	
	// 회사전용양식상세조회
	@Override
    public ApprovalFormVO getAprvFormById(int formId) {
        return approvalMapper.selectOneAprvFormById(formId);
    }
	
	// 회사전용양식수정
    @Override
    public void updateAprvForm(ApprovalFormVO formVO) {
        approvalMapper.updateAprvForm(formVO);
    }
    
    // 회사전용양식삭제
    @Override
    public void deleteAprvForm(int formId) {
        approvalMapper.deleteAprvForm(formId);
    }
	
	
	// 결재요청함, 임시저장함
	@Override
	public List<ApprovalVO> findAllList(ApprovalVO aprvVO) {
		return approvalMapper.selectAllList(aprvVO);
	}
	
	
	// 결재문서 상신, 임시저장
	@Override
	public int createAprvDocu(ApprovalVO aprvVO) {
	    String status = aprvVO.getAprvStatus();
	    if (status == null || status.trim().isEmpty()) {
	        aprvVO.setAprvStatus("대기");  // 기본값
	    } else {
	        aprvVO.setAprvStatus(status);  // 넘어온 값 그대로
	    }
	    return approvalMapper.insertAprvDocuments(aprvVO);
	}
	
	// 결재선 등록
	@Override
	public int createAprvRout(AprvRoutesVO aprvRoutesVO) {
		return approvalMapper.insertAprvRoutes(aprvRoutesVO);
	}

	@Override
	public List<ApprovalVO> findAprvListByRole(ApprovalVO aprvVO) {
		return approvalMapper.selectAprvListByRole(aprvVO);
	}

	@Override
	public List<AprvRoutesVO> findRoutes(AprvRoutesVO aprvRoutesVO) {
		return approvalMapper.selectAprvRout(aprvRoutesVO);
	}

	@Override
	public Map<String, Object> modifyStampForRoute(AprvRoutesVO routVO) {
		Integer stampId = approvalMapper.selectActiveStampId(routVO.getEmployeeNo());
		
	    routVO.setStampId(stampId);
	    int updated = approvalMapper.updateStampId(routVO);
	    
	    Map<String, Object> result = new HashMap<>();
	    result.put("success", updated > 0);
	    result.put("stampId", stampId);
	    return result;
	}

	@Override
	public void processApproval(AprvRoutesVO routVO) {
	    // aprv_order가 숫자로 처리되도록 변경
	    String maxOrderStr = approvalMapper.findMaxAprvOrder(routVO.getDraftNo());
	    
	    // maxOrder 값이 빈 문자열인 경우에는 0으로 설정
	    int maxOrder = (maxOrderStr == null || maxOrderStr.isEmpty()) ? 0 : Integer.parseInt(maxOrderStr);
	    
	    // currentOrder도 빈 문자열 체크 후 처리
	    String currentOrderStr = routVO.getAprvOrder();
	    int currentOrder = (currentOrderStr == null || currentOrderStr.isEmpty()) ? 0 : Integer.parseInt(currentOrderStr);
	    
	    // 상태 결정: currentOrder와 maxOrder 비교
	    String status = (currentOrder == maxOrder) ? "완료" : "진행";
	    routVO.setAprvStatus(status);

	    // 상태 업데이트
	    approvalMapper.updateAprvStatus(routVO);
	}


    @Override
    public void rejectApproval(AprvRoutesVO routVO) {
    	 routVO.setAprvStatus("반려");
    	 approvalMapper.updateAprvStatus(routVO); // 상태 업데이트
    	 approvalMapper.updateRejectReason(routVO); // ❗ 반려사유 저장
    }

	@Override
	public String getMaxAprvOrder(int draftNo) {
	    return approvalMapper.findMaxAprvOrder(draftNo);
	}
	
	@Override
	public int countAprvListByStatus(ApprovalVO vo) {
	    return approvalMapper.countAprvListByStatus(vo);
	}

	@Override
	public int createVacation(VacationRequestVO vacaVO) {
		return approvalMapper.insertVacation(vacaVO);
	}

	@Override
	public ApprovalVO findTitleEmpNo(int draftNo) {
		return approvalMapper.selectTitleEmpNo(draftNo);
	}
	
	@Override
    public List<AprvRoutesVO> findAprvRoutesForDone(AprvRoutesVO routVO) {
        return approvalMapper.selectAprvRoutForDone(routVO);
    }

	@Override
	public int countAllList(ApprovalVO aprvVO) {
		return approvalMapper.countAllList(aprvVO);
	}

	@Override
	public int countReferenceList(ApprovalVO aprvVO) {
		return approvalMapper.countReferenceList(aprvVO);
	}
	
	public void removeTemporaryData(Integer draftNo) {
		approvalMapper.deleteTemporaryData(draftNo);
	}
	
	// '대기' 문서 삭제
	@Override
    @Transactional
    public void deleteDraft(int draftNo) {
        // 1. 결재선 삭제
        approvalMapper.deleteRoutesByDraftNo(draftNo);

        // 2. 첨부파일 삭제
        approvalMapper.deleteFilesByDraftNo(draftNo);

        // 3. 휴가원 여부 확인 후 휴가요청 삭제
        if (approvalMapper.isVacationForm(draftNo) > 0) {
            approvalMapper.deleteVacationRequest(draftNo);
        }

        // 4. 마지막으로 문서 삭제
        approvalMapper.deleteDraft(draftNo);
    }
	
}
