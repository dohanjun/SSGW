package com.yedam.app.group.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Param;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.EmpserchVO;

public interface EmpMapper {
	
	// 사원등록
	public int insertEmpInfo(EmpVO empVO);
	
	// 사원전체 조회
	public List<EmpVO> selectEmpList(EmpVO empVO);
	
	// 사원상세 정보
	public EmpVO selectEmpInfo(EmpVO empVO);
	
	// 사원정보 수정
	public int updateEmpInfo(EmpVO empVO);
	
	// 사원번호 증가값 조회
	public int getNextEmployeeNo();
	
	// 로그인한 정보 가져오기
	public EmpVO findByEmployeeId(String employeeId);
	
    //  페이징 적용된 사원 목록 조회
    public List<EmpVO> pageselectEmp(EmpserchVO empsVO);

    //  전체 사원 수 조회
    public int countEmp(EmpserchVO empsVO);
    
    
    //  사원번호로 랜덤 비밀번호를 업데이트
    public void updatePassword(@Param("employeeNo") int employeeNo, @Param("employeePw") String employeePw);
    
    // 첫번째 ip
    String getFirstIpByEmployeeNo(Integer employeeNo);
    
    // 두번째 ip
    String getSecondIpByEmployeeNo(Integer employeeNo);

	public void updateEmployeePasswordBySuberNo(EmpVO employee);
	
	// 아이디 중복 체크
	public int isEmployeeIdDuplicate(String employeeId);

}
