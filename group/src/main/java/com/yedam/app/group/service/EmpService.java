package com.yedam.app.group.service;

import java.util.List;
import java.util.Map;


public interface EmpService {
	
	// 사원등록
	public int createEmpInfo(EmpVO empVO);
	
	// 사원전체 조회
	public List<EmpVO> findAllEmp();
	
	// 사원상세 정보
	public EmpVO findempInfo(EmpVO empVO);
	
	// 사원정보 수정
	public Map<String, Object>
	             modifyEmpInfo(EmpVO empVO);	
	
	// 사원번호 자동 증가 조회
    public int getNextEmployeeNo();
	
    // 로그인한 대상 정보 가져오기
    public EmpVO getLoggedInUserInfo();

}
