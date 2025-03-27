package com.yedam.app.group.web;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yedam.app.group.service.AttendanceManagementVO;
import com.yedam.app.group.service.AttendanceService;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/attendance") // 공통 URL prefix
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EmpService empService;

    // ✅ 현재 시간이 9시 이후인지 확인 → 지각 여부 판단 API
    @GetMapping("/check-late")
    public Map<String, String> checkLate() {
        LocalDateTime now = LocalDateTime.now(); // 현재 시간
        LocalDateTime nineAM = now.withHour(9).withMinute(0).withSecond(0).withNano(0); // 오늘 09:00:00

        // 현재 시간이 9시 이후면 지각(LATE), 아니면 정상출근(OK)
        return Map.of("status", now.isAfter(nineAM) ? "LATE" : "OK");
    }

    // ✅ 오늘 출근 기록이 이미 있는지 확인
    @GetMapping("/already-clock-in")
    public boolean alreadyClockedIn(HttpSession session) {
        EmpVO emp = empService.getLoggedInUserInfo(); // 현재 로그인 사원
        return attendanceService.hasClockedInToday(emp.getEmployeeNo()); // true: 이미 출근
    }

    // ✅ 오늘 퇴근 기록이 이미 있는지 확인
    @GetMapping("/already-clock-out")
    public boolean alreadyClockedOut(HttpSession session) {
        EmpVO emp = empService.getLoggedInUserInfo();
        return attendanceService.hasClockedOutToday(emp.getEmployeeNo()); // true: 이미 퇴근
    }

    // ✅ 출근 등록 API
    @PostMapping("/clock-in")
    public String clockIn(@RequestBody Map<String, String> payload, HttpSession session) {
        String reason = payload.get("reason"); // 지각 사유 (optional)
        EmpVO loggedInUser = empService.getLoggedInUserInfo(); // 로그인 정보

        if (loggedInUser == null) {
            return "{\"message\": \"로그인이 필요합니다.\"}";
        }

        // 이미 출근한 경우 중복 방지
        if (attendanceService.hasClockedInToday(loggedInUser.getEmployeeNo())) {
            return "{\"message\": \"⚠️ 이미 오늘 출근 기록이 있습니다.\"}";
        }

        // 출근 VO 구성
        AttendanceManagementVO vo = new AttendanceManagementVO();
        vo.setEmployeeNo(loggedInUser.getEmployeeNo());
        vo.setAttendanceDate(java.sql.Date.valueOf(LocalDate.now())); // 오늘 날짜
        vo.setClockInTime(Timestamp.valueOf(LocalDateTime.now())); // 현재 시각

        // 지각 여부 판단
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nineAM = now.withHour(9).withMinute(0).withSecond(0).withNano(0);

        if (now.isAfter(nineAM)) {
            vo.setWorkAttitudeType("지각");
            vo.setReason(reason); // 지각 사유 저장
        } else {
            vo.setWorkAttitudeType("정상출근");
            vo.setReason(null);
        }

        attendanceService.createClockIn(vo); // 출근 기록 저장
        return "{\"message\": \"✅ 출근 시간이 등록되었습니다.\"}";
    }

    // ✅ 퇴근 등록 API
    @PostMapping("/clock-out")
    public String clockOut(HttpSession session) {
        EmpVO loggedInUser = empService.getLoggedInUserInfo(); // 로그인된 사원

        if (loggedInUser == null) {
            return "{\"message\": \"로그인이 필요합니다.\"}";
        }

        // 이미 퇴근했는지 확인
        if (attendanceService.hasClockedOutToday(loggedInUser.getEmployeeNo())) {
            return "{\"message\": \"⚠️ 이미 오늘 퇴근 기록이 있습니다.\"}";
        }

        // 퇴근 VO 구성
        AttendanceManagementVO vo = new AttendanceManagementVO();
        vo.setEmployeeNo(loggedInUser.getEmployeeNo());
        vo.setAttendanceDate(java.sql.Date.valueOf(LocalDate.now()));
        vo.setClockOutTime(Timestamp.valueOf(LocalDateTime.now())); // 현재 시간 저장

        // 출근 기록이 없으면 퇴근 실패 처리
        int updated = attendanceService.modifyClockOut(vo);
        if (updated == 0) {
            return "{\"message\": \"⚠️ 출근 기록이 없어 퇴근 저장이 실패했습니다.\"}";
        }

        return "{\"message\": \"✅ 퇴근 시간이 등록되었습니다.\"}";
    }
}
