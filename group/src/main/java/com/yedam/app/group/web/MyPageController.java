package com.yedam.app.group.web;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

@Controller
public class MyPageController {  

    private final EmpService empService;

    @Autowired
    public MyPageController(EmpService empService) {
        this.empService = empService;
    }

    // 1️⃣ 수정 화면 진입 (GET)
    @GetMapping("MyPageUpdate")
    public String empUpdate(Model model) {
        EmpVO loginUser = empService.getLoggedInUserInfo();
        if (loginUser == null) return "redirect:/login";

        EmpVO empVO = new EmpVO();
        empVO.setEmployeeNo(loginUser.getEmployeeNo());
        empVO.setSuberNo(loginUser.getSuberNo());

        EmpVO findVO = empService.findempInfo(empVO);
        if (findVO == null) return "redirect:/main";

        if (findVO.getProfileImageBLOB() != null) {
            String base64Image = Base64.getEncoder().encodeToString(findVO.getProfileImageBLOB());
            model.addAttribute("profileImageBase64", base64Image);
        }

        model.addAttribute("emp", findVO);
        return "group/MyPage/MyPageUpdate";
    }

    // 2️⃣ 정보 수정 처리 (POST) - AJAX 전용
    @PostMapping("MyPageUpdate")
    public ResponseEntity<?> updateEmpInfo(@ModelAttribute EmpVO empVO) throws IOException {
        // 로그인 정보 확인
        EmpVO loginUser = empService.getLoggedInUserInfo();
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("로그인이 필요합니다.");
        }

        // 사원 이름 유효성 검사
        if (empVO.getProfileImageFile() != null && !empVO.getProfileImageFile().isEmpty()) {
            empVO.setProfileImageBLOB(empVO.getProfileImageFile().getBytes());
        } else {
            EmpVO existingEmp = empService.findempInfo(empVO);
            if (existingEmp != null) {
                empVO.setProfileImageBLOB(existingEmp.getProfileImageBLOB());
            }
        }

        empVO.setSuberNo(loginUser.getSuberNo());
        empVO.setEmployeeNo(loginUser.getEmployeeNo());

        if ("N".equals(empVO.getResignationStatus())) {
            empVO.setExitDate(null);
        }

        empService.modifyEmpInfo(empVO);

        // AJAX 요청에서 리다이렉트 응답
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/MyPageInfo");
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 응답
    }

    // 3️⃣ 수정 완료 후 보기 화면 (GET)
    @GetMapping("MyPageInfo")
    public String empInfo(Model model) {
        EmpVO loginUser = empService.getLoggedInUserInfo();
        if (loginUser == null) return "redirect:/login";

        EmpVO empVO = new EmpVO();
        empVO.setEmployeeNo(loginUser.getEmployeeNo());

        EmpVO findVO = empService.findempInfo(empVO);

        if (findVO.getProfileImageBLOB() != null) {
            String base64Image = Base64.getEncoder().encodeToString(findVO.getProfileImageBLOB());
            model.addAttribute("profileImageBase64", base64Image);
        }

        model.addAttribute("emp", findVO);
        return "group/MyPage/MyPageInfo";
    }
}
