package com.yedam.app.group.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.VacationMapper;
import com.yedam.app.group.service.VacationService;
import com.yedam.app.group.service.VacationVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VacationServiceImpl implements VacationService {

    private final VacationMapper vacationMapper;

    @Override
    public void autoGrantAnnualLeave(int employeeNo, int suberNo, Date hireDate, int draftNo) {
        LocalDate currentDate = LocalDate.now();
        LocalDate hireLocalDate = hireDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long months = ChronoUnit.MONTHS.between(hireLocalDate, currentDate);
        long years = ChronoUnit.YEARS.between(hireLocalDate, currentDate);

        int grantDays = (years >= 1) ? (int)(15 + ((years - 1) / 2)) : (int)Math.min(months, 11);

        String yearStr = String.valueOf(currentDate.getYear());
        java.sql.Date yearDate = java.sql.Date.valueOf(currentDate.withMonth(1).withDayOfMonth(1));

        // 중복 여부 확인
        int count = vacationMapper.existsLeaveHistory(employeeNo, yearStr);

        VacationVO vo = new VacationVO();
        vo.setEmployeeNo(employeeNo);
        vo.setGrantedVacation(grantDays);
        vo.setRemainingVacation(grantDays);
        vo.setYear(yearDate);
        vo.setDraftNo(draftNo);

        if (count > 0) {
            // 퇴사자면 0으로 업데이트
            // 나중에 RESIGNATION_STATUS를 VO에도 넣는 게 좋아
            // 임시로 VO가 없다고 치면 → 쿼리 수정하거나 조건을 밖에서 처리
            vacationMapper.updateLeaveHistory(vo);
        } else {
            vacationMapper.insertLeaveHistory(vo);
        }
    }
}