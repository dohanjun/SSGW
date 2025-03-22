package com.yedam.app.group.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.VacationVO;

@Mapper
public interface VacationMapper {
	
	// 연차 기록 존재 여부 확인 (사원 + 연도)
    public boolean findAllLeaveHistory(@Param("employeeNo") int employeeNo, @Param("year") int year);

    // 연차 자동 부여 INSERT
    public int createLeaveHistory(VacationVO leaveHistory);

    // 연차유형(예: 연차) 조회 (회사번호 기준)
    public VacationVO findAllVacationType(@Param("suberNo") int suberNo);

}
