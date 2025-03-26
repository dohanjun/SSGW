package com.yedam.app.group.service.impl;

import java.util.Base64;
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
	@Override
    public List<DeptVO> getOrgChart(DeptVO deptVO) {
	    List<DeptVO> deptList = deptMapper.getOrgChart(deptVO);

	    for (DeptVO dept : deptList) {
	        List<EmpVO> empList = dept.getEmployees(); // 각 부서에 사원 리스트가 있다고 가정
	        if (empList != null) {
	            for (EmpVO emp : empList) {
	                if (emp.getProfileImageBLOB() != null) {
	                    String base64 = Base64.getEncoder().encodeToString(emp.getProfileImageBLOB());
	                    emp.setProfileImageBase64(base64);
	                }
	            }
	        }
	    }

	    return deptList;
    }

    @Override
    public int insertDepartment(DeptVO deptVO) {
        return deptMapper.insertDepartment(deptVO);
    }


}
