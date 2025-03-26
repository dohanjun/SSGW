package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import com.yedam.app.group.service.DeptVO;
import com.yedam.app.group.service.RankVO;
import com.yedam.app.group.service.RightsVO;

public interface DeptMapper {
	
	// 전체 부서 조회
	public List<DeptVO> selectAllDepartments(@Param("suberNo") int suberNo);
	
	
	// 부서 등록
	
	
    // 모든 부서 목록 조회
    public List<DeptVO> getAllDepartments(DeptVO deptVO);

    // 모든 직급 목록 조회
    public List<RankVO> getAllRanks(RankVO rankVO);

    // 모든 권한 목록 조회
    public List<RightsVO> getAllRights(RightsVO rightsVO);
    
    // 직원 조직도
    public List<DeptVO> getOrgChart(DeptVO deptVO);
	
	

}
