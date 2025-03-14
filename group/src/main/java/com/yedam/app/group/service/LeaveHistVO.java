package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class LeaveHistVO {
	private int grantedVacation;		// 부여된 휴가일
	private int userVacation;			// 사용한 휴가일
	private int remainingVacation;		// 휴가 잔여일
	private int emplkoyeeNo;			// 사원번호
	private Integer leaveHistoryId;		// 휴가내역번호
	private Date Yeat;					// 연도
	private int draftNo;				// 기안문서번호

}
