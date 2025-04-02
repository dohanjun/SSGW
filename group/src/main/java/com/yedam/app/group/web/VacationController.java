package com.yedam.app.group.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.VacationService;
import com.yedam.app.group.service.VacationVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor // 생성자 자동 생성 (vacationService, empService 자동 주입)
public class VacationController {

    private final VacationService vacationService;
    private final EmpService empService;

    // 휴가유형 등록 폼 이동
    @GetMapping("/vacaInsert")
    public String vacaInsertForm(Model model) {
        EmpVO loggedInUser = empService.getLoggedInUserInfo(); // 로그인한 사용자 정보 가져오기
        Integer suberNo = loggedInUser.getSuberNo(); // 회사번호

        VacationVO vacationVO = new VacationVO();
        vacationVO.setSuberNo(suberNo); // VO에 회사번호 미리 설정

        model.addAttribute("vacationVO", vacationVO); // form과 바인딩할 객체 설정
        return "group/personnel/vacaInsert"; // HTML 경로
    }

    // 등록 처리
    @PostMapping("/vacaInsert")
    public String vacaInsert(@ModelAttribute VacationVO vacationVO) {
        // 체크박스 미선택 시 null → "N" 처리
        if (vacationVO.getRequiredProofDocumentFile() == null) {
            vacationVO.setRequiredProofDocumentFile("N");
        }

        vacationService.insertVacationType(vacationVO); // DB insert 실행
        return "redirect:/vacationList"; // 성공 시 목록 페이지로 이동
    }
    
    // 휴가유형 전체 리스트
    @GetMapping("/vacationList") 
    public String vacationList(@ModelAttribute VacationVO vo, Model model) {
        EmpVO loginUser = empService.getLoggedInUserInfo();
        vo.setSuberNo(loginUser.getSuberNo());

        //  기본값 보장
        if (vo.getPage() <= 0) vo.setPage(1);
        if (vo.getSize() <= 0) vo.setSize(10);

        vo.setOffset((vo.getPage() - 1) * vo.getSize());

        int total = vacationService.countVacationTypes(vo);
        int totalPages = (int) Math.ceil((double) total / vo.getSize());
        List<VacationVO> list = vacationService.getVacationTypesByPaging(vo);

        model.addAttribute("vacations", list);
        model.addAttribute("currentPage", vo.getPage());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("vacationVO", vo); //  중요 (페이징 유지)
        
        return "group/personnel/vacationList";
    }
    
    // 휴가 현황 조회
    @GetMapping("/vacaList")
    public String vacaList(VacationVO vo, Model model) {
        EmpVO loginUser = empService.getLoggedInUserInfo();
        vo.setSuberNo(loginUser.getSuberNo());

        if (vo.getPage() <= 0) vo.setPage(1);
        if (vo.getSize() <= 0) vo.setSize(10);
        vo.setOffset((vo.getPage() - 1) * vo.getSize());

        List<VacationVO> list = vacationService.getVacationStatusPaging(vo);
        int total = vacationService.countVacationStatus(vo);
        int totalPages = (int) Math.ceil((double) total / vo.getSize());

        model.addAttribute("vacations", list);
        model.addAttribute("currentPage", vo.getPage());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("vacationVO", vo);

        return "group/personnel/vacaList";
    }
    
    // 휴가유형 삭제
    
    @PostMapping("/vacaDelete")
    @ResponseBody
    public String vacaDelete(@RequestParam("vacationTypeId") int vacationTypeId) {
        try {
            vacationService.deleteVacationType(vacationTypeId);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }
}
