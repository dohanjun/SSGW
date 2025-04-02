package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.DeptHistVO;

public interface DeptHistMapper {
	
	// 부서이동 직책이동 내역
	public int insertDeptTransferHistory(DeptHistVO deptHist);
	
	// 이동 전체 이력 조회
    public List<DeptHistVO> selectAllDeptHistList(int suberNo);
    
    // 마지막 이력 종료일자 업데이트
    public int updateLastDeptHistory(int employeeNo);
    
    // 페이징된 부서/직책 이력 조회
    public List<DeptHistVO> selectDeptHistPaging(DeptHistVO searchVO);
    
    // 전체 이력 개수 조회
    public int countDeptHist(DeptHistVO searchVO);

}
