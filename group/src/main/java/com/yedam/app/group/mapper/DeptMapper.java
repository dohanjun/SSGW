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
	public int insertDepartment(DeptVO deptVO);
	
	// 상위 부서레벨 조회
	public int getUpperDepLevel(int upperDepNo);	
	
    // 모든 부서 목록 조회
    public List<DeptVO> getAllDepartments(DeptVO deptVO);

    // 모든 직급 목록 조회
    public List<RankVO> getAllRanks(RankVO rankVO);

    // 모든 권한 목록 조회
    public List<RightsVO> getAllRights(RightsVO rightsVO);
    
    // 직원 조직도
    public List<DeptVO> getOrgChart(DeptVO deptVO);
    
    // 부서장 업데이트
    public int updateManager(DeptVO deptVO);

    //
	public void updateDepManager(DeptVO deptVO);
	
    // 하위 부서 개수 확인
    public int countChildDepartments(@Param("departmentNo") int departmentNo);

    // 부서에 속한 사원 수 확인
    public int countEmployeesInDept(@Param("departmentNo") int departmentNo);

    // 부서 삭제
    public int deleteDepartment(@Param("departmentNo") int departmentNo);
    
    // 부서장 null처리
    public int clearManagerBeforeDelete(int departmentNo);
    
    // 사원 부서 NULL 처리 
    public int clearEmployeesInDept(int departmentNo);
        
    // 부서 자료실 삭제
    public int deleteFileRepositoryByDeptNo(int departmentNo);
    

	
	

}
