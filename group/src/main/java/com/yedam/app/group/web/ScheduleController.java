package com.yedam.app.group.web;


import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.ScheduleService;
import com.yedam.app.group.service.ScheduleVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
	
	private final ScheduleService scheduleService;
	private final EmpService empService;
	
	// 일정등록
	@PostMapping("/save")
    public ResponseEntity<?> saveSchedule(@RequestBody ScheduleVO scheduleVO) {
		
		// 로그인한 사용자 정보 가져오기
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		scheduleVO.setEmployeeNo(loggedInUser.getEmployeeNo());  //  로그인한 사용자 정보 설정
		scheduleVO.setSuberNo(loggedInUser.getSuberNo()); // 로그인한 사용자 회사번호
		scheduleVO.setDepartmentNo(loggedInUser.getDepartmentNo()); // 로그인한 사용자 부서번호
		
		int result = scheduleService.saveSchedule(scheduleVO);

        if (result > 0) {
            return ResponseEntity.ok().body(Map.of("success", 1, "message", "일정이 저장되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("success", 0, "message", "일정 저장 실패"));
        }
    }
	
	// 일정 조회
	@GetMapping("/list")
    public ResponseEntity<List<ScheduleVO>> getScheduleList() {
		
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		
		ScheduleVO scheduleVO = new ScheduleVO();
		
		scheduleVO.setEmployeeNo(loggedInUser.getEmployeeNo());  //  로그인한 사용자 정보 설정
		scheduleVO.setSuberNo(loggedInUser.getSuberNo()); // 로그인한 사용자 회사번호
		
		
		
        List<ScheduleVO> scheduleList = scheduleService.getAllSchedules();
        return ResponseEntity.ok(scheduleList);
    }
	
}
