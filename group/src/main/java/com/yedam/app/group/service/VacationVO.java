package com.yedam.app.group.service;

import lombok.Data;

@Data
public class VacationVO {
	private Integer vacationTypeId; 			// 휴가유형
	private String vacationTypeName;			// 휴가이름
	private int vacationDate;					// 휴가일수
	private String requiredProofDocumentFile;	// 증빙서류 필요여부
	private int suberNo;						// 회사번호

}
