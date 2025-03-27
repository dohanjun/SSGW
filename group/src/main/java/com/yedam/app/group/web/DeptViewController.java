package com.yedam.app.group.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.yedam.app.group.service.DeptService;
import com.yedam.app.group.service.DeptVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DeptViewController {

    private final DeptService deptService;
    private final EmpService empService;

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
     * 부서 등록 화면 요청
     * 로그인한 사용자의 회사번호(suberNo)를 기준으로 부서 목록을 가져와 화면에 출력
     */
    @GetMapping("/deptInsert")
    public String showDeptInsertForm(Model model) {
        // 현재 로그인한 사용자 정보 가져오기
        EmpVO loggedInUser = empService.getLoggedInUserInfo();
        Integer suberNo = loggedInUser.getSuberNo();

        // 부서 등록용 VO 생성 및 회사번호 세팅
        DeptVO deptVO = new DeptVO();
        deptVO.setSuberNo(suberNo);

        // 회사번호 기준 기존 부서 목록 조회
        List<DeptVO> deptList = deptService.getAllDepartments(deptVO);

        // 모델에 전달
        model.addAttribute("deptVO", deptVO);         // 입력 폼 바인딩용
        model.addAttribute("deptList", deptList);     // 상위 부서 선택용 목록

        return "group/personnel/deptInsert"; // 등록 화면으로 이동
    }

    /**
     * 부서 등록 처리
     * 부서레벨 자동 계산 후 부서 등록
     */
    @PostMapping("/deptInsert")
    public String insertDepartment(DeptVO deptVO) {
        // 부서레벨 자동 설정
        if (deptVO.getUpperDepNo() == null || deptVO.getUpperDepNo() == 0) {
            deptVO.setDepartmentLevel(1); // 최상위 부서
        } else {
            deptVO.setDepartmentLevel(2); // 하위 부서 (원하면 +1 로직도 가능)
        }

        // 부서 등록
        deptService.registerDepartment(deptVO);

        // 부서 목록 화면으로 리다이렉트
        return "redirect:/deptMgmt";
    }

    

}