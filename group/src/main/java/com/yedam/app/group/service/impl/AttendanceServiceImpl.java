package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yedam.app.group.mapper.AttendanceMapper;
import com.yedam.app.group.service.AttendanceManagementVO;
import com.yedam.app.group.service.AttendanceService;
import com.yedam.app.group.service.AttendanceSummaryDTO;
import com.yedam.app.group.service.OvertimeVO;

@Service
@Transactional // 모든 메서드에서 트랜잭션 처리됨 (자동 rollback 처리 포함)
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceMapper attendanceMapper;

    // ✅ 생성자 기반 의존성 주입 (테스트/확장에 유리)
    @Autowired
    public AttendanceServiceImpl(AttendanceMapper attendanceMapper) {
        this.attendanceMapper = attendanceMapper;
    }

    // ✅ 전체 출결 데이터 조회
    // 관리자용 전체 리스트 페이지에서 사용
    @Override
    public List<AttendanceManagementVO> selectAll() {
        return attendanceMapper.selectAll();
    }

    // ✅ 특정 사원의 출결 목록 조회
    // 초과근무 시간이 존재할 경우 분 ➝ 시간으로 변환하여 VO에 설정
    @Override
    public List<AttendanceManagementVO> selectInfo(Integer employeeNo) {
        List<AttendanceManagementVO> list = attendanceMapper.selectInfo(employeeNo);

        for (AttendanceManagementVO vo : list) {
            if (vo.getTotalOvertimeTime() != null) {
                vo.setOvertimeHours(vo.getTotalOvertimeTime() / 60.0); // 예: 90분 → 1.5시간
            } else {
                vo.setOvertimeHours(0.0); // null 방지
            }
        }

        return list;
    }

    // ✅ 특정 사원의 누적 초과근무 시간 조회 (분 단위)
    // 개인 마이페이지에서 통계로 활용
    @Override
    public int getTotalOvertimeMinutes(Integer employeeNo) {
        Integer totalMinutes = attendanceMapper.calculateTotalOvertime(employeeNo);
        return (totalMinutes != null) ? totalMinutes : 0; // null 방지
    }

    // ✅ 특정 사원의 출결 요약 정보 조회
    // 가장 최근 출근/퇴근 정보, 총 근무 시간 등 포함
    @Override
    public AttendanceManagementVO getAttendanceSummary(Integer employeeNo) {
        return attendanceMapper.getAttendanceSummary(employeeNo);
    }

    // ✅ 출근 등록 (Clock In)
    // 출근 버튼 클릭 시 실행됨
    @Override
    public Integer createClockIn(AttendanceManagementVO vo) {
        return attendanceMapper.insertClockIn(vo);
    }

    // ✅ 퇴근 등록 및 초과근무 자동 삽입
    // 퇴근 처리와 동시에 초과근무 계산 및 insert
    @Override
    public Integer modifyClockOut(AttendanceManagementVO vo) {
        // ✅ 출근 기록 확인
        AttendanceManagementVO latest = attendanceMapper.getAttendanceSummary(vo.getEmployeeNo());

        if (latest == null || latest.getClockInTime() == null) {
            return 0; // 출근 기록 없으면 퇴근 불가
        }

        // ✅ 근무 시간 계산 (초 단위 → 시간 단위 변환)
        long milliseconds = vo.getClockOutTime().getTime() - latest.getClockInTime().getTime();
        int workingHours = (int) (milliseconds / (1000 * 60 * 60));

        // ✅ VO에 세팅
        vo.setTotalWorkingHours(workingHours);

        // ✅ 퇴근 + 근무시간 업데이트 쿼리 호출
        Integer result = attendanceMapper.updateClockOutAndWorkingHours(vo);

        // ✅ 초과근무 자동 등록 (9시간 초과 시)
        if (workingHours > 9) {
            OvertimeVO overtime = new OvertimeVO();
            overtime.setWorkAttitudeId(latest.getWorkAttitudeId());
            overtime.setOvertimeTime((workingHours - 9) * 60); // 분 단위
            overtime.setOvertimeDate(latest.getAttendanceDate());
            overtime.setOvertimeType("연장근무");
            overtime.setDraftDocumentNumber(null);

            attendanceMapper.insertOvertime(overtime);
        }

        return result;
    }


    // ✅ 오늘 출근 여부 확인
    // 출근 버튼 중복 클릭 방지, 상태 판단에 사용
    @Override
    public boolean hasClockedInToday(int employeeNo) {
        return attendanceMapper.countTodayClockIn(employeeNo) > 0;
    }

    // ✅ 오늘 퇴근 여부 확인
    // 퇴근 버튼 중복 클릭 방지
    @Override
    public boolean hasClockedOutToday(int employeeNo) {
        return attendanceMapper.countTodayClockOut(employeeNo) > 0;
    }

    // ✅ 부서별 출결 요약 통계 조회
    // Bar 차트, Pie 차트 등 관리자 시각화용 데이터
    @Override
    public List<AttendanceSummaryDTO> getDepartmentAttendanceSummary(int departmentNo) {
        return attendanceMapper.getDepartmentAttendanceSummary(departmentNo);
    }

    // ✅ 부서원 전체 출결 리스트 조회
    // 관리자 페이지에서 테이블로 활용
    @Override
    public List<AttendanceManagementVO> selectDeptAttendance(int departmentNo) {
        return attendanceMapper.selectDeptAttendance(departmentNo);
    }

    // ✅ 특정 출결에 연결된 초과근무 상세 정보 조회
    // 모달 팝업 등에서 사용
    @Override
    public OvertimeVO getOvertimeByWorkAttitudeId(int workAttitudeId) {
        return attendanceMapper.getOvertimeByWorkAttitudeId(workAttitudeId);
    }

    // ✅ 오늘 특정 부서에 소속된 사원 중 출근한 인원 조회
    // 실시간 현황판 또는 관리자 대시보드에서 사용
    @Override
    public List<AttendanceManagementVO> getTodayAttendanceByDept(int deptNo) {
        return attendanceMapper.selectTodayAttendanceByDept(deptNo);
    }
    
    // ✅ (추가) 부서원 상세 출결 리스트 조회
    @Override
    public List<AttendanceManagementVO> getDepartmentAttendanceDetail(Integer departmentNo) {
        return attendanceMapper.selectDepartmentAttendanceDetail(departmentNo);
    }
    
}
