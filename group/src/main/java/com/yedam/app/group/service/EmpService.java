package com.yedam.app.group.service;

import java.util.List;
import java.util.Map;


public interface EmpService {
	
	// 사원등록
	public int createEmpInfo(EmpVO empVO);
	
	// 사원전체 조회
	public List<EmpVO> findAllEmp(EmpVO empVO);
	
    //  기존 목록 조회 메서드 변경: 페이징 지원 추가
    public List<EmpVO> findAllEmp(EmpserchVO empsVO);

    //  전체 데이터 개수 조회 (페이징 계산용)
    public int countAllEmp(EmpserchVO empsVO);
	
	// 사원상세 정보
	public EmpVO findempInfo(EmpVO empVO);
	
	// 사원정보 수정
	public Map<String, Object>
	             modifyEmpInfo(EmpVO empVO);	
	
	// 사원번호 자동 증가 조회
    public int getNextEmployeeNo();
	
    // 로그인한 대상 정보 가져오기
    public EmpVO getLoggedInUserInfo();
    
    // 비밀번호 초기화 기능 추가
    public void resetPassword(int employeeNo); 

}
