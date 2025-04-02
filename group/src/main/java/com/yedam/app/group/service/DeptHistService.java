package com.yedam.app.group.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeptHistService {
	
	// 부서이동 직책이동 내역
	public void saveDeptTransferHistory(DeptHistVO deptHist);
	
    // 이동 이력 전체 조회
    public List<DeptHistVO> getAllDeptTransferHistory(int suberNo);
    
    // 마지막 부서/직책 이력 종료 처리
    public void closePreviousDeptHistory(int employeeNo);
    
    // 페이징된 부서/직책 이력 조회
    public List<DeptHistVO> getDeptHistPaging(DeptHistVO searchVO);
    
    // 전체 이력 개수 조회
    public int countDeptHist(DeptHistVO searchVO);


}
