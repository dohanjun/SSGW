package com.yedam.app.group.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yedam.app.group.service.DeptService;
import com.yedam.app.group.service.DeptVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

import lombok.RequiredArgsConstructor;


/**
 * 부서관리 뷰 컨트롤러 (화면 이동, 등록, 삭제 등 처리)
 * 
 * @author 김예찬
 * @since 2025-04-03
 */


@Controller
@RequiredArgsConstructor
public class DeptViewController {

    private final DeptService deptService;
    private final EmpService empService;
    
    /**
     * 부서 관리 화면 이동
     *
     * @param model 화면에 전달할 부서 목록
     * @return 부서 관리 화면 뷰명
     */
    
    // View용: 부서관리 화면
    @GetMapping("/deptMgmt")
    public String getDepartmentList(Model model) {
        EmpVO loggedInUser = empService.getLoggedInUserInfo();
        int suberNo = loggedInUser.getSuberNo();

        List<DeptVO> deptList = deptService.getAllDepartments(suberNo);
        model.addAttribute("deptList", deptList);

        return "group/personnel/deptMgmt";
    }
    
    /**
     * 부서 등록 화면 이동
     *
     * @param model 부서VO, 부서 목록
     * @return 부서 등록 화면 뷰명
     */
    
    @GetMapping("/deptInsert")
    public String deptInsert(Model model) {
        // 현재 로그인한 사용자 정보 가져오기
        EmpVO loggedInUser = empService.getLoggedInUserInfo();
        Integer suberNo = loggedInUser.getSuberNo();

        // 부서 등록용 VO 생성 및 회사번호 세팅
        DeptVO deptVO = new DeptVO();
        deptVO.setSuberNo(suberNo);

        // 회사번호 기준 기존 부서 목록 조회
        List<DeptVO> deptList = deptService.getAllDepartments(suberNo);

        // 모델에 전달
        model.addAttribute("deptVO", deptVO);         // 입력 폼 바인딩용
        model.addAttribute("deptList", deptList);     // 상위 부서 선택용 목록

        return "group/personnel/deptInsert"; // 등록 화면으로 이동
    }

    /**
     * 부서 등록 처리
     *
     * @param deptVO 등록할 부서 정보
     * @return 부서 관리 화면으로 리다이렉트
     */
    
    @PostMapping("/deptInsert")
    public String insertDepartment(DeptVO deptVO) {
        // 부서레벨 자동 설정
        if (deptVO.getUpperDepNo() == null || deptVO.getUpperDepNo() == 0) {
            deptVO.setDepartmentLevel(1); // 최상위 부서
        } else {
            deptVO.setDepartmentLevel(2); // 하위 부서 (원하면 +1 로직도 가능)
        }

        
        deptService.registerDepartment(deptVO);

        // 부서 목록 화면으로 리다이렉트
        return "redirect:/deptMgmt";
    }
    
    /**
     * 부서장 업데이트 (Ajax 요청)
     *
     * @param employeeNo 선택된 사원 번호
     * @param departmentNo 부서 번호
     * @return 성공 여부 문자열 ("success")
     */
    
    // 부서장 업데이트
    @PostMapping("/updateManager")
    @ResponseBody
    public String updateManager(@RequestParam int employeeNo,
                                @RequestParam int departmentNo) {

        // 로그인한 사용자 회사번호 가져오기
        EmpVO loggedInUser = empService.getLoggedInUserInfo();
        int suberNo = loggedInUser.getSuberNo();

        DeptVO deptVO = new DeptVO();
        deptVO.setEmployeeNo(employeeNo);         // 사원 번호 (선택된 사원)
        deptVO.setManager(employeeNo);            // 실제 DB에 들어갈 필드
        deptVO.setDepartmentNo(departmentNo);     // 선택된 부서
        deptVO.setSuberNo(suberNo);               // 로그인한 회사 번호

        deptService.updateManager(deptVO);        // Mapper 호출

        return "success";
    }
    
    /**
     * 부서 삭제 처리 (Ajax 요청)
     *
     * @param departmentNo 삭제할 부서 번호
     * @return 삭제 결과 문자열 ("success", "hasChild", "hasEmployee", "fail")
     */
    
    // 부서삭제
    @PostMapping("/deleteDepartment")
    @ResponseBody
    public String deleteDepartment(@RequestParam int departmentNo) {
        if (deptService.hasChildDepartments(departmentNo)) return "hasChild";
        if (deptService.hasEmployeesInDept(departmentNo)) return "hasEmployee";

        boolean deleted = deptService.deleteDepartment(departmentNo);
        return deleted ? "success" : "fail";
    }



    

}