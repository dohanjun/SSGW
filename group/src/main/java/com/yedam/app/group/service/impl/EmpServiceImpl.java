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
	public List<EmpVO> findAllEmp() {
		return empMapper.selectEmpList();
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
	

}
