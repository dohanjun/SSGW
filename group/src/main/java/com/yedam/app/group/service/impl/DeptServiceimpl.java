package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.DeptMapper;
import com.yedam.app.group.service.DeptService;
import com.yedam.app.group.service.DeptVO;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.RankVO;
import com.yedam.app.group.service.RightsVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeptServiceimpl implements DeptService{
	
    @Autowired
    private DeptMapper deptMapper;

	@Override
	public List<DeptVO> getAllDepartments(DeptVO deptVO) {
        return deptMapper.getAllDepartments(deptVO);
	}

	@Override
	public List<RankVO> getAllRanks(RankVO rankVO) {
		return deptMapper.getAllRanks(rankVO);
	}

	@Override
	public List<RightsVO> getAllRights(RightsVO rightsVO) {
		return deptMapper.getAllRights(rightsVO);
	}
	
	// 조직도 목록
//	public List<DeptVO> getOrgChart(DeptVO deptVO) {
//	    return deptMapper.getOrgChart(deptVO);
//	}
	
	// 부서전체 조회
	@Override
	public List<DeptVO> getAllDepartments(int suberNo) {
		return deptMapper.selectAllDepartments(suberNo);
	}
	
	@Override
	public List<DeptVO> getOrgChart(DeptVO deptVO) {
	    List<DeptVO> deptList = deptMapper.getOrgChart(deptVO);

	    for (DeptVO dept : deptList) {
	        System.out.println("부서: " + dept.getDepartmentName() + " / 사원 수: " + (dept.getEmployees() != null ? dept.getEmployees().size() : 0));

	        List<EmpVO> empList = dept.getEmployees();
	        if (empList != null) {
	            for (EmpVO emp : empList) {
	                System.out.println("   - 사원: " + emp.getEmployeeName() + ", 부서번호: " + emp.getDepartmentNo() + ", 직급: " + emp.getJobTitleName());
	            }
	        }
	    }

	    return deptList;
    }
	
	// 그룹웨어 구독시 대표 부서등록
    @Override
    public int insertDepartment(DeptVO deptVO) {
        return deptMapper.insertDepartment(deptVO);
    }
    
    // 부서 추가등록
    @Override
    public void registerDepartment(DeptVO deptVO) {
        // 상위 부서가 있을 경우 해당 부서의 LEVEL + 1 설정
        if (deptVO.getUpperDepNo() != null) {
            Integer parentLevel = deptMapper.getUpperDepLevel(deptVO.getUpperDepNo());
            deptVO.setDepartmentLevel(parentLevel != null ? parentLevel + 1 : 2);
        } else {
            deptVO.setDepartmentLevel(1); // 최상위 부서
        }
        deptMapper.insertDepartment(deptVO);
    }
    
    // 부서장등록
    @Override
    public int updateManager(DeptVO deptVO) {
        return deptMapper.updateManager(deptVO);
    }

	@Override
	public void updateDepManager(DeptVO deptVO) {
		deptMapper.updateDepManager(deptVO);
		
	}


}
