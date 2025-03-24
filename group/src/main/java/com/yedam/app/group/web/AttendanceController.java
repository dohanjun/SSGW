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

    // ✅ 출근 여부 체크 (하루 한 번 제한)
    @GetMapping("/already-clock-in")
    public boolean alreadyClockedIn(HttpSession session) {
        EmpVO emp = empService.getLoggedInUserInfo();
        return attendanceService.hasClockedInToday(emp.getEmployeeNo());
    }

    // ✅ 퇴근 여부 체크 (하루 한 번 제한)
    @GetMapping("/already-clock-out")
    public boolean alreadyClockedOut(HttpSession session) {
        EmpVO emp = empService.getLoggedInUserInfo();
        return attendanceService.hasClockedOutToday(emp.getEmployeeNo());
    }

    // ✅ 출근 기록 저장 (지각이면 reason 포함)
    @PostMapping("/clock-in")
    public String clockIn(@RequestBody Map<String, String> payload, HttpSession session) {
        String reason = payload.get("reason");
        EmpVO loggedInUser = empService.getLoggedInUserInfo();
        if (loggedInUser == null) {
            return "{\"message\": \"로그인이 필요합니다.\"}";
        }

        if (attendanceService.hasClockedInToday(loggedInUser.getEmployeeNo())) {
            return "{\"message\": \"⚠️ 이미 오늘 출근 기록이 있습니다.\"}";
        }

        AttendanceManagementVO vo = new AttendanceManagementVO();
        vo.setEmployeeNo(loggedInUser.getEmployeeNo());
        vo.setAttendanceDate(java.sql.Date.valueOf(LocalDate.now()));

        LocalDateTime now = LocalDateTime.now();
        vo.setClockInTime(Timestamp.valueOf(now));

        LocalDateTime nineAM = now.withHour(9).withMinute(0).withSecond(0).withNano(0);
        if (now.isAfter(nineAM)) {
            vo.setWorkAttitudeType("지각");
            vo.setReason(reason);
        } else {
            vo.setWorkAttitudeType("정상출근");
            vo.setReason(null);
        }

        attendanceService.createClockIn(vo);
        return "{\"message\": \"✅ 출근 시간이 등록되었습니다.\"}";
    }

    // ✅ 퇴근
    @PostMapping("/clock-out")
    public String clockOut(HttpSession session) {
        EmpVO loggedInUser = empService.getLoggedInUserInfo();
        if (loggedInUser == null) {
            return "{\"message\": \"로그인이 필요합니다.\"}";
        }

        if (attendanceService.hasClockedOutToday(loggedInUser.getEmployeeNo())) {
            return "{\"message\": \"⚠️ 이미 오늘 퇴근 기록이 있습니다.\"}";
        }

        AttendanceManagementVO vo = new AttendanceManagementVO();
        vo.setEmployeeNo(loggedInUser.getEmployeeNo());
        vo.setAttendanceDate(java.sql.Date.valueOf(LocalDate.now()));
        vo.setClockOutTime(Timestamp.valueOf(LocalDateTime.now()));

        int updated = attendanceService.modifyClockOut(vo);
        if (updated == 0) {
            return "{\"message\": \"⚠️ 출근 기록이 없어 퇴근 저장이 실패했습니다.\"}";
        }

        return "{\"message\": \"✅ 퇴근 시간이 등록되었습니다.\"}";
    }
}