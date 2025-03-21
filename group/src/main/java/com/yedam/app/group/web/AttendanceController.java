package com.yedam.app.group.web;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yedam.app.group.service.AttendanceService;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // ✅ 출근 요청 API
    @PostMapping("/clock-in")
    public ResponseEntity<?> clockIn(@RequestBody Map<String, Integer> request) {
        System.out.println("✅ [DEBUG] 출근 요청 도착: " + request);

        if (!request.containsKey("employeeNo") || request.get("employeeNo") == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "사원번호가 필요합니다."));
        }

        Integer employeeNo = request.get("employeeNo");
        System.out.println("✅ [DEBUG] 사원번호: " + employeeNo);

        attendanceService.clockIn(employeeNo);
        return ResponseEntity.ok(Collections.singletonMap("message", "✅ 출근 시간이 등록되었습니다."));
    }

    // ✅ 퇴근 요청 API
    @PostMapping("/clock-out")
    public ResponseEntity<?> clockOut(@RequestBody Map<String, Integer> request) {
        System.out.println("✅ [DEBUG] 퇴근 요청 도착: " + request);

        if (!request.containsKey("employeeNo") || request.get("employeeNo") == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "사원번호가 필요합니다."));
        }

        Integer employeeNo = request.get("employeeNo");
        System.out.println("✅ [DEBUG] 사원번호: " + employeeNo);

        attendanceService.clockOut(employeeNo);
        return ResponseEntity.ok(Collections.singletonMap("message", "✅ 퇴근 시간이 등록되었습니다."));
    }
}
