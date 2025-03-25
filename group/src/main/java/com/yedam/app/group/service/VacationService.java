package com.yedam.app.group.service;

import java.util.Date;
import java.util.List;

public interface VacationService {
	
    // 연차 자동 부여
    void autoGrantAnnualLeave(int employeeNo, int suberNo, Date hireDate, int draftNo);

   	void setZeroVacation(int employeeNo, int draftNo);
	
    // 휴가유형 등록
    void insertVacationType(VacationVO vo);
    
    // 휴가유형 목록 조회
    List<VacationVO> getAllVacationTypes(VacationVO vo);
    
    
    List<VacationVO> getVacationTypesByPaging(VacationVO vo);
    
    
    int countVacationTypes(VacationVO vo);

}
