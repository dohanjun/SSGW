package com.yedam.app.group.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

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

        int grantDays = (years >= 1) ? (15 + (int)((years - 1) / 2)) : (int) Math.min(months, 11);

        String yearStr = String.valueOf(currentDate.getYear());
        java.sql.Date yearDate = java.sql.Date.valueOf(currentDate.withMonth(1).withDayOfMonth(1));

        int count = vacationMapper.existsLeaveHistory(employeeNo, yearStr);

        VacationVO vo = new VacationVO();
        vo.setEmployeeNo(employeeNo);
        vo.setGrantedVacation(grantDays);
        vo.setRemainingVacation(grantDays);
        vo.setYear(yearDate);
        vo.setDraftNo(draftNo);

        if (count > 0) {
            vacationMapper.updateLeaveHistory(vo);
        } else {
            vo.setLeaveHistoryId(vacationMapper.getNextLeaveHistoryId());
            vacationMapper.insertLeaveHistory(vo);
        }
    }

    @Override
    public void setZeroVacation(int employeeNo, int draftNo) {
        java.sql.Date yearDate = java.sql.Date.valueOf(LocalDate.now().withMonth(1).withDayOfMonth(1));

        VacationVO vo = new VacationVO();
        vo.setEmployeeNo(employeeNo);
        vo.setDraftNo(draftNo);
        vo.setYear(yearDate);
        vo.setGrantedVacation(0);
        vo.setRemainingVacation(0);

        vacationMapper.updateLeaveHistory(vo);
    }
    
    // 휴가유형 등록
    @Override
    public void insertVacationType(VacationVO vo) {
        int nextId = vacationMapper.getNextVacationTypeId();
        vo.setVacationTypeId(nextId);

        vacationMapper.insertVacationType(vo);
    }
    
    
    // 휴가유형 전체 조회
    @Override
    public List<VacationVO> getAllVacationTypes(VacationVO vo) {
        return vacationMapper.selectVacationTypeList(vo.getSuberNo());
    }

    @Override
    public List<VacationVO> getVacationTypesByPaging(VacationVO vo) {
        return vacationMapper.selectVacationTypePaging(vo);
    }

    @Override
    public int countVacationTypes(VacationVO vo) {
        return vacationMapper.countVacationType(vo);
    }

}