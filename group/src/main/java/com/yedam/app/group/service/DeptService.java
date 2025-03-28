package com.yedam.app.group.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeptService {
	
	// 부서 전체 목록 조회
    List<DeptVO> getAllDepartments(DeptVO deptVO);
    
	// 직급 전체 목록 조회
    List<RankVO> getAllRanks(RankVO rankVO);
    
	// 권한 전체 목록 조회
    List<RightsVO> getAllRights(RightsVO rightsVO);
    
    // 조직도 목록
    List<DeptVO> getOrgChart(DeptVO deptVO);
    
    // 부서전체 조회
    List<DeptVO> getAllDepartments(int suberNo);

    // 그룹웨어 구독시 대표 부서등록
    int insertDepartment(DeptVO deptVO); 
    
    // 회사별 부서추가
    void registerDepartment(DeptVO deptVO);

    // 부서장 등록
	int updateManager(DeptVO deptVO);

	void updateDepManager(DeptVO deptVO);


}
