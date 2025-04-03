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

    // 생성자 기반 의존성 주입
    @Autowired
    public MyPageController(EmpService empService) {
        this.empService = empService;
    }

    // 1️⃣ [GET] 마이페이지 수정 폼 진입
    @GetMapping("MyPageUpdate")
    public String MyPageUpdate(Model model) {
        // 현재 로그인된 사용자 정보 조회
        EmpVO loginUser = empService.getLoggedInUserInfo();
        if (loginUser == null) return "redirect:/login"; // 로그인 안 되어있으면 로그인 페이지로 리다이렉트

        // 로그인된 사원의 사번과 상급자 번호로 조회 조건 생성
        EmpVO empVO = new EmpVO();
        empVO.setEmployeeNo(loginUser.getEmployeeNo());
        empVO.setSuberNo(loginUser.getSuberNo());

        // DB에서 사원 정보 조회
        EmpVO findVO = empService.findempInfo(empVO);
        if (findVO == null) return "redirect:/main"; // 정보 없을 경우 메인으로 이동

        // 프로필 이미지가 있으면 Base64로 인코딩해서 model에 추가
        if (findVO.getProfileImageBLOB() != null) {
            String base64Image = Base64.getEncoder().encodeToString(findVO.getProfileImageBLOB());
            model.addAttribute("profileImageBase64", base64Image);
        }

        // 수정 폼에 사원 정보 전달
        model.addAttribute("emp", findVO);
        return "group/Mypage/MyPageUpdate"; // 수정 페이지 뷰 반환
    }

    // 2️⃣ [POST] 사원 정보 수정 처리 (AJAX 요청 전용)
    @PostMapping("MyPageUpdate")
    public ResponseEntity<?> updateEmpInfo(@ModelAttribute EmpVO empVO) throws IOException {
        // 로그인된 사용자 정보 확인
        EmpVO loginUser = empService.getLoggedInUserInfo();
        if (loginUser == null) {
            // 인증 실패 시 401 상태 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("로그인이 필요합니다.");
        }

        // 프로필 이미지가 첨부되었으면 BLOB으로 변환
        if (empVO.getProfileImageFile() != null && !empVO.getProfileImageFile().isEmpty()) {
            empVO.setProfileImageBLOB(empVO.getProfileImageFile().getBytes());
        } else {
            // 이미지가 없으면 기존 이미지 유지
            EmpVO existingEmp = empService.findempInfo(empVO);
            if (existingEmp != null) {
                empVO.setProfileImageBLOB(existingEmp.getProfileImageBLOB());
            }
        }

        // 수정 시 로그인 사용자 정보로 세팅
        empVO.setSuberNo(loginUser.getSuberNo());
        empVO.setEmployeeNo(loginUser.getEmployeeNo());

        // 퇴사 상태가 아니면 퇴사일자 초기화
        if ("N".equals(empVO.getResignationStatus())) {
            empVO.setExitDate(null);
        }

        // DB 업데이트 수행
        empService.modifyEmpInfo(empVO);

        // 302 Redirect 응답 (AJAX에서 처리 가능)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/MyPageInfo"); // 수정 완료 후 보기 페이지로 이동
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 응답 반환
    }

    // 3️⃣ [GET] 수정 완료 후 상세 정보 보기 화면
    @GetMapping("MyPageInfo")
    public String MyPageInfo(Model model) {
        // 로그인된 사용자 정보 확인
        EmpVO loginUser = empService.getLoggedInUserInfo();
        if (loginUser == null) return "redirect:/login";

        // 조회용 VO 생성
        EmpVO empVO = new EmpVO();
        empVO.setEmployeeNo(loginUser.getEmployeeNo());

        // 사원 정보 조회
        EmpVO findVO = empService.findempInfo(empVO);

        // 프로필 이미지 처리
        if (findVO.getProfileImageBLOB() != null) {
            String base64Image = Base64.getEncoder().encodeToString(findVO.getProfileImageBLOB());
            model.addAttribute("profileImageBase64", base64Image);
        }

        // 사원 정보 모델에 담아 뷰로 전달
        model.addAttribute("emp", findVO);
        return "group/Mypage/MyPageInfo"; // 마이페이지 보기 화면
    }
}
