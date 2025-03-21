package com.yedam.app.group.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Param;
import com.yedam.app.group.service.EmpVO;

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
    public List<EmpVO> pageselectEmp(@Param("offset") int offset,
							            @Param("size") int size,
							            @Param("category") String category,
							            @Param("keyword") String keyword,
							            @Param("suberNo") Integer suberNo);

    //  전체 사원 수 조회
    public int countEmp(@Param("category") String category,
			            @Param("keyword") String keyword,
			            @Param("suberNo") Integer suberNo);
    
    
    //  사원번호로 랜덤 비밀번호를 업데이트
    public void updatePassword(@Param("employeeNo") int employeeNo, @Param("employeePw") String employeePw);
    
    
	
}
