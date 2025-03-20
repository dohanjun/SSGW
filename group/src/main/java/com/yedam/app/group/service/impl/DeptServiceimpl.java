package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.DeptMapper;
import com.yedam.app.group.service.DeptService;
import com.yedam.app.group.service.DeptVO;
import com.yedam.app.group.service.RankVO;
import com.yedam.app.group.service.RightsVO;

@Service
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

}
