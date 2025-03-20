package com.yedam.app.group.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yedam.app.group.mapper.DeptMapper;
import com.yedam.app.group.service.DeptVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.RankVO;
import com.yedam.app.group.service.RightsVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeptController {
	
    private final DeptMapper deptMapper;
    private final EmpService empService;
    
    

    // 부서 목록 조회 API
    @GetMapping("/departments")
    public List<com.yedam.app.group.service.DeptVO> getDepartments(DeptVO deptVO) {
    	// 로그인한 사용자 정보 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();   	
    	deptVO.setSuberNo(loggedInUser.getSuberNo());
	    	    
        return deptMapper.getAllDepartments(deptVO);
    }
    // 직급 목록 조회 API
    @GetMapping("/ranks")
    public List<RankVO> getRanks(RankVO rankVO) {
    	// 로그인한 사용자 정보 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();   	
	    rankVO.setSuberNo(loggedInUser.getSuberNo());
    	
        return deptMapper.getAllRanks(rankVO);
    }
    // 권한 목록 조회 API
    @GetMapping("/rights")
    public List<RightsVO> getRights(RightsVO rightsVO) {
    	// 로그인한 사용자 정보 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    rightsVO.setSuberNo(loggedInUser.getSuberNo());
    	
        return deptMapper.getAllRights(rightsVO);
    }

}
