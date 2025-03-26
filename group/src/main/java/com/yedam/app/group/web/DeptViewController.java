package com.yedam.app.group.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}