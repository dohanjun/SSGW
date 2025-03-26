package com.yedam.app.group.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yedam.app.group.mapper.DeptMapper;
import com.yedam.app.group.service.DeptService;
import com.yedam.app.group.service.DeptVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.RankVO;
import com.yedam.app.group.service.RightsVO;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



/** 
 *  그룹웨어 부서관리 등록,조회,조직도 등컨트롤러   
 *  @author DEPT관리자 개발팀 김예찬
 *  @serial 2025-03-24
 *  <pre>
 *  <pre>
 *  	수정일자		수정자		수정내용
 * ---------------------------------------------------
 *  </pre>
 */

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeptController {
	
    private final DeptMapper deptMapper;
    private final EmpService empService;
    private final DeptService deptService;
    

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
    
    // 조직도 컨트롤러
    @GetMapping("/orgchart")
    public List<DeptVO> getOrgChart(DeptVO deptVO) {
    	// 로그인한 사용자 정보 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    deptVO.setSuberNo(loggedInUser.getSuberNo());
    	
        return deptMapper.getOrgChart(deptVO);
    }
    
    @PostMapping("/saveDepartMent")
    public ResponseEntity<Integer> saveDept(@RequestBody DeptVO deptVO) {
    	System.out.println("Controller 진입");
    	System.out.println(deptVO);
        deptService.insertDepartment(deptVO);
        System.out.println("삽입 후 부서번호: " + deptVO.getDepartmentNo());
        return ResponseEntity.ok(deptVO.getDepartmentNo());
    }
}
