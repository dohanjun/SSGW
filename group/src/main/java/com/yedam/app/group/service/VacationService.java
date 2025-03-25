package com.yedam.app.group.service;

import java.util.Date;

public interface VacationService {
	
    // 연차 자동 부여
    void autoGrantAnnualLeave(int employeeNo, int suberNo, Date hireDate, int draftNo);
    
}
