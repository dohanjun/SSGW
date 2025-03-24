package com.yedam.app.group.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.VacationMapper;

@Service
public class VacationScheduler {

    private final VacationService vacationService;
    private final VacationMapper vacationMapper;

    // 생성자 주입
    public VacationScheduler(VacationService vacationService, VacationMapper vacationMapper) {
        this.vacationService = vacationService;
        this.vacationMapper = vacationMapper;
    }

    // 5초마다 연차 자동 부여
    @Scheduled(cron = "*/5 * * * * ?")  // 5초마다 실행
//    @Scheduled(cron = "0 0 * * * ?")  // 5초마다 실행
    public void autoGrantAnnualLeaveForAllEmployees() {
        // 모든 사원 목록을 가져오는 로직 (예: DB에서 사원 목록을 가져옴)
        List<VacationVO> allEmployees = vacationMapper.getAllEmployeesWithHireDate(1);  // 예시로 1번 회사 번호 사용

        // 모든 사원에 대해 연차 부여
        for (VacationVO employee : allEmployees) {
            // 기안문서번호를 가져오는 로직 추가 (예시로 draftMapper를 사용하여 자동 생성)
            int draftNo = vacationMapper.getNextDraftNo();  // 기안문서번호 가져오기

            // 연차 자동 부여 메서드 호출
            vacationService.autoGrantAnnualLeave(employee.getEmployeeNo(), employee.getSuberNo(), employee.getHireDate(), draftNo);
        }
    }
}
