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

/**
 * 부서/직급 이동 이력 컨트롤러
 * - 페이징, 검색 기반 이력 조회
 * 
 * @author 김예찬
 * @since 2025-04-03
 */

@Controller
@RequiredArgsConstructor
public class DeptHistController {
	
    private final DeptHistService deptHistService;
    private final EmpService empService;
    
    
    /**
     * 부서이동/직급이동 이력 목록 조회 화면
     *
     * @param searchVO 검색 및 페이징 파라미터
     * @param model    화면 전달용 데이터
     * @return         deptHist.html 화면 반환
     */
    
    
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
