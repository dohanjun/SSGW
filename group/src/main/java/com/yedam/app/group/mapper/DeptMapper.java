package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.DeptVO;

public interface DeptMapper {
	
	// 부서 조회
	public List<DeptVO> selectDeptList();
	
	
	// 부서 등록

}
