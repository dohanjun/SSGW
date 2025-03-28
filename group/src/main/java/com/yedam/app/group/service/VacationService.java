package com.yedam.app.group.service;

import java.util.Date;
import java.util.List;

public interface VacationService {
	
    // 연차 자동 부여
    void autoGrantAnnualLeave(int employeeNo, int suberNo, Date hireDate);

   	void setZeroVacation(int employeeNo);
	
    // 휴가유형 등록
    void insertVacationType(VacationVO vo);
    
    // 휴가유형 목록 조회
    List<VacationVO> getAllVacationTypes(VacationVO vo);
    
    
    List<VacationVO> getVacationTypesByPaging(VacationVO vo);
    
    
    int countVacationTypes(VacationVO vo);
    
    // 페이징된 휴가현황 목록 조회
    List<VacationVO> getVacationStatusPaging(VacationVO vo);
    
    // 전체 휴가현황 개수 조회 (페이징용)
    int countVacationStatus(VacationVO vo);
    
    // 연차 사용일/잔여일 업데이트 (사번, 회사번호, 연도 기준)
	void updateVacationUsage(int employeeNo, int suberNo, String year);
	
	//
	void findUsedVacation(VacationRequestVO vrVO);
}
