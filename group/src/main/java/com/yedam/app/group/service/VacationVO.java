package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class VacationVO {
	private Integer vacationTypeId; 				// 휴가유형
	private String vacationTypeName;				// 휴가이름
	private Integer vacationDate;					// 휴가일수
	private String requiredProofDocumentFile;		// 증빙서류 필요여부
	private Integer suberNo;						// 회사번호
	
	private Integer grantedVacation;		// 부여된 휴가일
	private Integer userVacation;			// 사용한 휴가일
	private Integer remainingVacation;		// 휴가 잔여일
	private Integer emplkoyeeNo;			// 사원번호
	private Integer leaveHistoryId;			// 휴가내역번호
	private Date Yeat;						// 연도
	private Integer draftNo;				// 기안문서번호

}
