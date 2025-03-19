package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class ScheduleVO {
	private Integer scheduleId;       // 일정id
	private String scheduleTitle;     // 일정제목
	private String scheduleContent;   // 일정내용
	private Date scheduleStart;       // 시작일시
	private Date scheduleEnd;         // 종료일시
	private int employeeNo;           // 사원번호
	private int departmentNo;         // 부서번호
	private String scheduleDivision;  // 일정구분(개인, 부서, 회사)
	private String repeatCycle;       // 반복주기
	private int suberNo;              // 회사번호
	
	private int scheduleSharingNo;    // 일정공유번호
	
}
