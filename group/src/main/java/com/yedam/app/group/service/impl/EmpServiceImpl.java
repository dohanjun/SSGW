package com.yedam.app.group.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.EmpMapper;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

@Service
public class EmpServiceImpl implements EmpService{
	
	private EmpMapper empMapper;
	
	@Autowired
	public EmpServiceImpl(EmpMapper empMapper){
		this.empMapper = empMapper;
	}
	
	// 사원등록
	@Override
	public int createEmpInfo(EmpVO empVO) {
		int result = empMapper.insertEmpInfo(empVO);
		return result == 1 ? empVO.getEmployeeNo() : -1;
	}

	// 사원 전체조회
	@Override
	public List<EmpVO> findAllEmp() {
		// TODO Auto-generated method stub
		return null;
	}

	// 사원 상세정보
	@Override
	public EmpVO findempInfo(EmpVO empVO) {
		return empMapper.selectEmpInfo(empVO);
	}

	// 사원 정보 수정
	@Override
	public Map<String, Object> modifyEmpInfo(EmpVO empVO) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
