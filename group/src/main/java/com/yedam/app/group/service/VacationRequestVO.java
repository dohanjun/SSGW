package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class VacationRequestVO {
	private Integer draftNo;        // 기안문서번호
	private int usedVacation;      // 휴가 사용일수
	private int employeeNo;         // 사원번호
	private Date startVacationDate; // 휴가 시작일
	private Date endVacationDate;   // 휴가 종료일
	private int vacationTypeId;     // 휴가유형ID
}
