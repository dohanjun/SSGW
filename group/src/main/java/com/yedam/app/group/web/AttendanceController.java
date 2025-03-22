package com.yedam.app.group.web;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yedam.app.group.service.AttendanceManagementVO;
import com.yedam.app.group.service.AttendanceService;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private EmpService empService;
    
    @GetMapping("/check-late")
    public Map<String, String> checkLate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nineAM = now.withHour(9).withMinute(0).withSecond(0).withNano(0);

        return Map.of("status", now.isAfter(nineAM) ? "LATE" : "OK");
    }
    // ✅ 출근 기록 저장 (지각이면 reason 포함)
    @PostMapping("/clock-in")
    public String clockIn(@RequestBody Map<String, String> payload, HttpSession session) {
    	String reason = payload.get("reason");
        EmpVO loggedInUser = empService.getLoggedInUserInfo(); // 로그인 정보
        if (loggedInUser == null) {
            return "{\"message\": \"로그인이 필요합니다.\"}";
        }

        AttendanceManagementVO vo = new AttendanceManagementVO();
        vo.setEmployeeNo(loggedInUser.getEmployeeNo());
        vo.setAttendanceDate(java.sql.Date.valueOf(LocalDate.now()));

        LocalDateTime now = LocalDateTime.now();
        vo.setClockInTime(Timestamp.valueOf(now));

        LocalDateTime nineAM = now.withHour(9).withMinute(0).withSecond(0).withNano(0);
        if (now.isAfter(nineAM)) {
            vo.setWorkAttitudeType("지각");
            vo.setReason(payload.get("reason")); // ✅ 지각 사유 저장
        } else {
            vo.setWorkAttitudeType("정상출근");
            vo.setReason(null); // 정상출근이면 사유 없음
        }

        attendanceService.createClockIn(vo); // DB 저장
        return "{\"message\": \"✅ 출근 시간이 등록되었습니다.\"}";
    }
    // ✅ 퇴근
    @PostMapping("/clock-out")
    public String clockOut(HttpSession session) {
    	EmpVO loggedInUser = empService.getLoggedInUserInfo();
        if (loggedInUser == null) {
            return "{\"message\": \"로그인이 필요합니다.\"}";
        }

        AttendanceManagementVO vo = new AttendanceManagementVO();
        vo.setEmployeeNo(loggedInUser.getEmployeeNo());
        vo.setAttendanceDate(java.sql.Date.valueOf(LocalDate.now())); // 오늘 날짜
        vo.setClockOutTime(Timestamp.valueOf(LocalDateTime.now()));   // 현재 시간

        int updated = attendanceService.modifyClockOut(vo);
        if (updated == 0) {
            return "{\"message\": \"⚠️ 출근 기록이 없어 퇴근 저장이 실패했습니다.\"}";
        }

        return "{\"message\": \"✅ 퇴근 시간이 등록되었습니다.\"}";
    }
}
