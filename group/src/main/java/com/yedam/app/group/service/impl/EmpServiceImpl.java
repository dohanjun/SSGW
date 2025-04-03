package com.yedam.app.group.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.yedam.app.group.service.DeptHistVO;
import com.yedam.app.group.mapper.DeptHistMapper;
import com.yedam.app.group.mapper.EmpMapper;
import com.yedam.app.group.service.DeptHistVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.EmpserchVO;
import com.yedam.app.group.service.PasswordUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmpServiceImpl implements EmpService{
	
	private final EmpMapper empMapper;
	private final DeptHistMapper deptHistMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
//	@Autowired
//	public EmpServiceImpl(EmpMapper empMapper ){
//		this.empMapper = empMapper;
//	}
	
	// 사원등록
	@Override
	public int createEmpInfo(EmpVO empVO) {
        int result = empMapper.insertEmpInfo(empVO);

        //  등록 성공 시, 부서/직책 최초 이력 저장
        if (result > 0) {
            DeptHistVO hist = new DeptHistVO();
            hist.setEmployeeNo(empVO.getEmployeeNo());
            hist.setDepartmentNo(0); // 이전 없음
            hist.setMovedToDepartment(empVO.getDepartmentNo());
            hist.setPreviousRankId(null);
            hist.setCurrentRankId(empVO.getRankId());
            hist.setContent("신규 등록");

            deptHistMapper.insertDeptTransferHistory(hist);
        }

        return result;
    }


	// 사원 전체조회
	@Override
	public List<EmpVO> findAllEmp(EmpVO empVO) {
		return empMapper.selectEmpList(empVO);
	}

	// 사원 상세정보
	@Override
	public EmpVO findempInfo(EmpVO empVO) {
		return empMapper.selectEmpInfo(empVO);
	}

	// 사원 정보 수정 (부서/직급 변경 시 이력 기록 포함)
	@Override
	public Map<String, Object> modifyEmpInfo(EmpVO empVO) {
	    Map<String, Object> map = new HashMap<>();
	    boolean isSuccessed = false;

	    // 1. 기존 사원 정보 가져오기 (변경 전 데이터)
	    EmpVO oldEmp = empMapper.selectEmpInfo(empVO);

	    // 2. 부서 또는 직급이 변경되었는지 확인
	    boolean isDeptChanged = empVO.getDepartmentNo() != null &&
	                            !empVO.getDepartmentNo().equals(oldEmp.getDepartmentNo());
	    boolean isRankChanged = empVO.getRankId() != null &&
	                            !empVO.getRankId().equals(oldEmp.getRankId());

	    // 3. 사원 정보 업데이트 실행
	    int result = empMapper.updateEmpInfo(empVO);

	    // 4. 업데이트 성공 시 변경 이력 저장
	    if (result == 1) {
	        isSuccessed = true;

	        // 부서 또는 직급이 바뀐 경우만 이력 기록
	        if (isDeptChanged || isRankChanged) {
	            DeptHistVO hist = new DeptHistVO();
	            hist.setEmployeeNo(empVO.getEmployeeNo());                         // 사원번호
	            hist.setDepartmentNo(oldEmp.getDepartmentNo());                   // 이전 부서번호
	            hist.setDeleteDate(new java.util.Date());                         // 부서 종료 날짜 = 이동한 날짜
	            hist.setMovedToDepartment(empVO.getDepartmentNo());               // 이동한 부서
	            hist.setPreviousRankId(oldEmp.getRankId());                       // 이전 직책 ID
	            hist.setCurrentRankId(empVO.getRankId());                         // 이동 후 직책 ID

	            // 변경 유형에 따라 content 작성
	            if (isDeptChanged && isRankChanged) {
	                hist.setContent("부서/직책 변경");
	            } else if (isDeptChanged) {
	                hist.setContent("부서 이동");
	            } else if (isRankChanged) {
	                hist.setContent("직책 변경");
	            }

	            // 이력 저장
	            deptHistMapper.insertDeptTransferHistory(hist);
	        }
	    }

	    // 결과 전달 (Ajax 응답용)
	    map.put("result", isSuccessed);
	    map.put("target", empVO);
	    return map;
	}


	// 사원번호 자동증가 조회
	@Override
	public int getNextEmployeeNo() {
		return empMapper.getNextEmployeeNo();
	}
	
	
	// 로그인한 대상 정보가져오기
	@Override
	public EmpVO getLoggedInUserInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			
			String employeeId = authentication.getName(); // 로그인한 사용자의 ID(employeeId)
			
			return empMapper.findByEmployeeId(employeeId); // DB에서 해당 ID로 정보 조회
		}
		return null;
	}
	
    //  페이징된 사원 목록 조회
    public List<EmpVO> findAllEmp(EmpserchVO empsVO) {
        int offset = (empsVO.getPage() - 1) * empsVO.getSize(); // OFFSET 계산
        empsVO.setOffset(offset);
        return empMapper.pageselectEmp(empsVO);
    }

    //  전체 사원 수 조회
    @Override
    public int countAllEmp(EmpserchVO empsVO) {
        return empMapper.countEmp(empsVO);
    }

    // 비밀번호 업데이트
    @Override
    public void resetPassword(int employeeNo, String defaultPw) {
        String encodedPw = bCryptPasswordEncoder.encode(defaultPw); // 해시 암호화
        empMapper.updatePassword(employeeNo, encodedPw);
    }
  
    @Override
    public String getFirstIpByEmployeeNo(Integer employeeNo) {
        return empMapper.getFirstIpByEmployeeNo(employeeNo);
    }

    @Override
    public String getSecondIpByEmployeeNo(Integer employeeNo) {
        return empMapper.getSecondIpByEmployeeNo(employeeNo);
    }

    @Override
    public void updateEmployeePasswordBySuberNo(EmpVO employee) {
        empMapper.updateEmployeePasswordBySuberNo(employee);
    }
    
    
    // 아이디 중복체크
    @Override
    public boolean isEmployeeIdDuplicate(String employeeId) {
        return empMapper.isEmployeeIdDuplicate(employeeId) > 0;
    }
	

}
