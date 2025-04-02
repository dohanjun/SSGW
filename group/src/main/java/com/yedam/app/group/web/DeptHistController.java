package com.yedam.app.group.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.yedam.app.group.service.DeptHistService;
import com.yedam.app.group.service.DeptHistVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DeptHistController {
	
    private final DeptHistService deptHistService;
    private final EmpService empService;
    
    // 이동이력 리스트 화면
    @GetMapping("deptHist")
    public String deptTransferList(DeptHistVO searchVO, Model model) {
        EmpVO loginUser = empService.getLoggedInUserInfo();
        int suberNo = loginUser.getSuberNo();
        searchVO.setSuberNo(suberNo);

        // 페이징된 리스트
        List<DeptHistVO> list = deptHistService.getDeptHistPaging(searchVO);
        int totalRecords = deptHistService.countDeptHist(searchVO);
        int totalPages = (int) Math.ceil((double) totalRecords / searchVO.getSize());

        model.addAttribute("deptHistList", list);
        model.addAttribute("currentPage", searchVO.getPage());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("searchVO", searchVO);

        return "group/personnel/deptHist";
    }

}
