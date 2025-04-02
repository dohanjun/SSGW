package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.DeptHistMapper;
import com.yedam.app.group.service.DeptHistService;
import com.yedam.app.group.service.DeptHistVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeptHistServiceImpl implements DeptHistService{
	
	private final DeptHistMapper deptHistMapper;
	
	// 부서이동 직책이동 내역
    @Override
    public void saveDeptTransferHistory(DeptHistVO deptHist) {
        deptHistMapper.insertDeptTransferHistory(deptHist);
    }
    
    // 이동 이력 전체 조회
    @Override
    public List<DeptHistVO> getAllDeptTransferHistory(int suberNo) {
        return deptHistMapper.selectAllDeptHistList(suberNo);
    }
    
    // 마지막 부서/직책 이력 종료 처리
    @Override
    public void closePreviousDeptHistory(int employeeNo) {
        deptHistMapper.updateLastDeptHistory(employeeNo);
    }
    
    // 페이징된 부서/직책 이력 조회
    @Override
    public List<DeptHistVO> getDeptHistPaging(DeptHistVO searchVO) {
        int offset = (searchVO.getPage() - 1) * searchVO.getSize();
        searchVO.setStart(offset);
        searchVO.setAmount(searchVO.getSize());
        return deptHistMapper.selectDeptHistPaging(searchVO);
    }
    
    // 전체 이력 개수 조회
    @Override
    public int countDeptHist(DeptHistVO searchVO) {
        return deptHistMapper.countDeptHist(searchVO);
    }

}
