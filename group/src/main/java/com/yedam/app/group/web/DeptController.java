package com.yedam.app.group.web;

import java.util.Base64;
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
    
    // 조직도 데이터 제공 (JSON 응답)
    @GetMapping("/orgchart")
    public List<DeptVO> getOrgChart(DeptVO deptVO) {
       // 로그인한 사용자 정보 가져오기
	   EmpVO loggedInUser = empService.getLoggedInUserInfo();
	   deptVO.setSuberNo(loggedInUser.getSuberNo());
    	
       List<DeptVO> result = deptMapper.getOrgChart(deptVO);
       
       //  사원 BLOB → Base64 변환 후 profileImageBase64 세팅
       for (DeptVO dept : result) {
           if (dept.getEmployees() != null) {
               for (EmpVO emp : dept.getEmployees()) {
                   if (emp.getProfileImageBLOB() != null) {
                       String base64 = Base64.getEncoder().encodeToString(emp.getProfileImageBLOB());
                       emp.setProfileImageBase64(base64); // 🔹 VO에 해당 필드 있어야 함
                   }
               }
           }
       }

        //  디버깅 로그 찍기
        System.out.println("[조직도 호출] 회사번호: " + deptVO.getSuberNo());
        for (DeptVO d : result) {
            System.out.println("부서: " + d.getDepartmentName() + ", 사원 수: " + (d.getEmployees() != null ? d.getEmployees().size() : 0));
        }

        return result;
    }
    
    // 그룹웨어 구독시 대표 부서등록
    @PostMapping("/saveDepartMent")
    public ResponseEntity<Integer> saveDept(@RequestBody DeptVO deptVO) {
        deptService.insertDepartment(deptVO);
        return ResponseEntity.ok(deptVO.getDepartmentNo());
    }
    
    @PostMapping("/updateDepartMent")
    public ResponseEntity<String> updateManager(@RequestBody DeptVO deptVO) {
    	deptService.updateDepManager(deptVO);
        return ResponseEntity.ok("success");
    }
}
