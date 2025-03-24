package com.yedam.app.group.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.EmpMapper;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.EmpserchVO;
import com.yedam.app.group.service.PasswordUtils;

@Service
public class EmpServiceImpl implements EmpService{
	
	private EmpMapper empMapper;
	
	@Autowired
	public EmpServiceImpl(EmpMapper empMapper ){
		this.empMapper = empMapper;
	}
	
	// 사원등록
	@Override
	public int createEmpInfo(EmpVO empVO) {
		return empMapper.insertEmpInfo(empVO);
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

	// 사원 정보 수정
	@Override
	public Map<String, Object> modifyEmpInfo(EmpVO empVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;
		
		int result = empMapper.updateEmpInfo(empVO);
		
		if(result == 1) {
			isSuccessed = true;
		}
		
		map.put("result", isSuccessed);
		map.put("target", empVO);
		// 자바 내부 기준말고 아작스 기준으로 만듬 자바스크립트한태 전달형태 지금만든 맵
		
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
    public void resetPassword(int employeeNo) {
        // 1️ 랜덤 비밀번호 생성
        String randomPassword = PasswordUtils.generateRandomPassword();
        
        // 2️ 비밀번호 암호화(해싱)
        String hashedPassword = PasswordUtils.hashPassword(randomPassword);

        // 3️ 비밀번호 업데이트 (DB에 저장)
        empMapper.updatePassword(employeeNo, hashedPassword);

//        // 4️ (선택) 이메일 전송 가능
//        System.out.println("새로운 비밀번호: " + randomPassword); // 콘솔 확인용
    }
  
    @Override
    public String getFirstIpByEmployeeNo(Integer employeeNo) {
        return empMapper.getFirstIpByEmployeeNo(employeeNo);
    }

    @Override
    public String getSecondIpByEmployeeNo(Integer employeeNo) {
        return empMapper.getSecondIpByEmployeeNo(employeeNo);
    }
	

}
