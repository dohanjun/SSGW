package com.yedam.app.group.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.VacationMapper;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.VacationRequestVO;
import com.yedam.app.group.service.VacationService;
import com.yedam.app.group.service.VacationVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VacationServiceImpl implements VacationService {

    private final VacationMapper vacationMapper;
    private final EmpService empService;

    // 연차 자동 부여 메서드
    @Override
    public void autoGrantAnnualLeave(int employeeNo, int suberNo, Date hireDate) {
        // 현재 날짜와 입사일(LocalDate) 계산
        LocalDate currentDate = LocalDate.now();
        LocalDate hireLocalDate = hireDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 입사일 기준으로 경과된 개월 수와 년 수 계산
        long months = ChronoUnit.MONTHS.between(hireLocalDate, currentDate);
        long years = ChronoUnit.YEARS.between(hireLocalDate, currentDate);

        // 부여할 연차 계산
        // 1년 이상 근속 시: 15일 + 추가일수, 1년 미만은 월 단위로 최대 11일
        int grantDays = (years >= 1) ? (15 + (int)((years - 1) / 2)) : (int) Math.min(months, 11);

        // 연도 문자열 (예: "2025")
        String yearStr = String.valueOf(currentDate.getYear());

        // 이미 해당 연도에 연차가 등록되어 있는지 확인
        int count = vacationMapper.existsLeaveHistory(employeeNo, yearStr);

        // 연차정보 객체 생성
        VacationVO vo = new VacationVO();
        vo.setEmployeeNo(employeeNo);
        vo.setGrantedVacation(grantDays);       // 부여 연차
        vo.setRemainingVacation(grantDays);     // 초기 잔여 연차는 부여 연차와 동일
        vo.setYear(yearStr);                    // 연도 문자열로 설정

        if (count > 0) {
            // 이미 등록된 연차가 있으면 → 사용된 연차를 고려해 잔여일 다시 계산
            VacationVO existing = vacationMapper.getLeaveHistory(employeeNo, yearStr);
            int used = existing.getUsedVacation();

            vo.setRemainingVacation(grantDays - used); // ❗ 사용일 차감한 잔여일
            vacationMapper.updateLeaveHistory(vo);
        } else {
            // 처음 생성일 경우는 granted와 remaining 동일
            vo.setRemainingVacation(grantDays);
            vo.setLeaveHistoryId(vacationMapper.getNextLeaveHistoryId());
            vacationMapper.insertLeaveHistory(vo);
        }
    }

    // 퇴사자 등 연차를 0으로 리셋하는 메서드
    @Override
    public void setZeroVacation(int employeeNo) {
        // 현재 연도 문자열
        String yearStr = String.valueOf(LocalDate.now().getYear());

        // 연차 정보 객체 생성
        VacationVO vo = new VacationVO();
        vo.setEmployeeNo(employeeNo);
        vo.setYear(yearStr);             // 연도 설정
        vo.setGrantedVacation(0);        // 부여 연차 0
        vo.setRemainingVacation(0);      // 잔여 연차 0

        // 연차 내역 업데이트
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
    
    
    // 페이징된 휴가현황 목록 조회
    @Override
    public List<VacationVO> getVacationStatusPaging(VacationVO vo) {
        int offset = (vo.getPage() - 1) * vo.getSize();
        vo.setOffset(offset);
        return vacationMapper.selectVacationStatusPaging(vo);
    }

    // 전체 휴가현황 개수 조회 (페이징용)
    @Override
    public int countVacationStatus(VacationVO vo) {
        return vacationMapper.countVacationStatus(vo);
    }
    
    // 연차 사용일/잔여일 업데이트 로직
    @Override
    public void updateVacationUsage(int employeeNo, int suberNo, String year) {
        // 로그인한 사용자 정보 가져오기
        EmpVO loggedInUser = empService.getLoggedInUserInfo();

        // VO에 필요한 값 설정
        VacationVO vo = new VacationVO();
        vo.setEmployeeNo(loggedInUser.getEmployeeNo());
        vo.setSuberNo(loggedInUser.getSuberNo());
        vo.setYear(String.valueOf(LocalDate.now().getYear()));

        // XML Mapper 호출
        vacationMapper.updateVacationUsage(vo);
    }

	@Override
	public void findUsedVacation(VacationRequestVO vrVO) {
		VacationRequestVO request = vacationMapper.selectVacationRequest(vrVO);

        if (request != null) {
            VacationVO vacationVO = new VacationVO();
            vacationVO.setEmployeeNo(request.getEmployeeNo());
            vacationVO.setUsedVacation(request.getUsedVacation());

            vacationMapper.updateLeaveHistoryRequest(vacationVO);
        }
	}
	
    // 휴가유형 삭제
    @Override
    public void deleteVacationType(int vacationTypeId) {
        vacationMapper.deleteVacationType(vacationTypeId);
    }

}