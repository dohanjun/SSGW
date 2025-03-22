package com.yedam.app.group.service;

import java.util.Date;

public interface LeaveHistService {
	
	// 사원 1명 연차 자동 부여
    void autoGrantAnnualLeave(int employeeNo, int suberNo, Date hireDate);

}
