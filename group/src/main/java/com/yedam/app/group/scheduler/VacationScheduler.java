package com.yedam.app.group.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.VacationMapper;
import com.yedam.app.group.service.VacationService;
import com.yedam.app.group.service.VacationVO;

@Service
public class VacationScheduler {

    private final VacationService vacationService;
    private final VacationMapper vacationMapper;

    public VacationScheduler(VacationService vacationService, VacationMapper vacationMapper) {
        this.vacationService = vacationService;
        this.vacationMapper = vacationMapper;
    }

    //  자동 연차 부여 스케줄링 - 1시간마다 실행
    @Scheduled(cron = "0 0 * * * ?")
//    @Scheduled(cron = "*/5 * * * * ?") // 테스트용: 5초마다
    public void autoGrantAnnualLeaveForAllEmployees() {

        //  1. 전체 회사 번호 목록 조회 (구독형 처리)
        List<Integer> suberNos = vacationMapper.getAllSuberNos();

        //  2. 각 회사(suberNo)마다 사원 조회 및 연차 부여
        for (Integer suberNo : suberNos) {
            List<VacationVO> allEmployees = vacationMapper.getAllEmployeesWithHireDateAndStatus(suberNo);

            for (VacationVO employee : allEmployees) {
                int draftNo = vacationMapper.getNextDraftNo();

                if ("Y".equals(employee.getResignationStatus())) {
                    //  퇴사자: 연차를 0으로 업데이트
                    vacationService.setZeroVacation(employee.getEmployeeNo());
                } else {
                    //  재직자: 자동 연차 계산 및 부여
                    vacationService.autoGrantAnnualLeave(
                        employee.getEmployeeNo(),
                        employee.getSuberNo(),
                        employee.getHireDate()

                    );
                }
            }
        }
    }
}