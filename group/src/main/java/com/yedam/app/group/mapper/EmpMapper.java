package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.EmpVO;

public interface EmpMapper {
	
	// 사원등록
	public int insertEmpInfo(EmpVO empVO);
	
	// 사원전체 조회
	public List<EmpVO> selectEmpList();
	
	// 사원상세 정보
	public EmpVO selectEmpInfo(EmpVO empVO);
	
	// 사원정보 수정
	public int updateEmpInfo(EmpVO empVO);
	
	// 사원번호 증가값 조회
	public int getNextEmployeeNo();

}
